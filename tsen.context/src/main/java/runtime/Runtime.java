package runtime;

import context.Context;
import org.kevoree.modeling.api.Callback;
import org.kevoree.modeling.api.KObject;
import tsen.IndoorHumidity;
import tsen.Room;
import tsen.TsenDimension;
import utilities.ContextHelper;

/**
 * Created by mathi_000 on 09/01/2015.
 */
public class Runtime {

    public static void main (String[] args){

        Context ctx = ContextHelper.initContext("Salle test");
        TsenDimension dim = ctx.getDimension();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        dim.time(System.currentTimeMillis()).select("/", new Callback<KObject[]>() {
            @Override
            public void on(KObject[] kObjects) {
                if(kObjects !=null && kObjects.length!=0){
                    for(KObject kObject : kObjects){
                        Room room  =(Room) kObject;
                        System.out.println(room.toString());

                        room.eachIndoorHumiditySensor(new Callback<IndoorHumidity[]>() {
                            @Override
                            public void on(IndoorHumidity[] indoorHumidities) {
                                for(IndoorHumidity hum : indoorHumidities)
                                System.out.println(hum.toJSON());
                            }
                        });
                    }


                }
            }
        });






    }
}
