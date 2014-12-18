package com.esir.nsoc.ade.parser;

import net.fortuna.ical4j.model.Component;
import net.fortuna.ical4j.model.DateTime;
import net.fortuna.ical4j.model.Property;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.model.Calendar;

public class Cal {
	
	private Set<ADE_Event> set;
	
	public Cal(String content) {
		try {
			//FileInputStream fin = new FileInputStream(
					//"C:\\Users\\Quentin25\\Documents\\NSOC\\Github\\NSOC-2014\\tsen.ade\\calendarManager\\src\\main\\java\\ADECal.ics");

			CalendarBuilder builder = new CalendarBuilder();

			Calendar calendar = builder.build(new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8)));
			//Calendar calendar = builder.build(fin);
			

			set = new HashSet<ADE_Event>();

			// Iterating over a Calendar
			for (Iterator i = calendar.getComponents().iterator(); i.hasNext();) {
				Component component = (Component) i.next();
				ADE_Event ade_event = new ADE_Event();
				System.out.println("Component [" + component.getName() + "]");

				for (Iterator j = component.getProperties().iterator(); j.hasNext();) {
					Property property = (Property) j.next();

					switch (property.getName().toString()) {
					case "UID":
						ade_event.setUid(property.getValue());
						break;
					case "DTSTART":
						DateTime dtstart;

						try {
							dtstart = new DateTime(property.getValue().toString());
							java.text.SimpleDateFormat sdf = 
								     new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							ade_event.setDtstart(sdf.format(dtstart));
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						break;
					case "DTEND":
						DateTime dtend;
						try {
							dtend = new DateTime(property.getValue().toString());
							java.text.SimpleDateFormat sdf = 
								     new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							ade_event.setDtend(sdf.format(dtend));
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						break;
					case "SUMMARY":
						ade_event.setSummary(property.getValue());
						break;
					case "LOCATION":
						ade_event.setLocation(property.getValue());
						break;
					case "DESCRIPTION":
						ade_event.setDescription(property.getValue());
						break;
					}
				}
				set.add(ade_event);
			}

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
	
	public Cal() {
		try {
			FileInputStream fin = new FileInputStream(
					"C:\\Users\\Quentin25\\Documents\\NSOC\\Github\\NSOC-2014\\tsen.ade\\calendarManager\\src\\main\\java\\ADECal.ics");

			CalendarBuilder builder = new CalendarBuilder();

			Calendar calendar = builder.build(fin);

			set = new HashSet<ADE_Event>();

			// Iterating over a Calendar
			for (Iterator i = calendar.getComponents().iterator(); i.hasNext();) {
				Component component = (Component) i.next();
				ADE_Event ade_event = new ADE_Event();
				System.out.println("Component [" + component.getName() + "]");

				for (Iterator j = component.getProperties().iterator(); j.hasNext();) {
					Property property = (Property) j.next();

					switch (property.getName().toString()) {
					case "UID":
						ade_event.setUid(property.getValue());
						break;
					case "DTSTART":
						DateTime dtstart;
						try {
							dtstart = new DateTime(property.getValue().toString());
							java.text.SimpleDateFormat sdf = 
								     new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							ade_event.setDtstart(sdf.format(dtstart));
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						break;
					case "DTEND":
						DateTime dtend;
						try {
							dtend = new DateTime(property.getValue().toString());
							java.text.SimpleDateFormat sdf = 
								     new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							ade_event.setDtend(sdf.format(dtend));
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						break;
					case "SUMMARY":
						ade_event.setSummary(property.getValue());
						break;
					case "LOCATION":
						ade_event.setLocation(property.getValue());
						break;
					case "DESCRIPTION":
						ade_event.setDescription(property.getValue());
						break;
					}
				}
				set.add(ade_event);
			}

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
	
	public Set getSet(){
		return set;
	}
}