import json
from channels.generic.websocket import WebsocketConsumer
from asgiref.sync import async_to_sync
from time import sleep
from rts.models import Car
from rts.serializers import CarSerializer
import rts.car_data

class CarConsumer(WebsocketConsumer):
    def connect(self):
        self.room_name = self.scope["url_route"]["kwargs"]["car_name"]
        self.room_group_name = f"group_{self.room_name}"

        # Join room group
        async_to_sync(self.channel_layer.group_add)(
            self.room_group_name, self.channel_name
        )

        try: 
            self.car = Car.objects.get(name=self.room_name)                
        except Car.DoesNotExist:
            pass

        self.accept()
    
    def disconnect(self, close_code):
         # Leave room group
        async_to_sync(self.channel_layer.group_discard)(
            self.room_group_name, self.channel_name
        )
        print(f"disconnected {close_code}")
    
    def receive(self, text_data):
        msg = text_data
        if msg == "info":
            if self.car is None:
                self.send("car does not exist")
            else:
                serializer = CarSerializer(self.car)
                self.send(json.dumps(serializer.data))
            return
        print(msg)
        async_to_sync(self.channel_layer.group_send)(
            self.room_group_name, {"type": "command", "command": msg}
        )        

    def command(self, event):

        print(event)
        print("command event received")
        com = event.get("command", None)
        if com is None:
            return        
        if com in rts.car_data.command_list and self.car is not None:
            self.car.current_command = com
            self.car.save()
        self.send(text_data=json.dumps({"command":com}))