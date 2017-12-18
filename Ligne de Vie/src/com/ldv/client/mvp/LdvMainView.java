package com.ldv.client.mvp;

import net.customware.gwt.presenter.client.widget.WidgetDisplay;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class LdvMainView extends Composite implements LdvMainPresenter.Display {
	
	private FlowPanel _body ;
	private FlowPanel _header ;
	private FlowPanel _workspace ;
	private FlowPanel _footer ;

	public LdvMainView() 
	{
		_body = new FlowPanel() ;
		_body.addStyleName("ldvMainPanel") ;
		initHeader() ; 		
		initWorkspace() ;
		initMainFooter() ; 
		initWidget(_body) ;
	}	
	
	public void initHeader()
	{
		_header = new FlowPanel() ;
		_header.add(new Label("header")) ;
		_header.addStyleName("ldvHeaderPanel") ;
		_body.add(_header) ;
	}
		 	 
	public void initWorkspace()
	{
		_workspace = new FlowPanel();
		_workspace.addStyleName("workspace");
		_workspace.add(new Label("workspace"));
		_body.add(_workspace) ;
	}
		
	public void initMainFooter()
	{
		HTML footerContent = new HTML("Ldv version 0.02.0000") ;
		_footer = new FlowPanel() ;
		_footer.addStyleName("ldvFooterPanel") ;
		_footer.add(footerContent) ;
		_body.add(_footer) ;
	}
		
	@Override
	public FlowPanel getHeader() {
		return _header ;
	}
	@Override
	public FlowPanel getWorkspace() {
		return _workspace ;
	}
	@Override
	public FlowPanel getFooter() {
		return _footer ;
	}
	
	@Override
	public void setBlocksPosition(int iWindowHeight)
	{
		int iHeaderHeight = _header.getOffsetHeight() ;
		
		int iFooterHeight = _footer.getOffsetHeight() ;
		DOM.setStyleAttribute(_footer.getElement(), "bottom", "0 px") ;
		
		int iWorkspaceHeight = iWindowHeight - iHeaderHeight - iFooterHeight ;
		if (iWorkspaceHeight > 0)
			_workspace.setHeight("" + iWorkspaceHeight + " px") ;
	}

	public void reset() {
		// Focus the cursor on the name field when the app loads
	}

	/**
	 * Returns this widget as the {@link WidgetDisplay#asWidget()} value.
	 */
	public Widget asWidget() {
		return this;
	}
}
