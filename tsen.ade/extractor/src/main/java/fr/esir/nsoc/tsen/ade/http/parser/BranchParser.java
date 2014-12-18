package fr.esir.nsoc.tsen.ade.http.parser;

import java.util.HashSet;

import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import fr.esir.nsoc.tsen.ade.object.Branch;

public class BranchParser {
	
	private String _html;
	private int projectID;
	private String category;

	public BranchParser(String _html, int projectID, String category) {
		super();
		this._html = _html;
		this.projectID = projectID;
		this.category = category;
	}

	public HashSet<Branch> Parse()
	{
		Document doc = Jsoup.parse(_html);
		Elements list = doc.select("div.treeline");
		
		HashSet<Branch> hs = new HashSet<Branch>();
		
		for (Element e1 : list)
		{
			if (StringUtils.countMatches(e1.html(), "&nbsp;") == 0)
			{
				Element e2 = e1.select("a[href*=\"javascript:checkCategory\"]").first();
				System.out.println(e2.attr("href").split("'")[1] + " > " + e2.text());
//				Branch b = new Branch(e2.attr("href").split("'")[1], e2.text(), projectID);
//				hs.add(b);
			}
		}
		return hs;
	}

	public String get_html() {
		return _html;
	}

	public int getProjectID() {
		return projectID;
	}

	public String getCategory() {
		return category;
	}
	
	
}
