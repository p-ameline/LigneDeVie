package com.ldv.client.mvp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Vector;

import com.allen_sauer.gwt.log.client.Log;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTMLTable.Cell;
import com.google.gwt.user.client.ui.Panel;

import com.google.inject.Inject;

import com.ldv.shared.calendar.Event;
import com.ldv.shared.model.LdvTime;
import com.ldv.shared.rpc4caldav.GetPlannedEventsAction;
import com.ldv.shared.rpc4caldav.GetPlannedEventsResult;
import com.ldv.shared.rpc4caldav.SaveNewEventAction;
import com.ldv.shared.rpc4caldav.SaveNewEventResult;
import com.ldv.client.event.AgendaAvailabilityManagerInitEvent;
import com.ldv.client.event.AgendaDayControllerInitEvent;
import com.ldv.client.event.AgendaDayInitEvent;
import com.ldv.client.event.AgendaDaySummaryInitEvent;
import com.ldv.client.event.AgendaRedrawDayEvent;
import com.ldv.client.event.LdvAgendaSentEvent;
import com.ldv.client.event.LdvAgendaSentEventHandler;
import com.ldv.client.gin.LdvGinjector;
import com.ldv.client.mvp.LdvAgendaView.AgendaRectangle;
import com.ldv.client.mvp.LdvAgendaView.DailyComponents;
import com.ldv.client.mvp_toons_agenda.AgendaDayPresenter;
import com.ldv.client.mvp_toons_agenda.AgendaDaySummaryPresenter;
import com.ldv.client.mvp_toons_agenda.AgendaDaySummaryView;
import com.ldv.client.mvp_toons_agenda.AgendaDayView;
import com.ldv.client.mvp_toons_agenda.AgendaTimeDisplayPresenter;
import com.ldv.client.util.LdvGraphManager;
import com.ldv.client.util.LdvSupervisor;

import net.customware.gwt.dispatch.client.DispatchAsync;
import net.customware.gwt.presenter.client.EventBus;
import net.customware.gwt.presenter.client.widget.WidgetDisplay;
import net.customware.gwt.presenter.client.widget.WidgetPresenter;

public class LdvAgendaPresenter extends WidgetPresenter<LdvAgendaPresenter.Display>
{
	/**
   * Describes the display mode : single day, single week, multiple weeks with current on top, single month
   */
  public enum DisplayMode { NONE, DAY, WEEK, MULTI_WEEK, MONTH } ;
	
	private       LdvGraphManager  _graphManager ;
	private       LdvTime          _selectedDay ;
	private       LdvTime          _displayedDayFrom = new LdvTime(0) ;
	private       LdvTime          _displayedDayTo   = new LdvTime(0) ;
	private       DisplayMode      _displayMode ;

	private       ScheduledCommand _pendingEvents  = null ;
	
	private final DispatchAsync    _dispatcher ;
	private final LdvSupervisor    _supervisor ;
	
	private       Vector<Event>    _aEvents = new Vector<Event>() ;
	private       Event            _editedEvent = new Event() ;
	
	private       AgendaTimeDisplayPresenter _timeController ;
	
	private       int              _iHourHeightInPixels = 65 ;
	
	public interface Display extends WidgetDisplay 
	{	
		public Panel                         getMainPanel() ;
		
		public void                          displayEvents(final Vector<Event> aEvents) ;
		
		public void                          setSelectedDay(final LdvTime tToSet) ;
		public void                          setDisplayMode(DisplayMode displayMode) ;
		public DisplayMode                   getDisplayMode() ;
		public DisplayMode                   getModeForPosition(int iTabIndex) ;
		
		public void                          onResize(int iWidth, int iHeight) ;
		
		public LdvTime                       getmonthSelectionDisplayedTime() ;
		public void                          setmonthSelectionDisplayedTime(final LdvTime tToSet) ;
		public void                          selectDisplayedTime() ;
		public void                          selectDisplayedTimeForSummary() ;
		public HasClickHandlers              getDisplayedMonthPreviousButton() ;
		public HasClickHandlers              getDisplayedMonthNextButton() ;
		public HasClickHandlers              getDisplayedMonthNowButton() ;
		public HasSelectionHandlers<Integer> getModeSelectionTab() ;
		
		public HasClickHandlers              getDisplayedPeriodPreviousButton() ;
		public HasClickHandlers              getDisplayedPeriodNextButton() ;
		public HasClickHandlers              getDisplayedPeriodNowButton() ;
		public void                          displaySelectedDayLabel() ;
		public void                          displaySelectedTimeIntervalLabel(final LdvTime dayFrom, final LdvTime dayTo) ;
		
		public HasClickHandlers              getAvailabilityEditButton() ;
		
		public Panel                         getDisplayWorkspacePanel() ;
		public void                          clearDisplayWorkspace() ;
		public Panel                         prepareWorkspaceForDayMode(int iHourHeightInPixels) ;
		public void                          prepareWorkspaceForWeeksMode() ;
		public FocusPanel                    createWorkspaceForDay(LdvTime tDay) ;
		public FocusPanel                    createWorkspaceForDaySummary(LdvTime tDay, int iWeeksCount, boolean bDisplayMonth) ;
		public void                          setDaysContainersWidths() ;
		public AgendaRectangle               getDaysContainerSize(int iVisibleHoursCount) ;
		public void                          resetDayPanels() ;
		public ArrayList<DailyComponents>    getDailyComponents() ;
		public int                           getClientYInDay(int iClientYInBrowser) ;
		
		public String                        getDayForMonthGridCell(int iRow, int iCol) ;
		public Grid                          getDaySelectorGrid() ; 
		public HasClickHandlers              getDisplayedMonthGrid() ;
		
		public void                          showEventEditDialog(final Event editedEvent) ;
		public void                          hideEventEditDialog() ;
		public HasClickHandlers              getEditEventOk() ;
		public HasClickHandlers              getEditEventCancel() ;
		public void                          getEditedEvent(Event editedEvent) ;
	} 

	@Inject
	public LdvAgendaPresenter(final Display display, 
			                      final EventBus eventBus,
			                      final DispatchAsync dispatcher,
			                      final LdvSupervisor supervisor) 
	{
		super(display, eventBus) ;
		
		Log.info("Entering constructor of LdvAgendaPresenter.") ;
		
		_displayMode = DisplayMode.NONE ;
		
		_dispatcher   = dispatcher ;
		_supervisor   = supervisor ;	
		_graphManager = (LdvGraphManager) null ;
		
		bind() ;
	}

	@Override
	protected void onBind() 
	{
		/**
		 * Event received when it is time to start
		 */
		eventBus.addHandler(LdvAgendaSentEvent.TYPE, new LdvAgendaSentEventHandler() 
		{
			@Override
			public void onAgendaSent(LdvAgendaSentEvent event) 
			{
				Log.info("Handling LdvAgendaSentEvent event");
				event.getWorkspace().clear();
				 
				_graphManager = _supervisor.getDisplayedGraph() ;
				
				FlowPanel workspace = (FlowPanel) event.getWorkspace() ;
				workspace.add(getDisplay().asWidget()) ;
				
				open() ;
			}
		});
		
		/**
		 * Month selection buttons
		 */
		
		// Previous month
		//
		display.getDisplayedMonthPreviousButton().addClickHandler(new ClickHandler() 
		{
			public void onClick(final ClickEvent event) 
			{
				LdvTime displayedMonth = display.getmonthSelectionDisplayedTime() ;
				displayedMonth.addMonths(-1, true) ;
				display.setmonthSelectionDisplayedTime(displayedMonth) ;
			}
		});
		
		// Next month
		//
		display.getDisplayedMonthNextButton().addClickHandler(new ClickHandler() 
		{
			public void onClick(final ClickEvent event) 
			{
				LdvTime displayedMonth = display.getmonthSelectionDisplayedTime() ;
				displayedMonth.addMonths(1, true) ;
				display.setmonthSelectionDisplayedTime(displayedMonth) ;
			}
		});
		
		/**
		 * Period selection buttons
		 */
		
		// Today
		//
		display.getDisplayedPeriodNowButton().addClickHandler(new ClickHandler() 
		{
			public void onClick(final ClickEvent event) {
				changePeriodNow() ;
			}
		});
		
		// Previous period
		//
		display.getDisplayedPeriodPreviousButton().addClickHandler(new ClickHandler() 
		{
			public void onClick(final ClickEvent event) {
				changePeriod(-1) ;
			}
		});
		
		// Next period
		//
		display.getDisplayedPeriodNextButton().addClickHandler(new ClickHandler() 
		{
			public void onClick(final ClickEvent event) {
				changePeriod(1) ;
			}
		});
		
		/**
		 * Mode selector
		 */
		display.getModeSelectionTab().addSelectionHandler(new SelectionHandler<Integer>() {
      public void onSelection(SelectionEvent<Integer> event) 
      {
      	DisplayMode mode = display.getModeForPosition(event.getSelectedItem()) ;
      	setMode(mode) ;
      }
    });
		
		/**
		 * React to resize
		 */
		Window.addResizeHandler(new ResizeHandler()
		{
			@Override
			public void onResize(ResizeEvent event) {
				resize(event.getWidth(), event.getHeight()) ;
			}
		});

		display.getDisplayedMonthGrid().addClickHandler(new ClickHandler() 
		{
			public void onClick(final ClickEvent event) 
			{
				Cell clickedCell = display.getDaySelectorGrid().getCellForEvent(event) ;
				Element child = DOM.getFirstChild(clickedCell.getElement()) ;
				String sId = child.getPropertyString("id") ;
				
				if ((null != sId) && (false == sId.equals("")))
				{
					String[] decomposition = sId.split("_") ;
					final String sAction = decomposition[0] ;
					if ("day".equals(sAction))
					{
						final String sDay = decomposition[1] ;
						changePeriodForDay(sDay) ;
					}
				}
			}
		});
		
		/**
		 * Command event edition dialog box
		 */	
		display.getEditEventCancel().addClickHandler(new ClickHandler(){
			public void onClick(final ClickEvent event) {
			  Log.info("Edit event cancelled");
			  display.hideEventEditDialog() ;
			  _editedEvent.reset() ;
			}
		});
		
		display.getEditEventOk().addClickHandler(new ClickHandler(){
			public void onClick(final ClickEvent event) {
			  Log.info("Edit even acepted");
			  validateEditedEvent() ;
			}
		});
		
		/**
		 * Switch to availability edit workspace
		 */
		display.getAvailabilityEditButton().addClickHandler(new ClickHandler(){
			public void onClick(final ClickEvent event) {
			  Log.info("Switch to availability edition");
			  switchToAvailabilityWorkspace() ;
			}
		});
		
		
		// open() ;
	}
	
	/**
	 * Initialize
	 */
	private void open()
	{
		connectDaySelectorButtonsClickHandlers() ;
		
		_selectedDay = new LdvTime(0) ;
		_selectedDay.takeTime() ;
		
		display.setSelectedDay(_selectedDay) ;
		display.setDisplayMode(DisplayMode.DAY) ;
		display.setmonthSelectionDisplayedTime(_selectedDay) ;
		
		setMode(DisplayMode.DAY) ;
	}
	
	/**
	 * Change the selected day
	 * 
	 * @param tNewDay Day to set as new selected day
	 */
	protected void changeSelectedDay(final LdvTime tNewDay)
	{
		// If selected day didn't change, then nothing to do
		//
		if ((null == tNewDay) || (_selectedDay.deltaDaysLocal(tNewDay) == 0))
			return ;
		
		_selectedDay.initFromLdvTime(tNewDay) ;
		
		boolean bDontReloadEvents = isDayInTimeRange(tNewDay) ;
		
		if (false == bDontReloadEvents)
			setDisplayedTimeRangeForMode() ;
		
		display.setSelectedDay(_selectedDay) ;
		
		switch(_displayMode)
		{
			case DAY :
		
				// Refresh the display area
				//
				display.resetDayPanels() ;
				createDaysWorspaces() ;
		
				// Reload the list of events for selected day
				//
				getAppointmentsForSingleDay() ;
				
				break ;
				
			case WEEK :
				
				// Refresh the display area
				//
				display.resetDayPanels() ;
				createDaysWorspaces() ;
		
				// Reload the list of events for selected week
				//
				if (bDontReloadEvents)
					redrawEvents() ;
				else
					getAppointmentsForTimeInterval() ;
				
				break ;
				
			case MULTI_WEEK :
			case MONTH :
				
				// Refresh the display area
				//
				display.resetDayPanels() ;
				createDaySummariesWorspaces() ;
		
				// Reload the list of events for selected week
				//
				if (bDontReloadEvents)
					redrawEvents() ;
				else
					getAppointmentsForTimeInterval() ;
				
				break ;
		}
		
		display.setmonthSelectionDisplayedTime(_selectedDay) ;
		
		if      ((DisplayMode.DAY == _displayMode) || (DisplayMode.WEEK == _displayMode)) 
			display.selectDisplayedTime() ;
		else if ((DisplayMode.MULTI_WEEK == _displayMode) || (DisplayMode.MONTH == _displayMode)) 
			display.selectDisplayedTimeForSummary() ;
	}

	/**
	 * Change current period
	 * 
	 * @param iPreviousNext <code>-1</code> for previous period, <code>1</code> for next period 
	 */
	protected void changePeriod(final int iPreviousNext)
	{
		LdvTime tNewSelectedDay = new LdvTime(0) ;
		tNewSelectedDay.initFromLdvTime(_selectedDay) ;
		
		switch(_displayMode)
		{
			case DAY :
				tNewSelectedDay.addDays(iPreviousNext, true) ;
				break ;
			case WEEK :
			case MULTI_WEEK :
				tNewSelectedDay.addDays(iPreviousNext * 7, true) ;
				break ;
			case MONTH :
				tNewSelectedDay.addMonths(iPreviousNext, true) ;
				break ;
		}
		
		changeSelectedDay(tNewSelectedDay) ;
	}
	
	/**
	 * Change current period for now
	 */
	protected void changePeriodNow()
	{
		LdvTime tNow = new LdvTime(0) ;
		tNow.takeTime() ;
		
		changeSelectedDay(tNow) ;
	}
	
	/**
	 * Change current period a specific date
	 * 
	 * @param sDate Day, in AAAAMMDD format, to set as new selected day
	 */
	protected void changePeriodForDay(final String sDate)
	{
		if ((null == sDate) || "".equals(sDate))
			return ;
		
		LdvTime tNewSelectedDay = new LdvTime(0) ;
		tNewSelectedDay.initFromLocalDate(sDate) ;
		
		changeSelectedDay(tNewSelectedDay) ;
	}
	
	protected void setMode(DisplayMode mode)
	{
		_displayMode = mode ;
		
		setDisplayedTimeRangeForMode() ;
		
		if (DisplayMode.DAY == mode)
		{
			setSingleDayMode() ;
			getAppointmentsForSingleDay() ;
			return ;
		}
		if (DisplayMode.WEEK == mode)
		{
			setWeekMode() ;
			getAppointmentsForTimeInterval() ;
			return ;
		}
		if ((DisplayMode.MULTI_WEEK == mode) || (DisplayMode.MONTH == mode))
		{
			setMultiWeekMode() ;
			getAppointmentsForTimeInterval() ;
			return ;
		}
	}
	
	/**
	 * Initialize the workspace for a single day
	 */
	protected void setSingleDayMode()
	{
		Panel timeControlPanel = display.prepareWorkspaceForDayMode(_iHourHeightInPixels) ;
		
		LdvGinjector injector = _supervisor.getInjector() ;
		_timeController = injector.getAgendaTimeDisplayPresenter() ;
		
		initTimeControllerPanel(timeControlPanel) ;
	}
	
	/**
	 * Initialize the workspace for a week
	 */
	protected void setWeekMode()
	{
		Panel timeControlPanel = display.prepareWorkspaceForDayMode(_iHourHeightInPixels) ;
		
		LdvGinjector injector = _supervisor.getInjector() ;
		_timeController = injector.getAgendaTimeDisplayPresenter() ;
		
		initTimeControllerPanel(timeControlPanel) ;
	}
	
	/**
	 * Initialize the workspace for a 4 weeks display
	 */
	protected void setMultiWeekMode() 
	{
		display.prepareWorkspaceForWeeksMode() ;
		createDaySummariesWorspaces() ;
	}
	
	
	protected void getAppointmentsForSingleDay()
	{
		String sUserId = _supervisor.getUser().getLdvId() ;
		String sLdvId  = _supervisor.getDisplayedPerson().getLdvId() ;
		String sToken  = _supervisor.getSessionToken() ;
  	
		_dispatcher.execute(new GetPlannedEventsAction(_selectedDay.getLocalSimpleDate(), "", sLdvId, sUserId, sToken), new GetAppointmentsCallback()) ;
	}
	
	protected void getAppointmentsForTimeInterval()
	{
		String sUserId = _supervisor.getUser().getLdvId() ;
		String sLdvId  = _supervisor.getDisplayedPerson().getLdvId() ;
		String sToken  = _supervisor.getSessionToken() ;
  	
		_dispatcher.execute(new GetPlannedEventsAction(_displayedDayFrom.getLocalSimpleDate(), _displayedDayTo.getLocalSimpleDate(), sLdvId, sUserId, sToken), new GetAppointmentsCallback()) ;
	}
	
	/**
	*  Asynchronous callback function for calls to getAppointments
	**/
	public class GetAppointmentsCallback implements AsyncCallback<GetPlannedEventsResult> 
	{
		public GetAppointmentsCallback() {
			super();
		}

		@Override
		public void onFailure(final Throwable cause)
		{
			Log.error("Cannot get appointments:", cause) ;
			// eventBus.fireEvent(new GoToLdvAgendaEvent()) ;
		}

		@Override
		public void onSuccess(final GetPlannedEventsResult result)
		{
			// take the result from the server
			if (true == result.wasSuccessful())
			{
				// Fill the Events vector
				//
				_aEvents.clear() ;
				
				if (false == result.getEvents().isEmpty())
					for (Iterator<Event> it = result.getEvents().iterator() ; it.hasNext() ; )
						_aEvents.add(new Event(it.next())) ;
				
				redrawEvents() ;
			}
		}
	}
	
	/**
	*  Ask the agenda elements to redraw when events have changed
	**/
	protected void redrawEvents()
	{
		if (DisplayMode.DAY == _displayMode)
		{
			eventBus.fireEvent(new AgendaRedrawDayEvent(_selectedDay, _aEvents)) ;
			return ;
		}
		
		// For each day in the interval, select the list of concerned events, than ask for redraw 
		//
		LdvTime tDay = new LdvTime(0) ;
		tDay.initFromLdvTime(_displayedDayFrom) ;
		
		do
		{
			Vector<Event> dayEvents = new Vector<Event>() ;
			
			for (Iterator<Event> it = _aEvents.iterator() ; it.hasNext() ; )
			{
				Event event = it.next() ;
				
				if (AgendaDayPresenter.isConcernedByEvent(event, tDay))
					dayEvents.add(new Event(event)) ;
			}
			
			// Sort events by starting date
			//
			Collections.sort(dayEvents, new Event.EventComparator()) ;
			
			// Ask the day display widget to redraw
			//
			eventBus.fireEvent(new AgendaRedrawDayEvent(tDay, dayEvents)) ;
			
			tDay.addDays(1, true) ;
			
		} while (false == tDay.isAfter(_displayedDayTo)) ;
	}
	
	/**
	 * Send the time controller a command to become active 
	 * 
	 */
	private void initTimeControllerPanel(final Panel timeControlPanel)
	{
		// If AgendaDayControllerInitEvent is not handled yet, we have to defer fireEvent
		//
		if (false == eventBus.isEventHandled(AgendaDayControllerInitEvent.TYPE))
		{
			if (null == _pendingEvents) 
			{
				_pendingEvents = new ScheduledCommand() 
				{
	        public void execute() {
	        	_pendingEvents = null ;
	        	initTimeControllerPanel(timeControlPanel) ;
	        }
	      };
	      Scheduler.get().scheduleDeferred(_pendingEvents) ;
	    }
		}
		else
		{
			eventBus.fireEvent(new AgendaDayControllerInitEvent(timeControlPanel)) ;
			
			createDaysWorspaces() ;
		}
	}

	/**
	 * Ask the display to create a detail day workspace (ruler on left and day(s) on right)
	 */
	protected void createDaysWorspaces()
	{
		LdvGinjector injector = _supervisor.getInjector() ;
		
		switch(_displayMode)
		{
			case DAY :
		
				FocusPanel dayWorkspacePanel = display.createWorkspaceForDay(_selectedDay) ;
				display.displaySelectedDayLabel() ;

				AgendaDayPresenter dayPresenter = injector.getAgendaDayPresenter() ;

				initDayPresenter(dayPresenter, _selectedDay, dayWorkspacePanel) ;

				dayWorkspacePanel.addClickHandler(new ClickHandler() 
				{
					public void onClick(final ClickEvent event) 
					{
						manageDayClick(event, _selectedDay) ;
					}
				});
				
				break ;
				
			case WEEK :
		
				display.displaySelectedTimeIntervalLabel(_displayedDayFrom, _displayedDayTo) ;
				
				LdvTime tDay = new LdvTime(0) ;
				tDay.initFromLdvTime(_displayedDayFrom) ;
				
				// Create a workspace for each day between "day from" and "day to"
				//
				for (int i = 0 ; i < 7 ; i++)
				{
					FocusPanel dowWorkspacePanel = display.createWorkspaceForDay(tDay) ;
		
					AgendaDayView view = new AgendaDayView() ;
					AgendaDayPresenter dowPresenter = new AgendaDayPresenter(view, eventBus, _supervisor) ; 
					
					initDayPresenter(dowPresenter, tDay, dowWorkspacePanel) ;
					
					tDay.addDays(1, true) ;
				}
				
				break ;
		}
		
		display.selectDisplayedTime() ;
	}
	
	/**
	 * Ask the display to create a days summaries workspace (paved individual day boxes)
	 */
	protected void createDaySummariesWorspaces()
	{
		LdvGinjector injector = _supervisor.getInjector() ;
		
		int iWeeksCount = 4 ;  // count for MULTI_WEEK
		if (DisplayMode.MONTH == _displayMode)
		{
			int iNbDays = (int) _displayedDayTo.deltaDaysLocal(_displayedDayFrom) ;
			iWeeksCount = iNbDays / 7 ;
			if (iNbDays - iWeeksCount * 7 > 0)
				iWeeksCount++ ;
		}
			
		display.displaySelectedTimeIntervalLabel(_displayedDayFrom, _displayedDayTo) ;
						
		LdvTime tDay = new LdvTime(0) ;
		tDay.initFromLdvTime(_displayedDayFrom) ;
				
		// Create a workspace for each day between "day from" and "day to"
		//
		for (int i = 0 ; i < 7 * iWeeksCount ; i++)
		{
			boolean bDisplayMonth = false ;
			if ((0 == i) || (tDay.getLocalDate() == 1) || (tDay.getLocalDate() == tDay.daysCountWithinMonth()))
				bDisplayMonth = true ;
			
			Panel dowWorkspacePanel = display.createWorkspaceForDaySummary(tDay, iWeeksCount, bDisplayMonth) ;
		
			AgendaDaySummaryView view = new AgendaDaySummaryView() ;
			AgendaDaySummaryPresenter dowPresenter = new AgendaDaySummaryPresenter(view, eventBus, _supervisor) ; 
					
			initDaySummaryPresenter(dowPresenter, tDay, dowWorkspacePanel) ;
					
			tDay.addDays(1, true) ;
		}
		
		// Finalize, for example by setting a specific style to the selected day and to "now"
		//
		display.selectDisplayedTimeForSummary() ;		
	}
	
	/**
	 * Send the day controller a command to become active 
	 * 
	 */
	private void initDayPresenter(final AgendaDayPresenter dayPresenter, final LdvTime tDay, final Panel timeControlPanel)
	{
		// If AgendaDayInitEvent is not handled yet, we have to defer fireEvent
		//
		if (false == eventBus.isEventHandled(AgendaDayInitEvent.TYPE))
		{
			if (null == _pendingEvents) 
			{
				_pendingEvents = new ScheduledCommand() 
				{
	        public void execute() {
	        	_pendingEvents = null ;
	        	initDayPresenter(dayPresenter, tDay, timeControlPanel) ;
	        }
	      };
	      Scheduler.get().scheduleDeferred(_pendingEvents) ;
	    }
		}
		else
			eventBus.fireEvent(new AgendaDayInitEvent(dayPresenter, timeControlPanel, tDay, _timeController)) ;
	}
	
	/**
	 * Send the day summary controller a command to become active 
	 * 
	 */
	private void initDaySummaryPresenter(final AgendaDaySummaryPresenter dayPresenter, final LdvTime tDay, final Panel timeControlPanel)
	{
		// If AgendaDayInitEvent is not handled yet, we have to defer fireEvent
		//
		if (false == eventBus.isEventHandled(AgendaDayInitEvent.TYPE))
		{
			if (null == _pendingEvents) 
			{
				_pendingEvents = new ScheduledCommand() 
				{
	        public void execute() {
	        	_pendingEvents = null ;
	        	initDaySummaryPresenter(dayPresenter, tDay, timeControlPanel) ;
	        }
	      };
	      Scheduler.get().scheduleDeferred(_pendingEvents) ;
	    }
		}
		else
			eventBus.fireEvent(new AgendaDaySummaryInitEvent(dayPresenter, timeControlPanel, tDay, _timeController)) ;
	}
	
	/**
	 * Do what must be done when the window is resized 
	 * 
	 */
	public void resize(int iWidth, int iHeight)
	{
		display.onResize(iWidth, iHeight) ;
		
		if ((DisplayMode.DAY == _displayMode) || (DisplayMode.WEEK == _displayMode))
		{
			
		}
	}
	
	protected void refreshMonthSelector()
	{
		
	}
	
	/**
	 * Connect all day selection buttons  
	 * 
	 */
	protected void connectDaySelectorButtonsClickHandlers()
	{
/*
		Grid selectorButtonsGrid = display.getDaySelectorGrid() ;
		
		for (Iterator<Widget> BtnIter = selectorButtonsGrid.iterator() ; BtnIter.hasNext() ; )
		{
			Button button = (Button) BtnIter.next() ;
			if (null != button)
			{
				String sId = button.getElement().getId() ;
				if ((null != sId) && (false == sId.equals("")))
				{
					String[] decomposition = sId.split("_") ;
					final String sAction = decomposition[0] ;
					if ("day".equals(sAction))
					{
						final String sDay = decomposition[1] ;
						button.addClickHandler(new ClickHandler() {
								public void onClick(ClickEvent event) {
									changeSelectedDay(sDay) ;
								}
						}); 
					}
				}
			}
		}
*/		
/*
		ArrayList<Button> aButtons = display.getDaySelectorButtons() ;
		
		if ((null == aButtons) || aButtons.isEmpty())
			return ;
		
		for (Iterator<Button> it = aButtons.iterator() ; it.hasNext() ; )
		{
			Button button = it.next() ;
			
			String sId = button.getElement().getId() ;
			if ((null != sId) && (false == sId.equals("")))
			{
				String[] decomposition = sId.split("_") ;
				final String sAction = decomposition[0] ;
				if ("day".equals(sAction))
				{
					final String sDay    = decomposition[1] ;
			
					final LdvTime day = new LdvTime(0) ;
					day.initFromDate(sDay) ;
				
					button.addClickHandler(new ClickHandler() {
							public void onClick(ClickEvent event) {
								changeSelectedDay(day) ;
							}
					});
				}
			}
		}
*/
	}
	
	/**
	 * Initialize "day from" and "day to", the limits of displayed time interval.   
	 * 
	 */
	protected void setDisplayedTimeRangeForMode()
	{
		switch(_displayMode)
		{
			// Just one day... no brainer
			//
			case DAY : 
				_displayedDayFrom.initFromLdvTime(_selectedDay) ;
				_displayedDayTo.initFromLdvTime(_selectedDay) ;
				break ;
		
			// The week that includes selected day (from Monday to Sunday)
			//
			case WEEK : 
				setSelectedWeekBoundaries() ;
				break ;
		
			// The week that includes selected day plus next three weeks
			//
			case MULTI_WEEK : 
				setSelectedWeekBoundaries() ;
				_displayedDayTo.addDays(21, true) ;
				break ;
				
			// The month that includes selected day as 4 to 6 weeks
			//
			case MONTH : 
					setMonthBoundaries() ;
					break ;
		}
	}
	
	/**
	 * Initialize "day from" and "day to" for the week that includes selected day    
	 */
	protected void setSelectedWeekBoundaries() {
		setWeekBoundaries(_selectedDay) ;
	}
	
	/**
	 * Initialize "day from" and "day to" for the week that includes selected day    
	 */
	protected void setWeekBoundaries(final LdvTime targetDay)
	{
		// Get the day of week for the target
		int iDoW = targetDay.getLocalDayOfWeek() ;
		
		// To find "day from" to display, go from this day of week to Monday
		_displayedDayFrom.initFromLdvTime(targetDay) ;
		for (int i = iDoW ; i > 1 ; i--)
			_displayedDayFrom.addDays(-1, true) ;
		
		// To find "day to" to display, go from this day of week to Sunday
		_displayedDayTo.initFromLdvTime(targetDay) ;
		for (int i = iDoW ; i < 7 ; i++)
			_displayedDayTo.addDays(1, true) ;
	}
	
	/**
	 * Initialize "day from" and "day to" for the month that includes selected day    
	 */
	protected void setMonthBoundaries()
	{
		// Get the first day of the month
		LdvTime dayOne = new LdvTime(0) ;
		dayOne.initFromLdvTime(_selectedDay) ;
		dayOne.putLocalDate(1) ;
		
		// Get the week boundaries for the first week in the month
		setWeekBoundaries(dayOne) ;
		
		// Get the number of weeks needed to include end of month
		int iDaysInMonth    = _selectedDay.daysCountWithinMonth() ;
		int iRemainingDays  = iDaysInMonth - _displayedDayTo.getLocalDate() ;
		int iRemainingWeeks = iRemainingDays / 7 ;
		if (iRemainingDays - iRemainingWeeks * 7 > 0)
			iRemainingWeeks++ ;
		
		_displayedDayTo.addDays(7 * iRemainingWeeks, true) ;
	}
	
	/**
	 * Is a day inside the displayed time interval?   
	 * 
	 * @param tDay Date which status inside current time interval is to be checked
	 * 
	 * @return <code>true</code> if tDay is inside the 
	 */
	protected boolean isDayInTimeRange(final LdvTime tDay)
	{
		if (null == tDay)
			return false ;
		if (tDay.deltaDaysLocal(_displayedDayFrom) < 0)
			return false ;
		if (tDay.deltaDaysLocal(_displayedDayTo) > 0)
			return false ;
		return true ;
	}
	
	/**
	 * The user clicked inside the day workspace, either to edit an existing event or to create a new one   
	 * 
	 * @param tDay  Date which display area was clicked
	 * @param event The click event 
	 * 
	 */
	protected void manageDayClick(ClickEvent event, LdvTime tDay)
	{
		int iYclickPos = display.getClientYInDay(event.getClientY()) ;
		if (-1 == iYclickPos)
			return ;
		
		int iMinutesInDay = _timeController.getMinutesFromYCoordinate(iYclickPos) ;
		
		_editedEvent.reset() ; 
		
		// Initialize event starting time as date time pointed by the click
		//
		LdvTime startingTime = new LdvTime(0) ;
		startingTime.initFromLdvTime(tDay) ;
		startingTime.putLocalHours(0) ;
		startingTime.putLocalMinutes(0) ;
		startingTime.putLocalSeconds(0) ;
		startingTime.putLocalMilliseconds(0) ;
		
		startingTime.addMinutes(iMinutesInDay, true) ;
		_editedEvent.setDateStart(startingTime) ;
		
		// Initialize event ending time as starting time + X minutes
		//
		startingTime.addMinutes(15, true) ;
		_editedEvent.setDateEnd(startingTime) ;
		
		display.showEventEditDialog(_editedEvent) ;
	}
	
	/**
	 * Validate the edition of a new or already existing event    
	 */
	protected void validateEditedEvent()
	{
		if ("".equals(_editedEvent.getUID()))
			validateNewEvent() ;
	}
	
	/**
	 * Validate the edition of a new event    
	 */
	protected void validateNewEvent()
	{
		Event event = new Event() ;
		display.getEditedEvent(event) ;
		
		saveNewEvent(event) ;
	}
	
	/**
	*  Asynchronously save a new event
	**/
	protected void saveNewEvent(final Event event)
	{
		String sUserId = _supervisor.getUser().getLdvId() ;
		String sLdvId  = _supervisor.getDisplayedPerson().getLdvId() ;
		String sToken  = _supervisor.getSessionToken() ;
  	
		_dispatcher.execute(new SaveNewEventAction(event, sLdvId, sUserId, sToken), new SaveNewEventCallback()) ;
	}
	
	/**
	*  Asynchronous callback function for call to save a new event
	**/
	public class SaveNewEventCallback implements AsyncCallback<SaveNewEventResult> 
	{
		public SaveNewEventCallback() {
			super();
		}

		@Override
		public void onFailure(final Throwable cause)
		{
			Log.error("Cannot record appointment:", cause) ;
			// eventBus.fireEvent(new GoToLdvAgendaEvent()) ;
		}

		@Override
		public void onSuccess(final SaveNewEventResult result)
		{
			// take the result from the server
			if (true == result.wasSuccessful())
			{
				_aEvents.add(new Event(result.getEvent())) ;				
				redrawEvents() ;
			}
		}
	}
	
	/**
	 * Clear the display workspace in order to install the Availability components edition workspace
	 */
	protected void switchToAvailabilityWorkspace()
	{
		display.clearDisplayWorkspace() ;
		
		LdvGinjector injector = _supervisor.getInjector() ;
		injector.getAgendaAvailabilityManagementPresenter() ;
		
		// If AgendaAvailabilityManagerInitEvent is not handled yet, we have to defer fireEvent
		//
		if (false == eventBus.isEventHandled(AgendaAvailabilityManagerInitEvent.TYPE))
		{
			if (null == _pendingEvents) 
			{
				_pendingEvents = new ScheduledCommand() 
				{
					public void execute() {
		        _pendingEvents = null ;
		        eventBus.fireEvent(new AgendaAvailabilityManagerInitEvent(display.getDisplayWorkspacePanel())) ;
		      }
		    };
		    Scheduler.get().scheduleDeferred(_pendingEvents) ;
		  }
		}
		else
			eventBus.fireEvent(new AgendaAvailabilityManagerInitEvent(display.getDisplayWorkspacePanel())) ;
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
