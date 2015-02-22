package fr.esir.regulation;

import android.app.Fragment;
import android.os.AsyncTask;
import android.util.Log;
import fr.esir.fragments.MainFragment;
import fr.esir.maintasks.MyActivity;
import fr.esir.maintasks.R;

public class Regulator extends AsyncTask<Void, Void, Void> {

    // recuperer valeur capteur sur contexte
    double temp_int = MyActivity.lastTemp_in; // temperature interieur, fixée pour test
    // récuperer valeur consigne sur contexte ou optimisateur directement
    double temp_cons = 21; // temperature de consigne, fixée pour test
    private double Kp; // coefficient proportionnel
    private double Ki; // coefficient integrateur
    private double Kd; // coefficient dérivateur
    private boolean activated;

    public Regulator() {
        this.Kp = 1;
        this.Ki = 1;
        this.Kd = 1;

    }

    public Regulator(double m_Kp) {
        this.Kp = m_Kp;
        this.Ki = 0;
        this.Kd = 0;
    }

    public Regulator(double m_Kp, double m_Ki) {
        this.Kp = m_Kp;
        this.Ki = m_Ki;
        this.Kd = 0;
    }

    public Regulator(double m_Kp, double m_Ki, double m_Kd) {
        this.Kp = m_Kp;
        this.Ki = m_Ki;
        this.Kd = m_Kd;
    }

    @Override
    protected Void doInBackground(Void... params) {
        double diff_temp = 0; // erreur : différence entre temperature désirée
        // et température mesurée
        double valeur_sortie = 0; // valeur de sortie
        double somme_erreurs = 0; // somme des erreurs (répéter à chaque
        // itération)
        double variation_erreur = 0; // différence entre erreur actuelle et
        // erreur précédente
        boolean on = false;
        while (!this.isCancelled()) {
            somme_erreurs = somme_erreurs + diff_temp; // calcule la nouvelle somme des erreurs
            variation_erreur = temp_cons - temp_int - diff_temp; // calcule la différence entre la nouvelle erreur et la précédente
            diff_temp = temp_cons - temp_int; // calcul la nouvelle erreur
            // calcule de la nouvelle valeur de sortie ::: régulation PID,dépend du constructeur utilisé
            valeur_sortie = diff_temp * this.Kp + somme_erreurs * this.Ki
                    + variation_erreur * Kd;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            // envoyer valeur de sortie vers KNX
            Fragment currentFragment = MyActivity.act.getFragmentManager().findFragmentById(R.id.containerMain);
            if (currentFragment instanceof MainFragment) {
                if (valeur_sortie > 0 && !on) {
                    MainFragment.iaf.setImage(true);
                } else if (valeur_sortie < 0 && on)
                    MainFragment.iaf.setImage(false);
            }
        }

        return null;
    }

    public void setConsigne(double consigne) {
        this.temp_cons = consigne;
        Log.i("New Consigne Regul", consigne + "");
    }

    public void setTempInterieur(double tempInt) {
        this.temp_int = tempInt;
    }

}
