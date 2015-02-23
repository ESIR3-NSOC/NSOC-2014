package fr.esir.knx;

import android.util.Log;
import fr.esir.services.Context_service;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import tuwien.auto.calimero.exception.KNXException;
import tuwien.auto.calimero.link.KNXNetworkLinkIP;
import tuwien.auto.calimero.link.medium.TPSettings;

import java.io.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

public class Utility {
    public static KNXNetworkLinkIP openKnxConnection(InetAddress destination) {
        KNXNetworkLinkIP netLinkIp = null;
        try {
            InetAddress source = InetAddress.getByName(Reference.HOST_ADDRESS);
            InetSocketAddress socketSource = new InetSocketAddress(source, 8060);
            System.out.println("address ip local :" + source.toString() + " on port 8060 ");
            InetSocketAddress socketDestination = new InetSocketAddress(destination, Reference.KNX_PORT);
            System.out.println("addresse ip maquette " + socketDestination.toString() + " on port " + Reference.KNX_PORT);
            netLinkIp = new KNXNetworkLinkIP(KNXNetworkLinkIP.TUNNEL, socketSource, socketDestination, false, new TPSettings(false));
        } catch (KNXException e) {
            System.out.println("KNX exception");
            e.printStackTrace();
        } catch (UnknownHostException e) {
            System.out.println("Unknown host connection");
            e.printStackTrace();
        }
        if(netLinkIp == null)
            Log.i("NETLINKIP", "JE SUIS NULL");
        return netLinkIp;
    }

    public static JsonNode importGroup(InputStream file) {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node;
        StringBuilder AllConf = new StringBuilder();
        try {
            InputStreamReader lecture = new InputStreamReader(file);
            BufferedReader br = new BufferedReader(lecture);
            String line;
            while ((line = br.readLine()) != null) {
                AllConf.append(line);
            }
            br.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            node = mapper.readTree(AllConf.toString());
        } catch (Exception e) {
            System.out.println("Could not import KNXGroup");
            node = null;
        }
        return node;
    }
}