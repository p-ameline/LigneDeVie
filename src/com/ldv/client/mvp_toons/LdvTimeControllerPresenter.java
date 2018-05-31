package com.ldv.client.mvp_toons;

import com.allen_sauer.gwt.log.client.Log;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.inject.Inject;

import com.ldv.client.canvas.LdvScrollArea;
import com.ldv.client.event.LdvTimeControllerReadyEvent;
import com.ldv.client.event.LdvTimeControllerSentEvent;
import com.ldv.client.event.LdvTimeControllerSentEventHandler;
import com.ldv.client.event.LdvTimeDisplayReadyEvent;
import com.ldv.client.event.LdvTimeDisplayReadyEventHandler;
import com.ldv.client.event.LdvTimeDisplaySentEvent;
import com.ldv.client.event.LdvTimeStepsRedrawEvent;
import com.ldv.client.event.LdvTimeStepsRedrawEventHandler;
import com.ldv.client.util.LdvSupervisor;
import com.ldv.client.util.LdvTimeZoomLevel;
import com.ldv.shared.model.LdvTime;

import net.customware.gwt.presenter.client.EventBus;
import net.customware.gwt.presenter.client.widget.WidgetDisplay;
import net.customware.gwt.presenter.client.widget.WidgetPresenter;

/**
 * LdvTimeControllerPresenter controls all time related widgets :
 * <ul>
 * <li>LdvTimeDisplayPresenter for drawing time scale</li>
 * <li>LdvScrollBar for scrolling over time</li>
 * </ul>
 */
public class LdvTimeControllerPresenter extends WidgetPresenter<LdvTimeControllerPresenter.Display>
{
	private LdvTime                 _topRightTime ;
	private LdvTimeZoomLevel        _currentZoomLevel ;
	
	private LdvTimeDisplayPresenter _TimeDisplay ;
	
	private ScheduledCommand        _pendingEvents  = null ;
	private ScheduledCommand        _pendingEvent2  = null ;
	
	public interface Display extends WidgetDisplay 
	{
		public int           getDisplayAbsoluteLeft() ;
		public int           getOffsetLeft() ;
		public int           getOffsetHeight() ;
		public int           getOffsetTop() ;
		public LdvScrollArea getScrollArea() ;
		public FlowPanel     getWorkspace() ;
		public void          setScrollButtons(Button right, Button left) ;
	}
	
	private final LdvSupervisor _supervisor ;

	@Inject
	public LdvTimeControllerPresenter(final Display display, EventBus eventBus, final LdvSupervisor supervisor) 
	{	
		super(display, eventBus) ;
		
		Log.info("LdvTimeControllerPresenter: entering constructor.");
		
		_supervisor = supervisor ;
		
		bind() ;
	}
	  
	@Override
	protected void onBind() 
	{			
		/**
		 * Event received when it is time to start
		 */
		eventBus.addHandler(LdvTimeControllerSentEvent.TYPE, new LdvTimeControllerSentEventHandler() 
		{
			@Override
			public void onTimeControllerSend(LdvTimeControllerSentEvent event) 
			{
				Log.info("LdvTimeControllerPresenter: Handling LdvTimeControllerSentEvent event") ;
				_topRightTime     = event.getTopRightTime() ;
				_currentZoomLevel = event.getCurrentZoomLevel() ;
				
				AbsolutePanel WelcomeWorkspace = event.getWorkspace() ;
				WelcomeWorkspace.add(display.asWidget()) ;
				
				display.setScrollButtons(event.getRightScrollButton(), event.getLeftScrollButton()) ;
				
				addComponents() ;
			}
		});
		
		/**
		 * Event received when time display components are ready to work
		 */
		eventBus.addHandler(LdvTimeDisplayReadyEvent.TYPE, new LdvTimeDisplayReadyEventHandler() 
		{
			@Override
			public void onTimeDisplayReady(LdvTimeDisplayReadyEvent event) 
			{
				Log.info("LdvTimeControllerPresenter: Handling LdvTimeDisplayReadyEvent event");
				signalThatReady() ;
			}
		});
		
		eventBus.addHandler(LdvTimeStepsRedrawEvent.TYPE, new LdvTimeStepsRedrawEventHandler() 
		{
			@Override
			public void onRedrawTimeSteps(LdvTimeStepsRedrawEvent event) 
			{
				// Log.info("Handling LdvTimeStepsRedrawEvent event");
				// connectToProject(event) ;
			}
		});
	}
	
	/**
	 * Initialize from a bootstrap event 
	 */
	private void bootstrap(LdvTimeControllerSentEvent event)
	{
		if (this != event.getTarget())
			return ;
	}
	
	private void addComponents()
	{
		// Create time display component
		//
		LdvTimeDisplayView timeDisplayView = new LdvTimeDisplayView() ;
		_TimeDisplay = new LdvTimeDisplayPresenter(timeDisplayView, eventBus) ;
			
		// Send time display a startup message
		// 
		// If LdvTimeDisplaySentEvent is not handled yet, we have to defer fireEvent
		//
		if (false == eventBus.isEventHandled(LdvTimeDisplaySentEvent.TYPE))
		{
			if (null == _pendingEvents) 
			{
				_pendingEvents = new ScheduledCommand() 
				{
					public void execute() {
						_pendingEvents = null ;
						eventBus.fireEvent(new LdvTimeDisplaySentEvent(display.getWorkspace(), _topRightTime, _currentZoomLevel, _TimeDisplay)) ;
		       }
		     };
		     Scheduler.get().scheduleDeferred(_pendingEvents) ;
			}
		}
		else
			eventBus.fireEvent(new LdvTimeDisplaySentEvent(display.getWorkspace(), _topRightTime, _currentZoomLevel, _TimeDisplay)) ;	
	}
	
	private void signalThatReady()
	{
		// Send time display a startup message
		// 
		// If LdvTimeDisplaySentEvent is not handled yet, we have to defer fireEvent
		//
		if (false == eventBus.isEventHandled(LdvTimeControllerReadyEvent.TYPE))
		{
			if (null == _pendingEvent2) 
			{
				_pendingEvent2 = new ScheduledCommand() 
				{
					public void execute() {
						_pendingEvent2 = null ;
						eventBus.fireEvent(new LdvTimeControllerReadyEvent()) ;
		       }
		     };
		     Scheduler.get().scheduleDeferred(_pendingEvent2) ;
			}
		}
		else
			eventBus.fireEvent(new LdvTimeControllerReadyEvent()) ;	
	}
	
	public void refresh(LdvTime topRightTime, LdvTimeZoomLevel currentZoomLevel)
	{	
		_TimeDisplay.setTopRightTime(topRightTime) ;
		_TimeDisplay.setCurrentZoomLevel(currentZoomLevel) ;
		_TimeDisplay.drawTimeElements() ;
	}

	public void setTopRightTime(LdvTime topRightTime) {
		_TimeDisplay.setTopRightTime(topRightTime) ;
	}
	
	public void setCurrentZoomLevel(LdvTimeZoomLevel currentZoomLevel) {
		_TimeDisplay.setCurrentZoomLevel(currentZoomLevel) ;
	}
	
	/**
	 * Get the number of pixels corresponding to a number of time units
	 * 
	 * <br>If UnitPerPixel == 0 both are equal
	 * 
	 * @param iTimeUnitCount Number of time units (i.e. hours, days... depending to zoom level)
	 * @return Number of pixels
	 */
	public int getPhysicalWidthFromTimeUnit(int iTimeUnitCount) 
	{ 
		return _TimeDisplay.getPhysicalWidthFromTimeUnit(iTimeUnitCount) ; 
	}
	
	/**
	 * Get the number of time units corresponding to a number of pixels
	 * <br>
	 * <br>If UnitPerPixel == 0 both are equal
	 * 
	 * @param iPhysicalWidth Number of pixels
	 * @return Number of time units (i.e. hours, days... depending to zoom level)
	 */
	public int getTimeUnitFromPhysicalWidth(int iPhysicalWidth)
	{ 
		return _TimeDisplay.getTimeUnitFromPhysicalWidth(iPhysicalWidth) ; 
	}
	
	/**
	 * Get the difference in pixels between 2 dates
	 * <br>
	 * <br>The result is positive if ldvOtherTime > ldvRefTime and negative if not
	 * 
	 * @param ldvRefTime   Reference date as LdvTime object
	 * @param ldvOtherTime Date to be evaluated as LdvTime object
	 * 
	 * @return Difference in pixels (as long)
	 */
	public long getPhysicalDistance(LdvTime ldvRefTime, LdvTime ldvOtherTime)
	{
		return _TimeDisplay.getPhysicalDistance(ldvRefTime, ldvOtherTime) ;
	}
	
	/**
	 * Get the position of a date, in pixel, relative to upper right corner
	 * 
	 * The result is positive if ldvTime > _topRightTime and negative elsewhere
	 * 
	 * Contrary to getPhysicalDistance, this function uses the compression factor (getUppRate)
	 */
	public long getRelativePhysicalPosition(LdvTime ldvTime) {	
		return _TimeDisplay.getRelativePhysicalPosition(ldvTime) ;
	}
	
	/**
	 * Get the left position of a date, in pixel, inside the Element 
	 * 
	 * If the date is inside the Element, the value should range between 0 and offsetWidth
	 */
	public int getInternalPhysicalPosition(LdvTime ldvTime) {
		return _TimeDisplay.getInternalPhysicalPosition(ldvTime) ;		
	}
	
	public int getDisplayAbsoluteLeft() {
		return display.getDisplayAbsoluteLeft() ;
	}
	
	public int getDisplayOffsetLeft() {
		return display.getOffsetLeft() ;
	}

	public LdvScrollArea getScrollArea() {
		return display.getScrollArea() ;
	}
	
	public int getDisplayOffsetTop() {
		return display.getOffsetTop() ;		
	}
	
	public int getDisplayOffsetHeight() {
		return display.getOffsetHeight() ;		
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
