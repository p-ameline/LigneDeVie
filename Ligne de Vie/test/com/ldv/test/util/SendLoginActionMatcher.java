package com.ldv.test.util;

import static org.easymock.EasyMock.reportMatcher;

import com.ldv.shared.rpc.SendLoginAction;

/**
 * Matches an expected EasyMock argument against the provided SendLogin
 * action
 *
 * @author qianyue
 *
 */
public class SendLoginActionMatcher extends AbstractMatcher<SendLoginAction> {

	private SendLoginActionMatcher(final SendLoginAction expected) {
		super(expected);
	}

	/**
	 * Expects an Object that matches the given value using the rules specified
	 * in matches().
	 *
	 * @param value
	 *            the expected value.
	 * @return <code>null</code>.
	 */
	
   public static final SendLoginAction matches(final SendLoginAction value) {
 
 		reportMatcher(new SendLoginActionMatcher(value));
 
 		return null;
 	}
 
	
	@Override
	protected boolean isMatch(final SendLoginAction expected, final SendLoginAction actual) {
		final boolean result = (expected.getIdentifier().equals(actual.getIdentifier())
								&& expected.getEncryptedPassword().equals(actual.getEncryptedPassword()));
		return result;
	}
}