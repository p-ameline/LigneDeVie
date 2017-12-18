package com.ldv.shared.model ;

import junit.framework.TestCase ;

public class LdvIntTest extends TestCase
{

  public void testConstructor() 
  {
  	// Test default constructor
  	//
  	LdvInt ldvInt = new LdvInt(2325) ; 
  	assertNotNull(ldvInt) ;
  	assertEquals(2325, ldvInt.getValue()) ;
  	
  	ldvInt.setValue(3212) ;
  	assertEquals(3212, ldvInt.getValue()) ;
  } ;
  
  public void testToStringBoundaries() 
  {
  	LdvInt ldvInt  = new LdvInt(62) ; 
  	assertNotNull(ldvInt) ;
  	assertEquals("10",   ldvInt.intToBaseString(-1, 62)) ;
  	
  	LdvInt ldvInt2 = new LdvInt(3844) ;
  	assertNotNull(ldvInt2) ;
  	assertEquals("100",  ldvInt2.intToBaseString(-1, 62)) ;
  	
  	LdvInt ldvInt3 = new LdvInt(0) ;
  	assertNotNull(ldvInt3) ;
  	assertEquals("0",   ldvInt3.intToBaseString(-1, 62)) ;
  	assertEquals("000", ldvInt3.intToBaseString(3, 62)) ;
  }
  
  public void testToStringFunctions() 
  {
  	LdvInt ldvInt = new LdvInt(622) ; 
  	assertNotNull(ldvInt) ;

  	assertEquals("A2",   ldvInt.intToBaseString(-1, 62)) ;
  	assertEquals("00A2", ldvInt.intToBaseString(4, 62)) ;
  	assertEquals("", ldvInt.intToBaseString(1, 62)) ;
  	
  	assertEquals("622",  ldvInt.intToString(-1)) ;
  	assertEquals("0622", ldvInt.intToString(4)) ;
  	assertEquals("", ldvInt.intToString(2)) ;
  	
  	ldvInt.setValue(3907) ;
  	assertEquals("111",   ldvInt.intToBaseString(-1, 62)) ;
  	assertEquals("0111", ldvInt.intToBaseString(4, 62)) ;
  	assertEquals("", ldvInt.intToBaseString(2, 62)) ;
  	
  	assertEquals("3907",  ldvInt.intToString(-1)) ;
  	assertEquals("3907", ldvInt.intToString(4)) ;
  	assertEquals("", ldvInt.intToString(2)) ;
  }
}
