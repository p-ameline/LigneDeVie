package com.ldv.client.canvas;

import com.google.gwt.dom.client.Style; 
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.FocusPanel;

public class LdvScrollArea extends AbsolutePanel
{
	
	/**
	 * The Thumb object.
	 */
	private FocusPanel _thumb ;
  private int 			 _thumbwidth;
		
	public LdvScrollArea() 
	{
		super() ;
		
		addStyleName("ldvScrollArea") ;
		
		_thumb = new FocusPanel() ;
		_thumb.addStyleName("ldvScrollAreaThumb") ;	
		_thumb.getElement().getStyle().setPosition(Position.ABSOLUTE) ;
			
		add(_thumb) ;
				
	}

	public FocusPanel getThumb(){
		return this._thumb ;
	}
	
	public void moveThumb(int iPixel){
		
		int x = _thumb.getElement().getOffsetLeft() ;
		int left = x + iPixel ;
		_thumb.getElement().getStyle().setLeft(left, Style.Unit.PX) ;
		
	}
	
	public void setScrollAreaWidth(int width){
		this.getElement().getStyle().setWidth(width, Style.Unit.PX) ;
	}
	
	public void setThumbWidth(int width){
		this._thumbwidth = width ;
	}
	
	public int getThumbWidth(){
		return this._thumbwidth ;
	}
}

