package context;

import knx.GroupEvent;
import knx.KnxManager;
import knx.SensorType;
import knx.Utility;
import org.codehaus.jackson.JsonNode;
import org.kevoree.modeling.api.Callback;
import org.kevoree.modeling.api.KObject;
import tsen.*;

import java.util.UUID;


public class Context {

    private TsenUniverse _universe;
    private TsenDimension _dim0;

    private Thread _bufferReader;
    private boolean _knxRun;



    private KnxManager _knxManager;


    public Context (TsenUniverse universe){
        _knxManager = new KnxManager();
        _universe = universe ;
        _dim0 = _universe.dimension(0L);
        initSensors();

        _knxRun = true ;

        _bufferReader = new Thread(new Runnable() {
            @Override
            public void run() {
                while(_knxRun){

                    for(GroupEvent ge : _knxManager.getKNXFrameBuffer()){
                        ge.addToContext(_dim0);
                        _knxManager.getKNXFrameBuffer().remove(ge);
                    }
                }
            }
        });
    }



    public TsenDimension getDimension(){
        return _dim0;
    }

    private void initSensors(){

        System.out.println("sensor initialisation ...");

        JsonNode groups = _knxManager.getGroups();

        for(JsonNode node : groups){


            TsenView view = _dim0.time(System.currentTimeMillis());

            view.select("/", new Callback<KObject[]>() {
                @Override
                public void on(KObject[] kObjects) {
                    if(kObjects!=null && kObjects.length!=0){
                        Room room = (Room) kObjects[0];
                        switch (node.get("sensorsType").asText()){
                            case SensorType.CO2_SENSOR : Sensor CO2sensor = view.createSensor();
                                CO2sensor.setSensorType(SensorType.CO2_SENSOR);
                                CO2sensor.setGroupAddress(node.get("address").asText());
                                CO2sensor.setAssociatedDPT(node.get("DPT").asText());
                                CO2sensor.setScale(SensorType.AIR_QUALITY_SENSOR_SCALE);
                                CO2sensor.setSensorId(UUID.randomUUID().toString());
                                room.addMeasurement(CO2sensor);
                                break;
                            case SensorType.INDOOR_HUMIDITY : Sensor humidity = view.createSensor();
                                humidity.setSensorType(SensorType.INDOOR_HUMIDITY);
                                humidity.setGroupAddress(node.get("address").asText());
                                humidity.setAssociatedDPT(node.get("DPT").asText());
                                humidity.setScale(SensorType.HUMIDITY_SCALE);
                                humidity.setSensorId(UUID.randomUUID().toString());
                                room.addMeasurement(humidity);
                                break;
                            case SensorType.OUTDOOR_HUMIDITY : Sensor outdoorHumidity = view.createSensor();
                                outdoorHumidity.setSensorType(SensorType.INDOOR_HUMIDITY);
                                outdoorHumidity.setGroupAddress(node.get("address").asText());
                                outdoorHumidity.setAssociatedDPT(node.get("DPT").asText());
                                outdoorHumidity.setScale(SensorType.HUMIDITY_SCALE);
                                outdoorHumidity.setSensorId(UUID.randomUUID().toString());
                                room.addMeasurement(outdoorHumidity);
                                break;
                            case SensorType.INDOOR_TEMPERATURE : Sensor indoorTemperature = view.createSensor();
                                indoorTemperature.setSensorType(SensorType.INDOOR_TEMPERATURE);
                                indoorTemperature.setGroupAddress(node.get("address").asText());
                                indoorTemperature.setAssociatedDPT(node.get("DPT").asText());
                                indoorTemperature.setScale(SensorType.TEMPERATURE_SCALE);
                                indoorTemperature.setSensorId(UUID.randomUUID().toString());
                                room.addMeasurement(indoorTemperature);
                                break;
                            case SensorType.OUTDOOR_TEMPERATURE : Sensor outDoorTemperature = view.createSensor();
                                outDoorTemperature.setSensorType(SensorType.OUTDOOR_TEMPERATURE);
                                outDoorTemperature.setGroupAddress(node.get("address").asText());
                                outDoorTemperature.setAssociatedDPT((node.get("address").asText()));
                                outDoorTemperature.setScale(SensorType.TEMPERATURE_SCALE);
                                outDoorTemperature.setSensorId(UUID.randomUUID().toString());
                                room.addMeasurement(outDoorTemperature);
                                break;
                            case SensorType.VALVE : Sensor heater = view.createSensor();
                                heater.setScale(SensorType.VALVE_SCALE);
                                heater.setGroupAddress(node.get("address").asText());
                                heater.setAssociatedDPT(node.get("DPT").asText());
                                heater.setSensorType(SensorType.VALVE);
                                heater.setSensorId(UUID.randomUUID().toString());
                                room.addMeasurement(heater);
                                break;
                            case SensorType.OUTDOOR_BRIGHTNESS : Sensor outdoorBrightness = view.createSensor();
                                outdoorBrightness.setSensorType(SensorType.OUTDOOR_BRIGHTNESS);
                                outdoorBrightness.setGroupAddress(node.get("address").asText());
                                outdoorBrightness.setAssociatedDPT(node.get("DPT").asText());
                                outdoorBrightness.setScale(SensorType.OUTDOOR_BRIGHTNESS_SCALE);
                                outdoorBrightness.setSensorId(UUID.randomUUID().toString());
                                room.addMeasurement(outdoorBrightness);
                                break;
                            default :
                                System.out.println("Configuration could not be loaded : " + node.get("sensorsType").asText() + " does not exist");
                                break;
                        }



                    }

                }
            });
        }
        _dim0.save(new Callback<Throwable>() {
            @Override
            public void on(Throwable throwable) {
                if(throwable!=null){
                    throwable.printStackTrace();
                }else{
                    System.out.println("sensors initialisation complete !");
                }

            }
        });

    }




}
