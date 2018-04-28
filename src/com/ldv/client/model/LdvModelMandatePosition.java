package com.ldv.client.model;

/** 
 * Position in rosace: angle and distance
 * 
 */
public class LdvModelMandatePosition
{
	protected double _iDistance ;
	protected double _iAngle ;     // [0, 360[
	
	public LdvModelMandatePosition(double iDist, double iAngle)
	{
		_iDistance = iDist ;
		_iAngle    = iAngle ;
	}
	
	public LdvModelMandatePosition()
	{
		reset() ; 
	}
	
	void reset()
	{
		_iDistance = -1 ;
		_iAngle    = -1 ;
	}

	/** 
	 * Similar to operator=
	 * 
	 * @param src model to initialize this object from
	 */
	public void deepCopy(LdvModelMandatePosition src)
	{
		reset() ;
		
		if (null == src)
			return ;
		
		_iDistance = src._iDistance ;
		_iAngle    = src._iAngle ;
	}
	
	/**
	 * Is the position invalid? is valid  if distance is 0 (no angle needed for the center) or both distance and angle are positive or 0   
	 * 
	 * @return <code>false</code> if storage element was successfully found/created, <code>false</code> if not
	 * 
	 **/
	public boolean isEmpty() {
		return ((-1 == _iDistance) && (-1 == _iAngle)) ;
	}
	
	/**
	 * Is the position valid?     
	 * 
	 * @return <code>true</code> if distance is 0 (no angle needed for the center) or both distance and angle are positive or 0, <code>false</code> if not
	 * 
	 **/
	public boolean isValid() {
		return ((_iDistance < 0) || ((_iDistance > 0) && (_iAngle < 0))) ;
	}
	
	/**
	 * Is the distance valid?     
	 * 
	 * @return <code>true</code> if distance is >= 0, <code>false</code> if not
	 * 
	 **/
	public boolean isValidDistance() {
		return (_iDistance >= 0) ;
	}
	
	/**
	 * Is the angle valid?     
	 * 
	 * @return <code>true</code> if 0 >= angle < 360, <code>false</code> if not
	 * 
	 **/
	public boolean isValidAngle() {
		return ((_iAngle >= 0) && (_iAngle < 360)) ;
	}
	
	public double getDistance() {
  	return _iDistance ;
  }
	public void setDistance(double iDistance) {
  	_iDistance = iDistance ;
  }

	public double getAngle() {
  	return _iAngle ;
  }
	public void setAngle(double iAngle) {
  	_iAngle = iAngle ;
  }
}
