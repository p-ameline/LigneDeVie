package com.ldv.client.mvp ;

import net.customware.gwt.dispatch.client.DispatchAsync;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.inject.Inject;

//
// MVP controller 
//

public class LdvAppPresenter
{
  private HasWidgets       _container ;
  private LdvMainPresenter _mainPresenter ;
  //private LdvTimeControlPresenter _ldvTimeControlPresenter;
  
  @Inject
  public LdvAppPresenter(final DispatchAsync dispatcher, final LdvMainPresenter mainPresenter)
  {
  	_mainPresenter = mainPresenter ;
  }

/*
  @Inject
  public LdvAppPresenter(final DispatchAsync dispatcher, final LdvTimeControlPresenter ldvTimeControlPresenter)
  {
	  _ldvTimeControlPresenter = ldvTimeControlPresenter ;
	
  }
*/
  	
  public void go(final HasWidgets container, String sStep, String sId)
  {
    _container = container ;
		
    showMain() ;
    
    // sStep = "creation" ;
    // sId   = "0807f4291e332c385673983d8d4b0d40b7ab6781" ;
    
    if ((null != sStep) && (sStep.equals("") == false))
    {
    	Log.info("LdvAppPresenter.go: entering for step " + sStep) ;
    	
    	if (sStep.equals("creation"))
    	{
    		Log.info("LdvAppPresenter.go: going to validation page for pid " + sId) ;
    		_mainPresenter.goToValidatingPage(sId) ;
    		return ;
    	}
    }
    else
    	Log.info("LdvAppPresenter.go: entering with no specific step") ;
    
    // showMain() ;
  }
  
  /**
   * Open main page
   */
  private void showMain()
  {
    _container.clear() ;
    _container.add(_mainPresenter.getDisplay().asWidget()) ;
  }
}
