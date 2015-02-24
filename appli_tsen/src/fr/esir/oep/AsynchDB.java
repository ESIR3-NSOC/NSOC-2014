package fr.esir.oep;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import com.example.esir.nsoc2014.tsen.lob.interfaces.OnSearchCompleted;
import fr.esir.knx.Reference;
import fr.esir.maintasks.ConfigParams;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AsynchDB extends AsyncTask<Void, Void, ResultSet> {
    private Context context = ConfigParams.context;

    private OnSearchCompleted listener;
    private Connection c;

    public AsynchDB(OnSearchCompleted listener) {
        this.listener = listener;
    }

    @Override
    protected ResultSet doInBackground(Void... params) {
        try {
            Log.w("Jdbc", "Trying to connect");
            String url = DONT_PUT_IT_ON_GIT.db_address;
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            c = DriverManager.getConnection(url, DONT_PUT_IT_ON_GIT.USER, DONT_PUT_IT_ON_GIT.PASSWORD);
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

        listener.onSearchCompleted(res);
        try {
            c.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ResultSet getDataFromDB(Connection conn) throws SQLException {
        SharedPreferences sh = context.getSharedPreferences("APPLI_TSEN", Context.MODE_PRIVATE);
        Date dt = new Date();
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
        String datenow = ft.format(dt);

        if (conn != null) {
            Statement st = conn.createStatement();
            String sql = "select DISTINCT ADE_ID,DTSTART,DTEND,SUMMARY from tree_object_22 join (select ADE_ID, EVENT_ID from correspondence_22 join (SELECT UID FROM correspondence_22 join event_22 on event_22.UID = correspondence_22.EVENT_ID WHERE ADE_ID=\""
                    + sh.getString("IDROOM", Reference.idRoom)
                    + "\" and date(event_22.DTSTART) LIKE '"
                    + sh.getString("DATE", datenow)
                    + "') as tmp1 on correspondence_22.EVENT_ID = tmp1.UID) as tmp2 on tree_object_22.id=tmp2.ADE_ID join event_22 on tmp2.EVENT_ID=event_22.UID WHERE NAME NOT LIKE \"%"
                    + sh.getString("NAMEROOM", Reference.roomName) + "%\"";
            Log.w("sql", sql);
            return st.executeQuery(sql);
        }
        return null;
    }

}
