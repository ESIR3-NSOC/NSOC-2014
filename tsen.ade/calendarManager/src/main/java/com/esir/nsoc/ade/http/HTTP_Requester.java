package com.esir.nsoc.ade.http;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class HTTP_Requester {

	private final String USER_AGENT = "Mozilla/5.0";
	
	private String _adeServerUrl = "";
	
	public HTTP_Requester(String adeServerUrl){
		this._adeServerUrl = adeServerUrl;
	}

	public HTTP_Response sendGet(String path) {
		URL url;
		try {
			url = new URL(_adeServerUrl + path);
			HttpURLConnection con;
			con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			
			// request header		
			con.setRequestProperty("User-Agent", USER_AGENT);
			
			int code = con.getResponseCode();
			
			String content = "";
			if (code >= 200 && code < 400)
			{
				BufferedReader in;
				in = new BufferedReader(new InputStreamReader(con.getInputStream()));
				String inputLine;
				StringBuffer response = new StringBuffer();
				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
					response.append("\r\n");
				}
				in.close();
				content = response.toString();
			}
			
			return new HTTP_Response(_adeServerUrl + path, "GET", code, content);
		} catch (IOException  e) {
			e.printStackTrace();
			return new HTTP_Response(_adeServerUrl + path, "GET");
		}

	}
	
	// HTTP POST request
		public HTTP_Response sendPost(String path, String parameters) throws Exception {
			URL url;
			try {
				url = new URL(_adeServerUrl + path);
				HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
		 
				//add reuqest header
				con.setRequestMethod("POST");
				
				con.setRequestProperty("User-Agent", USER_AGENT);
		 
				// Send post request
				con.setDoOutput(true);
				DataOutputStream wr = new DataOutputStream(con.getOutputStream());
				wr.writeBytes(parameters);
				wr.flush();
				wr.close();
				
				
				int code = con.getResponseCode();
				
				String content = "";
				if (code >= 200 && code < 400)
				{
					BufferedReader in;
					in = new BufferedReader(new InputStreamReader(con.getInputStream()));
					String inputLine;
					StringBuffer response = new StringBuffer();
					while ((inputLine = in.readLine()) != null) {
						response.append(inputLine);
					}
					in.close();
					content = response.toString();
				}
				
				return new HTTP_Response(_adeServerUrl + path, "POST", code, content);
			} catch (IOException  e) {
				e.printStackTrace();
				return new HTTP_Response(_adeServerUrl + path, "POST");
			}
	 
		}

	public String getAdeServerUrl() {
		return _adeServerUrl;
	}
}
