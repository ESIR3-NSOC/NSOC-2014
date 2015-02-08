package fr.esir.fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import fr.esir.maintasks.ConfigParams;
import fr.esir.maintasks.MyActivity;
import fr.esir.maintasks.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Nicolas on 08/02/2015.
 */
public class TestParams extends Fragment {
    FragmentManager fm;
    Context context = ConfigParams.context;
    SharedPreferences pref;
    View v;

    EditText idr;
    EditText nr;

    private void setSharedPref(){
        pref.edit().putLong("DELAY", Long.valueOf(idr.getText().toString()).longValue()).apply();
        pref.edit().putString("DATE", nr.getText().toString()).apply();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final Context contextThemeWrapper = new ContextThemeWrapper(getActivity(), android.R.style.Theme_Black);
        LayoutInflater localInflater = inflater.cloneInContext(contextThemeWrapper);
        v =  localInflater.inflate(R.layout.test_params, container, false);
        pref = context.getSharedPreferences("APPLI_TSEN", Context.MODE_PRIVATE);

        idr = (EditText) v.findViewById(R.id.delay);
        nr = (EditText) v.findViewById(R.id.date);

        setTv();

        v.findViewById(R.id.button_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSharedPref();
                Intent intent = new Intent(context, MyActivity.class);
                startActivity(intent);
            }
        });

        v.findViewById(R.id.but_def).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDefault();
            }
        });

        v.findViewById(R.id.button_main).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSharedPref();
                MainParams mp = new MainParams();
                fm.beginTransaction().replace(R.id.container,mp).commit();
            }
        });

        return v;
    }

    private void setTv(){
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_MONTH, 1);
        c.set(Calendar.HOUR_OF_DAY, 1);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        long howMany = c.getTimeInMillis()-System.currentTimeMillis();
        idr.setText(pref.getLong("DELAY", howMany)+"");

        Date dt = new Date();
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
        String datenow = ft.format(dt);
        nr.setText(pref.getString("DATE",datenow));
    }

    private void setDefault(){
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_MONTH, 1);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        long howMany = c.getTimeInMillis()-System.currentTimeMillis();
        idr.setText(howMany+"");

        Date dt = new Date();
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
        String datenow = ft.format(dt);
        nr.setText(datenow);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fm = getFragmentManager();
    }
}