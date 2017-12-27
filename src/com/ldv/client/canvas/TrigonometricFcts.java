package com.ldv.client.canvas;

/**
 * <p>
 * TrigonometricFcts hosts a set of static trigonometric functions
 * </p>
 * 
 */
public class TrigonometricFcts {

	/**
	 * Convert an angle unit from Degree (typical range [0, 360[) to Radian (corresponding range [0, 2PI[) 
	 * 
	 * @param  dDegreeAngle angle expressed in Degrees
	 * @return The angle expressed in Radians
	 */
	public static double getRadianAngleFromDegreeAngle(double dDegreeAngle) {
		return (dDegreeAngle * Math.PI / 180) ;
	}
	
	/**
	 * Convert an angle expressed in Degree in Rosace coordinates (clockwise, 0 located at 9h00) 
	 *       to the same angle in Radian in canvas coordinates (clockwise too, but 0 located at 3h00) 
	 * 
	 * @param  ldvAngle angle expressed in Degrees in usual trigonometric reference frame
	 * @return The same angle expressed in Radian in Rosace reference frame
	 */
	public static double getCanvasRAngleFromLdvDAngle(double ldvAngle)
	{
		// Switch from zero at right to zero at left (hence the 180 degrees adjustment)
		//
		double canvasAngle = ldvAngle - 180 ;
		
		// Normalize negative values, so, for example '-90' becomes '270'
		//
		if (canvasAngle < 0)
			canvasAngle = canvasAngle + 360 ;
		
/* True or false, but not natural
		if (ldvAngle >= 180)
			canvasAngle = ldvAngle - 180 ;
		else
			canvasAngle = ldvAngle + 180 ;
*/
		
		return getRadianAngleFromDegreeAngle(canvasAngle) ;		
	}
	
	/**
	 * Return the angle to rotate from dAngleFrom to dAngleTo
	 * 
	 * @param  dAngleFrom origin angle
	 * @param  dAngleTo   destination angle
	 * @return The rotation angle expressed in Radians
	 */
	public static double getRadianAngleToRotate(double dAngleFrom, double dAngleTo) 
	{
		if (dAngleTo >= dAngleFrom)
			return dAngleTo - dAngleFrom ;
		
		return 2 * Math.PI + dAngleTo - dAngleFrom ;
	}
}
