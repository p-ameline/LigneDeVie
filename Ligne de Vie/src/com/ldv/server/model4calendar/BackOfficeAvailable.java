package com.ldv.server.model4calendar;

import java.text.ParseException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.ldv.shared.calendar.Available;
import com.ldv.shared.calendar.NumberList;
import com.ldv.shared.model.LdvTime;

import net.fortuna.ical4j.model.DateList;
import net.fortuna.ical4j.model.DateTime;
import net.fortuna.ical4j.model.Dur;
import net.fortuna.ical4j.model.Property;
import net.fortuna.ical4j.model.PropertyList;
import net.fortuna.ical4j.model.Recur;
import net.fortuna.ical4j.model.TextList;
import net.fortuna.ical4j.model.WeekDay;
import net.fortuna.ical4j.model.property.Categories ;
import net.fortuna.ical4j.model.property.Comment;
import net.fortuna.ical4j.model.property.Contact;
import net.fortuna.ical4j.model.property.Created;
import net.fortuna.ical4j.model.property.DateProperty;
import net.fortuna.ical4j.model.property.Description;
import net.fortuna.ical4j.model.property.DtEnd;
import net.fortuna.ical4j.model.property.DtStamp;
import net.fortuna.ical4j.model.property.DtStart;
import net.fortuna.ical4j.model.property.ExDate;
import net.fortuna.ical4j.model.property.LastModified;
import net.fortuna.ical4j.model.property.RDate;
import net.fortuna.ical4j.model.property.RRule;
import net.fortuna.ical4j.model.property.RecurrenceId;
import net.fortuna.ical4j.model.property.Summary;
import net.fortuna.ical4j.model.property.Uid;
import net.fortuna.ical4j.model.property.XProperty;

/**
 * A server side Available object that provides an interface with iCal4j objects
 * 
 * @author Philippe Ameline
 */
public class BackOfficeAvailable extends Available
{
	public BackOfficeAvailable() {
		super() ;
	}
		
	public BackOfficeAvailable(final Available model) {
		super(model) ;
	}
	
	/**
	 * Instantiate an Availability object form an iCalendar VAvailability Component
	 */
	public void fillFromVAvailable(final net.fortuna.ical4j.model.component.Available component)
	{
		if (null == component)
			return ;
		
		// Treat the properties
		//
		PropertyList<Property> properties = component.getProperties() ;
		if ((null == properties) || properties.isEmpty())
			return ;
			
		for (Iterator<Property> it = properties.iterator() ; it.hasNext() ; )
			fillFromProperty(it.next()) ;
	}
	
	/**
	 * Initialize one of the internal variables from a iCalendar Property
	 */
	protected void fillFromProperty(final Property property)
	{
		if (null == property)
			return ;
		
		String sPropertyName = property.getName() ; 	
		
		// The following are required, but must not occur more than once: dtstamp / dtstart / uid / 
		//
		if (Property.UID.equals(sPropertyName))
		{
			setUID(property.getValue()) ;
			return ;
		}
		if (Property.DTSTAMP.equals(sPropertyName))
		{
			setDateStamp((java.util.Date) ((DateProperty) property).getDate()) ;
			return ;
		}
		if (Property.DTSTART.equals(sPropertyName))
		{
			setDateStart((java.util.Date) ((DateProperty) property).getDate()) ;
			return ;
		}
		
		// Either 'dtend' or 'duration' may appear in a 'eventprop', but 'dtend' and 'duration' MUST NOT occur in the same 'availableprop'
		//
		if (Property.DTEND.equals(sPropertyName))
		{
			setDateEnd((java.util.Date) ((DateProperty) property).getDate()) ;
			return ;
		}
		if (Property.DURATION.equals(sPropertyName))
		{
			setDuration(property.getValue()) ;
			return ;
		}
		
		// The following are optional, but must not occur more than once:
		// created / description / last-mod / location / recurid / rrule / summary / 
		//
		if (Property.CREATED.equals(sPropertyName))
		{
			setDateCreated((java.util.Date) ((DateProperty) property).getDate()) ;
			return ;
		}
		if (Property.DESCRIPTION.equals(sPropertyName))
		{
			setDescription(property.getValue()) ;
			return ;
		}
		if (Property.LAST_MODIFIED.equals(sPropertyName))
		{
			setLastModified((java.util.Date) ((DateProperty) property).getDate()) ;
			return ;
		}
		if (Property.LOCATION.equals(sPropertyName))
		{
			setLocationText(property.getValue()) ;
			return ;
		}
		if (Property.RECURRENCE_ID.equals(sPropertyName))
		{
			setRecurrenceId(property.getValue()) ;
			return ;
		}
		if (Property.RRULE.equals(sPropertyName))
		{
			setRRule(property.getValue()) ;
			return ;
		}
		if (Property.SUMMARY.equals(sPropertyName))
		{
			setSummary(property.getValue()) ;
			return ;
		}
		
		// The following are optional, and MAY occur more than once: categories / comment / contact / exdate / rdate / x-prop / iana-prop
		//
		if (Property.CATEGORIES.equals(sPropertyName))
		{
			addToCategories(property.getValue()) ;
			return ;
		}
		if (Property.COMMENT.equals(sPropertyName))
		{
			addToComments(property.getValue()) ;
			return ;
		}
		if (Property.CONTACT.equals(sPropertyName))
		{
			addToContacts(property.getValue()) ;
			return ;
		}
		if (Property.EXDATE.equals(sPropertyName))
		{
			addToExDates((java.util.Date) ((DateProperty) property).getDate()) ;
			return ;
		}
		if (Property.RDATE.equals(sPropertyName))
		{
			addToRDates((java.util.Date) ((DateProperty) property).getDate()) ;
			return ;
		}
		// TODO x-prop / iana-prop ?
	}
	
	/**
	 * Fill a iCal4j Available component from the content of this object
	 * 
	 * @param vAvailable
	 */
	public void fillVAvailable(net.fortuna.ical4j.model.component.Available vAvailable)
	{
		if (null == vAvailable)
			return ;
		
		// Clear all content potentially already there
		//
		resetVAvailable(vAvailable) ;
		
		// Fill vAvailable
		//
		PropertyList<Property> properties = vAvailable.getProperties() ;
		
		if (false == "".equals(_sUID))
			properties.add(new Uid(_sUID)) ;
		if (false == "".equals(_sDescription))
			properties.add(new Description(_sDescription)) ;		
		if (false == "".equals(_sSummary))
			properties.add(new Summary(_sSummary)) ;
		
		if (false == _tDateStart.isEmpty())
			properties.add(new DtStart(new DateTime(_tDateStart.toJavaDate()))) ;
		if (false == _tDateStamp.isEmpty())
			properties.add(new DtStamp(new DateTime(_tDateStamp.toJavaDate()))) ;
		if (false == _tDateEnd.isEmpty())
			properties.add(new DtEnd(new DateTime(_tDateEnd.toJavaDate()))) ;
		if (false == _tDateCreated.isEmpty())
			properties.add(new Created(new DateTime(_tDateCreated.toJavaDate()))) ;
		if (false == _tLastModified.isEmpty())
			properties.add(new LastModified(new DateTime(_tLastModified.toJavaDate()))) ;
		
		if (false == "".equals(_sRecurrenceId))
			try {
				properties.add(new RecurrenceId(_sRecurrenceId)) ;
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		String sRRule = getRRule() ;
		if (false == "".equals(sRRule))
			try {
				properties.add(new RRule(sRRule)) ;
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		if (false == _aCategories.isEmpty())
		{
			TextList list = BackOfficeCalendarUtilities.getTextList(_aCategories) ;
			if (null != list)
				properties.add(new Categories(list)) ;
		}
		
		if (false == _aComments.isEmpty())
			for (Iterator<String> it = _aComments.iterator() ; it.hasNext() ; )
				properties.add(new Comment(it.next())) ;
		
		if (false == _aContacts.isEmpty())
			for (Iterator<String> it = _aContacts.iterator() ; it.hasNext() ; )
				properties.add(new Contact(it.next())) ;
		
		if (false == _aXProps.isEmpty())
			for (Iterator<String> it = _aXProps.iterator() ; it.hasNext() ; )
				properties.add(new XProperty(it.next())) ;
		
		// TODO determine what to do with _aIanaProps
		
		if (false == _aExDates.isEmpty())
		{
			DateList list = BackOfficeCalendarUtilities.getDateList(_aExDates) ;
			if (null != list)
				properties.add(new ExDate(list)) ;
		}
		
		if (false == _aRDates.isEmpty())
		{
			DateList list = BackOfficeCalendarUtilities.getDateList(_aRDates) ;
			if (null != list)
				properties.add(new RDate(list)) ;
		}
		
		if (false == _duration.isZero())
		{
			if (_duration.hasOnlyWeeks())
				properties.add(new net.fortuna.ical4j.model.property.Duration(new Dur(_duration.getWeeks()))) ;
			else
				properties.add(new net.fortuna.ical4j.model.property.Duration(new Dur(_duration.getDays() + _duration.getWeeks() * 7, _duration.getHours(), _duration.getMinutes(), _duration.getSeconds()))) ;
		}
	
		// TODO Fix this issue
		if (false == _Location.isEmpty())
		{
			
		}		
	}
	
	/**
	 * Clear all properties from an iCal4j Available object
	 */
	public void resetVAvailable(net.fortuna.ical4j.model.component.Available vAvailable)
	{
		if (null == vAvailable)
			return ;
		
		PropertyList<Property> properties = vAvailable.getProperties() ;
		if (null != properties)
			properties.clear() ;
	}	
	
	/**
	 * Initialize the recurrence object from a RRule string
	 */
	protected boolean setRRule(final String sValue)
	{
		_Recurrence.reset() ;
	
		if ((null == sValue) || "".equals(sValue))
			return true ;
		
		try {
			Recur calRecur = new Recur(sValue) ;
			initRecurrence(calRecur) ;
		} catch (ParseException e) {
			return false ;
		}
		
		return true ;
	}
	
	/**
	 * Get a RRule string from the recurrence object
	 */
	protected String getRRule()
	{
		// A recurrence rule MUST contain a FREQ rule part
		//
		if ("".equals(_Recurrence.getFrequency()))
			return "" ;
		
		Recur calRecur = new Recur() ;
		initRecur(calRecur) ;
		return calRecur.toString() ;
	}
	
	/**
	 * Initialize the recurrence object from a net.fortuna.ical4j.model.Recur
	 */
	protected void initRecurrence(final Recur calRecur)
	{
		_Recurrence.reset() ;
		
		if (null == calRecur)
			return ;
		
		_Recurrence.setFrequency(calRecur.getFrequency()) ;
		_Recurrence.setUntil(calRecur.getUntil()) ;
		_Recurrence.setCount(calRecur.getCount()) ;
		_Recurrence.setInterval(calRecur.getInterval()) ;
		
		initNumberList(_Recurrence.getSecondList(),   calRecur.getSecondList()) ;
		initNumberList(_Recurrence.getMinuteList(),   calRecur.getMinuteList()) ;
		initNumberList(_Recurrence.getHourList(),     calRecur.getHourList()) ;
		initNumberList(_Recurrence.getDayList(),      calRecur.getDayList()) ;
		initNumberList(_Recurrence.getMonthDayList(), calRecur.getMonthDayList()) ;
		initNumberList(_Recurrence.getYearDayList(),  calRecur.getYearDayList()) ;
		initNumberList(_Recurrence.getWeekNoList(),   calRecur.getWeekNoList()) ;
		initNumberList(_Recurrence.getMonthList(),    calRecur.getMonthList()) ;
		initNumberList(_Recurrence.getSetPosList(),   calRecur.getSetPosList()) ;
		
		// Process experimental values
		//
		Map<String, String> XPValues = calRecur.getExperimentalValues() ;
		if ((null == XPValues) || XPValues.isEmpty())
			return ;
		
		for (Entry<String, String> entry : XPValues.entrySet())
			_Recurrence.addExperimentalValue(entry.getKey(), entry.getValue()) ;
	}
	
	/**
	 * Initialize a net.fortuna.ical4j.model.Recur object from the recurrence object 
	 */
	protected void initRecur(Recur calRecur)
	{
		if (null == calRecur)
			return ;
		
		String sFrequency = _Recurrence.getFrequency() ;
		
		// A recurrence rule MUST contain a FREQ rule part
		//
		if ("".equals(sFrequency))
			return ;
		
		calRecur.setFrequency(sFrequency) ;
		calRecur.setCount(_Recurrence.getCount()) ;
		calRecur.setInterval(_Recurrence.getInterval()) ;
		
		LdvTime tUntil = _Recurrence.getUntil() ;
		if (false == tUntil.isEmpty())
		{
			DateTime dateTime = new DateTime(tUntil.toJavaDate()) ; 
			calRecur.setUntil(dateTime) ;
		}
		
		initCalendarNumberList(calRecur.getSecondList(),   _Recurrence.getSecondList()) ;
		initCalendarNumberList(calRecur.getMinuteList(),   _Recurrence.getMinuteList()) ;
		initCalendarNumberList(calRecur.getHourList(),     _Recurrence.getHourList()) ;
		initCalendarNumberList(calRecur.getDayList(),      _Recurrence.getDayList()) ;
		initCalendarNumberList(calRecur.getMonthDayList(), _Recurrence.getMonthDayList()) ;
		initCalendarNumberList(calRecur.getYearDayList(),  _Recurrence.getYearDayList()) ;
		initCalendarNumberList(calRecur.getWeekNoList(),   _Recurrence.getWeekNoList()) ;
		initCalendarNumberList(calRecur.getMonthList(),    _Recurrence.getMonthList()) ;
		initCalendarNumberList(calRecur.getSetPosList(),   _Recurrence.getSetPosList()) ;
		
		// Process experimental values
		//
		Map<String, String> XPValues = _Recurrence.getExperimentalValues() ;
		if ((null == XPValues) || XPValues.isEmpty())
			return ;
		
		Map<String, String> recurXPValues = calRecur.getExperimentalValues() ;
		
		for (Entry<String, String> entry : XPValues.entrySet())
			recurXPValues.put(entry.getKey(), entry.getValue()) ;
	}
	
	/**
	 * Initialize a NumberList from an ical4j NumberList
	 */
	protected void initNumberList(NumberList target, final net.fortuna.ical4j.model.NumberList model)
	{
		if (null == target)
			return ;
		
		target.clear() ;
		
		if ((null == model) || model.isEmpty())
			return ;
		
		target.fill(model) ;
	}
	
	/**
	 * Initialize a NumberList from an ical4j WeekDayList
	 */
	protected void initNumberList(NumberList target, final net.fortuna.ical4j.model.WeekDayList model)
	{
		if (null == target)
			return ;
		
		target.clear() ;
		
		if ((null == model) || model.isEmpty())
			return ;
		
		for (Iterator<WeekDay> it = model.iterator() ; it.hasNext() ; )
		{
			WeekDay day = it.next() ;
			switch(day.getDay())
			{
				case MO :
					target.add(1) ;
					break ;
				case TU :
					target.add(2) ;
					break ;
				case WE :
					target.add(3) ;
					break ;
				case TH :
					target.add(4) ;
					break ;
				case FR :
					target.add(5) ;
					break ;
				case SA :
					target.add(6) ;
					break ;
				case SU :
					target.add(7) ;
					break ;
			}
		}
	}
	
	/**
	 * Initialize an ical4j NumberList from a NumberList
	 */
	protected void initCalendarNumberList(net.fortuna.ical4j.model.NumberList target, final NumberList model)
	{
		if (null == target)
			return ;
		
		target.clear() ;
		
		if ((null == model) || model.isEmpty())
			return ;
		
		for (Iterator<Integer> it = model.iterator() ; it.hasNext() ; )
			target.add(it.next()) ;
	}
	
	/**
	 * Initialize an ical4j WeekDayList from a NumberList
	 */
	protected void initCalendarNumberList(net.fortuna.ical4j.model.WeekDayList target, final NumberList model)
	{
		if (null == target)
			return ;
		
		target.clear() ;
		
		if ((null == model) || model.isEmpty())
			return ;
		
		for (Iterator<Integer> it = model.iterator() ; it.hasNext() ; )
		{
			Integer iElem = it.next() ;
			
			if      (1 == iElem)
				target.add(WeekDay.MO) ;
			else if (2 == iElem)
				target.add(WeekDay.TU) ;
			else if (3 == iElem)
				target.add(WeekDay.WE) ;
			else if (4 == iElem)
				target.add(WeekDay.TH) ;
			else if (5 == iElem)
				target.add(WeekDay.FR) ;
			else if (6 == iElem)
				target.add(WeekDay.SA) ;
			else if (7 == iElem)
				target.add(WeekDay.SU) ;
		}
	}
}
