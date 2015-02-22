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
import android.widget.TextView;
import fr.esir.maintasks.MyActivity;
import fr.esir.maintasks.R;

/**
 * Created by Nicolas on 21/02/2015.
 */
public class NextProgramming extends Fragment {
    TextView tvprog;
    SharedPreferences pref;
    Context ctx = MyActivity.ct;
    View v;
    FragmentManager fm;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.next_prog, container, false);
        pref = ctx.getSharedPreferences("APPLI_TSEN", Context.MODE_PRIVATE);
        tvprog = (TextView) v.findViewById(R.id.tvProg);
        tvprog.setText(pref.getString("TVPROG", "None"));
        v.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    DayProgram dp = new DayProgram();
                    fm.beginTransaction().replace(R.id.containerMain, dp).commit();
                }
                return true;
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
