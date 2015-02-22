package fr.esir.fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.example.esir.nsoc2014.tsen.lob.objects.DatesInterval;
import fr.esir.maintasks.R;
import fr.esir.resources.MySimpleArrayAdapter;
import fr.esir.services.Regulation_service;

import java.util.ArrayList;

/**
 * Created by Nicolas on 21/02/2015.
 */
public class DayProgram extends Fragment {
    FragmentManager fm;
    View v;
    ListView lv;
    ArrayList<DatesInterval> listD;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.day_program, container, false);
        lv = (ListView) v.findViewById(R.id.list);
        listD = Regulation_service.list;
        v.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    NextProgramming np = new NextProgramming();
                    fm.beginTransaction().replace(R.id.containerMain, np).commit();
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
        if (listD != null) {
            MySimpleArrayAdapter msaa = new MySimpleArrayAdapter(this.getActivity(), listD);
            lv.setAdapter(msaa);
        }
    }
}
