package com.ldv.client.event;

import com.google.gwt.event.shared.GwtEvent;
import com.ldv.client.mvp.LdvProjectWindowPresenter;

public class LdvRedrawAllConcernLinesEvent extends GwtEvent<LdvRedrawAllConcernLinesEventHandler> 
{	
	public static Type<LdvRedrawAllConcernLinesEventHandler> TYPE = new Type<LdvRedrawAllConcernLinesEventHandler>();
	
	private LdvProjectWindowPresenter   _father ;
	
	public static Type<LdvRedrawAllConcernLinesEventHandler> getType() 
	{
		if (null == TYPE)
			TYPE = new Type<LdvRedrawAllConcernLinesEventHandler>();
		return TYPE;
	}

	public LdvRedrawAllConcernLinesEvent(LdvProjectWindowPresenter father){
		_father = father ;
	}

	public LdvProjectWindowPresenter getFather(){
		return _father ;
	}
	
	@Override
	protected void dispatch(LdvRedrawAllConcernLinesEventHandler handler) {
		handler.onRedrawAllConcernLinesSend(this) ;
	}

	@Override
	public Type<LdvRedrawAllConcernLinesEventHandler> getAssociatedType() {
		return TYPE;
	}
}
