package knx;

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
            Discoverer discoverer = new Discoverer (Reference.KNX_PORT, true);
            discoverer.startSearch(Reference.DISCOVERER_TIMEOUT, true);

            while(discoverer.isSearching()){
                Thread.sleep(100);
            }

            for(SearchResponse sr : discoverer.getSearchResponses()){
                return sr;
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

            InetAddress source = InetAddress.getLocalHost();
            InetSocketAddress socketSource = new InetSocketAddress(source,8000);


            InetSocketAddress socketDestination = new InetSocketAddress(destination,8000);

            netLinkIp = new KNXNetworkLinkIP(KNXNetworkLinkIP.TUNNEL,socketSource,socketDestination,false, new TPSettings(false));
        } catch (KNXException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
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
