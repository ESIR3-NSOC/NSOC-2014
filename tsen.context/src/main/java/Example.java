
import org.kevoree.modeling.api.Callback;
import org.kevoree.modeling.api.KObject;
import org.kevoree.modeling.databases.leveldb.LevelDbDataBase;
import tsen.*;

import java.io.File;
import java.io.IOException;
import java.util.Date;

/**
 * Created by gregory.nain on 29/12/14.
 */
public class Example {

    /*public static void main(String[] args) {

        try {
            TsenUniverse universe = new TsenUniverse();
            universe.storage().setDataBase(new LevelDbDataBase(File.createTempFile("Mabase",".db").getAbsolutePath()));
            universe.connect(new Callback<Throwable>() {
                @Override
                public void on(Throwable throwable) {
                    if(throwable != null) {
                        throwable.printStackTrace();
                    }
                }
            });
            TsenDimension dim0 = universe.dimension(0);
            TsenView view = dim0.time(0L);

            view.select("/", new Callback<KObject[]>() {
                @Override
                public void on(KObject[] kObjects) {
                    if( kObjects == null || kObjects.length == 0) {
                        Room r = view.createRoom();
                        r.setName("Lipari");
                        view.setRoot(r, new Callback<Throwable>() {
                            @Override
                            public void on(Throwable throwable) {
                                if(throwable != null) {
                                    throwable.printStackTrace();
                                }
                            }
                        });
                        dim0.save(new Callback<Throwable>() {
                            @Override
                            public void on(Throwable throwable) {
                                if(throwable != null) {
                                    throwable.printStackTrace();
                                }
                            }
                        });
                    }
                }
            });


            TsenView viewNow = dim0.time(System.currentTimeMillis());
            viewNow.select("/",new Callback<KObject[]>() {
                @Override
                public void on(KObject[] kObjects) {
                    if( kObjects != null && kObjects.length > 0) {

                        Room lipari = (Room)kObjects[0];

                        Activity acti = viewNow.createActivity();
                        acti.setHour("");
                        lipari.addLesson(acti);

                        dim0.save(new Callback<Throwable>() {
                            @Override
                            public void on(Throwable throwable) {
                                if(throwable != null) {
                                    throwable.printStackTrace();
                                }
                            }
                        });
                    }
                }
            } );



        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

}
