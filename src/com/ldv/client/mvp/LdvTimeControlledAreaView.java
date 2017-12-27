package com.ldv.client.mvp;

import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.HasMouseDownHandlers;
import com.google.gwt.event.dom.client.HasMouseUpHandlers;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.widgetideas.client.ResizableWidget;

//public class LdvTimeControlView extends SimplePanel implements ResizableWidget, /*SourcesChangeEvents,*/ LdvTimeControlPresenter.Display 
public class LdvTimeControlledAreaView extends Composite implements ResizableWidget, HasMouseUpHandlers, /*SourcesChangeEvents,*/ LdvTimeControlledAreaPresenter.Display
{	
	// private LdvTime          _topRightTime ;
	// private LdvTimeZoomLevel _currentZoomLevel ;
	
	/**
	 * The FlowPanel containing the TimeWidget and the composite
	 */
	private final AbsolutePanel _mainPanel ;
		
	/**
	 * The right scrollbar button.
	 */
	private final Button _rightScrollButton;
	
	/**
	 * The left scrollbar button.
	 */
	private final Button _leftScrollButton;
	
	// private final int _scrollAreaWidth ;
	
	public LdvTimeControlledAreaView()
	{	
		super() ;
		
		// _projectPanel = new LdvProjectWindowView() ;
		
		// _currentZoomLevel = new LdvTimeZoomLevel(pixUnit.pixMonth, 0, 0, 0) ;
		// _topRightTime = new LdvTime(0) ;
		// _topRightTime.takeTime() ;
		
		_rightScrollButton = new Button(">") ;
		_rightScrollButton.addStyleName("ldvScrollBarRightButton") ;
		_leftScrollButton = new Button("<") ;
		_leftScrollButton.addStyleName("ldvScrollBarLeftButton") ;
		
		_mainPanel = new AbsolutePanel() ;
		_mainPanel.addStyleName("ldvMainWorkspace") ;
		_mainPanel.getElement().getStyle().setPosition(Position.RELATIVE) ;
		
		// _scrollAreaWidth = this.getScrollArea().getOffsetWidth() ;
		
		initWidget(_mainPanel) ;
	}	
		
	@Override
	public int getAreaWidth(){
		return _mainPanel.getOffsetWidth() ;		
	}
	
	/**
	 * Gets time controlled area's absolute left position in pixels, as measured from the browser window's client area.
	 */
	@Override
	public int getAbsoluteLeft(){
		return _mainPanel.getAbsoluteLeft() ;
	}
	
	/**
	 * Gets time controlled area's absolute top position in pixels, as measured from the browser window's client area. 
	 */
	@Override
	public int getAbsoluteTop(){
		return _mainPanel.getAbsoluteLeft() ;
	}
	
	@Override
	public int getProjectsAreaWidth(){
		return _mainPanel.getOffsetWidth() ;		
	}
	
	public HasClickHandlers getRightButton() {
		return _rightScrollButton ;
	}
	
	public HasMouseDownHandlers getRightButtonMouseDown(){
		return _rightScrollButton ;
	}
	
	public HasMouseUpHandlers getRightButtonMouseUp(){
		return _rightScrollButton ;
	}
	
	@Override
	public Button getRightScrollButton() {
		return _rightScrollButton ;
	}
	
	public HasClickHandlers getLeftButton() {
		return _leftScrollButton ;
	}
	
	public HasMouseDownHandlers getLeftButtonMouseDown(){
		return _leftScrollButton ;
	}
	
	public HasMouseUpHandlers   getLeftButtonMouseUp(){
		return _leftScrollButton ;
	}
	
	@Override
	public Button getLeftScrollButton() {
		return _leftScrollButton ;
	}
	
	@Override
	public AbsolutePanel getMainPanel() {
		return _mainPanel ;
	}
	
/*
	@Override
	public LdvTimeZoomLevel getCurrentZoomLevel() {
		return _currentZoomLevel;
	}

	@Override
	public LdvTime getTopRightTime() {
		return _topRightTime;
	}

	@Override
	public void setCurrentZoomLevel(LdvTimeZoomLevel currentZoomLevel) {
		_currentZoomLevel = currentZoomLevel;
	}

	@Override
	public void setTopRightTime(LdvTime topRightTime) {
		_topRightTime = topRightTime;
	}
*/
	
/*	
	@Override
	public int getScrollAreaWidth(){
		return this._scrollAreaWidth ;
	}
*/
		
	@Override
	public void onResize(int width, int height) {
		// TODO Auto-generated method stub		
	}

	@Override
	public HandlerRegistration addMouseUpHandler(MouseUpHandler handler)
	{
		return addDomHandler(handler, MouseUpEvent.getType()) ;
	}
	
	@Override
	public Widget asWidget() {
		return this ;
	}
}
