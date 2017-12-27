package com.ldv.client.event;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.user.client.ui.FlowPanel;

public class LdvManagementPageEvent extends GwtEvent<LdvManagementPageEventHandler> {
	
	public static Type<LdvManagementPageEventHandler> TYPE = new Type<LdvManagementPageEventHandler>();
	
	private FlowPanel _header ;
	private FlowPanel _workspace ;
	
	public static Type<LdvManagementPageEventHandler> getType() 
	{
		if (null == TYPE)
			TYPE = new Type<LdvManagementPageEventHandler>();
		return TYPE;
	}
	
	public LdvManagementPageEvent(FlowPanel workspace, FlowPanel header) 
	{
		_header    = header ;
		_workspace = workspace ;		
	}
	
	public FlowPanel getWorkspace(){
		return _workspace ;
	}

	public FlowPanel getHeader(){
		return _header ;
	}
	
	@Override
	protected void dispatch(LdvManagementPageEventHandler handler) {
		handler.onManagement(this);
	}

	@Override
	public Type<LdvManagementPageEventHandler> getAssociatedType() {
		return TYPE;
	}

}
