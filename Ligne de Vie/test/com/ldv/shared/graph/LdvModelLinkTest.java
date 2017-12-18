package com.ldv.shared.graph ;

import junit.framework.TestCase ;

public class LdvModelLinkTest extends TestCase
{

  public void testConstructor() 
  {
  	// Test default constructor
  	//
  	LdvModelLink ldvLink = new LdvModelLink() ; 
  	assertNotNull(ldvLink) ;
  	  	
  	assertEquals("", ldvLink.getQualified()) ;
  	assertEquals("", ldvLink.getLink()) ;
  	assertEquals("", ldvLink.getQualifier()) ;
  	
  	// Test comprehensive constructor
  	//
  	LdvModelLink ldvLink2 = new LdvModelLink("Qied", "Link", "Qier") ; 
  	assertNotNull(ldvLink2) ;
  	  	
  	assertEquals("Qied", ldvLink2.getQualified()) ;
  	assertEquals("Link", ldvLink2.getLink()) ;
  	assertEquals("Qier", ldvLink2.getQualifier()) ;
  }
  
  public void testLinkInformations() 
  {
  	// Test default constructor
  	//
  	LdvModelLink ldvLink = new LdvModelLink("00000B3000006", "ZDATA", "00000B300000700005") ; 
  	assertNotNull(ldvLink) ;
  	  	
  	assertEquals("00000B3000006", ldvLink.getQualified()) ;
  	assertEquals("ZDATA", ldvLink.getLink()) ;
  	assertEquals("00000B300000700005", ldvLink.getQualifier()) ;
  	
  	assertEquals("00000B3000006", ldvLink.getQualifiedDocumentId()) ;
  	assertEquals("00000B3",       ldvLink.getQualifiedPersonId()) ;
  	assertEquals("000006",        ldvLink.getQualifiedTreeId()) ;
  	assertEquals("",              ldvLink.getQualifiedNodeId()) ;
  	
  	assertEquals("00000B3000007", ldvLink.getQualifierDocumentId()) ;
  	assertEquals("00000B3",            ldvLink.getQualifierPersonId()) ;
  	assertEquals("000007",             ldvLink.getQualifierTreeId()) ;
  	assertEquals("00005",              ldvLink.getQualifierNodeId()) ;
  }
}
