package com.ldv.client.canvas;

import com.ldv.client.model.LdvModelRosacePetal;
import com.ldv.client.model.LdvModelRosacePetalSegment;

/** 
 * A LdvTeamRosacePetalDescriptor represents an angular sector for a given radius
 * 
 */
public class LdvTeamRosacePetalDescriptor 
{
	private int                    _iRadius ;
	private int                    _iLeftRosaceAngleLdvD ;   // Left and right regarding text writing
	private int                    _iRightRosaceAngleLdvD ;
	private String                 _sColor ;
	
	private LdvTeamRosaceStructure _structure ;
		
	public LdvTeamRosacePetalDescriptor(LdvTeamRosaceStructure structure, int radius, String color, int iLeftRosaceAngleLdvD, int iRightRosaceAngleLdvD) 
	{
		_structure             = structure ;
		_iRadius               = radius ;
		_sColor                = color ;
		_iLeftRosaceAngleLdvD  = iLeftRosaceAngleLdvD ;
		_iRightRosaceAngleLdvD = iRightRosaceAngleLdvD ;
	}
	
	public LdvTeamRosacePetalDescriptor(LdvTeamRosaceStructure structure, LdvModelRosacePetal petal, LdvModelRosacePetalSegment petalSegment) 
	{
		_structure             = structure ;
		_iRadius               = petalSegment.getRadius() ;
		_sColor                = petalSegment.getColor() ;
		_iLeftRosaceAngleLdvD  = petal.getLeftRosaceAngleLdvD() ;
		_iRightRosaceAngleLdvD = petal.getRightRosaceAngleLdvD() ;
	}
		
	public int getRadius() {
		return _iRadius ;
	}
	public void setRadius(int iRadius) {
		_iRadius = iRadius ;
	}
		
	public String getColor() {
		return _sColor ;
	}
	public void setColor(String sColor) {
		_sColor = sColor ;
	}
	
	public int getLeftRosaceAngleLdvD() {
		return _iLeftRosaceAngleLdvD ;
	}
	public void setLeftRosaceAngleLdvD(int angle) {
		_iLeftRosaceAngleLdvD = angle ;
	}
	
	public int getRightRosaceAngleLdvD() {
		return _iRightRosaceAngleLdvD ;
	}
	public void setRightRosaceAngleLdvD(int angle) {
		_iRightRosaceAngleLdvD = angle ;
	}
	
	public LdvTeamRosaceStructure getStructure() {
		return _structure ;
	}
}
