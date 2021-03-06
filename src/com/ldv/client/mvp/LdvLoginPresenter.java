package com.ldv.client.mvp ;

import net.customware.gwt.presenter.client.EventBus;
import net.customware.gwt.presenter.client.widget.WidgetDisplay;
import net.customware.gwt.presenter.client.widget.WidgetPresenter;

import com.ldv.client.event.IdentifiersProvidedEvent;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HasHTML;
import com.google.gwt.user.client.ui.HasText;
import com.google.inject.Inject;

public class LdvLoginPresenter extends WidgetPresenter<LdvLoginPresenter.Display>
{
	public interface Display extends WidgetDisplay 
	{
		HasText getTextToServer() ;

		HasHTML getServerResponse() ;

		HasClickHandlers getLogin() ;
		HasClickHandlers getCancel() ;
		
		String getIdentifier() ;
		String getPassword() ;

		DialogBox getDialogBox() ;
	}

	@Inject
	public LdvLoginPresenter(final Display display, final EventBus eventBus) 
	{
		super(display, eventBus) ;

		bind() ;
	}
	
	@Override
	protected void onBind()
	{
		// Add a handler to close the DialogBox
		display.getLogin().addClickHandler(new ClickHandler() 
		{
			public void onClick(final ClickEvent event) {
				eventBus.fireEvent(new IdentifiersProvidedEvent(display.getIdentifier(), display.getPassword()));
				display.getDialogBox().hide();

				// Not sure of a nice place to put these!
				// sendButton.setEnabled(true);
				// sendButton.setFocus(true);
			}
		});
		
	// Add a handler to close the DialogBox
		display.getCancel().addClickHandler(new ClickHandler() 
		{
			public void onClick(final ClickEvent event) {
				display.getDialogBox().hide();

				// Not sure of a nice place to put these!
//				sendButton.setEnabled(true);
//				sendButton.setFocus(true);
			}
		});

/*
		eventBus.addHandler(LdvSentEvent.TYPE, new LdvSentEventHandler()
			{
				@Override
				public void onLoginSent(final LdvSentEvent event) 
				{
					Log.info("Handling GreetingSent event");
				
					display.getTextToServer().setText(event.getSessionId());
					display.getServerResponse().setHTML(event.getMessage());
					display.getDialogBox().show();
				}
		});
*/
	}

	@Override
	protected void onUnbind() 
	{
		// Add unbind functionality here for more complex presenters.
	}

	public void refreshDisplay() 
	{
		// This is called when the presenter should pull the latest data
		// from the server, etc. In this case, there is nothing to do.
	}

	public void revealDisplay() 
	{
		// Nothing to do. This is more useful in UI which may be buried
		// in a tab bar, tree, etc.
	}

	@Override
	protected void onRevealDisplay() {
		// TODO Auto-generated method stub
		
	}
}
