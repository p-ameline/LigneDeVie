package com.ldv.client.mvp_toons_agenda;

import java.util.Iterator;
import java.util.Vector;

import com.allen_sauer.gwt.log.client.Log;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Panel;

import com.google.inject.Inject;
import com.ldv.client.event.AgendaAvailabilityDeleteEvent;
import com.ldv.client.event.AgendaAvailabilityDeleteEventHandler;
import com.ldv.client.event.AgendaAvailabilityInitEvent;
import com.ldv.client.event.AgendaAvailabilityManagerInitEvent;
import com.ldv.client.event.AgendaAvailabilityManagerInitEventHandler;
import com.ldv.client.event.AgendaRedrawAvailabilityEvent;
import com.ldv.client.event.AgendaRedrawAvailabilityEventHandler;
import com.ldv.client.util.LdvSupervisor;
import com.ldv.shared.calendar.Availability;
import com.ldv.shared.rpc4caldav.DeleteComponentAction;
import com.ldv.shared.rpc4caldav.DeleteComponentResult;
import com.ldv.shared.rpc4caldav.GetAvailabilityAction;
import com.ldv.shared.rpc4caldav.GetAvailabilityResult;
import com.ldv.shared.rpc4caldav.SaveNewAvailabilityAction;
import com.ldv.shared.rpc4caldav.SaveNewAvailabilityResult;

import net.customware.gwt.dispatch.client.DispatchAsync;
import net.customware.gwt.presenter.client.EventBus;
import net.customware.gwt.presenter.client.widget.WidgetDisplay;
import net.customware.gwt.presenter.client.widget.WidgetPresenter;

/**
 * AgendaDayPresenter manages a day that displays its events according to a time scale 
 */
public class AgendaAvailabilityManagementPresenter extends WidgetPresenter<AgendaAvailabilityManagementPresenter.Display>
{
	public interface Display extends WidgetDisplay 
	{
		public FlowPanel        getMainPanel() ;
		
		public void             clearAll() ;
		public FlowPanel        createWorkspaceForAvailability() ;
		
		public HasClickHandlers getNewAvailabilityButton() ;
		
		public void             showAvailabilityEditDialog(final Availability editedAvailability) ;
		public void             hideAvailabilityEditDialog() ;
		public void             getEditedAvailability(Availability editedAvailability) ;
		public HasClickHandlers getEditAvailabilityOk() ;
		public HasClickHandlers getEditAvailabilityCancel() ;
	}

	private final LdvSupervisor    _supervisor ;
	private final DispatchAsync    _dispatcher ;
	private       Panel 			     _workspace ;
	private       ScheduledCommand _pendingEvents  = null ;
	
	private       Vector<Availability> _aAvailabilities = new Vector<Availability>() ;
	private       Availability         _editedAvailability = new Availability() ;
		
	@Inject
	public AgendaAvailabilityManagementPresenter(final Display display,
			                                         EventBus eventBus,
			                                         DispatchAsync dispatcher,
			                                         LdvSupervisor supervisor) 
	{	
		super(display, eventBus) ;
		
		_dispatcher = dispatcher ;
		_supervisor = supervisor ;
		
		bind() ;
		
		Log.info("AgendaAvailabilityPresenter constructed") ;
	}

	@Override
	protected void onBind() 
	{			
		eventBus.addHandler(AgendaAvailabilityManagerInitEvent.TYPE, new AgendaAvailabilityManagerInitEventHandler() 
		{
			@Override
			public void onInitAgendaAvailabilityManager(AgendaAvailabilityManagerInitEvent event) 
			{
				initialize(event) ;
			}
		});
		
		eventBus.addHandler(AgendaRedrawAvailabilityEvent.TYPE, new AgendaRedrawAvailabilityEventHandler() 
		{
			@Override
			public void onRedrawAgendaAvailability(AgendaRedrawAvailabilityEvent event) 
			{
				Log.info("AgendaAvailabilityPresenter handling AgendaRedrawAvailabilityEvent") ;
				redraw() ;
			}
		});
		
		eventBus.addHandler(AgendaAvailabilityDeleteEvent.TYPE, new AgendaAvailabilityDeleteEventHandler() 
		{
			@Override
			public void onDeleteAgendaAvailability(AgendaAvailabilityDeleteEvent event) 
			{
				Log.info("AgendaAvailabilityPresenter handling AgendaAvailabilityDeleteEvent") ;
				deleteAvailability(event.getAvailability().getUID()) ;
			}
		});
		
		/*
		 * Command event edition dialog box
		 */
		display.getNewAvailabilityButton().addClickHandler(new ClickHandler(){
			public void onClick(final ClickEvent event) {
			  Log.info("Asking for a new Availability component");
			  _editedAvailability.reset() ; 		
				display.showAvailabilityEditDialog(_editedAvailability) ;
			}
		});
		
		display.getEditAvailabilityCancel().addClickHandler(new ClickHandler(){
			public void onClick(final ClickEvent event) {
			  Log.info("Edit Availability cancelled");
			  display.hideAvailabilityEditDialog() ;
			  _editedAvailability.reset() ;
			}
		});
		
		display.getEditAvailabilityOk().addClickHandler(new ClickHandler(){
			public void onClick(final ClickEvent event) {
			  Log.info("Edit Availability acepted");
			  validateEditedAvailability() ;
			}
		});
	}
	
	/** 
	 * Initialize all information
	 * 
	 * @param event Event that contains all needed information for initialization
	 */
	protected void initialize(AgendaAvailabilityManagerInitEvent event)
	{
		if (null == event)
			return ;
		
		// Add the view to the workspace
		//
		_workspace = event.getHostPanel() ;
		_workspace.add(display.asWidget()) ;
		
		//
		//
		redraw() ;
	}
	
	/**
	 * Get Availability objects from server and display them
	 */
	public void redraw()
	{	
		display.clearAll() ;
		getComponents() ;		
	}
	
	/** 
	 * Get the Availability objects from the server in order to display them
	 */
	protected void getComponents()
	{
		String sUserId = _supervisor.getUser().getLdvId() ;
		String sLdvId  = _supervisor.getDisplayedPerson().getLdvId() ;
		String sToken  = _supervisor.getSessionToken() ;
  	
		_dispatcher.execute(new GetAvailabilityAction(sLdvId, sUserId, sToken), new GetAvailabilityCallback()) ;
	}
	
	/**
	 *  Asynchronous callback function for calls to getComponents
	 **/
	public class GetAvailabilityCallback implements AsyncCallback<GetAvailabilityResult> 
	{
		public GetAvailabilityCallback() {
			super();
		}

		@Override
		public void onFailure(final Throwable cause)
		{
			Log.error("Cannot get availability components:", cause) ;
			// eventBus.fireEvent(new GoToLdvAgendaEvent()) ;
		}

		@Override
		public void onSuccess(final GetAvailabilityResult result)
		{
			// take the result from the server
			if (true == result.wasSuccessful())
			{
				// Fill the Availability vector
				//
				_aAvailabilities.clear() ;
				
				if (false == result.getAvailabilities().isEmpty())
					for (Iterator<Availability> it = result.getAvailabilities().iterator() ; it.hasNext() ; )
						_aAvailabilities.add(new Availability(it.next())) ;
				
				redrawAvailabilities() ;
			}
		}
	}
	
	/**
	 *  Create a dedicated panel for each Availability component
	 **/
	protected void redrawAvailabilities()
	{
		display.clearAll() ;
		
		if (_aAvailabilities.isEmpty())
			return ;
		
		for (Iterator<Availability> it = _aAvailabilities.iterator() ; it.hasNext() ; )
		{
			Availability availability = it.next() ;
			
			FlowPanel availabilityWorkspacePanel = display.createWorkspaceForAvailability() ;
			
			AgendaAvailabilityView view = new AgendaAvailabilityView() ;
			AgendaAvailabilityPresenter availabilityPresenter = new AgendaAvailabilityPresenter(view, eventBus, _dispatcher, _supervisor) ; 
			
			initAvailabilityPresenter(availabilityPresenter, availabilityWorkspacePanel, availability) ;
		}
	}
	
	/**
	 * Send the Availability interface object a command to become active 
	 * 
	 */
	private void initAvailabilityPresenter(final AgendaAvailabilityPresenter presenter, final Panel availabilityWorkspacePanel, final Availability availability)
	{
		// If AgendaAvailabilityInitEvent is not handled yet, we have to defer fireEvent
		//
		if (false == eventBus.isEventHandled(AgendaAvailabilityInitEvent.TYPE))
		{
			if (null == _pendingEvents) 
			{
				_pendingEvents = new ScheduledCommand() 
				{
	        public void execute() {
	        	_pendingEvents = null ;
	        	initAvailabilityPresenter(presenter, availabilityWorkspacePanel, availability) ;
	        }
	      };
	      Scheduler.get().scheduleDeferred(_pendingEvents) ;
	    }
		}
		else
			eventBus.fireEvent(new AgendaAvailabilityInitEvent(availabilityWorkspacePanel, presenter, availability)) ;
	}
	
	/**
	 * Validate the edition of a new or already existing event    
	 */
	protected void validateEditedAvailability()
	{
		if ("".equals(_editedAvailability.getUID()))
			validateNewAvailability() ;
		
		display.hideAvailabilityEditDialog() ;
	}
	
	/**
	 * Validate the edition of a new event    
	 */
	protected void validateNewAvailability()
	{
		Availability availability = new Availability() ;
		display.getEditedAvailability(availability) ;
		
		saveNewAvailability(availability) ;
	}
	
	/**
	*  Asynchronously save a new event
	**/
	protected void saveNewAvailability(final Availability availability)
	{
		String sUserId = _supervisor.getUser().getLdvId() ;
		String sLdvId  = _supervisor.getDisplayedPerson().getLdvId() ;
		String sToken  = _supervisor.getSessionToken() ;
  	
		_dispatcher.execute(new SaveNewAvailabilityAction(availability, sLdvId, sUserId, sToken), new SaveNewAvailabilityCallback()) ;
	}
	
	/**
	*  Asynchronous callback function for call to save a new event
	**/
	public class SaveNewAvailabilityCallback implements AsyncCallback<SaveNewAvailabilityResult> 
	{
		public SaveNewAvailabilityCallback() {
			super();
		}

		@Override
		public void onFailure(final Throwable cause)
		{
			Log.error("Cannot record Availability:", cause) ;
			// eventBus.fireEvent(new GoToLdvAgendaEvent()) ;
		}

		@Override
		public void onSuccess(final SaveNewAvailabilityResult result)
		{
			// take the result from the server
			if (true == result.wasSuccessful())
			{
				_aAvailabilities.add(new Availability(result.getAvailability())) ;				
				redrawAvailabilities() ;
			}
		}
	}
	
	/**
	 * Ask server to delete an Availability component  
	 */
	protected void deleteAvailability(final String sUID)
	{
		if ((null == sUID) || "".equals(sUID))
			return ;
		
		String sUserId = _supervisor.getUser().getLdvId() ;
		String sLdvId  = _supervisor.getDisplayedPerson().getLdvId() ;
		String sToken  = _supervisor.getSessionToken() ;
  	
		_dispatcher.execute(new DeleteComponentAction(sUID, sLdvId, sUserId, sToken), new DeleteAvailabilityCallback()) ;
	}
	
	/**
	*  Asynchronous callback function for call to delete an Availability component
	**/
	public class DeleteAvailabilityCallback implements AsyncCallback<DeleteComponentResult> 
	{
		public DeleteAvailabilityCallback() {
			super();
		}

		@Override
		public void onFailure(final Throwable cause)
		{
			Log.error("Cannot delete Availability:", cause) ;
			// eventBus.fireEvent(new GoToLdvAgendaEvent()) ;
		}

		@Override
		public void onSuccess(final DeleteComponentResult result)
		{
			// take the result from the server
			if (true == result.wasSuccessful())
				redraw() ;
		}
	}
	
	@Override
	protected void onUnbind() {
	}

	@Override
	public void revealDisplay() {
	}

	@Override
	protected void onRevealDisplay() {
		// TODO Auto-generated method stub
		
	}
}
