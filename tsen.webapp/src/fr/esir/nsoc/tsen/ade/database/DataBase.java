package fr.esir.nsoc.tsen.ade.database;

import java.util.HashSet;
import java.util.Set;

import fr.esir.nsoc.tsen.ade.object.Correspondence;
import fr.esir.nsoc.tsen.ade.object.Event;
import fr.esir.nsoc.tsen.ade.object.Project;
import fr.esir.nsoc.tsen.ade.object.TreeObject;
import fr.esir.nsoc.tsen.objects.Vote;

public interface DataBase {
	
	public boolean isConnected();

	public void createProjectTable();
	public boolean addProject(Project project);
	public void fillProject(HashSet<Project> projects);
	public Project getProject(int projectId);
	public HashSet<Project> getProjects();
	
	public void createTreeObjectTable(int projectid);
	public boolean addTreeObject(TreeObject treeObject);
	public HashSet<TreeObject> getTreeObjectChildren(TreeObject treeObject);
	public TreeObject getTreeObject(String id, Project project);
	
	public void createEventTable(int project_ID);
	public boolean addEvent(Event event, Project project);
	public boolean fillEvent(Set<Event> set, int projectid);
	public Event getEventByUID(String UID, Project project);
	public HashSet<Event> getEventByDate(String date, Project project);
	public Event getEventByUserIdByDate(String date, int uid, Project project);
	/*
	public void createUidTable(int project_ID);
	public boolean fillUid(Set<Event> set, String adeid, int projectid);
	
	*/
	public  HashSet<Correspondence> getCorrespondence(String date, Project project);
	public boolean addCorrespondence(Event event, TreeObject treeObject);
	public boolean fillCorrespondence(HashSet<Event> events, TreeObject treeObject);
	public HashSet<TreeObject> getTreeObjectSession(String UID, Project projet);

	public void addVote(Project project, TreeObject userTo, TreeObject roomTo,
			Event event, String rate, String date);

	public HashSet<TreeObject> getTreeObjectByEvent(Event event, Project project);

	public HashSet<TreeObject> getTreeObjectByEventByRoot(Event event, String root,
			Project project);

	public HashSet<Vote> getVoteByRoomByDelay(Project project, String roomId,
			int delaySec);



	


	

	

}
