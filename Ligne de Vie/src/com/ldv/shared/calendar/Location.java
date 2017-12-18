package com.ldv.shared.calendar ;

import com.google.gwt.user.client.rpc.IsSerializable ;
import com.ldv.shared.util.MiscellanousFcts;

/**
 * Person.java
 *
 * The Person class represents object type user in database
 * 
 * Created: 19 Jul 2011
 *
 * Author: PA
 * 
 */
public class Location implements IsSerializable 
{
	private String _sText ;
	private String _sLocParam ; 
	
	private static final String DELIMITER = ";" ;
	
	/**
	 * Default constructor (does nothing)
	 */
	public Location() {
		reset() ;
	}
	
	/**
	 * Constructor from two strings
	 * 
	 * @param sText    The location as a text
	 * @param sFileURI The location as a file (LDAP or .vcf)
	 */
	public Location(final String sText, final String sLocParam) 
	{
		_sText     = sText ;
		_sLocParam = sLocParam ;
	}
	
	/**
	 * Constructor from a string
	 * 
	 * @param sLatLon A String in the form <code>locparam ";" text</code> containing respectively the text and the file URI
	 */
	public Location(final String sTextFile) {
		setFromString(sTextFile) ;
	}
	
	/**
	 * Initialize from another Geo object
	 * 
	 * @param other Other Geo object to initialize from
	 */
	public void initFromLocation(Location other)
	{
		reset() ;
		
		if (null == other)
			return ;
		
		_sText     = other._sText ;
		_sLocParam = other._sLocParam ;
	}
	
	/**
	 * Reset to "";""
	 */
	public void reset()
	{
		_sText     = "" ;
		_sLocParam = "" ;
	}
	
	/**
	 * Get the Location object as a String
	 * 
	 * @return A String in the form locparam ";" text
	 */
	public String getValue() 
	{
		String sResult = "" ;
		
		if (false == "".equals(_sLocParam))
		{
			sResult = _sLocParam ;
			if (false == "".equals(_sText))
				sResult += DELIMITER ;
		}
		
		if (false == "".equals(_sText))
			sResult += _sText ;
		
    return sResult ;
	}
	
	public String getText() {
		return _sText ;
	}
	public void setText(final String sText) {
		_sText = sText ;
	}
	
	public String getLocParam() {
		return _sLocParam ;
	}
	public void setLocParam(final String sLocParam) {
		_sLocParam = sLocParam ;
	}
	
	/**
	 * Is this object in its reseted state 
	 */
	public boolean isEmpty() {
		return ("".equals(_sText) && "".equals(_sLocParam)) ;
	}
	
	/**
	 * Initialize text and locparam from a string
	 * 
	 * @param sLatLon A String in the form <code>locparam ";" text</code>
	 */
	public void setFromString(final String sLocation)
	{
/*
		if ((null == sLocation) || "".equals(sLocation))
			return ;
		 
		String[] components = sLocation.split(DELIMITER) ;
		
		if (0 == components.length)
			return ;
		
		if (1 == components.length)
			return ;
		
		_bdLatitude  = new BigDecimal(components[0]) ;
		_bdLongitude = new BigDecimal(components[1]) ;
*/
	}

	/**
	 * Determine whether two Location objects are exactly similar
	 * 
	 * @param  other Other Location to compare to
	 * 
	 * @return <code>true</code> if all data are the same, <code>false</code> if not
	 */
	public boolean equals(final Location other)
	{
		if (this == other) {
			return true ;
		}
		if (null == other) {
			return false ;
		}
		
		return (MiscellanousFcts.areIdenticalStrings(_sText,     other._sText) &&
		        MiscellanousFcts.areIdenticalStrings(_sLocParam, other._sLocParam)) ;
	}
	
	/**
	  * Determine whether an object is exactly similar to this Location object
	  * 
	  * designed for ArrayList.contains(Obj) method
		* because by default, contains() uses equals(Obj) method of Obj class for comparison
	  * 
	  * @param o Location to compare to
	  * 
	  * @return <code>true</code> if all data are the same, <code>false</code> if not
	  */
	public boolean equals(Object o) 
	{
		if (this == o) {
			return true ;
		}
		if (null == o || getClass() != o.getClass()) {
			return false;
		}

		final Location other = (Location) o ;

		return (this.equals(other)) ;
	}
}
