package com.ldv.client.bigbro_mvp;

import com.allen_sauer.gwt.log.client.Log;
import com.google.inject.Inject;

import com.ldv.client.event.ArchetypeDialogPresenterInitEvent;
import com.ldv.client.event.ArchetypeDialogPresenterInitEventHandler;

import com.ldv.shared.graph.LdvModelNodeArray;
import com.ldv.shared.archetype.LdvArchetype;

import net.customware.gwt.dispatch.client.DispatchAsync;
import net.customware.gwt.presenter.client.EventBus;
import net.customware.gwt.presenter.client.widget.WidgetDisplay;
import net.customware.gwt.presenter.client.widget.WidgetPresenter;

/**
 * Component that control a set of description dialogs
 * 
 * @author Philippe Ameline
 * 
 */
public class MultipleDialogPresenter extends WidgetPresenter<MultipleDialogPresenter.Display>  
{
	public interface Display extends WidgetDisplay 
	{
		public void show() ;
	}
	
	protected final DispatchAsync     _dispatcher ;
	protected       LdvArchetype      _archetype ;
	protected final LdvModelNodeArray _patpatho ;
	
	@Inject
	public MultipleDialogPresenter(final Display       display, 
			                           final EventBus      eventBus,
			                           final DispatchAsync dispatcher) 
	{
		super(display, eventBus) ;
		
		_dispatcher = dispatcher ;
		_archetype  = null ;
		_patpatho   = null ;
		
		Log.debug("ArchetypeDialogPresenter constructor") ;
		
		bind() ;
	}
	
	@Override
	protected void onBind() 
	{
		Log.debug("ArchetypeDialogPresenterInitEvent entering onBind") ;
		
		eventBus.addHandler(ArchetypeDialogPresenterInitEvent.TYPE, new ArchetypeDialogPresenterInitEventHandler() 
		{
			@Override
			public void onInitArchetypePresenter(ArchetypeDialogPresenterInitEvent event) 
			{
				Log.debug("Handling ArchetypeDialogPresenterInitEvent event") ;
				init(event) ;
			}
		});
	}    
	
	protected void init(final ArchetypeDialogPresenterInitEvent event) 
	{
		
	}
	
	public void show() {
		display.show() ;
	}
	
	@Override
	protected void onUnbind()
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onRevealDisplay()
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void revealDisplay() {
		// TODO Auto-generated method stub
		
	}
}
