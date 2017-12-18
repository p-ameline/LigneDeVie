package com.ldv.client.mvp ;

import net.customware.gwt.presenter.client.widget.WidgetDisplay;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHTML;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class LdvLoginView extends DialogBox implements LdvLoginPresenter.Display
{
	private final Label   _textToServerLabel ;
	private final HTML    _serverResponseLabel ;
	private final TextBox _identifierBox ;
	private final TextBox _passwordBox ;
	private final Button  _loginButton ;
	private final Button  _cancelButton ;

	public LdvLoginView() 
	{
		Log.info("entering loginView()") ;
		
		setText("Identify yourself");
		setAnimationEnabled(true);

		_identifierBox = new TextBox();
		_passwordBox   = new TextBox();
		_loginButton   = new Button("Login") ;
		_cancelButton  = new Button("Cancel") ;

		// We can set the id of a widget by accessing its Element
		_loginButton.getElement().setId("loginButton");
		_cancelButton.getElement().setId("cancelButton");
		_identifierBox.getElement().setId("identifierBox");
		_passwordBox.getElement().setId("passwordBox");

		_textToServerLabel = new Label() ;
		_serverResponseLabel = new HTML() ;
/*
		final VerticalPanel dialogVPanel = new VerticalPanel();

		dialogVPanel.addStyleName("dialogVPanel");
		dialogVPanel.add(new Label("Identifier?"));
		dialogVPanel.add(_identifierBox);
		dialogVPanel.add(new Label("Password?"));
		dialogVPanel.add(_passwordBox);
		dialogVPanel.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
		dialogVPanel.add(_loginButton);
		dialogVPanel.add(_cancelButton);

		setWidget(dialogVPanel) ;
*/
	}

	public String getIdentifier() 
	{
		return _identifierBox.getText() ;
	}
	
	public String getPassword() 
	{
		return _passwordBox.getText() ;
	}
	
	public HasText getTextToServer() 
	{
		return _textToServerLabel ;
	}

	public HasHTML getServerResponse() 
	{
		return _serverResponseLabel ;
	}

	public HasClickHandlers getLogin() 
	{
		return _loginButton ;
	}
	
	public HasClickHandlers getCancel() 
	{
		return _cancelButton ;
	}

	public DialogBox getDialogBox() 
	{
		return this ;
	}

	/**
	 * Returns this widget as the {@link WidgetDisplay#asWidget()} value.
	 */
	public Widget asWidget() 
	{
		return this ;
	}
}