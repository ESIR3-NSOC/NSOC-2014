package fr.esir.nsoc.tsen.ade.http.parser;

import java.util.HashMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class CategoryParser {
	
	private String _html = "";
	
	public CategoryParser(String html) {
		super();
		this._html = html;
	}

	public HashMap<String, String> Parse()
	{
		Document doc = Jsoup.parse(_html);
		Elements list = doc.select("div.treeline");
		
		HashMap<String, String> hm = new HashMap<String, String>();
		
		for (Element e1 : list)
		{
			Element e2 = e1.select("a[href*=\"javascript:checkCategory\"]").first();
			hm.put(new String(e2.attr("href").split("'")[1]), new String(e2.text()));
		}
		return hm;
	}
}
