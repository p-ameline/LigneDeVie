package com.ldv.test;

import com.google.gwt.junit.client.GWTTestCase;
import com.ldv.shared.model.LdvInt;

public class LdvIntTest extends GWTTestCase
{
	@Override
  public String getModuleName() 
  {
  	return "com.ldv.Ligne_de_Vie" ;
  }
 
  public void testInit() 
  {
  	LdvInt ldvInt1 = new LdvInt(10) ; 
  	assertNotNull(ldvInt1) ;
  	assertEquals("", ldvInt1.intToString(1)) ;
  	assertEquals("10", ldvInt1.intToString(2)) ;
  	assertEquals("010", ldvInt1.intToString(3)) ;
  	assertEquals("0010", ldvInt1.intToString(4)) ;
  	assertEquals("10", ldvInt1.intToString(-1)) ;
  }
}
