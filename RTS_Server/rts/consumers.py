import json
from channels.generic.websocket import WebsocketConsumer
from asgiref.sync import async_to_sync
from time import sleep
from rts.models import Car, CarSession
from rts.serializers import CarSerializer
from rts.constants import GROUP_CLOSING_COMMAND, COMMAND_LIST

class CarConsumer(WebsocketConsumer):
    def connect(self):
        self.room_name = self.scope["url_route"]["kwargs"]["car_id"]
        self.room_group_name = f"group_{self.room_name}"
        print(self.room_group_name)
        # Join room group
        async_to_sync(self.channel_layer.group_add)(
            self.room_group_name, self.channel_name
        )

        try: 
            self.car = Car.objects.get(id=self.room_name)                
            print(f"{self.room_name} : {self.channel_name}")
            # TODO this shoudl only happen when the car joins the channel maybe specific message
            self.car.channel_name = self.channel_name
            self.car.save()
        except Car.DoesNotExist:
            print("Websocket car not found")
            pass

        self.accept()
    
    def disconnect(self, close_code):
         # Leave room group
        async_to_sync(self.channel_layer.group_discard)(
            self.room_group_name, self.channel_name
        )
        print(f"disconnected {close_code}")
    
    def move(self, newChannel):
        # TODO find out why no messages are received in the new group
        async_to_sync(self.channel_layer.group_add)(
            newChannel, self.channel_name
        )
        self.send("moved succesfully")

    def receive(self, text_data):
        msg = text_data
        print(msg)
        if msg == "info":
            if self.car is None:
                self.send("car does not exist")
            else:
                serializer = CarSerializer(self.car)
                self.send(json.dumps(serializer.data))
            return
        if msg=="move":
            self.move("group_car3")
        if msg == GROUP_CLOSING_COMMAND:
            async_to_sync(self.channel_layer.group_discard)(self.room_group_name, self.channel_name)
        print(msg)
        async_to_sync(self.channel_layer.group_send)(
            self.room_group_name, {"type": "command", "command": msg}
        )    
    def system(self, event):
        print("received system command")

    def command(self, event):
        print(event)
        print("command event received")
        com = event.get("command", None)
        if com is None:
            return        
        if com.upper() in COMMAND_LIST and self.car is not None:
            self.car.current_command = com.upper()
            self.car.save()
        self.send(text_data=json.dumps({"command":com}))

class SessionConsumer(WebsocketConsumer):
    def connect(self):
        # TODO add different rout for this and replace car_name
        self.room_name = self.scope["url_route"]["kwargs"]["session_name"]
        self.room_group_name = f"group_{self.room_name}"
        print(self.room_group_name)
        # Join room group
        async_to_sync(self.channel_layer.group_add)(
            self.room_group_name, self.channel_name
        )
        self.accept()

    def command(self, event):
        print(event)
        print("command event received")
        com = event.get("command", None)
        if com is None:
            return        
        if com.upper() in COMMAND_LIST and self.car is not None:
            self.car.current_command = com.upper()
            self.car.save()
        self.send(text_data=json.dumps({"command":com}))

    def receive(self, text_data):
        msg = text_data
        print(msg)
        if msg == "info":
            if self.car is None:
                self.send("car does not exist")
            else:
                serializer = CarSerializer(self.car)
                self.send(json.dumps(serializer.data))
            return
        if msg=="move":
            self.move("group_car3")
        if msg == GROUP_CLOSING_COMMAND:
            async_to_sync(self.channel_layer.group_discard)(self.room_group_name, self.channel_name)
        print(msg)
        async_to_sync(self.channel_layer.group_send)(
            self.room_group_name, {"type": "command", "command": msg}
        )  

    def disconnect(self, close_code):
         # Leave room group
        async_to_sync(self.channel_layer.group_discard)(
            self.room_group_name, self.channel_name
        )
        print(f"disconnected {close_code}")