from base64 import b64decode
from rts.constants import MESSAGE_TYPES


def parse_carinfo(msg):
    pass

def parse_position_info(msg):
    
    enc = msg.encode("utf-8")
    dec = b64decode(enc)
    print(dec)
    print(type(dec))
    print(len(dec))
    x = int.from_bytes(dec[:4], "little")
    y = int.from_bytes(dec[4:8], "little")
    occupied = bool(dec[8])
    print(x, y, occupied)
    return (x,y,occupied)


def parse_command(msg):
    pass

def parse_car_status(msg):
    pass

def get_parser(msg_type : MESSAGE_TYPES):
    return parser_map.get(msg_type, lambda x: tuple())


parser_map = {
    MESSAGE_TYPES.carinfo : parse_carinfo,
    MESSAGE_TYPES.command : parse_command,
    MESSAGE_TYPES.position_info : parse_position_info,
    MESSAGE_TYPES.car_status : parse_car_status,
}