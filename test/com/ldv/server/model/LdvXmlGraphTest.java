package com.ldv.server.model ;

import com.ldv.shared.graph.LdvGraphConfig;
import com.ldv.shared.graph.LdvModelNode;
import com.ldv.shared.graph.LdvModelTree;
import com.ldv.shared.model.LdvTime;

import junit.framework.TestCase ;

public class LdvXmlGraphTest extends TestCase
{
  public void testConstructor() 
  {
  	// Test default constructor
  	//
  	int    iType        = LdvGraphConfig.COLLECTIVE_SERVER ;
  	String sUnknownUser = LdvGraphConfig.UNKNOWN_USER[iType] ;
  	
  	LdvXmlGraph ldvXmlGraph = new LdvXmlGraph(iType, sUnknownUser, sUnknownUser) ;
  	assertNotNull(ldvXmlGraph) ;
  	// assertNotNull(ldvXmlGraph.getLinks()) ;
  	
  	ldvXmlGraph.getNexTreeId() ;
  	assertEquals("000000", ldvXmlGraph.getMaxTreeId()) ;
  	ldvXmlGraph.getNexTreeId() ;
  	assertEquals("000001", ldvXmlGraph.getMaxTreeId()) ;
  }
  
  public void testMandateFunctions() 
  {
  	int    iType        = LdvGraphConfig.COLLECTIVE_SERVER ;
  	String sUnknownUser = LdvGraphConfig.UNKNOWN_USER[iType] ;
  	
  	LdvXmlGraph ldvXmlGraph = new LdvXmlGraph(iType, sUnknownUser, sUnknownUser) ; 
  	assertNotNull(ldvXmlGraph) ;
  	
  	LdvModelTree tree = new LdvModelTree() ;
  	assertNotNull(tree) ;
  	
  	LdvTime dNow = new LdvTime(0) ;
  	dNow.takeTime() ;
  	
  	LdvTime dNoLimit = new LdvTime(0) ;
  	dNoLimit.setNoLimit() ;
  	
  	ldvXmlGraph.addMandate(tree, "LROOT1", 23, 2, dNow, dNoLimit) ;
  
  	LdvModelNode rootNode = tree.findNode(0, 2) ;
  	assertNotNull(rootNode) ;
  	
  	int iPositIndex = tree.findFirstSonIndex(0) ;
  	assertFalse(-1 == iPositIndex) ;
  }
}
