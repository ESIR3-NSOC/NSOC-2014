package knx;

import org.codehaus.jackson.JsonNode;
import org.kevoree.modeling.api.Callback;
import org.kevoree.modeling.api.KObject;
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
        TsenView view = dimension.time(System.currentTimeMillis());

        String sensorType = _groupInfo.get("sensorsType").asText();
        String addressGroup = _groupInfo.get("sensorsType").asText();

        switch(sensorType){

        }

        view.select("/", new Callback<KObject[]>() {
            @Override
            public void on(KObject[] kObjects) {

            }
        });
    }

}
