package webSocketServer;

import org.webbitserver.BaseWebSocketHandler;
import org.webbitserver.WebSocketConnection;
import org.webbitserver.WebServer;



public class WebSocketHandler extends BaseWebSocketHandler {

    private WebServer _wss;
    private static final String TAG = "WEB SOCKET SERVER";

    public WebSocketHandler(WebServer wss) {
        System.out.println("lauching web server");
        _wss = wss;
    }

    @Override
    public void onOpen(WebSocketConnection connection) throws Exception {
        System.out.println("Opening WS connection " + connection.toString());
        //Log.i(TAG,"Opening WS connection " + connection.toString());
    }

    @Override
    public void onClose(WebSocketConnection connection) throws Exception {
        System.out.println("web socket server has ended connection " + connection.toString());
    }

    public void onMessage(WebSocketConnection connection, String message) {
        System.out.println("web socket server has received a message : " + message);
        //Log.w(TAG,"web socket server has received a message : " + message);
    }

}