package fr.esir.maintasks;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import fr.esir.fragments.MainParams;

/**
 * Created by Nicolas on 08/02/2015.
 */
public class ConfigParams extends Activity {
    public static Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.config_main);
        context = this;
        FragmentManager fm = getFragmentManager();
        MainParams mp = new MainParams();
        fm.beginTransaction().add(R.id.container, mp).commit();
    }
}