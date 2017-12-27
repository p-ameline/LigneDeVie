package com.ldv.client.mvp_toons;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.widgetideas.client.ResizableWidget;
import com.google.gwt.widgetideas.client.ResizableWidgetCollection;

// public class LdvTimeWidget extends FlowPanel implements ResizableWidget
public class LdvTimeDisplayView extends AbsolutePanel implements ResizableWidget, LdvTimeDisplayPresenter.Display
{	
	public LdvTimeDisplayView()
	{
		super() ;
		
		// Create the outer shell
		//
		// DOM.setStyleAttribute(getElement(), "position", "relative") ;
		// DOM.setStyleAttribute(getElement(), "position", "absolute") ;
		setStyleName("ldv-TimeControl-shell") ;	    
	}
	
	/**
	 * This method is called immediately after a widget becomes attached to the
	 * browser's document.
	 */
	@Override
	protected void onLoad() 
	{
		// Reset the position attribute of the parent element
		// DOM.setStyleAttribute(getElement(), "position", "relative") ;
		ResizableWidgetCollection.get().add(this) ;
		redraw() ;
	}

	@Override
	protected void onUnload() {
		ResizableWidgetCollection.get().remove(this);
	}
	
	/**
	 * Redraw the progress bar when something changes the layout.
	 */
	public void redraw() 
	{
		if (isAttached()) 
		{
			int width  = DOM.getElementPropertyInt(getElement(), "clientWidth") ;
			int height = DOM.getElementPropertyInt(getElement(), "clientHeight") ;
			onResize(width, height) ;
		}
	}
	
	/**
	  * This method is called when the dimensions of the parent element change.
	  * Subclasses should override this method as needed.
	  * 
	  * @param width the new client width of the element
	  * @param height the new client height of the element
	  */
	public void onResize(int width, int height) 
	{  
	  // Draw the other components
		// drawTimeElements() ;
	}
	
	@Override
	public int getOffsetWidth() {
		return DOM.getElementPropertyInt(getElement(), "offsetWidth") ;
	}
}

