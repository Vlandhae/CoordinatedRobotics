from enum import Enum

COMMAND_LIST = ["U", "L", "K","R", "D", "S", "H", "0", "1", "2","3", "4","5","6","7","8","9", "I" ]
GROUP_CLOSING_COMMAND = "system.group_closing"

class MESSAGE_TYPES(Enum):
    carinfo = 1
    command = 2
    car_status = 3
    position_info = 4
    unknown = 9999

class CONSUMER_TYPE(Enum):
    CAR = 1
    SESSION = 2

MESSAGE_TYPES_MAPPING = {
    "Y" : MESSAGE_TYPES.carinfo,
    "C" : MESSAGE_TYPES.command,
    "S" : MESSAGE_TYPES.car_status,
    "P" : MESSAGE_TYPES.position_info,
}

def get_message_type(identifier : MESSAGE_TYPES):
    return MESSAGE_TYPES_MAPPING.get(identifier, MESSAGE_TYPES.unknown)