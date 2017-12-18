package com.ldv.shared.graph ;

import com.ldv.shared.graph.LdvModelGraph.NSGRAPHTYPE;

import junit.framework.TestCase ;

public class LdvModelGraphTest extends TestCase
{

  public void testConstructor() 
  {
  	// Test default constructor
  	//
  	LdvModelGraph ldvGraph = new LdvModelGraph() ; 
  	assertNotNull(ldvGraph) ;
  	assertNotNull(ldvGraph.getTrees()) ;
  	assertNotNull(ldvGraph.getLinks()) ;
  	assertNotNull(ldvGraph.getRights()) ;
  	  	
  	String sDefaultRootId = LdvGraphConfig.UNKNOWN_USER[1] + LdvGraphConfig.UNKNOWN_ROOTDOC[1] ;
  	
  	assertEquals(sDefaultRootId, ldvGraph.getRootID()) ;
  	assertTrue(NSGRAPHTYPE.personGraph == ldvGraph.getGraphType()) ;
  	
  	// Test comprehensive constructor
  	//
  	LdvModelGraph ldvGraph2 = new LdvModelGraph(NSGRAPHTYPE.personGraph) ; 
  	assertNotNull(ldvGraph2) ;
  	assertNotNull(ldvGraph2.getTrees()) ;
  	assertNotNull(ldvGraph2.getLinks()) ;
  	assertNotNull(ldvGraph2.getRights()) ;
  	  	
  	assertEquals(sDefaultRootId, ldvGraph2.getRootID()) ;
  	assertTrue(NSGRAPHTYPE.personGraph == ldvGraph2.getGraphType()) ;
  	
  	ldvGraph2.setGraphType(NSGRAPHTYPE.objectGraph) ;
  	assertTrue(NSGRAPHTYPE.objectGraph == ldvGraph2.getGraphType()) ;
  } ;
  
  public void testGetIncrementedTreeId()
  {
  	LdvModelGraph ldvGraph = new LdvModelGraph() ;
  	
  	assertEquals("001", ldvGraph.getIncrementedTreeId("000")) ;
  	assertEquals("005", ldvGraph.getIncrementedTreeId("004")) ;
  	assertEquals("00A", ldvGraph.getIncrementedTreeId("009")) ;
  	assertEquals("00D", ldvGraph.getIncrementedTreeId("00C")) ;
  	assertEquals("0F0", ldvGraph.getIncrementedTreeId("0EZ")) ;
  	
  	assertEquals("", ldvGraph.getIncrementedTreeId("#ZZ")) ;
  }
}
