package com.ldv.client.mvp_toons;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.ldv.client.canvas.LdvScrollArea;
import com.ldv.client.canvas.LdvScrollBar;

// public class LdvTimeController extends FlowPanel implements ResizableWidget
public class LdvTimeControllerView extends FlowPanel implements LdvTimeControllerPresenter.Display
{
	/**
	 * Time scale
	 */
	// private LdvTimeWidget _timeWidget ;
	
	/**
	 * Scroll component
	 */
	private LdvScrollBar  _scrollComponent ;
	
	public LdvTimeControllerView()
	{
		super() ;
		setStyleName("ldvTimeController") ;
		
		// _timeWidget      = new LdvTimeWidget(topRightTime, currentZoomLevel) ;
		_scrollComponent = new LdvScrollBar() ;
		
		//int scrollAreaWidth = this.getScrollArea().getOffsetWidth() ;
		//this.getScrollArea().getThumb().getElement().getStyle().setLeft(scrollAreaWidth,  Style.Unit.PX) ;
		
		// add(_timeWidget) ;
		add(_scrollComponent) ;
	}
	
	public LdvScrollBar getScrollBar() {
		return _scrollComponent ;
	}
	
	public LdvScrollArea getScrollArea() {
		return _scrollComponent.getScrollArea() ;
	}
	
	@Override
	public void setScrollButtons(Button right, Button left) {
		_scrollComponent.setScrollButtons(right, left) ;
	}
	
	@Override
	public int getDisplayAbsoluteLeft() {
		return getAbsoluteLeft() ;
	}

	@Override
	public int getOffsetLeft() {
		return DOM.getElementPropertyInt(getElement(), "offsetLeft") ;
	}
	
	@Override
	public int getOffsetTop() {
		return getElement().getOffsetTop() ;
	}
	
	public int getOffsetHeight() {
		return getElement().getOffsetHeight() ;
	}
	
	@Override
	public FlowPanel getWorkspace() {
		return this ;
	}
}
