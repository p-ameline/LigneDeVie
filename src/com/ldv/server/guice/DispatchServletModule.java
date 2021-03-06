package com.ldv.server.guice;

import net.customware.gwt.dispatch.server.guice.GuiceStandardDispatchServlet;
import com.google.inject.servlet.ServletModule ;

public class DispatchServletModule extends ServletModule
{
	@Override
	public void configureServlets()
	{
		// NOTE: the servlet context will probably need changing
		// serve("/ligne_de_vie/dispatch").with(DispatchServiceServlet.class) ;
		serve("/ligne_de_vie/dispatch").with(GuiceStandardDispatchServlet.class) ;
	}
}
