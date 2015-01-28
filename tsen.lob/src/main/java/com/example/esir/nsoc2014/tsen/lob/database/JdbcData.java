package com.example.esir.nsoc2014.tsen.lob.database;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

public class JdbcData {
	private Connection conn;

	private ResultSet result;
	private String room;
	private String id;

	public JdbcData() {
		this.result = null;
	}

	public ResultSet getResultSet() {
		return result;
	}

	/**
	 * connect to the database of a student to add his vote
	 * 
	 */
	public void connexionData() {
		try {
			findIDRoom();
			String url = "jdbc:mysql://tsen.uion.fr:3306/tsen_ade";
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(url, "tsen", "nsoc-tsen");

			if (conn.isValid(5000))
				System.out.println("success");
			else
				System.out.println("failed");

		} catch (Exception e) {
			System.err.println("Got an exception! ");
			System.err.println(e.getMessage());
		}

	}

	/**
	 * 
	 * @throws SQLException
	 */
	public void getDataFromDB() throws SQLException {
		Date dt = new Date();
		SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
		String datenow = ft.format(dt);
		Statement st = conn.createStatement();
		String sql = "select ADE_ID,DTSTART,DTEND from tree_object_22 join (select ADE_ID, EVENT_ID from correspondence_22 join (SELECT UID FROM correspondence_22 join event_22 on event_22.UID = correspondence_22.EVENT_ID WHERE ADE_ID=\""
				+ id
				+ "\" and date(event_22.DTSTART) LIKE '"
				+ datenow
				+ "') as tmp1 on correspondence_22.EVENT_ID = tmp1.UID) as tmp2 on tree_object_22.id=tmp2.ADE_ID join event_22 on tmp2.EVENT_ID=event_22.UID WHERE NAME NOT LIKE \"%"
				+ room + "%\"";
		// System.out.println(sql);
		result = st.executeQuery(sql);
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
	public void findIDRoom() {
		try {
			FileReader fileToRead = new FileReader("./data/confData.txt");
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
			System.out.println(e.getMessage());
		}
	}
}
