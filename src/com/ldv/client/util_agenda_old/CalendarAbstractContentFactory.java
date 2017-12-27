package com.ldv.client.util_agenda;

import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;

/**
 * Abstract implementation of a content factory.
 *
 * Forked from net.fortuna.ical4j.model.ParameterFactoryImpl for GWT compilability reasons
 */
public abstract class CalendarAbstractContentFactory<T>
{
	private   final     Map<String, T>   extendedFactories ;
	protected transient ServiceLoader<T> factoryLoader;

	/**
	 * Default constructor.
	 */
	public CalendarAbstractContentFactory(ServiceLoader<T> factoryLoader)
	{
		extendedFactories = new HashMap<String, T>() ;
		this.factoryLoader = factoryLoader ;
	}

	/**
	 * Register a non-standard content factory.
	 * @deprecated Define extensions in META-INF/services/net.fortuna.ical4j.model.[Type]Factory
	 */
	@Deprecated
	protected final void registerExtendedFactory(String key, T factory) {
		extendedFactories.put(key, factory) ;
	}

	protected abstract boolean factorySupports(T factory, String key) ;

	/**
	 * @param key a factory key
	 * @return a factory associated with the specified key, giving preference to
	 * standard factories
	 */
	protected final T getFactory(String key)
	{
		T factory = null ;
		for (T candidate : factoryLoader)
		{
			if (factorySupports(candidate, key))
			{
				factory = candidate ;
				break ;
			}
		}
		if (null == factory)
			factory = extendedFactories.get(key) ;
		return factory ;
	}

	/**
	 * @return true if non-standard names are allowed, otherwise false
	 */
	protected boolean allowIllegalNames() {
		return CalendarCompatibilityHints.isHintEnabled(CalendarCompatibilityHints.KEY_RELAXED_PARSING) ;
	}
}
