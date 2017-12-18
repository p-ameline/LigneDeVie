package com.ldv.client.util_agenda;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.URISyntaxException;
import java.util.ServiceLoader;

/**
 * A factory for creating iCalendar parameters.
 * <p/>
 *
 * Forked from net.fortuna.ical4j.model.ParameterFactoryImpl for GWT compilability reasons
 */
public class CalendarParameterFactoryImpl extends CalendarAbstractContentFactory<CalendarParameterFactory>
{
	private static CalendarParameterFactoryImpl instance = new CalendarParameterFactoryImpl() ;

	protected CalendarParameterFactoryImpl() {
		super(ServiceLoader.load(CalendarParameterFactory.class, CalendarParameterFactory.class.getClassLoader())) ;
	}

	/**
	 * @return Returns the instance.
	 */
	public static CalendarParameterFactoryImpl getInstance() {
		return instance;
	}

	@Override
	protected boolean factorySupports(CalendarParameterFactory factory, String key) {
		return factory.supports(key) ;
	}

	/**
	 * Creates a parameter.
	 *
	 * @param name  name of the parameter
	 * @param value a parameter value
	 * @return a component
	 * @throws URISyntaxException thrown when the specified string is not a valid representation of a URI for selected
	 *                            parameters
	 */
	public CalendarParameter createParameter(final String name, final String value) throws URISyntaxException
	{
		final CalendarParameterFactory factory = getFactory(name) ;
		CalendarParameter parameter ;
		if (null != factory) 
			parameter = factory.createParameter(value) ;
		else if (isExperimentalName(name))
			parameter = new CalendarXParameter(name, value) ;
		else if (allowIllegalNames()) 
			parameter = new CalendarXParameter(name, value) ;
		else
			throw new IllegalArgumentException(String.format("Unsupported parameter name: %s", name)) ;    
		return parameter;
	}

	/**
	 * @param name
	 * @return
	 */
	private boolean isExperimentalName(final String name) {
		return name.startsWith(CalendarParameter.EXPERIMENTAL_PREFIX) && name.length() > CalendarParameter.EXPERIMENTAL_PREFIX.length() ;
	}
    
	/**
	 * Needed for initializing the transient member after deserializing a <code>Calendar</code>
	 * 
	 * @param in
	 * 
	 */
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException 
	{
		in.defaultReadObject() ;
		this.factoryLoader = ServiceLoader.load(CalendarParameterFactory.class, CalendarParameterFactory.class.getClassLoader());
	}
}
