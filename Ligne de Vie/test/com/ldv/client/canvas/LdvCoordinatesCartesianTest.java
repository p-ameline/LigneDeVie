package com.ldv.client.canvas;

import junit.framework.TestCase;

public class LdvCoordinatesCartesianTest extends TestCase
{
  public void testConstructor() 
  {
  	LdvCoordinatesCartesian coordinate1 = new LdvCoordinatesCartesian(10d, 15d) ;
  	assertNotNull(coordinate1) ;
  	assertEquals(10d, coordinate1.getX()) ;
  	assertEquals(15d, coordinate1.getY()) ;
  	
  	LdvCoordinatesPolar polar1 = new LdvCoordinatesPolar(3 * Math.PI / 2d, 20d) ;
  	assertNotNull(polar1) ;
  	LdvCoordinatesCartesian polarCenter1 = new LdvCoordinatesCartesian(10d, 35d) ;
  	assertNotNull(polarCenter1) ;
  	LdvCoordinatesCartesian coordinate2 = new LdvCoordinatesCartesian(polar1, polarCenter1) ;
  	assertNotNull(coordinate2) ;
  	
  	assertTrue(coordinate2.equals(coordinate1)) ;
  	
  	LdvCoordinatesPolar polar2 = new LdvCoordinatesPolar(0d, 0d) ;
  	assertNotNull(polar1) ;
  	LdvCoordinatesCartesian coordinate3 = new LdvCoordinatesCartesian(polar2, coordinate1) ;
  	assertNotNull(coordinate3) ;
  	
  	assertTrue(coordinate3.equals(coordinate1)) ;
  }
}
