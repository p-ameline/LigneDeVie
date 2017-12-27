package com.ldv.client.canvas;

import com.google.gwt.user.client.ui.FocusPanel;

public class LdvProjectTab extends FocusPanel
{
	private String _sTooltipText ; 
	
	public LdvProjectTab()
	{
		super() ;
	}
	
	public LdvProjectTab(String sTooltipText)
	{
		super() ;
		
		setTooltipText(sTooltipText) ;
	}
	
	public void setTooltipText(String sTooltipText)
	{
		_sTooltipText = sTooltipText ;
		getElement().setAttribute("title", _sTooltipText) ;
	}	
}
