package fr.esir.services;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.StrictMode;
import android.util.Log;
import com.example.esir.nsoc2014.tsen.lob.objects.DatesInterval;
import context.Context;
import fr.esir.context.dataPackage.EnvironmentData;
import fr.esir.context.dataPackage.StudentData;
import fr.esir.resources.FilterString;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.kevoree.modeling.api.Callback;
import org.kevoree.modeling.api.KObject;
import tsen.*;

import fr.esir.knx.SensorType;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Context_service extends Service {

    private final static String TAG = Context_service.class.getSimpleName();
    private final IBinder mBinder = new LocalBinder();
    private Context _ctx;
    private final BroadcastReceiver mServicesUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(android.content.Context context, Intent intent) {
            final String action = intent.getAction();
            Bundle bundle;
            switch (action) {
                case FilterString.OEP_DATA_STUDENTS_OF_DAY:
                    Log.i(TAG, "reception students ok");
                    bundle = intent.getBundleExtra("Data");
                    if (bundle != null) {
                        HashMap<Time, List<DatesInterval>> map = (HashMap<Time, List<DatesInterval>>) bundle.getSerializable("HashMap");
                        for (Map.Entry<Time, List<DatesInterval>> entry : map.entrySet()) {
                            Log.w(TAG, entry.getKey() + "");
                            for (DatesInterval entryDi : entry.getValue()) {
                                Log.w(TAG, entryDi.getId() + " will be in classroom " + entryDi.getLesson()
                                        + ". His consigne must be " + entryDi.getConsigne() + " °C");
                                addStudent(entryDi.getStartDate().getTime(), entryDi.getEndDate().getTime(), entryDi.getId(), entryDi.getConsigne(), entryDi.getLesson());
                            }
                        }
                    }
                    break;
                case FilterString.RECEIVE_DATA_KNX:
                    Log.i(TAG, "Updating value in context");
                    String value = intent.getStringExtra("DATA");
                    String address = intent.getStringExtra("ADDRESS");

                    TsenView view = _ctx.getDimension().time(System.currentTimeMillis());

                    view.select("/", new Callback<KObject[]>() {
                        @Override
                        public void on(KObject[] kObjects) {
                            if (kObjects != null && kObjects.length != 0) {
                                Room room = (Room) kObjects[0];

                                room.eachMeasurement(new Callback<Sensor[]>() {
                                    @Override
                                    public void on(Sensor[] sensors) {
                                        for (Sensor sensor : sensors) {
                                            if (sensor.getGroupAddress().compareTo(address) == 0) {
                                                sensor.setValue(value);
                                                Log.i(TAG, "updating context with " + sensor.getSensorType() + " : " + sensor.getValue());
                                            }
                                        }
                                    }
                                });
                            }
                        }
                    });

                    break;

            }

        }


    };

    private static IntentFilter makeServicesUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        //students of the day, coming from oep
        intentFilter.addAction((FilterString.OEP_DATA_STUDENTS_OF_DAY));
        intentFilter.addAction(FilterString.RECEIVE_DATA_KNX);

        return intentFilter;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        unregisterReceiver(mServicesUpdateReceiver);
        return super.onUnbind(intent);
    }

    public boolean initialize() {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        InputStream file = null;
        try {
            file = getAssets().open("knxGroup.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
        _ctx = new Context(new TsenUniverse(), file);
        registerReceiver(mServicesUpdateReceiver, makeServicesUpdateIntentFilter());

        return true;
    }

    public void broadcastUpdate(String action, String data) {
        final Intent intent = new Intent(action);
        intent.putExtra("VOTE", data);
        sendBroadcast(intent);
    }

    private void addStudent(long start, long end, String studentId, double consigne, String lesson) {
        TsenView startView = _ctx.getDimension().time(start);

        User user = startView.createUser();
        user.setId(studentId);
        user.setLesson(lesson);
        user.setTargetTemp(consigne);

        Log.i(TAG, "adding user" + user.toString()  + " between " + new Date(start) + " and " + new Date(end));


        startView.select("/", new Callback<KObject[]>() {
            @Override
            public void on(KObject[] kObjects) {
                if (kObjects != null && kObjects.length != 0) {
                    Room room = (Room) kObjects[0];
                    room.addMember(user);
                }
            }
        });
       TsenView endView = _ctx.getDimension().time(end);

        endView.select("/", new Callback<KObject[]>() {
            @Override
            public void on(KObject[] kObjects) {
                if (kObjects != null && kObjects.length != 0) {
                    Room room = (Room) kObjects[0];
                    room.removeMember(user);
                }
            }
        });

        _ctx.getDimension().save(new Callback<Throwable>() {
            @Override
            public void on(Throwable throwable) {
                if (throwable != null) {
                    Log.e(TAG, throwable.toString());
                }
            }
        });

        long lessonTime = end-5;

        TsenView lessonView = _ctx.getDimension().time(lessonTime);

        lessonView.select("/", new Callback<KObject[]>() {
            @Override
            public void on(KObject[] kObjects) {
                if (kObjects != null && kObjects.length != 0) {
                    Room room = (Room) kObjects[0];

                    room.eachMember(new Callback<User[]>() {
                        @Override
                        public void on(User[] users) {
                            for (User user : users) {
                                Log.d(TAG, "user " + user.getId() + " has been registered in this classroom between" + new Date(start) + " and" + new Date(end) + " => lesson = " + user.getLesson());
                            }
                        }
                    });
                }
            }
        });
    }

    public Context getContext() {
        return _ctx;
    }

    // get student data for time (in milliseconde) with 3h step
    public StudentData getStudentData(String studentId, long time) {
        StudentData sd = new StudentData(studentId);

        for (long i = System.currentTimeMillis() - time; i < System.currentTimeMillis(); i += 3 * 60 * 60) {
            final long CURRENT_TIMESTAMP = i;
            TsenView view = _ctx.getDimension().time(i);
            view.select("/", new Callback<KObject[]>() {
                @Override
                public void on(KObject[] kObjects) {
                    if (kObjects != null && kObjects.length != 0) {
                        Room room = (Room) kObjects[0];

                        room.eachMember(new Callback<User[]>() {
                            @Override
                            public void on(User[] users) {
                                for (User user : users) {
                                    if (user.getId().compareTo(sd.get_studentId()) == 0) {
                                        sd.addEnvironmentData(new EnvironmentData(time));
                                        sd.setCurrentVote(user.getVote());
                                        sd.setTargetTemp(user.getTargetTemp());

                                        sd.addEnvironmentData(getEnvironmentData(CURRENT_TIMESTAMP));


                                    }
                                }
                            }
                        });
                    }
                }
            });
        }

        return sd;
    }

    public EnvironmentData getEnvironmentData(long ts) {
        EnvironmentData data = new EnvironmentData(ts);


        TsenView view = _ctx.getDimension().time(ts);

        view.select("/", new Callback<KObject[]>() {
            @Override
            public void on(KObject[] kObjects) {
                if (kObjects != null && kObjects.length != 0) {
                    Room room = (Room) kObjects[0];
                    room.eachMeasurement(new Callback<Sensor[]>() {
                        @Override
                        public void on(Sensor[] sensors) {

                            for (Sensor s : sensors) {
                                switch (s.getSensorType()) {
                                    case SensorType.OUTDOOR_BRIGHTNESS:
                                        data.setOutdoorLum(Double.parseDouble(s.getValue().split(" ")[0]));
                                        break;
                                    case SensorType.OUTDOOR_HUMIDITY:
                                        data.setOutdoorHum(Double.parseDouble(s.getValue().split(" ")[0]));
                                        break;
                                    case SensorType.OUTDOOR_TEMPERATURE:
                                        data.setOutdoorTemp(Double.parseDouble(s.getValue().split(" ")[0]));
                                        break;
                                    case SensorType.INDOOR_TEMPERATURE:
                                        data.setIndoorTemp(Double.parseDouble(s.getValue().split(" ")[0]));
                                        break;
                                    case SensorType.CO2_SENSOR:
                                        data.setAirQuality(Double.parseDouble(s.getValue().split(" ")[0]));
                                        break;
                                    case SensorType.INDOOR_HUMIDITY:
                                        data.setIndoorHum(Double.parseDouble(s.getValue().split(" ")[0]));
                                        break;
                                    case SensorType.VALVE:
                                        data.setValve(Double.parseDouble(s.getValue().split(" ")[0]));
                                        break;
                                    case SensorType.INDOOR_BRIGHTNESS:
                                        data.setIndoorLum(Double.parseDouble(s.getValue().split(" ")[0]));
                                        break;
                                }
                            }
                        }
                    });
                }
            }
        });

        _ctx.getDimension().save(new Callback<Throwable>() {
            @Override
            public void on(Throwable throwable) {
                if (throwable != null) {
                    throwable.printStackTrace();
                } else {
                    System.out.println("updated value");
                }

            }
        });

        return data;
    }

    public void displayAll(long ts) {

        TsenView view = _ctx.getDimension().time(ts);
        Log.d(TAG, "displaying all context Data");

        view.select("/", new Callback<KObject[]>() {
            @Override
            public void on(KObject[] kObjects) {
                if (kObjects != null && kObjects.length != 0) {
                    Room room = (Room) kObjects[0];
                    room.eachMeasurement(new Callback<Sensor[]>() {
                        @Override
                        public void on(Sensor[] sensors) {
                            for (Sensor s : sensors) {
                                Log.d(TAG, s.toJSON().toString());
                            }
                        }
                    });

                    room.eachMember(new Callback<User[]>() {
                        @Override
                        public void on(User[] users) {
                            for (User user : users) {
                                Log.d(TAG, user.toJSON().toString());
                            }

                        }
                    });
                }
            }
        });

    }


    public class LocalBinder extends Binder {
        public Context_service getService() {
            return Context_service.this;
        }
    }

}
