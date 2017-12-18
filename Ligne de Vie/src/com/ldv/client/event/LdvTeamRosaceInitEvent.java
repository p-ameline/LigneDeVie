package com.ldv.client.event;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.user.client.ui.Panel;
import com.ldv.client.mvp.LdvTeamRosacePresenter;
import com.ldv.client.mvp.LdvProjectWindowPresenter;

/** 
 * Team rosace initialization event
 * 
 */
public class LdvTeamRosaceInitEvent extends GwtEvent<LdvTeamRosaceInitEventHandler> 
{	
	public static Type<LdvTeamRosaceInitEventHandler> TYPE = new Type<LdvTeamRosaceInitEventHandler>();

	private LdvTeamRosacePresenter    _target ;
	private Panel                     _project ;
	private LdvProjectWindowPresenter _father ;

	public static Type<LdvTeamRosaceInitEventHandler> getType() 
	{
		if (null == TYPE)
			TYPE = new Type<LdvTeamRosaceInitEventHandler>();
		return TYPE;
	}

	public LdvTeamRosaceInitEvent(Panel project, LdvTeamRosacePresenter target, LdvProjectWindowPresenter father)
	{
		_project = project ;
		_target  = target ;
		_father  = father ;
	}

	public Panel getProject(){
		return _project ;
	}

	public LdvTeamRosacePresenter getTarget() {
		return _target ;
	}
	
	public LdvProjectWindowPresenter getFather() {
		return _father ;
	}

	@Override
	protected void dispatch(LdvTeamRosaceInitEventHandler handler) {
		handler.onInitSend(this) ;
	}

	@Override
	public Type<LdvTeamRosaceInitEventHandler> getAssociatedType() {
		return TYPE ;
	}
}
