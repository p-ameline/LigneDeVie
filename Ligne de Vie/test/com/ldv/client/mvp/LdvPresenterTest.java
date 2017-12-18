package com.ldv.client.mvp;

import net.customware.gwt.dispatch.client.DispatchAsync;
import net.customware.gwt.presenter.client.EventBus;
import net.customware.gwt.presenter.client.place.PlaceRequestEvent;

import org.junit.Before;
import org.junit.Test;

import com.ldv.client.event.IdentifiersProvidedEvent;
import com.ldv.client.event.LdvOpenEvent;
import com.ldv.client.event.LdvSentEvent;
import com.ldv.shared.rpc.SendLoginAction;
import com.ldv.test.util.MvpTestHelper;
import com.ldv.test.util.SendLoginActionMatcher;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.inject.Guice;
import com.google.inject.Injector;

import static org.easymock.EasyMock.*;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.resetToStrict;
import static org.easymock.classextension.EasyMock.verify;
import static org.junit.Assert.*;


public class LdvPresenterTest {

	private EventBus eventBus;
	private DispatchAsync dispatchAsync;
	private LdvWelcomePresenter presenter;
	private LdvWelcomePresenter.Display view;
	
	@Before
	public void setUp() throws Exception 
	{	
		final Injector injector = Guice.createInjector(new WelcomeTestModule(LdvWelcomePresenter.class));

		eventBus = injector.getInstance(EventBus.class);

		dispatchAsync = injector.getInstance(DispatchAsync.class);

		view = injector.getInstance(LdvWelcomePresenter.Display.class);
		
		// Setting expectations for handlers created when the presenter will bind
		// (bind is called by the constructor)		
		MvpTestHelper.expectHasClickHandlers(view.getSend());
		
		MvpTestHelper.expectAddAnyHandler(eventBus, IdentifiersProvidedEvent.getType());
		MvpTestHelper.expectAddAnyHandler(eventBus, LdvSentEvent.getType());
		MvpTestHelper.expectAddAnyHandler(eventBus, LdvOpenEvent.getType());
		
		// This handler is created by BasicPresenter
		MvpTestHelper.expectAddAnyHandler(eventBus, PlaceRequestEvent.getType());
		
		// Put the following mocks into replay, presenter construction will make
		// calls to the mocks.
		replay(eventBus, view, dispatchAsync);

		presenter = injector.getInstance(LdvWelcomePresenter.class);

		// Verify calls to mock objects were as expected.
		// verify(eventBus, view, dispatchAsync);

		// Reset so tests can defined their own expectations.
		resetToStrict(eventBus, view, dispatchAsync);
	}
	
	/**
	 * Verify the dispatcher
	 */
	@Test
	public void testFirstEasyMockTest() 
	{
	    // Create mock object based on HasClickHandlers interface
	    final HasClickHandlers clickHandlerMock = createMock(HasClickHandlers.class);

	    // Set expectation - one call to addClickHandler with an argument
	    // matching the type "ClickHandler" and returning null.
	    expect(clickHandlerMock.addClickHandler(isA(ClickHandler.class))).andReturn(null);

	    // Put mock into replay mode
	    replay(clickHandlerMock);

	    // Run test code
	    clickHandlerMock.addClickHandler(new ClickHandler() {
	        @Override
	            public void onClick(final ClickEvent event) { }
	        });

	    // Verify all expected were met.
	    verify(clickHandlerMock);
	}

	/**
	 * Verify that the name and the password are correct
	 */
	@Test
	public void testDoSendIdentifiers() 
	{
		final SendLoginAction expectedCommand = new SendLoginAction("test", "9c7f2033a199b8233d47fb3c6b082574");

		dispatchAsync.execute(SendLoginActionMatcher.matches(expectedCommand), isA(LdvWelcomePresenter.LoginCallback.class));

		replay(dispatchAsync);

		// test code here
		presenter.doSendIdentifiers("test", "9c7f2033a199b8233d47fb3c6b082574");

		verify(dispatchAsync);
	}
	
	/**
	 * Verify that the boolean in doSendIdentifier is true.
	 */
/*
	@Test
	public void testBooleanUserMd5() {
		
		boolean bUseMd5 = true;
		assertEquals(bUseMd5, presenter.getbDoSendMd5());
	}
*/
}
