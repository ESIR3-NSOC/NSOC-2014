package com.esir.nsoc.ade.parser;

import net.fortuna.ical4j.model.Component;
import net.fortuna.ical4j.model.DateTime;
import net.fortuna.ical4j.model.Property;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.HashSet;
import java.util.Iterator;

import fr.esir.nsoc.tsen.ade.object.Event;
import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.model.Calendar;

public class Cal {

	private String icsContent;

	public Cal(String icsContent) {
		super();
		this.icsContent = icsContent;
	}

	public HashSet<Event> parse() {

		HashSet<Event> _ADE_Events = new HashSet<Event>();
		// Premier mot du HTTP_GET
		String test = icsContent.substring(0, 5);
		// Test si c'est un ICS
		if (test.equals("BEGIN")) {
			try {
				CalendarBuilder builder = new CalendarBuilder();

				Calendar calendar = builder.build(new ByteArrayInputStream(
						icsContent.getBytes(StandardCharsets.UTF_8)));

				// Iterating over a Calendar
				for (Iterator<Component> i = calendar.getComponents()
						.iterator(); i.hasNext();) {
					Component component = i.next();
					Event ade_event = new Event();


					for (Iterator<Property> j = component.getProperties()
							.iterator(); j.hasNext();) {
						Property property = j.next();

						switch (property.getName().toString()) {
						case "UID":
							ade_event.setId(property.getValue());
							break;
						case "DTSTART":
							DateTime dtstart;

							try {
								dtstart = new DateTime(property.getValue()
										.toString());
								java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(
										"yyyy-MM-dd HH:mm:ss");
								ade_event.setDtstart(sdf.format(dtstart));
							} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							break;
						case "DTEND":
							DateTime dtend;
							try {
								dtend = new DateTime(property.getValue()
										.toString());
								java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(
										"yyyy-MM-dd HH:mm:ss");
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
					_ADE_Events.add(ade_event);
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
		return _ADE_Events;
	}
}