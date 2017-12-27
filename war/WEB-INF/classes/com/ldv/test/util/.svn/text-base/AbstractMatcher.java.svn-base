package com.ldv.test.util;

import org.easymock.IArgumentMatcher;

/**
 * Shell matcher that defines a generic message reporting, assignability checker
 * and type safe isMatch method.
 * 
 * @author lowec
 * 
 * @param <T>
 */
public abstract class AbstractMatcher<T> implements IArgumentMatcher {

	private final T expected;

	/**
	 * Construct a matcher for the expected value
	 * 
	 * @param expected
	 */
	protected AbstractMatcher(final T expected) {
		this.expected = expected;
	}
	
	@Override
	public void appendTo(final StringBuffer buf) {
		buf.
			append(getClass().getSimpleName()).
			append(".matches(").
			append(expected).
			append(")");
	}
	
	/**
	 * Type safe matcher method
	 * 
	 * @param expected
	 * @param actual
	 * @return true if objects match
	 */
	protected abstract boolean isMatch(T expected, T actual);

	
	@Override
	public boolean matches(final Object arg0) {
		// (arg0 instanceof T) is not permitted, the following has the same effect. 
		if (!expected.getClass().isAssignableFrom(arg0.getClass())) {
			return false;
		}

		@SuppressWarnings("unchecked")
		final T actual = (T) arg0;
		
		final boolean result = isMatch(expected, actual);

		return result;
	}
}