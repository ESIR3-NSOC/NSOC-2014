package fr.esir.nsoc.tsen.ade.http.parser;

import java.util.HashSet;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import fr.esir.nsoc.tsen.ade.object.Project;

public class ProjectParser {
	
	private String _html = "";
	
	public ProjectParser(String html) {
		super();
		this._html = html;
	}

	public HashSet<Project> Parse()
	{
		Document doc = Jsoup.parse(_html);
		Element list = doc.select("SELECT").first();
		Elements projects = list.select("option");

		HashSet<Project> hs = new HashSet<Project>();
		
		for (Element project : projects)
		{
			Project p = new Project(Integer.parseInt(project.attr("value")), project.text());
			hs.add(p);
		}
		return hs;
	}
}
