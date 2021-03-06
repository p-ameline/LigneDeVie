package com.ldv.client.canvas;

import com.google.gwt.user.client.ui.AbsolutePanel;
import com.ldv.shared.model.LdvTime;

public class LdvBoxSeverity extends AbsolutePanel
{
	private LdvTime _startTime ;
	private LdvTime _endTime ;
	private int 		_startPosition ;
	private int 		_endPosition ;
	private String  _color ;
	private int 		_boxWidth ;
	
	public LdvBoxSeverity(){
		super() ;	
	}
	
	public LdvBoxSeverity(LdvTime startTime, LdvTime endTime){
		super() ;	
		this._startTime = startTime ;
		this._endTime = endTime ;
	}
	
	public LdvTime getStartTime()
	{
		return _startTime;
	}
	
	public void setStartTime(LdvTime startTime)
	{
		this._startTime = startTime;
	}
	
	public LdvTime getEndTime()
	{
		return _endTime;
	}
	
	public void setEndTime(LdvTime endTime)
	{
		this._endTime = endTime;
	}
	
	public int getStartPosition()
	{
		return _startPosition;
	}
	
	public void setStartPosition(int startPosition)
	{
		this._startPosition = startPosition;
	}
	
	public int getEndPosition()
	{
		return _endPosition;
	}
	
	public void setEndPosition(int endPosition)
	{
		this._endPosition = endPosition;
	}
	
	public String getColor()
	{
		return _color;
	}
	
	public void setColor(String color)
	{
		this._color = color;
	}
	
	public int getBoxWidth()
	{
		return _boxWidth;
	}
	
	public void setBoxWidth(int width)
	{
		this._boxWidth = width;
	}
}
