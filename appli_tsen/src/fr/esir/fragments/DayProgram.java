package fr.esir.fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import com.example.esir.nsoc2014.tsen.lob.objects.DatesInterval;
import fr.esir.maintasks.MyActivity;
import fr.esir.maintasks.R;
import fr.esir.resources.MySimpleArrayAdapter;
import fr.esir.services.Control_service;

import java.util.ArrayList;

/**
 * Created by Nicolas on 21/02/2015.
 */
public class DayProgram extends Fragment {
    View v;
    ListView lv;
    ArrayList<DatesInterval> listD;
    TextView tvprog;
    SharedPreferences pref;
    Context ctx = MyActivity.ct;
    FragmentManager fm;

    public void updateList(ArrayList<DatesInterval> l) {
        listD = l;
        if (listD != null) {
            MySimpleArrayAdapter msaa = new MySimpleArrayAdapter(this.getActivity(), listD);
            lv.setAdapter(msaa);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.day_program, container, false);

        lv = (ListView) v.findViewById(R.id.list);
        listD = Control_service.list;
        tvprog = (TextView) v.findViewById(R.id.tvProg);
        setProg();

        v.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    MainFragment np = new MainFragment();
                    fm.beginTransaction().replace(R.id.containerMain, np).commit();
                }

                return true;
            }
        });

        return v;
    }

    public void setProg() {
        pref = ctx.getSharedPreferences("APPLI_TSEN", Context.MODE_PRIVATE);
        tvprog.setText(pref.getString("TVPROG", "None"));
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fm = getFragmentManager();
        if (listD != null) {
            MySimpleArrayAdapter msaa = new MySimpleArrayAdapter(this.getActivity(), listD);
            lv.setAdapter(msaa);
        }
    }
}
