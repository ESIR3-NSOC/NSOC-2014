package fr.esir.nsoc.tsen.ade.http;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

public class HTTP_Requester {

	private final String USER_AGENT = "Mozilla/5.0";

	private String _adeServerUrl = "";
	private String _jSessionId = "";

	public HTTP_Requester(String adeServerUrl, String jSessionId) {
		this._adeServerUrl = adeServerUrl;
		this._jSessionId = jSessionId;
	}

	public HTTP_Response sendGet(String path, HashSet<HTTP_Parameter> parameters) {
		URL url;
		try {
			if (parameters != null && parameters.size() > 0) {
				StringBuilder sbParameters = new StringBuilder();
				Iterator<HTTP_Parameter> i = parameters.iterator();
				while(i.hasNext()) {
					HTTP_Parameter hp = i.next();
					sbParameters.append(sbParameters.length() == 0 ? "?" : "&");
					sbParameters.append(hp.getName() + "=" + hp.getValue());
				}
				url = new URL(_adeServerUrl + path + sbParameters);
			} else {
				url = new URL(_adeServerUrl + path);
			}

			HttpURLConnection con;
			con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");

			// request header
			con.setRequestProperty("User-Agent", USER_AGENT);
			con.setRequestProperty("Cookie", "JSESSIONID=" + _jSessionId
					+ ".plannings1");

			int code = con.getResponseCode();

			String content = "";
			if (code >= 200 && code < 400) {
				BufferedReader in;
				in = new BufferedReader(new InputStreamReader(
						con.getInputStream()));
				String inputLine;
				StringBuffer response = new StringBuffer();
				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
					response.append("\r\n");
				}
				in.close();
				content = response.toString();
			}

			return new HTTP_Response(_adeServerUrl + path, "GET", code, content, parameters);
		} catch (IOException e) {
			e.printStackTrace();
			return new HTTP_Response(_adeServerUrl + path, "GET");
		}

	}

	// HTTP POST request
	public HTTP_Response sendPost(String path, HashSet<HTTP_Parameter> parameters)
			throws Exception {
		URL url;
		try {
			url = new URL(_adeServerUrl + path);
			HttpsURLConnection con = (HttpsURLConnection) url.openConnection();

			// add reuqest header
			con.setRequestMethod("POST");

			con.setRequestProperty("User-Agent", USER_AGENT);
			con.setRequestProperty("Cookie", "JSESSIONID=" + _jSessionId
					+ ".plannings1");

			// Send post request
			con.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			if (parameters != null && parameters.size() > 0) {
				StringBuilder sbParameters = new StringBuilder();
				Iterator<HTTP_Parameter> i = parameters.iterator();
				while(i.hasNext()) {
					HTTP_Parameter hp = i.next();
					sbParameters.append(sbParameters.length() == 0 ? "" : "&");
					sbParameters.append(hp.getName() + "=" + hp.getValue());
				}
				wr.writeBytes(sbParameters.toString());
			}
			wr.flush();
			wr.close();

			int code = con.getResponseCode();

			String content = "";
			if (code >= 200 && code < 400) {
				BufferedReader in;
				in = new BufferedReader(new InputStreamReader(
						con.getInputStream()));
				String inputLine;
				StringBuffer response = new StringBuffer();
				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
					response.append("\r\n");
				}
				in.close();
				content = response.toString();
			}

			return new HTTP_Response(_adeServerUrl + path, "POST", code,
					content, parameters);
		} catch (IOException e) {
			e.printStackTrace();
			return new HTTP_Response(_adeServerUrl + path, "POST");
		}

	}

	public String getAdeServerUrl() {
		return _adeServerUrl;
	}
}
