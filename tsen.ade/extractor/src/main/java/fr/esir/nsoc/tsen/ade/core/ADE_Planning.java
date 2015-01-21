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
import fr.esir.nsoc.tsen.ade.object.TreeObject;

public class ADE_Planning {

	private final static boolean DEBUG = true;

	private DataBase dataBase;
	private ADE_Scope scope;

	public ADE_Planning(DataBase dataBase, ADE_Scope scope) {
		super();
		this.dataBase = dataBase;
		this.scope = scope;
	}

	public void retrieve(int poolSize) {
		ArrayList<Callable<Boolean>> pool = new ArrayList<Callable<Boolean>>();

		HashSet<TreeObject> tos = scope.getScope();
		Iterator<TreeObject> i = tos.iterator();
		while (i.hasNext()) {
			TreeObject to = i.next();
			pool.add(new ICSExtractor(to, scope.getStartPoint(), scope
					.getEndPoint()));

		}

		ExecutorService executor = Executors.newFixedThreadPool(poolSize);

		solve(executor, pool);

	}

	public DataBase getDataBase() {
		return dataBase;
	}

	public ADE_Scope getScope() {
		return scope;
	}

	private void solve(final ExecutorService executor,
			List<Callable<Boolean>> taches) {

		// Le service de terminaison
		CompletionService<Boolean> completionService = new ExecutorCompletionService<Boolean>(
				executor);

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
