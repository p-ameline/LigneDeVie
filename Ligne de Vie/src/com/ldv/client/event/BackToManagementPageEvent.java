package com.ldv.client.event;

import com.google.gwt.event.shared.GwtEvent;

public class BackToManagementPageEvent extends GwtEvent<BackToManagementPageEventHandler> 
{	
	public static Type<BackToManagementPageEventHandler> TYPE = new Type<BackToManagementPageEventHandler>() ;
	
	public static Type<BackToManagementPageEventHandler> getType() 
	{
		if (null == TYPE)
			TYPE = new Type<BackToManagementPageEventHandler>() ;
		return TYPE ;
	}
	
	public BackToManagementPageEvent(){	
	}
		
	@Override
	protected void dispatch(BackToManagementPageEventHandler handler) {
		handler.onBackToManagement(this) ;
	}

	@Override
	public Type<BackToManagementPageEventHandler> getAssociatedType() {
		return TYPE ;
	}
}
