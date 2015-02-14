package context;

import knx.GroupEvent;
import knx.SensorType;
import org.codehaus.jackson.JsonNode;
import org.kevoree.modeling.api.Callback;
import org.kevoree.modeling.api.KObject;
import org.webbitserver.WebServers;
import org.webbitserver.WebSocketConnection;
import tsen.*;
import webSocketServer.WebSocketHandler;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;


public class Context {

    private TsenUniverse _universe;
    private TsenDimension _dim0;

    private org.webbitserver.WebServer _wss;
    private Map<String,WebSocketConnection> _connections;

    public static final int TSEN_WS_PORT = 8081;

    private Thread _bufferReader;
    private boolean _isProcessingBuffer;

    private Queue<GroupEvent> _eventBuffer;

    public Context (TsenUniverse universe){
        _universe = universe ;
        _dim0 = _universe.dimension(0L);
        _eventBuffer = new ConcurrentLinkedQueue<>();

        //createBufferReader();
        _wss = WebServers.createWebServer(TSEN_WS_PORT).add("/data",new WebSocketHandler(_wss, this));
        _wss.start();
        System.out.println("Web socket server running at : " + _wss.getUri());


        //startContext();
    }

    public void startContext(){
        _isProcessingBuffer = true ;
        _bufferReader.start();
    }

    public void stopContext(){
        _isProcessingBuffer = false;
        _wss.stop();

    }

    public void createBufferReader(){

        _bufferReader = new Thread(new Runnable() {
            @Override
            public void run() {
                while(_isProcessingBuffer){


                    while(!_eventBuffer.isEmpty()){
                        System.out.println(_eventBuffer);
                        GroupEvent grpEvt = _eventBuffer.poll();
                        grpEvt.addToContext(_dim0);
                    }
                }

                try {
                    Thread.sleep(30000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public TsenDimension getDimension(){
        return _dim0;
    }


    public void initSensors(JsonNode groups){

        String result = "";
        System.out.println("sensor initialisation ...");
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
                                outDoorTemperature.setAssociatedDPT((node.get("DPT").asText()));
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

    public boolean isRunning(){
        return _isProcessingBuffer;
    }

    public void setVote(String id, String vote, long ts, WebSocketConnection connection){

        TsenView view = _dim0.time(ts);

        view.select("/", new Callback<KObject[]>() {
            @Override
            public void on(KObject[] kObjects) {
                if(kObjects!=null && kObjects.length!=0){
                    Room room = (Room) kObjects[0];

                    room.eachMember(new Callback<User[]>() {
                        @Override
                        public void on(User[] users) {

                            boolean findUser = false ;
                            for(User user : users){
                                if(user.getId().compareTo(id)==0){
                                    user.setVote(vote);
                                    findUser = true;
                                }
                            }

                            if(findUser){
                                connection.send("200");
                            }else{
                                connection.send("404");
                            }
                        }
                    });

                }
            }
        });
    }









}
