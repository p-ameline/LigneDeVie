package com.ldv.client.canvas;

// import com.google.gwt.widgetideas.graphics.client.Color;

public class LdvTeamRosaceCircle extends LdvTeamRosaceObject{

	private LdvCoordinatesCartesian _centerPoint = new LdvCoordinatesCartesian(0.0d, 0.0d) ;
	private double                  _dRadius ;
	private int                     _iHeight ;
	
	public LdvTeamRosaceCircle(LdvTeamRosaceCanvas canvas, double dCenterX, double dCenterY, double dRadius, int iHeight)
	{
		super(canvas, "circle") ;
		
		_centerPoint.setX(dCenterX) ;
		_centerPoint.setY(dCenterY) ;
		
		_dRadius  = dRadius ;
		_iHeight  = iHeight ;
	}
	
	public void draw()
	{	  	
		_Canvas.drawCircle(_centerPoint, _dRadius, "Black", "white") ;
		
		/*
		_Canvas.beginPath() ;
		_Canvas.setFillStyle(new Color("white")) ;
		_Canvas.setStrokeStyle(new Color("Black")) ;
		_Canvas.arc(_centerPoint.getX(), _centerPoint.getY(), _dRadius, 0, Math.PI*2, false) ;
		_Canvas.fill() ;		
		_Canvas.stroke() ;
		*/		
	}
   
	public int getHeight() {
		return _iHeight ;
	}

	@Override
	public boolean contains(double x, double y)
	{
		LdvCoordinatesPolar polar = new LdvCoordinatesPolar(new LdvCoordinatesCartesian(x, y), _centerPoint) ;
		
		if (polar.getRadius() > _dRadius)
			return false ;
		
		System.out.println("Circle!") ;
		return true ;
	}

	@Override
	public double getLeftAngleCanvasR() {
		return 0.0d ;
	}

	@Override
	public double getRadius() {
		return _dRadius ;
	}

	@Override
	public double getRightAngleCanvasR() {
		return 0.0d ;
	}
}
