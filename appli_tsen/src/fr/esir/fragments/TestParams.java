package fr.esir.fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.EditText;
import fr.esir.maintasks.ConfigParams;
import fr.esir.maintasks.MyActivity;
import fr.esir.maintasks.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by Nicolas on 08/02/2015.
 */
public class TestParams extends Fragment {
    FragmentManager fm;
    Context context = ConfigParams.context;
    SharedPreferences pref;
    View v;

    EditText idr;
    CalendarView calendar;

    private void setSharedPref() {
        pref.edit().putLong("DELAY", Long.valueOf(idr.getText().toString()).longValue() * 60000).apply();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //final Context contextThemeWrapper = new ContextThemeWrapper(getActivity(), android.R.style.Theme_Holo_);
        //LayoutInflater localInflater = inflater.cloneInContext(contextThemeWrapper);
        v = inflater.inflate(R.layout.test_params, container, false);
        pref = context.getSharedPreferences("APPLI_TSEN", Context.MODE_PRIVATE);
        calendar = (CalendarView) v.findViewById(R.id.calendarView);
        idr = (EditText) v.findViewById(R.id.delay);
        initializeCalendar();
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
                fm.beginTransaction().replace(R.id.container, mp).commit();
            }
        });

        return v;
    }

    private void setTv() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_MONTH, 1);
        c.set(Calendar.HOUR_OF_DAY, 1);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        long howMany = c.getTimeInMillis() - System.currentTimeMillis();
        long d = pref.getLong("DELAY", howMany);
        String st = String.format("%d",
                TimeUnit.MILLISECONDS.toMinutes(d)
        );
        idr.setText(st);

        Date dt = new Date();
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
        String datenow = ft.format(dt);
        //nr.setText(pref.getString("DATE",datenow));
    }

    private void setDefault() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_MONTH, 1);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        long howMany = c.getTimeInMillis() - System.currentTimeMillis();
        String s = String.format("%d",
                TimeUnit.MILLISECONDS.toMinutes(howMany)
        );
        idr.setText(s);

/*
        Date dt = new Date();
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
        String datenow = ft.format(dt);*/
        //nr.setText(datenow);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fm = getFragmentManager();
    }

    public void initializeCalendar() {
        //calendar.setBackgroundColor(Color.BLACK);
        // sets whether to show the week number.
        calendar.setShowWeekNumber(false);

        // sets the first day of week according to Calendar.
        // here we set Monday as the first day of the Calendar
        calendar.setFirstDayOfWeek(2);

        //The background color for the selected week.
        calendar.setSelectedWeekBackgroundColor(getResources().getColor(R.color.green));

        //sets the color for the dates of an unfocused month.
        calendar.setUnfocusedMonthDateColor(getResources().getColor(R.color.transparent));

        //sets the color for the separator line between weeks.
        calendar.setWeekSeparatorLineColor(getResources().getColor(R.color.transparent));

        //sets the color for the vertical bar shown at the beginning and at the end of the selected date.
        calendar.setSelectedDateVerticalBar(R.color.darkgreen);

        //sets the listener to be notified upon selected date change.
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            //show the selected date as a toast
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int day) {
                month += 1;
                String m = month < 10 ? "0" + month : month + "";
                String d = day < 10 ? "0" + day : day + "";
                Log.w("Date", year + "-" + m + "-" + d);
                pref.edit().putString("DATE", year + "-" + m + "-" + d).apply();
            }
        });
    }
}