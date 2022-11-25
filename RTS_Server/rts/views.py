import json
from django.shortcuts import render
from asgiref.sync import async_to_sync
from rts.models import Somedata, Car, CarSession
from rts.serializers import SomedataSerializer, CarSerializer, CarSessionSerializer
from django.http import HttpResponse, JsonResponse, HttpRequest
from django.views.decorators.csrf import csrf_exempt
from rest_framework.parsers import JSONParser
from rts.constants import COMMAND_LIST, GROUP_CLOSING_COMMAND
from channels.layers import get_channel_layer
from rest_framework.decorators import api_view
from rest_framework import generics
from rest_framework.response import Response
from rest_framework.views import APIView
import rts.custom_schemas as custom_schema

#TODO cleanup

class SomedataList(generics.ListCreateAPIView):
    """
    List existing kv pairs and create new ones
    """
    queryset = Somedata.objects.all()
    serializer_class = SomedataSerializer

class SomedataDetail(generics.RetrieveUpdateDestroyAPIView):
    """
    View, edit and delete existing kv pairs
    """
    queryset = Somedata.objects.all()
    serializer_class = SomedataSerializer
    lookup_field = "key"
    lookup_url_kwarg = "key"

class CarList(generics.ListAPIView):
    """
    List existing cars and create new ones
    """
    queryset = Car.objects.all()
    serializer_class = CarSerializer




class RegisterCar(generics.GenericAPIView):
    """
    Register a new car (should only be used by cars)
    Returns the id of the newly created car as plaintext
    """    
    serializer_class = CarSerializer
    queryset = Car.objects.all()
    schema = custom_schema.RegisterCarSchema()
    def post(self, request):
        # TODO remove
        print(self.schema.get_components(path="/cars/{name}/", method="get"))
        # TODO decide if this supports any content types beside json
        # get the car data from the request
        if request.content_type == "application/json":
            #data = JSONParser.parse(request)
            data = request.data
            print(data)
            serializer = CarSerializer(data=data)
            if serializer.is_valid():
                serializer.save()
                # return the id in plain format so the car doesn't have to parse JSON
                id = serializer.data.get("id", "")
                return HttpResponse(id, status=201, content_type="text/plain")
            else:
                return Response(serializer.errors, status=400)            
            
        else: 
            return Response("Invalid Content-Type", status="415")
        

class CarDetail(generics.RetrieveUpdateDestroyAPIView):
    """
    View, update and delete specific existing cars
    """
    queryset = Car.objects.all()
    serializer_class = CarSerializer
    lookup_field = "id"
    lookup_url_kwarg = "id"

class CarSessionList(generics.ListCreateAPIView):
    """
    List and create sessions
    """
    queryset = CarSession.objects.all()
    serializer_class = CarSessionSerializer
    lookup_field = "id"    

class CarSessionDetails(generics.RetrieveDestroyAPIView):
    """
    View, update or delete existing sessions (to update cars use the .../sessions/<id>/cars endpoint)
    """
    queryset = CarSession.objects.all()
    serializer_class = CarSessionSerializer
    lookup_field = "id"
    lookup_url_kwarg = "id"

 
class EditCarSession(APIView):
    """
    Add cars to the session
    Include the id of the car in the body, either as text/plain with only the id in the body
    or as application/json with {"car":<car_id>} in the body
    """  
    schema = custom_schema.EditCarSessionSchema()      
    def get_data(self, request, id):        
        # TODO review
        try:
            self.channel_layer = get_channel_layer()
            self.sess:CarSession = CarSession.objects.get(id=id)
        except CarSession.DoesNotExist: 
            return Response(data={"error":"session does not exist"},status=404)    
        try: 
            if request.content_type == "application/json":
                data = json.loads(request.body.decode())
                self.car_id = data.get("car", "")
            elif len(request.body) > 0:
                self.car_id = request.body.decode()
            else:
                return Response(data={"error" : "body does not specify the car"}, status=400 )
            self.car:Car = Car.objects.get(id=self.car_id)
        except:
            return Response(data={"error":"the specified car does not exist"}, status=404)
        if self.car.channel_name == "":
            return JsonResponse(data={"error":"the car doesn't have a valid channel name. Check if the car is connected"})
        return None
        
    def post(self, request, id):       
        print(self.schema)
        resp = self.get_data(request, id)
        # if any errors occured during the car retrieval get_data returns an error response
        if resp != None:
            return resp
        # add the cars websocket channel to the session group
        async_to_sync(self.channel_layer.group_add)(
            f"group_{self.sess.id}", self.car.channel_name
        )
        self.sess.cars.add(self.car)
        self.sess.save()
        serializer = CarSessionSerializer(self.sess)
        return JsonResponse(serializer.data)        



class CarSessionCarsDetail(APIView):
    """
    Remove cars from the session
    """
    def get_data(self, request, id, name):
        # TODO review
        try:
            self.channel_layer = get_channel_layer()
            self.sess:CarSession = CarSession.objects.get(id=id)
        except CarSession.DoesNotExist: 
            return Response(data={"error":"session does not exist"},status=404)    
        try:
            self.car:Car = Car.objects.get(name=name)
        except:
            return Response(data={"error":"the specified car does not exist"}, status=404)
        if self.car.channel_name == "":
            return JsonResponse(data={"error":"the car doesn't have a valid channel name. Check if the car is connected"})
        return None     
    
    def delete(self, request, id, name):
        self.get_data(request, id, name)
        # remove the cars websocket channel from the session group
        async_to_sync(self.channel_layer.group_discard)(
            f"group_{self.sess.id}", self.car.channel_name
        )
        self.sess.cars.remove(self.car)
        serializer = CarSessionSerializer(self.sess)
        return JsonResponse(serializer.data)



@csrf_exempt
@api_view(["DELETE","POST"])
def edit_car_session(request : HttpRequest, id):
    """

    """
    try:
        channel_layer = get_channel_layer()
        sess:CarSession = CarSession.objects.get(id=id)
    except CarSession.DoesNotExist: 
        return JsonResponse({"error":"session does not exist"},status=404)    
    try: 
        if request.content_type == "application/json":
            data = json.loads(request.body.decode())
            car_name = data.get("car", "")
        elif len(request.body) > 0:
            car_name = request.body.decode()
        else:
            return JsonResponse({"error" : "body does not specify the car"}, status=400 )
        car = Car.objects.get(name=car_name)
    except:
        return JsonResponse({"error":"the specified car does not exist"}, status=404)
    if car.channel_name == "":
        return JsonResponse({"error":"the car doesn't have a valid channel name. Check if the car is connected"})
    if request.method == "POST":
        # add the cars websocket channel to the session group
        async_to_sync(channel_layer.group_add)(
            f"group_{sess.id}", car.channel_name
        )
        sess.cars.add(car)
        sess.save()
        serializer = CarSessionSerializer(sess)
        return JsonResponse(serializer.data)
    elif request.method == "DELETE":
        # remove the cars websocket channel from the session group
        async_to_sync(channel_layer.group_discard)(
            f"group_{sess.id}", car.channel_name
        )
        sess.cars.remove(car)
        serializer = CarSessionSerializer(sess)
        return JsonResponse(serializer.data)
    return JsonResponse({"error":"invalid operation"}, status=400)
