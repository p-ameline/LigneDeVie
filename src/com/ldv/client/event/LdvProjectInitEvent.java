package com.ldv.client.event;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.user.client.ui.Panel;
import com.ldv.client.model.LdvModelProject;
import com.ldv.client.mvp.LdvProjectWindowPresenter;
import com.ldv.client.mvp.LdvTimeControlledAreaPresenter;

/** 
 * Event sent to a project Presenter. It contains all necessary information for Project initialization.
 * 
 */
public class LdvProjectInitEvent extends GwtEvent<LdvProjectInitEventHandler> 
{	
	public static Type<LdvProjectInitEventHandler> TYPE = new Type<LdvProjectInitEventHandler>();
	
	private Panel                          _workspace ;
	private LdvProjectWindowPresenter      _target ;				// target of this event
	private LdvTimeControlledAreaPresenter _father ;				// sender of this event
	private LdvModelProject 							 _projectModel ;	// Project information
	
	public static Type<LdvProjectInitEventHandler> getType() 
	{
		if (null == TYPE)
			TYPE = new Type<LdvProjectInitEventHandler>();
		return TYPE;
	}
	
	public LdvProjectInitEvent(Panel workspace, LdvProjectWindowPresenter target, LdvTimeControlledAreaPresenter father, LdvModelProject projetModel){
		_workspace = workspace ;
		_target    = target ;
		_father    = father ;
		_projectModel = projetModel ;
	}
	
	public Panel getWorkspace(){
		return _workspace ;
	}
	
	public LdvProjectWindowPresenter getTarget(){
		return _target ;
	}
	
	public LdvTimeControlledAreaPresenter getFather(){
		return _father ;
	}
	
	public LdvModelProject getModelProject(){
		return _projectModel ;
	}
	
	@Override
	protected void dispatch(LdvProjectInitEventHandler handler) {
		handler.onInitSend(this);
	}

	@Override
	public Type<LdvProjectInitEventHandler> getAssociatedType() {
		return TYPE;
	}
}
