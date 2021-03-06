package context;

import ContextUtility.ContextMethod;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.kevoree.modeling.api.Callback;
import org.kevoree.modeling.api.KObject;
import tsen.*;

import java.io.*;
import java.util.Date;
import java.util.UUID;

public class Context {

    private static InputStream file;
    private final String TAG = "ContextObject";
    private TsenUniverse _universe;
    private TsenDimension _dim0;


    public Context(TsenUniverse universe, InputStream file) {
        _universe = universe;
        _dim0 = _universe.dimension(0L);
        this.file = file;

        ContextMethod.createRoom(_dim0, "104");


        initSensors(importGroup());

    }

    public static JsonNode importGroup() {

        ObjectMapper mapper = new ObjectMapper();
        JsonNode node;
        StringBuilder AllConf = new StringBuilder();

        try {

            /*File groupConfiguration = new File("/resources/knxGroup.txt");
            if (groupConfiguration.exists()) {
                System.out.println("File exists");
            }*/
            //InputStream read = new FileInputStream(groupConfiguration);
            InputStreamReader lecture = new InputStreamReader(file);
            BufferedReader br = new BufferedReader(lecture);
            String line;
            while ((line = br.readLine()) != null) {
                AllConf.append(line);
            }
            br.close();


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            node = mapper.readTree(AllConf.toString());
        } catch (Exception e) {
            System.out.println("Could not import KNXGroup");
            node = null;
        }
        return node;
    }

    public TsenDimension getDimension() {
        return _dim0;
    }

    public void initSensors(JsonNode groups) {

        String result = "";
        System.out.println(groups.toString());
        System.out.println("sensor initialisation ...");
        for (JsonNode node : groups.get("groups")) {
            System.out.println("node : => " + node);
            TsenView view = _dim0.time(System.currentTimeMillis());
            view.select("/", new Callback<KObject[]>() {
                @Override
                public void on(KObject[] kObjects) {
                    if (kObjects != null && kObjects.length != 0) {
                        Room room = (Room) kObjects[0];
                        System.out.println("Sensor Type" + node.get("sensorsType").asText());
                        switch (node.get("sensorsType").asText()) {
                            case SensorType.CO2_SENSOR:
                                System.out.println("Adding C02 sensor");
                                Sensor CO2sensor = view.createSensor();
                                CO2sensor.setSensorType(SensorType.CO2_SENSOR);
                                CO2sensor.setGroupAddress(node.get("address").asText());
                                CO2sensor.setAssociatedDPT(node.get("DPT").asText());
                                CO2sensor.setScale(SensorType.AIR_QUALITY_SENSOR_SCALE);
                                CO2sensor.setSensorId(UUID.randomUUID().toString());
                                room.addMeasurement(CO2sensor);
                                break;
                            case SensorType.INDOOR_HUMIDITY:
                                System.out.println("Adding indoor humidity sensor");
                                Sensor humidity = view.createSensor();
                                humidity.setSensorType(SensorType.INDOOR_HUMIDITY);
                                humidity.setGroupAddress(node.get("address").asText());
                                humidity.setAssociatedDPT(node.get("DPT").asText());
                                humidity.setScale(SensorType.HUMIDITY_SCALE);
                                humidity.setSensorId(UUID.randomUUID().toString());
                                room.addMeasurement(humidity);
                                break;
                            case SensorType.OUTDOOR_HUMIDITY:
                                System.out.println("Adding outdoor humidity sensor");
                                Sensor outdoorHumidity = view.createSensor();
                                outdoorHumidity.setSensorType(SensorType.INDOOR_HUMIDITY);
                                outdoorHumidity.setGroupAddress(node.get("address").asText());
                                outdoorHumidity.setAssociatedDPT(node.get("DPT").asText());
                                outdoorHumidity.setScale(SensorType.HUMIDITY_SCALE);
                                outdoorHumidity.setSensorId(UUID.randomUUID().toString());
                                room.addMeasurement(outdoorHumidity);
                                break;
                            case SensorType.INDOOR_TEMPERATURE:
                                System.out.println("Adding indoor temperature sensor");
                                Sensor indoorTemperature = view.createSensor();
                                indoorTemperature.setSensorType(SensorType.INDOOR_TEMPERATURE);
                                indoorTemperature.setGroupAddress(node.get("address").asText());
                                indoorTemperature.setAssociatedDPT(node.get("DPT").asText());
                                indoorTemperature.setScale(SensorType.TEMPERATURE_SCALE);
                                indoorTemperature.setSensorId(UUID.randomUUID().toString());
                                room.addMeasurement(indoorTemperature);
                                break;
                            case SensorType.OUTDOOR_TEMPERATURE:
                                System.out.println("Adding outdoor temperature sensor");
                                Sensor outDoorTemperature = view.createSensor();
                                outDoorTemperature.setSensorType(SensorType.OUTDOOR_TEMPERATURE);
                                outDoorTemperature.setGroupAddress(node.get("address").asText());
                                outDoorTemperature.setAssociatedDPT((node.get("DPT").asText()));
                                outDoorTemperature.setScale(SensorType.TEMPERATURE_SCALE);
                                outDoorTemperature.setSensorId(UUID.randomUUID().toString());
                                room.addMeasurement(outDoorTemperature);
                                break;
                            case SensorType.VALVE:
                                System.out.println("Adding valve sensor");
                                Sensor heater = view.createSensor();
                                heater.setScale(SensorType.VALVE_SCALE);
                                heater.setGroupAddress(node.get("address").asText());
                                heater.setAssociatedDPT(node.get("DPT").asText());
                                heater.setSensorType(SensorType.VALVE);
                                heater.setSensorId(UUID.randomUUID().toString());
                                room.addMeasurement(heater);
                                break;
                            case SensorType.OUTDOOR_BRIGHTNESS:
                                System.out.println("Adding outdoor brightness sensor");
                                Sensor outdoorBrightness = view.createSensor();
                                outdoorBrightness.setSensorType(SensorType.OUTDOOR_BRIGHTNESS);
                                outdoorBrightness.setGroupAddress(node.get("address").asText());
                                outdoorBrightness.setAssociatedDPT(node.get("DPT").asText());
                                outdoorBrightness.setScale(SensorType.OUTDOOR_BRIGHTNESS_SCALE);
                                outdoorBrightness.setSensorId(UUID.randomUUID().toString());
                                room.addMeasurement(outdoorBrightness);
                                break;
                            default:
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
                if (throwable != null) {
                    throwable.printStackTrace();
                } else {
                    System.out.println("sensors initialisation complete !");
                }

            }
        });

    }

    public void setVote(String id, String vote, long ts) {

        TsenView view = _dim0.time(ts);
        System.out.println("ADDING VOTE : " + vote + " at " + new Date(ts));
        view.select("/", new Callback<KObject[]>() {
            @Override
            public void on(KObject[] kObjects) {
                if (kObjects != null && kObjects.length != 0) {
                    Room room = (Room) kObjects[0];

                    room.eachMember(new Callback<User[]>() {
                        @Override
                        public void on(User[] users) {

                            boolean findUser = false;
                            for (User user : users) {
                                if (user.getId().compareTo(id) == 0) {
                                    user.setVote(vote);
                                    findUser = true;
                                    System.out.println("USER AFTER VOTE " + user.toJSON());
                                }
                            }
                        }
                    });

                }
            }
        });
    }


}
