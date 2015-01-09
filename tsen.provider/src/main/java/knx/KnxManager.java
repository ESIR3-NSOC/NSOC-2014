package knx;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import tuwien.auto.calimero.CloseEvent;
import tuwien.auto.calimero.FrameEvent;
import tuwien.auto.calimero.knxnetip.servicetype.SearchResponse;
import tuwien.auto.calimero.link.KNXNetworkLinkIP;
import tuwien.auto.calimero.link.event.NetworkLinkListener;

import java.net.InetAddress;
import java.util.LinkedList;
import java.util.List;


public class KnxManager {

    private InetAddress _knxIpAddress ;
    private static KNXNetworkLinkIP netLinkIP ;
    private static List<FrameEvent> _eventBuffer;

    private static Logger _logger = LoggerFactory.getLogger(KnxManager.class);


    public KnxManager(){

        SearchResponse response =  Utility.Discover();
        _knxIpAddress = response.getControlEndpoint().getAddress();

        netLinkIP = Utility.openKnxConnection(_knxIpAddress);

        _eventBuffer = new LinkedList<FrameEvent>();
    }
    public static void CloseConnection(){
        netLinkIP.close();
    }
    public static void createKNXListener (KNXNetworkLinkIP netLinkIp ){

        netLinkIp.addLinkListener(new NetworkLinkListener(){

            public void confirmation(FrameEvent arg0) {

            }

            public void indication(FrameEvent arg0) {
                _eventBuffer.add(arg0);
            }

            public void linkClosed(CloseEvent arg0) {
                _logger.error("KNX link has beeen closed");
            }

        });
    }
    public static List<FrameEvent> getKNXFrameBuffer(){
        return _eventBuffer;

    }
}
