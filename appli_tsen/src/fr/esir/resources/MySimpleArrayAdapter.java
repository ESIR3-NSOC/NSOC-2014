package fr.esir.resources;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.example.esir.nsoc2014.tsen.lob.objects.DatesInterval;
import fr.esir.maintasks.R;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * Created by Nicolas on 21/02/2015.
 */
public class MySimpleArrayAdapter extends ArrayAdapter<DatesInterval> {
    private ArrayList<DatesInterval> list;
    private Context context;

    public MySimpleArrayAdapter(Context context, ArrayList<DatesInterval> list) {
        super(context, R.layout.row_layout, list);
        this.list = list;
        this.context = context;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.row_layout, parent, false);

        TextView textView = (TextView) rowView.findViewById(R.id.lessontv);
        TextView tv = (TextView) rowView.findViewById(R.id.consignetv);

        String startD = changeMilliInhhSSmm(list.get(position).getStartDate().getTime());
        String endD = changeMilliInhhSSmm(list.get(position).getEndDate().getTime());
        String form = changeStringForm(startD, endD, list.get(position).getLesson());

        textView.setText(form);
        tv.setText("Temperature set : " + list.get(position).getConsigne() + " °C");

        return rowView;
    }

    public String changeMilliInhhSSmm(long millis) {
        return String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis),
                TimeUnit.MILLISECONDS.toMinutes(millis) % TimeUnit.HOURS.toMinutes(1),
                TimeUnit.MILLISECONDS.toSeconds(millis) % TimeUnit.MINUTES.toSeconds(1));
    }

    public String changeStringForm(String s1, String s2, String s3) {
        return s1 + " - " + s2 + "  " + s3;
    }
}
