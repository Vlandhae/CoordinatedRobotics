package com.application.base.services.internet;

import com.application.base.services.actionlog.ActionLogContentService;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

public class MyWebSocketClient extends WebSocketClient{
    private ActionLogContentService actionLog;

    public MyWebSocketClient(URI serverUri) {
      super(serverUri);
    }
    public MyWebSocketClient(URI serverUri, ActionLogContentService actionLog) {
      super(serverUri);
      this.actionLog = actionLog;
    }

    @Override
    public void connect()
    {
      if(actionLog != null)
        actionLog.addAdditionalContent("connecting " + this.uri);
      super.connect();
    }

    //TODO replace with good code
    public void setActionLog(ActionLogContentService actionLog)
    {
      this.actionLog = actionLog;
    }

    @Override
    public void onMessage(String message) {
      System.out.println("received message " + message);
      if(this.actionLog != null)
      {
        this.actionLog.addAdditionalContent("Received from ws: " + message);
      }
    }

    @Override
    public void onOpen(ServerHandshake handshake) {
      System.out.println("connected to server");
      this.actionLog.addAdditionalContent("connected");
      if(this.actionLog != null)
      {
        this.actionLog.addAdditionalContent("connected");
      }
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
    }

    @Override
    public void onError(Exception ex) {
      ex.printStackTrace();
    }

    public void openConnection(String internetAddress) {
      connect();
    }

    public void closeConnection() {
    }

    public void ensureConnection() {
    }

    public void sendCommand(String command) {
      send(command);
    }
}
