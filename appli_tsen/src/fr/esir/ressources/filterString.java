package fr.esir.ressources;

public class FilterString {

    public static final int STATE_DISCONNECTED = 0;
    public static final int STATE_CONNECTING = 1;
    public static final int STATE_CONNECTED = 2;

    public final static String ACTION_CONTEXT_CONNECTED =
            "fr.esir.services.Context_service.ACTION_GATT_CONNECTED";
    public final static String ACTION_CONTEXT_DISCONNECTED =
            "fr.esir.services.Context_service.ACTION_GATT_DISCONNECTED";
    public final static String CONTEXT_EXTRA_DATA =
            "fr.esir.services.Context_service.EXTRA_DATA";

    public final static String ACTION_REGULATION_CONNECTED =
            "fr.esir.services.Regulation_service.ACTION_GATT_CONNECTED";
    public final static String ACTION_REGULATION_DISCONNECTED =
            "fr.esir.services.Regulation_service.ACTION_GATT_DISCONNECTED";
    public final static String REGULATION_EXTRA_DATA =
            "fr.esir.services.Regulation_service.EXTRA_DATA";

    public final static String ACTION_OEP_CONNECTED =
            "fr.esir.services.Oep_service.ACTION_GATT_CONNECTED";
    public final static String ACTION_OEP_DISCONNECTED =
            "fr.esir.services.Oep_service.ACTION_GATT_DISCONNECTED";
    public final static String OEP_EXTRA_DATA =
            "fr.esir.services.Oep_service.EXTRA_DATA";

    public final static String ACTION_KNX_CONNECTED =
            "fr.esir.services.Oep_service.ACTION_GATT_CONNECTED";
    public final static String ACTION_KNX_DISCONNECTED =
            "fr.esir.services.Oep_service.ACTION_GATT_DISCONNECTED";
    public final static String KNX_EXTRA_DATA =
            "fr.esir.services.Oep_service.EXTRA_DATA";

}
