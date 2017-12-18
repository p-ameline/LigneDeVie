package com.ldv.shared.model ;

import java.util.ArrayList;

import junit.framework.TestCase ;

public class LdvArrayUtilitiesGwtTest extends TestCase
{

  public void test_isStringInArray() 
  {
  	// Test bad parameters
  	//
  	assertFalse(LdvArrayUtilities.isStringInArray(null, null)) ;
  	assertFalse(LdvArrayUtilities.isStringInArray("test", null)) ;
  	
  	ArrayList<String> aStrings = new ArrayList<String>() ;
  	
  	assertFalse(LdvArrayUtilities.isStringInArray("test", aStrings)) ;
  	
  	aStrings.add("one") ;
  	aStrings.add("two") ;
  	aStrings.add("three") ;
  	aStrings.add("four") ;
  	
  	assertTrue(LdvArrayUtilities.isStringInArray("one", aStrings)) ;
  	assertTrue(LdvArrayUtilities.isStringInArray("two", aStrings)) ;
  	assertTrue(LdvArrayUtilities.isStringInArray("three", aStrings)) ;
  	assertTrue(LdvArrayUtilities.isStringInArray("four", aStrings)) ;
  	
  	assertFalse(LdvArrayUtilities.isStringInArray("five", aStrings)) ;
  	assertFalse(LdvArrayUtilities.isStringInArray("", aStrings)) ;
  } ;  
}
