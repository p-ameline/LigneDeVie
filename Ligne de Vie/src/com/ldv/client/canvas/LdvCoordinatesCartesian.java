package com.ldv.client.canvas;

/**
	* Cartesian coordinates (x, y)
	* 
	*/
public class LdvCoordinatesCartesian
{
	// Don't forget that 0,0 is top-left
	//
	private double _dX ;
	private double _dY ;
	
	private static double dEpsilon = 0.000000001d ;
    	
	public LdvCoordinatesCartesian(double dX, double dY) {
		_dX = dX ;
		_dY = dY ;
	}
	
	public LdvCoordinatesCartesian(final LdvCoordinatesPolar polar, final LdvCoordinatesCartesian pole) {
		initFromPolar(polar, pole) ;
	}
	
	public double getX() {
		return _dX ;
	}
	public void setX(double dX) {
		_dX = dX ;
	}
	
	public double getY() {
		return _dY ;
	}
	public void setY(double dY) {
		_dY = dY ;
	}
	
	public void reset()
	{
		_dX = 0.0d ;
		_dY = 0.0d ;
	}
	
	/**
	 * Initialize this point from a set of polar coordinates
	 * 
	 * @param  polar The set of polar coordinates to express in cartesian reference frame
	 * @param  pole Cartesian coordinates of the polar coordinates center (pole). . If null, it is considered that the pole is also the center of the Cartesian reference frame
	 * 
	 */
	public void initFromPolar(final LdvCoordinatesPolar polar, final LdvCoordinatesCartesian pole)
	{
		reset() ;
		
		if (null == polar)
			return ;
		
		double angle  = polar.getAngleR() ;
		double radius = polar.getRadius() ;
		
		_dX = radius * Math.cos(angle) ;
		_dY = radius * Math.sin(angle) ;
		
		if (null != pole)
		{
			_dX += pole.getX() ;
			_dY += pole.getY() ;
		}
	}
	
	/**
	  * Determine whether two LdvCoordinatesCartesian are exactly similar
	  * 
	  * @return true if all data are the same, false if not
	  * @param  adminData AdminData to compare
	  * 
	  */
	public boolean equals(LdvCoordinatesCartesian cartesianO)
	{
		if (this == cartesianO) {
			return true ;
		}
		if (null == cartesianO) {
			return false ;
		}
		
		return (Math.abs(_dX - cartesianO._dX) < dEpsilon) && (Math.abs(_dY - cartesianO._dY) < dEpsilon) ;
	}

	/**
	  * Determine whether two LdvCoordinatesCartesian are exactly similar
	  * 
	  * designed for ArrayList.contains(Obj) method
		* because by default, contains() uses equals(Obj) method of Obj class for comparison
	  * 
	  * @return true if all data are the same, false if not
	  * @param o Object to compare
	  * 
	  */
	public boolean equals(Object o) 
	{
		if (this == o) {
			return true ;
		}
		if (null == o || getClass() != o.getClass()) {
			return false;
		}

		final LdvCoordinatesCartesian cartesianO = (LdvCoordinatesCartesian) o ;

		return equals(cartesianO) ;
	}
}
