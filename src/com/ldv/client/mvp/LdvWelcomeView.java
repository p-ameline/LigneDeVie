package com.ldv.client.mvp ;

import net.customware.gwt.presenter.client.widget.WidgetDisplay;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.ldv.client.loc.LdvConstants;

//
// View component for the MVP model for Main Page
//

public class LdvWelcomeView extends Composite implements LdvWelcomePresenter.Display 
{
	private final LdvConstants constants = GWT.create(LdvConstants.class) ;
	
	// private final TextBox _name ;
		
	private final TextBox         _identifierBox ;
	private final PasswordTextBox _passwordBox ;
	private final Button          _loginButton ;
	private final Button          _registerButton ;
	private final Button          _disconnectButton ;
	private final FlowPanel       _loginPanel ;

	private final FlowPanel       _mainPanel ;
	private final FlowPanel       _bodyPanel ;
	
	public LdvWelcomeView() 
	{
		_identifierBox    = new TextBox() ;
		_identifierBox.addStyleName("loginIdText") ;
		_passwordBox      = new PasswordTextBox() ;
		_passwordBox.addStyleName("loginPassText") ;
		
		_loginButton      = new Button(constants.loginBtn()) ;
		_loginButton.addStyleName("loginButton") ;
		
		_registerButton   = new Button(constants.registerBtn()) ;
		_registerButton.addStyleName("registerButton") ;
		
		_disconnectButton = new Button("Disconnect") ;
		_loginPanel       = new FlowPanel() ;
		_loginPanel.addStyleName("loginTable") ;
		
		_mainPanel = new FlowPanel() ;
		_mainPanel.addStyleName("ldvMainPanel") ;
		
		_bodyPanel = new FlowPanel() ;
		_bodyPanel.setStyleName("ldvBodyPanel") ;
		
		initWelcomePage() ;
				
		// initWidget(_mainPanel) ;
		initWidget(_bodyPanel) ;

		reset() ;
	}

/*
	public HasValue<String> getName() 
	{
		return _name;
	}
*/
	
	public void reset() 
	{
		// Focus the cursor on the identifier field when app loads
		_identifierBox.setFocus(true);
		_identifierBox.selectAll();
	}
	
	public void initWelcomePage() 
	{
		_mainPanel.clear() ;
		
		// initWelcomeHeader() ;
		initWelcomeBody() ;
		// initWelcomeFooter() ;
	}
	
/*
	private void initWelcomeHeader() 
	{
		SimplePanel headerPanel = new SimplePanel() ;
		headerPanel.setStyleName("ldvHeaderPanel") ;
		
		HTML headerContent = new HTML("Ligne de vie") ;
		headerContent.setStyleName("ldvHeaderTitle") ;
		headerPanel.add(headerContent) ;
		
		_mainPanel.add(headerPanel) ;
	}
*/
	
	private void initWelcomeBody() 
	{
		// Log.info("entering initWelcomeBody()") ;
		// DockLayoutPanel bodyPannel = new DockLayoutPanel(Unit.EM) ;
				
		// Left side: 
		//
		HTML descriptorContent = new HTML("") ;
		SimplePanel videoPannel = new SimplePanel() ;
		videoPannel.addStyleName("ldvVideoArea") ;
		videoPannel.add(descriptorContent) ;
		
		_bodyPanel.add(videoPannel) ;
		
		// Right side
		//
		FlowPanel vPannel = new FlowPanel() ;
		vPannel.setStyleName("bodyRightPanel") ;
		
		// Right side: login
		//
		FlowPanel loginPannel = new FlowPanel() ;
		loginPannel.addStyleName("loginPanel") ;
		
		_loginButton.getElement().setId("loginButton") ;
		_identifierBox.getElement().setId("identifierBox") ;
		_passwordBox.getElement().setId("passwordBox") ;
		
		// _loginTable.setWidth("10%") ;
		
		FlowPanel loginIdPannel = new FlowPanel() ;
		loginIdPannel.addStyleName("loginBlock") ;
		Label labelId = new Label(constants.loginIdentifier()) ;
		labelId.addStyleName("loginIdLabel") ;
		loginIdPannel.add(labelId) ;
		loginIdPannel.add(_identifierBox) ;
		_loginPanel.add(loginIdPannel) ;
		
		FlowPanel loginPassPannel = new FlowPanel() ;
		loginPassPannel.addStyleName("loginBlock") ;
		Label labelPass = new Label(constants.loginPassword()) ;
		labelPass.addStyleName("loginPassLabel") ;
		loginPassPannel.add(labelPass) ;
		loginPassPannel.add(_passwordBox) ;
		_loginPanel.add(loginPassPannel) ;
		
		_loginPanel.add(_loginButton) ;
		
		loginPannel.add(_loginPanel) ;
		
		vPannel.add(loginPannel) ;
		
		// Right side: register
		//
		FlowPanel registerPannel = new FlowPanel() ;
		registerPannel.addStyleName("registerPanel") ;
		
		Label registerLabel = new Label(constants.registerText()) ;
		registerLabel.addStyleName("registerLabel") ;
		
		registerPannel.add(registerLabel) ;
		registerPannel.add(_registerButton) ;
		
		vPannel.add(registerPannel) ;
		
		_bodyPanel.add(vPannel) ;
		
		// _mainPanel.add(_bodyPanel) ;
	}
	
/*
	private void initWelcomeFooter() 
	{
		HTML footerContent = new HTML("footer") ;
		footerContent.setStyleName("ldvFooterPanel") ;
		_mainPanel.add(footerContent) ;
	}
*/
	
	public void initWorkingPage() 
	{
		_mainPanel.clear() ;
		
		initWorkingHeader() ;
		initWorkingBody() ;
		initWorkingFooter() ;
	}
	
	private void initWorkingHeader() 
	{
		HorizontalPanel headerHPanel = new HorizontalPanel() ;
		HTML headerContent = new HTML("<h1 id=\"logo\">Ligne de vie</h1>") ;
		headerHPanel.add(headerContent) ;
		_disconnectButton.getElement().setId("disconnectButton") ;
		headerHPanel.add(_disconnectButton) ;
		_mainPanel.add(headerHPanel) ;
	}
	
	private void initWorkingBody() 
	{
		Log.info("entering initWorkingBody()") ;
		DockLayoutPanel bodyPannel = new DockLayoutPanel(Unit.EM) ;
		HTML descriptorContent = new HTML("Video area") ;
		bodyPannel.addWest(descriptorContent, 50) ;
				
		_mainPanel.add(bodyPannel) ;
	}
	
	private void initWorkingFooter() 
	{
		HTML footerContent = new HTML("footer") ;
		_mainPanel.add(footerContent) ;
	}
	
	@Override
	public String getIdentifier() {
		return _identifierBox.getText() ;
	}
	
	@Override
	public String getPassword() {
		return _passwordBox.getText() ;
	}
	
	@Override
	public FlowPanel getMainPannel() {
		return _mainPanel ;
	}
	
	@Override
	public FlowPanel getBodyPannel() {
		return _bodyPanel ;
	}

	@Override
	public HasClickHandlers getSend() {
		return _loginButton ;
	}
	
	@Override
	public HasClickHandlers getDisconnect() {
		return _disconnectButton ;
	}
	
	@Override
	public HasClickHandlers getRegister() {
		return _registerButton ;
	}
	
	/**
	 * Returns this widget as the {@link WidgetDisplay#asWidget()} value.
	 */
	public Widget asWidget() {
		return this ;
	}
}
