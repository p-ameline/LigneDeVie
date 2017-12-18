package com.ldv.client.canvas;

import com.google.gwt.dom.client.Document;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Widget;

public class LdvTimeContextBox extends Widget
{
	private int     _iLeftPos ;
	private int     _iWidth ;
	private String  _sText ;
	
	private boolean _bVisible ;
			
	public LdvTimeContextBox(int iLeftPos, int iWidth, String sText)
	{
		super() ;
		setElement(Document.get().createDivElement()) ;
		
		_iLeftPos = iLeftPos ;
		_iWidth   = iWidth ;
		_sText    = sText ;
		_bVisible = true ;
	}
		
	/**
	 * Redraw the progress bar when something changes the layout.
	 */
	public void reInitialize(int iLeftPos, int iWidth, String sText)
	{
		if (iWidth != _iWidth)
		{
			_iWidth = iWidth ;
			setWidthInPixel() ;
		}
		
		_iLeftPos = iLeftPos ;

		if (false == sText.equals(_sText))
		{
			_sText = sText ;
			setText() ;
		}
	}
	
	/**
	 * Set width in pixel
	 */
	public void setWidthInPixel() {
		setWidth("" + _iWidth + "px") ;
	}
	
	/**
	 * Set text
	 */
	public void setText() {
		DOM.setElementProperty(getElement(), "innerHTML", _sText) ;
	}
	
	/**
	 * Show and hide
	 */
	public void showBox()
	{
		if (_bVisible)
			return ;
		
		DOM.setStyleAttribute(getElement(), "display", "block") ;
	
		_bVisible = true ;
	}
	
	public void hideBox() 
	{
		if (false == _bVisible)
			return ;
		
		DOM.setStyleAttribute(getElement(), "display", "none") ;
		
		_bVisible = false ;
	}
	
	public int getLeftPos() {
		return _iLeftPos ;
	}
	
	public Widget asWidget() {
    return this;
  }
}
