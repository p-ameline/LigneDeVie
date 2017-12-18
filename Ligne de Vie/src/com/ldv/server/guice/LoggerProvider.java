package com.ldv.server.guice;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import com.ldv.server.DbParameters;
import com.ldv.server.Logger;

@Singleton
public class LoggerProvider implements Provider<Logger>
{
	private DbParameters _dbparameters ;
	
	@Inject
	public LoggerProvider(DbParameters dbparameters)
	{
		_dbparameters = dbparameters ;
	}
	
	@Override
	public Logger get()
	{
		return new Logger(_dbparameters) ;
	}
}

