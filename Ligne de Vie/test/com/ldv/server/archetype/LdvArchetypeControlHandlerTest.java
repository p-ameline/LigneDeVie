package com.ldv.server.archetype ;

import com.ldv.shared.archetype.LdvArchetypeControl;

import junit.framework.TestCase ;

public class LdvArchetypeControlHandlerTest extends TestCase
{
  public void test_Constructors() 
  {
  	// Test default constructor
   	//
  	LdvArchetypeControlHandler Ctrl0 = new LdvArchetypeControlHandler() ;
  	
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
  	LdvArchetypeControlHandler Ctrl2 = new LdvArchetypeControlHandler() ;
  	Ctrl2.initFromArchetypeControl(Ctrl) ;
  	
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
  	Ctrl0.initFromArchetypeControlHandler(Ctrl2) ;
  	
  	assertTrue(Ctrl2.equals(Ctrl0)) ;
  	
  	Ctrl2.setCaption("Other title") ;
  	
  	assertFalse(Ctrl2.equals(Ctrl0)) ;
  } ;
  
  public void test_InitFromValue() 
  {
  	// Test default constructor
   	//
  	LdvArchetypeControlHandler Ctrl = new LdvArchetypeControlHandler() ;
  	 	
  	assertTrue(Ctrl.initDataFromValue("CONTROL \"IDENTITE\", 100, \"BorShade\", BSS_GROUP | BSS_CAPTION | BSS_LEFT | WS_CHILD | WS_VISIBLE | WS_GROUP, 4, 19, 372, 88")) ;
  	
  	assertTrue(Ctrl.getCaption().equals("IDENTITE")) ;
  	assertTrue(Ctrl.getHelpText().equals("")) ;
  	assertTrue(Ctrl.getLang().equals("")) ;
  	assertTrue(Ctrl.getType().equals("BorShade")) ;
  	assertTrue(Ctrl.getStyleAsString().equals("BSS_GROUP | BSS_CAPTION | BSS_LEFT | WS_CHILD | WS_VISIBLE | WS_GROUP")) ;
  	assertTrue(Ctrl.getCoords().equals("4, 19, 372, 88")) ;
  	assertTrue(Ctrl.getDataIdentity().equals("100")) ;
  	
  	assertTrue(Ctrl.getX() == 4) ;
  	assertTrue(Ctrl.getY() == 19) ;
  	assertTrue(Ctrl.getW() == 372) ;
  	assertTrue(Ctrl.getH() == 88) ;  	
  } ;
}
