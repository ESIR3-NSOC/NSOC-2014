package fr.esir.nsoc.tsen.ade.http.parser;

import java.util.HashSet;

import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import fr.esir.nsoc.tsen.ade.object.Category;
import fr.esir.nsoc.tsen.ade.object.Project;

public class CategoryParser {
	
	private String _html;
	private Project project;

	public CategoryParser(String _html, Project project) {
		super();
		this._html = _html;
		this.project = project;
	}

	public HashSet<Category> Parse()
	{
		Document doc = Jsoup.parse(_html);
		Elements list = doc.select("div.treeline");
		
		HashSet<Category> hs = new HashSet<Category>();
		
		for (Element e1 : list)
		{
			if (StringUtils.countMatches(e1.html(), "&nbsp;") == 0)
			{
				Element e2 = e1.select("a[href*=\"javascript:checkCategory\"]").first();
				System.out.println(e2.attr("href").split("'")[1] + " > " + e2.text());
				Category c = new Category(e2.attr("href").split("'")[1], e2.text(), project);
				hs.add(c);
			}
		}
		return hs;
	}

	public String get_html() {
		return _html;
	}

	public Project getProject() {
		return project;
	}
	
}
