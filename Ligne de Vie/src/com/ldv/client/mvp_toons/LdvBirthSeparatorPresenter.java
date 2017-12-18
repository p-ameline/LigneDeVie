package com.ldv.client.mvp_toons;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.inject.Inject;

import com.ldv.client.event.LdvBirthSeparatorInitEvent;
import com.ldv.client.event.LdvBirthSeparatorInitEventHandler;
import com.ldv.client.mvp.LdvProjectWindowPresenter;

import net.customware.gwt.presenter.client.EventBus;
import net.customware.gwt.presenter.client.widget.WidgetDisplay;
import net.customware.gwt.presenter.client.widget.WidgetPresenter;

/** 
 * The Presenter object for a Base line
 * 
 */
public class LdvBirthSeparatorPresenter extends WidgetPresenter<LdvBirthSeparatorPresenter.Display>
{
	public interface Display extends WidgetDisplay 
	{
		public void setPosition(int left) ;
	}

	protected LdvProjectWindowPresenter _project ;
	private   boolean                   _isBound = false ;
		
	@Inject
	public LdvBirthSeparatorPresenter(final Display display, EventBus eventBus) 
	{	
		super(display, eventBus);	
		bind();
		Log.info("entering constructor of LdvBasePresenter.");
	}
	  
	public void connectToProject(LdvBirthSeparatorInitEvent event) 
	{			
		if (event.getTarget() != this)
			return ;
		
		getDisplay().setPosition(event.getX()) ;
		AbsolutePanel project = (AbsolutePanel) event.getProject() ;
		project.add(getDisplay().asWidget()) ;
	}

	@Override
	protected void onBind() 
	{			
		eventBus.addHandler(LdvBirthSeparatorInitEvent.TYPE, new LdvBirthSeparatorInitEventHandler() 
		{
			@Override
			public void onInitSend(LdvBirthSeparatorInitEvent event) 
			{
				Log.info("Handling LdvBirthSeparatorInitEvent event") ;
				connectToProject(event) ;
			}
		});
	}

	public void setXPos(int iLeftPosForNow) {
		if (_isBound)
			return ;
			
		display.setPosition(iLeftPosForNow) ;
	}

	public void draw(Panel workspacePanel, int iLeftPosForNow) {
		if (null == workspacePanel)
			return ;
		
		getDisplay().setPosition(iLeftPosForNow) ;
		workspacePanel.add(getDisplay().asWidget()) ;
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
