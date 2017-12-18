package com.ldv.client.canvas;

import junit.framework.TestCase;

public class LdvCoordinatesPolarTest extends TestCase
{

  public void testConstructor() 
  {
  	LdvCoordinatesPolar polar0 = new LdvCoordinatesPolar(0d, 0d) ; 
  	assertNotNull(polar0) ;
  	
  	LdvCoordinatesPolar polar1 = new LdvCoordinatesPolar(4d * Math.PI, 0d) ; 
  	assertNotNull(polar1) ;
  	
  	assertEquals(polar0, polar1) ;
  	
  	LdvCoordinatesPolar polar2 = new LdvCoordinatesPolar(Math.PI / 2d, 3d) ; 
  	assertNotNull(polar2) ;
  	
  	LdvCoordinatesPolar polar3 = new LdvCoordinatesPolar(3d * (Math.PI / 2d), -3d) ; 
  	assertNotNull(polar3) ;
  	
  	assertEquals(polar2, polar3) ;
  }
  
  public void testInitFromCartesian() 
  {
  	LdvCoordinatesCartesian cartesian0 = new LdvCoordinatesCartesian(0d, 1d) ;
  	
  	LdvCoordinatesPolar polar0 = new LdvCoordinatesPolar(cartesian0, null) ; 
  	assertNotNull(polar0) ;
  	
  	// Check with a pole that is not located at X=0, y=0
  	//
  	LdvCoordinatesCartesian cartesian1 = new LdvCoordinatesCartesian(3d, 4d) ;
  	LdvCoordinatesCartesian pole0      = new LdvCoordinatesCartesian(3d, 3d) ;
  	
  	LdvCoordinatesPolar polar1 = new LdvCoordinatesPolar(cartesian1, pole0) ; 
  	assertNotNull(polar1) ;
  	
  	assertEquals(polar0, polar1) ;
  	
  	assertTrue(1d == polar0.getRadius()) ;
  	assertTrue(Math.PI / 2d == polar0.getAngleR()) ;
  	
  	// Check in various quadrants
  	//
  	double rac2on2 = Math.sqrt(2) / 2d ;
  	
  	polar0.initFromCartesian(new LdvCoordinatesCartesian(rac2on2, rac2on2), null) ;
  	assertTrue(1d == polar0.getRadius()) ;
  	assertTrue(Math.PI / 4d == polar0.getAngleR()) ;
  	
  	cartesian0.initFromPolar(polar0, null) ;
  	assertEquals(cartesian0, new LdvCoordinatesCartesian(rac2on2, rac2on2)) ;
  	
  	polar0.initFromCartesian(new LdvCoordinatesCartesian(-rac2on2, rac2on2), null) ;
  	assertTrue(1d == polar0.getRadius()) ;
  	assertTrue(3d * Math.PI / 4d == polar0.getAngleR()) ;
  	
  	cartesian0.initFromPolar(polar0, null) ;
  	assertEquals(cartesian0, new LdvCoordinatesCartesian(-rac2on2, rac2on2)) ;
  	
  	polar0.initFromCartesian(new LdvCoordinatesCartesian(-rac2on2, -rac2on2), null) ;
  	assertTrue(1d == polar0.getRadius()) ;
  	assertTrue(5d * Math.PI / 4d == polar0.getAngleR()) ;
  	
  	cartesian0.initFromPolar(polar0, null) ;
  	assertEquals(cartesian0, new LdvCoordinatesCartesian(-rac2on2, -rac2on2)) ;
  	
  	polar0.initFromCartesian(new LdvCoordinatesCartesian(rac2on2, -rac2on2), null) ;
  	assertTrue(1d == polar0.getRadius()) ;
  	assertTrue(7 * Math.PI / 4d == polar0.getAngleR()) ;
  	
  	cartesian0.initFromPolar(polar0, null) ;
  	assertEquals(cartesian0, new LdvCoordinatesCartesian(rac2on2, -rac2on2)) ;
  }
}
