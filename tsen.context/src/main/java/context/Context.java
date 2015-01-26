package context;

import knx.GroupEvent;
import knx.KnxManager;
import knx.SensorType;
import knx.Utility;
import org.codehaus.jackson.JsonNode;
import org.kevoree.modeling.api.Callback;
import org.kevoree.modeling.api.KObject;
import tsen.*;



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
                            case SensorType.CO2_SENSOR : CO2 co2 = view.createCO2();
                                co2.setName(SensorType.CO2_SENSOR + (room.sizeOfAirQualitySensor() + 1));
                                co2.setGroup(node.get("address").asText());
                                co2.setDPT(node.get("DPT").asText());
                                co2.setScale(SensorType.AIR_QUALITY_SENSOR_SCALE);
                                room.addAirQualitySensor(co2);
                                break;
                            case SensorType.INDOOR_HUMIDITY : IndoorHumidity humidity = view.createIndoorHumidity();
                                humidity.setName(SensorType.INDOOR_HUMIDITY);
                                humidity.setGroup(node.get("address").asText());
                                humidity.setDPT(node.get("DPT").asText());
                                humidity.setScale(SensorType.HUMIDITY_SCALE);
                                room.addIndoorHumiditySensor(humidity);
                                break;
                            case SensorType.OUTDOOR_HUMIDITY : OutdoorHumidity outdoorHumidity = view.createOutdoorHumidity();
                                outdoorHumidity.setName(SensorType.INDOOR_HUMIDITY);
                                outdoorHumidity.setGroup(node.get("address").asText());
                                outdoorHumidity.setDPT(node.get("DPT").asText());
                                outdoorHumidity.setScale(SensorType.HUMIDITY_SCALE);
                                room.addOutdoorHumiditySensor(outdoorHumidity);
                                break;
                            case SensorType.INDOOR_TEMPERATURE : IndoorTemperature indoorTemperature = view.createIndoorTemperature();
                                indoorTemperature.setName(SensorType.INDOOR_TEMPERATURE);
                                indoorTemperature.setGroup(node.get("address").asText());
                                indoorTemperature.setDPT(node.get("DPT").asText());
                                indoorTemperature.setScale(SensorType.TEMPERATURE_SCALE);
                                room.addIndoorTemperatureSensor(indoorTemperature);
                                break;
                            case SensorType.OUTDOOR_TEMPERATURE :  OutDoorTemperature outDoorTemperature = view.createOutDoorTemperature();
                                outDoorTemperature.setName(SensorType.OUTDOOR_TEMPERATURE);
                                outDoorTemperature.setGroup(node.get("address").asText());
                                outDoorTemperature.setDPT((node.get("address").asText()));
                                outDoorTemperature.setScale(SensorType.TEMPERATURE_SCALE);
                                room.addOutDoorTemperatureSensor(outDoorTemperature);
                                break;
                            case SensorType.VALVE : Heater heater = view.createHeater();
                                heater.setScale(SensorType.VALVE_SCALE);
                                heater.setGroup(node.get("address").asText());
                                heater.setDPT(node.get("DPT").asText());
                                heater.setName(SensorType.VALVE);
                                room.addValves(heater);
                                break;
                            case SensorType.OUTDOOR_BRIGHTNESS : OutdoorBrightness outdoorBrightness = view.createOutdoorBrightness();
                                outdoorBrightness.setName(SensorType.OUTDOOR_BRIGHTNESS);
                                outdoorBrightness.setGroup(node.get("address").asText());
                                outdoorBrightness.setDPT(node.get("DPT").asText());
                                outdoorBrightness.setScale(SensorType.OUTDOOR_BRIGHTNESS_SCALE);
                                room.addLuxSensors(outdoorBrightness);
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
