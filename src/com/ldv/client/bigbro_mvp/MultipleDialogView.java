package com.ldv.client.bigbro_mvp;

import com.allen_sauer.gwt.log.client.Log;

import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import com.ldv.client.util.ArchetypePanel;

public class MultipleDialogView extends DialogBox implements MultipleDialogPresenter.Display  // was Composite
{	
	private ArchetypePanel _pannel ;
	private Label           _emToPixCalcLabel ;
	
	public MultipleDialogView()
	{
		super(true, true) ;
		setAnimationEnabled(true) ;
		
		Log.debug("ArchetypeDialogView constructor") ;
		
		_pannel           = new ArchetypePanel() ;
		
		_emToPixCalcLabel = new Label("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz") ;
		_emToPixCalcLabel.setVisible(false) ;
		
		_pannel.add(_emToPixCalcLabel) ;
		
		setWidget(_pannel) ;
		
		int iTextHeight = _emToPixCalcLabel.getOffsetHeight() ;
		if (0 == iTextHeight)
			iTextHeight = 15 ;
		
		int iTextWidth  = _emToPixCalcLabel.getOffsetWidth() ;
		if (0 == iTextWidth)
			iTextWidth = 6240 ;
		
		// Dialog boxes' units are "em"
		//
		_pannel.setUnit("px") ;
		_pannel.setBaseUnitX((iTextWidth / 26 + 1) / 2 / 48) ;
		_pannel.setBaseUnitY(iTextHeight / 6) ;
	}
	
	@Override
	public Widget asWidget() {
		return this ;
	}
}
