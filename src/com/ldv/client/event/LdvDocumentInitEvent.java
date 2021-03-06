package com.ldv.client.event;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.ldv.client.model.LdvModelDocument;
import com.ldv.client.mvp_toons.LdvConcernLinePresenter;
import com.ldv.client.mvp_toons.LdvDocumentPresenter;

public class LdvDocumentInitEvent extends GwtEvent<LdvDocumentInitEventHandler> 
{	
	public static Type<LdvDocumentInitEventHandler> TYPE = new Type<LdvDocumentInitEventHandler>();
	
	private AbsolutePanel 						_linePanel ;
	private LdvDocumentPresenter      _target ;
	private LdvConcernLinePresenter   _father ;
	private LdvModelDocument					_model ;
	
	public static Type<LdvDocumentInitEventHandler> getType() 
	{
		if (null == TYPE)
			TYPE = new Type<LdvDocumentInitEventHandler>();
		return TYPE;
	}
	
	public LdvDocumentInitEvent(AbsolutePanel linePanel, LdvDocumentPresenter target, LdvConcernLinePresenter father, LdvModelDocument model){
		_linePanel = linePanel ;
		_target  = target ;
		_father  = father ;
		_model	 = model ;
	}
	
	public AbsolutePanel getLinePanel(){
		return _linePanel ;
	}
	
	public LdvDocumentPresenter getTarget(){
		return _target ;
	}
	
	public LdvConcernLinePresenter getFather(){
		return _father ;
	}
	
	public LdvModelDocument getModel(){
		return _model ;
	}
	
	@Override
	protected void dispatch(LdvDocumentInitEventHandler handler) {
		handler.onInitSend(this);
	}

	@Override
	public Type<LdvDocumentInitEventHandler> getAssociatedType() {
		return TYPE;
	}
}
