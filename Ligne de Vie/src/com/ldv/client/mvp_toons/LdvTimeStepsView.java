package com.ldv.client.mvp_toons;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.ldv.client.canvas.LdvTimeStepBox;

/**
 * LdvTimeStepsView is the panel that hosts the LdvTimeStepBox objects
 *    
 */
public class LdvTimeStepsView extends AbsolutePanel implements LdvTimeStepsPresenter.Display
{
	public LdvTimeStepsView() 
	{
		super() ;
		setStyleName("ldv-TimeControl-stepsArea") ;
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
	public void setBoxPosition(LdvTimeStepBox box, int iLeftPosition, int iTopPosition) {
		setWidgetPositionImpl(box, iLeftPosition, 0) ;
	}
	
	@Override
	public void addBox(LdvTimeStepBox box) {
		add(box) ;
	}
	
	@Override
	public void showBox(LdvTimeStepBox box) {
		box.showBox() ;
	}
	
	@Override
	public void hideBox(LdvTimeStepBox box) {
		box.hideBox() ;
	}
}
