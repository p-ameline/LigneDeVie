package com.ldv.client.mvp_toons;

import com.allen_sauer.gwt.log.client.Log;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.inject.Inject;

import com.ldv.client.event.LdvTimeContextReadyEvent;
import com.ldv.client.event.LdvTimeContextReadyEventHandler;
import com.ldv.client.event.LdvTimeContextRedrawEvent;
import com.ldv.client.event.LdvTimeContextSentEvent;
import com.ldv.client.event.LdvTimeDisplayReadyEvent;
import com.ldv.client.event.LdvTimeDisplaySentEvent;
import com.ldv.client.event.LdvTimeDisplaySentEventHandler;
import com.ldv.client.event.LdvTimeStepsReadyEvent;
import com.ldv.client.event.LdvTimeStepsReadyEventHandler;
import com.ldv.client.event.LdvTimeStepsRedrawEvent;
import com.ldv.client.event.LdvTimeStepsSentEvent;
import com.ldv.client.util.LdvTimeZoomLevel;
import com.ldv.client.util.LdvTimeZoomLevel.pixUnit;
import com.ldv.shared.model.LdvTime;

import net.customware.gwt.presenter.client.EventBus;
import net.customware.gwt.presenter.client.widget.WidgetDisplay;
import net.customware.gwt.presenter.client.widget.WidgetPresenter;

/**
 * LdvTimeDisplayPresenter is in charge of drawing the time scale :
 * <ul>
 * <li>Time steps are colored boxes</li>
 * <li>Time contexts is higher scope information</li>
 * </ul>   
 */
public class LdvTimeDisplayPresenter extends WidgetPresenter<LdvTimeDisplayPresenter.Display>
{
	private LdvTime          _topRightTime     = new LdvTime(0) ;
	private LdvTimeZoomLevel _currentZoomLevel = new LdvTimeZoomLevel() ;
	
	private ScheduledCommand _pendingEvents    = null ;
	private ScheduledCommand _pendingEvents2   = null ;
	
	private boolean          _bStepsComponentReady ;
	private boolean          _bContextComponentReady ;
	
	private LdvTimeStepsPresenter   _timeStepController    = null ;
	private LdvTimeContextPresenter _timeContextController = null ;
	
	public interface Display extends WidgetDisplay 
	{
		public int getOffsetWidth() ;
	}

	@Inject
	public LdvTimeDisplayPresenter(final Display display, EventBus eventBus) 
	{	
		super(display, eventBus);
		
		Log.info("entering constructor of LdvTimeDisplayPresenter.") ;
		
		_bStepsComponentReady   = false ;
		_bContextComponentReady = false ;
		
		bind() ;
	}
	  
	@Override
	protected void onBind() 
	{
		/**
		 * Event received when it is time to start
		 */
		eventBus.addHandler(LdvTimeDisplaySentEvent.TYPE, new LdvTimeDisplaySentEventHandler() 
		{
			@Override
			public void onTimeDisplaySend(LdvTimeDisplaySentEvent event) 
			{
				Log.info("Handling LdvTimeDisplaySentEvent event") ;
				bootstrap(event) ;
			}
		});
		
		/**
		 * Event received when Time Steps component is ready to work
		 */
		eventBus.addHandler(LdvTimeStepsReadyEvent.TYPE, new LdvTimeStepsReadyEventHandler() 
		{
			@Override
			public void onTimeStepsReady(LdvTimeStepsReadyEvent event) 
			{
				Log.info("Handling LdvTimeStepsReadyEvent event") ;
				timeStepsReady(event) ;
			}
		});
		
		/**
		 * Event received when Time Context component is ready to work
		 */
		eventBus.addHandler(LdvTimeContextReadyEvent.TYPE, new LdvTimeContextReadyEventHandler() 
		{
			@Override
			public void onTimeContextReady(LdvTimeContextReadyEvent event) 
			{
				Log.info("Handling LdvTimeContextReadyEvent event") ;
				timeContextReady(event) ;
			}
		});
	}
	
	/**
	 * Initialize from a bootstrap event 
	 */
	private void bootstrap(final LdvTimeDisplaySentEvent event)
	{
		if (this != event.getTarget())
			return ;
		
		_topRightTime.initFromLdvTime(event.getTopRightTime()) ;
		_currentZoomLevel.initFromLdvTimeZoomLevel(event.getCurrentZoomLevel()) ;
		
		// event.getWorkspace().clear() ;
		FlowPanel WelcomeWorkspace = event.getWorkspace() ;
		WelcomeWorkspace.add(display.asWidget()) ;
		
		addComponents() ;
	}
	
	/**
	 * Create Time Steps and Time Context component 
	 * 
	 */
	private void addComponents()
	{
		createSteps() ;
		createContext() ;
	}
	
	/**
	 * Create Time Steps component 
	 * 
	 */
	private void createSteps()
	{
		LdvTimeStepsView timeStepsView = new LdvTimeStepsView() ;
		_timeStepController = new LdvTimeStepsPresenter(timeStepsView, eventBus) ;
			
		final LdvTimeDisplayPresenter displayPresenter = this ;
		
		// Send time steps a startup message
		// 
		// If LdvTimeStepsSentEvent is not handled yet, we have to defer fireEvent
		//
		if (false == eventBus.isEventHandled(LdvTimeStepsSentEvent.TYPE))
		{
			if (null == _pendingEvents) 
			{
				_pendingEvents = new ScheduledCommand() 
				{
					public void execute() {
						_pendingEvents = null ;
						eventBus.fireEvent(new LdvTimeStepsSentEvent(displayPresenter, _timeStepController)) ;
		       }
		     };
		     Scheduler.get().scheduleDeferred(_pendingEvents) ;
			}
		}
		else
			eventBus.fireEvent(new LdvTimeStepsSentEvent(displayPresenter, _timeStepController)) ;
	}
	
	/**
	 * Create Time Context component 
	 * 
	 */
	private void createContext()
	{
		LdvTimeContextView timeContextView = new LdvTimeContextView() ;
		_timeContextController = new LdvTimeContextPresenter(timeContextView, eventBus) ;
		
		final LdvTimeDisplayPresenter displayPresenter = this ;
		
		// Send time context a bootstrap message
		// 
		// If LdvTimeContextSentEvent is not handled yet, we have to defer fireEvent
		//
		if (false == eventBus.isEventHandled(LdvTimeContextSentEvent.TYPE))
		{
			if (null == _pendingEvents2) 
			{
				_pendingEvents2 = new ScheduledCommand() 
				{
					public void execute() {
						_pendingEvents2 = null ;
						eventBus.fireEvent(new LdvTimeContextSentEvent(displayPresenter, _timeContextController)) ;
		       }
		     };
		     Scheduler.get().scheduleDeferred(_pendingEvents2) ;
			}
		}
		else
			eventBus.fireEvent(new LdvTimeContextSentEvent(displayPresenter, _timeContextController)) ;	
	}
	
	/**
	 * The steps display component is ready
	 */
	protected void timeStepsReady(LdvTimeStepsReadyEvent event)
	{
		if (this != event.getTarget())
			return ;
		
		_bStepsComponentReady = true ;
		aComponentIsReady() ;
	}
	
	/**
	 * The steps display component is ready
	 */
	protected void timeContextReady(LdvTimeContextReadyEvent event)
	{
		if (this != event.getTarget())
			return ;
		
		_bContextComponentReady = true ;
		aComponentIsReady() ;
	}
	
	/**
	 * Tells father components that TimeDisplay is ready to work 
	 * 
	 */
	private void aComponentIsReady()
	{
		if (_bStepsComponentReady && _bContextComponentReady)
			eventBus.fireEvent(new LdvTimeDisplayReadyEvent()) ;
	}
	
	public void setTopRightTime(LdvTime topRightTime) {
		_topRightTime.initFromLdvTime(topRightTime) ;
	}
	
	public void setCurrentZoomLevel(LdvTimeZoomLevel currentZoomLevel){
		_currentZoomLevel.initFromLdvTimeZoomLevel(currentZoomLevel) ;
	}
	
	/**
	 * Get the number of pixels corresponding to a number of time units (int)
	 * <br>
	 * <br>If UnitPerPixel == 0 both are equal
	 * 
	 * @param iTimeUnitCount Number of time units as int (i.e. hours, days... depending to zoom level)
	 * @return Number of corresponding pixels as int
	 */
	public int getPhysicalWidthFromTimeUnit(int iTimeUnitCount) { 
		return (_currentZoomLevel.getUppRate() == 0) ? iTimeUnitCount : iTimeUnitCount * (_currentZoomLevel.getUpperLimit() - _currentZoomLevel.getUppRate()) / _currentZoomLevel.getUpperLimit() ; 
	}
	
	/**
	 * Get the number of pixels corresponding to a number of time units (long)
	 * 
	 * If UnitPerPixel == 0 both are equal
	 * 
	 * @param iTimeUnitCount Number of time units as long (i.e. hours, days... depending to zoom level)
	 * @return Number of corresponding pixels as long
	 */
	public long getLongPhysicalWidthFromTimeUnit(long iTimeUnitCount) { 
		return (_currentZoomLevel.getUppRate() == 0) ? iTimeUnitCount : iTimeUnitCount * (_currentZoomLevel.getUpperLimit() - _currentZoomLevel.getUppRate()) / _currentZoomLevel.getUpperLimit() ; 
	}
	
	/**
	 * Get the number of time units corresponding to a number of pixels
	 * 
	 * If UnitPerPixel == 0 both are equal
	 * 
	 * @param iPhysicalWidth Number of pixels as int
	 * @return Number of corresponding time units as int (i.e. hours, days... depending to zoom level)
	 */
	public int getTimeUnitFromPhysicalWidth(int iPhysicalWidth) { 
		return (_currentZoomLevel.getUppRate() == 0) ? iPhysicalWidth : iPhysicalWidth * _currentZoomLevel.getUpperLimit() / (_currentZoomLevel.getUpperLimit() - _currentZoomLevel.getUppRate()) ; 
	}
	
	/**
	 * Get the number of time units corresponding to a number of pixels
	 * 
	 * If UnitPerPixel == 0 both are equal
	 * 
	 * @param iPhysicalWidth Number of pixels as long
	 * @return Number of corresponding time units as long (i.e. hours, days... depending to zoom level)
	 */
	public long getLongTimeUnitFromPhysicalWidth(long iPhysicalWidth) { 
		return (_currentZoomLevel.getUppRate() == 0) ? iPhysicalWidth : iPhysicalWidth * _currentZoomLevel.getUpperLimit() / (_currentZoomLevel.getUpperLimit() - _currentZoomLevel.getUppRate()) ; 
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
	public long getPhysicalDistance(final LdvTime ldvRefTime, final LdvTime ldvOtherTime)
	{
		if (ldvRefTime == ldvOtherTime)
			return (long) 0 ;
		
		long lTuDistance = getTimeUnitDistance(ldvRefTime, ldvOtherTime) ;
		
		return getLongPhysicalWidthFromTimeUnit(lTuDistance) ;
	}
	
	/**
	 * Get the difference in time units between 2 dates
	 * <br>
	 * <br>The result is positive if ldvOtherTime > ldvRefTime and negative if not
	 * 
	 * @param ldvRefTime   Reference date as LdvTime object
	 * @param ldvOtherTime Date to be evaluated as LdvTime object
	 * 
	 * @return Difference in time units (as long)
	 */
	public long getTimeUnitDistance(final LdvTime ldvRefTime, final LdvTime ldvOtherTime)
	{
		if (ldvRefTime == ldvOtherTime)
			return (long) 0 ;
		
		//
		// Differences for each column.
		//
		// For optimization reasons, we process UTC date only once.
		//
		String sOtherDateTime = ldvOtherTime.getUTCFullDateTime() ;
		String sRefDateTime   = ldvRefTime.getUTCFullDateTime() ;
		
		if (sOtherDateTime.equals(sRefDateTime))
			return (long) 0 ;
		
		int iDeltaYear  = LdvTime.getYY(sOtherDateTime) - LdvTime.getYY(sRefDateTime) ;
		int iDeltaMonth = LdvTime.getMM(sOtherDateTime) - LdvTime.getMM(sRefDateTime) ;

		// One pixel per year, return years difference rounded by 6 months
		//
		// Example : 201701 - 201610 -> iDeltaYear =  1 and iDeltaMonth = -9 -> iDelta = 0
		//           201610 - 201701 -> iDeltaYear = -1 and iDeltaMonth =  9 -> iDelta = 0
		//
		// Example : 201710 - 201602 -> iDeltaYear =  1 and iDeltaMonth =  8 -> iDelta =  2
		//           201602 - 201710 -> iDeltaYear = -1 and iDeltaMonth = -8 -> iDelta = -2
		//
		if (_currentZoomLevel.getPixUnit() == pixUnit.pixYear)
		{
			int iDelta = iDeltaYear ;
			if      (iDeltaMonth > 6)
				iDelta++ ;
			else if (iDeltaMonth < -6)
				iDelta-- ;
			return iDelta ;
		}

		int iDeltaDays = LdvTime.getDD(sOtherDateTime) - LdvTime.getDD(sRefDateTime) ;

		// One pixel per month, return months difference rounded by 15 days
		//
		if (_currentZoomLevel.getPixUnit() == pixUnit.pixMonth)
		{
			int iDelta = iDeltaYear * 12 + iDeltaMonth ;
			if      (iDeltaDays > 15)
				iDelta++ ;
			else if (iDeltaDays < -15)
				iDelta-- ;
			return iDelta ;
		}

		//
		// At least a daily precision
		//
		// One pixel per week
		//
		long lDeltaJ = ldvOtherTime.deltaDaysUTC(ldvRefTime) ;
	        
		if (_currentZoomLevel.getPixUnit() == pixUnit.pixWeek)
		{
			double dDelta = (double) lDeltaJ / 7 ;
			double dFloor = Math.floor(dDelta) ;
			if ((dDelta - dFloor) > 0.5)
				dFloor++ ;
			return (long) dFloor ;
		}

		int iDeltaHours = LdvTime.getHr(sOtherDateTime) - LdvTime.getHr(sRefDateTime) ;

		// One pixel per day, return days difference rounded by 12 hours
		//
		if (_currentZoomLevel.getPixUnit() == pixUnit.pixDay)
		{
			if (iDeltaHours > 12)
				return(lDeltaJ++) ;
			if (iDeltaHours < -12)
				return(lDeltaJ--) ;
			return(lDeltaJ) ;
		}

		int iDeltaMinutes = LdvTime.getMn(sOtherDateTime) - LdvTime.getMn(sRefDateTime) ;

		long lDeltaH = (lDeltaJ * 24) + iDeltaHours ;

		// One pixel per hour, return hours difference rounded by 30 minutes
		//
		if (_currentZoomLevel.getPixUnit() == pixUnit.pixHour)
		{
			if (iDeltaMinutes > 30)
				return(lDeltaH++) ;
			if (iDeltaMinutes < -30)
				return(lDeltaH--) ;
			return(lDeltaH) ;
		}

		int iDeltaSeconds = LdvTime.getSe(sOtherDateTime) - LdvTime.getSe(sRefDateTime) ;

		long lDeltaM = (lDeltaH * 60) + iDeltaMinutes ;

		// One pixel per minute, return minutes difference rounded by 30 seconds
		//
		if (_currentZoomLevel.getPixUnit() == pixUnit.pixMinute)
		{
			if (iDeltaSeconds > 30)
				return(lDeltaM++) ;
			if (iDeltaSeconds < -30)
				return(lDeltaM--) ;
			return(lDeltaM) ;
		}

		long lDeltaS = (lDeltaM * 60) + iDeltaSeconds ;

		if (_currentZoomLevel.getPixUnit() == pixUnit.pixSecond)
			return(lDeltaS) ;

		return 0 ;
	}
	
	/**
	 * Get the position of a date, in pixel, relative to upper right corner
	 * 
	 * The result is positive if ldvTime > _topRightTime and negative elsewhere
	 * 
	 * Contrary to getPhysicalDistance, this function uses the compression factor (getUppRate)
	 */
	public int getRelativePhysicalPosition(final LdvTime ldvTime)
	{	
		if (ldvTime.isNoLimit())
			return Integer.MAX_VALUE ;
		
		long lDeltaPix = getPhysicalDistance(_topRightTime, ldvTime) ;
		
		return (int) lDeltaPix ;
	}
	
	/**
	 * Get the left position of a date, in pixel, inside the Element 
	 * 
	 * If the date is inside the Element, the value should range between 0 and offsetWidth
	 */
	public int getInternalPhysicalPosition(final LdvTime ldvTime)
	{			
		if (ldvTime.isNoLimit())
			return Integer.MAX_VALUE ;
		
		int elementWidth = display.getOffsetWidth() ;
		int iRelativePos = getRelativePhysicalPosition(ldvTime) ;

		return elementWidth + iRelativePos ;		
	}
	
	public LdvTime getTopRightTime() {         
		return _topRightTime ;
	}
	
	public LdvTimeZoomLevel getCurrentZoomLevel() {
		return _currentZoomLevel ;
	}
	
	public int getHourTimeZone() {
		return _topRightTime.getHourTimeZone() ;
	}
	
	public void drawTimeElements()
	{
		Log.info("Calling drawTimeElements") ;
		
		LdvTime leftLimit = new LdvTime(_topRightTime.getHourTimeZone()) ;
		leftLimit.initFromLdvTime(_topRightTime) ;
		int iDisplayWidth = display.getOffsetWidth() ;
		
		switch(_currentZoomLevel.getPixUnit())
		{
			case pixMonth :
				leftLimit.addMonths(-iDisplayWidth, true) ;
				break ;
			case pixWeek :
				leftLimit.addDays(-iDisplayWidth * 7, true) ;
				break ;
			case pixDay :
				leftLimit.addDays(-iDisplayWidth, true) ;
				break ;
			case pixHour :
				leftLimit.addHours(-iDisplayWidth, true) ;
				break ;
			case pixMinute :
				leftLimit.addMinutes(-iDisplayWidth, true) ;
				break ;
			case pixSecond :
				leftLimit.addSeconds(-iDisplayWidth, true) ;
				break ;
			default:
				return ;
		}
		
		eventBus.fireEvent(new LdvTimeContextRedrawEvent(leftLimit, _timeContextController)) ;
		eventBus.fireEvent(new LdvTimeStepsRedrawEvent(leftLimit, _timeStepController)) ;
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
