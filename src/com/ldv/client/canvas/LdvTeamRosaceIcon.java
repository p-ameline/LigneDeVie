package com.ldv.client.canvas;

import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.widgetideas.graphics.client.ImageLoader;
import com.ldv.client.model.LdvModelMandatePair;

public class LdvTeamRosaceIcon extends LdvTeamRosaceObject /*implements MouseOverHandler,MouseDownHandler,MouseMoveHandler*/
{
	private double              _dX ;
	private double              _dY ;
	private int                 _iHeight ;
	
	private LdvModelMandatePair _mandatePair ;
	
	// private String[]            _urls = new String[] {"LDV.ICO"} ;
	private String[]            _urls = new String[] {""} ;
	
	public LdvTeamRosaceIcon(LdvTeamRosaceCanvas canvas, int iX, int iY, int iHeight, LdvModelMandatePair mandatePair)
	{
		super(canvas, "icon") ;
		
		_dX      = iX - 10 ; //make the icon appear in the right place of the mouseclick 
		_dY      = iY - 10 ;
		_iHeight = iHeight ;
		
		_mandatePair = mandatePair ;
	}
	
	public LdvTeamRosaceIcon(LdvTeamRosaceCanvas canvas, LdvModelMandatePair mandatePair, int iHeight) 
	{
		super(canvas, "icon") ;
		
		_mandatePair = mandatePair ;
	}
		
	public void draw() 
	{
		if ("".equals(_urls[0]))
		{
			drawVector() ;
			return ;
		}
		
		ImageLoader.loadImages(_urls, new ImageLoader.CallBack() {
	    	public void onImagesLoaded(ImageElement[] imageElements) {
	    	  ImageElement img = imageElements[0] ;  
	        _Canvas.drawImage(img, _dX, _dY) ;	        
	      }
		});
	}
	
	protected void drawVector()
	{
		LdvCoordinatesCartesian centerPoint = new LdvCoordinatesCartesian(_dX, _dY) ;
		_Canvas.drawCircle(centerPoint, 5, "Black", "white") ;
	}
	
	@Override
	public boolean contains(double x, double y) {
		return false ;
	}
	
	@Override
	public int getHeight() {
		return _iHeight ;
	}

	@Override
	public String getName() {
		return _sName ;
	}
	
	public void setX(double x) {
		_dX = x - 10 ;
	}
	
	public void setY(double y) {
		_dY = y - 10 ;
	}

	LdvModelMandatePair getMandatePair() { 
		return _mandatePair ;
	}
	
	@Override
	public double getLeftAngleCanvasR() {
		return 0.0d ;
	}

	@Override
	public double getRadius() {
		return 0.0d ;
	}

	@Override
	public double getRightAngleCanvasR() {
		return 0.0d ;
	}
}
