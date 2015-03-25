package fr.esir.nsoc.tsen.ade.object;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jbourcie on 17/03/2015.
 */
public class DayRoomUsageByHours {

    private Date date;
    private List<Course> windowsRoomUsed8_10;
    private List<Course> windowsRoomUsed10_12;
    private List<Course> windowsRoomUsed14_16;
    private List<Course> windowsRoomUsed16_18;
    private List<Course> linuxRoomUsed8_10;
    private List<Course> linuxRoomUsed10_12;
    private List<Course> linuxRoomUsed14_16;
    private List<Course> linuxRoomUsed16_18;

    public DayRoomUsageByHours(Date date) {
        this.date = date;
        windowsRoomUsed8_10 = new ArrayList<Course>();
        windowsRoomUsed10_12 = new ArrayList<Course>();
        windowsRoomUsed14_16 = new ArrayList<Course>();
        windowsRoomUsed16_18 = new ArrayList<Course>();
        linuxRoomUsed8_10 = new ArrayList<Course>();
        linuxRoomUsed10_12 = new ArrayList<Course>();
        linuxRoomUsed14_16 = new ArrayList<Course>();
        linuxRoomUsed16_18 = new ArrayList<Course>();
    }

    public List<Course> getWindowsRoomUsed8_10() {
        return windowsRoomUsed8_10;
    }

    public List<Course> getWindowsRoomUsed10_12() {
        return windowsRoomUsed10_12;
    }

    public List<Course> getWindowsRoomUsed14_16() {
        return windowsRoomUsed14_16;
    }

    public List<Course> getWindowsRoomUsed16_18() {
        return windowsRoomUsed16_18;
    }

    public List<Course> getLinuxRoomUsed8_10() {
        return linuxRoomUsed8_10;
    }

    public List<Course> getLinuxRoomUsed10_12() {
        return linuxRoomUsed10_12;
    }

    public List<Course> getLinuxRoomUsed14_16() {
        return linuxRoomUsed14_16;
    }

    public List<Course> getLinuxRoomUsed16_18() {
        return linuxRoomUsed16_18;
    }

    public void addCourse(Course c) {
        System.out.println(c.getRoomName() + " starting at : " + c.getStartPoint());
        if (c.getStartPoint().after(Time.valueOf("07:45:00")) && c.getStartPoint().before(Time.valueOf("10:00:00"))) {
            if (isWindows(c.getRoomID())){
                windowsRoomUsed8_10.add(c);
            } else {
                linuxRoomUsed8_10.add(c);
            }

        } else if (c.getStartPoint().after(Time.valueOf("10:00:00")) && c.getStartPoint().before(Time.valueOf("12:00:00"))) {
            if (isWindows(c.getRoomID())){
                windowsRoomUsed10_12.add(c);
            } else {
                linuxRoomUsed10_12.add(c);
            }
        } else if (c.getStartPoint().after(Time.valueOf("13:45:00")) && c.getStartPoint().before(Time.valueOf("16:00:00"))) {
            if (isWindows(c.getRoomID())){
                windowsRoomUsed14_16.add(c);
            } else {
                linuxRoomUsed14_16.add(c);
            }
        } else if (c.getStartPoint().after(Time.valueOf("16:00:00")) && c.getStartPoint().before(Time.valueOf("18:00:00"))) {
            if (isWindows(c.getRoomID())){
                windowsRoomUsed16_18.add(c);
            } else {
                linuxRoomUsed16_18.add(c);
            }
        }  else {
        }


    }

    private boolean isWindows(String roomId){
        return (roomId.equalsIgnoreCase("4140") || roomId.equalsIgnoreCase("4138") || roomId.equalsIgnoreCase("4132") || roomId.equalsIgnoreCase("4137") );
    }

    public String generateESIRStringFromCourseList(List<Course> courses){
        String results = "";
        for (Course course : courses){
            if (course.getStudentGroupName().equalsIgnoreCase("ESIR1") || course.getStudentGroupName().equalsIgnoreCase("ESIR2") || course.getStudentGroupName().equalsIgnoreCase("ESIR3")){
                results += course.getRoomName()+",";
            }
        }
        return results;
    }

    public String generateISTICStringFromCourseList(List<Course> courses){
        String results = "";
        for (Course course : courses){
            if (course.getStudentGroupName().equalsIgnoreCase("undefined")){
                results += course.getRoomName()+",";
            }
        }
        return results;
    }

    public String generateTOTALStringFromCourseList(List<Course> courses){
        String results = "";
        for (Course course : courses){
            results += course.getRoomName()+",";
        }
        return results;
    }

    public int getESIRNumberFromCourseList(List<Course> courses){
        int results = 0;
        for (Course course : courses){
            if (course.getStudentGroupName().equalsIgnoreCase("ESIR1") || course.getStudentGroupName().equalsIgnoreCase("ESIR2") || course.getStudentGroupName().equalsIgnoreCase("ESIR3")){
                results ++;
            }
        }
        return results;
    }

    public int getISTICNumberFromCourseList(List<Course> courses){
        int results = 0;
        for (Course course : courses){
            if (course.getStudentGroupName().equalsIgnoreCase("undefined")){
                results ++;
            }
        }
        return results;
    }

    public int getTOTALNumberFromCourseList(List<Course> courses){
        return courses.size();
    }
}
