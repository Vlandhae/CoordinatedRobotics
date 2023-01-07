import json
from channels.generic.websocket import WebsocketConsumer
from asgiref.sync import async_to_sync
from time import sleep
from rts.models import Car, CarSession, RTSMap
from rts.serializers import CarSerializer
from rts.constants import GROUP_CLOSING_COMMAND, COMMAND_LIST, get_message_type, MESSAGE_TYPES
import rts.parsers
from rts.map_thingy import RTS_Map

class CarConsumer(WebsocketConsumer):
    def connect(self):
        self.room_name = self.scope["url_route"]["kwargs"]["car_id"]
        self.room_group_name = f"group_{self.room_name}"
        self.maps = []
        print(self.room_group_name)
        # Join room group
        async_to_sync(self.channel_layer.group_add)(
            self.room_group_name, self.channel_name
        )

        try: 
            self.car = Car.objects.get(id=self.room_name)                
            print(f"{self.room_name} : {self.channel_name}")            
            self.car.channel_name = self.channel_name
            self.car.connected = True
            sessions = CarSession.objects.filter(cars__id=self.car.id)
            print("sessions", sessions)
            for session in sessions:
                # get the name of the session group
                session_group = f"group_{session.id}"
                # join the sessions group
                async_to_sync(self.channel_layer.group_add)(
                    session_group, self.channel_name
                )     
                print(type(RTSMap))  
                m = RTSMap.objects.filter(session__id=session.id)
                if len(m) <= 0:
                    m = RTSMap(session=session)
                    m.save()
                else:
                    m = m.first()
                self.maps.append(RTS_Map(m.id))

            self.car.save()
        except Car.DoesNotExist:
            print("Websocket car not found")
            pass

        self.accept()
        
    def update_maps(self):
        if self.car is None:
            return
        sessions = CarSession.objects.filter(cars__id=self.car.id)
        print("sessions", sessions)
        for session in sessions:
            # get all maps of sessions the car is connected to
            #TODO leave maps when the car leaves the session 
            m = RTSMap.objects.filter(session__id=session.id)
            if len(m) <= 0:
                m = RTSMap(session=session)
                m.save()
            else:
                m = m.first()
            self.maps.append(RTS_Map(m.id))
    
    def disconnect(self, close_code):
         # Leave room group
        async_to_sync(self.channel_layer.group_discard)(
            self.room_group_name, self.channel_name
        )
        # set the cars connected status to false
        if self.car != None:
            self.car.connected = False
            self.car.save()
        print(f"disconnected {close_code}")
        print(f"CAR DISCONNECTED {close_code}")
    
    def receive(self, text_data):
        self.update_maps()
        msg = text_data
        print(msg)
        if msg == "info":
            if self.car is None:
                self.send("car does not exist")
            else:
                serializer = CarSerializer(self.car)
                self.send(json.dumps(serializer.data))
            return
        if msg == GROUP_CLOSING_COMMAND:
            async_to_sync(self.channel_layer.group_discard)(self.room_group_name, self.channel_name)
        # allow direct commands from cars for now
        if msg in COMMAND_LIST:
            async_to_sync(self.channel_layer.group_send)(
                self.room_group_name, {"type": "command", "command": msg}
        )
        # broadcast all data received from cars as a carinfo event
        async_to_sync(self.channel_layer.group_send)(
            self.room_group_name, {"type": "carinfo", "message": msg}
        )    
        # TODO replace with handler 

        # split the individual messages
        parts = msg.split(";")
        if msg[-1] != ";":
            pass
            # TODO make sure partial messages dont get lost

        # remove any empty strings
        parts = list(filter(len, parts))
        for part in parts:
            msg_type = get_message_type(msg[0])
            # TODO parse the message usign the correct parser
            # TODO handle the parsed message
            if part[0] == "P":       
                data = rts.parsers.parse_position_info(msg[1:])
                print(data)         
                for m in self.maps:
                    x, y, occupied = data
                    m.add_point(x, y, occupied)
                    print(m.obstacles)
                    print(m.explored_squares)
                    print(m.to_list())
                    print("test")

    def system(self, event):
        print("received system command")

    def command(self, event):
        print(event)
        print("command event received")
        com = event.get("command", None)
        if com is None:
            return        
        if com.upper() in COMMAND_LIST:
            if self.car is not None:
                self.car.current_command = com.upper()
                self.car.save()
            self.send(text_data=json.dumps(com))

    def carinfo(self, event):
        print("carinfo received", event)
        msg = event.get("message", "")
        # TODO message end marker e.g. ;
        # TODO decide on info message start character e.g. Y
        # TODO move them to constants
        end_char = ";"
        message_begin = "Y"
        self.send(text_data=f"{message_begin}{msg}{end_char}")


class SessionConsumer(WebsocketConsumer):
    def connect(self):        
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
        print("test")
        com = event.get("command", None)
        if com is None:
            return        
        self.send(text_data=json.dumps({"command":com}))
    
    def carinfo(self, event):
        self.send(text_data=json.dumps({"carinfo":event.get("messsage", "")}))
        pass

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
        print(f"SESSION DISCONNECT {close_code}")