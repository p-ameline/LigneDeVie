package com.ldv.shared.archetype ;

import junit.framework.TestCase ;

public class LdvArchetypeControlTest extends TestCase
{
  public void test_Constructors() 
  {
  	// Test default constructor
   	//
  	LdvArchetypeControl Ctrl0 = new LdvArchetypeControl() ;
  	
  	assertTrue(Ctrl0.getCaption().equals("")) ;
  	assertTrue(Ctrl0.getType().equals("")) ;
  	assertTrue(Ctrl0.getLang().equals("")) ;
  	assertTrue(Ctrl0.getStyleAsString().equals("")) ;
  	assertTrue(Ctrl0.getCoords().equals("")) ;
  	
  	// Test standard constructor
   	//
  	LdvArchetypeControl Ctrl = new LdvArchetypeControl("Caption", "fr", "Type", "Style", "10,20,30,40", "DataIdentity", "DataFunction", "DataExtension", "Filling", "HelpText") ;
  	
  	assertTrue(Ctrl.getCaption().equals("Caption")) ;
  	assertTrue(Ctrl.getHelpText().equals("HelpText")) ;
  	assertTrue(Ctrl.getLang().equals("fr")) ;
  	assertTrue(Ctrl.getType().equals("Type")) ;
  	assertTrue(Ctrl.getStyleAsString().equals("Style")) ;
  	assertTrue(Ctrl.getCoords().equals("10,20,30,40")) ;
  	assertTrue(Ctrl.getDataIdentity().equals("DataIdentity")) ;
  	assertTrue(Ctrl.getDataFunction().equals("DataFunction")) ;
  	assertTrue(Ctrl.getDataExtension().equals("DataExtension")) ;
  	assertTrue(Ctrl.getFilling().equals("Filling")) ;
  	
  	assertTrue(Ctrl.getX() == 10) ;
  	assertTrue(Ctrl.getY() == 20) ;
  	assertTrue(Ctrl.getW() == 30) ;
  	assertTrue(Ctrl.getH() == 40) ;
  	
  	// Test copy constructor
   	//
  	LdvArchetypeControl Ctrl2 = new LdvArchetypeControl(Ctrl) ;
  	
  	assertTrue(Ctrl2.getCaption().equals("Caption")) ;
  	assertTrue(Ctrl2.getHelpText().equals("HelpText")) ;
  	assertTrue(Ctrl2.getLang().equals("fr")) ;
  	assertTrue(Ctrl2.getType().equals("Type")) ;
  	assertTrue(Ctrl2.getStyleAsString().equals("Style")) ;
  	assertTrue(Ctrl2.getCoords().equals("10,20,30,40")) ;
  	assertTrue(Ctrl2.getDataIdentity().equals("DataIdentity")) ;
  	assertTrue(Ctrl2.getDataFunction().equals("DataFunction")) ;
  	assertTrue(Ctrl2.getDataExtension().equals("DataExtension")) ;
  	assertTrue(Ctrl2.getFilling().equals("Filling")) ;
  	
  	assertTrue(Ctrl2.getX() == 10) ;
  	assertTrue(Ctrl2.getY() == 20) ;
  	assertTrue(Ctrl2.getW() == 30) ;
  	assertTrue(Ctrl2.getH() == 40) ;
  	
  	// Test equals operator
   	//
  	assertTrue(Ctrl2.equals(Ctrl)) ;
  	
  	Ctrl2.setCaption("Other title") ;
  	
  	assertFalse(Ctrl2.equals(Ctrl)) ;
  } ;
}
