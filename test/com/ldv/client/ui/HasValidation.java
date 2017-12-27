package com.ldv.client.ui;

import eu.maydu.gwt.validation.client.ValidationException;


/**
 * An object that implements this interface may have validation components.
 *
 * @author lowec
 *
 */
public interface HasValidation {

	/**
	 * Executes validation and returns true if all values were valid.
	 *
	 * @return true if all values are valid.
	 */
	boolean isValid();

	/**
	 * When a server call has resulted in an exception, use this to pass the
	 * validation exception for processing by validation actions.
	 *
	 * @param cause
	 */
	void processServerErrors(ValidationException cause);

}
