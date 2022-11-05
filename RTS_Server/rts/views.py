import json
from django.shortcuts import render
from rts.models import Somedata, Car, CarSession
from rts.serializers import SomedataSerializer, CarSerializer, CarSessionSerializer
from django.http import HttpResponse, JsonResponse, HttpRequest
from django.views.decorators.csrf import csrf_exempt
from rest_framework.parsers import JSONParser
from rts.car_data import command_list


@csrf_exempt
def somedata_list(request:HttpRequest):
    print(request.session)
    
    if request.method == "GET":
        data = Somedata.objects.all()
        serializer = SomedataSerializer(data, many=True)
        return JsonResponse(serializer.data, safe=False)
    elif request.method == "POST":
        data = JSONParser().parse(request)
        serializer = SomedataSerializer(data=data)
        if serializer.is_valid():
            serializer.save()
            return JsonResponse(serializer.data, status=201)
        else:
            return JsonResponse(serializer.errors, status=400)      
    else:
        return JsonResponse({"error":"unsuported operation"}, status=400)

@csrf_exempt
def somedata(request, key):
    try:
        element = Somedata.objects.get(key=key)
    except Somedata.DoesNotExist:
        return HttpResponse(status=404)
    
    if request.method == "GET":
        serializer = SomedataSerializer(element)
        return JsonResponse(serializer.data)
    elif request.method == "PUT":
        data = JSONParser().parse(request)
        serializer = SomedataSerializer(element, data=data)
        if serializer.is_valid():
            serializer.save()
            return JsonResponse(serializer.data)
        else:
            return JsonResponse(serializer.errors, status=400)
    elif request.method == "DELETE": 
        element.delete()
        return HttpResponse(status=204)

@csrf_exempt
def session_test(request:HttpRequest):
    return JsonResponse({"id" : list(request.session.items())})

@csrf_exempt
def car_root(request:HttpRequest):
    if request.method == "GET":
        data = Car.objects.all()
        serializer = CarSerializer(data, many=True)
        return JsonResponse(serializer.data, safe=False)
    if request.method == "POST":
        data = JSONParser().parse(request)
        serializer = CarSerializer(data=data)
        if serializer.is_valid():
            serializer.save()
            return JsonResponse(serializer.data, status=201)
        else:
            return JsonResponse(serializer.errors, status=400)      
    else:
        return JsonResponse({"error":"unsuported operation"}, status=400)

@csrf_exempt
def car_individual(request : HttpRequest, name):
    try:
        c = Car.objects.get(name=name)
    except Car.DoesNotExist:
        return HttpResponse(status=404)
    if request.method == "GET":
        serializer = CarSerializer(c)
        return JsonResponse(serializer.data)
    elif request.method == "PUT":
        data = JSONParser().parse(request)
        serializer = CarSerializer(c, data=data)
        if serializer.is_valid():
            serializer.save()
            return JsonResponse(serializer.data)
        else:
            return JsonResponse(serializer.errors, status=400)
    elif request.method == "DELETE": 
        c.delete()
        return HttpResponse(status=204)

@csrf_exempt
def car_current_command(request : HttpRequest, name):
    try:
        c = Car.objects.get(name=name)
    except Car.DoesNotExist:
        return HttpResponse(status=404)
    # GET is used to retrieve the command
    if request.method == "GET":
        format = request.GET.get("format", "")
        # depending on format query parameter either return json or raw command in body
        if format.lower() == "json":
            return JsonResponse({"command":c.current_command})
        else:
            response = HttpResponse(c.current_command)
            response["Content-Type"] = "text/plain"
            return response
    # POST or PUT are used to update the command
    if request.method in ["PUT", "POST"] and request.body.count:
        # check the content type of the data and process accordingly
        if request.content_type == "application/x-www-form-urlencoded":
            # extract the command from the form and check its format
            data = request.POST.values().get("command", None)
            if data is None or len(data) < 1:
                return JsonResponse({"error":"POST form data did not contain command key"}, status=400)
            elif len(data) > 1:
                return JsonResponse({"error":"Form data contained invalid key"}, status=400)
        # if the data is not a form check if the body is just the command 
        elif request.body.count() == 1:
            data = request.body.decode()
        # check if the command is valid
        if data not in command_list:
            return JsonResponse({"error":"command %s does not exist" % data}, status=400)
            
        # here we can assume that a command was passed and is correct
        c.current_command = data
        c.save()

@csrf_exempt
def car_session_root(request : HttpRequest):
    if request.method == "GET":
        data = CarSession.objects.all()
        serializer = CarSessionSerializer(data, many=True)
        return JsonResponse(serializer.data, safe=False)
    if request.method == "POST":
        data = JSONParser().parse(request)
        serializer = CarSessionSerializer(data=data)
        if serializer.is_valid():
            serializer.save()
            return JsonResponse(serializer.data, status=201)
        else:
            return JsonResponse(serializer.errors, status=400)      
    else:
        return JsonResponse({"error":"unsuported operation"}, status=400)


def individual_car_session(request : HttpRequest, id):
    try:
        sess = CarSession.objects.get(id=id)
    except CarSession.DoesNotExist: 
        return HttpResponse(status=404)    
    if request.method == "GET":
        return JsonResponse(CarSessionSerializer(sess).data)
    elif request.method == "PUT":
        data = JSONParser().parse(request)
        serializer = CarSessionSerializer(sess, data=data)
        if serializer.is_valid():
            serializer.save()
            return JsonResponse(serializer.data, status=201)
        else:
            return JsonResponse(serializer.errors, status=400)      
    elif request.method == "DELETE":
        sess.delete()
        return HttpResponse(status=204)
    return JsonResponse({"erorr":"unsupported operation"}, status=400)

@csrf_exempt
def edit_car_session(request : HttpRequest, id):
    try:
        sess = CarSession.objects.get(id=id)
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
    if request.method == "POST":
        sess.cars.add(car)
        sess.save()
        serializer = CarSessionSerializer(sess)
        return JsonResponse(serializer.data)
    elif request.method == "DELETE":
        sess.cars.remove(car)
        serializer = CarSessionSerializer(sess)
        return JsonResponse(serializer.data)
    return JsonResponse({"error":"invalid operation"}, status=400)

