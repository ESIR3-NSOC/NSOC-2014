package fr.esir.resources;

/**
 * Created by mathi_000 on 04/02/2015.
 */
public class FilterString {


    // Data send from OEP
    public final static String OEP_DATA_STUDENTS_OF_DAY =
            "Oep_service.STUDENTS_OF_DAY";

    public final static String OEP_DATA_CONSIGNES_OF_DAY =
            "Oep_service.CONSIGNES_OF_DAY";

    public final static String CONTEXT_INIT_SENSOR =
            "Context_service.INIT_SENSOR";

    //Data send from Context


    //Data send from Websocket

    public final static String WEBSOCKET_VOTE_UPDATE = "WebSocket.VoteUpdate";


    //Data send from KNX manager

    public final static String CONTEXT_UPDATE_VALUE =
            "Context.updateValue";

    public final static String CONTEXT_EXTRA_DATA =
            "fr.esir.services.Context_service.EXTRA_DATA";

    public final static String REGULATION_EXTRA_DATA =
            "fr.esir.services.Regulation_service.EXTRA_DATA";

    public final static String OEP_EXTRA_DATA =
            "fr.esir.services.Oep_service.EXTRA_DATA";

    public final static String RECEIVE_VOTE =
            "fr.esir.context.vote";



    public final static String RECEIVE_DATA_KNX =
            "fr.esir.services.Knx_service.DATA_TO_DISPLAY";
}
