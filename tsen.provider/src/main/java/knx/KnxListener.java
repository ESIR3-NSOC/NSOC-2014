package knx;

import com.sun.org.apache.bcel.internal.generic.TABLESWITCH;
import tuwien.auto.calimero.DetachEvent;
import tuwien.auto.calimero.process.ProcessEvent;
import tuwien.auto.calimero.process.ProcessListener;

import java.nio.ByteBuffer;

/**
 * Created by Nicolas on 05/02/2015.
 */
public class KnxListener implements ProcessListener{

    KnxManager manager;

    public KnxListener(KnxManager manager) {
        this.manager = manager;
    }

    @Override
    public void groupWrite(ProcessEvent processEvent) {

        String destAddr = processEvent.getDestination().toString();

        //Message value (state)
        byte[] asdu = processEvent.getASDU();
        //Check if state was correct
        double state = -100;
        if((asdu != null) && (asdu.length > 0)){
            if(asdu.length < 4){
                byte[] tab = new byte[8];
                System.arraycopy(asdu, 0, tab, tab.length - asdu.length, tab.length);
                for (int k = 0; k < tab.length - asdu.length; k++) {
                    tab[k] = 0; // we add the value 00 for each byte missing
                }
                state = ByteBuffer.wrap(tab).getDouble();
            }
        }

        System.out.println("Group address is " + destAddr + "and state is " + state);
    }

    @Override
    public void detached(DetachEvent detachEvent) {
        System.out.println("The KNXNetworkLink has been disconnected from the Process Monitor");
    }
}
