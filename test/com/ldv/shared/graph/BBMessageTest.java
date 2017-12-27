package com.ldv.shared.graph ;

import junit.framework.TestCase ;

public class BBMessageTest extends TestCase
{

  public void testConstructor() 
  {
  	// Test default constructor
  	//
  	BBMessage bbMessage = new BBMessage() ; 
  	assertNotNull(bbMessage) ;
  	assertEmptyness(bbMessage) ;  	  	
  }
  
  public void testInitFromLabel()
  {
  	BBMessage bbMessage = new BBMessage() ; 
  	assertNotNull(bbMessage) ;
  	
  	// Test valid labels
   	//
  	bbMessage.initFromLabel("PDI401") ;
  	assertEquals("PDI401", bbMessage.getLexique()) ;
  	assertEquals("", bbMessage.getComplement()) ;
  	assertEquals("", bbMessage.getCertitude()) ;
  	assertEquals("", bbMessage.getUnit()) ;
  	assertEquals("", bbMessage.getPlural()) ;
  	
  	bbMessage.initFromLabel("PDI401.WCE001") ;
  	assertEquals("PDI401", bbMessage.getLexique()) ;
  	assertEquals("", bbMessage.getComplement()) ;
  	assertEquals("WCE001", bbMessage.getCertitude()) ;
  	assertEquals("", bbMessage.getUnit()) ;
  	assertEquals("", bbMessage.getPlural()) ;
  	
  	bbMessage.initFromLabel("PDI401.WCE001.WPLUR1") ;
  	assertEquals("PDI401", bbMessage.getLexique()) ;
  	assertEquals("", bbMessage.getComplement()) ;
  	assertEquals("WCE001", bbMessage.getCertitude()) ;
  	assertEquals("", bbMessage.getUnit()) ;
  	assertEquals("WPLUR1", bbMessage.getPlural()) ;
  	
  	bbMessage.initFromLabel("2DA021.£T0;19.$19980615130952") ;
  	assertEquals("£T0;19", bbMessage.getLexique()) ;
  	assertEquals("19980615130952", bbMessage.getComplement()) ;
  	assertEquals("", bbMessage.getCertitude()) ;
  	assertEquals("2DA021", bbMessage.getUnit()) ;
  	assertEquals("", bbMessage.getPlural()) ;
  	
  	// Test weird labels
   	//
  	bbMessage.initFromLabel("PDI401.") ;  // a separator at end
  	assertEquals("PDI401", bbMessage.getLexique()) ;
  	assertEquals("", bbMessage.getComplement()) ;
  	assertEquals("", bbMessage.getCertitude()) ;
  	assertEquals("", bbMessage.getUnit()) ;
  	assertEquals("", bbMessage.getPlural()) ;
  }
  
  public void assertEmptyness(BBMessage bbMessage)
  {
  	assertNotNull(bbMessage) ;
  	
  	assertEquals("", bbMessage.getTreeID()) ;
  	assertEquals("", bbMessage.getNodeID()) ;
  	assertEquals("", bbMessage.getLexique()) ;
  	assertEquals("", bbMessage.getComplement()) ;
  	assertEquals("", bbMessage.getCertitude()) ;
  	assertEquals("", bbMessage.getUnit()) ;
  	assertEquals("A", bbMessage.getInterest()) ;
  	assertEquals("", bbMessage.getPlural()) ;
  	assertEquals("1", bbMessage.getVisible()) ;
  	assertEquals("", bbMessage.getType()) ;
  	assertEquals("", bbMessage.getFreeText()) ;
  	assertEquals("", bbMessage.getArchetype()) ;
  	assertEquals("", bbMessage.getRights()) ;
  	
  	assertTrue(bbMessage.GetTemporaryLinks().isEmpty()) ;
  }
}
