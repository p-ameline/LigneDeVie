package com.ldv.server.model ;

import org.w3c.dom.Element;

import com.ldv.shared.graph.LdvGraphConfig;
import com.ldv.shared.graph.LdvModelNode;
import com.ldv.shared.model.DocumentLabel;
import com.ldv.shared.model.LdvTime;

import junit.framework.TestCase ;

public class LdvXmlDocumentTest extends TestCase
{
  public void testConstructor() 
  {
  	// Test default constructor
  	//
  	int    iType        = LdvGraphConfig.COLLECTIVE_SERVER ;
  	String sUnknownUser = LdvGraphConfig.UNKNOWN_USER[iType] ;
  	
  	LdvXmlGraph ldvXmlGraph = new LdvXmlGraph(iType, sUnknownUser, sUnknownUser) ; 
  	assertNotNull(ldvXmlGraph) ;
  	
  	LdvTime timeNow = new LdvTime(0) ;
		timeNow.takeTime() ;
  	
  	DocumentLabel rootLabelDoc = new DocumentLabel(ldvXmlGraph.getMaxTreeId(), LdvGraphConfig.SYSTEM_USER, "ZCS001", "root", "HHUMA3", timeNow) ;
  	assertNotNull(rootLabelDoc) ;
  	
  	LdvXmlDocument ldvXmlDocument = new LdvXmlDocument(ldvXmlGraph, rootLabelDoc) ; 
  	assertNotNull(ldvXmlDocument) ;
  	
  	// assertEquals("", ldvXmlDocument.getMaxCollectiveNodeId()) ;
  	assertTrue(ldvXmlDocument.isCollectiveServer()) ;
  	assertFalse(ldvXmlDocument.isGroupServer()) ;
  	assertFalse(ldvXmlDocument.isLocalServer()) ;
  	
  	String sNexNodeId = ldvXmlDocument.getNextNodeId("") ;
  	
  	assertEquals("00000", sNexNodeId) ;
  	
  	sNexNodeId = ldvXmlDocument.getNextNodeId(sNexNodeId) ;
  	
  	assertEquals("00001", sNexNodeId) ;
  	
  	String sDocAsString = LdvXmlDocument.documentToString(ldvXmlDocument.getFinalDocument()) ;
  	assertFalse(sDocAsString.equals("")) ;
  }
  
  public void testInitialize()
  {
  	int    iType        = LdvGraphConfig.COLLECTIVE_SERVER ;
  	String sUnknownUser = LdvGraphConfig.UNKNOWN_USER[iType] ;
  	
  	LdvXmlGraph ldvXmlGraph = new LdvXmlGraph(iType, sUnknownUser, sUnknownUser) ; 
  	assertNotNull(ldvXmlGraph) ;
  	
  	LdvTime timeNow = new LdvTime(0) ;
		timeNow.takeTime() ;
  	
  	DocumentLabel rootLabelDoc = new DocumentLabel(ldvXmlGraph.getMaxTreeId(), LdvGraphConfig.SYSTEM_USER, "ZCS001", "root", "HHUMA3", timeNow) ;
  	assertNotNull(rootLabelDoc) ;
  	
  	LdvXmlDocument ldvXmlDocument = new LdvXmlDocument(ldvXmlGraph, rootLabelDoc) ; 
  	assertNotNull(ldvXmlDocument) ;
  	
  	LdvModelNode modelNode = new LdvModelNode("PersonId", "TreeId", "NodeId", 
  			                                      "Type", 
  			                                      "ZPOMR1",
                                              "Complement", 
                                              "Certitude", 
                                              "WPLUR1", 
                                              "200001",
                                              "FreeText", 
                                              "Loc", 
                                              "Visible", 
                                              "Interest") ;
  	Element eNode = ldvXmlDocument.getTree().createElement(LdvXmlDocument.NODE_LABEL) ;
  	ldvXmlDocument.initializeElementFromNode(eNode, modelNode) ;
  	
  	assertTrue(eNode.getAttribute(LdvXmlDocument.NODE_ID_ATTRIBUTE).equals("NodeId")) ;
  	assertTrue(eNode.getAttribute(LdvXmlDocument.LEXIQUE_ATTRIBUTE).equals("ZPOMR1")) ;
  	assertTrue(eNode.getAttribute(LdvXmlDocument.COMPLEMENT_ATTRIBUTE).equals("Complement")) ;
  	assertTrue(eNode.getAttribute(LdvXmlDocument.PLURAL_ATTRIBUTE).equals("WPLUR1")) ;
  	assertTrue(eNode.getAttribute(LdvXmlDocument.FREETEXT_ATTRIBUTE).equals("FreeText")) ;
  	assertTrue(eNode.getAttribute(LdvXmlDocument.UNIT_ATTRIBUTE).equals("200001")) ;
  }
}
