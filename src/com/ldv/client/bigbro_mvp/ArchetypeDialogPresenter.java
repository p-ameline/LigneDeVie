package com.ldv.client.bigbro_mvp;

import com.allen_sauer.gwt.log.client.Log;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.Panel;
import com.google.inject.Inject;

import com.ldv.client.bigbro.BBSmallBrother;
import com.ldv.client.event.ArchetypeDialogPresenterInitEvent;
import com.ldv.client.event.ArchetypeDialogPresenterInitEventHandler;
import com.ldv.client.event.SavePatPathoEvent;

import com.ldv.shared.graph.LdvModelNodeArray;
import com.ldv.shared.archetype.LdvArchetype;

import net.customware.gwt.dispatch.client.DispatchAsync;
import net.customware.gwt.presenter.client.EventBus;

/**
 * Presenter from the MVP model for an Archetype controlled dialog 
 * 
 * @author Philippe Ameline
 * 
 */
public class ArchetypeDialogPresenter extends ArchetypePresenterModel<ArchetypeDialogPresenter.Display>  
{
	public interface Display extends ArchetypePresenterModel.ArchetypeDisplayModel 
	{
		public void showArchetype() ;
		public void setZIndex(int iZIndex) ;
		
		public HasClickHandlers getOk() ;
		public HasClickHandlers getCancel() ;
		
		public void close() ;
	}
	
	protected final DispatchAsync     _dispatcher ;
	protected       LdvArchetype      _archetype ;
	protected final LdvModelNodeArray _patpatho ;
	
	@Inject
	public ArchetypeDialogPresenter(final Display       display, 
			                            final EventBus      eventBus,
			                            final DispatchAsync dispatcher) 
	{
		super(display, eventBus, dispatcher) ;
		
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
				connectButtons() ;
				display.showArchetype() ;
			}
		});
	}
	
	/**
	 * Once all controls have been created, it is time to connect a ClickHandler to the buttons among them  
	 */
	protected void connectButtons()
	{
		/**
		 * Reacts to Ok button in new basket dialog box
		 * */
		HasClickHandlers okButtonClickHandler = display.getOk() ;  
		
		if (null != okButtonClickHandler)
		{
			display.getOk().addClickHandler(new ClickHandler() {
				public void onClick(final ClickEvent event)
				{
					Log.debug("User click Ok") ;
					saveAndClose() ;
				}
			});
		}
		
		/**
		 * Reacts to Ok button in new basket dialog box
		 * */
		HasClickHandlers cancelButtonClickHandler = display.getCancel() ;  
		
		if (null != cancelButtonClickHandler)
		{
			display.getCancel().addClickHandler(new ClickHandler() {
				public void onClick(final ClickEvent event)
				{
					Log.debug("User click Cancel") ;
					if (_bbItem.canWeClose(true, false))
						display.close() ;
				}
			});
		}
	}
	
	protected void saveAndClose()
	{
		boolean bOkToClose = true ;
		
		if (null != _bbItem)
		{
			bOkToClose = _bbItem.canWeClose(true, true) ;
		
			if (bOkToClose)
			{
				boolean bRootArchetype = false ;
				BBSmallBrother bigBoss = _bbItem.getBigBoss() ;
				if ((null != bigBoss) && (_bbItem == bigBoss.getBBItem()))
					bRootArchetype = true ;
				
				if (bRootArchetype)
					eventBus.fireEvent(new SavePatPathoEvent(bigBoss)) ;
			}
		}
		
		if (bOkToClose)
			display.close() ;
	}
	
	protected void init(final ArchetypeDialogPresenterInitEvent event) 
	{
		if (event.getTargetArchetypePresenter() != this)
			return ;
		
		getDisplay().initialize() ;
		
		initComponents(event.getArchetype(), event.getSmallBrother().getBBItem(), event.getLang()) ;
		
		getDisplay().setZIndex(1005) ;
		getDisplay().showArchetype() ;
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
}
