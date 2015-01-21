package knx;

import org.jcp.xml.dsig.internal.dom.DOMXPathFilter2Transform;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import tuwien.auto.calimero.CloseEvent;
import tuwien.auto.calimero.FrameEvent;
import tuwien.auto.calimero.GroupAddress;
import tuwien.auto.calimero.cemi.CEMILData;
import tuwien.auto.calimero.dptxlator.DPT;
import tuwien.auto.calimero.exception.KNXTimeoutException;
import tuwien.auto.calimero.knxnetip.servicetype.SearchResponse;
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

    private InetAddress _knxIpAddress ;
    private static KNXNetworkLinkIP netLinkIP ;
    private static List<FrameEvent> _eventBuffer;




    public KnxManager(){

        _eventBuffer = new LinkedList<>();

        KNXNetworkLinkIP netLinkIp = null;

        try {
            netLinkIp = Utility.openKnxConnection(InetAddress.getByName("192.168.1.15"));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }



        if(netLinkIp != null ){
            try {
                ProcessCommunicator communicator = new ProcessCommunicatorImpl(netLinkIp);

                createKNXListener(netLinkIp);


                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        int i = 0;
                        while (i<10){

                            System.out.println(i);
                            try {
                                communicator.write(new GroupAddress(0,0,1),true);
                                System.out.println("pass to true");
                                Thread.sleep(1000);

                                communicator.write(new GroupAddress(0,0,1),false);
                                System.out.println("pass to false");

                                Thread.sleep(1000);
                            } catch (KNXTimeoutException e) {
                                e.printStackTrace();
                            } catch (KNXLinkClosedException e) {
                                e.printStackTrace();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }

                        i++;
                    }
                });

                try {
                    Thread.sleep(60000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


                netLinkIp.close();
            } catch (KNXLinkClosedException e) {
                e.printStackTrace();
            }

            System.out.println("buffer content : ");

            if(getKNXFrameBuffer().size()!=0){
                for(FrameEvent arg : getKNXFrameBuffer()){
                    System.out.println(arg);
                }
            }else{
                System.out.println("buffer empty");
            }

        }else{
            System.out.println("Could not open KNX connection");
        }

        /*SearchResponse response =  Utility.Discover();

        if(response != null){
            _knxIpAddress = response.getControlEndpoint().getAddress();
            //netLinkIP = Utility.openKnxConnection(_knxIpAddress);
            _eventBuffer = new LinkedList<FrameEvent>();
        }else{
            System.out.println("Could not found any address");
        }

        */
    }
    public static void CloseConnection(){
        netLinkIP.close();
    }
    public static void createKNXListener (KNXNetworkLinkIP netLinkIp ){

        netLinkIp.addLinkListener(new NetworkLinkListener(){

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
    public static List<FrameEvent> getKNXFrameBuffer(){
        return _eventBuffer;

    }

    private static void addFrameToBuffer(FrameEvent evnt){
        _eventBuffer.add(evnt);
    }
}
