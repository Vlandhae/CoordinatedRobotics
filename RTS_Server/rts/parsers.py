from base64 import b64decode,b64encode
from rts.constants import MESSAGE_TYPES, get_message_type, MESSAGE_TYPES_MAPPING, END_MESSAGE_IDENTIFIER


def parse_carinfo(msg):
    pass

def parse_position_info(msg):    
    if len(msg) != 12:
        return None
    enc = msg.encode("utf-8")
    print(msg)
    print(len(msg))
    dec = b64decode(enc)
    x = int.from_bytes(dec[:4], "little")
    y = int.from_bytes(dec[4:8], "little")
    occupied = bool(dec[8])
    return (x,y,occupied)

def parse_position_info_request(msg):    
    enc = msg.encode("utf-8")
    dec = b64decode(enc)    
    print(type(dec))
    print(len(dec))
    # TODO better error handling
    if len(dec != 8):
        return
    x = int.from_bytes(dec[:4], "little")
    y = int.from_bytes(dec[4:8], "little")
    return (x,y)

def parse_command(msg):
    pass

def parse_car_status(msg):
    pass

def get_parser(msg_type : MESSAGE_TYPES):
    return parser_map.get(msg_type, lambda x: tuple())

def encode_command(msg):
    command_identifier = MESSAGE_TYPES_MAPPING.get(MESSAGE_TYPES.command)
    return f"{command_identifier}{msg}{END_MESSAGE_IDENTIFIER}"

def encode_position_data(x : int, y : int, occupied : bool):
    xb = x.to_bytes(4, "little")
    yb = y.to_bytes(4, "little")
    ob = occupied.to_bytes(1, "little")
    to_enc = xb + yb + ob
    print(to_enc)
    enc = b64encode(to_enc)
    print(enc.decode("utf-8"))
    identifier = MESSAGE_TYPES_MAPPING.get(MESSAGE_TYPES.position_data)
    return f"{identifier}{enc.decode('utf-8')}{END_MESSAGE_IDENTIFIER}"


parser_map = {
    MESSAGE_TYPES.carinfo : parse_carinfo,
    MESSAGE_TYPES.command : parse_command,
    MESSAGE_TYPES.position_info : parse_position_info,
    MESSAGE_TYPES.car_status : parse_car_status,
}