package com.ldv.client.canvas;

// import com.google.gwt.widgetideas.graphics.client.Color;

public 	class LdvTeamRosacePie extends LdvTeamRosaceObject
{	
	private LdvCoordinatesCartesian _centerPoint = new LdvCoordinatesCartesian(0.0d, 0.0d) ;
	private double                  _dRadius ;
	private double                  _dLeftAngleCanvasR ;  // angles in Radian
	private double                  _dRightAngleCanvasR ;
	private String                  _sColor ;
	private int                     _iHeight ;
	
	public LdvTeamRosacePie(LdvTeamRosaceCanvas canvas, double dCenterX, double dCenterY, double dRadius, double dLeftAngleCanvasR, double dRightAngleCanvasR, String sColor, int iHeight)
	{
		super(canvas, "pie") ;
		
		_centerPoint.setX(dCenterX) ;
		_centerPoint.setY(dCenterY) ;
		
		_dRadius            = dRadius ;
		_dLeftAngleCanvasR  = dLeftAngleCanvasR ;
		_dRightAngleCanvasR = dRightAngleCanvasR ;
		_sColor             = sColor ;
		_iHeight            = iHeight ;
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
	
	public boolean contains(double x, double y)
	{			
		LdvCoordinatesCartesian cartesian = new LdvCoordinatesCartesian(x, y) ;
		LdvCoordinatesPolar     polar     = new LdvCoordinatesPolar(cartesian, _centerPoint) ;
		
		if (polar.getRadius() > _dRadius)
			return false ;
		
		// In canvas coordinates, left angle > right angle
		//
		if ((polar.getAngleR() < _dRightAngleCanvasR) || (polar.getAngleR() > _dLeftAngleCanvasR))
			return false ;
		
		System.out.println(_sColor +" Pie") ;
		return true ;
		
/*
		double angleX = x - _dCenterX ;
		double angleY = y - _dCenterY ;
		
		if (angleX*angleX + angleY*angleY <= _dRadius*_dRadius)
		{			
			double angle = polar.getAngle() ;
			if ((angle >= _dStartAngle) && (angle <= _dEndAngle))
			{
				System.out.println(_sColor +" Pie") ;
				return true ;
			}		
		}		
		return false ;
*/
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
