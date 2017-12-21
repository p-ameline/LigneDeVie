package com.ldv.client.mvp_toons;

import java.util.ArrayList;
import java.util.List;

import com.allen_sauer.gwt.log.client.Log;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.inject.Inject;

import com.ldv.client.canvas.LdvTimeStepBox;
import com.ldv.client.event.LdvTimeStepsReadyEvent;
import com.ldv.client.event.LdvTimeStepsRedrawEvent;
import com.ldv.client.event.LdvTimeStepsRedrawEventHandler;
import com.ldv.client.event.LdvTimeStepsSentEvent;
import com.ldv.client.event.LdvTimeStepsSentEventHandler;
import com.ldv.shared.model.LdvInt;
import com.ldv.shared.model.LdvTime;

import net.customware.gwt.presenter.client.EventBus;
import net.customware.gwt.presenter.client.widget.WidgetDisplay;
import net.customware.gwt.presenter.client.widget.WidgetPresenter;

public class LdvTimeStepsPresenter extends WidgetPresenter<LdvTimeStepsPresenter.Display>
{
	public interface Display extends WidgetDisplay 
	{
		public int  getLeftMargin() ;
		public int  getOffsetWidth() ;
		public void setBoxPosition(LdvTimeStepBox box, int iLeftPosition, int iTopPosition) ;
		public void addBox(LdvTimeStepBox box) ;
		public void showBox(LdvTimeStepBox box) ;
		public void hideBox(LdvTimeStepBox box) ;
	}

	/**
	 * Elements used to display steps marks
	 */
	private List<LdvTimeStepBox>    _stepBoxElements = new ArrayList<LdvTimeStepBox>() ;
	
	private LdvTimeDisplayPresenter _fatherTimeDisplay ;
	
	/**
	 * Current drawing objects
	 */
	private RedrawTimer _timer ;
	private int         _repeatDelay = 30 ;
	
	private LdvTime     _leftLimitTime ;
	private LdvTime     _blockRightTime ;
	private LdvTime     _blockLeftTime ;
	
	private boolean     _bIsDrawing ;
	private int         _iTotalStepsIndex ;
	
	private int         _iBlockSize = 10 ;
	private boolean     _bRed ;
	
	/**
   * The timer used to draw blocks of Steps in order to be interruptible
   */
  private class RedrawTimer extends Timer 
  {
    /**
     * Method called when timer fires
     */
    @Override
    public void run() 
    {
    	drawBlock() ;
    	if (_bIsDrawing)
    		schedule(_repeatDelay) ;
    }
  }
	
	@Inject
	public LdvTimeStepsPresenter(final Display display, EventBus eventBus) 
	{	
		super(display, eventBus) ;
		
		// Log.debug("entering constructor of LdvTimeStepsPresenter.") ;
		
		_leftLimitTime  = new LdvTime(0) ;
		_blockRightTime = new LdvTime(0) ;
		_blockLeftTime  = new LdvTime(0) ;
		
		bind() ;
		
		_timer = new RedrawTimer() ;
	}
	  
	@Override
	protected void onBind() 
	{
		/**
		 * Bootstrapping event received when it is time to start
		 */
		eventBus.addHandler(LdvTimeStepsSentEvent.TYPE, new LdvTimeStepsSentEventHandler() 
		{
			@Override
			public void onTimeStepsSend(LdvTimeStepsSentEvent event) 
			{
				// Log.debug("LdvTimeStepsPresenter Handling LdvTimeStepsSentEvent event") ;
				bootstrap(event) ;
			}
		});
		
		/**
		 * Event received when it is time to redraw
		 */
		eventBus.addHandler(LdvTimeStepsRedrawEvent.TYPE, new LdvTimeStepsRedrawEventHandler() 
		{
			@Override
			public void onRedrawTimeSteps(LdvTimeStepsRedrawEvent event) 
			{
				// Log.debug("LdvTimeStepsPresenter Handling LdvTimeStepsRedrawEvent event") ;
				askedToRedraw(event) ;
			}
		});
	}
	
	/**
	 * Initialize from a bootstrap event 
	 */
	private void bootstrap(LdvTimeStepsSentEvent event)
	{
		if (this != event.getTarget())
			return ;
		
		_fatherTimeDisplay = event.getFatherTimeDisplay() ;
		
		// Add display to father's display
		//
		AbsolutePanel TimeDisplayView = (AbsolutePanel) _fatherTimeDisplay.getDisplay().asWidget() ; 
		TimeDisplayView.add(getDisplay().asWidget()) ;
		
		// Signal father that the bootstrapping operation is done
		//
		eventBus.fireEvent(new LdvTimeStepsReadyEvent(_fatherTimeDisplay)) ;
	}
	
	/**
	 * Processing a "need to redraw" event
	 */
	private void askedToRedraw(LdvTimeStepsRedrawEvent event)
	{
		if (this != event.getTarget())
			return ;
		
		if (_bIsDrawing)
		{
			_timer.cancel() ;
			_bIsDrawing = false ;
		}
			
		draw(event.getLeftLimit()) ;
	}
	
	/**
	 * Draw (or redraw) Steps
	 * 
	 * @param leftLimit Lower limit for drawing Steps
	 */
	private void draw(LdvTime leftLimit)
	{
		if (null == leftLimit)
			return ;
		
		// Init drawing objects
		//
		_leftLimitTime.initFromLdvTime(leftLimit) ;
		_blockRightTime.initFromLdvTime(_fatherTimeDisplay.getTopRightTime()) ;
		
		// Log.info("LdvTimeStepsPresenter::draw: from " + _leftLimitTime.getUTCDateTime() + " (" + _leftLimitTime.getTimeZone() + ") to " + _blockRightTime.getUTCDateTime() + " (" + _blockRightTime.getTimeZone() + ")") ;
		
		_bIsDrawing       = true ;
		_bRed             = false ;
		_iTotalStepsIndex = 0 ;
		
		_timer.schedule(_repeatDelay) ;
		
		switch(_fatherTimeDisplay.getCurrentZoomLevel().getPixUnit())
		{
			case pixMonth :
				drawStepDecades() ;
				break ;
			case pixWeek :
				drawStepYears() ;
				break ;
			case pixDay :
				drawStepMonths() ;
				break ;
			case pixHour :
				drawStepDays() ;
				break ;
			case pixMinute :
				drawStepHours() ;
				break ;
			case pixSecond :
				drawStepMinutes() ;
				break ;
			default:
				return ;
		}
	}
	
	/**
	 * Draw a block of Steps
	 */
	private void drawBlock()
	{
		switch(_fatherTimeDisplay.getCurrentZoomLevel().getPixUnit())
		{
			case pixMonth :
				drawDecadesBlocks() ;
				break ;
			case pixWeek :
				drawYearsBlocks() ;
				break ;
			case pixDay :
				drawMonthsBlocks() ;
				break ;
			case pixHour :
				drawDaysBlocks() ;
				break ;
			case pixMinute :
				drawHoursBlocks() ;
				break ;
			case pixSecond :
				drawMinutesBlocks() ;
				break ;
			default:
				return ;
		}
		
		// If drawing is done...
		//
		if (false == _blockRightTime.isAfter(_leftLimitTime))
		{
			hideRemainingStepElements(_iTotalStepsIndex) ;
			_bIsDrawing = false ;
			_timer.cancel() ;
		}
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
		if ((null == ldvTime) || ldvTime.isNoLimit())
			return Integer.MAX_VALUE ;
		
		return _fatherTimeDisplay.getRelativePhysicalPosition(ldvTime) ;		
	}
	
	/**
	 * Get the left position of a date, in pixel, inside the Element 
	 * 
	 * If the date is inside the Element, the value should range between 0 and offsetWidth
	 */
	public int getInternalPhysicalPosition(final LdvTime ldvTime)
	{			
		if ((null == ldvTime) || ldvTime.isNoLimit())
			return Integer.MAX_VALUE ;
		
		int elementWidth = display.getOffsetWidth() ;
		int iRelativePos = getRelativePhysicalPosition(ldvTime) ;

		return elementWidth + iRelativePos ;		
	}
	
	/**
	 * Draw the alternating black and red numbered boxes that "pave" time
	 * 
	 * Take care that all boxes are not necessarily of equal width. For example, a month can be made of 28, 29, 30 or 31 days 
	 * 
	 * @param iStepIndex Step number (from right to left)
	 * @param left Time for step's left border
	 * @param left Time for step's right border
	 * @param sText Step's text
	 * @param bRed Is this step a red (or black) one
	 * 
	 */
	public void drawChronoStep(int iStepIndex, final LdvTime left, final LdvTime right, final String sText, boolean bRed)
	{
		int iLeftMargin = display.getLeftMargin() ;
		
		// position are converted from time to pixel
		//
		// Log.debug("LdvTimeStepsPresenter::drawChronoStep - converting from time positions to pixels") ;
		
		int iLeftPosition  = (int) getInternalPhysicalPosition(left) - iLeftMargin ;
		int iRightPosition = (int) getInternalPhysicalPosition(right) - iLeftMargin ;
	    
		if (iRightPosition <= iLeftPosition)
			return ;
	    
		int iBlockWidth = iRightPosition - iLeftPosition ;
	    
		// Create the box or make it visible	    
		LdvTimeStepBox box = null ;
		if (iStepIndex < _stepBoxElements.size()) 
		{
			box = _stepBoxElements.get(iStepIndex) ;
			box.showBox() ;
			
			if (box.getLeftPos() != iLeftPosition)
			{
				// Log.debug("LdvTimeStepsPresenter::drawChronoStep - set new position") ;
				display.setBoxPosition(box, iLeftPosition, 0) ;
			}
			
			// Log.debug("LdvTimeStepsPresenter::drawChronoStep - reinitialize") ;
			box.reInitialize(iLeftPosition, iBlockWidth, sText) ;
		} 
		else 
		{ // Create the new box
			
			// Log.debug("LdvTimeStepsPresenter::drawChronoStep - creating Div") ;
			
			box = new LdvTimeStepBox(iLeftPosition, iBlockWidth, sText) ;
			
			if (bRed)
				box.addStyleName("ldv-stepcolor-box-red") ;
			else
				box.addStyleName("ldv-stepcolor-box-black") ;
			
			display.addBox(box) ;
			display.setBoxPosition(box, iLeftPosition, 0) ;
			box.setWidthInPixel() ;
			box.setText() ;
			
			_stepBoxElements.add(box) ;
		}
	}
		
	/**
	 * Hiding steps that are no longer needed  
	 */
	public void hideRemainingStepElements(int iStepIndex)
	{
		if (_stepBoxElements.isEmpty())
			return ;
		
		// Log.debug("LdvTimeStepsPresenter::hideRemainingStepElements entering") ;
		
		for (int i = iStepIndex; i < _stepBoxElements.size(); i++)
			display.hideBox(_stepBoxElements.get(i)) ;
		
		// Log.debug("LdvTimeStepsPresenter::hideRemainingStepElements leaving") ;
	}
			
	/**
	 * Draw steps where each box is a year (12 months) 
	 */
	public void drawStepDecades()
	{
		// Log.debug("Entering LdvTimeStepsPresenter::drawStepDecades") ;		
		_blockLeftTime.initFromLdvTime(new LdvTime(_blockRightTime.getHourTimeZone(), _blockRightTime.getLocalFullYear(), 1, 1, 0, 0, 0)) ;
	}
		
	/**
	 * Draw Steps blocks where each box is a year (12 months) 
	 */
	public void drawDecadesBlocks()
	{
		// Log.info("Entering LdvTimeStepsPresenter::drawDecadesBlocks") ;
		
		int iStepIndex = 0 ;
		// while ((iStepIndex < _iBlockSize) && _blockRightTime.isAfter(_leftLimitTime))
		while (_blockRightTime.isAfter(_leftLimitTime))
		{
			LdvInt iYear = new LdvInt(_blockLeftTime.getLocalFullYear() % 10) ;
			String sText = iYear.intToString(1) ;

			drawChronoStep(_iTotalStepsIndex, _blockLeftTime, _blockRightTime, sText, _bRed) ;

			_bRed = !_bRed ;
			_blockRightTime.initFromLdvTime(_blockLeftTime) ;
			
			// Switch to previous year
	    //
			_blockLeftTime.addYears(-1, true) ;
			
			iStepIndex++ ;
			_iTotalStepsIndex++ ;
		}
		
		// Log.info("Leaving LdvTimeStepsPresenter::drawDecadesBlocks") ;
	}
		
	/**
	 * Draw steps where each box is a year (52 weeks) 
	 */
	public void drawStepYears()
	{
		_blockLeftTime.initFromLdvTime(new LdvTime(_blockRightTime.getHourTimeZone(), _blockRightTime.getLocalFullYear(), 1, 1, 0, 0, 0)) ;
	}
	
	/**
	 * Draw Steps blocks where each box is a year (52 weeks) 
	 */
	public void drawYearsBlocks()
	{
		// Log.info("Entering LdvTimeStepsPresenter::drawYearsBlocks") ;
		
		int iStepIndex = 0 ;
		// while ((iStepIndex < _iBlockSize) && _blockRightTime.isAfter(_leftLimitTime))
		while (_blockRightTime.isAfter(_leftLimitTime))
		{
			LdvInt iYear = new LdvInt(_blockLeftTime.getLocalFullYear()) ;
			String sText = iYear.intToString(4) ;

			drawChronoStep(_iTotalStepsIndex, _blockLeftTime, _blockRightTime, sText, _bRed) ;

			_bRed = !_bRed ;
			_blockRightTime.initFromLdvTime(_blockLeftTime) ;
			
			// Switch to previous year
	    //
			_blockLeftTime.addYears(-1, true) ;
			
			iStepIndex++ ;
			_iTotalStepsIndex++ ;
		}
		
		// Log.info("Leaving LdvTimeStepsPresenter::drawYearsBlocks") ;
	}
		
	/**
	 * Draw steps where each box is a month (28, 29, 30 or 31 days) 
	 */
	public void drawStepMonths()
	{
		_blockLeftTime.initFromLdvTime(new LdvTime(_blockRightTime.getHourTimeZone(), _blockRightTime.getLocalFullYear(), _blockRightTime.getLocalMonth(), 1, 0, 0, 0)) ;
	}
	
	/**
	 * Draw Steps blocks where each box is a month (28, 29, 30 or 31 days)
	 */
	public void drawMonthsBlocks()
	{
		// Log.info("Entering LdvTimeStepsPresenter::drawMonthsBlocks") ;
		
		int iStepIndex = 0 ;
		// while ((iStepIndex < _iBlockSize) && _blockRightTime.isAfter(_leftLimitTime))
		while (_blockRightTime.isAfter(_leftLimitTime))
		{
			LdvInt iMonth = new LdvInt(_blockLeftTime.getLocalMonth()) ;
			String sText = iMonth.intToString(2) ;

			drawChronoStep(_iTotalStepsIndex, _blockLeftTime, _blockRightTime, sText, _bRed) ;

			_bRed = !_bRed ;
			_blockRightTime.initFromLdvTime(_blockLeftTime) ;
			
			// Switch to previous month
	    //
			_blockLeftTime.addMonths(-1, true) ;
			
			iStepIndex++ ;
			_iTotalStepsIndex++ ;
		}		
	}
	
	/**
	 * Draw steps where each box is a day (24 hours) 
	 */
	public void drawStepDays()
	{
		_blockLeftTime.initFromLdvTime(new LdvTime(_blockRightTime.getHourTimeZone(), _blockRightTime.getLocalFullYear(), _blockRightTime.getLocalMonth(), _blockRightTime.getLocalDate(), 0, 0, 0)) ;
	}
	
	/**
	 * Draw Steps blocks where each box is a day (24 hours)
	 */
	public void drawDaysBlocks()
	{
		// Log.info("Entering LdvTimeStepsPresenter::drawDaysBlocks") ;
		
		int iStepIndex = 0 ;
		// while ((iStepIndex < _iBlockSize) && _blockRightTime.isAfter(_leftLimitTime))
		while (_blockRightTime.isAfter(_leftLimitTime))
		{
			LdvInt iDay = new LdvInt(_blockLeftTime.getLocalDate()) ;
			String sText = null ;
			if (iDay.getValue() < 10)
				sText = iDay.intToString(1) ;
			else
				sText = iDay.intToString(2) ;
			
			drawChronoStep(_iTotalStepsIndex, _blockLeftTime, _blockRightTime, sText, _bRed) ;

			_bRed = !_bRed ;
			_blockRightTime.initFromLdvTime(_blockLeftTime) ;
			
			// Switch to previous day
	    //
			_blockLeftTime.addDays(-1, true) ;
			
			iStepIndex++ ;
			_iTotalStepsIndex++ ;
		}
	}
		
	/**
	 * Draw steps where each box is an hour (60 minutes) 
	 */
	public void drawStepHours()
	{
		_blockLeftTime.initFromLdvTime(new LdvTime(_blockRightTime.getHourTimeZone(), _blockRightTime.getLocalFullYear(), _blockRightTime.getLocalMonth(), _blockRightTime.getLocalDate(), _blockRightTime.getLocalHours(), 0, 0)) ;
	}
	
	/**
	 * Draw Steps blocks where each box is an hour (60 minutes)
	 */
	public void drawHoursBlocks()
	{
		// Log.info("Entering LdvTimeStepsPresenter::drawHourssBlocks to draw between " + _leftLimitTime.getUTCDateTime() + " and " + _blockRightTime.getUTCDateTime()) ;
		
		int iStepIndex = 0 ;
		// while ((iStepIndex < _iBlockSize) && _blockRightTime.isAfter(_leftLimitTime))
		while (_blockRightTime.isAfter(_leftLimitTime))
		{
			LdvInt iHour = new LdvInt(_blockLeftTime.getLocalHours()) ;
			String sText = null ;
			if (iHour.getValue() < 10)
				sText = iHour.intToString(1) ;
			else
				sText = iHour.intToString(2) ;

			// Log.info("LdvTimeStepsPresenter::drawHourssBlocks, send drawChronoStep " + sText + " from " + _blockLeftTime.getLocalDateTime() + " (" + _blockLeftTime.getTimeZone() + ") to " + _blockRightTime.getLocalDateTime() + " (" + _blockRightTime.getTimeZone() + ")") ;
			
			drawChronoStep(iStepIndex, _blockLeftTime, _blockRightTime, sText, _bRed) ;

			_bRed = !_bRed ;
			
			_blockRightTime.initFromLdvTime(_blockLeftTime) ;
			
			// Log.info("blockRightTime= " + _blockRightTime.getLocalDateTime() + " (" + _blockRightTime.getTimeZone() + ")") ;
			
			// Switch to previous hour
	    //
			_blockLeftTime.addHours(-1, true) ;
			
			// Log.info("Next: from " + _blockLeftTime.getLocalDateTime() + " (" + _blockLeftTime.getTimeZone() + ") to " + _blockRightTime.getLocalDateTime() + " (" + _blockRightTime.getTimeZone() + ")") ;
			
			iStepIndex++ ;
			_iTotalStepsIndex++ ;
		}
		
		// Log.info("LdvTimeStepsPresenter::drawHourssBlocks, " + _blockRightTime.getLocalDateTime() + " (" + _blockRightTime.getTimeZone() + ") is no longer after " + _leftLimitTime.getLocalDateTime()  + " (" + _leftLimitTime.getTimeZone() + ")") ;
	}

	/**
	 * Draw steps where each box is a minute (60 seconds) 
	 */
	public void drawStepMinutes()
	{
		_blockLeftTime.initFromLdvTime(new LdvTime(_blockRightTime.getHourTimeZone(), _blockRightTime.getLocalFullYear(), _blockRightTime.getLocalMonth(), _blockRightTime.getLocalDate(), _blockRightTime.getLocalHours(), _blockRightTime.getLocalMinutes(), 0)) ;
	}		
	
	/**
	 * Draw Steps blocks where each box is a minute (60 seconds)
	 */
	public void drawMinutesBlocks()
	{
		int iStepIndex = 0 ;
		// while ((iStepIndex < _iBlockSize) && _blockRightTime.isAfter(_leftLimitTime))
		while (_blockRightTime.isAfter(_leftLimitTime))
		{
			LdvInt iMinute = new LdvInt(_blockLeftTime.getLocalMinutes()) ;
			String sText = null ;
			if (iMinute.getValue() < 10)
				sText = iMinute.intToString(1) ;
			else
				sText = iMinute.intToString(2) ;

			drawChronoStep(iStepIndex, _blockLeftTime, _blockRightTime, sText, _bRed) ;

			_bRed = !_bRed ;
			_blockRightTime.initFromLdvTime(_blockLeftTime) ;
			
			// Switch to previous minute
	    //
			_blockLeftTime.addMinutes(-1, true) ;
			
			iStepIndex++ ;
			_iTotalStepsIndex++ ;
		}
	}
	
	public boolean IsDrawing() {
		return _bIsDrawing ;
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
