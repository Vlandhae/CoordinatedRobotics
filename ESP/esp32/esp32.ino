
#include <base64.hpp>

#include <WebSockets.h>
#include <WebSocketsClient.h>
#include <WiFi.h>
#include <list>


#define RX2 33
#define TX2 4

// modify ssid and password
char ssid[] = ""; 
char pass[] = "";

int keyIndex = 0;

int status = WL_IDLE_STATUS;

WiFiClient client;

WebSocketsClient webSocket;
String end = ";";
String positionBegin = "P";
String positionRequestIdentifier = "M";
String positionObstacle = "O";

void requestPositionData()
{
  webSocket.sendTXT(positionRequestIdentifier + end);
  Serial.println("requesting position data");
}

void sendPositionData(int x, int y, bool occupied)
{
  Serial.println("xxx");
  Serial.println(x);
  assert(sizeof(int) == 4);
  uint8_t toEncode[9];
  memcpy(toEncode, &x, 4) ;
  memcpy((toEncode + 4), &y, 4) ;
  toEncode[8] = occupied;
  unsigned char enc[64];
  const auto s = encode_base64(toEncode, 9, enc);
  String encoded = (const char*) enc;
  Serial.println(encoded);
  webSocket.sendTXT(positionBegin + encoded + end);
}

void requestObstacleData()
{
  webSocket.sendTXT(positionObstacle + end);
  Serial.println("requesting position data");
}

int obst[100][2];
int i;
int j = 0;
void handlePositionData(unsigned char* start, int length)
{
  if(length != 13)
  {
    return;
  }

  unsigned char buf[16]{0};
  // + 1 to skip the identifier at the start of the string
  const auto size = decode_base64(start + 1, length - 1, buf);
  Serial.println(buf[16]);
  int x = 0;
  int y = 0;
  bool occupied = buf[8];

  // +1 to skip the identifier
  memcpy(&x, buf, 4);
  memcpy(&y, buf+4, 4);

  //TODO handle
  Serial.printf("%d %d %x\n", x,y,occupied);
  Serial.println(buf[16]);
  if(occupied)
  {
    Serial1.write('M');
    Serial1.write(x);
    Serial1.write(y);
    Serial1.write(';');
  }

}


void handleRemoteCommand(unsigned char* begin, int length)
{
  if(length != 2)
  {
    return;
  }
  char command = begin[1];
}

char buffer[32]{};
int bufferpos = 0;
uint8_t handleWebsocketMessage(uint8_t * payload, size_t length)
{
  //TODO use the partial buffered message correctly
  String current;
  // check if partial message is in buffer
  if(buffer[0] != 0)
  {
    current = buffer;
    for(int i = 0; i < 32; ++i)
    {
      buffer[i] = 0;
    }
  }
  int i = 0;
  unsigned char* begin;
  int currentLength;
  uint8_t lastEnd = 0;

  while(i < length)
  {
    char c;
    begin = payload + i;
    currentLength = 0;
    while( i < length && (c = payload[i++]) != ';')
    {
      //Serial.printf("%d %02x%c\n", i,c,c);
      current += c;
      currentLength += 1;
    }
    if(payload[i] == ';'){
      lastEnd = i;
    }
    char type = current.charAt(0);
    switch(type)
    {
      case 'Q':
        handlePositionData(begin, currentLength);
        break;
      case 'C':
        handleRemoteCommand(begin, currentLength);
        break;

      default:
        Serial.println("Unknown message type");
        break;
    }
    current = "";
  }
  return lastEnd;

}

void webSocketEvent(WStype_t type, uint8_t * payload, size_t length) {

	switch(type) {
		case WStype_DISCONNECTED:
			Serial.printf("[WSc] Disconnected!\n");
			break;
		case WStype_CONNECTED: {
			Serial.printf("[WSc] Connected to url: %s\n", payload);

			// send message to server when Connected
			webSocket.sendTXT("Connected");
		}
			break;
		case WStype_TEXT:
			Serial.printf("[WSc] get text: %s\n", payload);
      Serial1.write(payload, length);


      handleWebsocketMessage(payload, length);
			break;
		case WStype_BIN:
			Serial.printf("[WSc] get binary length: %u\n", length);
			//hexdump(payload, length);

			// send data to server
			// webSocket.sendBIN(payload, length);
			break;
        case WStype_PING:
            // pong will be sent automatically
            Serial.printf("[WSc] get ping\n");
            break;
        case WStype_PONG:
            // answer to a ping we send
            Serial.printf("[WSc] get pong\n");
            break;
    }
}



void printWifiStatus() {

  //print the SSID of the network you're attached to:

  Serial.print("SSID: ");

  Serial.println(WiFi.SSID());

  //print your WiFi shield's IP address:

  IPAddress ip = WiFi.localIP();

  Serial.print("IP Address: ");

  Serial.println(ip);

  //print the received signal strength:

  long rssi = WiFi.RSSI();

  Serial.print("signal strength (RSSI):");

  Serial.print(rssi);

  Serial.println(" dBm");
}

void setup() {
 Serial.begin(9600);
 Serial1.begin(9600, SERIAL_8N1, RX2, TX2);

  while (!Serial) {

    ; // wait for serial port to connect. Needed for native USB port only

  }

  // attempt to connect to Wifi network:
  WiFi.begin(ssid, pass);
  while (status != WL_CONNECTED) {

    Serial.print("Attempting to connect to SSID: ");

    Serial.println(ssid);
    Serial.println(status);

    // Connect to WPA/WPA2 network. Change this line if using open or WEP network:

    status = WiFi.status();

    // wait 10 seconds for connection:

    delay(10000);

  }

  Serial.println("Connected to wifi");

  printWifiStatus();

  Serial.println("\nStarting connection to server...");

  String s("1");
  if (client.connect("159.69.196.15", 8000)) {

    Serial.println("connected to server");

    // Make a HTTP request:

    client.println("POST /cars/register/ HTTP/1.1");
    client.println("Connection: close");
    client.println("Content-Type: application/json");
    client.println("Content-Length: 22");
    client.println();
    client.println("{\"name\":\"arduinoTest\"}");
    delay(1000);

    while (client.available()) {
      s = client.readStringUntil('\n');
      Serial.write(s.c_str());
    //Serial1.println(s.c_str());
    }
    const auto id = s;
    Serial.write(s.c_str());
    Serial.flush();
  }
  // if you get a connection, report back via serial:

  // server address, port and URL
  String addr = String("/ws/car/") + 4 + String("/");
	webSocket.begin("159.69.196.15", 8000, addr);

	// event handler
	webSocket.onEvent(webSocketEvent);

}

int available = 0;
uint16_t counter = 0;
bool test = true;
void loop() {
  webSocket.loop();
  //every few iterations check if output from the car is available and send it to ws
  while(++counter == 0 && (available = Serial1.available()) > 0 )
  {
    char buf[64]{0};
    uint8_t size = available > 63 ? 63 : available;
    
    const auto len = Serial1.readBytesUntil(';', buf, size);
    //Serial.println(buf);

    Serial.println("start");
    for(int i = 0; i < len; i++){
      if(buf[i]=='P'){
        sendPositionData(buf[i+1], buf[i+2], buf[i+3]);
        //Serial.println(buf[i]);
      }
      if(buf[i] == 'O')
      {
        i = 0;
        requestObstacleData();

      }  
    }
    buf[size] = 0;
    //webSocket.sendTXT(buf, size);
  }
 
}


