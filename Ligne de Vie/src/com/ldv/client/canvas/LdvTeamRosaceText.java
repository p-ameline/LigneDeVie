package com.ldv.client.canvas;

public class LdvTeamRosaceText extends LdvTeamRosaceObject
{
	private LdvCoordinatesCartesian _centerPoint = new LdvCoordinatesCartesian(0.0d, 0.0d) ;
	
	private double    _dRadius ;
	private double    _dLeftAngleCanvasR ;
	private double    _dRightAngleCanvasR ;
	private String    _sText ;
	
	private final int _iSpace = 10 ;
	
	public LdvTeamRosaceText(LdvTeamRosaceCanvas canvas, double dCenterX, double dCenterY, double dRadius, double dLeftAngleCanvasR, double dRightAngleCanvasR, String sText)
	{
		super(canvas, "text") ;
		
		_centerPoint.setX(dCenterX) ;
		_centerPoint.setY(dCenterY) ;
		
		_dRadius             = dRadius + _iSpace ;
		_dLeftAngleCanvasR   = dLeftAngleCanvasR ;
		_dRightAngleCanvasR  = dRightAngleCanvasR ;
		_sText               = sText ;
	}
	
	@Override
	public boolean contains(double x, double y) {
		return false ;
	}

	@Override
	public void draw() 
	{
		_Canvas.drawPieLabel(_sText, _centerPoint, _dRadius, _dLeftAngleCanvasR, _dRightAngleCanvasR) ;
	}

	@Override
	public int getHeight() {
		return 0;
	}

	@Override
	public String getName() {
		return _sName ;
	}

	@Override
	public double getLeftAngleCanvasR() {
		return _dLeftAngleCanvasR ;
	}

	@Override
	public double getRadius() {
		return 0.0d ;
	}

	@Override
	public double getRightAngleCanvasR() {
		return _dRightAngleCanvasR ;
	}	
}
