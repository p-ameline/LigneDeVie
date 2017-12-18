package com.ldv.client.mvp_toons;

import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.widgetideas.client.ResizableWidget;

public class LdvNowSeparatorView extends Composite implements ResizableWidget, LdvNowSeparatorPresenter.Display
{	
	private final AbsolutePanel _mainPanel ;
		
	public LdvNowSeparatorView()
	{	
		super() ;
		
		_mainPanel = new AbsolutePanel() ;
		_mainPanel.addStyleName("ldv-Toon-NowSeparator") ;
		_mainPanel.getElement().getStyle().setPosition(Position.ABSOLUTE) ;
		initWidget(_mainPanel) ;
	}	
	
	/**
	 * Set concern line's left and bottom coordinates 
	 * 
	 * @param left Left position for line
	 * @param top  Top position for line
	 */
	@Override
	public void setPosition(int left)
	{
		_mainPanel.getElement().getStyle().setLeft(left, Style.Unit.PX) ;
	}
	
	@Override
	public Widget asWidget() {
		return this ;
	}

	@Override
	public void onResize(int width, int height) {
		// TODO Auto-generated method stub		
	}
}
