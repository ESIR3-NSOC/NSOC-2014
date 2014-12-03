package com.esir3.nsoc2014.ade;

import net.fortuna.ical4j.model.Component;
import net.fortuna.ical4j.model.Property;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;

import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.model.Calendar;

public class Cal {
	public Cal(){
		//FileInputStream fin;
		try {
			/*fin = new FileInputStream("src/main/resource/ADECal.ics");
			CalendarBuilder builder = new CalendarBuilder();
			Calendar calendar = builder.build(fin);
			for (Object obj:  calendar.getComponents()){
				Component cp = (Component)obj;
				System.out.println(cp.getName());
				for (Object obj1:  cp.getProperties()){
					Property prop = (Property)obj1;
					System.out.println(prop.getName());
				}
			}*/
			//Now Parsing an iCalendar file
			  FileInputStream fin = new FileInputStream("src/main/resource/ADECal.ics");

			  CalendarBuilder builder = new CalendarBuilder();

			  Calendar calendar = builder.build(fin);
			  
			  //Iterating over a Calendar
			  for (Iterator i = calendar.getComponents().iterator(); i.hasNext();) {
			      Component component = (Component) i.next();
			      System.out.println("Component [" + component.getName() + "]");

			      for (Iterator j = component.getProperties().iterator(); j.hasNext();) {
			          Property property = (Property) j.next();
			          System.out.println("Property [" + property.getName() + ", " + property.getValue() + "]");
			      }
			  }//for	
			
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

			
	}
	
	public static void main(String[] args){
		Cal cal = new Cal();
	}
}