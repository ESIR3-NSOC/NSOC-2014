package fr.esir.nsoc.tsen.ade.http.parser;

import java.util.HashSet;



import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import fr.esir.nsoc.tsen.ade.object.Project;
import fr.esir.nsoc.tsen.ade.object.TreeObject;

public class TreeParser {

	private String html;
	private Project project;

	private final static boolean DEBUG = false;
	private final static int TREE_INDENTATION = 3;
	
	
	public TreeParser(String html, Project project) {
		super();
		this.html = html;
		this.project = project;
	}


	public HashSet<TreeObject> Parse()
	{
		

		Document doc = Jsoup.parse(html);
		Elements list = doc.select("div.treeline");
		
		HashSet<TreeObject> hs = new HashSet<TreeObject>();
		
		for (Element e1 : list)
		{
			int level = StringUtils.countMatches(e1.text(), "\u00a0");
			boolean leaf = e1.html().contains("javascript:check(");
			boolean branch = e1.html().contains("javascript:checkBranch(");
			boolean category = e1.html().contains("javascript:checkCategory(");
			
			Element e2 = e1.select("a[href*=\"javascript:check\"]").first();
			
			String name = "";
			String id = "";
			if (level > 0)
			{
				name = e1.text().replace("\u00a0", "");
				name = leaf ? (name.length()>=2 ? name.substring(2) : name) : (name.length()>=2 ? name.substring(1) : name);
				
				
				//if (leaf) name = e1.text().replace("\u00a0", "").substring(2);
				//else name = e1.text().replace("\u00a0", "").substring(1);
				id = e2.attr("href").split("[\\(||,||//)]")[1];
			} else {
				name = e1.text().replace("\u00a0", "");
				id = e2.attr("href").split("[\\(||,||//)]")[1].split("'")[1];
			}
			
			String type = (leaf ? "leaf" : "") + (branch ? "branch" : "") + (category ? "category" : "");
			
			if (DEBUG) System.out.println(type +
					": \"" + name + "\", level:" + level/3 + 
					", id: \"" + id + "\"");
			
		
			TreeObject to = new TreeObject(project, level/TREE_INDENTATION, name, id, type);
			hs.add(to);
		}
		


		return hs;
	}
	
	
	
	
	public String getHtml() {
		return html;
	}


	public Project getProject() {
		return project;
	}
	
	
	
	
	
}
