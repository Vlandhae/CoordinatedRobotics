from enum import Enum

COMMAND_LIST = ["U", "L", "K","R", "D", "S", "H", "0", "1", "2","3", "4","5","6","7","8","9", "I" ]
GROUP_CLOSING_COMMAND = "system.group_closing"

class MESSAGE_TYPES(Enum):
    carinfo = 1
    command = 2
    car_status = 3
    position_info = 4
    position_data = 5
    position_data_request = 6
    obstacle_position_request = 7
    unknown = 9999

class CONSUMER_TYPE(Enum):
    CAR = 1
    SESSION = 2

MESSAGE_TYPES_MAPPING = {
    "Y" : MESSAGE_TYPES.carinfo,
    "C" : MESSAGE_TYPES.command,
    "S" : MESSAGE_TYPES.car_status,
    "P" : MESSAGE_TYPES.position_info,
    "Q" : MESSAGE_TYPES.position_data, 
    "M" : MESSAGE_TYPES.position_data_request,
    "O" : MESSAGE_TYPES.obstacle_position_request,    
}
END_MESSAGE_IDENTIFIER = ";"
MESSAGE_TYPES_MAPPING.update({v:k for k,v in MESSAGE_TYPES_MAPPING.items()})



def get_message_type(identifier):
    print(identifier)
    return MESSAGE_TYPES_MAPPING.get(identifier, MESSAGE_TYPES.unknown)