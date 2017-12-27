package com.ldv.test.util;

import org.easymock.IAnswer;
import org.easymock.IExpectationSetters;

/**
 * Context encapsulating expectation setters and an assigned mock value.
 *
 * @author lowec
 *
 * @param <T>
 */
public class ExpectationContext<T> implements IExpectationSetters<T> {

	private final IExpectationSetters<T> expectationSetter;
	private final T value;

	/**
	 * @param expectationSetter
	 * @param value
	 */
	public ExpectationContext(final IExpectationSetters<T> expectationSetter, final T value) {
		super();

		this.expectationSetter = expectationSetter;
		this.value = value;
	}

	/**
	 * Get the value assigned as a return value.
	 *
	 * @return value
	 */
	public T getValue() {
		return value;
	}

	@Override
	public IExpectationSetters<T> andAnswer(final IAnswer<? extends T> answer) {
		expectationSetter.andAnswer(answer);

		return this;
	}

	@Override
	public IExpectationSetters<T> andDelegateTo(final Object delegateTo) {
		expectationSetter.andDelegateTo(delegateTo);

		return this;
	}

	@Override
	public IExpectationSetters<T> andReturn(final T value) {
		expectationSetter.andReturn(value);

		return this;
	}

	@Override
	public void andStubAnswer(final IAnswer<? extends T> answer) {
		expectationSetter.andStubAnswer(answer);
	}

	@Override
	public void andStubDelegateTo(final Object delegateTo) {
		expectationSetter.andStubDelegateTo(delegateTo);
	}

	@Override
	public void andStubReturn(final T value) {
		expectationSetter.andStubReturn(value);
	}

	@Override
	public void andStubThrow(final Throwable throwable) {
		expectationSetter.andStubThrow(throwable);
	}

	@Override
	public IExpectationSetters<T> andThrow(final Throwable throwable) {
		expectationSetter.andThrow(throwable);

		return this;
	}

	@Override
	public IExpectationSetters<T> anyTimes() {
		expectationSetter.anyTimes();

		return this;
	}

	@Override
	public void asStub() {
		expectationSetter.asStub();
	}

	@Override
	public IExpectationSetters<T> atLeastOnce() {
		expectationSetter.atLeastOnce();

		return this;
	}

	@Override
	public IExpectationSetters<T> once() {
		expectationSetter.once();

		return this;
	}

	@Override
	public IExpectationSetters<T> times(final int min, final int max) {
		expectationSetter.times(min, max);

		return this;
	}

	@Override
	public IExpectationSetters<T> times(final int count) {
		expectationSetter.times(count);

		return this;
	}

}