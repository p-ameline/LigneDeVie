package com.ldv.client.mvp_toons;

import com.allen_sauer.gwt.log.client.Log;

import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Image;
import com.google.inject.Inject;

import com.ldv.client.event.LdvDocumentInitEvent;
import com.ldv.client.event.LdvDocumentInitEventHandler;
import com.ldv.client.model.LdvModelDocument;
import com.ldv.shared.model.LdvTime;

import net.customware.gwt.presenter.client.EventBus;
import net.customware.gwt.presenter.client.widget.WidgetDisplay;
import net.customware.gwt.presenter.client.widget.WidgetPresenter;

public class LdvDocumentPresenter extends WidgetPresenter<LdvDocumentPresenter.Display>
{
	public interface Display extends WidgetDisplay 
	{	
		public Image getIcon() ;
		public void setIconPosition(int position) ;
		public void showDocTip(String title) ;
		public void hideDocTip() ;
	}
	
	private AbsolutePanel           _linePanel ;
	private LdvConcernLinePresenter _concernline ;
	private LdvModelDocument        _document;
	private LdvTime                 _startTime ;
	private String                  _title ;
	private int                     _startPosition ;
	private int                     _rightPosition ;

	protected LdvConcernLinePresenter _line ;
		
	@Inject
	public LdvDocumentPresenter(final Display display, EventBus eventBus) 
	{	
		super(display, eventBus);	
		bind();
		Log.info("entering constructor of LdvDocumentPresenter.");
	}
	  
	public void connectToLine(LdvDocumentInitEvent event) 
	{			
		if (event.getTarget() != this)
		{	
			return ;
		}
		
		_concernline = event.getFather() ;
		_document = event.getModel() ;
		_startTime = _document.getBeginDate() ;
		_startPosition = this.getLinePosition(_startTime) ;
		
		//_rightPosition = this._concernline.getDisplay().getMainPanel().getOffsetWidth() - _startPosition ;
		
		_title = _document.getTitle() ;
		
		display.setIconPosition(_startPosition) ;
		
		_linePanel = (AbsolutePanel) event.getLinePanel() ;
		_linePanel.add(getDisplay().asWidget()) ;		
	}

	public int getLinePosition(LdvTime time){
		return this._concernline.getBoxPosition(time) ;
	}
	
	@Override
	protected void onBind() 
	{					
		display.getIcon().addMouseOverHandler(new MouseOverHandler(){		
			@Override
			public void onMouseOver(MouseOverEvent event)
			{
				display.showDocTip(_title) ;
			}
		});
		
		
		//we have problems here, the event of "MouseOutEvent" doesn't work!
		//
		display.getIcon().addMouseOutHandler(new MouseOutHandler(){			
			@Override
			public void onMouseOut(MouseOutEvent event)
			{
				display.hideDocTip() ;
			}
		});
		
		eventBus.addHandler(LdvDocumentInitEvent.TYPE, new LdvDocumentInitEventHandler() 
		{
			@Override
			public void onInitSend(LdvDocumentInitEvent event) 
			{
				Log.info("Handling LdvDocumentInitEvent event");
				connectToLine(event) ;
			}
		});
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
