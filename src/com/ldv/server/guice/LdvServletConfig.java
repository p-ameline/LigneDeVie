package com.ldv.server.guice;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;

import com.ldv.server.DbParameters;
import com.ldv.server.Logger;

public class LdvServletConfig extends GuiceServletContextListener
{
	@SuppressWarnings("unused")
	private Logger       _loggerToInstantiate ; 
	private DbParameters _parametersToInstantiate ;
	
	@Override
	protected Injector getInjector() 
	{
		ServerModule serverModule = new ServerModule() ;
		Injector terroirsInjector = Guice.createInjector(serverModule, new DispatchServletModule()) ;
		
		DbParametersProvider parametersProvider = new DbParametersProvider() ;
		_parametersToInstantiate = parametersProvider.get() ;
		
		LoggerProvider loggerProvider = new LoggerProvider(_parametersToInstantiate) ;
		_loggerToInstantiate = loggerProvider.get() ;
		
		Logger.trace("Listener started", -1, Logger.TraceLevel.STEP) ;
		
		return terroirsInjector ;
	}
}
