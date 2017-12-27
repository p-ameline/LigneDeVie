package com.ldv.shared.archetype ;

import junit.framework.TestCase ;

public class LdvArchetypeDialogBoxTest extends TestCase
{
  public void test_Constructors() 
  {
  	// Test default constructor
   	//
  	LdvArchetypeDialogBox DB0 = new LdvArchetypeDialogBox() ;
  	
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
  	LdvArchetypeDialogBox DB2 = new LdvArchetypeDialogBox(DB) ;
  	
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
  	assertTrue(DB2.equals(DB)) ;
  	
  	DB2.setCaption("Other caption") ;
  	
  	assertFalse(DB2.equals(DB)) ;
  } ;
  
  public void test_CoordinatesFct() 
  {
  	LdvArchetypeDialogBox DB = new LdvArchetypeDialogBox("Lang", "Caption", "Type", "Style", "5 2 373 306", "Class", "FontSize", "FontType") ;
  	
  	assertTrue(DB.getX() == 5) ;
  	assertTrue(DB.getY() == 2) ;
  	assertTrue(DB.getW() == 373) ;
  	assertTrue(DB.getH() == 306) ;
  }
}
