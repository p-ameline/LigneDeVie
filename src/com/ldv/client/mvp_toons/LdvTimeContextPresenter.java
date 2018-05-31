package com.ldv.client.mvp_toons;

import java.util.ArrayList;
import java.util.List;

import com.allen_sauer.gwt.log.client.Log;

import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.inject.Inject;

import com.ldv.client.canvas.LdvTimeContextBox;
import com.ldv.client.event.LdvTimeContextReadyEvent;
import com.ldv.client.event.LdvTimeContextRedrawEvent;
import com.ldv.client.event.LdvTimeContextRedrawEventHandler;
import com.ldv.client.event.LdvTimeContextSentEvent;
import com.ldv.client.event.LdvTimeContextSentEventHandler;
import com.ldv.shared.model.LdvInt;
import com.ldv.shared.model.LdvTime;

import net.customware.gwt.presenter.client.EventBus;
import net.customware.gwt.presenter.client.widget.WidgetDisplay;
import net.customware.gwt.presenter.client.widget.WidgetPresenter;

public class LdvTimeContextPresenter extends WidgetPresenter<LdvTimeContextPresenter.Display>
{
	public interface Display extends WidgetDisplay 
	{
		public int  getLeftMargin() ;
		public int  getOffsetWidth() ;
		public void setBoxPosition(LdvTimeContextBox box, int iLeftPosition, int iTopPosition) ;
		public void addBox(LdvTimeContextBox box) ;
		public void showBox(LdvTimeContextBox box) ;
		public void hideBox(LdvTimeContextBox box) ;
	}

	/**
	 * Elements used to display steps marks
	 */
	private List<LdvTimeContextBox> _contextBoxElements = new ArrayList<LdvTimeContextBox>() ;
	
	private LdvTimeDisplayPresenter __fatherTimeDisplay ;
	
	private boolean _bKeepOnDrawing ;
	private boolean _bIsDrawing ;
	
	@Inject
	public LdvTimeContextPresenter(final Display display, EventBus eventBus) 
	{	
		super(display, eventBus) ;
		
		Log.info("LdvTimeContextPresenter: entering constructor.") ;
		
		bind() ;
	}
	  
	@Override
	protected void onBind() 
	{
		/**
		 * Event received when it is time to start
		 */
		eventBus.addHandler(LdvTimeContextSentEvent.TYPE, new LdvTimeContextSentEventHandler() 
		{
			@Override
			public void onTimeContextSend(LdvTimeContextSentEvent event) 
			{
				Log.info("LdvTimeContextPresenter: Handling LdvTimeContextSentEvent event") ;
				bootstrap(event) ;
			}
		});
		
		/**
		 * Event received when it is time to redraw
		 */
		eventBus.addHandler(LdvTimeContextRedrawEvent.TYPE, new LdvTimeContextRedrawEventHandler() 
		{
			@Override
			public void onRedrawTimeContext(LdvTimeContextRedrawEvent event) 
			{
				Log.info("LdvTimeContextPresenter: Handling LdvTimeContextRedrawEvent event") ;
				askedToRedraw(event) ;
			}
		});
	}
	
	/**
	 * Initialize from a bootstrap event 
	 */
	private void bootstrap(final LdvTimeContextSentEvent event)
	{
		if (this != event.getTarget())
			return ;
		
		__fatherTimeDisplay = event.getFatherTimeDisplay() ;
		
		// Add display to father's display
		//
		AbsolutePanel TimeDisplayView = (AbsolutePanel) __fatherTimeDisplay.getDisplay().asWidget() ; 
		TimeDisplayView.add(getDisplay().asWidget()) ;
		
		// Signal father that the bootstrapping operation is done
		//
		eventBus.fireEvent(new LdvTimeContextReadyEvent(__fatherTimeDisplay)) ;
	}
	
	/**
	 * Processing a "need to redraw" event
	 */
	private void askedToRedraw(LdvTimeContextRedrawEvent event)
	{
		if (this != event.getTarget())
			return ;
		
		while (_bIsDrawing)
			_bKeepOnDrawing = false ;
			
		draw(event.getLeftLimit()) ;
	}
	
	/**
	 * Draw the context depending on the time scale
	 * 
	 * @param leftLimit Date-Time of the left part of the area to draw
	 */
	private void draw(LdvTime leftLimit)
	{
		if (null == leftLimit)
			return ;
		
		switch(__fatherTimeDisplay.getCurrentZoomLevel().getPixUnit())
		{
			case pixMonth :
				drawContextDecades(leftLimit) ;
				break ;
			case pixWeek :
				eraseContext() ;
				break ;
			case pixDay :
				drawContextYears(leftLimit) ;
				break ;
			case pixHour :
				drawContextMonths(leftLimit) ;
				break ;
			case pixMinute :
				drawContextDays(leftLimit) ;
				break ;
			case pixSecond :
				drawContextHours(leftLimit) ;
				break ;
			default:
				return ;
		}
	}
	
	/**
	 * Get the position of a date, in pixel, relative to upper right corner
	 * 
	 * The result is positive if ldvTime > _topRightTime and negative elsewhere
	 * 
	 * Contrary to getPhysicalDistance, this function uses the compression factor (getUppRate)
	 */
	public int getRelativePhysicalPosition(LdvTime ldvTime)
	{	
		if ((null == ldvTime) || ldvTime.isNoLimit())
			return Integer.MAX_VALUE ;
		
		return __fatherTimeDisplay.getRelativePhysicalPosition(ldvTime) ;		
	}
	
	/**
	 * Get the left position of a date, in pixel, inside the Element 
	 * 
	 * If the date is inside the Element, the value should range between 0 and offsetWidth
	 */
	public int getInternalPhysicalPosition(LdvTime ldvTime)
	{			
		if ((null == ldvTime) || ldvTime.isNoLimit())
			return Integer.MAX_VALUE ;
		
		int elementWidth = display.getOffsetWidth() ;
		int iRelativePos = getRelativePhysicalPosition(ldvTime) ;

		return elementWidth + iRelativePos ;		
	}
	
	/**
	 * Draw labels that provide a context to black and red step boxes
	 * 
	 * Take care that all boxes are not necessarily of equal width. For example, a month can be made of 28, 29, 30 or 31 days 
	 * 
	 * @param iStepIndex Step number (from right to left)
	 * @param left Time for step's left border
	 * @param right Time for step's right border
	 * @param sText Step's text
	 * 
	 */
	public void drawChronoContext(int iStepIndex, LdvTime left, LdvTime right, String sText)
	{
		int iLeftMargin = display.getLeftMargin() ;
		
		// position are converted from time to pixel
		//
		int iLeftPosition  = (int) getInternalPhysicalPosition(left) - iLeftMargin ;
		int iRightPosition = (int) getInternalPhysicalPosition(right) - iLeftMargin ;
	    
		if (iRightPosition <= iLeftPosition)
			return ;
	    
		int iBlockWidth = iRightPosition - iLeftPosition ;
	    	    
		// Create the label or make it visible    
		LdvTimeContextBox box = null ;
		
		if (iStepIndex < _contextBoxElements.size()) 
		{
			box = _contextBoxElements.get(iStepIndex) ;
			box.showBox() ;
			
			if (box.getLeftPos() != iLeftPosition)
			{
				Log.info("LdvTimeContextPresenter::drawChronoStep - set new position") ;
				display.setBoxPosition(box, iLeftPosition, 0) ;
			}
			
			// Log.info("LdvTimeContextPresenter::drawChronoStep - reinitialize") ;
			box.reInitialize(iLeftPosition, iBlockWidth, sText) ;
		} 
		// Create the new label
		else 
		{ 
			box = new LdvTimeContextBox(iLeftPosition, iBlockWidth, sText) ;
			
			box.addStyleName("ldv-context-label") ;
			display.addBox(box) ;
			display.setBoxPosition(box, iLeftPosition, 0) ;
			box.setWidthInPixel() ;
			box.setText() ;
			
			_contextBoxElements.add(box) ;
		}
	}
		
	public void hideRemainingContextElements(int iStepIndex)
	{
		if (_contextBoxElements.isEmpty())
			return ;
		
		for (int i = iStepIndex; i < _contextBoxElements.size(); i++)
			display.hideBox(_contextBoxElements.get(i)) ;
	}
	
	/**
	 * Erase context panel 
	 */
	public void eraseContext()
	{
		hideRemainingContextElements(0) ;
	}
	
	/**
	 * Draw context where each box is a year (12 months) 
	 */
	public void drawContextDecades(LdvTime leftLimit)
	{
		Log.info("Entering LdvTimeContextPresenter::drawContextDecades") ;
		
		// First text in the rightest "box" (from 1st day of _topRightTime's decade to _topRightTime)
		//
		LdvTime right = new LdvTime(__fatherTimeDisplay.getHourTimeZone()) ;
		right.initFromLdvTime(__fatherTimeDisplay.getTopRightTime()) ;
		LdvTime left = new LdvTime(right.getHourTimeZone(), (right.getLocalFullYear() / 10) * 10, 1, 1, 0, 0, 0) ;
		
		// Draw texts, each in a decade wide "box" (except for extremities) 
		//
		int iStepIndex = 0 ;
		while (right.isAfter(leftLimit))
		{			
			LdvInt iYears = new LdvInt(left.getLocalFullYear()) ;
			String sText  = iYears.intToString(-1) ;
			
			if (leftLimit.isAfter(left))
				left.initFromLdvTime(leftLimit) ;
			
	    drawChronoContext(iStepIndex, left, right, sText) ;

	    right.initFromLdvTime(left) ;
	    
	    // Switch to previous decade
	    //
	    left.addYears(-10, true) ;
			
			iStepIndex++ ;
		}
		
		Log.info("LdvTimeContextPresenter: Calling drawContextDecades done for " + iStepIndex + " steps") ;
		
		hideRemainingContextElements(iStepIndex) ;
		
		// Log.info("Leaving LdvTimeContextPresenter::drawContextDecades") ;
	}
		
	public void drawContextYears(LdvTime leftLimit)
	{
		// First text in the rightest "box" (from 1st day of _topRightTime's year to _topRightTime)
		//
		LdvTime right = new LdvTime(__fatherTimeDisplay.getHourTimeZone()) ;
		right.initFromLdvTime(__fatherTimeDisplay.getTopRightTime()) ;
		LdvTime left = new LdvTime(right.getHourTimeZone(), right.getLocalFullYear(), 1, 1, 0, 0, 0) ;
		
		// Draw texts, each in a year wide "box" (except for extremities) 
		//
		int iStepIndex = 0 ;
		while (right.isAfter(leftLimit))
		{			
			LdvInt iYears = new LdvInt(left.getLocalFullYear()) ;
			String sText  = iYears.intToString(-1) ;
			
			if (leftLimit.isAfter(left))
				left.initFromLdvTime(leftLimit) ;
			
	    drawChronoContext(iStepIndex, left, right, sText) ;

	    right.initFromLdvTime(left) ;
	    
	    // Switch to previous year
	    //
	    left.addYears(-1, true) ;
			
			iStepIndex++ ;
		}
		
		hideRemainingContextElements(iStepIndex) ;
	}
		
	public void drawContextMonths(LdvTime leftLimit)
	{
		// First text in the rightest "box" (from 1st day of _topRightTime's month to _topRightTime)
		//
		LdvTime right = new LdvTime(__fatherTimeDisplay.getHourTimeZone()) ;
		right.initFromLdvTime(__fatherTimeDisplay.getTopRightTime()) ;
		LdvTime left = new LdvTime(right.getHourTimeZone(), right.getLocalFullYear(), right.getLocalMonth(), 1, 0, 0, 0) ;
		
		// Draw texts, each in a month wide "box" (except for extremities) 
		//
		int iStepIndex = 0 ;
		while (right.isAfter(leftLimit))
		{			
			LdvInt iYears = new LdvInt(left.getLocalFullYear()) ;
			String sText  = iYears.intToString(-1) ;
			LdvInt iMonth = new LdvInt(left.getLocalMonth()) ;
			sText = iMonth.intToString(-1) + "/" + sText ;
			
			if (leftLimit.isAfter(left))
				left.initFromLdvTime(leftLimit) ;
			
	    drawChronoContext(iStepIndex, left, right, sText) ;

	    right.initFromLdvTime(left) ;
	    
	    // Switch to previous month
	    //
	    left.addMonths(-1, true) ;
			
			iStepIndex++ ;
		}
		
		hideRemainingContextElements(iStepIndex) ;
	}
		
	public void drawContextDays(LdvTime leftLimit)
	{
		// First text in the rightest "box" (from 1st hour of _topRightTime's day to _topRightTime)
		//
		LdvTime right = new LdvTime(__fatherTimeDisplay.getHourTimeZone()) ;
		right.initFromLdvTime(__fatherTimeDisplay.getTopRightTime()) ;
		LdvTime left = new LdvTime(right.getHourTimeZone(), right.getLocalFullYear(), right.getLocalMonth(), right.getLocalDate(), 1, 0, 0) ;
		
		// Draw texts, each in a day wide "box" (except for extremities) 
		//
		int iStepIndex = 0 ;
		while (right.isAfter(leftLimit))
		{			
			LdvInt iYears = new LdvInt(left.getLocalFullYear()) ;
			String sText  = iYears.intToString(-1) ;
			LdvInt iMonth = new LdvInt(left.getLocalMonth()) ;
			sText = iMonth.intToString(-1) + "/" + sText ;
			LdvInt iDay   = new LdvInt(left.getLocalDate()) ;
			sText = iDay.intToString(-1) + "/" + sText ;
			
			if (leftLimit.isAfter(left))
				left.initFromLdvTime(leftLimit) ;
			
	    drawChronoContext(iStepIndex, left, right, sText) ;

	    right.initFromLdvTime(left) ;
	    
	    // Switch to previous day
	    //
	    left.addDays(-1, true) ;
			
			iStepIndex++ ;
		}
		
		hideRemainingContextElements(iStepIndex) ;
	}
		
	public void drawContextHours(LdvTime leftLimit)
	{
		// First text in the rightest "box" (from 1st minute of _topRightTime's day to _topRightTime)
		//
		LdvTime right = new LdvTime(__fatherTimeDisplay.getHourTimeZone()) ;
		right.initFromLdvTime(__fatherTimeDisplay.getTopRightTime()) ;
		LdvTime left = new LdvTime(right.getHourTimeZone(), right.getLocalFullYear(), right.getLocalMonth(), right.getLocalDate(), right.getLocalHours(), 1, 0) ;
		
		// Draw texts, each in a day wide "box" (except for extremities) 
		//
		int iStepIndex = 0 ;
		while (right.isAfter(leftLimit))
		{			
			LdvInt iYears = new LdvInt(left.getLocalFullYear()) ;
			String sText  = iYears.intToString(-1) ;
			LdvInt iMonth = new LdvInt(left.getLocalMonth()) ;
			sText = iMonth.intToString(-1) + "/" + sText ;
			LdvInt iDay   = new LdvInt(left.getLocalDate()) ;
			sText = iDay.intToString(-1) + "/" + sText ;
			LdvInt iHour  = new LdvInt(left.getLocalHours()) ;
			sText = sText + " " + iHour.intToString(-1) + ":00" ;
			
			if (leftLimit.isAfter(left))
				left.initFromLdvTime(leftLimit) ;
			
	    drawChronoContext(iStepIndex, left, right, sText) ;

	    right.initFromLdvTime(left) ;
	    
	    // Switch to previous hour
	    //
	    left.addHours(-1, true) ;
			
			iStepIndex++ ;
		}
		
		hideRemainingContextElements(iStepIndex) ;
	}
	
	public boolean IsDrawing() {
		return _bIsDrawing ;
	}
	
	public void StopDrawing() 
	{
		if (false == _bIsDrawing)
			return ;
			
		_bKeepOnDrawing = false ;
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
