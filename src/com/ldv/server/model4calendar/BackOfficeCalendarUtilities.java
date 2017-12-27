package com.ldv.server.model4calendar;

import java.util.Iterator;
import java.util.Vector;

import com.ldv.shared.model.LdvTime;

import net.fortuna.ical4j.model.DateList;
import net.fortuna.ical4j.model.DateTime;
import net.fortuna.ical4j.model.TextList;

public class BackOfficeCalendarUtilities
{
	/**
	 * Get a TextList from a vector of String
	 */
	public static TextList getTextList(final Vector<String> vector)
	{
		if ((null == vector) || vector.isEmpty())
			return null ;
		
		TextList list = new TextList() ;
		
		for (Iterator<String> it = vector.iterator() ; it.hasNext() ; )
			list.add(it.next()) ;
		
		return list ;
	}
	
	/**
	 * Get a DateList from a vector of LdvTime objects
	 */
	public static DateList getDateList(final Vector<LdvTime> vector)
	{
		if ((null == vector) || vector.isEmpty())
			return null ;
		
		DateList list = new DateList() ;
		
		for (Iterator<LdvTime> it = vector.iterator() ; it.hasNext() ; )
			list.add(new DateTime(it.next().toJavaDate())) ;
		
		return list ;
	}
}
