package com.ldv.client.mvp;

import net.customware.gwt.dispatch.client.DispatchAsync;
import net.customware.gwt.presenter.client.EventBus;
import net.customware.gwt.presenter.client.widget.WidgetDisplay;
import net.customware.gwt.presenter.client.widget.WidgetPresenter;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.inject.Inject;

import com.ldv.client.event.GoToLdvMainEvent;
import com.ldv.client.event.GoToLdvMainEventHandler;
import com.ldv.client.event.LdvMainSentEvent;
import com.ldv.client.gin.LdvGinjector;
import com.ldv.client.util.LdvSupervisor;
import com.ldv.client.widgets.LexiqueTextBoxManager;
import com.ldv.client.event.GoToLdvManagerEvent;
import com.ldv.client.event.GoToLdvManagerEventHandler;
import com.ldv.client.event.GoToLdvRegisterPageEvent;
import com.ldv.client.event.GoToLdvRegisterPageEventHandler;
import com.ldv.client.event.LdvAgendaSentEvent;
import com.ldv.client.event.LdvManagementPageEvent;
import com.ldv.client.event.LdvRegisterSentEvent;
import com.ldv.client.event.LdvValidatorSentEvent;
import com.ldv.client.event.LdvWelcomePageEvent;
import com.ldv.client.event.BackToWelcomePageEvent;
import com.ldv.client.event.BackToWelcomePageEventHandler;
import com.ldv.client.event.GoToLdvAgendaEvent;
import com.ldv.client.event.GoToLdvAgendaEventHandler;

/**
 * LdvMainPresenter is the Root component, switching workspaces and headers
 * according to user navigation
 */
public class LdvMainPresenter extends WidgetPresenter<LdvMainPresenter.Display> 
{
	/**
	 * The message displayed to the user when the server cannot be reached or
	 * returns an error.
	 */
/*private static final String SERVER_ERROR = "An error occurred while "
			+ "attempting to contact the server. Please check your network "
			+ "connection and try again.";
	*/
	public interface Display extends WidgetDisplay 
	{
		public FlowPanel getWorkspace() ;
		public FlowPanel getHeader() ;
		public FlowPanel getFooter() ; 
		public void      setBlocksPosition(int iWindowHeight) ;
		//public VerticalPanel getBody();
	}
	
	private final DispatchAsync    _dispatcher ;
	private final LdvSupervisor    _supervisor ;
	
	private       boolean          _isWelcomePageCreated ;
	private       boolean          _isManagementPageCreated ;
	private       boolean          _isRegisterPageCreated ;
	private       boolean          _isValidatingPageCreated ;
	private       boolean          _isMainLdvPageCreated ;
	private       boolean          _isAgendaPageCreated ;

	private       ScheduledCommand _pendingEvents ;
	
	// Array of 
	//
	private       LexiqueTextBoxManager _lexiqueTextBoxManager ;

	@Inject
	public LdvMainPresenter(final Display       display, 
						              final EventBus      eventBus, 
						              final DispatchAsync dispatcher,
						              final LdvSupervisor supervisor) 
	{
		super(display, eventBus) ;
		
		_isRegisterPageCreated   = false ;
		_isValidatingPageCreated = false ;
		_isManagementPageCreated = false ;
		_isWelcomePageCreated    = false ;
		_isMainLdvPageCreated    = false ;
		_isAgendaPageCreated     = false ;
		
		_dispatcher = dispatcher ;
		_supervisor = supervisor ;
		
		_lexiqueTextBoxManager = new LexiqueTextBoxManager() ; 
		
		bind() ;
	}
	
	@Override
	protected void onBind() 
	{
		eventBus.addHandler(BackToWelcomePageEvent.TYPE, new BackToWelcomePageEventHandler() {
			@Override
			public void onBackToWelcome(BackToWelcomePageEvent event) 
			{
				// Log.info("Back to welcome page") ;
				doLoad() ;	
			}
		});
		
		eventBus.addHandler(GoToLdvRegisterPageEvent.TYPE, new GoToLdvRegisterPageEventHandler() 
		{
			@Override
			public void onGoToRegister(GoToLdvRegisterPageEvent event) 
			{
				// Log.info("Call to go to post login page") ;
				goToRegisterPage() ;	
			}
		});
		
		eventBus.addHandler(GoToLdvManagerEvent.TYPE, new GoToLdvManagerEventHandler() 
		{
			@Override
			public void onGoToMain(GoToLdvManagerEvent event)
			{
				// Log.info("Call to go to manager page") ;
				goToManagementPage() ;
			}
		});
		
		eventBus.addHandler(GoToLdvMainEvent.TYPE, new GoToLdvMainEventHandler() 
		{
			@Override
			public void onGoToMain(GoToLdvMainEvent event)
			{
				// Log.info("Call to go to main page") ;
				openMainLdvPage() ;
			}
		});
		
		eventBus.addHandler(GoToLdvAgendaEvent.TYPE, new GoToLdvAgendaEventHandler() 
		{
			@Override
			public void onGoToAgenda(GoToLdvAgendaEvent event)
			{
				// Log.info("Call to go to agenda page") ;
				openAgendaPage() ;
			}
		});
		
		Window.addResizeHandler(new ResizeHandler()
		{
			@Override
			public void onResize(ResizeEvent event)
			{
				// Log.info("Call to resize workspace") ;
				resizeWorkspace(event.getHeight()) ;
			}
		});
		
		resizeWorkspace(Window.getClientHeight()) ;
		doLoad() ;
	}

	/**
   * Called at load time; open LdvWelcome Presenter/View
   */
	public void doLoad()
	{
		// Log.info("Calling Load");
		if ((false == _isWelcomePageCreated) && (null != _supervisor) && (null != _supervisor.getInjector()))
		{
			LdvGinjector injector = _supervisor.getInjector() ;
			injector.getWelcomePresenter() ;
			_isWelcomePageCreated = true ;
		}
		display.getWorkspace().clear() ;
		eventBus.fireEvent(new LdvWelcomePageEvent(display.getWorkspace())) ;
	}
	
	public void goToManagementPage()
	{
		// Log.info("Going to management page") ;
		display.getWorkspace().clear() ;
		if ((false == _isManagementPageCreated) && (null != _supervisor) && (null != _supervisor.getInjector()))
		{
			LdvGinjector injector = _supervisor.getInjector() ;
			injector.getManagementPresenter() ;
			_isManagementPageCreated = true ;
		}

		// If LdvManagementPageEvent is not handled yet, we have to defer fireEvent
		//
		if (false == eventBus.isEventHandled(LdvManagementPageEvent.TYPE))
		{
			if (null == _pendingEvents) 
			{
				_pendingEvents = new ScheduledCommand() 
				{
	        public void execute() {
	        	_pendingEvents = null ;
	        	eventBus.fireEvent(new LdvManagementPageEvent(display.getWorkspace(), display.getHeader())) ;
	        }
	      };
	      Scheduler.get().scheduleDeferred(_pendingEvents) ;
	    }
		}
		else
			eventBus.fireEvent(new LdvManagementPageEvent(display.getWorkspace(), display.getHeader())) ;
	}
		
	public void openMainLdvPage()
	{
		// Log.info("Going to main Ldv page") ;
		display.getWorkspace().clear() ;
		if ((false == _isMainLdvPageCreated) && (null != _supervisor) && (null != _supervisor.getInjector()))
		{
			LdvGinjector injector = _supervisor.getInjector() ;
			injector.getTimeControlPresenter() ;
			_isMainLdvPageCreated = true ;
		}
		
		// If LoginSuccessEvent is not handled yet, we have to defer fireEvent
		//
		if (false == eventBus.isEventHandled(LdvMainSentEvent.TYPE))
		{
			if (null == _pendingEvents) 
			{
				_pendingEvents = new ScheduledCommand() 
				{
	        public void execute() {
	        	_pendingEvents = null ;
	        	eventBus.fireEvent(new LdvMainSentEvent(display.getWorkspace())) ;
	        }
	      };
	      Scheduler.get().scheduleDeferred(_pendingEvents) ;
	    }
		}
		else
			eventBus.fireEvent(new LdvMainSentEvent(display.getWorkspace())) ;	
	}
	
	public void openAgendaPage()
	{
		// Log.info("Going to agenda page") ;
		display.getWorkspace().clear() ;
		if ((false == _isAgendaPageCreated) && (null != _supervisor) && (null != _supervisor.getInjector()))
		{
			LdvGinjector injector = _supervisor.getInjector() ;
			injector.getAgendaPresenter() ;
			_isAgendaPageCreated = true ;
		}
		
		// If LoginSuccessEvent is not handled yet, we have to defer fireEvent
		//
		if (false == eventBus.isEventHandled(LdvAgendaSentEvent.TYPE))
		{
			if (null == _pendingEvents) 
			{
				_pendingEvents = new ScheduledCommand() 
				{
	        public void execute() {
	        	_pendingEvents = null ;
	        	eventBus.fireEvent(new LdvAgendaSentEvent(display.getWorkspace())) ;
	        }
	      };
	      Scheduler.get().scheduleDeferred(_pendingEvents) ;
	    }
		}
		else
		{
			_pendingEvents = null ;
			eventBus.fireEvent(new LdvAgendaSentEvent(display.getWorkspace())) ;
		}
	}
	
	public void goToRegisterPage()
	{
		// Log.info("Going to register page") ;
		display.getWorkspace().clear() ;
		if ((false == _isRegisterPageCreated) && (null != _supervisor) && (null != _supervisor.getInjector()))
		{
			LdvGinjector injector = _supervisor.getInjector() ;
			injector.getRegisterPresenter() ;
			_isRegisterPageCreated = true ;
		}

		// If LoginSuccessEvent is not handled yet, we have to defer fireEvent
		//
		if (false == eventBus.isEventHandled(LdvRegisterSentEvent.TYPE))
		{
			if (null == _pendingEvents) 
			{
				_pendingEvents = new ScheduledCommand() 
				{
	        public void execute() {
	        	_pendingEvents = null ;
	        	eventBus.fireEvent(new LdvRegisterSentEvent(display.getWorkspace())) ;
	        }
	      };
	      Scheduler.get().scheduleDeferred(_pendingEvents) ;
	    }
		}
		else
			eventBus.fireEvent(new LdvRegisterSentEvent(display.getWorkspace())) ;	
	}
	
	public void goToValidatingPage(final String sFutureId)
	{
		if ((null == sFutureId) || sFutureId.equals(""))
			return ;
		
		// Log.info("Going to validating page") ;
		display.getWorkspace().clear() ;
		if ((false == _isValidatingPageCreated) && (null != _supervisor) && (null != _supervisor.getInjector()))
		{
			LdvGinjector injector = _supervisor.getInjector() ;
			injector.getValidatorPresenter() ;
			_isValidatingPageCreated = true ;
		}

		// If LoginSuccessEvent is not handled yet, we have to defer fireEvent
		//
		if (false == eventBus.isEventHandled(LdvValidatorSentEvent.TYPE))
		{
			if (null == _pendingEvents) 
			{
				_pendingEvents = new ScheduledCommand() 
				{
	        public void execute() {
	        	_pendingEvents = null ;
	        	eventBus.fireEvent(new LdvValidatorSentEvent(display.getWorkspace(), sFutureId)) ;
	        }
	      };
	      Scheduler.get().scheduleDeferred(_pendingEvents) ;
	    }
		}
		else
			eventBus.fireEvent(new LdvValidatorSentEvent(display.getWorkspace(), sFutureId)) ;	
	}

	private void resizeWorkspace(int iWindowHeight)
	{
		display.setBlocksPosition(iWindowHeight) ;
	}
		
	@Override
	protected void onUnbind() {
	}

	public void refreshDisplay() {	
	}

	public void revealDisplay() {
	}

	@Override
	protected void onRevealDisplay() {
		// TODO Auto-generated method stub
		
	}
}
