package com.ldv.shared.model;

import java.io.Serializable;

public class LdvStatus implements Serializable
{
	private static final long serialVersionUID = -564752566129900285L;
	
	public static enum AvailabilityLevel { UNKNOWN, OPEN, HERE, OUT }
	
	private AvailabilityLevel _availability ;

	public LdvStatus() 
	{
		_availability = AvailabilityLevel.UNKNOWN ;
	}
	
	public AvailabilityLevel getAvailabilityLevel() {
  	return _availability ;
  }
	public void setAvailabilityLevel(AvailabilityLevel availability) {
		_availability = availability ;
  }
}
