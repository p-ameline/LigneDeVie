package com.ldv.client.mvp_toons_agenda;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.widgetideas.client.ResizableWidget;
import com.ldv.client.loc.LdvConstants;

public class AgendaAvailableView extends Composite implements ResizableWidget, AgendaAvailablePresenter.Display
{	
	private final LdvConstants    constants       = GWT.create(LdvConstants.class) ;
	
	private final FlowPanel _mainPanel ;
	
	private Label           _summaryLabel ;
	private Button          _editButton ;
	private Button          _deleteButton ;
	
	public AgendaAvailableView()
	{	
		super() ;
		
		_mainPanel = new FlowPanel() ;
		_mainPanel.addStyleName("AgendaDisplayAreaForAvailable") ;
		
		createElements() ;
				
		initWidget(_mainPanel) ;
	}	
	
	/**
	 * Remove all child widgets 
	 * 
	 */
	@Override
	public void clearAll() {
		_summaryLabel.setText("") ;
	}
			
	protected void createElements()
	{
		_editButton = new Button(constants.generalEdit()) ;
		_editButton.addStyleName("AgendaAvailableEditButton") ;
		_mainPanel.add(_editButton) ;
		
		_summaryLabel = new Label("") ;
		_summaryLabel.addStyleName("AgendaAvailableSummary") ;
		_mainPanel.add(_summaryLabel) ;
		
		_deleteButton = new Button(constants.generalDelete()) ;
		_deleteButton.addStyleName("AgendaAvailableDeleteButton") ;
		_mainPanel.add(_deleteButton) ;
	}
	
	public void setSummary(final String sSummary) {
		_summaryLabel.setText(sSummary) ;
	}
	
	@Override
	public FlowPanel getMainPanel() {
		return _mainPanel ;
	}
	
	@Override
	public HasClickHandlers getEditButton() {
		return _editButton ;
	}
	
	@Override
	public HasClickHandlers getDeleteButton() {
		return _deleteButton ;
	}
	
	@Override
	public Widget asWidget() {
		return this ;
	}

	@Override
	public void onResize(int width, int height) {
		// TODO Auto-generated method stub		
	}
}
