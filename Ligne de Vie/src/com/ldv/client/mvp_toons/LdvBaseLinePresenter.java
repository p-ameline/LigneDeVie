package com.ldv.client.mvp_toons;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.inject.Inject;
import com.ldv.client.event.LdvBaseLineInitEvent;
import com.ldv.client.event.LdvBaseLineInitEventHandler;
import com.ldv.client.mvp.LdvProjectWindowPresenter;

import net.customware.gwt.presenter.client.EventBus;
import net.customware.gwt.presenter.client.widget.WidgetDisplay;
import net.customware.gwt.presenter.client.widget.WidgetPresenter;

/** 
 * The Presenter object for a Base line
 * 
 */
public class LdvBaseLinePresenter extends WidgetPresenter<LdvBaseLinePresenter.Display>
{
	public interface Display extends WidgetDisplay 
	{
	}

	protected LdvProjectWindowPresenter _project ;
		
	@Inject
	public LdvBaseLinePresenter(final Display display, EventBus eventBus) 
	{	
		super(display, eventBus) ;
		
		Log.info("entering constructor of LdvBasePresenter.") ;
		
		bind() ;
	}
	  
	@Override
	protected void onBind() 
	{			
		eventBus.addHandler(LdvBaseLineInitEvent.TYPE, new LdvBaseLineInitEventHandler() 
		{
			@Override
			public void onInitSend(LdvBaseLineInitEvent event) 
			{
				Log.info("LdvBaseLinePresenter project " + _project.getProjectUri() + " handling LdvBaseLineInitEvent for " + event.getTarget().getProject().getProjectUri()) ;
				connectToProject(event) ;
			}
		});
	}

	protected void connectToProject(LdvBaseLineInitEvent event) 
	{			
		if (false == _project.getProjectUri().equals(event.getTarget().getProject().getProjectUri()))
			return ;
		
		Log.info("LdvBaseLinePresenter... connecting display") ;
		
		AbsolutePanel baseLinePanel = (AbsolutePanel) event.getBaseLinePanel() ;
		baseLinePanel.add(getDisplay().asWidget()) ;
	}
	
	public void setProject(LdvProjectWindowPresenter project) {
		_project = project ;
	}
	
	public LdvProjectWindowPresenter getProject() {
		return _project ;
	}
	
	@Override
	protected void onUnbind() {
	}

	@Override
	public void revealDisplay() {
	}

	@Override
	protected void onRevealDisplay() {
		// TODO Auto-generated method stub
		
	}
}
