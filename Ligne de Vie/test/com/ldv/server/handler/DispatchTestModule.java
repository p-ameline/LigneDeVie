package com.ldv.server.handler ;

import net.customware.gwt.dispatch.client.standard.StandardDispatchService;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

public class DispatchTestModule extends AbstractModule
{
	@Override
	protected void configure()
	{
		bind(StandardDispatchService.class).to(DispatchTestService.class).in(Singleton.class);
	}
}
