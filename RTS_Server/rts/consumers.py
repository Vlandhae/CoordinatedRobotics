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
        self.mapped_sessions = set()
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
                self.mapped_sessions.add(m.session.id)

            self.car.save()
        except Car.DoesNotExist:
            print("Websocket car not found")
            pass

        self.accept()
        
    def update_maps(self):
        if self.car is None:
            return
        sessions = CarSession.objects.filter(cars__id=self.car.id)
        
        for session in sessions:
            if session.id in self.mapped_sessions:
                continue
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
        print("msg received: ", msg)
        print("msg end")
        if msg == "info":
            if self.car is None:
                self.send("car does not exist")
            else:
                serializer = CarSerializer(self.car)
                self.send(json.dumps(serializer.data))
            return
        if msg == GROUP_CLOSING_COMMAND:
            async_to_sync(self.channel_layer.group_discard)(self.room_group_name, self.channel_name)
                
        # broadcast all data received from cars as a carinfo event
        async_to_sync(self.channel_layer.group_send)(
            self.room_group_name, {"type": "carinfo", "message": msg}
        )         
        # TODO filter some messages
        if msg == "Connected": 
            return
        self.handle_car_message(msg)

    def handle_car_message(self, msg):        
        # split the individual messages
        parts = msg.split(";")
        if msg[-1] != ";":
            pass
            # TODO make sure partial messages dont get lost
        # remove any empty strings
        parts = list(filter(len, parts))
        for part in parts:            
            msg_type = get_message_type(part[0])          
            print(msg_type)
            msg_content = part[1:]  
            if msg_type == MESSAGE_TYPES.position_info:       
                print(msg)
                data = rts.parsers.parse_position_info(msg_content)    
                if data is None:
                    return            
                for m in self.maps:
                    x, y, occupied = data
                    m.add_point(x, y, occupied)                        
            elif msg_type == MESSAGE_TYPES.position_data_request:
                # ensure that a map exists
                if len(self.maps) < 1:
                    return
                # TODO what if multiple maps exist
                map_to_send = self.maps[0].to_list()                
                for row in range(len(map_to_send)):
                    for col in range(len(map_to_send[row])):
                        occ = bool(map_to_send[row, col] == 1)                        
                        self.send(text_data=rts.parsers.encode_position_data(row, col, occ))
            elif msg_type == MESSAGE_TYPES.obstacle_position_request:
                print("sending obstacle poistioin")
                map_to_send = self.maps[0]
                
                obstacles = map_to_send.obstacles
                for obstacle in obstacles:
                    x, y = obstacle
                    self.send(rts.parsers.encode_position_data(x, y, True))
                
                


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
            self.send(text_data=rts.parsers.encode_command(com))

    def carinfo(self, event):
        print("carinfo received", event)
        msg = event.get("message", "")
        # TODO message end marker e.g. ;
        # TODO decide on info message start character e.g. Y
        # TODO move them to constants
        end_char = ";"
        message_begin = "Y"
        #self.send(text_data=f"{message_begin}{msg}{end_char}")


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