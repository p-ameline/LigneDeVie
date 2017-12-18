package com.ldv.client.canvas;

/**
 * Polar coordinates (radius, angle)
 *
 * Radius is in the [0, infinite[ range
 * Angle  is expressed in radians, in the [O, 2PI[ range (the canvas polar circle is clockwise, 0 being at 3h00)
 * <pre>                 
 *       3PI/2     
 *         |       
 *  PI --------- 0 
 *         |              
 *        PI/2
 * </pre>
 * 
 */
public class LdvCoordinatesPolar
{
	private double _dAngleR ;  // Angle expressed in Radians
  private double _dRadius ;

  private static double dEpsilon = 0.000000001d ;
  
  /**
	 * Constructor
	 * 
	 * @param  dAngleR angle expressed in Radian
	 * @param  dRadius radius
	 */
  public LdvCoordinatesPolar(double dAngleR, double dRadius) {
  	setCoordinates(dAngleR, dRadius) ;
  }
  
  /**
	 * Constructor from a point expressed in Cartesian coordinates
	 * 
	 * @param  cartesian Set of Cartesian coordinates to express in polar reference frame
	 * @param  pole      Coordinates of the polar coordinates center (pole). If null, it is considered that the pole is also the center of the Cartesian reference frame
	 */
  public LdvCoordinatesPolar(final LdvCoordinatesCartesian cartesian, final LdvCoordinatesCartesian pole) {
  	initFromCartesian(cartesian, pole) ;
  }

  // There is no individual setters to avoid possible side effects of normalization when setting a single coordinate at a time
  //
  public void setCoordinates(double dAngleR, double dRadius)
  {
  	_dAngleR = dAngleR ;
  	_dRadius = dRadius ;
  	normalize() ;
  }

  public void reset()
  {
  	_dAngleR = 0 ;
  	_dRadius = 0 ;
  }
  
  public double getAngleR() {
  	return _dAngleR ;
  }
  public double getRadius() {
  	return _dRadius ;
  }
  
  /**
	 * Normalize coordinates, to make certain that the radius is positive or null and the angle is in the range [0, 2PI[
	 * 
	 */
  private void normalize()
  {
  	if (_dRadius < 0)
  	{
  		_dRadius = -_dRadius ;
  		_dAngleR = _dAngleR + Math.PI ;
  	}
  	
  	while (_dAngleR < 0)
  		_dAngleR = _dAngleR + 2 * Math.PI ;
  	
  	while (_dAngleR >= 2 * Math.PI)
  		_dAngleR = _dAngleR - 2 * Math.PI ;
  }
  
  /**
	 * Returns the polar coordinates that translates a set of Cartesian coordinates
	 * 
	 * @param  cartesian Set of Cartesian coordinates to express in polar reference frame
	 * @param  pole      Coordinates of the polar coordinates center (pole). If null, it is considered that the pole is also the center of the Cartesian reference frame 
	 * 
	 * @return The same point expressed in polar coordinates
	 */
	public void initFromCartesian(final LdvCoordinatesCartesian cartesian, final LdvCoordinatesCartesian pole)
	{	
		reset() ;
		
		if (null == cartesian)
			return ;
		
		// Coordinates in the Cartesian reference frame centered on the Polar pole
		//
		double Xcoordinate = cartesian.getX() ;
		double Ycoordinate = cartesian.getY() ;
		
		if (null != pole)
		{
			Xcoordinate -= pole.getX() ;
		  Ycoordinate -= pole.getY() ;
		}
		
		// Fist, address tricky places (pole and axis)
		//
		
		// X axis and pole
		//
		if (0.0d == Ycoordinate)
		{
			_dAngleR = 0 ;
	  	_dRadius = Xcoordinate ;
	  	normalize() ;
	  	return ;
		}
			
		// Y axis
		//
		if (0.0d == Xcoordinate)
		{
			_dAngleR = Math.PI / 2d ;
	  	_dRadius = Ycoordinate ;
	  	normalize() ;
	  	return ;
		}
		
		// Calculate radius (as a positive value)
		// 
		_dRadius = Math.sqrt(Xcoordinate * Xcoordinate + Ycoordinate * Ycoordinate) ;
		
		// The tangent is the same for angle and PI + angle (because Y/X == -Y/-X)
		//
		// Math.atan returned angle is in the range -pi/2 through pi/2 (hence, always in the right quadrants)
		// If X is negative, we have to add PI to properly set the angle in the left quadrants 
		//
		// The '-' comes from origin being top left and ordinates going backward
		//
		_dAngleR = Math.atan(Ycoordinate / Xcoordinate) ; // get angle from tangent
		
		if (Xcoordinate < 0)
			_dAngleR += Math.PI ;
		
  	normalize() ;
	}
  
  /**
	  * Determine whether two AdminDatas are exactly similar
	  * 
	  * @return true if all data are the same, false if not
	  * @param  adminData AdminData to compare
	  * 
	  */
	public boolean equals(LdvCoordinatesPolar polarO)
	{
		if (this == polarO) {
			return true ;
		}
		if (null == polarO) {
			return false ;
		}
		
		return (Math.abs(_dAngleR - polarO._dAngleR) < dEpsilon) && 
				   (Math.abs(_dRadius - polarO._dRadius) < dEpsilon) ;
	}
 
	/**
	  * Determine whether two LdvCoordinatesPolar are exactly similar
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

		final LdvCoordinatesPolar polarO = (LdvCoordinatesPolar) o ;

		return equals(polarO) ;
	}
}
