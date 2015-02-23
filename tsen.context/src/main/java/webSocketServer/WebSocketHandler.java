/*
package webSocketServer;

import context.Context;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.webbitserver.BaseWebSocketHandler;
import org.webbitserver.WebSocketConnection;
import org.webbitserver.WebServer;

import java.io.IOException;


public class WebSocketHandler extends BaseWebSocketHandler {

    private WebServer _wss;
    private Context _ctx;
    private static final String TAG = "WEB SOCKET SERVER";

    public WebSocketHandler(WebServer wss, Context ctx) {
        System.out.println("lauching web server");
        _wss = wss;
        _ctx = ctx;
    }

    @Override
    public void onOpen(WebSocketConnection connection) throws Exception {
        System.out.println("Opening WS connection " + connection.toString() + "...");
        //Log.i(TAG,"Opening WS connection " + connection.toString());
    }

    @Override
    public void onClose(WebSocketConnection connection) throws Exception {
        System.out.println("web socket server has ended connection " + connection.toString());
    }

    public void onMessage(WebSocketConnection connection, String message) {

        JsonNode jsonRpc;
        try {
            jsonRpc = new ObjectMapper().readTree(message);
            _ctx.setVote(jsonRpc.get("id").asText(),jsonRpc.get("vote").asText(), jsonRpc.get("ts").asLong(),connection);

        } catch (IOException e) {
            System.out.println("message :" + message + " is not a json");
        }

        System.out.println("web socket server has received a message : " + message);
        //Log.w(TAG,"web socket server has received a message : " + message);
    }

}*/
