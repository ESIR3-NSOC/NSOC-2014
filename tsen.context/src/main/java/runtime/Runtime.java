package runtime;

import ContextUtility.ContextMethod;
import context.Context;
import tsen.TsenDimension;

/**
 * Created by mathi_000 on 09/01/2015.
 */
public class Runtime {

    public static void main (String[] args){

        Context ctx = ContextMethod.initContext("Salle 928");
        TsenDimension dim = ctx.getDimension();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }








    }
}
