package com.esir.nsoc.ade.http;

public class HTTP_Response {
	
	private boolean ok = false;
	private String url = "";
	private String method = "";
	private int code = 0;
	private String content = "";
	

	public HTTP_Response(String url, String method, int code, String content) {
		super();
		ok = true;
		this.url = url;
		this.method = method;
		this.code = code;
		this.content = content;
	}
	
	public HTTP_Response(String url, String method) {
		super();
		ok = false;
		this.url = url;
		this.method = method;
	}

	
	public boolean isOk() {
		return ok;
	}

	
	public String getUrl() {
		return url;
	}


	public String getMethod() {
		return method;
	}


	public int getCode() {
		return code;
	}


	public String getContent() {
		return content;
	}
}
