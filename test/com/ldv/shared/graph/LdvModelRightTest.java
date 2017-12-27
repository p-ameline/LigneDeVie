package com.ldv.shared.graph ;

import junit.framework.TestCase ;

public class LdvModelRightTest extends TestCase
{

  public void testConstructor() 
  {
  	// Test default constructor
  	//
  	LdvModelRight ldvRight = new LdvModelRight() ; 
  	assertNotNull(ldvRight) ;
  	  	
  	assertEquals("", ldvRight.getNode()) ;
  	assertEquals("", ldvRight.getRight()) ;
  	
  	// Test comprehensive constructor
  	//
  	LdvModelRight ldvRight2 = new LdvModelRight("Node", "Right") ; 
  	assertNotNull(ldvRight2) ;
  	  	
  	assertEquals("Node",  ldvRight2.getNode()) ;
  	assertEquals("Right", ldvRight2.getRight()) ;
  }
}
