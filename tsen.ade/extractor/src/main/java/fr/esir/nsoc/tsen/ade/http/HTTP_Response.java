package fr.esir.nsoc.tsen.ade.http;

import java.util.HashSet;

public class HTTP_Response {

	private String url = "";
	private String method = "";
	private int code = 0;
	private String content = "";
	private HashSet<HTTP_Parameter> parameters = null;

	public HTTP_Response(String url, String method, int code, String content,
			HashSet<HTTP_Parameter> parameters) {
		super();
		this.url = url;
		this.method = method;
		this.code = code;
		this.content = content;
		this.parameters = parameters;
	}

	public HTTP_Response(String url, String method) {
		super();
		this.url = url;
		this.method = method;
	}

	public HashSet<HTTP_Parameter> getParameters() {
		return parameters;
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
