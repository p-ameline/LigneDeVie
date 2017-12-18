package com.ldv.shared.model ;

import junit.framework.TestCase ;

public class LdvTimeGwtTest extends TestCase
{

  public void testConstructor() 
  {
  	// Test default constructor
  	//
  	LdvTime ldvTime = new LdvTime(0) ; 
  	assertNotNull(ldvTime) ;
  	assertTrue(ldvTime.isEmpty()) ;
  	
  	ldvTime.initFromDateTime("20161226131942") ;
  	assertFalse(ldvTime.isEmpty()) ;
  	assertEquals(2016, ldvTime.getFullYear()) ;
  } ;
  
  public void testISO8601() 
  {
  	LdvTime ldvTime = new LdvTime(5) ; 
  	ldvTime.initFromDateTime("20161226131942") ;
  	
  	assertEquals("2016-12-26T13:19:42", ldvTime.getISO8601DateTime()) ;
  	assertEquals("2016-12-26T18:19:42+05:00Z", ldvTime.getISO8601UTCDateTime()) ;
  }  
}
