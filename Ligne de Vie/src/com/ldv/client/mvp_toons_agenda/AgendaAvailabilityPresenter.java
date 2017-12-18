package com.ldv.client.mvp_toons_agenda;

import java.util.Iterator;
import java.util.Vector;

import com.allen_sauer.gwt.log.client.Log;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasChangeHandlers;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Panel;

import com.google.inject.Inject;

import com.ldv.client.event.AgendaAvailabilityDeleteEvent;
import com.ldv.client.event.AgendaAvailabilityInitEvent;
import com.ldv.client.event.AgendaAvailabilityInitEventHandler;
import com.ldv.client.event.AgendaAvailableEditEvent;
import com.ldv.client.event.AgendaAvailableEditEventHandler;
import com.ldv.client.event.AgendaAvailableInitEvent;
import com.ldv.client.event.AgendaRedrawAvailabilityEvent;
import com.ldv.client.event.AgendaRedrawAvailabilityEventHandler;
import com.ldv.client.util.LdvSupervisor;
import com.ldv.shared.calendar.Availability;
import com.ldv.shared.calendar.Available;
import com.ldv.shared.calendar.CalendarRecur;
import com.ldv.shared.rpc4caldav.UpdateAvailabilityAction;
import com.ldv.shared.rpc4caldav.UpdateAvailabilityResult;

import net.customware.gwt.dispatch.client.DispatchAsync;
import net.customware.gwt.presenter.client.EventBus;
import net.customware.gwt.presenter.client.widget.WidgetDisplay;
import net.customware.gwt.presenter.client.widget.WidgetPresenter;

/**
 * AgendaDayPresenter manages a single iCalendar Availability component 
 */
public class AgendaAvailabilityPresenter extends WidgetPresenter<AgendaAvailabilityPresenter.Display>
{
	public interface Display extends WidgetDisplay 
	{
		public FlowPanel getMainPanel() ;
		
		public void              clearAll() ;
		public void              setSummary(final String sSummary) ;
		public FlowPanel         createWorkspaceForAvailable() ;
		
		public HasClickHandlers  getNewAvailableButton() ;
		
		public void              showAvailableEditDialog(final Available editedAvailable) ;
		public void              hideAvailableEditDialog() ;
		public void              getEditedAvailable(Available editedAvailable) ;
		public HasChangeHandlers getFrequencyChanged() ;
		public String            getSelectedFrequency() ;
		public void              initFrequencyPanel(final String sFrequency) ;
		public String            getEvery() ;
		public void              setEvery(final int iInterval) ;
		public void              initializeFromCalRecur(final CalendarRecur calRecur) ;
		public HasClickHandlers  getEditAvailableOk() ;
		public HasClickHandlers  getEditAvailableCancel() ;
		
		public void              showDeleteAvailabilityWarningDialog() ;
		public void              hideDeleteAvailabilityWarningDialog() ;
		public HasClickHandlers  getDeleteAvailabilityOk() ;
		public HasClickHandlers  getDeleteAvailabilityCancel() ;
		
		public HasClickHandlers  getDeleteButton() ;
	}

	private final LdvSupervisor    _supervisor ;
	private final DispatchAsync    _dispatcher ;
	
	private 	    Availability	   _availability    = new Availability() ;
	private       Available        _editedAvailable = new Available() ;
	private       Panel 			     _workspace ;
	
	protected     Vector<AgendaAvailableInitEvent> _aToBeDrawn = new Vector<AgendaAvailableInitEvent>() ;
	private       ScheduledCommand _pendingEvents  = null ;
	
	protected     ChangeHandler    _FrequencyChangeHandler ;
	protected     String           _sFrequency ;
		
	@Inject
	public AgendaAvailabilityPresenter(final Display display, EventBus eventBus, DispatchAsync dispatcher, LdvSupervisor supervisor) 
	{	
		super(display, eventBus) ;
		
		_supervisor = supervisor ;
		_dispatcher = dispatcher ;
		
		_sFrequency = "" ;
		_workspace  = null ;
		
		_FrequencyChangeHandler = null ;
		
		bind() ;
		
		Log.info("AgendaAvailabilityPresenter constructed") ;
	}

	@Override
	protected void onBind() 
	{			
		eventBus.addHandler(AgendaAvailabilityInitEvent.TYPE, new AgendaAvailabilityInitEventHandler() 
		{
			@Override
			public void onInitAgendaAvailability(AgendaAvailabilityInitEvent event) 
			{
				connectToWorkspace(event) ;
			}
		});
		
		eventBus.addHandler(AgendaRedrawAvailabilityEvent.TYPE, new AgendaRedrawAvailabilityEventHandler() 
		{
			@Override
			public void onRedrawAgendaAvailability(AgendaRedrawAvailabilityEvent event) 
			{
				Log.info("AgendaAvailabilityPresenter for UID \"" + event.getAvailability().getUID() + "\" handling AgendaRedrawAvailabilityEvent") ;
				redraw(event) ;
			}
		});
		
		eventBus.addHandler(AgendaAvailableEditEvent.TYPE, new AgendaAvailableEditEventHandler() 
		{
			@Override
			public void onEditAgendaAvailable(AgendaAvailableEditEvent event) 
			{
				if (false == _availability.getUID().equals(event.getAvailabilityUid()))
					return ;
				
				Log.info("AgendaAvailabilityPresenter, editing Available with UID \"" + event.getAvailable().getUID()) ;
				_editedAvailable.initFromAvailable(event.getAvailable()) ;
				display.showAvailableEditDialog(_editedAvailable) ;
			}
		});
		
		/*
		 * Delete this Availability component
		 */
		display.getDeleteButton().addClickHandler(new ClickHandler(){
			public void onClick(final ClickEvent event) {
			  Log.info("Asking to delete this Availability component");
				display.showDeleteAvailabilityWarningDialog() ;
			}
		});
		
		display.getDeleteAvailabilityCancel().addClickHandler(new ClickHandler(){
			public void onClick(final ClickEvent event) {
			  Log.info("Delete this Availability cancelled");
			  display.hideDeleteAvailabilityWarningDialog() ;
			}
		});
		
		display.getDeleteAvailabilityOk().addClickHandler(new ClickHandler(){
			public void onClick(final ClickEvent event) {
			  Log.info("Delete this Availability validated");
			  display.hideDeleteAvailabilityWarningDialog() ;
			  validateDeleteAvailability() ;
			}
		});
		
		/*
		 * Command  edition dialog box
		 */
		display.getNewAvailableButton().addClickHandler(new ClickHandler(){
			public void onClick(final ClickEvent event) {
			  Log.info("Asking for a new Available component");
			  _editedAvailable.reset() ; 		
				display.showAvailableEditDialog(_editedAvailable) ;
			}
		});
		
		display.getEditAvailableCancel().addClickHandler(new ClickHandler(){
			public void onClick(final ClickEvent event) {
			  Log.info("Edit Available cancelled");
			  display.hideAvailableEditDialog() ;
			  _editedAvailable.reset() ;
			}
		});
		
		display.getEditAvailableOk().addClickHandler(new ClickHandler(){
			public void onClick(final ClickEvent event) {
			  Log.info("Edit Available accepted");
			  validateEditedAvailable() ;
			}
		});
		
		createChangeHandlers() ;
	}
	
	/** 
	 * Initialize all information
	 * 
	 * @param event Event that contains all needed information for initialization
	 */
	protected void connectToWorkspace(AgendaAvailabilityInitEvent event)
	{
		if ((null == event) || (event.getPresenter() != this))
			return ;
		
		_availability.initFromAvailability(event.getAvailability()) ;
		
		// Add this project display the Agenda view
		//
		_workspace = event.getHostPanel() ;
		_workspace.add(display.asWidget()) ;
		
		redraw() ;
	}
	
	public void redraw(AgendaRedrawAvailabilityEvent event)
	{	
		//Log.info("entering redraw concern line.");
		if (false == _availability.getUID().equals(event.getAvailability().getUID()))
			return ;
		
		redraw() ;
	}
	
	public void redraw()
	{	
		display.clearAll() ;
		
		display.setSummary(_availability.getSummary()) ;
		drawAvailableComponents() ;
	}
	
	public void drawAvailableComponents()
	{
		if (_availability.getAvailables().isEmpty())
			return ;
		
		for (Iterator<Available> it = _availability.getAvailables().iterator() ; it.hasNext() ; )
		{
			Available available = it.next() ;
			
			FlowPanel availableWorkspacePanel = display.createWorkspaceForAvailable() ;
			
			AgendaAvailableView view = new AgendaAvailableView() ;
			AgendaAvailablePresenter availablePresenter = new AgendaAvailablePresenter(view, eventBus, _supervisor) ; 
			
			initAvailablePresenter(availablePresenter, availableWorkspacePanel, available) ;
		}
	}
	
	/**
	 * Send the Available interface object a command to become active 
	 * 
	 */
	private void initAvailablePresenter(final AgendaAvailablePresenter presenter, final Panel availableWorkspacePanel, final Available available)
	{
		// If AgendaAvailableInitEvent is not handled yet, we have to defer fireEvent
		//
		if (false == eventBus.isEventHandled(AgendaAvailableInitEvent.TYPE))
		{
			_aToBeDrawn.add(new AgendaAvailableInitEvent(availableWorkspacePanel, presenter, available, _availability.getUID())) ;
			
			if (null == _pendingEvents) 
			{
				_pendingEvents = new ScheduledCommand() 
				{
	        public void execute() {
	        	_pendingEvents = null ;
	        	sendWaitingMessages() ;
	        }
	      };
	      Scheduler.get().scheduleDeferred(_pendingEvents) ;
	    }
		}
		else
			eventBus.fireEvent(new AgendaAvailableInitEvent(availableWorkspacePanel, presenter, available, _availability.getUID())) ;
	}
	
	/**
	 * Send all awaiting messages 
	 * 
	 */
	private void sendWaitingMessages()
	{
		if (_aToBeDrawn.isEmpty())
			return ;
		
		// If the event is not yet handled, then retry later
		//
		if (false == eventBus.isEventHandled(AgendaAvailableInitEvent.TYPE))
		{
			if (null == _pendingEvents) 
			{
				_pendingEvents = new ScheduledCommand() 
				{
	        public void execute() {
	        	_pendingEvents = null ;
	        	sendWaitingMessages() ;
	        }
	      };
	      Scheduler.get().scheduleDeferred(_pendingEvents) ;
	    }
		}
		
		// If the event is handled, send all awaiting events
		//
		for (Iterator<AgendaAvailableInitEvent> it = _aToBeDrawn.iterator() ; it.hasNext() ; )
			eventBus.fireEvent(it.next()) ;
		
		_aToBeDrawn.clear() ;
	}
	
	/**
	 * Validate the edition of a new or already existing event    
	 */
	protected void validateEditedAvailable()
	{
		if ("".equals(_editedAvailable.getUID()))
			validateNewAvailable() ;
		else
		{
			// Get edited Available component in array
			//
			Available available = getAvailableFormUID(_editedAvailable.getUID()) ;
			if (null == available)
				return ; // TODO fire an exception
			
			// Update edited component from user entries
			//
			display.getEditedAvailable(_editedAvailable) ;
			
			// If no change, nothing to do
			//
			if (available.equals(_editedAvailable))
				return ;
			
			// Update the Available component in array
			//
			available.initFromAvailable(_editedAvailable) ;
			
			updateAvailability() ;
		}
		
		display.hideAvailableEditDialog() ;
	}
	
	/**
	 * Validate the edition of a new event    
	 */
	protected void validateNewAvailable()
	{
		Available available = new Available() ;
		display.getEditedAvailable(available) ;
		
		_availability.addToAvailables(available) ;
		
		updateAvailability() ;
	}
	
	/**
	*  Asynchronously save a new event
	**/
	protected void updateAvailability()
	{
		String sUserId = _supervisor.getUser().getLdvId() ;
		String sLdvId  = _supervisor.getDisplayedPerson().getLdvId() ;
		String sToken  = _supervisor.getSessionToken() ;
  	
		_dispatcher.execute(new UpdateAvailabilityAction(_availability, sLdvId, sUserId, sToken), new UpdateAvailabilityCallback()) ;
	}
	
	/**
	*  Asynchronous callback function for call to save a new event
	**/
	public class UpdateAvailabilityCallback implements AsyncCallback<UpdateAvailabilityResult> 
	{
		public UpdateAvailabilityCallback() {
			super();
		}

		@Override
		public void onFailure(final Throwable cause)
		{
			Log.error("Cannot update Availability:", cause) ;
			// eventBus.fireEvent(new GoToLdvAgendaEvent()) ;
		}

		@Override
		public void onSuccess(final UpdateAvailabilityResult result)
		{
			// take the result from the server
			if (true == result.wasSuccessful())
			{
				_availability.initFromAvailability(result.getAvailability()) ;
				drawAvailableComponents() ;
			}
		}
	}
	
	/**
	 * Get the Available component from its UID
	 * 
	 * @param  sUID unique identifier to look for
	 * @return The corresponding Available component if found, <code>null</code> if not
	 */
	protected Available getAvailableFormUID(final String sUID)
	{
		if ((null == sUID) || "".equals(sUID) || _availability.getAvailables().isEmpty())
			return null ;
		
		for (Iterator<Available> it = _availability.getAvailables().iterator() ; it.hasNext() ; )
		{
			Available available = it.next() ;
			if (sUID.equals(available.getUID()))
				return available ;
		}
		
		return null ;
	}
	
	/**
	 * Delete this Availability component
	 */
	public void validateDeleteAvailability() {
		eventBus.fireEvent(new AgendaAvailabilityDeleteEvent(_availability)) ;
	}
	
	protected void createChangeHandlers()
	{
		// Change handler to check if ongoing report doesn't already exist in database
		//
		_FrequencyChangeHandler = new ChangeHandler()
		{
			public void onChange(final ChangeEvent event) 
			{
				_sFrequency = display.getSelectedFrequency() ;
				display.initFrequencyPanel(_sFrequency) ;
				
				updateControlsFromRRule() ;
			}
		} ;
		
		HasChangeHandlers frequencyChngHandler = display.getFrequencyChanged() ;
		if (null != frequencyChngHandler)
			frequencyChngHandler.addChangeHandler(_FrequencyChangeHandler) ;
	}
	
	/**
	 * Fill Available component edition controls with the content of the Recurrence object<br>
	 * Typically used when user changes the frequencies selector 
	 */
	protected void updateControlsFromRRule()
	{
		CalendarRecur calRecur = _editedAvailable.getRecurrence() ;
		if (null == calRecur)
			return ;
		
		// Only update if currently selected frequency is the same as previous one 
		//
		String sFrequency = calRecur.getFrequency() ;
		if (_sFrequency.equals(sFrequency))
		{
			display.initializeFromCalRecur(calRecur) ;
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
