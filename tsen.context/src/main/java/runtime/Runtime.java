package runtime;

import ContextUtility.ContextMethod;
import context.Context;
import org.kevoree.modeling.api.Callback;
import org.kevoree.modeling.api.KObject;
import tsen.Room;
import tsen.Sensor;
import tsen.TsenView;

/**
 * Created by mathi_000 on 09/01/2015.
 */
public class Runtime {

    /*public static void main (String[] args){

        Context ctx = ContextMethod.initContext("Salle 928");

        try {
            Thread.sleep(30000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ctx.getDimension().time(System.currentTimeMillis()).select("/", new Callback<KObject[]>() {
            @Override
            public void on(KObject[] kObjects) {

                Room room = null;
                for(KObject kObject : kObjects){
                    room = (Room)kObject;
                    System.out.println(room.toJSON());
                }

                if(room!=null){
                    room.eachMeasurement(new Callback<Sensor[]>() {
                        @Override
                        public void on(Sensor[] sensors) {
                            for(Sensor s : sensors){
                                System.out.println(s.toJSON());
                            }
                        }
                    });
                }

            }
        });

        ctx.stopContext();

    }*/
}
