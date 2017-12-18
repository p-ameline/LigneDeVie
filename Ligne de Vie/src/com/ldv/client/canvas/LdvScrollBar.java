package com.ldv.client.canvas;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.SimplePanel;

public class LdvScrollBar extends Composite
{
	private final DockPanel     _mainpanel ;
	private       Button        _rightScrollButton ;
	private       Button        _leftScrollButton ;
	private final LdvScrollArea _scrollArea ;
	private final SimplePanel 	_rightButtonPanel ;
	private final SimplePanel 	_leftButtonPanel ;
	
	public LdvScrollBar()
	{	
		_rightScrollButton = null ;
		_leftScrollButton  = null ;
		_scrollArea = new LdvScrollArea() ;
			
		_mainpanel = new DockPanel() ;
		_mainpanel.addStyleName("ldvScrollBar") ;
		
		_rightButtonPanel = new SimplePanel() ;
		_rightButtonPanel.addStyleName("ldvRightButtonPanel") ;
		// _rightButtonPanel.add(_rightScrollButton) ;
		
		_leftButtonPanel = new SimplePanel() ;
		_leftButtonPanel.addStyleName("ldvleftButtonPanel") ;
		// _leftButtonPanel.add(_leftScrollButton) ;
		
		_mainpanel.add(_leftButtonPanel, DockPanel.WEST);
		_mainpanel.add(_rightButtonPanel, DockPanel.EAST);
		_mainpanel.add(_scrollArea, DockPanel.CENTER);
		_mainpanel.setCellWidth(_scrollArea, "100%") ;
		_mainpanel.getElement().setPropertyString("width", "100%");
		
		initWidget(_mainpanel) ;
	}

	public void setScrollButtons(Button right, Button left) 
	{
		_rightScrollButton = right ;
		_rightButtonPanel.add(_rightScrollButton) ;
		
		_leftScrollButton  = left ;
		_leftButtonPanel.add(_leftScrollButton) ;
	}
	
	public LdvScrollArea getScrollArea(){
		return this._scrollArea ;
	}
	
	/**
	 * This method is called immediately after a widget becomes attached to the
	 * browser's document.
	 */
	@Override
	protected void onLoad() {
	    // Reset the position attribute of the parent element
	    //DOM.setStyleAttribute(getElement(), "position", "relative");
	    //ResizableWidgetCollection.get().add(this);
	    redraw();
	  }
	
	/**
	 * Redraw the progress bar when something changes the layout.
	 */
	public void redraw() {
		// LdvInt leftButtonWidth = new LdvInt(DOM.getElementPropertyInt(_leftScrollButton.getElement(), "offsetWidth"));
		// LdvInt rightButtonWidth = new LdvInt(DOM.getElementPropertyInt(_rightScrollButton.getElement(), "offsetWidth"));
		//_scrollArea.getElement().setPropertyInt("left", leftButtonWidth);
		//_scrollArea.getElement().setPropertyInt("right", rightButtonWidth);
		// _mainpanel.setCellWidth(_leftScrollButton, leftButtonWidth.intToString(-1)+"px");
		// _mainpanel.setCellWidth(_rightScrollButton, rightButtonWidth.intToString(-1)+"px");
	}	
}
