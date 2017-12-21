package com.ldv.client.mvp_toons;

import com.google.gwt.user.client.ui.Panel;
import com.google.inject.Inject;

import com.ldv.client.mvp.LdvProjectWindowPresenter;

import net.customware.gwt.presenter.client.EventBus;
import net.customware.gwt.presenter.client.widget.WidgetDisplay;
import net.customware.gwt.presenter.client.widget.WidgetPresenter;

/** 
 * The Presenter object for a Base line
 * 
 */
public class LdvNowSeparatorPresenter extends WidgetPresenter<LdvNowSeparatorPresenter.Display>
{
	public interface Display extends WidgetDisplay 
	{
		public void setPosition(int left) ;
	}

	protected LdvProjectWindowPresenter _project ;
	private   boolean                   _isBound = false ;
		
	@Inject
	public LdvNowSeparatorPresenter(final LdvProjectWindowPresenter project, final Display display, EventBus eventBus) 
	{	
		super(display, eventBus);	
		bind();
	}

	@Override
	protected void onBind() 
	{			
	}

	/**
	 * Set to the proper X position inside the workspace
	 * 
	 * @param iLeftPosForNow New X position
	 */
	public void setXPos(int iLeftPosForNow) {
		display.setPosition(iLeftPosForNow) ;
	}

	/**
	 * Insert the display at the proper place inside workspace
	 * 
	 * @param panel          Workspace panel
	 * @param iLeftPosForNow Position of "now" inside the workspace
	 */
	public void draw(Panel workspacePanel, int iLeftPosForNow)
	{
		if (null == workspacePanel)
			return ;
		
		setXPos(iLeftPosForNow) ;
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
