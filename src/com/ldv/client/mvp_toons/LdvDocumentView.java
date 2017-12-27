package com.ldv.client.mvp_toons;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.widgetideas.client.ResizableWidget;
import com.google.gwt.dom.client.Style; 
import com.google.gwt.dom.client.Style.Position;

public class LdvDocumentView extends Composite implements ResizableWidget, LdvDocumentPresenter.Display
{	
	private final Image _icon ;
	private final int _top = 16 ;
	private DialogBox dialogbox = new DialogBox() ;
			
	public LdvDocumentView()
	{	
		super() ;
		
		_icon = new Image() ;
		_icon.addStyleName("ldv-Icon") ;
		_icon.getElement().getStyle().setPosition(Position.ABSOLUTE) ;
		
		_icon.setUrl("/icon.png") ;
		//_icon.setVisibleRect(70, 0, 47, 110);

		initWidget(_icon) ;
	}	
	
	@Override
	public Image getIcon(){
		return this._icon ;
	}
	
	@Override
	public void setIconPosition(int position){
		_icon.getElement().getStyle().setLeft(position, Style.Unit.PX) ;
		_icon.getElement().getStyle().setTop(_top, Style.Unit.PX) ;
	}
	
	@Override
	public void showDocTip(String title){
		dialogbox.setText(title) ;
		dialogbox.show() ;
	}
	
	@Override
	public void hideDocTip(){
		dialogbox.hide() ;
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
