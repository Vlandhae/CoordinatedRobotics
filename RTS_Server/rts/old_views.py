@csrf_exempt
@api_view(["GET","POST"])
def somedata_list(request:HttpRequest):
    """
    GET: Get a list of all key-value pairs
    POST: Add a new kv pair. Format: {"key" : <yourkey>, "value" : <yourvalue>}
    """
    print(request.session)
    
    if request.method == "GET":
        data = Somedata.objects.all()
        serializer = SomedataSerializer(data, many=True)
        return JsonResponse(serializer.data, safe=False)
    elif request.method == "POST":
        print("body", request.body)
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
@api_view(["GET","PUT", "DELETE"])
def somedata(request, key):
    """
    Inspect, edit or delete a specific kv pair denoted by <key> in the url
    GET: Returns the kv pair
    PUT: Updates the kv pair. Format JSON with "key" and or "value" and the respective new value 
    DELETE: Deletes the kv pair
    """
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
@api_view(["GET","POST"])
def car_root(request:HttpRequest):
    """
    List all available cars or add a new one
    GET: Returns a JSON List containing all currently existing car objects
    POST: 
    """
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
@api_view(["GET","PUT", "DELETE"])
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
@api_view(["GET","POST","PUT"])
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
        if data not in COMMAND_LIST:
            return JsonResponse({"error":"command %s does not exist" % data}, status=400)
            
        # here we can assume that a command was passed and is correct
        c.current_command = data
        c.save()

@csrf_exempt
@api_view(["GET","POST"])
def car_session_root(request : HttpRequest):
    if request.method == "GET":
        data = CarSession.objects.all()
        serializer = CarSessionSerializer(data, many=True)
        return JsonResponse(serializer.data, safe=False)
    if request.method == "POST":
        # TODO decide if this should contain cars
        data = JSONParser().parse(request)
        serializer = CarSessionSerializer(data=data)
        if serializer.is_valid():
            serializer.save()
            return JsonResponse(serializer.data, status=201)
        else:
            return JsonResponse(serializer.errors, status=400)      
    else:
        return JsonResponse({"error":"unsuported operation"}, status=400)

@csrf_exempt
@api_view(["GET","DELETE"])
def individual_car_session(request : HttpRequest, id):
    try:
        sess = CarSession.objects.get(id=id)
    except CarSession.DoesNotExist: 
        return HttpResponse(status=404)    
    if request.method == "GET":
        return JsonResponse(CarSessionSerializer(sess).data)    
    elif request.method == "DELETE":
        print("deleting")        
        # TODO make sure all clients leave the websocket
        async_to_sync(get_channel_layer().group_send)(f"group_{sess.id}", {GROUP_CLOSING_COMMAND : f"group_{sess.id}"})
        sess.delete()
        return HttpResponse(status=204)
    return JsonResponse({"erorr":"unsupported operation"}, status=400)
