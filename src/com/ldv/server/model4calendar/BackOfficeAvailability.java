package com.ldv.server.model4calendar;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Iterator;

import com.ldv.shared.calendar.Availability;
import com.ldv.shared.calendar.Available;

import net.fortuna.ical4j.model.ComponentList;
import net.fortuna.ical4j.model.DateTime;
import net.fortuna.ical4j.model.Dur;
import net.fortuna.ical4j.model.Property;
import net.fortuna.ical4j.model.PropertyList;
import net.fortuna.ical4j.model.TextList;
import net.fortuna.ical4j.model.component.VAvailability;
import net.fortuna.ical4j.model.property.BusyType;
import net.fortuna.ical4j.model.property.Categories ;
import net.fortuna.ical4j.model.property.Clazz;
import net.fortuna.ical4j.model.property.Comment;
import net.fortuna.ical4j.model.property.Contact;
import net.fortuna.ical4j.model.property.Created;
import net.fortuna.ical4j.model.property.DateProperty;
import net.fortuna.ical4j.model.property.Description;
import net.fortuna.ical4j.model.property.DtEnd;
import net.fortuna.ical4j.model.property.DtStamp;
import net.fortuna.ical4j.model.property.DtStart;
import net.fortuna.ical4j.model.property.LastModified;
import net.fortuna.ical4j.model.property.Organizer;
import net.fortuna.ical4j.model.property.Sequence;
import net.fortuna.ical4j.model.property.Summary;
import net.fortuna.ical4j.model.property.Uid;
import net.fortuna.ical4j.model.property.Url;
import net.fortuna.ical4j.model.property.XProperty;

/**
 * A server side Availability object that provides an interface with iCal4j objects
 * 
 * @author Philippe Ameline
 */
public class BackOfficeAvailability extends Availability
{
	public BackOfficeAvailability() {
		super() ;
	}
		
	public BackOfficeAvailability(final Availability availability) {
		super(availability) ;
	}
	
	/**
	 * Instantiate an Availability object form an iCalendar VAvailability Component
	 */
	public void fillFromVAvailability(final VAvailability component)
	{
		if (null == component)
			return ;
		
		// Treat the properties
		//
		PropertyList<Property> properties = component.getProperties() ;
		
		if ((null != properties) && (false == properties.isEmpty()))
			for (Iterator<Property> it = properties.iterator() ; it.hasNext() ; )
				fillFromProperty(it.next()) ;
		
		// Manage the Available sub-components
		//
		ComponentList<net.fortuna.ical4j.model.component.Available> availableList = component.getAvailable() ;
		if ((null == availableList) || availableList.isEmpty())
			return ;
		
		for (Iterator<net.fortuna.ical4j.model.component.Available> it = availableList.iterator() ; it.hasNext() ; )
		{
			BackOfficeAvailable boAvailable = new BackOfficeAvailable() ;
			boAvailable.fillFromVAvailable(it.next()) ;
			
			// Add to the list of Available
			//
			addToAvailables(boAvailable) ;
		}
	}
		
	/**
	 * Initialize one of the internal variables from a iCalendar Property
	 */
	protected void fillFromProperty(final Property property)
	{
		if (null == property)
			return ;
		
		String sPropertyName = property.getName() ; 	
		
		if (Property.DTSTART.equals(sPropertyName))
		{
			setDateStart((java.util.Date) ((DateProperty) property).getDate()) ;
			return ;
		}
		
		// The following are required, but must not occur more than once: dtstamp / uid / 
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
		
		// Either 'dtend' or 'duration' may appear in a 'eventprop', but 'dtend' and 'duration' MUST NOT occur in the same 'availabilityprop'
		// 'duration' MUST NOT be present if 'dtstart' is not present: dtend / duration /
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
		// busytype / class / created / description / dtstart / last-mod / location / organizer / priority /seq / summary / url / 
		//
		if (Property.BUSYTYPE.equals(sPropertyName))
		{
			setDefaultBusyTimeType(property.getValue()) ;
			return ;
		}		
		if (Property.CLASS.equals(sPropertyName))
		{
			setAccessClassification(property.getValue()) ;
			return ;
		}
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
		if (Property.ORGANIZER.equals(sPropertyName))
		{
			setOrganizer(property.getValue()) ;
			return ;
		}
		if (Property.PRIORITY.equals(sPropertyName))
		{
			setPriority(Integer.parseInt(property.getValue())) ;
			return ;
		}
		if (Property.SEQUENCE.equals(sPropertyName))
		{
			setSeq(property.getValue()) ;
			return ;
		}
		if (Property.SUMMARY.equals(sPropertyName))
		{
			setSummary(property.getValue()) ;
			return ;
		}
		if (Property.URL.equals(sPropertyName))
		{
			setUrl(property.getValue()) ;
			return ;
		}
		
		// The following are optional, and MAY occur more than once: categories / comment / contact / x-prop / iana-prop
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
		// x-prop / iana-prop ?
	}
	
	/**
	 * Fill a iCal4j Available component from the content of this object
	 * 
	 * @param vAvailable
	 */
	public void fillVAvailability(VAvailability vAvailability)
	{
		if (null == vAvailability)
			return ;
		
		// Clear all content potentially already there
		//
		resetVAvailability(vAvailability) ;
		
		// Fill available components
		//
		fillAvailables(vAvailability) ;
		
		// Fill properties
		//
		PropertyList<Property> properties = vAvailability.getProperties() ;
		
		if (false == "".equals(_sUID))
			properties.add(new Uid(_sUID)) ;
		if (false == "".equals(_sDescription))
			properties.add(new Description(_sDescription)) ;		
		if (false == "".equals(_sSummary))
			properties.add(new Summary(_sSummary)) ;
		
		if (false == "".equals(_sDefaultBusyTimeType))
			properties.add(new BusyType(_sDefaultBusyTimeType)) ;
		if (false == "".equals(_sAccessClassification))
			properties.add(new Clazz(_sAccessClassification)) ;
		if (false == "".equals(_sOrganizer))
			try {
				properties.add(new Organizer(_sOrganizer)) ;
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		if (false == "".equals(_sSeq))
			properties.add(new Sequence(_sSeq)) ;
		if (false == "".equals(_sUrl))
			try {
				properties.add(new Url(new URI(_sUrl))) ;
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
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
	 * Fill a <code>ComponentList<Available></code> from the list of Available components  
	 */
	protected void fillAvailables(VAvailability vAvailability)
	{
		if ((null == vAvailability) || (null == _aAvailables) || _aAvailables.isEmpty())
			return ;
		
		ComponentList<net.fortuna.ical4j.model.component.Available> availables = vAvailability.getAvailable() ;
		
		for (Iterator<Available> it = _aAvailables.iterator() ; it.hasNext() ; )
		{
			BackOfficeAvailable boAvailable = new BackOfficeAvailable(it.next()) ;
			
			net.fortuna.ical4j.model.component.Available avlble = new net.fortuna.ical4j.model.component.Available() ;
			boAvailable.fillVAvailable(avlble) ;
			
			availables.add(avlble) ;
		}
	}
	
	/**
	 * Clear all properties and components from a VAvailability object
	 */
	public void resetVAvailability(VAvailability vAvailability)
	{
		if (null == vAvailability)
			return ;
		
		PropertyList<Property> properties = vAvailability.getProperties() ;
		if (null != properties)
			properties.clear() ;
		
		ComponentList<net.fortuna.ical4j.model.component.Available> availables = vAvailability.getAvailable() ;
		if (null != availables)
			availables.clear() ;
	}	
}
