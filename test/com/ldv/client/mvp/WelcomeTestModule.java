package com.ldv.client.mvp;

import static org.easymock.EasyMock.createStrictMock;
import static org.easymock.classextension.EasyMock.createNiceMock;
import net.customware.gwt.dispatch.client.DispatchAsync;
import net.customware.gwt.presenter.client.EventBus;
import com.ldv.test.util.AbstractTestModule;

import com.google.inject.Singleton;

/**
 * Create a Guice module equivalent to the GIN client module
 * {@link co.uk.hivedevelopment.wizardmvp.client.gin.WizardClientModule}.
 *
 * All mock objects are created in "nice" mode - it's up to individual tests to
 * put the mock into strict or default mode if required.
 *
 * @author lowec
 *
 */
public class WelcomeTestModule extends AbstractTestModule {

	/**
	 * Create a default module with no ignore class
	 */
	public WelcomeTestModule() {
		super();
	}

	/**
	 * Create a module where the class denoted by ignoreClass will not be bound.
	 *
	 * @param ignoreClass the class to be ignored
	 */
	public WelcomeTestModule(final Class<?> ignoreClass) {
		super(ignoreClass);
	}


	@Override
	protected void configure() 
	{
		final EventBus eventBus = createStrictMock(EventBus.class);
		bind(EventBus.class).toInstance(eventBus);

		// test gwt-dispatch
		final DispatchAsync dispatchAsync = createStrictMock(DispatchAsync.class);
		bind(DispatchAsync.class).toInstance(dispatchAsync);

		// The actual wizard
		bindPresenter(LdvWelcomePresenter.class, LdvWelcomePresenter.Display.class) ;

		bind(LdvAppPresenter.class).in(Singleton.class);		
	}
}
