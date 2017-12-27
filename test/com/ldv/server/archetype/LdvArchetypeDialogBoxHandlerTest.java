package com.ldv.server.archetype ;

import com.ldv.shared.archetype.LdvArchetypeDialogBox;

import junit.framework.TestCase ;

public class LdvArchetypeDialogBoxHandlerTest extends TestCase
{
  public void test_Constructors() 
  {
  	// Test default constructor
   	//
  	LdvArchetypeDialogBoxHandler DB0 = new LdvArchetypeDialogBoxHandler() ;
  	
  	assertTrue(DB0.getCaption().equals("")) ;
  	assertTrue(DB0.getType().equals("")) ;
  	assertTrue(DB0.getLang().equals("")) ;
  	assertTrue(DB0.getStyleAsString().equals("")) ;
  	assertTrue(DB0.getCoords().equals("")) ;
  	assertTrue(DB0.getDialogClass().equals("")) ;
  	assertTrue(DB0.getFontSize().equals("")) ;
  	assertTrue(DB0.getFontType().equals("")) ;
  	
  	// Test standard constructor
   	//
  	LdvArchetypeDialogBox DB = new LdvArchetypeDialogBox("Lang", "Caption", "Type", "Style", "0 0 373 306", "Class", "FontSize", "FontType") ;
  	
  	assertTrue(DB.getCaption().equals("Caption")) ;
  	assertTrue(DB.getType().equals("Type")) ;
  	assertTrue(DB.getLang().equals("Lang")) ;
  	assertTrue(DB.getStyleAsString().equals("Style")) ;
  	assertTrue(DB.getCoords().equals("0 0 373 306")) ;
  	assertTrue(DB.getDialogClass().equals("Class")) ;
  	assertTrue(DB.getFontSize().equals("FontSize")) ;
  	assertTrue(DB.getFontType().equals("FontType")) ;
  	
  	// Test copy constructor
   	//
  	LdvArchetypeDialogBoxHandler DB2 = new LdvArchetypeDialogBoxHandler() ;
  	DB2.initFromArchetypeDialog(DB) ;
  	
  	assertTrue(DB2.getCaption().equals("Caption")) ;
  	assertTrue(DB2.getType().equals("Type")) ;
  	assertTrue(DB2.getLang().equals("Lang")) ;
  	assertTrue(DB2.getStyleAsString().equals("Style")) ;
  	assertTrue(DB2.getCoords().equals("0 0 373 306")) ;
  	assertTrue(DB2.getDialogClass().equals("Class")) ;
  	assertTrue(DB2.getFontSize().equals("FontSize")) ;
  	assertTrue(DB2.getFontType().equals("FontType")) ;
  	
  	// Test equals operator
   	//
  	DB0.initFromArchetypeDialogHandler(DB2) ;
  	
  	assertTrue(DB2.equals(DB)) ;
  	
  	DB2.setCaption("Other caption") ;
  	
  	assertFalse(DB2.equals(DB)) ;
  } ;
}
