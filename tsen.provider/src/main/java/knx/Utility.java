package knx;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import tuwien.auto.calimero.KNXListener;
import tuwien.auto.calimero.exception.KNXException;
import tuwien.auto.calimero.knxnetip.Discoverer;
import tuwien.auto.calimero.knxnetip.servicetype.SearchResponse;
import tuwien.auto.calimero.link.KNXNetworkLinkIP;
import tuwien.auto.calimero.link.medium.TPSettings;

import java.io.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;


public class Utility {

    public static SearchResponse Discover (){

        try {
            System.out.println("running discover...");
            Discoverer discoverer = new Discoverer (Reference.KNX_PORT, true);
            discoverer.startSearch(Reference.DISCOVERER_TIMEOUT, false);

            if(!discoverer.isSearching()){
                System.out.println("not searching");
            }else{
                System.out.println("init search");
            }

            while(discoverer.isSearching()){
                System.out.println("searching");
                Thread.sleep(100);
            }

            if(discoverer.getSearchResponses().length!=0){
                for(SearchResponse sr : discoverer.getSearchResponses()){
                    if(sr!=null){
                        System.out.println("Adresse trouvée : " + sr.getControlEndpoint().getAddress());
                        return sr;
                    }

                }
            }else{
                System.out.println("No address found !");
            }



        } catch (KNXException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static KNXNetworkLinkIP openKnxConnection (InetAddress destination){

        KNXNetworkLinkIP netLinkIp = null;
        try {

            InetAddress source = InetAddress.getByName("192.168.1.12");
            InetSocketAddress socketSource = new InetSocketAddress(source,8060);
            System.out.println("address ip local :" + source.toString() + " on port 8060 ");


            InetSocketAddress socketDestination = new InetSocketAddress(destination,Reference.KNX_PORT);
            System.out.println("addresse ip maquette " +socketDestination.toString() +  " on port " + Reference.KNX_PORT);

            netLinkIp = new KNXNetworkLinkIP(KNXNetworkLinkIP.TUNNEL,socketSource,socketDestination,false, new TPSettings(false));
        } catch (KNXException e) {
            System.out.println("KNX exception");
            e.printStackTrace();
        } catch (UnknownHostException e) {
            System.out.println("Unknown host connection");
            e.printStackTrace();
        }


        return netLinkIp;
    }

    public static void closeConnection (KNXNetworkLinkIP knxLinkIp){
        knxLinkIp.close();
    }



    public static Map<String,String> importGroup(){

        Map<String,String> pairMap = new HashMap<String,String>();

        try {
            String line = "";
            File groupConfiguration = new File("tsen.provider/src/main/resources/knxGroup.txt");
            InputStream read = new FileInputStream(groupConfiguration);
            InputStreamReader lecture = new InputStreamReader(read);
            BufferedReader br = new BufferedReader(lecture);

            while((line=br.readLine())!=null){

                if(!line.startsWith("#")){
                    String [] pair = line.split(":");
                    pairMap.put(pair[0],pair[1]);

                }

            }

            br.close();



        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return pairMap;

    }

}
