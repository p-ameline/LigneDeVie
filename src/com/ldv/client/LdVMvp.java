package com.ldv.client ;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;
import com.ldv.client.gin.LdvGinjector;
import com.ldv.client.mvp.LdvAppPresenter;
import com.ldv.client.ui.LdvResources;
import com.ldv.client.util.LdvSupervisor;

public class LdVMvp implements EntryPoint
{
  private final LdvGinjector injector = GWT.create(LdvGinjector.class) ;

  public void onModuleLoad()
  {
  	LdvResources.INSTANCE.css().ensureInjected();
  	
  	final LdvSupervisor supervisor = injector.getSuperInjector() ;
	  supervisor.setInjector(injector) ;
	  
	  final LdvAppPresenter appPresenter = injector.getAppPresenter() ;
	  
	  // Log.info("LdVMvp.onModuleLoad: loading with query string " + Window.Location.getQueryString()) ;
	 	  
	  String sId   = "" ;
	  String sStep = Window.Location.getParameter("step") ;
	  
	  // Log.info("LdVMvp.onModuleLoad: step " + sStep) ;
		if ((null != sStep) && sStep.equals("creation"))
		{
			sId = Window.Location.getParameter("pid") ;
			// Log.info("LdVMvp.onModuleLoad: pid " + sId) ;
		}
				
	  appPresenter.go(RootPanel.get(), sStep, sId) ;

/*
	  injector.getPlaceManager().fireCurrentPlace() ;
	  LdvWindow window = new LdvWindow(supervisor) ;
	  window.drawWindow() ;	  
*/
  }
}
