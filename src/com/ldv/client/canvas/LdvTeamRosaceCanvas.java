package com.ldv.client.canvas;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.dom.client.ImageElement;

/**
 *  This class provides the functions to draw on a Canvas (for example "drawPie" to draw a pie and "drawCircle" to
 *  draw a circle) 
 */
public class LdvTeamRosaceCanvas 
{
	protected Canvas _Canvas ;
	
	/**
   * Constructor, sets width and height
   *
   * @param iWidth  both the width of the canvas and coordinate space width 
   * @param iHeight both the height of the canvas and coordinate space height
   */
	public LdvTeamRosaceCanvas(Canvas canvas, int iWidth, int iHeight) 
	{
		_Canvas = canvas ;
		if (null == _Canvas)
			return ;
		
		_Canvas.setWidth(iWidth + "px") ;
		_Canvas.setHeight(iHeight + "px") ;
		_Canvas.setCoordinateSpaceWidth(iWidth) ;
		_Canvas.setCoordinateSpaceHeight(iHeight) ;
	}
	
	/**
   * Draw a circle
   *
   * @param center       circle center coordinates 
   * @param dRadius      circle radius
   * @param sStrokeColor color of the border
   * @param sFillColor   color of the inner surface
   */
	public void drawCircle(LdvCoordinatesCartesian center, double dRadius, String sStrokeColor, String sFillColor)
	{
		Context2d context = getContext2d() ;
		if (null == context)
			return ;
		 
		context.beginPath() ;
		context.setFillStyle(sFillColor) ;
		context.setStrokeStyle(sStrokeColor) ;
		context.setLineWidth(1) ;
		
		context.arc(center.getX(), center.getY(), dRadius, 0, Math.PI*2, false) ;
		context.fill() ;		
		context.stroke() ;
	}
	
	/**
   * Draw a pie
   *
   * @param center       circle center coordinates 
   * @param dRadius      circle radius
   * @param dLeftAngleR  left angle in radian (anti-clockwise max)
   * @param dRightAngleR right angle in radian (anti-clockwise min)
   * @param sStrokeColor color of the border
   * @param sFillColor   color of the inner surface
   */
	public void drawPie(LdvCoordinatesCartesian center, double dRadius, double dLeftAngleR, double dRightAngleR, String sStrokeColor, String sFillColor)
	{
		Context2d context = getContext2d() ;
		if (null == context)
			return ;
		 
		LdvCoordinatesPolar     arcStartingPoint = new LdvCoordinatesPolar(dLeftAngleR, dRadius) ;
		LdvCoordinatesCartesian startingPoint    = new LdvCoordinatesCartesian(arcStartingPoint, center) ;
		
		context.beginPath() ;
		context.setFillStyle(sFillColor) ;
		context.setStrokeStyle(sStrokeColor) ;
		context.setLineWidth(1) ;
		
		context.moveTo(center.getX(), center.getY()) ;
		context.lineTo(startingPoint.getX(), startingPoint.getY()) ;		
		context.arc(center.getX(), center.getY(), dRadius, dLeftAngleR, dRightAngleR) ;
		context.closePath() ;
		
		context.fill() ;		
		context.stroke() ;
	}
		
	/**
   * Draw a pie
   *
   * @param center       circle center coordinates 
   * @param dRadius      circle radius
   * @param dLeftAngleR  left angle in radian (anti-clockwise max)
   * @param dRightAngleR right angle in radian (anti-clockwise min)
   * @param sStrokeColor color of the border
   * @param sFillColor   color of the inner surface
   */
	public void drawPieLabel(String sText, LdvCoordinatesCartesian center, double dRadius, double dLeftAngleR, double dRightAngleR)
	{
		drawPieLabelManually(sText, center, dRadius, dLeftAngleR, dRightAngleR) ;		 
	}
	
	public void drawPieLabelManually(String sText, LdvCoordinatesCartesian center, double dRadius, double dLeftAngleR, double dRightAngleR)
	{
		Context2d context = getContext2d() ;
		if (null == context)
			return ;
		
		context.setFont("20pt Calibri") ;
		context.setTextAlign("center") ;
		context.setFillStyle("blue") ;
		context.setStrokeStyle("blue") ;
		context.setLineWidth(4) ;
		
		int    iLen         = sText.length() ;
		double dAngle       = TrigonometricFcts.getRadianAngleToRotate(dLeftAngleR, dRightAngleR) ;
		double dLetterAngle = dAngle / iLen ;
		
		context.save() ;
		context.translate(center.getX(), center.getY()) ;
		
		// The standard writings occur at 3PI/2 according to the canvas trigo circle, hence, writing at
		// angle 0 means rotating PI/2 clockwise 
		// 
		double dAngleToRotate = TrigonometricFcts.getRadianAngleToRotate(3 * Math.PI / 2d, dLeftAngleR) ;
		
		context.rotate(dAngleToRotate) ;
		context.rotate(-1 * dLetterAngle / 2) ;
		for (int n = 0 ; n < iLen ; n++) 
		{
			context.rotate(dLetterAngle) ;
			context.save() ;
			context.translate(0, -1 * dRadius) ;
			String s = String.valueOf(sText.charAt(n)) ;
			context.fillText(s, 0d, 0d) ;
			context.restore() ;
		}
		context.restore() ;
	}
	
	public void drawImage(ImageElement image, double dx, double dy)
	{
		Context2d context = getContext2d() ;
		if (null == context)
			return ;
		
		context.drawImage(image, dx, dy) ;
	}
	
	protected Context2d getContext2d()
	{
		if (false == isValid())
			return null ;
		
		return _Canvas.getContext2d() ;
	}
	
	public boolean isValid() {
		return (null != _Canvas) ;
	}
}