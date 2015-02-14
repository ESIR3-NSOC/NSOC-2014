package fr.esir.knx;

import org.codehaus.jackson.JsonNode;
import org.kevoree.modeling.api.Callback;
import org.kevoree.modeling.api.KObject;
import tsen.Room;
import tsen.Sensor;
import tsen.TsenDimension;
import tsen.TsenView;
import tuwien.auto.calimero.FrameEvent;
import tuwien.auto.calimero.dptxlator.DPTXlator;
import tuwien.auto.calimero.dptxlator.DPTXlator2ByteFloat;
import tuwien.auto.calimero.dptxlator.DPTXlator8BitUnsigned;
import tuwien.auto.calimero.exception.KNXFormatException;

import java.util.Date;

//import tsen.TsenDimension;


public class GroupEvent  {

    private FrameEvent _frameEvent;
    private DPTXlator _dpt;

    private JsonNode _groupInfo;

    private long _ts;

    public GroupEvent (FrameEvent evt , JsonNode node){
        _frameEvent = evt;
        _ts = System.currentTimeMillis();
        _groupInfo = node;
        try {
            switch(_groupInfo.get("DPT").asText()){
                case "9.001" :
                    _dpt = new DPTXlator2ByteFloat(DPTXlator2ByteFloat.DPT_TEMPERATURE);
                    break;
                case "9.008" :
                    _dpt = new DPTXlator2ByteFloat(DPTXlator2ByteFloat.DPT_AIRQUALITY);
                    break;
                case "5.001" :
                    _dpt = new DPTXlator8BitUnsigned(DPTXlator8BitUnsigned.DPT_PERCENT_U8);
                    break;
                case "9.004" :
                    _dpt = new DPTXlator2ByteFloat(DPTXlator2ByteFloat.DPT_INTENSITY_OF_LIGHT);
                    break;
                default :
                    System.out.println("DPT " + _groupInfo.get("DPT").asText() + " does not exist");
                    break;
            }
        }catch(KNXFormatException e){
            e.printStackTrace();
        }
    }

    public String getDPTType(){
        return _groupInfo.get("DPT").asText();
    }

    public DPTXlator getDPT(){
        return _dpt;
    }


   public void addToContext(TsenDimension dimension){

        TsenView view = dimension.time(_ts);


        String addressGroup = _groupInfo.get("address").asText();
        System.out.println("processing " + toString());

        view.select("/", new Callback<KObject[]>() {
            @Override
            public void on(KObject[] kObjects) {
                if(kObjects!=null &&kObjects.length!=0){
                    Room room = (Room) kObjects[0];
                    room.eachMeasurement(new Callback<Sensor[]>() {
                        @Override
                        public void on(Sensor[] sensors) {
                            boolean valueUpdated = false;
                            for(Sensor sensor : sensors){
                                if(sensor.getGroupAddress().compareTo(addressGroup)==0){
                                    _dpt.setData(_frameEvent.getFrame().getPayload());
                                    System.out.println("data from " + addressGroup + " : " + _dpt.getAllValues()[1]);
                                    sensor.setValue(_dpt.getAllValues()[1]);
                                    valueUpdated = true;
                                }

                            }

                            if(!valueUpdated){
                                System.out.println("this address does not exist in context : " + addressGroup);
                                for(KObject kObject : kObjects){
                                    System.out.println(kObject.toJSON());
                                    Sensor sensor = (Sensor)kObject;
                                    sensor.setValue("/");
                                }
                            }


                        }
                    });

                    dimension.save(new Callback<Throwable>() {
                        @Override
                        public void on(Throwable throwable) {
                            if(throwable!=null){
                                throwable.printStackTrace();
                            }
                        }
                    });

                }else{
                    System.out.println("Could not update value from frame : ");
                    System.out.println(_frameEvent);
                    System.out.println("group address :" + addressGroup + "does not exist in context");
                }
            }
        });
    }

    @Override
    public String toString() {
        return new Date(_ts).toString()  + " at address " + _groupInfo.get("address").asText();
    }


    public static String bytesToHex(byte[] bytes) {
        char[] hexArray = "0123456789ABCDEF".toCharArray();
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }
}
