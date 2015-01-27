package knx;

import org.codehaus.jackson.JsonNode;
import org.kevoree.modeling.api.Callback;
import org.kevoree.modeling.api.KObject;
import tsen.Sensor;
import tsen.TsenDimension;
import tsen.TsenView;
import tuwien.auto.calimero.FrameEvent;
import tuwien.auto.calimero.dptxlator.DPT;



/**
 * Created by mathi_000 on 23/01/2015.
 */
public class GroupEvent  {

    private FrameEvent _frameEvent;
    private DPT _dpt;

    private JsonNode _groupInfo;

    private long _ts;

    public GroupEvent (FrameEvent evt , JsonNode node){
        _frameEvent = evt;
        _ts = System.currentTimeMillis();
        _groupInfo = node;
    }

    public String getDPTType(){
        return _groupInfo.get("DPT").asText();
    }

    public DPT getDPT(){
        return _dpt;
    }


    public void addToContext(TsenDimension dimension){
        TsenView view = dimension.time(_ts);

        String sensorType = _groupInfo.get("sensorsType").asText();
        String addressGroup = _groupInfo.get("address").asText();

        view.select("/Measurement[groupAddress="+addressGroup+"]", new Callback<KObject[]>() {
            @Override
            public void on(KObject[] kObjects) {
                if(kObjects!=null && kObjects.length!=0){
                    if(kObjects.length==1){
                        Sensor sensor = (Sensor)kObjects[0];
                        sensor.setValue(0D);
                    }else{
                        System.out.println("Warning there is more than one instance of Sensor with group address :" + addressGroup);
                        for(KObject kObject : kObjects){
                            System.out.println(kObject.toJSON());
                            Sensor sensor = (Sensor)kObject;
                            sensor.setValue(0D);
                        }
                    }

                }else{
                    System.out.println("Could not update value from frame : ");
                    System.out.println(_frameEvent);
                    System.out.println("group address :" + addressGroup + "does not exist in context");
                }
            }
        });
    }

}
