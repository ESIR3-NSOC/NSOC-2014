package fr.esir.nsoc.tsen.ade.core;

import fr.esir.nsoc.tsen.ade.database.DataBase;
import fr.esir.nsoc.tsen.ade.database.MySQLDB;
import fr.esir.nsoc.tsen.ade.object.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jbourcie on 10/03/2015.
 */
public class ExtractInfoFromBDD {

    public static void main(String ... args){
        Connection _connection = null;
        boolean _connected = false;
        String _login;
        String _password;
        String driver = "com.mysql.jdbc.Driver";
        String projectID = "22";

        String url="jdbc:mysql://localhost:3306/" + "tsen_ade";
        _login = "root";
        _password = "test";
        try {
            Class.forName(driver);
            _connection = DriverManager.getConnection(url, _login, _password);
            if(_connection.isValid(5000)) _connected=true;

        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }



        Statement stmt = null;
        ResultSet rs = null;
        Map<String, Course> courses = new HashMap<String, Course>();
        // Map<ROOM, MAP<SUTUDENT_GROUP_NAME, List<Course>>>
        Map<String, Map<String, List<Course>>> coursesInComputerRooms = new HashMap<String, Map<String, List<Course>>>();
        try {
            //gather all events and store them in memory
            String query = "SELECT *,Hour(Timediff(`dtend`, `dtstart`)) AS 'duree' FROM event_22 JOIN correspondence_22 ON event_22.UID = correspondence_22.EVENT_ID ORDER BY event_22.DTSTART;";
            stmt = _connection.createStatement();
            rs = stmt.executeQuery(query);
            int indication = 0;
            while (rs.next( )){
                indication++;
                if (courses.containsKey(rs.getString("UID")) ) {
                    Course course = courses.get(rs.getString("UID"));
                    // search for what information to add
                    query = "SELECT * FROM tree_object_22 WHERE ID = " + rs.getString("ADE_ID");
                    Statement stmt2 = _connection.createStatement();
                    ResultSet rs2 = stmt2.executeQuery(query);
                    rs2.next( ) ;
                    String parent = rs2.getString("PARENT_ID");
                    while (isRecognizable(parent)==null){
                        query = "SELECT * FROM tree_object_22 WHERE ID = " + parent;
                        Statement stmt3 = _connection.createStatement();
                        ResultSet rs3 = stmt3.executeQuery(query);
                        rs3.next();
                        parent = rs3.getString("PARENT_ID");
                        stmt3.close();
                    }
                    if (indication%100 == 0){
                        System.out.println(indication);
                    }
                    //System.out.println(isRecognizable(parent) +", " + rs.getString("SUMMARY"));
                    if (isRecognizable(parent)!=null) {
                        String recognizedParent = isRecognizable(parent);
                        if (recognizedParent.equalsIgnoreCase("room")) {
                            course.setRoomID(rs2.getString("ID"));
                            course.setRoomName(rs2.getString("NAME"));
                        }
                        if (recognizedParent.equalsIgnoreCase("ESIR1")) {
                            course.setStudentGroupName("ESIR1");
                            course.addPeopleId(rs2.getString("ID"));
                        }
                        if (recognizedParent.equalsIgnoreCase("ESIR2")) {
                            course.setStudentGroupName("ESIR2");
                            course.addPeopleId(rs2.getString("ID"));
                        }
                        if (recognizedParent.equalsIgnoreCase("ESIR3")) {
                            course.setStudentGroupName("ESIR3");
                            course.addPeopleId(rs2.getString("ID"));
                        }
                        if (recognizedParent.equalsIgnoreCase("trainee")) {
                            course.setStudentGroupName("Other Students");
                            course.addPeopleId(rs2.getString("ID"));
                        }
                    }
                    stmt2.close();
                }  else {
                    Course course = new Course(rs.getString("UID"), rs.getTime("DTSTART"), rs.getTime("DTEND"), rs.getInt("duree"), rs.getDate("DTSTART"));
                    // find which info to add
                    query = "SELECT * FROM tree_object_22 WHERE ID = " + rs.getString("ADE_ID");
                    Statement stmt2 = _connection.createStatement();
                    ResultSet rs2 = stmt2.executeQuery(query);
                    rs2.next( ) ;
                    String parent = rs2.getString("PARENT_ID");
                    while (isRecognizable(parent)==null){
                        query = "SELECT * FROM tree_object_22 WHERE ID = " + parent;
                        Statement stmt3 = _connection.createStatement();
                        ResultSet rs3 = stmt3.executeQuery(query);
                        rs3.next();
                        parent = rs3.getString("PARENT_ID");
                        stmt3.close();
                    }
                    if (indication%100 == 0){
                        System.out.println(indication);
                    }
                    if (isRecognizable(parent)!=null) {
                        String recognizedParent = isRecognizable(parent);
                        if (recognizedParent.equalsIgnoreCase("room")) {
                            course.setRoomID(rs2.getString("ID"));
                            course.setRoomName(rs2.getString("NAME"));
                        }
                        if (recognizedParent.equalsIgnoreCase("ESIR1")) {
                            course.setStudentGroupName("ESIR1");
                            course.addPeopleId(rs2.getString("ID"));
                        }
                        if (recognizedParent.equalsIgnoreCase("ESIR2")) {
                            course.setStudentGroupName("ESIR2");
                            course.addPeopleId(rs2.getString("ID"));
                        }
                        if (recognizedParent.equalsIgnoreCase("ESIR3")) {
                            course.setStudentGroupName("ESIR3");
                            course.addPeopleId(rs2.getString("ID"));
                        }
                        if (recognizedParent.equalsIgnoreCase("trainee")) {
                            course.setStudentGroupName("Other Students");
                            course.addPeopleId(rs2.getString("ID"));
                        }
                    }
                    courses.put(rs.getString("UID"), course);
                    stmt2.close();
                }
            }

            stmt.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("-------------" + courses.keySet().size());

        for (Course course : courses.values()){
            if (coursesInComputerRooms.keySet().contains(course.getRoomID())){
                Map<String, List<Course>> map = coursesInComputerRooms.get(course.getRoomID()) ;
                if (map.keySet().contains(course.getStudentGroupName())){
                    map.get(course.getStudentGroupName()).add(course);
                }  else {
                    List list = new ArrayList<Course>();
                    list.add(course);
                    map.put(course.getStudentGroupName(), list);
                }
            } else {
                // check if the course is in one of the computer science lab room
                if (course.getRoomID().equalsIgnoreCase("4143") || course.getRoomID().equalsIgnoreCase("4126") ||course.getRoomID().equalsIgnoreCase("4128") ||course.getRoomID().equalsIgnoreCase("4127") ||course.getRoomID().equalsIgnoreCase("4108") ||course.getRoomID().equalsIgnoreCase("4140") ||course.getRoomID().equalsIgnoreCase("4138") ||course.getRoomID().equalsIgnoreCase("4132") ||course.getRoomID().equalsIgnoreCase("4137")){
                    Map<String, List<Course>> map = new HashMap<String, List<Course>>();
                    List list = new ArrayList<Course>();
                    list.add(course);
                    map.put(course.getStudentGroupName(), list);
                    coursesInComputerRooms.put(course.getRoomID(), map);
                }
            }

        }

        Map<Date,Map<String,DayRoomUsage>> roomUsageByDate = new HashMap<Date,Map<String,DayRoomUsage>>();
        Map<Date, DayRoomUsageByHours> roomUsageByDateByHours =  new HashMap<Date,DayRoomUsageByHours>();
        for (String roomID : coursesInComputerRooms.keySet()){
            System.out.println("Room : " + roomID);
            for (String studentGroupName : coursesInComputerRooms.get(roomID).keySet()){
                List<Course> coursesInRoom = coursesInComputerRooms.get(roomID).get(studentGroupName);
                System.out.println("  " + studentGroupName + ": " + coursesInRoom.size());
                for (Course cir : coursesInRoom){
                    if (roomUsageByDate.containsKey(cir.getDate())){
                        //we already have this date in the big map
                        if (roomUsageByDate.get(cir.getDate()).containsKey(cir.getRoomID())){
                            // we already have this room in the small map
                            roomUsageByDate.get(cir.getDate()).get(cir.getRoomID()).addCourseToDayRoomUsage(cir);
                        } else {
                            // we don't have this room in the small map
                            DayRoomUsage dru = new DayRoomUsage(cir.getRoomID());
                            dru.setRoomName(cir.getRoomName());
                            dru.addCourseToDayRoomUsage(cir);
                            roomUsageByDate.get(cir.getDate()).put(cir.getRoomID(), dru );
                        }
                    }  else {
                        Map<String,DayRoomUsage> tempMap = new HashMap<String,DayRoomUsage>();
                        DayRoomUsage dru = new DayRoomUsage(cir.getRoomID());
                        dru.setRoomName(cir.getRoomName());
                        dru.addCourseToDayRoomUsage(cir);
                        tempMap.put(cir.getRoomID(), dru );
                        roomUsageByDate.put(cir.getDate(), tempMap);
                    }

                    if (roomUsageByDateByHours.containsKey(cir.getDate())){
                        roomUsageByDateByHours.get(cir.getDate()).addCourse(cir);
                    } else {
                        DayRoomUsageByHours drubh = new DayRoomUsageByHours(cir.getDate());
                        drubh.addCourse(cir);
                        roomUsageByDateByHours.put(cir.getDate(),drubh);
                    }
                }
            }
        }

        try {
            File file = new File("usage-Stat.txt");

            //if file doesnt exists, then create it
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fileWritter = new FileWriter(file.getName());
            BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
            bufferWritter.write("Date;Room Name;ESIR Ratio;ESIR Total Ratio;Room Usage Total Ratio;ESIR Total Usage;ESIR 1; ESIR 2; ESIR 3; ISTIC\n");
            for (Date date : roomUsageByDate.keySet()) {
                System.out.println("On : " + date);
                for (DayRoomUsage dru : roomUsageByDate.get(date).values()) {
                    System.out.println("  " + dru.getRoomName() + " : ESIR ratio: " + dru.getEsirUsageRatio() + " % | ESIR total ratio: " + dru.getRoomEsirUsageRatio() + " % | Room usage ratio: " + dru.getRoomUsageRatio() + " % | ESIR total Usage: " + dru.getEsirUsage() + "| ESIR1 usage: " + dru.getNumberOfHoursESIR1() + "| ESIR2 usage: " + dru.getNumberOfHoursESIR2() + "| ESIR3 usage: " + dru.getNumberOfHoursESIR3() + "| ISTIC usage: " + dru.getNumberOfHoursUndefined());
                    bufferWritter.write(date+ ";" + dru.getRoomName() + ";" + dru.getEsirUsageRatio() + ";" + dru.getRoomEsirUsageRatio() + ";" + dru.getRoomUsageRatio() + ";" + dru.getEsirUsage() + ";" + dru.getNumberOfHoursESIR1() + ";" + dru.getNumberOfHoursESIR2() + ";" + dru.getNumberOfHoursESIR3() + ";" + dru.getNumberOfHoursUndefined()+"\n");

                }
            }
            bufferWritter.close();
        } catch (Exception e){
            e.printStackTrace();
        }
        try {
            File fileESIR = new File("usage-Stat-by-hours-ESIR.txt");
            File fileISTIC = new File("usage-Stat-by-hours-ISTIC.txt");
            File fileTOTAL = new File("usage-Stat-by-hours-TOTAL.txt");
            File fileESIRNumber = new File("usage-Stat-by-hours-Number-ESIR.txt");
            File fileISTICNumber = new File("usage-Stat-by-hours-Number-ISTIC.txt");
            File fileTOTALNumber = new File("usage-Stat-by-hours-Number-TOTAL.txt");

            //if file doesnt exists, then create it
            if (!fileESIR.exists()) {
                fileESIR.createNewFile();
            }
            if (!fileISTIC.exists()) {
                fileISTIC.createNewFile();
            }
            if (!fileTOTAL.exists()) {
                fileTOTAL.createNewFile();
            }
            if (!fileESIRNumber.exists()) {
                fileESIRNumber.createNewFile();
            }
            if (!fileISTICNumber.exists()) {
                fileISTICNumber.createNewFile();
            }
            if (!fileTOTALNumber.exists()) {
                fileTOTALNumber.createNewFile();
            }

            FileWriter fileWritterESIR = new FileWriter(fileESIR.getName());
            BufferedWriter bufferWritterESIR = new BufferedWriter(fileWritterESIR);
            bufferWritterESIR.write("Date;Windows 8-10;Windows 10-12;Windows 14-16;Windows 16-18;Linux 8-10;Linux 10-12;Linux 14-16;Linux 16-18\n");
            FileWriter fileWritterISTIC = new FileWriter(fileISTIC.getName());
            BufferedWriter bufferWritterISTIC = new BufferedWriter(fileWritterISTIC);
            bufferWritterISTIC.write("Date;Windows 8-10;Windows 10-12;Windows 14-16;Windows 16-18;Linux 8-10;Linux 10-12;Linux 14-16;Linux 16-18\n");
            FileWriter fileWritterTOTAL = new FileWriter(fileTOTAL.getName());
            BufferedWriter bufferWritterTOTAL = new BufferedWriter(fileWritterTOTAL);
            bufferWritterTOTAL.write("Date;Windows 8-10;Windows 10-12;Windows 14-16;Windows 16-18;Linux 8-10;Linux 10-12;Linux 14-16;Linux 16-18\n");
            FileWriter fileWritterESIRNumber = new FileWriter(fileESIRNumber.getName());
            BufferedWriter bufferWritterESIRNumber = new BufferedWriter(fileWritterESIRNumber);
            bufferWritterESIRNumber.write("Date;Windows 8-10;Windows 10-12;Windows 14-16;Windows 16-18;Linux 8-10;Linux 10-12;Linux 14-16;Linux 16-18\n");
            FileWriter fileWritterISTICNumber = new FileWriter(fileISTICNumber.getName());
            BufferedWriter bufferWritterISTICNumber = new BufferedWriter(fileWritterISTICNumber);
            bufferWritterISTICNumber.write("Date;Windows 8-10;Windows 10-12;Windows 14-16;Windows 16-18;Linux 8-10;Linux 10-12;Linux 14-16;Linux 16-18\n");
            FileWriter fileWritterTOTALNumber = new FileWriter(fileTOTALNumber.getName());
            BufferedWriter bufferWritterTOTALNumber = new BufferedWriter(fileWritterTOTALNumber);
            bufferWritterTOTALNumber.write("Date;Windows 8-10;Windows 10-12;Windows 14-16;Windows 16-18;Linux 8-10;Linux 10-12;Linux 14-16;Linux 16-18\n");


            for (Date date : roomUsageByDateByHours.keySet()) {
                System.out.println("On : " + date);
                DayRoomUsageByHours drubh =roomUsageByDateByHours.get(date);
                System.out.println("  " + drubh.getWindowsRoomUsed8_10().size() + ";" + drubh.getWindowsRoomUsed10_12().size() + ";" + drubh.getWindowsRoomUsed14_16().size() + ";" + drubh.getWindowsRoomUsed16_18().size() +";" +  drubh.getLinuxRoomUsed8_10().size() + ";" + drubh.getLinuxRoomUsed10_12().size()+";" +  drubh.getLinuxRoomUsed14_16().size() + ";" + drubh.getLinuxRoomUsed16_18().size());
                bufferWritterESIR.write(date + ";" + drubh.generateESIRStringFromCourseList(drubh.getWindowsRoomUsed8_10()) + ";" + drubh.generateESIRStringFromCourseList(drubh.getWindowsRoomUsed10_12()) + ";" + drubh.generateESIRStringFromCourseList(drubh.getWindowsRoomUsed14_16()) + ";" + drubh.generateESIRStringFromCourseList(drubh.getWindowsRoomUsed16_18()) + ";" + drubh.generateESIRStringFromCourseList(drubh.getLinuxRoomUsed8_10()) + ";" + drubh.generateESIRStringFromCourseList(drubh.getLinuxRoomUsed10_12()) + ";" + drubh.generateESIRStringFromCourseList(drubh.getLinuxRoomUsed14_16()) + ";" + drubh.generateESIRStringFromCourseList(drubh.getLinuxRoomUsed16_18())+"\n");
                bufferWritterISTIC.write(date + ";" + drubh.generateISTICStringFromCourseList(drubh.getWindowsRoomUsed8_10()) + ";" + drubh.generateISTICStringFromCourseList(drubh.getWindowsRoomUsed10_12()) + ";" + drubh.generateISTICStringFromCourseList(drubh.getWindowsRoomUsed14_16()) + ";" + drubh.generateISTICStringFromCourseList(drubh.getWindowsRoomUsed16_18()) + ";" + drubh.generateISTICStringFromCourseList(drubh.getLinuxRoomUsed8_10()) + ";" + drubh.generateISTICStringFromCourseList(drubh.getLinuxRoomUsed10_12()) + ";" + drubh.generateISTICStringFromCourseList(drubh.getLinuxRoomUsed14_16()) + ";" + drubh.generateISTICStringFromCourseList(drubh.getLinuxRoomUsed16_18())+"\n");
                bufferWritterTOTAL.write(date + ";" + drubh.generateTOTALStringFromCourseList(drubh.getWindowsRoomUsed8_10()) + ";" + drubh.generateTOTALStringFromCourseList(drubh.getWindowsRoomUsed10_12()) + ";" + drubh.generateTOTALStringFromCourseList(drubh.getWindowsRoomUsed14_16()) + ";" + drubh.generateTOTALStringFromCourseList(drubh.getWindowsRoomUsed16_18()) + ";" + drubh.generateTOTALStringFromCourseList(drubh.getLinuxRoomUsed8_10()) + ";" + drubh.generateTOTALStringFromCourseList(drubh.getLinuxRoomUsed10_12()) + ";" + drubh.generateTOTALStringFromCourseList(drubh.getLinuxRoomUsed14_16()) + ";" + drubh.generateTOTALStringFromCourseList(drubh.getLinuxRoomUsed16_18())+"\n");

                bufferWritterESIRNumber.write(date + ";" + drubh.getESIRNumberFromCourseList(drubh.getWindowsRoomUsed8_10()) + ";" + drubh.getESIRNumberFromCourseList(drubh.getWindowsRoomUsed10_12()) + ";" + drubh.getESIRNumberFromCourseList(drubh.getWindowsRoomUsed14_16()) + ";" + drubh.getESIRNumberFromCourseList(drubh.getWindowsRoomUsed16_18()) + ";" + drubh.getESIRNumberFromCourseList(drubh.getLinuxRoomUsed8_10()) + ";" + drubh.getESIRNumberFromCourseList(drubh.getLinuxRoomUsed10_12()) + ";" + drubh.getESIRNumberFromCourseList(drubh.getLinuxRoomUsed14_16()) + ";" + drubh.getESIRNumberFromCourseList(drubh.getLinuxRoomUsed16_18())+"\n");
                bufferWritterISTICNumber.write(date + ";" + drubh.getISTICNumberFromCourseList(drubh.getWindowsRoomUsed8_10()) + ";" + drubh.getISTICNumberFromCourseList(drubh.getWindowsRoomUsed10_12()) + ";" + drubh.getISTICNumberFromCourseList(drubh.getWindowsRoomUsed14_16()) + ";" + drubh.getISTICNumberFromCourseList(drubh.getWindowsRoomUsed16_18()) + ";" + drubh.getISTICNumberFromCourseList(drubh.getLinuxRoomUsed8_10()) + ";" + drubh.getISTICNumberFromCourseList(drubh.getLinuxRoomUsed10_12()) + ";" + drubh.getISTICNumberFromCourseList(drubh.getLinuxRoomUsed14_16()) + ";" + drubh.getISTICNumberFromCourseList(drubh.getLinuxRoomUsed16_18())+"\n");
                bufferWritterTOTALNumber.write(date + ";" + drubh.getTOTALNumberFromCourseList(drubh.getWindowsRoomUsed8_10()) + ";" + drubh.getTOTALNumberFromCourseList(drubh.getWindowsRoomUsed10_12()) + ";" + drubh.getTOTALNumberFromCourseList(drubh.getWindowsRoomUsed14_16()) + ";" + drubh.getTOTALNumberFromCourseList(drubh.getWindowsRoomUsed16_18()) + ";" + drubh.getTOTALNumberFromCourseList(drubh.getLinuxRoomUsed8_10()) + ";" + drubh.getTOTALNumberFromCourseList(drubh.getLinuxRoomUsed10_12()) + ";" + drubh.getTOTALNumberFromCourseList(drubh.getLinuxRoomUsed14_16()) + ";" + drubh.getTOTALNumberFromCourseList(drubh.getLinuxRoomUsed16_18())+"\n");
            }
            bufferWritterESIR.close();
            bufferWritterISTIC.close();
            bufferWritterTOTAL.close();
            bufferWritterESIRNumber.close();
            bufferWritterISTICNumber.close();
            bufferWritterTOTALNumber.close();
        } catch (Exception e){
            e.printStackTrace();
        }

    }

    private static String isRecognizable(String parent) {
        if (parent.equalsIgnoreCase("5864") )
            return "ESIR1";
        if (parent.equalsIgnoreCase("5598") )
            return "ESIR2";
        if (parent.equalsIgnoreCase("5856") )
            return "ESIR3";
        if (parent.equalsIgnoreCase("room") )
            return "room";
        if (parent.equalsIgnoreCase("instructor") )
            return "instructor";
        if (parent.equalsIgnoreCase("trainee") )
            return "trainee";
        return null;
    }
}
