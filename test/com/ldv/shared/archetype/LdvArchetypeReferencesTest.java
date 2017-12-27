package com.ldv.shared.archetype ;

import java.util.ArrayList;

import com.ldv.shared.model.LdvArrayUtilities;

import junit.framework.TestCase ;

public class LdvArchetypeReferencesTest extends TestCase
{
  public void test_copyConstructor() 
  {
  	LdvArchetypeReferences references = new LdvArchetypeReferences() ;
  	
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
