package com.ldv.shared.calendar ;

import junit.framework.TestCase ;

public class DurationTest extends TestCase
{

  public void testConstructor() 
  {
  	// Test default constructor
  	//
  	Duration duration = new Duration() ; 
  	assertNotNull(duration) ;
  	assertTrue(duration.isZero()) ;
  } ;
  
  /**
   * RFC 5545 provides two examples:
   *
   * A duration of 15 days, 5 hours, and 20 seconds would be:
   *   P15DT5H0M20S
   *
   * A duration of 7 weeks would be:
   *   P7W
   */
  public void testGetValue() 
  {
  	Duration duration = new Duration() ; 
  	assertNotNull(duration) ;
  	
  	duration.setDays(15) ;
  	duration.setHours(5) ;
  	duration.setSeconds(20) ;
  	
  	assertEquals("P15DT5H0M20S", duration.getValue()) ;
  	
  	duration.reset() ;
  	duration.setWeeks(7) ;
  	
  	assertEquals("P7W", duration.getValue()) ;
  }
  
  public void testSetFromString() 
  {
  	Duration duration = new Duration() ; 
  	assertNotNull(duration) ;
  	
  	assertTrue(duration.setFromString("P15DT5H0M20S")) ;
  	assertEquals(0,  duration.getWeeks()) ;
  	assertEquals(15, duration.getDays()) ;
  	assertEquals(5,  duration.getHours()) ;
  	assertEquals(0,  duration.getMinutes()) ;
  	assertEquals(20, duration.getSeconds()) ;
  	
  	assertTrue(duration.setFromString("P7W")) ;
  	assertEquals(7, duration.getWeeks()) ;
  	assertEquals(0, duration.getDays()) ;
  	assertEquals(0, duration.getHours()) ;
  	assertEquals(0, duration.getMinutes()) ;
  	assertEquals(0, duration.getSeconds()) ;
  	
  	assertFalse(duration.setFromString("P7")) ;
  	assertFalse(duration.setFromString("15DT5H0M20S")) ;
  	assertFalse(duration.setFromString("P15DT5HM20S")) ;
  }
}
