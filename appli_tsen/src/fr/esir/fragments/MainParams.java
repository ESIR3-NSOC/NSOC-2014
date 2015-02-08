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

/**
 * Created by Nicolas on 08/02/2015.
 */
public class MainParams extends Fragment {
    FragmentManager fm;
    Context context = ConfigParams.context;
    SharedPreferences pref;

    EditText idr;
    EditText nr;

    private void setSharedPref(){
        pref.edit().putString("IDROOM", idr.getText().toString()).apply();
        pref.edit().putString("NAMEROOM", nr.getText().toString()).apply();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final Context contextThemeWrapper = new ContextThemeWrapper(getActivity(), android.R.style.Theme_Black);
        LayoutInflater localInflater = inflater.cloneInContext(contextThemeWrapper);

        View v = localInflater.inflate(R.layout.main_params, container, false);
        pref = context.getSharedPreferences("APPLI_TSEN", Context.MODE_PRIVATE);

        idr = (EditText) v.findViewById(R.id.idRoom);
        idr.setText(pref.getString("IDROOM", "1005"));

        nr = (EditText) v.findViewById(R.id.nameRoom);
        nr.setText(pref.getString("NAMEROOM","104"));

                (v.findViewById(R.id.button_test)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setSharedPref();
                        TestParams tp = new TestParams();
                        fm.beginTransaction().replace(R.id.container, tp).commit();
                    }
                });

        (v.findViewById(R.id.button_start)).setOnClickListener(new View.OnClickListener() {
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
                idr.setText("1005");
                nr.setText("104");
            }
        });

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fm = getFragmentManager();
    }
}