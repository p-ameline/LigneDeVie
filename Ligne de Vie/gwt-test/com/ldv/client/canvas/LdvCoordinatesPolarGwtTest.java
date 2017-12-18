package com.ldv.client.canvas;

import junit.framework.TestCase;

public class LdvCoordinatesPolarGwtTest extends TestCase
{

  public void testConstructor() 
  {
  	LdvCoordinatesPolar polar0 = new LdvCoordinatesPolar(0, 0) ; 
  	assertNotNull(polar0) ;
  	
  	LdvCoordinatesPolar polar1 = new LdvCoordinatesPolar(4 * Math.PI, 0) ; 
  	assertNotNull(polar1) ;
  	
  	assertEquals(polar0, polar1) ;
  	
  	LdvCoordinatesPolar polar2 = new LdvCoordinatesPolar(Math.PI / 2, 3) ; 
  	assertNotNull(polar2) ;
  	
  	LdvCoordinatesPolar polar3 = new LdvCoordinatesPolar(3 * (Math.PI / 2), -3) ; 
  	assertNotNull(polar3) ;
  	
  	assertEquals(polar2, polar3) ;
  }
  
  public void testInitFromCartesian() 
  {
  	LdvCoordinatesCartesian cartesian0 = new LdvCoordinatesCartesian(0, 1) ;
  	
  	LdvCoordinatesPolar polar0 = new LdvCoordinatesPolar(cartesian0, null) ; 
  	assertNotNull(polar0) ;
  	
  	// Check with a pole that is not located at X=0, y=0
  	//
  	LdvCoordinatesCartesian cartesian1 = new LdvCoordinatesCartesian(3, 4) ;
  	LdvCoordinatesCartesian pole0      = new LdvCoordinatesCartesian(3, 3) ;
  	
  	LdvCoordinatesPolar polar1 = new LdvCoordinatesPolar(cartesian1, pole0) ; 
  	assertNotNull(polar1) ;
  	
  	assertEquals(polar0, polar1) ;
  	
  	assertTrue(1 == polar0.getRadius()) ;
  	assertTrue(Math.PI / 2 == polar0.getAngleR()) ;
  	
  	// Check in various quadrants
  	//
  	double rac2on2 = Math.sqrt(2) / 2 ;
  	
  	polar0.initFromCartesian(new LdvCoordinatesCartesian(rac2on2, rac2on2), null) ;
  	assertTrue(1 == polar0.getRadius()) ;
  	assertTrue(Math.PI / 4 == polar0.getAngleR()) ;
  	
  	cartesian0.initFromPolar(polar0, null) ;
  	assertEquals(cartesian0, new LdvCoordinatesCartesian(rac2on2, rac2on2)) ;
  	
  	polar0.initFromCartesian(new LdvCoordinatesCartesian(-rac2on2, rac2on2), null) ;
  	assertTrue(1 == polar0.getRadius()) ;
  	assertTrue(3 * Math.PI / 4 == polar0.getAngleR()) ;
  	
  	cartesian0.initFromPolar(polar0, null) ;
  	assertEquals(cartesian0, new LdvCoordinatesCartesian(-rac2on2, rac2on2)) ;
  	
  	polar0.initFromCartesian(new LdvCoordinatesCartesian(-rac2on2, -rac2on2), null) ;
  	assertTrue(1 == polar0.getRadius()) ;
  	assertTrue(5 * Math.PI / 4 == polar0.getAngleR()) ;
  	
  	cartesian0.initFromPolar(polar0, null) ;
  	assertEquals(cartesian0, new LdvCoordinatesCartesian(-rac2on2, -rac2on2)) ;
  	
  	polar0.initFromCartesian(new LdvCoordinatesCartesian(rac2on2, -rac2on2), null) ;
  	assertTrue(1 == polar0.getRadius()) ;
  	assertTrue(7 * Math.PI / 4 == polar0.getAngleR()) ;
  	
  	cartesian0.initFromPolar(polar0, null) ;
  	assertEquals(cartesian0, new LdvCoordinatesCartesian(rac2on2, -rac2on2)) ;
  }
}
