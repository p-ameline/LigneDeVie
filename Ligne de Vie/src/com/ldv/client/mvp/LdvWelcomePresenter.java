package com.ldv.client.mvp ;

import net.customware.gwt.dispatch.client.DispatchAsync;
import net.customware.gwt.presenter.client.EventBus;
import net.customware.gwt.presenter.client.widget.WidgetDisplay;
import net.customware.gwt.presenter.client.widget.WidgetPresenter;

import com.ldv.client.gin.LdvGinjector;
import com.ldv.client.util.LdvSupervisor;
import com.ldv.client.event.GoToLdvManagerEvent;
import com.ldv.client.event.GoToLdvRegisterPageEvent;
import com.ldv.client.event.IdentifiersProvidedEvent;
import com.ldv.client.event.IdentifiersProvidedEventHandler;
import com.ldv.client.event.LdvOpenEvent;
import com.ldv.client.event.LdvOpenEventHandler;
import com.ldv.client.event.LdvSentEvent;
import com.ldv.client.event.LdvSentEventHandler;
import com.ldv.client.event.LdvValidatorSentEvent;
import com.ldv.client.event.LdvWelcomePageEvent;
import com.ldv.client.event.LdvWelcomePageEventHandler;
import com.ldv.shared.rpc.SendDisconnect;
import com.ldv.shared.rpc.SendDisconnectResult;
import com.ldv.shared.rpc.SendLoginAction;
import com.ldv.shared.rpc.SendLoginResult;

import com.allen_sauer.gwt.log.client.Log;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.inject.Inject;

/**
 * LdvPresenter manages the Welcome page : login, register and presentation
 */
public class LdvWelcomePresenter extends WidgetPresenter<LdvWelcomePresenter.Display>
{	
	private        String  _sSessionId = "" ;
	private static boolean _isRegisterCreated ;
	// private static boolean _isValidatorCreated ;
	
    /**
     * The message displayed to the user when the server cannot be reached or
     * returns an error.
     */
	private static final String SERVER_ERROR = "An error occurred while "
			+ "attempting to contact the server. Please check your network "
			+ "connection and try again.";
	
	public interface Display extends WidgetDisplay
	{
    public HasClickHandlers getSend() ;
    public HasClickHandlers getDisconnect() ;
    public HasClickHandlers getRegister() ;
    public String           getIdentifier() ;
    public String           getPassword() ;
    public void             initWorkingPage() ;
    public void             initWelcomePage() ;
    public FlowPanel        getMainPannel() ;
    public FlowPanel        getBodyPannel() ;
	}

	private final DispatchAsync _dispatcher ;
	private final LdvSupervisor _supervisor ;
    
	@Inject
	public LdvWelcomePresenter(final Display display, 
    		              final EventBus eventBus, 
    		              final DispatchAsync dispatcher,
    		              final LdvSupervisor supervisor)
	{
		super(display, eventBus) ;
		
		_dispatcher = dispatcher ;
		_supervisor = supervisor ;
		    
		bind() ;
	}
	      
	@Override
	protected void onBind() 
	{
		eventBus.addHandler(LdvWelcomePageEvent.TYPE, new LdvWelcomePageEventHandler() {
			public void onWelcome(LdvWelcomePageEvent event)
			{
				Log.info("Loading Welcome Page") ;
				// RootPanel.get().clear();
				// RootPanel.get().add(display.asWidget());
				event.getWorkspace().clear() ;
				FlowPanel WelcomeWorkspace = event.getWorkspace() ;
				WelcomeWorkspace.add(display.asWidget()) ;
			}
		});		
		
		// 'display' is a final global field containing the Display passed into the constructor.
		
		// Login button handler
		//
		display.getSend().addClickHandler(new ClickHandler() 
		{
			public void onClick(final ClickEvent event) 
			{
				//doGetIdentifiers();
				doSendIdentifiers(display.getIdentifier(), display.getPassword()) ;
			}
		});
		
		// Disconnect button handler
		//
		display.getDisconnect().addClickHandler(new ClickHandler() 
		{
			public void onClick(final ClickEvent event) 
			{
				doDisconnect() ;
			}
		});
		
		// Register button handler
		//
		display.getRegister().addClickHandler(new ClickHandler()
		{
			public void onClick(final ClickEvent event){
				doRegister();
			}
		});
		
		eventBus.addHandler(IdentifiersProvidedEvent.TYPE, new IdentifiersProvidedEventHandler()
		{
			@Override
			public void onIdentifierProvided(final IdentifiersProvidedEvent event)
			{
				Log.info("Do post login processing here");
				doSendIdentifiers(event.getIdentifier(), event.getPassword()) ;
			}
    });
		
		eventBus.addHandler(LdvSentEvent.TYPE, new LdvSentEventHandler()
		{
			@Override
			public void onLoginSent(final LdvSentEvent event) 
			{
				Log.info("Handling Login button click event");
				eventBus.fireEvent(new IdentifiersProvidedEvent(display.getIdentifier(), display.getPassword()));
			}
		});
		
		eventBus.addHandler(LdvOpenEvent.TYPE, new LdvOpenEventHandler()
	  {
			@Override
			public void onPatientOpen(final LdvOpenEvent event)
			{
				Log.info("Do post login processing here");
				doOpenPerson(event.getSessionId()) ;
			}
	  });
	}

	/**
   * Try to send the greeting message
   */
  public void doSendIdentifiers(String sIdentifier, String sPassword)
  {
  	Log.info("Calling doSend") ;

  	_supervisor.initCipherElements(sPassword) ;
  	_dispatcher.execute(new SendLoginAction(sIdentifier, _supervisor.getUserCipherPassword()), new LoginCallback());
  }

  public void doDisconnect()
  {
  	Log.info("Calling doSend") ;

  	_dispatcher.execute(new SendDisconnect(_sSessionId), new DisconnectCallback());
  }
  
  /**
   * Open person's LdV
   */
  private void doOpenPerson(String sSessionId)
  {
  	Log.info("Calling doSend") ;
  	/*
    	_dispatcher.execute(new OpenPerson(sLoginId, sPassword), new DisplayCallback<OpenPersonResult>(display) 
    	{
  		@Override
  	 	protected void handleFailure(final Throwable cause)
  	 	{
  			Log.error("Handle Failure:", cause) ;				
  			Window.alert(SERVER_ERROR) ;
  	 	}

  	 	@Override
  	 	protected void handleSuccess(final SendLoginResult result)
  	 	{
  			// take the result from the server and notify client interested components
  			if (false == result.getSessionId().equals(""))
  			eventBus.fireEvent(new LdvSentEvent(result.getSessionId(), result.getMessage())) ;
  	 	}
    	});
  	*/
  }

  /**
	  * Opens the registration interface
	  */
	public void doRegister()
	{
		Log.info("Calling doRegister") ;
		
		_supervisor.setUserPseudo(display.getIdentifier()) ;
		_supervisor.setUserPlainTextPassword(display.getPassword()) ; 

		eventBus.fireEvent(new GoToLdvRegisterPageEvent()) ;	
	}
	
	/**
	  * Opens the registration validation interface
	  */
	public void openValidationPage(String sId)
	{
		if ((null == sId) || sId.equals(""))
			return ;
		
		Log.info("Calling doRegister") ;
		if ((false == _isRegisterCreated) && (null != _supervisor) && (null != _supervisor.getInjector()))
		{
			Log.info("Creating a register presenter") ;
			LdvGinjector injector = _supervisor.getInjector() ;
			injector.getRegisterPresenter() ;
			_isRegisterCreated = true;
		}
		eventBus.fireEvent(new LdvValidatorSentEvent(display.getBodyPannel(), sId)) ;	
	}
	
	@Override
	protected void onUnbind() 
	{
		// Add unbind functionality here for more complex presenters.
	}

	public void refreshDisplay() 
	{
		// This is called when the presenter should pull the latest data
		// from the server, etc. In this case, there is nothing to do.
	}

	public void revealDisplay() 
	{
		// Nothing to do. This is more useful in UI which may be buried
		// in a tab bar, tree, etc.
	}

	protected void SwitchToWorking()
	{
	}
	
	public class LoginCallback implements AsyncCallback<SendLoginResult> 
	{
		public LoginCallback() {
			super() ;
		}

		@Override
    public void onFailure(final Throwable cause)
    {
			Log.error("Handle Failure:", cause) ;
				
			Window.alert(SERVER_ERROR) ;
    }

    @Override
    public void onSuccess(final SendLoginResult result)
    {
    	// take the result from the server and notify client interested components
    	if (false == result.getSessionToken().equals(""))
    	{
    		_sSessionId = result.getSessionToken() ;
    		_supervisor.setSessionToken(_sSessionId) ;
    		_supervisor.setUser(result.getUser()) ;
    		
    		eventBus.fireEvent(new GoToLdvManagerEvent()) ;
    	}
    }
	}
    	
	public class DisconnectCallback implements AsyncCallback<SendDisconnectResult> 
	{
		public DisconnectCallback() {
			super() ;
		}

		@Override
		public void onFailure(final Throwable cause)
		{
			Log.error("Handle Failure:", cause) ;
    				
			Window.alert(SERVER_ERROR) ;
		}

		@Override
		public void onSuccess(final SendDisconnectResult result)
		{
			// take the result from the server and notify client interested components
			if (true == result.isOk())
			{
				_sSessionId = "" ;
				display.initWelcomePage() ;
			}
		}
	}

	@Override
	protected void onRevealDisplay() {
		// TODO Auto-generated method stub
		
	}
}
