package fr.esir.resources;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.example.esir.nsoc2014.tsen.lob.objects.DatesInterval;
import fr.esir.maintasks.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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
        Log.e("START", list.get(position).getStartDate() + "");
        Log.e("END", list.get(position).getEndDate() + "");
        String startD = changeDateInhhSSmm(list.get(position).getStartDate());
        String endD = changeDateInhhSSmm(list.get(position).getEndDate());
        String form = changeStringForm(startD, endD, list.get(position).getLesson());

        textView.setText(form);
        tv.setText("Temperature set : " + list.get(position).getConsigne() + " °C");

        return rowView;
    }

    public String changeDateInhhSSmm(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
        return sdf.format(date);
    }

    public String changeStringForm(String s1, String s2, String s3) {
        return s1 + " - " + s2 + "  " + s3;
    }
}
