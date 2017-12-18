package com.ldv.client.canvas;

import junit.framework.TestCase;

public class TrigonometricFctsTest extends TestCase
{
  public void testConstructor() 
  {
  }
  
  public void testRadianFromDegree() 
  {
  	assertEquals(0d,           TrigonometricFcts.getRadianAngleFromDegreeAngle(0)) ;
  	assertEquals(Math.PI / 2d, TrigonometricFcts.getRadianAngleFromDegreeAngle(90)) ;
  	assertEquals(Math.PI,      TrigonometricFcts.getRadianAngleFromDegreeAngle(180)) ;
  	assertEquals(2 * Math.PI,  TrigonometricFcts.getRadianAngleFromDegreeAngle(360)) ;
  	
  	// More than one revolution
  	assertEquals(3 * Math.PI, TrigonometricFcts.getRadianAngleFromDegreeAngle(540)) ;
  }
  
  public void testCanvasRAngleFromLdvDAngle() 
  {
  	assertEquals(0d,               TrigonometricFcts.getCanvasRAngleFromLdvDAngle(180)) ;
    assertEquals(3 * Math.PI / 2d, TrigonometricFcts.getCanvasRAngleFromLdvDAngle(90)) ;
  	assertEquals(Math.PI,          TrigonometricFcts.getCanvasRAngleFromLdvDAngle(0)) ;
  	assertEquals(Math.PI,          TrigonometricFcts.getCanvasRAngleFromLdvDAngle(360)) ;
  	assertEquals(Math.PI / 2d,     TrigonometricFcts.getCanvasRAngleFromLdvDAngle(270)) ;
  }
  
  public void testgetRadianAngleToRotate()
  {
  	assertEquals(0d,           TrigonometricFcts.getRadianAngleToRotate(Math.PI, Math.PI)) ;
  	assertEquals(Math.PI,      TrigonometricFcts.getRadianAngleToRotate(Math.PI / 2d, 3 * Math.PI / 2d)) ;
  	assertEquals(Math.PI,      TrigonometricFcts.getRadianAngleToRotate(3 * Math.PI / 2d, Math.PI / 2d)) ;
  	assertEquals(Math.PI / 2d, TrigonometricFcts.getRadianAngleToRotate(3 * Math.PI / 2d, 0)) ;
  }
}
