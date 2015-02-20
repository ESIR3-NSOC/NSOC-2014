package context;

import knx.SensorType;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.kevoree.modeling.api.Callback;
import org.kevoree.modeling.api.KObject;
import org.webbitserver.WebSocketConnection;
import tsen.*;

import java.io.*;
import java.util.*;

public class Context {

    private TsenUniverse _universe;
    private TsenDimension _dim0;

    private double _indooTempValue;


    public Context (TsenUniverse universe){
        _universe = universe ;
        _dim0 = _universe.dimension(0L);

        initSensors(importGroup());

        _indooTempValue = getInstantIndoorTemp();
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

    public double getInstantIndoorTemp(){
        TsenView view = _dim0.time(System.currentTimeMillis());

        view.select("/", new Callback<KObject[]>() {
            @Override
            public void on(KObject[] kObjects) {
                if(kObjects!=null && kObjects.length!=0){

                    Room room = (Room) kObjects[0];

                    room.eachMeasurement(new Callback<Sensor[]>() {
                        @Override
                        public void on(Sensor[] sensors) {
                            for (Sensor sensor : sensors){
                                if(sensor.getSensorType().compareTo(SensorType.INDOOR_TEMPERATURE)==0) {
                                   setIndoorTemp(Double.parseDouble(sensor.getValue()));
                                }
                            }
                        }
                    });
                }
            }
        });
    }

    private void setIndoorTemp(double t){
        _indooTempValue = t ;
    }

    public static JsonNode importGroup(){

        ObjectMapper mapper = new ObjectMapper();
        JsonNode node;
        StringBuilder AllConf = new StringBuilder();

        try {

            File groupConfiguration = new File("tsen.provider/src/main/resources/knxGroup.txt");
            InputStream read = new FileInputStream(groupConfiguration);
            InputStreamReader lecture = new InputStreamReader(read);
            BufferedReader br = new BufferedReader(lecture);
            String line ;
            while((line=br.readLine())!=null){
                AllConf.append(line);
            }
            br.close();


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try{
            node = mapper.readTree(AllConf.toString());
        }catch (Exception e){
            System.out.println("Could not import KNXGroup");
            node =  null;
        }
        return node;
    }










}
