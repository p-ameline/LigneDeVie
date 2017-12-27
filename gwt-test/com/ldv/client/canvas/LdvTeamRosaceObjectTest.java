package com.ldv.client.canvas;

import com.google.gwt.junit.client.GWTTestCase;

public class LdvTeamRosaceObjectTest extends GWTTestCase
{
  public void testConstructor() 
  {
  }
  
  public void testRadianFromDegree() 
  {
  	assertEquals(0d,           LdvTeamRosaceObject.getRadianAngleFromDegreeAngle(0)) ;
  	assertEquals(Math.PI / 2d, LdvTeamRosaceObject.getRadianAngleFromDegreeAngle(90)) ;
  	assertEquals(Math.PI,      LdvTeamRosaceObject.getRadianAngleFromDegreeAngle(180)) ;
  	// assertEquals(2 * Math.PI, LdvTeamRosaceObject.getRadianAngleFromDegreeAngle(360)) ;
  	
  	// More than one revolution
  	// assertEquals(3 * Math.PI, LdvTeamRosaceObject.getRadianAngleFromDegreeAngle(540)) ;
  }
  
  public void testCanvasRAngleFromLdvDAngle() 
  {
  	assertEquals(0d,           LdvTeamRosaceObject.getCanvasRAngleFromLdvDAngle(180)) ;
  	assertEquals(Math.PI / 2d, LdvTeamRosaceObject.getRadianAngleFromDegreeAngle(90)) ;
  	assertEquals(Math.PI,      LdvTeamRosaceObject.getRadianAngleFromDegreeAngle(0)) ;
  	// assertEquals(2 * Math.PI, LdvTeamRosaceObject.getRadianAngleFromDegreeAngle(360)) ;
  }

	@Override
	public String getModuleName()
	{
		return "com.ldv.Ligne_de_Vie" ;
	}
}
