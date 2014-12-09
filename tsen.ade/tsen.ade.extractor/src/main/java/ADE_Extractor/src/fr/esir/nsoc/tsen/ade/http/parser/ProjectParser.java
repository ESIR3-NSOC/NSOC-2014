package ADE_Extractor.src.fr.esir.nsoc.tsen.ade.http.parser;

import java.util.HashMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ProjectParser {
	
	private String _html = "";
	
	public ProjectParser(String html) {
		super();
		this._html = html;
	}

	public HashMap<Integer, String> Parse()
	{
		Document doc = Jsoup.parse(_html);
		Element list = doc.select("SELECT").first();
		Elements projects = list.select("option");

		HashMap<Integer, String> hm = new HashMap<Integer, String>();
		
		for (Element project : projects)
		{
			hm.put(Integer.parseInt(project.attr("value")), new String(project.text()));
		}
		return hm;
	}
}
