package ContextUtility;

import context.Context;
import org.kevoree.modeling.api.Callback;
import org.kevoree.modeling.api.KObject;
import org.kevoree.modeling.databases.leveldb.LevelDbDataBase;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tsen.Room;
import tsen.TsenDimension;
import tsen.TsenUniverse;
import tsen.TsenView;

import java.io.File;
import java.io.IOException;


public class ContextMethod {


    public static Context initContext(String roomName) {
        TsenUniverse universe = new TsenUniverse();
        //connectToDataBase(universe);
        createRoom(universe.dimension(0),roomName);
        return new Context(universe);
    }

    public static void connectToDataBase(TsenUniverse universe) {

        try {
            File database = File.createTempFile("database",".db",new File(System.getProperty("java.io.tmpdir") + "/tsen/context"));
            System.out.println("trying to connect to database at :" + database.getAbsolutePath());
            universe.storage().setDataBase(new LevelDbDataBase(database.getAbsolutePath()));
            universe.connect(new Callback<Throwable>() {
                @Override
                public void on(Throwable throwable) {
                    if (throwable != null) {
                        System.out.println("Connecting to Database...");
                        throwable.printStackTrace();
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void createRoom(TsenDimension dim0, String roomName) {
        TsenView view = dim0.time(0L);
        view.select("/", new Callback<KObject[]>() {
            @Override
            public void on(KObject[] kObjects) {
                if (kObjects == null || kObjects.length == 0) {
                    Room r = view.createRoom();
                    r.setName(roomName);
                    view.setRoot(r, new Callback<Throwable>() {
                        @Override
                        public void on(Throwable throwable) {
                            if (throwable != null) {
                                throwable.printStackTrace();
                            }
                        }
                    });
                    dim0.save(new Callback<Throwable>() {
                        @Override
                        public void on(Throwable throwable) {
                            if (throwable != null) {
                                throwable.printStackTrace();
                            }
                        }
                    });


                }
            }
        });
    }

}
