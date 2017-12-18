package com.ldv.client.canvas;

import com.swtoolbox.canvasfont.client.SWTBCanvasText;

public abstract class LdvTeamRosaceObject extends SWTBCanvasText
{
	protected LdvTeamRosaceCanvas _Canvas ;
	protected String              _sName ;
	
	public abstract void    draw() ;
	public abstract int     getHeight() ;
	public abstract boolean contains(double x, double y) ;
	public abstract double  getLeftAngleCanvasR() ;
	public abstract double  getRightAngleCanvasR() ;
	public abstract double  getRadius() ;

	public LdvTeamRosaceObject(LdvTeamRosaceCanvas canvas, String sName)
	{	
		_sName  = sName ;
		_Canvas = canvas ;
	}
		
	public String getName() {
		return _sName ;
	}
}
