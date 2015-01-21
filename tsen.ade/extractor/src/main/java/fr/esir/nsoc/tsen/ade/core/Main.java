package fr.esir.nsoc.tsen.ade.core;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;


import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;



import fr.esir.nsoc.tsen.ade.database.DataBase;
import fr.esir.nsoc.tsen.ade.database.SQLiteDB;
import fr.esir.nsoc.tsen.ade.object.ADEDay;
import fr.esir.nsoc.tsen.ade.object.Project;
import fr.esir.nsoc.tsen.ade.object.TreeObject;

public class Main {



	private final static boolean DEBUG = true;
	

	public static void main(String[] args) {
		
		
		// connect to local DB
		DataBase db = new SQLiteDB("test1.db");
		if (DEBUG) System.out.println(db.isConnected() ? "ok" : "nok");
		
		// brows ADE Tree
		ADE_Tree at = new ADE_Tree(db);
		//at.browseTree();
		
		// get project
		Project project = at.getProject();
		
		// define scope
		ADE_Scope scope = new ADE_Scope(db);
		//scope.addChildrenToScope(new TreeObject(project, level, name, id, parentId, type))
		
		
		
		// example
		HashSet<TreeObject> tos = db.getTreeObjectChildren(new TreeObject(new Project(22, ""), -1, "", "7748", "", ""));
		Iterator<TreeObject> i = tos.iterator();
		while (i.hasNext()) {
			TreeObject to = i.next();
			
				if (true) System.out.println(to.getType() + ": \"" + to.getName() + "\", id: \"" + to.getId() + "\", level:" + to.getLevel());

				
				
			}
		
		
		
		
		
		
		

		
		List<Callable<Boolean>> taches = new ArrayList<Callable<Boolean>>();
		ADEDay Alex=new ADEDay(4128, "2015-01-19", 22);
		ADEDay Alexis=new ADEDay(6579, "2015-01-19", 22);
		ADEDay Mathou=new ADEDay(6434, "2015-01-19", 22);
		ADEDay Antoine=new ADEDay(6004, "2015-01-19", 22);
		ADEDay Coco=new ADEDay(5978, "2015-01-19", 22);
		ADEDay Etienne=new ADEDay(6301, "2015-01-19", 22);
		ADEDay Arnaud=new ADEDay(6662, "2015-01-19", 22);
		ADEDay Benji=new ADEDay(6580, "2015-01-19", 22);
		ADEDay Mathias=new ADEDay(6432, "2015-01-19", 22);
		ADEDay Maxime=new ADEDay(5912, "2015-01-19", 22);
		ADEDay Jeremy=new ADEDay(5916, "2015-01-19", 22);
		ADEDay Mathieu=new ADEDay(7799, "2015-01-19", 22);
		ADEDay Richard=new ADEDay(5893, "2015-01-19", 22);
		ADEDay Xavier=new ADEDay(6390, "2015-01-19", 22);
		ADEDay PAM=new ADEDay(6402, "2015-01-19", 22);
		ADEDay Theo=new ADEDay(7797, "2015-01-19", 22);
		ADEDay Nade=new ADEDay(7796, "2015-01-19", 22);
		ADEDay Thomas=new ADEDay(7800, "2015-01-19", 22);
		ADEDay Oceane=new ADEDay(7862, "2015-01-19", 22);
		ADEDay Mael=new ADEDay(8084, "2015-01-19", 22);
		
		Callable<Boolean> tache1 = new ICSExtractor(Alex);
		Callable<Boolean> tache2 = new ICSExtractor(Alexis);
		Callable<Boolean> tache3 = new ICSExtractor(Mathou);
		Callable<Boolean> tache4 = new ICSExtractor(Antoine);
		Callable<Boolean> tache5 = new ICSExtractor(Coco);
		Callable<Boolean> tache6 = new ICSExtractor(Etienne);
		Callable<Boolean> tache7 = new ICSExtractor(Arnaud);
		Callable<Boolean> tache8 = new ICSExtractor(Benji);
		Callable<Boolean> tache9 = new ICSExtractor(Mathias);
		Callable<Boolean> tache10 = new ICSExtractor(Maxime);
		Callable<Boolean> tache11 = new ICSExtractor(Jeremy);
		Callable<Boolean> tache12 = new ICSExtractor(Mathieu);
		Callable<Boolean> tache13 = new ICSExtractor(Richard);
		Callable<Boolean> tache14 = new ICSExtractor(Xavier);
		Callable<Boolean> tache15 = new ICSExtractor(PAM);
		Callable<Boolean> tache16 = new ICSExtractor(Theo);
		Callable<Boolean> tache17 = new ICSExtractor(Nade);
		Callable<Boolean> tache18 = new ICSExtractor(Thomas);
		Callable<Boolean> tache19 = new ICSExtractor(Oceane);
		Callable<Boolean> tache20 = new ICSExtractor(Mael);
	
		taches.add(tache1);
		taches.add(tache2);
		taches.add(tache3);
		taches.add(tache4);
		taches.add(tache5);
		taches.add(tache6);
		taches.add(tache7);
		taches.add(tache8);
		taches.add(tache9);
		taches.add(tache10);
		taches.add(tache11);
		taches.add(tache12);
		taches.add(tache13);
		taches.add(tache14);
		taches.add(tache15);
		taches.add(tache16);
		taches.add(tache17);
		taches.add(tache18);
		taches.add(tache19);
		taches.add(tache20);
			
		ExecutorService executor = Executors.newFixedThreadPool(10);
		
		resoudre(executor, taches);
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		

		// exit
		System.exit(0);
	}
	
	
	public static void resoudre(final ExecutorService executor, List<Callable<Boolean>> taches) {

		// Le service de terminaison
		CompletionService<Boolean> completionService = new ExecutorCompletionService<Boolean>(executor);

		// une liste de Future pour récupérer les résultats
		List<Future<Boolean>> futures = new ArrayList<Future<Boolean>>();

		Boolean res = null;
		try {
			// On soumet toutes les tâches à l'executor
			for (Callable<Boolean> t : taches) {
				futures.add(completionService.submit(t));
			}

			for (int i = 0; i < taches.size(); ++i) {

				try {

					// On récupère le premier résultat disponible
					// sous la forme d'un Future avec take(). Puis l'appel
					// à get() nous donne le résultat du Callable.
					res = completionService.take().get();
					if (res != null) {

						// On affiche le resultat de la tâche
						System.out.println("Resultat : " + res);
					}
				} catch (ExecutionException ignore) {
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			executor.shutdown();
		}
}



}
