package com.ldv.client.model;

import com.ldv.shared.graph.LdvGraphConfig;
import com.ldv.shared.graph.LdvModelNode;
import com.ldv.shared.graph.LdvModelTree;

import junit.framework.TestCase;

public class LdvModelRosaceTest extends TestCase
{
  public void testConstructor() 
  {
  	LdvModelRosace rosace = new LdvModelRosace() ; 
  	assertNotNull(rosace) ;
  	
  	// Check that data are properly initialized
   	//
  	assertTrue(isProperlyInitialized(rosace)) ;
  	assertTrue(rosace.isEmpty()) ;
  	
  	rosace.setTeamCategory("LEQSO1");
   	rosace.setLabel("Social team") ;
   	rosace.setID("testID") ;
   	
   	assertFalse(rosace.isEmpty()) ;
   	
   	LdvModelRosace copyRosace = new LdvModelRosace(rosace) ; 
  	assertNotNull(copyRosace) ;
  	
  	assertEquals("LEQSO1",      copyRosace.getTeamCategory()) ;
  	assertEquals("Social team", copyRosace.getLabel()) ;
   	assertEquals("testID",      copyRosace.getID()) ;
   	assertFalse(copyRosace.isEmpty()) ;
  }
  
  public void testInitFromTree() 
  {
  	LdvModelRosace rosace = new LdvModelRosace() ; 
  	assertNotNull(rosace) ;
  	
  	LdvModelTree ldvTree = getRosaceTree() ; 
  	assertNotNull(ldvTree) ;
  	assertNotNull(ldvTree.getNodes()) ;
  	  	
  	assertTrue(rosace.initFromTree(ldvTree, ldvTree.getRootNode())) ;
  	
  	assertEquals("LEQSO1",      rosace.getTeamCategory()) ;
  	assertEquals("Social team", rosace.getLabel()) ;
   	assertEquals(getURI(),      rosace.getID()) ;
  }
  
  public void testWeirdOperations() 
  {
  	// Copy constructor from a null object
  	//
  	LdvModelRosace nullRosace = null ;
  	
  	LdvModelRosace copyRosace = new LdvModelRosace(nullRosace) ; 
  	assertNotNull(copyRosace) ;
  	assertTrue(isProperlyInitialized(copyRosace)) ;
  	
  	// Properly initialized object, then deep copy from a null object
   	//
  	LdvModelTree ldvTree = getRosaceTree() ; 
  	assertNotNull(ldvTree) ;
  	assertNotNull(ldvTree.getNodes()) ;
  	  	
  	assertTrue(copyRosace.initFromTree(ldvTree, ldvTree.getRootNode())) ;
  	
  	copyRosace.deepCopy(nullRosace) ;
  	assertTrue(isProperlyInitialized(copyRosace)) ;
  }
  
  protected boolean isProperlyInitialized(final LdvModelRosace rosace)
  {
  	if (("".equals(rosace.getID()))    &&
  			("".equals(rosace.getLabel())) &&
  			(rosace.getPetals().isEmpty()))
  		return true ;

   	return false ; 
  }
  
  protected LdvModelTree getRosaceTree()
  {
  	LdvModelTree ldvTree = new LdvModelTree() ; 
  	
  	ldvTree.addNode(new LdvModelNode("0ROSA1"), 0) ;
  	ldvTree.addNode(new LdvModelNode("0TYPT1"), 1) ; 
  	ldvTree.addNode(new LdvModelNode("LEQSO1"), 2) ;
  	ldvTree.addNode(new LdvModelNode("LNOMA1"), 1) ; 
  	ldvTree.addNode(new LdvModelNode("£C;020", "Social team", true), 2) ;
  	
  	// Must be done this way since addNode doesn't keep Person and Tree Ids
  	//
  	LdvModelNode rootNode = ldvTree.getRootNode() ;
  	rootNode.setPersonID(getPersonId()) ;
  	rootNode.setTreeID(getTreeId()) ;
  	rootNode.setNodeID(getNodeId()) ;
  	
  	// Petal 1
  	//
  	ldvTree.addNode(new LdvModelNode("0PETA1"), 1) ;
  	ldvTree.addNode(new LdvModelNode("LNOMA1"), 2) ; 
  	ldvTree.addNode(new LdvModelNode("£C;020", "Friends", true), 3) ;
  	ldvTree.addNode(new LdvModelNode("VANPA1"), 2) ;
  	ldvTree.addNode(new LdvModelNode("£N0;03", "90", "2DEGA1"), 3) ;
  	
  	ldvTree.addNode(new LdvModelNode("0ROSE1"), 2) ;
  	ldvTree.addNode(new LdvModelNode("VDIPA1"), 3) ;
  	ldvTree.addNode(new LdvModelNode("£N0;03", "1", "200001"), 4) ;
  	ldvTree.addNode(new LdvModelNode("0COUL1"), 3) ; 
  	ldvTree.addNode(new LdvModelNode("£C;020", "blue", true), 4) ;
  	
  	ldvTree.addNode(new LdvModelNode("0ROSE1"), 2) ;
  	ldvTree.addNode(new LdvModelNode("VDIPA1"), 3) ;
  	ldvTree.addNode(new LdvModelNode("£N0;03", "2", "200001"), 4) ;
  	ldvTree.addNode(new LdvModelNode("0COUL1"), 3) ; 
  	ldvTree.addNode(new LdvModelNode("£C;020", "cyan", true), 4) ;

  	// Petal 2
   	//
   	ldvTree.addNode(new LdvModelNode("0PETA1"), 1) ;
   	ldvTree.addNode(new LdvModelNode("LNOMA"), 2) ; 
   	ldvTree.addNode(new LdvModelNode("£C;020", "Familly", true), 3) ;
   	ldvTree.addNode(new LdvModelNode("VANPA1"), 2) ;
   	ldvTree.addNode(new LdvModelNode("£N0;03", "180", "2DEGA1"), 3) ;
   	
   	ldvTree.addNode(new LdvModelNode("0ROSE1"), 2) ;
   	ldvTree.addNode(new LdvModelNode("VDIPA1"), 3) ;
   	ldvTree.addNode(new LdvModelNode("£N0;03", "1", "200001"), 4) ;
   	ldvTree.addNode(new LdvModelNode("0COUL1"), 3) ; 
   	ldvTree.addNode(new LdvModelNode("£C;020", "red", true), 4) ;
   	
   	ldvTree.addNode(new LdvModelNode("0ROSE1"), 2) ;
   	ldvTree.addNode(new LdvModelNode("VDIPA1"), 3) ;
   	ldvTree.addNode(new LdvModelNode("£N0;03", "2", "200001"), 4) ;
   	ldvTree.addNode(new LdvModelNode("0COUL1"), 3) ; 
   	ldvTree.addNode(new LdvModelNode("£C;020", "pink", true), 4) ;
  	
  	return ldvTree ;
  }
  
  protected String getPersonId()
  {
  	String sPersonId = "" ;
  	for (int i = 0 ; i < LdvGraphConfig.PERSON_ID_LEN ; i++) 
  		sPersonId += "p" ;
  	return sPersonId ;
  }
  
  protected String getTreeId()
  {
  	String sTreeId = "" ;
  	for (int i = 0 ; i < LdvGraphConfig.TREE_ID_LEN ; i++) 
  		sTreeId += "t" ;
  	return sTreeId ;
  }
  
  protected String getNodeId()
  {
  	String sNodeId = "" ;
  	for (int i = 0 ; i < LdvGraphConfig.NODE_ID_LEN ; i++) 
  		sNodeId += "n" ;
  	return sNodeId ;
  }
  
  protected String getURI()
  {
  	return getPersonId() + getTreeId() + getNodeId() ;
  }
}
