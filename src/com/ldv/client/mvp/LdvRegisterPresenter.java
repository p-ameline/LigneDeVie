package com.ldv.client.mvp;

import java.util.ArrayList;
import java.util.Iterator;

import com.allen_sauer.gwt.log.client.Log;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.inject.Inject;

import com.ldv.client.event.LdvRegisterSentEvent;
import com.ldv.client.event.LdvRegisterSentEventHandler;
import com.ldv.client.util.LdvSupervisor;
import com.ldv.shared.rpc.Language;
import com.ldv.shared.rpc.LdvGetLanguagesAction;
import com.ldv.shared.rpc.LdvGetLanguagesResult;
import com.ldv.shared.rpc.LdvRegisterUserAction;
import com.ldv.shared.rpc.LdvRegisterUserResult;
import com.ldv.client.event.BackToWelcomePageEvent;

import net.customware.gwt.dispatch.client.DispatchAsync;
import net.customware.gwt.presenter.client.EventBus;

public class LdvRegisterPresenter extends UserEditPresenter<LdvRegisterPresenter.Display> 
{
	public interface Display extends UserEditPresenter.DisplayModel
	{
		HasText          getUserPassword() ;
		HasText          getUserConfirmedPassword() ;
		// HasText          getUserEmail() ;
		HasClickHandlers getSubmit() ;
		HasClickHandlers getReset() ;
		
		TextBox          getPseudo() ;
		void             setPseudo(String sPseudo) ;
		PasswordTextBox  getPassword () ;
		void             setPassword(String sPass) ;
		PasswordTextBox  getConfirmedPassword() ;
		TextBox          getEmail() ;
		TextBox          getConfirmedEmail() ;
		
		String           getLanguage() ;
		void             addLanguage(String sLanguage) ;
				
		Button           getSubmitButton() ;
		
		//Methods to obtain Messages
		HasText          getPasswordMsg() ;
		HasText          getConfirmedMsg() ;
		HasText          getEmailMsg() ;
		
		// Error messages management
		void             emptyConfirmedMsg() ;
		void             setConfirmedMsgAreDifferent() ;
		void             emptyEmailMsg() ;
		void             setEmailMsgInvalid() ;
		void             emptyConfirmedMailMsg() ;
		void             setConfirmedMailsAreDifferent() ;
		void             emptyPseudoMsg() ;
		void             setPseudoMsgInvalid() ;
		void             setPseudoCheckError() ;
		void             setPseudoMsgNotAvailable() ;
		void             emptyPasswordMsg() ;
		void             setPasswordMsgInvalid() ;
		
		DialogBox        getMessageDialogBox() ;
		Button           getMessageDialogBoxOkButton() ;
	}

	private final DispatchAsync       _dispatcher ;
	private final LdvSupervisor       _supervisor ;
	private       FlowPanel           _registerSpace ; //content of view
	private       ArrayList<Language> _aLanguages = new ArrayList<Language>() ;
	
	@Inject
	public LdvRegisterPresenter(final Display display, 
							                final EventBus eventBus,
							                final DispatchAsync dispatcher,
							                final LdvSupervisor supervisor) 
	{
		super(display, eventBus, dispatcher) ;
		
		_dispatcher       = dispatcher ;
		_supervisor       = supervisor ;
		_bAvailablePseudo = false ;

		bind() ;
		
		// Disable Submit button
		//
		display.getSubmitButton().setEnabled(false) ;
		
		// Get languages list
		//
		_dispatcher.execute(new LdvGetLanguagesAction(), new GetLanguagesCallback()) ;
	}

	@Override
	protected void onBind() 
	{
		display.setPseudo(_supervisor.getUserPseudo()) ;
		display.setPassword(_supervisor.getUserPlainTextPassword()) ;

		managePseudoValidity() ;
		
		super.onBind() ;
		
		/**
		 * receive parameters from precedent presenter
		 * pass parameters to next presenter
		 * @param event : SimplePanel
		 *  
		 * */
		eventBus.addHandler(LdvRegisterSentEvent.TYPE, new LdvRegisterSentEventHandler() 
		{
			@Override
			public void onRegisterSend(LdvRegisterSentEvent event) 
			{
				Log.info("Handling RegisterSent event");
				event.getWorkspace().clear();
				 
				_registerSpace = (FlowPanel) event.getWorkspace() ;
				_registerSpace.add(getDisplay().asWidget()) ;
			}
		});
								
		/**
		 * submit user registration information
		 * */
		display.getSubmit().addClickHandler(new SubmitHandler()) ; 
		
		/**
		 * Ok button pressed in the message dialog box
		 * */
		display.getMessageDialogBoxOkButton().addClickHandler(new ClickHandler(){
			public void onClick(final ClickEvent event)
			{
				display.getMessageDialogBox().hide() ;
				eventBus.fireEvent(new BackToWelcomePageEvent()) ;
			}
		});

	}//end onBind()
						
	private class SubmitHandler implements ClickHandler
	{
		@Override
		public void onClick(ClickEvent event) 
		{
			doCreate(display.getPseudo().getText(),
					     display.getUserPassword().getText(),
					     display.getEmail().getText(),
					     GetLanguageIdFromLabel(display.getLanguage())) ;
		}
	} 

	private void doCreate(String sPseudo,
						            String sPassword,
						            String sEmail,
						            String sLanguage) 
	{
		Log.info("Calling doCreate");	
		Log.debug("doCreate(): RegisterPresenter");
		
		_dispatcher.execute(new LdvRegisterUserAction(sPseudo,
				                                          LdvSupervisor.MD5(sPassword), 
				                                          sEmail,
				                                          sLanguage), new RegisterUserCallback()) ;
	}
	
	protected class RegisterUserCallback implements AsyncCallback<LdvRegisterUserResult> 
	{
		public RegisterUserCallback() {
			super() ;
		}

		@Override
		public void onFailure(Throwable cause) {
			Log.error("Unhandled error", cause);
		}

		@Override
		public void onSuccess(LdvRegisterUserResult value) {
			// No user found
			if (false == value.wasSuccessful()) {
				Log.error("No user created") ;
			}
			else 
			{
				Log.info("Registration Success") ;
				// eventBus.fireEvent(new LdvCreateSentEvent(value.getLink(), _registerSpace)) ;
				// _registerSpace.clear() ;
				
				display.getMessageDialogBox().show() ;
			}
		}
	} 
	
	protected String GetLanguageIdFromLabel(String sLabel)
	{
		if ((null == sLabel) || (_aLanguages.isEmpty()))
			return "" ;
			
		Iterator<Language> it = _aLanguages.iterator() ;
		for ( ; it.hasNext() ; )
		{
			Language lang = it.next() ;
			if (sLabel.equals(lang.getLabel()))
				return lang.getIsoCode() ;
		}
		
		return "" ;
	}
	
	protected class GetLanguagesCallback implements AsyncCallback<LdvGetLanguagesResult> 
	{
		public GetLanguagesCallback() {
			super() ;
		}

		@Override
		public void onFailure(Throwable cause) {
			Log.error("Unhandled error", cause.getMessage()) ;
		}

		@Override
		public void onSuccess(LdvGetLanguagesResult value) 
		{
			if (value.isEmpty())
				return ;
			
			Iterator<Language> it = value.getLanguages().iterator() ;
			for ( ; it.hasNext() ; )
			{
				Language lang = it.next() ;
				_aLanguages.add(new Language(lang)) ;
				display.addLanguage(lang.getLabel()) ;
			}
		}
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
