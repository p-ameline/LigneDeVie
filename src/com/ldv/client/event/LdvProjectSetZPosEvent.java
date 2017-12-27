package com.ldv.client.event;

import com.google.gwt.event.shared.GwtEvent;

public class LdvProjectSetZPosEvent extends GwtEvent<LdvProjectSetZPosEventHandler> 
{	
	public static Type<LdvProjectSetZPosEventHandler> TYPE = new Type<LdvProjectSetZPosEventHandler>();
	
	public enum ZPosMovement { moveFront, moveBack, moveDown, moveUp } ;
	
	private String       _sProjectUri ;
	private ZPosMovement _movementType ;
	
	public static Type<LdvProjectSetZPosEventHandler> getType() 
	{
		if (null == TYPE)
			TYPE = new Type<LdvProjectSetZPosEventHandler>();
		return TYPE;
	}

	public LdvProjectSetZPosEvent(String sProjectUri, ZPosMovement askedMovement)
	{
		_sProjectUri  = sProjectUri ;
		_movementType = askedMovement ;
	}

	public String getProjectUri() {
		return _sProjectUri ;
	}
	
	public ZPosMovement getMovementType() {
		return _movementType ;
	}
	
	@Override
	protected void dispatch(LdvProjectSetZPosEventHandler handler) {
		handler.onProjectSetZOrderSend(this) ;
	}

	@Override
	public Type<LdvProjectSetZPosEventHandler> getAssociatedType() {
		return TYPE;
	}
}
