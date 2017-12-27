package com.ldv.client.mvp_toons;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.inject.Inject;

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

	protected final LdvProjectWindowPresenter _project ;
		
	@Inject
	public LdvBaseLinePresenter(final LdvProjectWindowPresenter project, final Display display, EventBus eventBus) 
	{	
		super(display, eventBus) ;
		
		_project = project ;
		
		bind() ;
	}
	  
	@Override
	protected void onBind() 
	{			
	}
	
	public void connectToProject(AbsolutePanel baseLinePanel) 
	{			
		// Log.info("LdvBaseLinePresenter... inserting in the display") ;
		baseLinePanel.add(getDisplay().asWidget()) ;
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
