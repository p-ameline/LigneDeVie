package com.ldv.client.mvp ;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.ldv.client.loc.LdvConstants;
import com.ldv.shared.model.LdvStatus;

//
// View component for the MVP model for Main Page
//

public class LdvManagementView implements LdvManagementPresenter.Display 
{
	private final LdvConstants constants = GWT.create(LdvConstants.class) ;
	
	// private final TextBox _name ;
		
	private FlowPanel _header ;
	private FlowPanel _workspace ;
	
	private FlowPanel _statusPanel ;
	private FlowPanel _onlineStatusPanel ;
	private FlowPanel _iconStatusPanel ;
	private FlowPanel _openStatusPanel ;
	
	private FlowPanel _currentPanel ;
	private FlowPanel _backwardPanel ;
	private FlowPanel _forwardPanel ;
	
	private Button    _removeButton ;
	private Button    _downloadButton ;
	private Button    _openButton ;
	private Button    _closeButton ;
	private Button    _uploadButton ;
	
	public LdvManagementView() 
	{
		_header        = null ;
		_workspace     = null ;
		
		_statusPanel       = null ;
		_onlineStatusPanel = null ;
		_iconStatusPanel   = null ;
		_openStatusPanel   = null ;
		_currentPanel      = null ;
		_backwardPanel     = null ;
		_forwardPanel      = null ;
		
		initButtons() ;
	}

	/**
	 * Create buttons objects so that presenter's binding functions won't crash
	 */
	public void initButtons()
	{
		_removeButton = new Button(constants.managementDestroy()) ;
		_removeButton.addStyleName("bigbutton button red") ;
		_closeButton = new Button(constants.managementClose()) ;
		_closeButton.addStyleName("bigbutton button red") ;
		
		_downloadButton = new Button(constants.managementDownload()) ;
		_downloadButton.addStyleName("smallbutton button white") ;
		
		_uploadButton = new Button(constants.managementUpload()) ;
		_uploadButton.addStyleName("bigbutton button green") ;
		_openButton = new Button(constants.managementOpen()) ;
		_openButton.addStyleName("bigbutton button green") ;
	}
	
	@Override
	public void initPanels(FlowPanel workspace, FlowPanel header)
	{
		_header    = header ;
		_workspace = workspace ;
		
		if (null != _header)
			_header.clear() ;
		
		if (null != _workspace)
		{
			_workspace.clear() ;
			
			_statusPanel = new FlowPanel() ;
			_statusPanel.addStyleName("statusManagementPanel") ;
			
			_onlineStatusPanel = new FlowPanel() ;
			_onlineStatusPanel.addStyleName("onLineStatusPanel") ;
			_iconStatusPanel   = new FlowPanel() ;
			_iconStatusPanel.addStyleName("iconStatusPanel") ;
			_openStatusPanel   = new FlowPanel() ;
			_openStatusPanel.addStyleName("openStatusPanel") ;
			_statusPanel.add(_onlineStatusPanel) ;
			_statusPanel.add(_iconStatusPanel) ;
			_statusPanel.add(_openStatusPanel) ;
			
			_backwardPanel = new FlowPanel() ;
			_backwardPanel.addStyleName("backwardManagementPanel") ;
			_currentPanel = new FlowPanel() ;
			_currentPanel.addStyleName("actualManagementPanel") ;
			_forwardPanel = new FlowPanel() ;
			_forwardPanel.addStyleName("forwardManagementPanel") ;
			
			_workspace.add(_statusPanel) ;
			_workspace.add(_backwardPanel) ;
			_workspace.add(_currentPanel) ;
			_workspace.add(_forwardPanel) ;
		}
	}
	
	@Override
	public void initStatus(LdvStatus.AvailabilityLevel status)
	{
		if (null == _statusPanel)
			return ;
		
		if (LdvStatus.AvailabilityLevel.OPEN == status)
			initWorkspaceForOpen() ;
		else if (LdvStatus.AvailabilityLevel.HERE == status)
			initWorkspaceForHere() ;
		else if (LdvStatus.AvailabilityLevel.OUT == status)
		{
			HTML logo = new HTML("<img src=\"cross_48.png\">") ;
			logo.addStyleName("statusLogo") ;
			_statusPanel.add(logo) ;
		}
	}
	
	/**
	 * Initialize workspace in the situation where Ldv is there but closed
	 */
	void initWorkspaceForHere()
	{
		_onlineStatusPanel.clear() ;
		_iconStatusPanel.clear() ;
		_openStatusPanel.clear() ;
		
		HTML logo = new HTML("<img src=\"lock_48.png\">") ;
		logo.addStyleName("statusLogo") ;
		_iconStatusPanel.add(logo) ;
		
		Label labelOpenStatus = new Label(constants.statusOpenClosed()) ;
		labelOpenStatus.addStyleName("openStatusText") ;
		_openStatusPanel.add(labelOpenStatus) ;
		
		Label labelOnlineStatus = new Label(constants.statusOnlineOnline()) ;
		labelOnlineStatus.addStyleName("onlineStatusText") ;
		_onlineStatusPanel.add(labelOnlineStatus) ;
		
		_backwardPanel.clear() ;
		_backwardPanel.add(_removeButton) ;
		
		_currentPanel.clear() ;
		_currentPanel.add(_downloadButton) ;
		
		_forwardPanel.clear() ;
		_forwardPanel.add(_openButton) ;
	}
	
	/**
	 * Initialize workspace in the situation where Ldv is there and open
	 */
	void initWorkspaceForOpen()
	{
		_onlineStatusPanel.clear() ;
		_iconStatusPanel.clear() ;
		_openStatusPanel.clear() ;
		
		HTML logo = new HTML("<img src=\"lock_open_48.png\">") ;
		logo.addStyleName("statusLogo") ;
		_iconStatusPanel.add(logo) ;
		
		Label labelOpenStatus = new Label(constants.statusOpenOpen()) ;
		labelOpenStatus.addStyleName("openStatusText") ;
		_openStatusPanel.add(labelOpenStatus) ;
		
		Label labelOnlineStatus = new Label(constants.statusOnlineOnline()) ;
		labelOnlineStatus.addStyleName("onlineStatusText") ;
		_onlineStatusPanel.add(labelOnlineStatus) ;
		
		_backwardPanel.clear() ;
		_backwardPanel.add(_closeButton) ;
		
		_currentPanel.clear() ;
		
		_forwardPanel.clear() ;
	}
	
	@Override
	public HasClickHandlers getRemove() 
	{
		return _removeButton ;
	}
	
	@Override
	public HasClickHandlers getDownload() 
	{
		return _downloadButton ;
	}
	
	@Override
	public HasClickHandlers getOpen() 
	{
		return _openButton ;
	}
	
	@Override
	public HasClickHandlers getClose() 
	{
		return _closeButton ;
	}
	
	@Override
	public HasClickHandlers getUpload() 
	{
		return _uploadButton ;
	}
	
	@Override
	public Widget asWidget()
	{
		return null ;
	}
}
