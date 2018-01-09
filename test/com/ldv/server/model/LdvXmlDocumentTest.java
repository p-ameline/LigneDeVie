package com.ldv.server.model ;

import java.util.Vector;

import org.w3c.dom.Element;

import com.ldv.shared.graph.LdvGraphConfig;
import com.ldv.shared.graph.LdvGraphMapping;
import com.ldv.shared.graph.LdvModelNode;
import com.ldv.shared.graph.LdvModelTree;
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
  	
  	Vector<LdvGraphMapping> aMappings = new Vector<LdvGraphMapping>() ;
  	ldvXmlDocument.initializeElementFromNode(eNode, modelNode, aMappings) ;
  	
  	assertTrue(eNode.getAttribute(LdvXmlDocument.NODE_ID_ATTRIBUTE).equals("NodeId")) ;
  	assertTrue(eNode.getAttribute(LdvXmlDocument.LEXIQUE_ATTRIBUTE).equals("ZPOMR1")) ;
  	assertTrue(eNode.getAttribute(LdvXmlDocument.COMPLEMENT_ATTRIBUTE).equals("Complement")) ;
  	assertTrue(eNode.getAttribute(LdvXmlDocument.PLURAL_ATTRIBUTE).equals("WPLUR1")) ;
  	assertTrue(eNode.getAttribute(LdvXmlDocument.FREETEXT_ATTRIBUTE).equals("FreeText")) ;
  	assertTrue(eNode.getAttribute(LdvXmlDocument.UNIT_ATTRIBUTE).equals("200001")) ;
  }
  
  public void testUnmaskHiddenNodes()
  {
  	// Create a LdvXmlDocument object
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
  	
  	// Start the test
  	//
  	
  	// Model (demographic) tree 
  	//
  	LdvModelTree previousTree = new LdvModelTree() ;
  	
  	previousTree.addNode(new LdvModelNode("0000008", "0000001", "00000", "", "ZADMI1", "", "", "", "", "", "", "", ""), 0) ;
  	previousTree.addNode(new LdvModelNode("0000008", "0000001", "00001", "", "LIDET1", "", "", "", "", "", "", "", ""), 1) ;
  	previousTree.addNode(new LdvModelNode("0000008", "0000001", "00002", "", "LNOM01", "", "", "", "", "", "", "", ""), 2) ;
  	previousTree.addNode(new LdvModelNode("0000008", "0000001", "00003", "", "�CLN00", "", "", "", "", "DUPONT", "", "", ""), 3) ;
  	previousTree.addNode(new LdvModelNode("0000008", "0000001", "00004", "", "LNOM21", "", "", "", "", "", "", "", ""), 2) ;
  	previousTree.addNode(new LdvModelNode("0000008", "0000001", "00005", "", "�CLN00", "", "", "", "", "Pierre", "", "", ""), 3) ;
  	previousTree.addNode(new LdvModelNode("0000008", "0000001", "00006", "", "LSEXE1", "", "", "", "", "", "", "", ""), 2) ;
  	previousTree.addNode(new LdvModelNode("0000008", "0000001", "00007", "", "HMASC2", "", "", "", "", "", "", "", ""), 3) ;
  	previousTree.addNode(new LdvModelNode("0000008", "0000001", "00008", "", "HCIVO1", "", "", "", "", "", "", "", ""), 4) ;
  	previousTree.addNode(new LdvModelNode("0000008", "0000001", "00009", "", "HMONP1", "", "", "", "", "", "", "", ""), 5) ;
  	previousTree.addNode(new LdvModelNode("0000008", "0000001", "0000A", "", "KNAIS1", "", "", "", "", "", "", "", ""), 2) ;
  	previousTree.addNode(new LdvModelNode("0000008", "0000001", "0000B", "", "�D0;10", "19350623", "", "", "2DA011", "", "", "", ""), 3) ;
  	previousTree.addNode(new LdvModelNode("0000008", "0000001", "0000C", "", "LFRAN1", "", "", "", "", "", "", "", ""), 1) ;
  	previousTree.addNode(new LdvModelNode("0000008", "0000001", "0000D", "", "LFRAB1", "", "", "", "", "", "", "", ""), 2) ;
  	previousTree.addNode(new LdvModelNode("0000008", "0000001", "0000E", "", "�CLN00", "", "", "", "", "13506234076", "", "", ""), 3) ;
  	
  	// The same tree, with an obfuscated block 
   	//
  	LdvModelTree newTree = new LdvModelTree() ;
  	
  	newTree.addNode(new LdvModelNode("0000008", "0000001", "00000", "", "ZADMI1", "", "", "", "", "", "", "", ""), 0) ;
  	newTree.addNode(new LdvModelNode("0000008", "0000001", "00001", "", "900001", "", "", "", "", "", "", "", ""), 1) ;
  	newTree.addNode(new LdvModelNode("0000008", "0000001", "0000C", "", "LFRAN1", "", "", "", "", "", "", "", ""), 1) ;
  	newTree.addNode(new LdvModelNode("0000008", "0000001", "0000D", "", "LFRAB1", "", "", "", "", "", "", "", ""), 2) ;
  	newTree.addNode(new LdvModelNode("0000008", "0000001", "0000E", "", "�CLN00", "", "", "", "", "13506234076", "", "", ""), 3) ;
  	
  	ldvXmlDocument.unmaskHiddenNodes(newTree, previousTree) ;
  	
  	assertTrue(newTree.getNodes().isSamePpt(previousTree.getNodes())) ;
  	
  	// The same tree, with two obfuscated block 
   	//
  	LdvModelTree newTree2 = new LdvModelTree() ;
  	
  	newTree2.addNode(new LdvModelNode("0000008", "0000001", "00000", "", "ZADMI1", "", "", "", "", "", "", "", ""), 0) ;
  	newTree2.addNode(new LdvModelNode("0000008", "0000001", "00001", "", "900001", "", "", "", "", "", "", "", ""), 1) ;
  	newTree2.addNode(new LdvModelNode("0000008", "0000001", "0000C", "", "900001", "", "", "", "", "", "", "", ""), 1) ;
  	
  	ldvXmlDocument.unmaskHiddenNodes(newTree2, previousTree) ;
  	
  	assertTrue(newTree2.getNodes().isSamePpt(previousTree.getNodes())) ;
  }
}
