package com.ldv.client.mvp_toons_agenda;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Panel;

import com.google.inject.Inject;
import com.ldv.client.event.AgendaAvailableEditEvent;
import com.ldv.client.event.AgendaAvailableInitEvent;
import com.ldv.client.event.AgendaAvailableInitEventHandler;
import com.ldv.client.event.AgendaRedrawAvailableEvent;
import com.ldv.client.event.AgendaRedrawAvailableEventHandler;
import com.ldv.client.util.LdvSupervisor;
import com.ldv.shared.calendar.Available;

import net.customware.gwt.presenter.client.EventBus;
import net.customware.gwt.presenter.client.widget.WidgetDisplay;
import net.customware.gwt.presenter.client.widget.WidgetPresenter;

/**
 * AgendaDayPresenter manages a single iCalendar Availability component 
 */
public class AgendaAvailablePresenter extends WidgetPresenter<AgendaAvailablePresenter.Display>
{
	public interface Display extends WidgetDisplay 
	{
		public FlowPanel getMainPanel() ;
		
		public void      clearAll() ;
		public void      setSummary(final String sSummary) ;
		
		public HasClickHandlers getEditButton() ;
		public HasClickHandlers getDeleteButton() ;
	}

	private final LdvSupervisor _supervisor ;
	private       String        _sAvailabilityUid ;
	private 	    Available	    _available = new Available() ;
	private       Panel 			  _workspace ;
		
	@Inject
	public AgendaAvailablePresenter(final Display display, EventBus eventBus, LdvSupervisor supervisor) 
	{	
		super(display, eventBus) ;
		
		_supervisor = supervisor ;
		
		bind();
		
		Log.info("AgendaAvailablePresenter constructed") ;
	}

	@Override
	protected void onBind() 
	{			
		eventBus.addHandler(AgendaAvailableInitEvent.TYPE, new AgendaAvailableInitEventHandler() 
		{
			@Override
			public void onInitAgendaAvailable(AgendaAvailableInitEvent event) 
			{
				connectToWorkspace(event) ;
			}
		});
		
		eventBus.addHandler(AgendaRedrawAvailableEvent.TYPE, new AgendaRedrawAvailableEventHandler() 
		{
			@Override
			public void onRedrawAgendaAvailable(AgendaRedrawAvailableEvent event) 
			{
				Log.info("AgendaAvailablePresenter for UID \"" + event.getAvailable().getUID() + "\" handling AgendaRedrawAvailableEvent") ;
				redraw(event) ;
			}
		});
		
		/*
		 * Command  edition dialog box
		 */
		display.getEditButton().addClickHandler(new ClickHandler(){
			public void onClick(final ClickEvent event) {
			  Log.info("Asking the Availibility Component to edit Available component " + _available.getUID()) ;
			  eventBus.fireEvent(new AgendaAvailableEditEvent(_available, _sAvailabilityUid)) ;
			}
		});
	}
	
	/** 
	 * Initialize all information
	 * 
	 * @param event Event that contains all needed information for initialization
	 */
	protected void connectToWorkspace(AgendaAvailableInitEvent event)
	{
		if ((null == event) || (event.getPresenter() != this))
			return ;
		
		_sAvailabilityUid = event.getAvailabilityUid() ;
		
		_available.initFromAvailable(event.getAvailable()) ;
		
		// Add this project display the Agenda view
		//
		_workspace = event.getHostPanel() ;
		_workspace.add(display.asWidget()) ;
		
		redraw() ;
	}
	
	public void redraw(AgendaRedrawAvailableEvent event)
	{	
		//Log.info("entering redraw concern line.");
		if (false == _available.getUID().equals(event.getAvailable().getUID()))
			return ;
		
		redraw() ;
	}
	
	public void redraw()
	{	
		display.clearAll() ;
		display.setSummary(_available.getSummary()) ;
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
