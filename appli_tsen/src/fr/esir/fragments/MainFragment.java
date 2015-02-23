package fr.esir.fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import fr.esir.maintasks.ImageActivity;
import fr.esir.maintasks.MyActivity;
import fr.esir.maintasks.R;

/**
 * Created by Nicolas on 22/02/2015.
 */
public class MainFragment extends Fragment {
    TextView hum_out;
    TextView hum_in;
    TextView temp_ou;
    TextView temp_in;
    TextView lum_ou;
    TextView lum_in;
    TextView co2;
    EditText etuser;
    EditText etvote;
    Button vote;
    View v;
    FragmentManager fm;
    public static ImageActivity iaf;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.main_fragment, container, false);
        temp_in = (TextView) v.findViewById(R.id.IndoorTempValue);
        temp_ou = (TextView) v.findViewById(R.id.OutdoorTempValue);
        hum_in = (TextView) v.findViewById(R.id.IndoorHumValue);
        hum_out = (TextView) v.findViewById(R.id.OutdoorHumValue);
        lum_ou = (TextView) v.findViewById(R.id.OutdoorLumValue);
        lum_in = (TextView) v.findViewById(R.id.IndoorLumValue);
        co2 = (TextView) v.findViewById(R.id.CO2Value);

        etuser = (EditText) v.findViewById(R.id.etuser);
        etvote = (EditText) v.findViewById(R.id.etvote);


        InputFilter[] filters = new InputFilter[1];
        filters[0] = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                if (end > start) {
                    char[] acceptedChars = new char[]{
                            '+', '-', '*'
                    };
                    for (int index = start; index < end; index++) {
                        if (!new String(acceptedChars).contains(String.valueOf(source.charAt(index)))) {
                            return "";
                        }
                    }
                }
                return null;
            }
        };

        etvote.setFilters(filters);

        vote = (Button) v.findViewById(R.id.bvote);
        vote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //manual vote
                //add in context

                String vote = etvote.getText().toString();
                MyActivity.mRegulation_service.executeVote(MyActivity.lastTemp_in, vote);
            }
        });

        v.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    DayProgram np = new DayProgram();
                    fm.beginTransaction().replace(R.id.containerMain, np).commit();
                }
                return true;
            }
        });

        return v;
    }

    private void setDisplayInit(){
        co2.setText(String.valueOf(MyActivity.lastCO2));
        temp_ou.setText(String.valueOf(MyActivity.lastTemp_out));
        temp_in.setText(String.valueOf(MyActivity.lastTemp_in));
        hum_in.setText(String.valueOf(MyActivity.lastHum_in));
        hum_out.setText(String.valueOf(MyActivity.lastHum_out));
        lum_ou.setText(String.valueOf(MyActivity.lastLum_out));
    }

    public void setDisplayData(String add, String data) {
        //variables lastX contain the last data of sensors
        String[] dataSplit = data.split(" ");
        if (add.equals("0/0/4")) { //co2 sensor
            co2.setText(data);
        } else if (add.equals("0/1/0")) { //outdoor temperature sensor
            temp_ou.setText(data);
        } else if (add.equals("0/0/5")) {//indoor humidity sensor
            hum_in.setText(data);
        } else if (add.equals("0/1/1")) {//outdoor luminosity sensor
            lum_ou.setText(data);
        } else if (add.equals("0/1/2")) { //outdoor humidity sensor
            hum_out.setText(data);
        } else if (add.equals("0/0/3")) { //indoor temperature sensor
            temp_in.setText(data);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fm = getFragmentManager();
        iaf = new ImageActivity(this.getActivity());
    }
}
