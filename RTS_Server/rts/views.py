from django.shortcuts import render
from rts.models import Somedata
from rts.serializers import SomedataSerializer
from django.http import HttpResponse, JsonResponse
from django.views.decorators.csrf import csrf_exempt
from rest_framework.parsers import JSONParser


@csrf_exempt
def somedata_list(request):
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