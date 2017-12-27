package com.ldv.shared.calendar ;

import java.math.BigDecimal;

import com.google.gwt.user.client.rpc.IsSerializable ;

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
public class Geo implements IsSerializable 
{
	private BigDecimal _bdLatitude ;
	private BigDecimal _bdLongitude ; 
	
	private static final String DELIMITER = ";" ;
	
	/**
	 * Default constructor (does nothing)
	 */
	public Geo() {
		reset() ;
	}
	
	/**
	 * Constructor from two BigDecimal values 
	 * 
	 * @param latitude  The latitude
	 * @param longitude The longitude
	 */
	public Geo(final BigDecimal latitude, final BigDecimal longitude) 
	{
		_bdLatitude  = latitude ;
		_bdLongitude = longitude ;
	}
	
	/**
	 * Constructor from a string
	 * 
	 * @param sLatLon A String in the form <code>float ";" float</code> containing respectively latitude and longitude
	 */
	public Geo(final String sLatLon) {
		setFromString(sLatLon) ;
	}
	
	/**
	 * Initialize from another Geo object
	 * 
	 * @param other Other Geo object to initialize from
	 */
	public void initFromGeo(Geo other)
	{
		reset() ;
		
		if (null == other)
			return ;
		
		_bdLatitude  = other._bdLatitude ;
		_bdLongitude = other._bdLongitude ;
	}
	
	/**
	 * Reset to 0;0
	 */
	public void reset()
	{
		_bdLatitude  = BigDecimal.ZERO ;
		_bdLongitude = BigDecimal.ZERO ;
	}
	
	/**
	 * Get the Geo object as a String
	 * 
	 * @return A String in the form latitude ";" longitude
	 */
	public String getValue() {
    return String.valueOf(_bdLatitude) + DELIMITER + String.valueOf(_bdLongitude) ;
	}
	
	public BigDecimal getLatitude() {
		return _bdLatitude ;
	}
	public BigDecimal getLongitude() {
		return _bdLongitude ;
	}
	
	/**
	 * Initialize latitude and longitude from a string
	 * 
	 * @param sLatLon A String in the form <code>float ";" float</code> containing respectively latitude and longitude
	 */
	public void setFromString(final String sLatLon) 
	{
		if ((null == sLatLon) || "".equals(sLatLon))
			return ;
		 
		String[] components = sLatLon.split(DELIMITER) ;
		
		if (components.length < 2)
			return ;
		
		_bdLatitude  = new BigDecimal(components[0]) ;
		_bdLongitude = new BigDecimal(components[1]) ;
	}
}
