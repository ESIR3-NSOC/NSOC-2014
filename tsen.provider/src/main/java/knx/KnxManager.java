package knx;

import org.codehaus.jackson.JsonNode;
import tuwien.auto.calimero.CloseEvent;
import tuwien.auto.calimero.FrameEvent;
import tuwien.auto.calimero.cemi.CEMILData;
import tuwien.auto.calimero.link.KNXLinkClosedException;
import tuwien.auto.calimero.link.KNXNetworkLinkIP;
import tuwien.auto.calimero.link.event.NetworkLinkListener;
import tuwien.auto.calimero.process.ProcessCommunicator;
import tuwien.auto.calimero.process.ProcessCommunicatorImpl;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.List;


public class KnxManager {

    private KNXNetworkLinkIP _netLinkIp ;
    private List<GroupEvent> _eventBuffer;
    private JsonNode _knxConf;

    boolean run ;

    private Thread _bufferReader;

    public KnxManager(){

        _knxConf = Utility.importGroup();
       
        //initConnection();
        //_eventBuffer = new LinkedList<>();
        //createKNXListener();




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
                System.out.println("srcadress " + arg0.getSource());
                System.out.println("sourceGroup" + ((CEMILData)arg0.getFrame()).getSource());
                System.out.println("targetadress " +((CEMILData)arg0.getFrame()).getDestination());
                System.out.println("value + " + arg0.getFrame());
                addFrameToBuffer(arg0);
            }

            public void linkClosed(CloseEvent arg0) {
                System.out.println("KNX link has been closed");
            }

        });
    }

    public  List<GroupEvent> getKNXFrameBuffer(){
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
                    _eventBuffer.add(new GroupEvent(frameEvent,n));
                }
            }
    }




}
