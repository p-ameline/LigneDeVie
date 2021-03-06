package com.ldv.test.util;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.classextension.EasyMock.createNiceMock;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;

import net.customware.gwt.presenter.client.Display;
import net.customware.gwt.presenter.client.Presenter;

import com.google.inject.Binder;
import com.google.inject.Key;
import com.google.inject.Module;
import com.google.inject.Provider;
import com.google.inject.Scope;
import com.google.inject.TypeLiteral;
import com.google.inject.binder.AnnotatedBindingBuilder;
import com.google.inject.binder.LinkedBindingBuilder;
import com.google.inject.binder.ScopedBindingBuilder;

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
public abstract class AbstractTestModule implements Module {

	private final Class<?> ignoreClass;
	private Binder binder;
	
	/**
	 * Create a default module with no ignore class
	 */
	public AbstractTestModule() {
		this(null);
	}
	
	/**
	 * Create a module where the class denoted by ignoreClass will not be bound.
	 *  
	 * @param ignoreClass the class to be ignored
	 */
	public AbstractTestModule(final Class<?> ignoreClass) {
		this.ignoreClass = ignoreClass;
	}
	
	@Override
	public void configure(final Binder binder) {
		this.binder = binder;
		
		configure();
	}
	
	/**
	 * 
	 */
	protected abstract void configure();

	
	/**
	 * Utility bind() which saves having to prefix "binder." and also accounts for ignoreClass
	 * 
	 * @param <T> bound type
	 * @param clazz class of bound type
	 * @return binding builder
	 */
	public <T> AnnotatedBindingBuilder<T> bind(final Class<T> clazz) {
		if (ignoreClass == clazz) {
			return new NullAnnotatedBindingBuilder<T>();
		}
		else {
			return binder.bind(clazz);
		}
	}
	
	
    /**
     * Create mock bindings for presenter and associated display.
     * 
     * @param <P>  presenter type
     * @param <D>  display type
     * @param presenter presenter class type
     * @param display   associated display type
     */
    public <P extends Presenter, D extends Display> void bindPresenter(final Class<P> presenter,
    						   										   final Class<D> display) {
    	final P mockPresenter = createNiceMock(presenter);
    	
    	bind(presenter).toInstance(mockPresenter);
    	
    	final D mockDisplay = createNiceMock(display);

    	bind(display).toInstance(mockDisplay);
    	
    	expect(mockPresenter.getDisplay()).andReturn(mockDisplay);
    	expectLastCall().anyTimes();
	}

    
    private static class NullAnnotatedBindingBuilder<T> implements AnnotatedBindingBuilder<T> {

		@Override
		public LinkedBindingBuilder<T> annotatedWith(final Class<? extends Annotation> arg0) {
			return null;
		}

		@Override
		public LinkedBindingBuilder<T> annotatedWith(final Annotation arg0) {
			return null;
		}

		@Override
		public ScopedBindingBuilder to(final Class<? extends T> arg0) {
			return null;
		}

		@Override
		public ScopedBindingBuilder to(final TypeLiteral<? extends T> arg0) {
			return null;
		}

		@Override
		public ScopedBindingBuilder to(final Key<? extends T> arg0) {
			return null;
		}

		@Override
		public void toInstance(final T arg0) {
		}

		@Override
		public ScopedBindingBuilder toProvider(final Provider<? extends T> arg0) {
			return null;
		}

		@Override
		public void asEagerSingleton() {
		}

		@Override
		public void in(final Class<? extends Annotation> arg0) {
		}

		@Override
		public void in(final Scope arg0) {			
		}

		@Override
    public <S extends T> ScopedBindingBuilder toConstructor(Constructor<S> arg0)
    {
	    // TODO Auto-generated method stub
	    return null;
    }

		@Override
    public <S extends T> ScopedBindingBuilder toConstructor(
        Constructor<S> arg0, TypeLiteral<? extends S> arg1)
    {
	    // TODO Auto-generated method stub
	    return null;
    }

		@Override
    public ScopedBindingBuilder toProvider(
        Class<? extends javax.inject.Provider<? extends T>> arg0)
    {
	    // TODO Auto-generated method stub
	    return null;
    }

		@Override
    public ScopedBindingBuilder toProvider(
        TypeLiteral<? extends javax.inject.Provider<? extends T>> arg0)
    {
	    // TODO Auto-generated method stub
	    return null;
    }

		@Override
    public ScopedBindingBuilder toProvider(
        Key<? extends javax.inject.Provider<? extends T>> arg0)
    {
	    // TODO Auto-generated method stub
	    return null;
    }
		
	};

}