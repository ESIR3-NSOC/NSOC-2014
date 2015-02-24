package fr.esir.fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;
import fr.esir.knx.Reference;
import fr.esir.maintasks.ConfigParams;
import fr.esir.maintasks.MyActivity;
import fr.esir.maintasks.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Nicolas on 08/02/2015.
 */
public class MainParams extends Fragment {
    private static final String PATTERN =
            "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";
    FragmentManager fm;
    Context context;
    SharedPreferences pref;
    Context mainparams;
    EditText idr;
    EditText nr;
    EditText mt;
    EditText tm;
    EditText knx_ip;
    EditText phone_ip;

    public static boolean validate(final String ip) {
        Pattern pattern = Pattern.compile(PATTERN);
        Matcher matcher = pattern.matcher(ip);
        return matcher.matches();
    }

    private void setSharedPref() {
        pref.edit().putString("IDROOM", idr.getText().toString()).apply();
        pref.edit().putString("NAMEROOM", nr.getText().toString()).apply();
        pref.edit().putString("TEMPMIN", mt.getText().toString()).apply();
        pref.edit().putString("TEMPMAX", tm.getText().toString()).apply();
        pref.edit().putString("KNX_IP", knx_ip.getText().toString()).apply();
        pref.edit().putString("PHONE_IP", phone_ip.getText().toString()).apply();
    }

    private void checkStatesAfterClick(String action) {
        if (nr.getText().toString().equals(""))
            Toast.makeText(mainparams, "Please, enter a room name or click default", Toast.LENGTH_SHORT).show();
        else if (idr.getText().toString().equals(""))
            Toast.makeText(mainparams, "Please, enter a room id or click default", Toast.LENGTH_SHORT).show();
        else if (mt.getText().toString().equals(""))
            Toast.makeText(mainparams, "Please, enter a max temperature value or click default", Toast.LENGTH_SHORT).show();
        else if (tm.getText().toString().equals(""))
            Toast.makeText(mainparams, "Please, enter a min temperature value or click default", Toast.LENGTH_SHORT).show();
        else if (phone_ip.getText().toString().equals(""))
            Toast.makeText(mainparams, "Please, enter the IP address of your mobile", Toast.LENGTH_SHORT).show();
        else if (knx_ip.getText().toString().equals(""))
            Toast.makeText(mainparams, "Please, enter the IP address of your KNX Network", Toast.LENGTH_SHORT).show();
        else if (!validate(phone_ip.getText().toString()) || !validate(knx_ip.getText().toString()))
            Toast.makeText(mainparams, "Please, enter a valid IP address", Toast.LENGTH_SHORT).show();
        else {
            setSharedPref();
            switch (action) {
                case "MyActivity":
                    Intent intent = new Intent(context, MyActivity.class);
                    startActivity(intent);
                    break;
                case "Test":
                    TestParams tp = new TestParams();
                    fm.beginTransaction().replace(R.id.container, tp).commit();
                    break;
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //final Context contextThemeWrapper = new ContextThemeWrapper(getActivity(), android.R.style.Theme_Holo_Light);
        //LayoutInflater localInflater = inflater.cloneInContext(contextThemeWrapper);
        context = ConfigParams.context;
        View v = inflater.inflate(R.layout.main_params, container, false);
        pref = context.getSharedPreferences("APPLI_TSEN", Context.MODE_PRIVATE);
        mainparams = this.getActivity();
        mt = (EditText) v.findViewById(R.id.tmin);
        mt.setText(pref.getString("TEMPMIN", Reference.minTemp));

        tm = (EditText) v.findViewById(R.id.tmax);
        tm.setText(pref.getString("TEMPMAX", Reference.maxTemp));

        idr = (EditText) v.findViewById(R.id.idRoom);
        idr.setText(pref.getString("IDROOM", Reference.idRoom));

        nr = (EditText) v.findViewById(R.id.nameRoom);
        nr.setText(pref.getString("NAMEROOM", Reference.roomName));

        knx_ip = (EditText) v.findViewById(R.id.knx_ip);
        knx_ip.setText(pref.getString("KNX_IP", Reference.KNX_ADDRESS));

        phone_ip = (EditText) v.findViewById(R.id.phone_ip);
        phone_ip.setText(pref.getString("PHONE_IP", Reference.HOST_ADDRESS));

        (v.findViewById(R.id.button_test)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkStatesAfterClick("Test");
            }
        });

        (v.findViewById(R.id.button_start)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkStatesAfterClick("MyActivity");
            }
        });

        v.findViewById(R.id.but_def).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                idr.setText("1005");
                nr.setText("104");
                mt.setText("20");
                tm.setText("25");
                phone_ip.setText(Reference.HOST_ADDRESS);
                knx_ip.setText(Reference.KNX_ADDRESS);
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