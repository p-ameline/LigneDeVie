package com.ldv.client.mvp_toons;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.AbsolutePanel;

import com.ldv.client.canvas.LdvTimeContextBox;

public class LdvTimeContextView extends AbsolutePanel implements LdvTimeContextPresenter.Display
{
	public LdvTimeContextView() 
	{
		super() ;
		setStyleName("ldv-TimeControl-contextsArea") ;
	}
	
	@Override
	public int getLeftMargin() {
		return DOM.getElementPropertyInt(getElement(), "left") ;
	}
	
	@Override
	public int getOffsetWidth() {
		return DOM.getElementPropertyInt(getElement(), "offsetWidth") ;
	}
	
	@Override
	public void setBoxPosition(LdvTimeContextBox box, int iLeftPosition, int iTopPosition) {
		setWidgetPositionImpl(box, iLeftPosition, 0) ;
	}
	
	@Override
	public void addBox(LdvTimeContextBox box) {
		add(box) ;
	}
	
	@Override
	public void showBox(LdvTimeContextBox box) {
		box.showBox() ;
	}
	
	@Override
	public void hideBox(LdvTimeContextBox box) {
		box.hideBox() ;
	}
}
