package com.ldv.client.mvp_toons;

import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.widgetideas.client.ResizableWidget;

public class LdvBaseLineView extends Composite implements ResizableWidget, LdvBaseLinePresenter.Display
{	
	private final AbsolutePanel _mainPanel ;
		
	public LdvBaseLineView()
	{	
		super() ;
		
		_mainPanel = new AbsolutePanel() ;
		_mainPanel.addStyleName("ldv-Toon-Baseline") ;
		_mainPanel.getElement().getStyle().setPosition(Position.ABSOLUTE) ;
		initWidget(_mainPanel) ;
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
