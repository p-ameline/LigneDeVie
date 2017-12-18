package com.ldv.client.mvp;

import com.allen_sauer.gwt.log.client.Log;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasText;
import com.google.inject.Inject;

import com.ldv.client.util.LdvSupervisor;
import com.ldv.client.event.LdvValidatorSentEvent;
import com.ldv.client.event.LdvValidatorSentEventHandler;
import com.ldv.shared.rpc.LdvValidatorUserAction;
import com.ldv.shared.rpc.LdvValidatorUserResult;

import net.customware.gwt.dispatch.client.DispatchAsync;
import net.customware.gwt.presenter.client.EventBus;
import net.customware.gwt.presenter.client.widget.WidgetDisplay;
import net.customware.gwt.presenter.client.widget.WidgetPresenter;

public class LdvValidatorPresenter extends WidgetPresenter<LdvValidatorPresenter.Display> 
{
	public interface Display extends WidgetDisplay
	{	
		public HasClickHandlers getRegister() ;
		public HasText          getUserPassword() ;
		
		public void             showMessageBox(String sTextIndex) ;
		public void             hideMessageBox() ;
		public HasClickHandlers getMessageBoxButton() ;
	}

	private final DispatchAsync _dispatcher ;
	private       FlowPanel     _registerSpace ; // content of view
	private       String        _sId ;           // registration validation string
	
	@Inject
	public LdvValidatorPresenter(final Display display, 
							                 final EventBus eventBus,
							                 final DispatchAsync dispatcher) 
	{
		super(display, eventBus) ;
		
		_dispatcher = dispatcher ;

		bind() ;		
	}

	@Override
	protected void onBind() 
	{
		/**
		 * receive parameters from precedent presenter
		 * pass parameters to next presenter
		 * @param event : SimplePanel
		 *  
		 * */
		eventBus.addHandler(LdvValidatorSentEvent.TYPE, new LdvValidatorSentEventHandler() 
		{
			@Override
			public void onValidatorSend(LdvValidatorSentEvent event) 
			{
				Log.info("Handling ValidatorSent event");
				event.getWorkspace().clear();
				
				_sId           = event.getId() ;
				_registerSpace = (FlowPanel) event.getWorkspace() ;
				_registerSpace.add(getDisplay().asWidget()) ;
			}
		});
		
		// Register button handler
		//
		display.getRegister().addClickHandler(new ClickHandler()
		{
			public void onClick(final ClickEvent event){
				createUser() ;
			}
		});
		
		// Message dialog Ok button handler
		//
		display.getMessageBoxButton().addClickHandler(new ClickHandler()
		{
			public void onClick(final ClickEvent event){
				display.hideMessageBox() ;
			}
		});
	}
		
	private void createUser()
	{	
		// Should never happen - security
		if (null == display.getUserPassword())
			return ;
		
		String sPass = display.getUserPassword().getText() ;
		if (sPass.equals(""))
		{
			display.showMessageBox("EmptyPassword") ;
			return ;
		}
		
		_dispatcher.execute(new LdvValidatorUserAction(_sId, LdvSupervisor.MD5(sPass), LdvSupervisor.leftToRightMD5(sPass)), new UserCreatedCallback()) ;
	}

	protected class UserCreatedCallback implements AsyncCallback<LdvValidatorUserResult> 
	{
		public UserCreatedCallback() {
			super() ;
		}

		@Override
		public void onFailure(Throwable cause) {
			Log.error("Unhandled error", cause);
		}

		@Override
		public void onSuccess(LdvValidatorUserResult value) 
		{
			if (value.wasSuccessful())
			{
				
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
