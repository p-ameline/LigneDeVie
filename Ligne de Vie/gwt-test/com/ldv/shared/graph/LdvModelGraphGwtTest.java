package com.ldv.shared.graph ;

import com.ldv.shared.graph.LdvModelGraph.NSGRAPHTYPE;
import com.ldv.shared.model.LdvTime;

import junit.framework.TestCase ;

public class LdvModelGraphGwtTest extends TestCase
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
}
