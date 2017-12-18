package com.ldv.client.event;

import com.google.gwt.event.shared.GwtEvent;

public class LdvRedrawProjectRecursiveEvent extends GwtEvent<LdvRedrawProjectRecursiveEventHandler> 
{	
	public static Type<LdvRedrawProjectRecursiveEventHandler> TYPE = new Type<LdvRedrawProjectRecursiveEventHandler>();
	
	private int _iTargetZorder ;
	
	public static Type<LdvRedrawProjectRecursiveEventHandler> getType() 
	{
		if (null == TYPE)
			TYPE = new Type<LdvRedrawProjectRecursiveEventHandler>();
		return TYPE;
	}
	
	public LdvRedrawProjectRecursiveEvent(int iTarget) {
		_iTargetZorder = iTarget ;
	}
	
	public int getTargetZorder(){
		return _iTargetZorder ;
	}
	
	@Override
	protected void dispatch(LdvRedrawProjectRecursiveEventHandler handler) {
		handler.onRedrawProjectRecursiveSend(this);
	}

	@Override
	public Type<LdvRedrawProjectRecursiveEventHandler> getAssociatedType() {
		return TYPE;
	}
}
