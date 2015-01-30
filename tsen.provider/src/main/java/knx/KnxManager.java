package knx;

import org.codehaus.jackson.JsonNode;
import tuwien.auto.calimero.CloseEvent;
import tuwien.auto.calimero.FrameEvent;
import tuwien.auto.calimero.GroupAddress;
import tuwien.auto.calimero.cemi.CEMILData;
import tuwien.auto.calimero.exception.KNXException;
import tuwien.auto.calimero.exception.KNXFormatException;
import tuwien.auto.calimero.link.KNXLinkClosedException;
import tuwien.auto.calimero.link.KNXNetworkLinkIP;
import tuwien.auto.calimero.link.event.NetworkLinkListener;
import tuwien.auto.calimero.process.ProcessCommunicator;
import tuwien.auto.calimero.process.ProcessCommunicatorImpl;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;


public class KnxManager {

    private KNXNetworkLinkIP _netLinkIp ;
    private Queue<GroupEvent> _eventBuffer;
    private JsonNode _knxConf;

    boolean run ;

    private Thread _bufferReader;

    public KnxManager(){

        _knxConf = Utility.importGroup();

        initConnection();
        _eventBuffer = new ConcurrentLinkedQueue<>();
        createKNXListener();

    }

    private void initConnection (){

        KNXNetworkLinkIP netLinkIp = null;

        try {
            _netLinkIp = Utility.openKnxConnection(InetAddress.getByName(Reference.KNX_ADDRESS));
        } catch (UnknownHostException e) {
            System.out.println("Could not open KNX connection");
            e.printStackTrace();
        }

        if(netLinkIp != null ){
            try {
                ProcessCommunicator communicator = new ProcessCommunicatorImpl(netLinkIp);
                createKNXListener();
            } catch (KNXLinkClosedException e) {
                e.printStackTrace();
            }
        }else{

        }
    }

    public void CloseConnection(){
        _netLinkIp.close();
        System.out.println("KNX connection has been closed");
    }

    public void createKNXListener (){

        _netLinkIp.addLinkListener(new NetworkLinkListener(){

            public void confirmation(FrameEvent arg0) {

            }

            public void indication(FrameEvent arg0) {
                System.out.println("frame captured !");
                addFrameToBuffer(arg0);
            }

            public void linkClosed(CloseEvent arg0) {
                System.out.println("KNX link has been closed");
            }

        });
    }

    public  Queue<GroupEvent> getKNXFrameBuffer(){
        return _eventBuffer;
    }

    public JsonNode getGroups(){
        JsonNode groups ;
        System.out.println(_knxConf.toString());
            groups= _knxConf.get("groups");
        return groups;
    }

    private void addFrameToBuffer(FrameEvent frameEvent){

        String group = ((CEMILData)frameEvent.getFrame()).getDestination().toString();
        JsonNode groupsArray;
        // Looking for in config which sensors match received event

            groupsArray = _knxConf.get("groups");
            for(JsonNode n : groupsArray) {
                if (group.compareTo(n.get("address").asText()) == 0) {
                    GroupEvent evt = new GroupEvent(frameEvent,n);
                    _eventBuffer.add(evt);
                }
            }
    }

    public boolean isConnected(){
        return _netLinkIp.isOpen();
    }


    public void setVanne(int percent, String address) throws KNXException{

            ProcessCommunicator processCommunicator = new ProcessCommunicatorImpl(_netLinkIp);
            processCommunicator.write(new GroupAddress(address),percent);
    }



}
