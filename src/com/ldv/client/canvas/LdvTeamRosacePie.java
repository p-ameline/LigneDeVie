package com.ldv.client.canvas;

// import com.google.gwt.widgetideas.graphics.client.Color;

/**
 * A LdvTeamRosacePie is a pie slice shaped petal  
 * 
 * @author PA
 */
public 	class LdvTeamRosacePie extends LdvTeamRosacePetal
{	
	private LdvCoordinatesCartesian _centerPoint = new LdvCoordinatesCartesian(0.0d, 0.0d) ;
	private double                  _dRadius ;
	private double                  _dLeftAngleCanvasR ;  // angles in Radian
	private double                  _dRightAngleCanvasR ;
	
	public LdvTeamRosacePie(final LdvTeamRosaceCanvas canvas, final double dCenterX, final double dCenterY, final double dRadius, final double dLeftAngleCanvasR, final double dRightAngleCanvasR, final String sColor, final int iHeight)
	{
		super(canvas, "pie", sColor, iHeight) ;
		
		_centerPoint.setX(dCenterX) ;
		_centerPoint.setY(dCenterY) ;
		
		_dRadius            = dRadius ;
		_dLeftAngleCanvasR  = dLeftAngleCanvasR ;
		_dRightAngleCanvasR = dRightAngleCanvasR ;
	}
	
	public void draw()
	{	
		_Canvas.drawPie(_centerPoint, _dRadius, _dLeftAngleCanvasR, _dRightAngleCanvasR, "black", _sColor) ;

		/*
		// LdvCoordinatesPolar     arcStartingPoint = new LdvCoordinatesPolar(_dLeftAngleCanvasR, _dRadius) ;
		LdvCoordinatesPolar     arcStartingPoint = new LdvCoordinatesPolar(_dRightAngleCanvasR, _dRadius) ;
		LdvCoordinatesCartesian startingPoint    = new LdvCoordinatesCartesian(arcStartingPoint, _centerPoint) ;
		
		// double pointX = _dRadius * Math.cos(_dStartAngle) + _dCenterX ;
		// double pointY = _dRadius * Math.sin(_dStartAngle) + _dCenterY ;
		
		_Canvas.setFillStyle(new Color(_sColor)) ;
		_Canvas.setStrokeStyle(new Color("black")) ;
		
		_Canvas.beginPath() ;
		_Canvas.moveTo(_centerPoint.getX(), _centerPoint.getY()) ;
		//the parameters of cos() ,sin() are radians
		_Canvas.lineTo(startingPoint.getX(), startingPoint.getY()) ;		
		// _Canvas.arc(_centerPoint.getX(), _centerPoint.getY(), _dRadius, _dLeftAngleCanvasR, _dRightAngleCanvasR, false) ;
		_Canvas.arc(_centerPoint.getX(), _centerPoint.getY(), _dRadius, _dRightAngleCanvasR, _dLeftAngleCanvasR, true) ;
		_Canvas.closePath() ;
		
		_Canvas.fill() ;
		_Canvas.stroke() ;
		*/		
	}
	
	public int getHeight() {
		return _iHeight ;
	}
	
	/**
	 * Is the (x, y) point inside the pie?
	 */
	public boolean contains(double x, double y)
	{			
		LdvCoordinatesCartesian cartesian = new LdvCoordinatesCartesian(x, y) ;
		LdvCoordinatesPolar     polar     = new LdvCoordinatesPolar(cartesian, _centerPoint) ;
		
		if (polar.getRadius() > _dRadius)
			return false ;
		
		// In canvas coordinates, left angle < right angle
		//
		if ((polar.getAngleR() > _dRightAngleCanvasR) || (polar.getAngleR() < _dLeftAngleCanvasR))
			return false ;
		
		// Is the point inside a petal that is closer from the center?
		//
		// Get the inner radius (_dRadius being the outer one)
		//
		int    iDivider     = 3 - _iHeight ;
		double dInnerRadius = _dRadius / iDivider ;  
		double dLowerLimit  = dInnerRadius * (2 - _iHeight) ;
		
		if (polar.getRadius() < dLowerLimit)
			return false ;
		
		return true ;
	}

	@Override
	public String getName() {
		return _sName ;
	}
	
	public double getLeftAngleCanvasR() {
		return _dLeftAngleCanvasR ;
	}
	
	public double getRightAngleCanvasR() {
		return _dRightAngleCanvasR ;
	}
	
	public double getRadius() {
		return _dRadius ;
	}
}
