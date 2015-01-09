package Utilities;

import knx.KnxManager;
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

/**
 * Created by mathi_000 on 29/12/2014.
 */
public class ContextInit {

    private Logger logger = LoggerFactory.getLogger(ContextInit.class);
    private static TsenUniverse _universe;
    private static TsenDimension _dim0;
    private KnxManager _knxManager;

    public static void initContext() {
        _universe = new TsenUniverse();
        _dim0 = _universe.dimension(0);
        connectToDataBase();
        createRoom();
        // _knxManager = new KnxManager();

    }

    public static void connectToDataBase() {

        try {
            _universe.storage().setDataBase(new LevelDbDataBase(File.createTempFile("Tsen", "db", new File(System.getProperty("java.io.tmpdir") + "/Tsen")).toString()));
            _universe.connect(new Callback<Throwable>() {
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

    public static void createRoom() {
        TsenView view = _dim0.time(0L);
        view.select("/", new Callback<KObject[]>() {
            @Override
            public void on(KObject[] kObjects) {
                if (kObjects == null || kObjects.length == 0) {
                    Room r = view.createRoom();
                    r.setName("Lipari");
                    view.setRoot(r, new Callback<Throwable>() {
                        @Override
                        public void on(Throwable throwable) {
                            if (throwable != null) {
                                throwable.printStackTrace();
                            }
                        }
                    });
                    _dim0.save(new Callback<Throwable>() {
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

    public TsenUniverse getUniverse(){
        return _universe;
    }

}
