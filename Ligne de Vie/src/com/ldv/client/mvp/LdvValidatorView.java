package com.ldv.client.mvp;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.Widget;
import com.ldv.client.loc.LdvConstants;

public class LdvValidatorView extends Composite implements LdvValidatorPresenter.Display
{
	private final LdvConstants constants = GWT.create(LdvConstants.class) ;
	
	private final FlowPanel       _mainPanel ;
	
	private final PasswordTextBox _passwordBox ;
	private final Button          _loginButton ;
	
	private final Label           _WelcomeLabel ;
	private final Label           _PleaseWaitLabel ;
	
	// Message Dialog box
	//
	private DialogBox             _MessageDialogBox ;
	private final Button          _MessageDialogBoxOkButton ;
	
	public LdvValidatorView()
	{
		_mainPanel   = new FlowPanel() ;
		_mainPanel.addStyleName("validationMainPanel") ;
		
		_passwordBox = new PasswordTextBox() ;
		_passwordBox.addStyleName("loginPassText") ;
		
		_loginButton = new Button(constants.loginBtn()) ;
		_loginButton.addStyleName("loginButton") ;
		
		// init all labels
		//
		_WelcomeLabel    = new Label(constants.generalNameFirst()) ;
		_WelcomeLabel.addStyleName("validationMainLabel") ;
		_PleaseWaitLabel = new Label(constants.generalNameSecond()) ;
		_PleaseWaitLabel.addStyleName("validationSubLabel") ;

		initValidatorBody() ;
		
		_mainPanel.add(_loginButton) ;
		
		initWidget(_mainPanel) ;
		
		_MessageDialogBox = new DialogBox() ;
		_MessageDialogBox.setSize("25em", "10em") ;
		_MessageDialogBox.setPopupPosition(800, 200) ;
		_MessageDialogBox.setText("-------------") ;
		_MessageDialogBox.setAnimationEnabled(true) ;
		
		_MessageDialogBoxOkButton = new Button(constants.generalOk()) ;
		_loginButton.addStyleName("messageDialogBoxOkButton") ;
		
		_MessageDialogBox.add(_MessageDialogBoxOkButton) ;
	}
		
	public void initValidatorBody()
	{
		Log.info("entering initValidatorBody()") ;
		
		// Right side: login
		//
		FlowPanel loginPannel = new FlowPanel() ;
		loginPannel.addStyleName("loginPanel") ;
	
		Label labelPass = new Label(constants.loginPassword()) ;
		labelPass.addStyleName("loginPassLabel") ;
		loginPannel.add(labelPass) ;
		loginPannel.add(_passwordBox) ;
		
		_mainPanel.add(loginPannel) ;
		
		// _VPanel.add(_WelcomeLabel) ;
		// _VPanel.add(_PleaseWaitLabel) ;
	}

	@Override
	public HasClickHandlers getRegister() {
		return _loginButton ;
	}
	
	@Override
	public HasText getUserPassword() {
		return _passwordBox ;
	}
	
	@Override
	public void showMessageBox(String sTextIndex) {
		_MessageDialogBox.setText("-------------") ;
		_MessageDialogBox.show() ;
	}
	
	public void hideMessageBox() {
		_MessageDialogBox.hide() ;
	}
	
	public HasClickHandlers getMessageBoxButton() {
		return _MessageDialogBoxOkButton ;
	}
	
	@Override
  public Widget asWidget()
  {
		return this ;
  }
}
