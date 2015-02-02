package fr.esir.oep;


import fr.esir.interfaces.ServiceConnection;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class JdbcData implements ServiceConnection {
    private Connection conn;

    private String room;
    private String id;

    /**
     * connect to the database
     */
    public boolean connect() {
        try {
            findIDRoom();
            String url = "jdbc:mysql://tsen.uion.fr:3306/tsen_ade";
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(url, "tsen", "nsoc-tsen");

            return conn.isValid(5000);

        } catch (Exception e) {
        }
        return false;
    }

    /**
     * @throws java.sql.SQLException
     */
    public ResultSet getDataFromDB() throws SQLException {
        Date dt = new Date();
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
        String datenow = ft.format(dt);
        findIDRoom();
        Statement st = conn.createStatement();
        String sql = "select DISTINCT ADE_ID,DTSTART,DTEND from tree_object_22 join (select ADE_ID, EVENT_ID from correspondence_22 join (SELECT UID FROM correspondence_22 join event_22 on event_22.UID = correspondence_22.EVENT_ID WHERE ADE_ID=\""
                + id
                + "\" and date(event_22.DTSTART) LIKE '"
                + datenow
                + "') as tmp1 on correspondence_22.EVENT_ID = tmp1.UID) as tmp2 on tree_object_22.id=tmp2.ADE_ID join event_22 on tmp2.EVENT_ID=event_22.UID WHERE NAME NOT LIKE \"%"
                + room + "%\"";
        return st.executeQuery(sql);
    }

    public void close() {
        try {
            conn.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     *
     */
    private void findIDRoom() {
        try {
            File file = new File("./data/confData.txt");
            FileReader fileToRead = new FileReader(file);
            BufferedReader bf = new BufferedReader(fileToRead);
            String aLine;
            String[] values = null;
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
        }
    }
}
