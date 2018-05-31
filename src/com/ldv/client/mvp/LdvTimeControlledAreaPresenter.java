package com.ldv.client.mvp;

import java.util.ArrayList;
import java.util.Iterator;

import com.allen_sauer.gwt.log.client.Log;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.HasMouseDownHandlers;
import com.google.gwt.event.dom.client.HasMouseUpHandlers;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.inject.Inject;

import com.ldv.shared.graph.LdvModelGraph;
import com.ldv.shared.model.LdvTime;
import com.ldv.shared.rpc.UpdateGraphAction;
import com.ldv.shared.rpc.UpdateGraphResult;
import com.ldv.client.canvas.LdvScrollArea;
import com.ldv.client.event.LdvMainSentEvent;
import com.ldv.client.event.LdvMainSentEventHandler;
import com.ldv.client.event.LdvTimeControllerReadyEvent;
import com.ldv.client.event.LdvTimeControllerReadyEventHandler;
import com.ldv.client.event.LdvTimeControllerSentEvent;
import com.ldv.client.event.LdvProjectInitEvent;
import com.ldv.client.event.LdvProjectSetZPosEvent;
import com.ldv.client.event.LdvProjectSetZPosEventHandler;
import com.ldv.client.event.LdvProjectsEventEvent;
import com.ldv.client.event.LdvRedrawAllProjectsWindowEvent;
import com.ldv.client.event.LdvRedrawProjectRecursiveEvent;
import com.ldv.client.model.LdvModelConcern;
import com.ldv.client.model.LdvModelProject;
import com.ldv.client.mvp_toons.LdvTimeControllerPresenter;
import com.ldv.client.mvp_toons.LdvTimeControllerView;
import com.ldv.client.util.LdvGraphManager;
import com.ldv.client.util.LdvSupervisor;
import com.ldv.client.util.LdvTimeZoomLevel;
import com.ldv.client.util.LdvTimeZoomLevel.pixUnit;

import net.customware.gwt.dispatch.client.DispatchAsync;
import net.customware.gwt.presenter.client.EventBus;
import net.customware.gwt.presenter.client.widget.WidgetDisplay;
import net.customware.gwt.presenter.client.widget.WidgetPresenter;

/**
 * A time controlled area is a diachronic display area that can be used to manage projects<br>
 * <br>
 * It typically contains an array of projects and a time controller 
 * 
 * @author Philippe
 *
 */
public class LdvTimeControlledAreaPresenter extends WidgetPresenter<LdvTimeControlledAreaPresenter.Display>
{
	private LdvTime          _topRightTime ;     // Absolute reference for all time related functions
	private LdvTimeZoomLevel _currentZoomLevel ;
	private pixUnit          _iPixUnit ;

	private LdvTime 				 _minTime ;           // Min of projects min times
	private LdvTime					 _maxTime ;           // Max of projects max times
	private LdvTime					 _minScrollTime ;     // _minTime
	private LdvTime 				 _maxScrollTime ;     // Max(_topRightTime, _maxTime)
	
	private LdvGraphManager  _graphManager ;
	private LdvModelGraph    _updateProcessedGraph = null ;
	
	// Main components: projects and time controller
	//
	private ArrayList<LdvProjectWindowPresenter> _projects ;
	private LdvProjectWindowPresenter            _currentProject ;
	
	private LdvTimeControllerPresenter           _ldvTimeController ;

	// Miscellaneous
	//
	private final LdvSupervisor _supervisor ;
	private final DispatchAsync _dispatcher ;
	
	private RightScrollTimer    _rightTimer ;
	private LeftScrollTimer     _leftTimer ;
	private int                 _startThumbDragRelativeX ;
	private boolean             _onThumbDrag = false ;
	private String              _colorOnMove = "#FFFF99" ;
	private String              _colorOrigin = "#FFFFF0" ;
	
	private ScheduledCommand    _pendingEvents  = null ;
	private ScheduledCommand    _pendingEvents2 = null ;
	
	public interface Display extends WidgetDisplay 
	{	
		public AbsolutePanel        getMainPanel() ;	
		public HasClickHandlers     getRightButton() ;
		public HasMouseDownHandlers getRightButtonMouseDown() ;
		public HasMouseUpHandlers   getRightButtonMouseUp() ;
		public Button               getRightScrollButton() ;
		public HasClickHandlers     getLeftButton() ;
		public HasMouseDownHandlers getLeftButtonMouseDown() ;
		public HasMouseUpHandlers   getLeftButtonMouseUp() ;
		public Button               getLeftScrollButton() ;
		// public LdvTime              getTopRightTime();
		// public LdvTimeZoomLevel     getCurrentZoomLevel();
		// public void             		setTopRightTime(LdvTime topRightTime);
		// public void             		setCurrentZoomLevel(LdvTimeZoomLevel currentZoomLevel);
		public int                  getAbsoluteLeft() ;
		public int                  getAbsoluteTop() ;
		public int 									getAreaWidth() ;
		public int                  getProjectsAreaWidth() ;
		public HandlerRegistration  addMouseUpHandler(MouseUpHandler handler) ;
		//public int 									getScrollAreaWidth() ;
	} 

	@Inject
	public LdvTimeControlledAreaPresenter(final Display       display, 
			                                  final EventBus      eventBus,
			                                  final DispatchAsync dispatcher,
			                                  final LdvSupervisor supervisor) 
	{
		super(display, eventBus) ;
		
		Log.info("LdvTimeControlledAreaPresenter: entering constructor.") ;
		
		_dispatcher = dispatcher ;
		_supervisor = supervisor ;
		
		_rightTimer = new RightScrollTimer() ;
		_leftTimer  = new LeftScrollTimer() ;
		
		_graphManager = (LdvGraphManager) null ;
		
		_currentZoomLevel = new LdvTimeZoomLevel(pixUnit.pixMonth,0,0,0);
		_topRightTime = new LdvTime(0);
		_topRightTime.takeTime() ;
				
		_projects = new ArrayList<LdvProjectWindowPresenter>() ;
		
		bind() ;
		
		// display.setTopRightTime(_topRightTime) ;
		// display.setCurrentZoomLevel(_currentZoomLevel) ;
	}

	@Override
	protected void onBind() 
	{
		/**
		 * Event received when it is time to start
		 */
		eventBus.addHandler(LdvMainSentEvent.TYPE, new LdvMainSentEventHandler() 
		{
			@Override
			public void onMainSend(LdvMainSentEvent event) 
			{
				Log.info("LdvTimeControlledAreaPresenter: Handling RegisterSent event") ;
				bootstrap(event) ;
			}
		});
		
		/**
		 * Event received when time components are ready to work
		 */
		eventBus.addHandler(LdvTimeControllerReadyEvent.TYPE, new LdvTimeControllerReadyEventHandler() 
		{
			@Override
			public void onTimeControllerReady(LdvTimeControllerReadyEvent event) 
			{
				Log.info("LdvTimeControlledAreaPresenter: Time Controller is ready, now initializing the Time Controlled Area") ;
				initialize() ;				 
			}
		});
		
		/**
		 * Event received when a project asks to move to the top position
		 */
		eventBus.addHandler(LdvProjectSetZPosEvent.TYPE, new LdvProjectSetZPosEventHandler() 
		{
			@Override
			public void onProjectSetZOrderSend(LdvProjectSetZPosEvent event) 
			{
				LdvProjectWindowPresenter project = getProjectFromUri(event.getProjectUri()) ;
				if (null != project)
					changeProjectPosition(project, event.getMovementType()) ;				 
			}
		});
		
/*
		display.getRightButton().addClickHandler(new ClickHandler()
		{
			public void onClick(final ClickEvent event)
			{
				doRight() ;
				display.getScrollArea().moveThumb(1) ;
				redrawProjectsWindows() ;
			}
		});
*/
		
		display.getRightButtonMouseDown().addMouseDownHandler(new MouseDownHandler()
		{
			public void onMouseDown(final MouseDownEvent event)
			{
				_rightTimer.run() ;
				_rightTimer.scheduleRepeating(100) ;
			}
		}); 
		
		display.getRightButtonMouseUp().addMouseUpHandler(new MouseUpHandler()
		{
			public void onMouseUp(final MouseUpEvent event){
				_rightTimer.cancel() ;
			}
		});

/*
		display.getLeftButton().addClickHandler(new ClickHandler(){
			public void onClick(final ClickEvent event)
			{
				doLeft() ;
				display.getScrollArea().moveThumb(-1) ;
				redrawProjectsWindows() ;
			}
		});
*/
		
		display.getLeftButtonMouseDown().addMouseDownHandler(new MouseDownHandler()
		{
			public void onMouseDown(final MouseDownEvent event)
			{
				// Log.info("LdvTimeControlledAreaPresenter Handling LeftButtonMouseDown event") ;
				
				_leftTimer.run() ;
				_leftTimer.scheduleRepeating(100) ;
			}
		}); 
		
		display.getLeftButtonMouseUp().addMouseUpHandler(new MouseUpHandler()
		{
			public void onMouseUp(final MouseUpEvent event)
			{
				// Log.info("LdvTimeControlledAreaPresenter Handling LeftButtonMouseUp event") ;
				
				_leftTimer.cancel() ;
			}
		});
		
		display.addMouseUpHandler(new MouseUpHandler()
		{
			public void onMouseUp(final MouseUpEvent event)
			{
				eventBus.fireEvent(new LdvProjectsEventEvent(LdvProjectsEventEvent.EventType.eventMouseUp)) ;
			}
		});
	}
	
	/**
	 * Initialize from a bootstrap event 
	 */
	private void bootstrap(LdvMainSentEvent event)
	{
		event.getWorkspace().clear();
		 
		_graphManager = _supervisor.getDisplayedGraph() ;
		
		// Add display to workspace
		//
		FlowPanel workspace = (FlowPanel) event.getWorkspace() ;
		workspace.add(getDisplay().asWidget()) ;
		
		open() ;
	}
	
	/**
	 * Add mouse handlers for thumb
	 */
	private void addThumbHandlers()
	{
		getThumb().addMouseUpHandler(new MouseUpHandler()
		{
			@Override
			public void onMouseUp(MouseUpEvent event)
			{
				_onThumbDrag = false ;
				getThumb().getElement().getStyle().setBackgroundColor(_colorOrigin) ;
			}
		});
		
		getThumb().addMouseDownHandler(new MouseDownHandler()
		{			
			@Override
			public void onMouseDown(MouseDownEvent event)
			{
				_onThumbDrag = true ;
				_startThumbDragRelativeX = event.getRelativeX(getThumb().getElement()) ;				
			}
		});
		
		getThumb().addMouseMoveHandler(new MouseMoveHandler()
		{		
			@Override
			public void onMouseMove(MouseMoveEvent event)
			{
				if (_onThumbDrag)
				{	
					getThumb().getElement().getStyle().setBackgroundColor(_colorOnMove) ;
					
					int relativeX = event.getRelativeX(getThumb().getElement()) ;
					int distance = relativeX - _startThumbDragRelativeX ;
/*					
					String edge = "0px";
					if(display.getThumb().getElement().getStyle().getLeft().equals(edge)){
					
					}else if(display.getThumb().getElement().getStyle().getRight().equals(edge)){
												
					}else{
						display.getScrollArea().moveThumb(distance) ;
					}
*/					
					getScrollArea().moveThumb(distance) ;
					
					if (distance > 0)
					{
						doRight() ;
						redrawProjectsWindows() ;			
					}
					else
					{
						doLeft() ;
						redrawProjectsWindows() ;			
					}
				}				
			}
		});
	}
	
	/**
	 * Creates all projects and initializes time manager
	 */
	private void open()
	{
		createTimeController() ;
	}
	
	/**
	 * Function called when Time Controller is ready to operate
	 * 
	 * Creates all projects and initializes time manager
	 */
	private void initialize()
	{
		addThumbHandlers() ;
		
		// Set position for "now"
		//
		// LdvTime timeNow = new LdvTime(0) ;
		// timeNow.takeTime() ;
				
		// resetTopRightTime(timeNow, (double) 2 / (double) 3) ;
		
		// Create projects
		//
		Log.info("LdvTimeControlledAreaPresenter: Time Controlled Area, creating projects") ;
		
		int iZorder = 1 ;
		
		if (_projects.isEmpty() && (null != _graphManager))
		{
			ArrayList<LdvModelProject> projectsModels = _graphManager.getProjects() ;
			
			if (false == projectsModels.isEmpty())
			{
				// Create a view for each project
				//
				for (Iterator<LdvModelProject> iter = projectsModels.iterator() ; iter.hasNext() ; ) 
				{
					LdvModelProject projectModel = iter.next() ;
				 	
					LdvProjectWindowView view = new LdvProjectWindowView() ;
					LdvProjectWindowPresenter projectPresenter = new LdvProjectWindowPresenter(view, eventBus, _dispatcher, _supervisor) ; 
					
					projectPresenter.setZOrder(iZorder) ;
					iZorder++ ;
			
					_projects.add(projectPresenter) ;
					initProject(projectPresenter, projectModel) ;
				}
			}
		}
		
		//set the first project as the current project
		if (false == _projects.isEmpty())
			_projects.get(0).setIsCurrentProject(true) ;
		setCurrentProject() ;
		
		resetTimeController() ;
		
		if (false == _projects.isEmpty())
			firstRedrawRecursive(_projects.size()) ;
	}
	
	/**
	 * Create the time controller and send it a bootstrap message
	 */
	private void createTimeController()
	{
		// Create controller
		//
		LdvTimeControllerView timeControllerView = new LdvTimeControllerView() ;
		_ldvTimeController = new LdvTimeControllerPresenter(timeControllerView, eventBus, _supervisor) ;
		
		// Send controller a startup message
		// 
		// If LdvTimeControllerSentEvent is not handled yet, we have to defer fireEvent
		//
		if (false == eventBus.isEventHandled(LdvTimeControllerSentEvent.TYPE))
		{
			if (null == _pendingEvents) 
			{
				_pendingEvents = new ScheduledCommand() 
				{
	        public void execute() {
	        	_pendingEvents = null ;
	        	eventBus.fireEvent(new LdvTimeControllerSentEvent(display.getMainPanel(), _topRightTime, _currentZoomLevel, display.getRightScrollButton(), display.getLeftScrollButton(), _ldvTimeController)) ;
	        }
	      };
	      Scheduler.get().scheduleDeferred(_pendingEvents) ;
	    }
		}
		else
			eventBus.fireEvent(new LdvTimeControllerSentEvent(display.getMainPanel(), _topRightTime, _currentZoomLevel, display.getRightScrollButton(), display.getLeftScrollButton(), _ldvTimeController)) ;
	}
	
	/**
	 * Sends a project its bootstrap message
	 * 
	 * @param projectPresenter Project to bootstrap
	 * @param projectModel     Content to populate the project
	 */
	public void initProject(final LdvProjectWindowPresenter projectPresenter, final LdvModelProject projectModel)
	{
		if (null == projectPresenter)
			return ;
		
		// If LdvProjectInitEvent is not handled yet, we have to defer fireEvent
		//
		if (false == eventBus.isEventHandled(LdvProjectInitEvent.TYPE))
		{
			_pendingEvents = new ScheduledCommand() 
				{
					public void execute() 
					{
						_pendingEvents = null ;
						initProject(projectPresenter, projectModel) ;
		      }
				};
		  Scheduler.get().scheduleDeferred(_pendingEvents) ;
		}
		else
			eventBus.fireEvent(new LdvProjectInitEvent(display.getMainPanel(), projectPresenter, this, projectModel)) ;
	}
	
	public void firstRedrawRecursive(final int iZorder)
	{
		// If LdvProjectInitEvent is not handled yet, we have to defer fireEvent
		//
		if (false == eventBus.isEventHandled(LdvRedrawProjectRecursiveEvent.TYPE))
		{
			_pendingEvents2 = new ScheduledCommand() 
				{
					public void execute() 
					{
						_pendingEvents2 = null ;
						firstRedrawRecursive(iZorder) ;
		      }
				};
		  Scheduler.get().scheduleDeferred(_pendingEvents2) ;
		}
		else
		{	
			eventBus.fireEvent(new LdvRedrawProjectRecursiveEvent(iZorder)) ;
		}
	}
	
	public void resetTimeController()
	{
		ArrayList<LdvModelProject> projectsModels = _graphManager.getProjects() ; 
		
		_minTime = this.getProjectsMinTime(projectsModels) ;
		_maxTime = this.getProjectsMaxTime(projectsModels) ;
		
		_minScrollTime = _minTime ;
		
		// if(_maxTime.isAfter(_topRightTime))
			resetTopRightTime(_topRightTime, (double)2/3) ;  //_topRightTime has been changed by this method
				
		if (_maxTime.isAfter(_topRightTime))
			_maxScrollTime = _maxTime ;
		else
			_maxScrollTime = _topRightTime ;

		_ldvTimeController.refresh(_topRightTime, _currentZoomLevel) ;		
	}
	
	/**
	 * Set top right time so that "now" separates past and future according to a given ratio
	 * 
	 * @param todayTime Today as LdvTime object
	 * @param dPastOverFutureRatio Ratio Past/Future (it is 2/3 if "now" is 1/3 to the right)
	 */
	public void resetTopRightTime(LdvTime todayTime, double dPastOverFutureRatio)
	{	
		// Set top right time as today
		_topRightTime = todayTime ;
		
		if ((double) 1 == dPastOverFutureRatio)
			return ;
		
		// Get width of "future" area in pixels
		int iFuturePixelWidth = (int) ((double)display.getAreaWidth() * (1 - dPastOverFutureRatio)) ;
		// Convert this width form pixels to time units
		int iFutureTimeUnitWidth = _ldvTimeController.getTimeUnitFromPhysicalWidth(iFuturePixelWidth) ;
		
		addPixUnitsToLdvTime(_topRightTime, iFutureTimeUnitWidth) ;
	}
	
	/**
	 * Set Thumb position and width for focused Project
	 * 
	 */
	public void resetThumb()
	{	
		setThumbWidth(_currentProject) ;		
		setThumbPosition(_currentProject) ;					
	}
	
	/**
	 * Set the Thumb position for a given Project
	 * 
	 * (Thumb's left position represents interval between _minScrollTime and Project window's left side)  
	 * 
	 * @param project Project the Thumb is to be set according to
	 */
	private void setThumbPosition(LdvProjectWindowPresenter project)
	{			
		// Get distance between Project's left side and TimeControlledArea's right side
		int iCurrentProjectBeginPositionFromRight = getProjectBeginPositionFromRight(project) ;
		
		// Get scroll area width in pixels
		int scrollAreaWidth = getScrollArea().getOffsetWidth() ;
		
		// Get scroll interval (between min and max scroll times) in pixels
		double scrollAreaIntervalInDay = _maxScrollTime.deltaDaysUTC(_minScrollTime) ;
		double scrollAreaInterval = transformInterval(scrollAreaIntervalInDay) ;
		
		// time unit X is scroll interval divided by scroll width (means really scrolling thumb one pixel represents scrolling X pixels inside project)  
		int timeUnitOfScrollArea = (int) (scrollAreaInterval / scrollAreaWidth) ;
		
		// get time for Project window's left side
		LdvTime projectBeginTime = this.getLdvTimeFromPosition(iCurrentProjectBeginPositionFromRight) ;
		
		// get number of days between min scroll time and Project window's left side and switch to pixels 
		double frontScrollAreaIntervalInDay = projectBeginTime.deltaDaysUTC(_minScrollTime) ; 
		double frontScrollAreaInterval = transformInterval(frontScrollAreaIntervalInDay) ;
		
		// get this number of pixels expressed in "scroll area unit"
		int thumbPosition = (int) (frontScrollAreaInterval / timeUnitOfScrollArea) ;
		
		// set thumb position
		getThumb().getElement().getStyle().setLeft(thumbPosition, Style.Unit.PX) ;
	}
	
	/**
	 * Set the Thumb width for a given Project
	 * <br>
	 * <br>(Thumb's width represents Project window's width in Thumb's coordinates)  
	 * 
	 * @param project Project the Thumb is to be set according to
	 */
	private void setThumbWidth(LdvProjectWindowPresenter project)
	{	
		setProjectWidthRatio(project) ;
		
		int scrollAreaWidth = getScrollArea().getOffsetWidth() ;
		
		int thumbWidth = (int) (project.getProjectScrollAreaWidthRatio() * scrollAreaWidth) ;
		String width_String = Integer.toString(thumbWidth) + "px" ;
		
		getScrollArea().setThumbWidth(thumbWidth) ;
		getThumb().setWidth(width_String) ;	
	}
	
	/**
	 * Set ratio between Project window's width and Ligne de vie's scrollable area width
	 * 
	 * @param project Project which ratio is to be set
	 */
	private void setProjectWidthRatio(LdvProjectWindowPresenter project)
	{	
		// Get Project window's width
		int projectWidth = project.getDisplay().getMainTimeControlledPanel().getOffsetWidth() ;
		// Get corresponding number of time units
		double projectInterval = _ldvTimeController.getTimeUnitFromPhysicalWidth(projectWidth) ;
		
		// Get TimeControlledArea width in days (as the largest area to be possibly scrolled)
		double scrollAreaIntervalInDay = _maxScrollTime.deltaDaysUTC(_minScrollTime) ;
		// Get corresponding number of time units
		double scrollAreaInterval = transformInterval(scrollAreaIntervalInDay) ;
		
		double ratio = projectInterval / scrollAreaInterval ;
		if (ratio > 1)
			ratio = 1 ;
		
		project.setProjectScrollAreaWidthRatio(ratio) ;				
	}
	
	/**
	 * Get left offset of the Project inside this TimeControlledArea 
	 * 
	 * @return pixels count as int
	 */
	public int getProjectLeftOffset(LdvProjectWindowPresenter project)
	{
		// Get left side position inside browser's client area for both project and time controlled area 
		int iProjectAbsoluteLeft = project.getDisplay().getMainTimeControlledPanel().getAbsoluteLeft() ;
		int iAreaAbsoluteLeft    = getDisplay().getAbsoluteLeft() ;
		
		// Distance between area's left side and project's left side
		return iProjectAbsoluteLeft - iAreaAbsoluteLeft ;
	}
	
	/**
	 * Get width in pixels between a Project left boundary and this TimeControlledArea right boundary 
	 * 
	 * @param project LdvProjectWindowPresenter whose position is to be evaluated
	 * @return pixels count as int
	 */
	public int getProjectBeginPositionFromRight(LdvProjectWindowPresenter project)
	{
		// Distance between area's left side and project's left side
		int iProjectOffsetInsideArea = getProjectLeftOffset(project) ;
		
		// Get TimeControlledArea width
		int controlledAreaWidth = display.getAreaWidth() ;
		
		// Return delta
		return (controlledAreaWidth - iProjectOffsetInsideArea) ;	
	}
	
	/**
	 * Given an interval expressed in "days", returns interval in time units 
	 * 
	 * @param intervalInDay Interval as a number of days
	 * @return Interval as a number of time units
	 */
	public double transformInterval(double intervalInDay)
	{	
		double result ;
		switch (_currentZoomLevel.getPixUnit()) 
		{
			case pixSecond :
				result = intervalInDay * 86400 ;
				break;
			case pixMinute :
				result = intervalInDay * 1440 ;
				break;
			case pixHour :
				result = intervalInDay * 24 ;
				break;
			case pixDay :
				result = intervalInDay ;
				break;
			case pixWeek :
				result = intervalInDay / 7 ;
				break;
			case pixMonth :
				result = intervalInDay / 30 ;
				break;
			default :
				result = 0 ;
				break;
		}				
		return result ;
	}
	
	/**
	 * Go one step (according to current time unit) to the right (toward future)
	 */
	public void doRight()
	{	
		// Log.info("Calling doRight") ;
		// _topRightTime     = display.getTopRightTime() ;
		// _currentZoomLevel = display.getCurrentZoomLevel() ;
		
		shiftBlock(1) ;
		
		// refresh time controller
		refreshTimeController() ;				
	}
	
	/**
	 * Go one step (according to current time unit) to the left (toward past)
	 */
	public void doLeft()
	{	
		// Log.info("Calling doLeft") ;
		// _topRightTime     = display.getTopRightTime() ;
		// _currentZoomLevel = display.getCurrentZoomLevel() ;
		
		shiftBlock(-1) ;
		
		// refresh time controller
		refreshTimeController() ;		
	}
	    
	/**
	 * Shift a given amount of time units to the past (negative value) or the future (positive value)<br>
	 * This function updates _topRightTime
	 * 
	 * @param iNumSteps Number of time unit steps to shift (positive for future, negative for past)
	 */
	public void shiftStep(int iNumSteps) 
	{
		switch (_currentZoomLevel.getPixUnit()) 
		{
			case pixSecond :
				_topRightTime.addSeconds(iNumSteps, true) ;
				break;
			case pixMinute :
				_topRightTime.addMinutes(iNumSteps, true) ;
				break;
			case pixHour :
				_topRightTime.addHours(iNumSteps, true) ;
				break;
			case pixDay :
				_topRightTime.addDays(iNumSteps, true) ;
				break;
			case pixWeek :
				_topRightTime.addWeeks(iNumSteps, true) ;
				break;
			case pixMonth :
				_topRightTime.addMonths(iNumSteps, true) ;
				break;
		}
	}
		
	/**
	 * Shift a block of time units to the past (negative value) or the future (positive value)<br>
	 * This function updates _topRightTime
	 * 
	 * @param iNumBlocks Number of time unit blocks to shift (positive for future, negative for past)
	 */
	public void shiftBlock(int iNumBlocks) 
	{
		switch (_currentZoomLevel.getPixUnit()) 
		{
			case pixSecond :
				_topRightTime.addMinutes(iNumBlocks, true) ;
				break;
			case pixMinute :
				_topRightTime.addHours(iNumBlocks, true) ;
				break;
			case pixHour :
				_topRightTime.addDays(iNumBlocks, true) ;
				break;
			case pixDay :
				_topRightTime.addMonths(iNumBlocks, true) ;
				break;
			case pixWeek :
				_topRightTime.addYears(iNumBlocks, true) ;
				break;
			case pixMonth :
				_topRightTime.addYears(iNumBlocks, true) ;
				break;
		}
	}
	
	/**
	 *  Zoom one step in time
	 */
	public void zoom(LdvTime timeForX)
	{
		LdvTimeZoomLevel.pixUnit newPixUnit = LdvTimeZoomLevel.pixUnit.pixDay ;
		
		switch (_currentZoomLevel.getPixUnit()) 
		{
			case pixSecond :
				return ;
			case pixMinute :
				newPixUnit = LdvTimeZoomLevel.pixUnit.pixSecond ;
				break ;
			case pixHour :
				newPixUnit = LdvTimeZoomLevel.pixUnit.pixMinute ;
				break;
			case pixDay :
				newPixUnit = LdvTimeZoomLevel.pixUnit.pixHour ;
				break;
			case pixWeek :
				newPixUnit = LdvTimeZoomLevel.pixUnit.pixDay ;
				break ;
			case pixMonth :
				newPixUnit = LdvTimeZoomLevel.pixUnit.pixWeek ;
				break;
		}
		
		changePixUnit(timeForX, newPixUnit)  ;
	}
	
	/**
	 *  Enlarge one step in time
	 */
	public void enlarge(LdvTime timeForX)
	{
		LdvTimeZoomLevel.pixUnit newPixUnit = LdvTimeZoomLevel.pixUnit.pixDay ;
		
		switch (_currentZoomLevel.getPixUnit()) 
		{
			case pixSecond :
				newPixUnit = LdvTimeZoomLevel.pixUnit.pixMinute ;
				break ;
			case pixMinute :
				newPixUnit = LdvTimeZoomLevel.pixUnit.pixHour ;
				break ;
			case pixHour :
				newPixUnit = LdvTimeZoomLevel.pixUnit.pixDay ;
				break;
			case pixDay :
				newPixUnit = LdvTimeZoomLevel.pixUnit.pixWeek ;
				break;
			case pixWeek :
				newPixUnit = LdvTimeZoomLevel.pixUnit.pixMonth ;
				break ;
			case pixMonth :
				return ;
		}
		
		changePixUnit(timeForX, newPixUnit)  ;
	}
	
	/**
	 * Set a new pix unit while keeping current pointer at the same place for the same date 
	 * 
	 * @param timeForX Date that is to be kept at the same position after change
	 * @param newPixUnit New pix unit
	 */
	public void changePixUnit(LdvTime timeForX, LdvTimeZoomLevel.pixUnit newPixUnit) 
	{
		int iOffset = getInternalPhysicalPosition(timeForX) ;
		int iRightOffset = display.getAreaWidth() - iOffset ;
		
		_currentZoomLevel.setPixUnit(newPixUnit) ;
		
		// We have to keep timeForX at the same place inside Time Controlled Area
		//
		int iTimeUnitRightOffset = _ldvTimeController.getTimeUnitFromPhysicalWidth(iRightOffset) ;
		
		_topRightTime = timeForX ;
		shiftStep(iTimeUnitRightOffset) ; 
		
		refreshTimeController() ;
		redrawProjectsWindows() ;
	}
	
	/**
	 * Set a new pix unit while keeping the pointer at the same place in current project 
	 * 
	 * @param iXPosInCurrentProject Pointer's X coordinate inside current project
	 * @param newPixUnit New pix unit
	 */
	public void changePixUnit(int iXPosInCurrentProject, LdvTimeZoomLevel.pixUnit newPixUnit) 
	{
		int iLeftProjectX = getProjectBeginPositionFromRight(_currentProject) ;
		int iXPosInArea = iLeftProjectX - iXPosInCurrentProject ; 
		
		LdvTime timeForX = getLdvTimeFromPosition(iXPosInArea) ;
		
		changePixUnit(timeForX, newPixUnit) ;
	}
	
	/**
	 * Get oldest date when something happened in projects 
	 * 
	 * @param projectsModelsArray ArrayList of LdvModelProject objects
	 * @return Minimal time as LdvTime
	 */
	public LdvTime getProjectsMinTime(ArrayList<LdvModelProject> projectsModelsArray)
	{	
		LdvTime minTime = new LdvTime(0) ;
		minTime.setNoLimit() ;	
		ArrayList<LdvModelConcern> concernModelArray ;
		
		for(Iterator<LdvModelProject> iter = projectsModelsArray.iterator() ; iter.hasNext() ; )
		{
			LdvModelProject modelProject = iter.next() ;
			concernModelArray = modelProject.getConcerns() ;
			
			LdvTime beginDate = getLinesMinTime(concernModelArray) ;
			
			if (beginDate.isBefore(minTime))
				minTime.initFromLdvTime(beginDate) ;
		}
		
		return minTime ;
	}
	
	public LdvTime getProjectsMaxTime(ArrayList<LdvModelProject> projectsModelsArray)
	{	
		LdvTime maxTime = new LdvTime(0) ;
		ArrayList<LdvModelConcern> concernModelArray ;
		
		for (Iterator<LdvModelProject> iter = projectsModelsArray.iterator() ; iter.hasNext() ; )
		{
			LdvModelProject modelProject = iter.next() ;
			concernModelArray = modelProject.getConcerns() ;
			
			LdvTime endDate = getLinesMaxTime(concernModelArray) ;
			
			if (endDate.isAfter(maxTime))
				maxTime.initFromLdvTime(endDate) ;
		}
		
		return maxTime ;
	}
	
	/**
	 * Get the smallest date appearing in a concern array (as min of starting dates)
	 * 
	 * @param concernModelArray Array of LdvModelConcern objects
	 * @return min of starting dates as LdvTime 
	 */
	public LdvTime getLinesMinTime(ArrayList<LdvModelConcern> concernModelArray){
		
		LdvTime minTime = new LdvTime(0) ;
		minTime.setNoLimit() ;		
		
		for(Iterator<LdvModelConcern> iter = concernModelArray.iterator(); iter.hasNext();)
		{
			LdvModelConcern concernModel = iter.next() ;
			LdvTime beginDate = concernModel.getBeginDate() ;
			if (beginDate.isBefore(minTime))
				minTime.initFromLdvTime(beginDate) ;
		}
		
		return minTime ;		 
	}
	
	/**
	 * get the biggest date appearing in a concern array (as max of ending dates and starting dates, discarding "no limit information")
	 * 
	 * @param concernModelArray Array of LdvModelConcern objects
	 * @return max of genuine dates as LdvTime 
	 */
	public LdvTime getLinesMaxTime(ArrayList<LdvModelConcern> concernModelArray)
	{	
		LdvTime maxTime = new LdvTime(0) ;
		
		for (Iterator<LdvModelConcern> iter = concernModelArray.iterator() ; iter.hasNext() ; )
		{
			LdvModelConcern concernModel = iter.next() ;
			LdvTime endDate = concernModel.getEndDate() ;
			
			if (endDate.isNoLimit())
				endDate = concernModel.getBeginDate() ;
			
			if (endDate.isAfter(maxTime))
				maxTime.initFromLdvTime(endDate) ;
		}
		
		return maxTime ;		 
	}
	
	/**
	 * Get the LdvTime for a given position in pixels from TimeControlledArea's right edge
	 * 
	 * @param positionFromSpaceRightEdge position in pixels
	 * @return LdvTime for this position
	 */
	public LdvTime getLdvTimeFromPosition(int positionFromSpaceRightEdge)
	{	
		LdvTime result = new LdvTime(0) ;
		result.initFromLdvTime(_topRightTime) ;
		
		/*
		int TimeWidgetleftEdgePosition = _ldvTimeController.getDisplayAbsoluteLeft() ;
		int interval = positionFromSpaceRightEdge - TimeWidgetleftEdgePosition ;
		int timeUnit = - _ldvTimeController.getTimeUnitFromPhysicalWidth(interval) ;
		*/
		
		int timeUnit = - _ldvTimeController.getTimeUnitFromPhysicalWidth(positionFromSpaceRightEdge) ;
		
		addPixUnitsToLdvTime(result, timeUnit) ;
				
		return result ;		
	}
	
	/**
	 * Add some pix units to a given time
	 * 
	 * @param timeToMove LdvTime object to be modified
	 * @param iPixUnitsCount Count of pix units to be added (can be negative)
	 */
	public void addPixUnitsToLdvTime(LdvTime timeToMove, int iPixUnitsCount)
	{	
		if ((null == timeToMove) || (0 == iPixUnitsCount))
			return ;
		
		switch (_currentZoomLevel.getPixUnit()) 
		{
			case pixSecond :
				timeToMove.addSeconds(iPixUnitsCount, true) ;
				break ;
			case pixMinute :
				timeToMove.addMinutes(iPixUnitsCount, true) ;
				break ;
			case pixHour :
				timeToMove.addHours(iPixUnitsCount, true) ;
				break ;
			case pixDay :
				timeToMove.addDays(iPixUnitsCount, true) ;
				break ;
			case pixWeek :
				timeToMove.addWeeks(iPixUnitsCount, true) ;
				break ;
			case pixMonth :
				timeToMove.addMonths(iPixUnitsCount, true) ;
				break ;
			case pixYear :
				timeToMove.addYears(iPixUnitsCount, true) ;
				break ;
		}
	}
	
	/**
	 * Initializes _currentProject by finding the project with _isCurrentProject set to true
	 */
	private void setCurrentProject()
	{
		if (_projects.isEmpty())
			return ;
		
		for (Iterator<LdvProjectWindowPresenter> iter = _projects.iterator() ; iter.hasNext() ; )
		{
			LdvProjectWindowPresenter project = iter.next() ;
			if (project.getIsCurrentProject() == true)
			{
				this._currentProject = project ;
				return ;
			}
		}
	}
	
	private LdvProjectWindowPresenter getProjectFromUri(String sProjectUri)
	{
		if (_projects.isEmpty())
			return (LdvProjectWindowPresenter) null ;
		
		for (Iterator<LdvProjectWindowPresenter> iter = _projects.iterator() ; iter.hasNext() ; )
		{
			LdvProjectWindowPresenter project = iter.next() ;
			if (project.getProjectUri().equals(sProjectUri))
				return project ;
		}
		
		return (LdvProjectWindowPresenter) null ;
	}
	
	public pixUnit getPixUnit() {
		return _iPixUnit ;
	}
	
	private class RightScrollTimer extends Timer
	{
		@Override
		public void run()
		{
			doRight() ;
			getScrollArea().moveThumb(1) ;
			redrawProjectsWindows() ;
		}
	}
	
	private class LeftScrollTimer extends Timer
	{
		@Override
		public void run()
		{
			doLeft() ;
			getScrollArea().moveThumb(-1) ;
			redrawProjectsWindows() ;
		}
	}
	
	/**
	 * Ask all projects to redraw
	 */
	private void redrawProjectsWindows()
	{
		if (_projects.isEmpty())
			return ;
		
		Log.info("LdvTimeControlledAreaPresenter::redrawProjectsWindows firing LdvRedrawAllProjectsWindowEvent event") ;
		
		eventBus.fireEvent(new LdvRedrawAllProjectsWindowEvent()) ;
		
/*
		for (Iterator<LdvProjectWindowPresenter> iter = _projects.iterator() ; iter.hasNext() ; )
		{
			LdvProjectWindowPresenter project = iter.next() ;
			eventBus.fireEvent(new LdvRedrawProjectWindowEvent(project)) ;
		}
*/
	}
	
	/**
	 * A project is to be moved
	 * 
	 * @param project      The project to move
	 * @param movementType The movement to be made (move front, back, up or down) 
	 */
	private void changeProjectPosition(LdvProjectWindowPresenter project, LdvProjectSetZPosEvent.ZPosMovement movementType)
	{
		if (null == project)
			return ;
		
		int iPreviousZPos = project.getZOrder() ;
		
		switch (movementType)
		{
			case moveFront :
				
				// If this project is already in front, there is nothing to do
				//
				if (1 == iPreviousZPos)
					return ;
				
				// moveDownProjects(1, iPreviousZPos) ;
				// project.setZOrder(1) ;
				rollZOrder(iPreviousZPos, 1) ;
				break ;
				
			case moveBack :
				
				// If this project is already back, there is nothing to do
				//
				if (_projects.size() == iPreviousZPos)
					return ;
				
				// moveUpProjects(iPreviousZPos, _projects.size()) ;
				// project.setZOrder(_projects.size()) ;
				rollZOrder(iPreviousZPos, _projects.size()) ;
				break ;
				
			case moveDown :
				
				// If this project is already back, there is nothing to do
				//
				if (_projects.size() == iPreviousZPos)
					return ;
				
				// moveUpProjects(iPreviousZPos + 1, iPreviousZPos + 1) ;
				// project.setZOrder(iPreviousZPos + 1) ;
				rollZOrder(iPreviousZPos, iPreviousZPos + 1) ;
				break ;
				
			case moveUp :
				
				// If this project is already in front, there is nothing to do
				//
				if (1 == iPreviousZPos)
					return ;
				
				// moveDownProjects(iPreviousZPos - 1, iPreviousZPos - 1) ;
				// project.setZOrder(iPreviousZPos - 1) ;
				rollZOrder(iPreviousZPos, iPreviousZPos - 1) ;
				break ;
		}
		
		// Ask projects to redraw recursively, starting from back
		//
		eventBus.fireEvent(new LdvRedrawProjectRecursiveEvent(_projects.size())) ;
	}

	/**
	 * Roll all projects so that the one with ZOrder iWas becomes iBecome
	 * 
	 * @param iWas    Initial Zorder of project to get moved
	 * @param iBecome Target ZOrder for this project
	 */
	protected void rollZOrder(int iWas, int iBecome)
	{
		if (_projects.isEmpty())
			return ;
		
		int iSize  = _projects.size() ;
		
		if ((iWas < 1) || (iWas > iSize) || (iBecome < 1) || (iBecome > iSize))
			return ;
		
		int iDelta = iBecome - iWas ;
		 
		for (Iterator<LdvProjectWindowPresenter> iter = _projects.iterator() ; iter.hasNext() ; )
		{
			LdvProjectWindowPresenter project = iter.next() ;
			
			int iNextZOrder = project.getZOrder() + iDelta ;
			
			if      (iNextZOrder < 1)
				iNextZOrder += iSize ;
			else if (iNextZOrder > iSize)
				iNextZOrder -= iSize ;
			
			project.setZOrder(iNextZOrder) ;
		}
	}
	
	/**
	 * Move a block of projects one step down<br>
	 * Make sure that iFrom <= iTo
	 * 
	 * @param iFrom lower Zorder to be processed
	 * @param iTo higher Zorder to be processed
	 */
	private void moveDownProjects(int iFrom, int iTo) {
		moveProjects(iFrom, iTo, 1) ;
	}
	
	/**
	 * Move a block of projects one step up
	 * * Make sure that iFrom <= iTo
	 * 
	 * @param iFrom lower Zorder to be processed
	 * @param iTo higher Zorder to be processed
	 */
	private void moveUpProjects(int iFrom, int iTo) {
		moveProjects(iFrom, iTo, -1) ;
	}
	
	/**
	 * Move a block of projects
	 * * Make sure that iFrom <= iTo
	 * 
	 * @param iFrom lower Zorder to be processed
	 * @param iTo higher Zorder to be processed
	 * @param iStep number of steps to move (can be positive or negative)
	 */
	private void moveProjects(int iFrom, int iTo, int iStep)
	{
		if (_projects.isEmpty())
			return ;
		
		for (Iterator<LdvProjectWindowPresenter> iter = _projects.iterator() ; iter.hasNext() ; )
		{
			LdvProjectWindowPresenter project = iter.next() ;
			
			int iCurrentZOrder = project.getZOrder() ;
			if ((iCurrentZOrder >= iFrom) && (iCurrentZOrder <= iTo))
				project.setZOrder(iCurrentZOrder + iStep) ;
		}
	}
	
	public int getProjectsCount() {
		return _projects.size() ;
	}

	/**
	 * Returns the position of a date from the left side of the TimeControlledArea window in pixel
	 * <br> 
	 * <br> 
	 * 
	 * @param ldvTime Date to get left position of inside window
	 * @return Pixels count as int (returns TimeControlledArea + 1 for a "noLimit" date)
	 */
	public int getInternalPhysicalPosition(LdvTime ldvTime)
	{			
		int iAreaWidth = display.getAreaWidth() ;
		
		if (ldvTime.isNoLimit())
			// return Integer.MAX_VALUE ;
			return iAreaWidth + 1 ;
		
		long lDeltaPix = _ldvTimeController.getPhysicalDistance(_topRightTime, ldvTime) ;
		
		return iAreaWidth + (int) lDeltaPix ;
		
		// int elementOffsetLeft = _ldvTimeController.getDisplayOffsetLeft() ;
		// return Math.min(_ldvTimeController.getInternalPhysicalPosition(ldvTime) + elementOffsetLeft, iAreaWidth + 1) ;		
	}
	
	/**
	 * Ask Time Controller to refresh
	 * 
	 */
	// public void refresh(LdvTime topRightTime, LdvTimeZoomLevel currentZoomLevel)
	public void refreshTimeController()
	{	
		_ldvTimeController.refresh(_topRightTime, _currentZoomLevel) ;
	}
	
	/**
	 * Send a graph to the server for save/update 
	 *  
	 * @param modifiedGraph Graph to be sent to the server
	 * @param sProjectURI   ID of project this information is to be connected to
	 */
	public void saveGraph(final LdvModelGraph modifiedGraph, final String sProjectURI)
	{
		String sLdvId = _supervisor.getDisplayedPerson().getLdvId() ;
  	String sToken = _supervisor.getSessionToken() ;
  	
  	_updateProcessedGraph = new LdvModelGraph(modifiedGraph) ;
  	
  	_dispatcher.execute(new UpdateGraphAction(sLdvId, sLdvId, sToken, _updateProcessedGraph, sProjectURI), new SaveGraphCallback()) ;
	}
	
	/**
	 * Callback object used when graph comes back from server
	 *
	 */
	public class SaveGraphCallback implements AsyncCallback<UpdateGraphResult> 
	{
		public SaveGraphCallback() {
			super() ;
		}

		@Override
		public void onFailure(final Throwable cause)
		{
			Log.error("Handle Failure:", cause) ;
    				
			// Window.alert(SERVER_ERROR) ;
		}

		@Override
		public void onSuccess(final UpdateGraphResult result)
		{
			// take the result from the server and notify client interested components
			if (true == result.wasSuccessful())
			{
				_supervisor.injectModifiedGraph(_updateProcessedGraph, result.getMappings()) ;
				_updateProcessedGraph = null ;
				
				// Refresh objects
				//
				redrawProjectsWindows() ;
			}
		}
	}
	
	public int getProjectsAreaHeight() {
		return _ldvTimeController.getDisplayOffsetTop() ;		
	}
	
	public int getProjectsAreaBottom() {
		return _ldvTimeController.getDisplayOffsetHeight() + 1 ;		
	}
	
	public LdvTimeControllerPresenter getTimeController() {
		return _ldvTimeController ;
	}
	
	public LdvScrollArea getScrollArea() {
		return _ldvTimeController.getScrollArea() ;
	}
	
	public LdvGraphManager getGraphManager() {
		return _graphManager ;
	}
	
	public FocusPanel getThumb(){
		return getScrollArea().getThumb() ;
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
