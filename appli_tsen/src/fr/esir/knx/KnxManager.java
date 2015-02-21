package fr.esir.knx;

import org.codehaus.jackson.JsonNode;
import tuwien.auto.calimero.CloseEvent;
import tuwien.auto.calimero.FrameEvent;
import tuwien.auto.calimero.GroupAddress;
import tuwien.auto.calimero.KNXAddress;
import tuwien.auto.calimero.cemi.CEMILData;
import tuwien.auto.calimero.dptxlator.DPTXlator;
import tuwien.auto.calimero.dptxlator.DPTXlator2ByteFloat;
import tuwien.auto.calimero.dptxlator.DPTXlator8BitUnsigned;
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

    boolean run;
    /**
     * The object used to read and write from the KNX network
     */
    private ProcessCommunicator pc = null;
    private KNXNetworkLinkIP _netLinkIp;
    private Queue<GroupEvent> _eventBuffer;
    private JsonNode _knxConf;
    private DPTXlator _dpt = null;
    private Service_knx sk;
    private Thread _bufferReader;

    public KnxManager() {

        _knxConf = Utility.importGroup();

        //initConnection();
        _eventBuffer = new ConcurrentLinkedQueue<>();
        //createKNXListener();

    }

    public boolean initConnection() {

        KNXNetworkLinkIP netLinkIp = null;

        try {
            _netLinkIp = Utility.openKnxConnection(InetAddress.getByName(Reference.KNX_ADDRESS));
        } catch (UnknownHostException e) {
            System.out.println("Could not open KNX connection");
            e.printStackTrace();
        }

        if (netLinkIp != null) {
            try {
                pc = new ProcessCommunicatorImpl(netLinkIp);
                createKNXListener();
                return true;
            } catch (KNXLinkClosedException e) {
                e.printStackTrace();
                return false;
            }
        } else {
            return false;
        }
    }

    public void CloseConnection() {
        _netLinkIp.close();
        System.out.println("KNX connection has been closed");
    }

    public void createKNXListener() {

        _netLinkIp.addLinkListener(new NetworkLinkListener() {

            public void confirmation(FrameEvent arg0) {

            }

            public void indication(FrameEvent arg0) {
                System.out.println("frame captured !");
                //addFrameToBuffer(arg0);
                KNXAddress addDest = ((tuwien.auto.calimero.cemi.CEMILData) arg0.getFrame()).getDestination();
                System.out.println("srcadress " + arg0.getSource());
                System.out.println("targetadress " + addDest);
                System.out.println("CEMILData " + ((tuwien.auto.calimero.cemi.CEMILData) arg0.getFrame()).toString());

                whatIsTheDptOfTheSensor(addDest.toString());
                _dpt.setData(arg0.getFrame().getPayload());
                displayData(addDest.toString(), _dpt.getAllValues()[1]);
            }

            public void linkClosed(CloseEvent arg0) {
                System.out.println("KNX link has been closed");
            }

        });
    }

    private DPTXlator whatIsTheDptOfTheSensor(String address) {
        switch (address) {
            case "0/0/4": //CO2
                try {
                    _dpt = new DPTXlator2ByteFloat(DPTXlator2ByteFloat.DPT_AIRQUALITY);
                } catch (KNXFormatException e) {
                    e.printStackTrace();
                }
                break;
            case "0/1/0": //outdoor temperature
                try {
                    _dpt = new DPTXlator2ByteFloat(DPTXlator2ByteFloat.DPT_TEMPERATURE);
                } catch (KNXFormatException e) {
                    e.printStackTrace();
                }
                break;
            case "0/1/1": //outdoor brightness
                try {
                    _dpt = new DPTXlator2ByteFloat(DPTXlator2ByteFloat.DPT_INTENSITY_OF_LIGHT);
                } catch (KNXFormatException e) {
                    e.printStackTrace();
                }
                break;
            case "0/0/5": //indoor humidity
                try {
                    _dpt = new DPTXlator8BitUnsigned(DPTXlator8BitUnsigned.DPT_PERCENT_U8);
                } catch (KNXFormatException e) {
                    e.printStackTrace();
                }
                break;
            case "0/1/2": //outdoor humidity
                try {
                    _dpt = new DPTXlator8BitUnsigned(DPTXlator8BitUnsigned.DPT_PERCENT_U8);
                } catch (KNXFormatException e) {
                    e.printStackTrace();
                }
                break;
            case "0/0/3": //indoor temperature
                try {
                    _dpt = new DPTXlator2ByteFloat(DPTXlator2ByteFloat.DPT_TEMPERATURE);
                } catch (KNXFormatException e) {
                    e.printStackTrace();
                }
                break;
        }
        return _dpt;
    }

    private void displayData(String add, String data) {
        sk.sendDisplayData(add, data);
    }

    public Queue<GroupEvent> getKNXFrameBuffer() {
        return _eventBuffer;
    }

    public JsonNode getGroups() {
        JsonNode groups;
        System.out.println(_knxConf.toString());
        groups = _knxConf.get("groups");
        return groups;
    }

    private void addFrameToBuffer(FrameEvent frameEvent) {

        String group = ((CEMILData) frameEvent.getFrame()).getDestination().toString();
        JsonNode groupsArray;
        // Looking for in config which sensors match received event


    }

    public boolean isConnected() {
        return _netLinkIp.isOpen();
    }

    public ProcessCommunicator getPc() {
        return pc;
    }


    public void setVanne(int percent, String address) throws KNXException {

        ProcessCommunicator processCommunicator = new ProcessCommunicatorImpl(_netLinkIp);
        processCommunicator.write(new GroupAddress(address), percent);
    }


}
