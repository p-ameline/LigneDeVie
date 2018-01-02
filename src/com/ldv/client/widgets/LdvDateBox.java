package com.ldv.client.widgets;

import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.gwt.user.datepicker.client.DatePicker;

import com.ldv.client.loc.LdvConstants;
import com.ldv.shared.model.LdvTime;

/**
 * A Date box that is adapted to enter LdvTime objects
 * 
 */
public class LdvDateBox extends DateBox
{   
	private final static LdvConstants   constants  = GWT.create(LdvConstants.class) ;
	private final static DateTimeFormat dateFormat = DateTimeFormat.getFormat(constants.systemDateTimeFormat()) ;
	
  /**
   * Default Constructor
   */
  public LdvDateBox()
  {
  	super(new DatePicker(), null, new DateBox.DefaultFormat(dateFormat)) ;
  	
  	removeStyleName("gwt-DateBox") ;
		addStyleName("ldv-DateTimeBox") ;
  }
  
  /**
   * Get value as a LdvTime object
   * 
   * @return Get the date displayed as a LdvTime, or null if the text box is empty, or cannot be interpreted
   */
  public LdvTime getLdvTime()
  {
  	Date valueAsDate = getValue() ;
  	if (null == valueAsDate)
  		return null ;
  	
  	return new LdvTime(valueAsDate) ;
  }
  
  /**
   * Initialize the control from a LdvTime object
   * 
   * @param ldvTime Object to initialize the control from
   */
  public void setLdvTime(final LdvTime ldvTime)
  {
  	if (null == ldvTime)
  		return ;
  	
  	setValue(ldvTime.toJavaDate()) ;
  }
}
