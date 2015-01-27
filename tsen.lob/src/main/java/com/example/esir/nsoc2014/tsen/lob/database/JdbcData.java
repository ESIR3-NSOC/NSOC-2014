package com.example.esir.nsoc2014.tsen.lob.database;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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
	 * @param id
	 * @param temp_ext
	 * @param humidity_ext
	 * @param temp_int
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
	
	public void getDataFromDB() throws SQLException {
		Statement st = conn.createStatement();
		String sql = "select ADE_ID,DTSTART,DTEND from tree_object_22 join (select ADE_ID, EVENT_ID from correspondence_22 join (SELECT UID FROM correspondence_22 join event_22 on event_22.UID = correspondence_22.EVENT_ID WHERE ADE_ID=\""
				+ id
				+ "\" and date(event_22.DTSTART) LIKE '2015-01-23') as tmp1 on correspondence_22.EVENT_ID = tmp1.UID) as tmp2 on tree_object_22.id=tmp2.ADE_ID join event_22 on tmp2.EVENT_ID=event_22.UID WHERE NAME NOT LIKE \"%"
				+ room + "%\"";
		//System.out.println(sql);
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

	/**
	 * read a file and insert data in the database
	 * 
	 * @param path_file
	 * @param st
	 * @param id
	 * @return
	 */
	private static boolean readFileTxt(String path_file, Statement st, String id) {
		try {
			FileReader fileToRead = new FileReader(path_file);
			BufferedReader bf = new BufferedReader(fileToRead);
			String aLine;
			while ((aLine = bf.readLine()) != null) {
				String newLine = aLine.replaceAll("X", id);
				System.out.println(newLine);
				try {
					st.executeUpdate(newLine);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			bf.close();
			return true;
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		return false;
	}
}
