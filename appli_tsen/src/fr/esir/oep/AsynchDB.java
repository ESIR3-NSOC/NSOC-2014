package fr.esir.oep;

import android.os.AsyncTask;
import android.util.Log;
import fr.esir.interfaces.OnSearchCompleted;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AsynchDB extends AsyncTask<Void, Void, ResultSet> {
    private String room;
    private String id;

    private OnSearchCompleted listener;

    public AsynchDB(OnSearchCompleted listener) {
        this.listener = listener;
    }

    private Connection c;

    @Override
    protected ResultSet doInBackground(Void... params) {
        try {
            Log.w("Jdbc", "Trying to connect");
            findIDRoom();
            String url = "jdbc:mysql://tsen.uion.fr:3306/tsen_ade";
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            c = DriverManager.getConnection(url, "tsen", "nsoc-tsen");
            if (c.isValid(5000))
                Log.w("Jdbc", "connected");

            ResultSet res = null;
            try {
                res = getDataFromDB(c);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return res;
        } catch (Exception e) {
            Log.e("Jdbc", e.toString());
        }
        return null;
    }

    @Override
    protected void onPostExecute(ResultSet res) {

        listener.onSearchbisCompleted(res);
        try {
            c.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ResultSet getDataFromDB(Connection conn) throws SQLException {
        Date dt = new Date();
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
        String datenow = ft.format(dt);
        findIDRoom();
        if (conn != null) {
            Statement st = conn.createStatement();
            String sql = "select DISTINCT ADE_ID,DTSTART,DTEND from tree_object_22 join (select ADE_ID, EVENT_ID from correspondence_22 join (SELECT UID FROM correspondence_22 join event_22 on event_22.UID = correspondence_22.EVENT_ID WHERE ADE_ID=\""
                    + "1005"
                    + "\" and date(event_22.DTSTART) LIKE '"
                    + "2015-01-23"
                    + "') as tmp1 on correspondence_22.EVENT_ID = tmp1.UID) as tmp2 on tree_object_22.id=tmp2.ADE_ID join event_22 on tmp2.EVENT_ID=event_22.UID WHERE NAME NOT LIKE \"%"
                    + "104" + "%\"";
            return st.executeQuery(sql);
        }
        return null;
    }

    private void findIDRoom() {
        try {
            //need to be changed when the db will be ok.
            File file = new File("./data/tsen_confData.txt");
            FileReader fileToRead = new FileReader(file);
            BufferedReader bf = new BufferedReader(fileToRead);
            String aLine;
            String[] values;
            while ((aLine = bf.readLine()) != null) {
                if (aLine.startsWith("ADE_ID")) {
                    values = aLine.split(":");
                    id = values[1];
                }
                if (aLine.startsWith("SALLE")) {
                    values = aLine.split(":");
                    room = values[1];
                }
            }
            bf.close();

        } catch (IOException e) {
            //blabla
        }
    }
}
