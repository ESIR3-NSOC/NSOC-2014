package fr.esir.knx;

import android.content.Context;
import android.content.SharedPreferences;
import fr.esir.maintasks.ConfigParams;
import tuwien.auto.calimero.knxnetip.KNXnetIPConnection;

import java.util.Calendar;

/**
 * Created by mathi_000 on 29/12/2014.
 */
public class Reference {
    //KNX part
    public static final int KNX_PORT = KNXnetIPConnection.IP_PORT;
    public static final int DISCOVERER_TIMEOUT = 30;
    public static final String KNX_ADDRESS = "192.168.1.100";
    public static final String HOST_ADDRESS = "192.168.1.168";
    //default values
    public static final String minTemp = "20";
    public static final String maxTemp = "25";
    public static final String idRoom = "1005";
    public static final String roomName = "104";
    public static final int END_OF_DAY_TEMPERATURE = 15;
    public static final int PAUSE_BTW_LESSONS_TEMPERATURE = 20;
    public static final long TIME_MIN_TO_CONSIDERE_A_LONG_PAUSE = 35 * 60000;
    //httpRequest
    public static final int TARGET_PORT = 80;
    public static final String TARGET_HOST = "tsen.uion.fr";
    public static final String TARGET_PROTOCOL = "http";
    static SharedPreferences pref = ConfigParams.context.getSharedPreferences("APPLI_TSEN", Context.MODE_PRIVATE);
    public static final String TARGET_REQUEST = "/tsen/vote?roomId=" + pref.getString("IDROOM", "1005") + "&delay=60000";

    public static long timeBefore() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_MONTH, 1);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        long howMany = c.getTimeInMillis() - System.currentTimeMillis();

        return howMany;
    }
}
