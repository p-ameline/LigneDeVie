package com.ldv.client.model;

import com.ldv.shared.graph.LdvModelNode;
import com.ldv.shared.graph.LdvModelTree;

import junit.framework.TestCase;

public class LdvModelRosacePetalTest extends TestCase
{
  public void testConstructor() 
  {
  	LdvModelRosacePetal petal = new LdvModelRosacePetal() ; 
  	assertNotNull(petal) ;
  	
  	// Check that data are properly initialized
   	//
  	assertTrue(isProperlyInitialized(petal)) ;
  	assertTrue(petal.isEmpty()) ;
  	
   	petal.setLabel("Friends") ;
   	petal.setAngle(90) ;
   	
   	assertFalse(petal.isEmpty()) ;
   	
   	LdvModelRosacePetal copyPetal = new LdvModelRosacePetal(petal) ; 
  	assertNotNull(copyPetal) ;
  	
  	assertEquals("Friends", copyPetal.getLabel()) ;
   	assertEquals(90,        copyPetal.getAngle()) ;
   	assertFalse(copyPetal.isEmpty()) ;
  }
  
  public void testWeirdOperations() 
  {
  	// Copy constructor from a null object
  	//
  	LdvModelRosacePetal nullPetal = null ;
  	
  	LdvModelRosacePetal copyPetal = new LdvModelRosacePetal(nullPetal) ; 
  	assertNotNull(copyPetal) ;
  	assertTrue(isProperlyInitialized(copyPetal)) ;
  	
  	// Properly initialized object, then deep copy from a null object
   	//
  	LdvModelTree ldvTree = getRosacePetalTree() ; 
  	assertNotNull(ldvTree) ;
  	assertNotNull(ldvTree.getNodes()) ;
  	  	
  	assertTrue(copyPetal.initFromTree(ldvTree, ldvTree.getRootNode())) ;
  	
  	copyPetal.deepCopy(null) ;
  	assertTrue(isProperlyInitialized(copyPetal)) ;
  }
  
  public void testInitFromTree() 
  {
  	LdvModelRosacePetal petal = new LdvModelRosacePetal() ; 
  	assertNotNull(petal) ;
  	
  	LdvModelTree ldvTree = getRosacePetalTree() ; 
  	assertNotNull(ldvTree) ;
  	assertNotNull(ldvTree.getNodes()) ;
  	  	
  	assertTrue(petal.initFromTree(ldvTree, ldvTree.getRootNode())) ;
  	
  	assertEquals("Friends", petal.getLabel()) ;
  	assertEquals("HAMIS1",  petal.getMembersCategory()) ;
   	assertEquals(90,        petal.getAngle()) ;
  }
  
  protected boolean isProperlyInitialized(final LdvModelRosacePetal petal)
  {
  	if (("".equals(petal.getLabel()))           &&
  			(-1 == petal.getAngle())                &&
  			(-1 == petal.getLeftRosaceAngleLdvD())  &&
  			(-1 == petal.getRightRosaceAngleLdvD()) &&
  			(petal.getSegments().isEmpty()))
  		return true ;

   	return false ; 
  }
  
  protected LdvModelTree getRosacePetalTree()
  {
  	LdvModelTree ldvTree = new LdvModelTree() ; 
  	
  	ldvTree.addNode(new LdvModelNode("0PETA1"), 0) ;
  	ldvTree.addNode(new LdvModelNode("0TYPM1"), 1) ; 
  	ldvTree.addNode(new LdvModelNode("HAMIS1"), 2) ;
  	ldvTree.addNode(new LdvModelNode("LNOMA1"), 1) ; 
  	ldvTree.addNode(new LdvModelNode("£C;020", "Friends", true), 2) ;
  	ldvTree.addNode(new LdvModelNode("VANPA1"), 1) ;
  	ldvTree.addNode(new LdvModelNode("£N0;03", "90", "2DEGA1"), 2) ;
  	
  	ldvTree.addNode(new LdvModelNode("0ROSE1"), 1) ;
  	ldvTree.addNode(new LdvModelNode("VDIPA1"), 2) ;
  	ldvTree.addNode(new LdvModelNode("£N0;03", "1", "200001"), 3) ;
  	ldvTree.addNode(new LdvModelNode("0COUL1"), 2) ; 
  	ldvTree.addNode(new LdvModelNode("£C;020", "blue", true), 3) ;
  	
  	ldvTree.addNode(new LdvModelNode("0ROSE1"), 1) ;
  	ldvTree.addNode(new LdvModelNode("VDIPA1"), 2) ;
  	ldvTree.addNode(new LdvModelNode("£N0;03", "2", "200001"), 3) ;
  	ldvTree.addNode(new LdvModelNode("0COUL1"), 2) ; 
  	ldvTree.addNode(new LdvModelNode("£C;020", "red", true), 3) ;
  	
  	return ldvTree ;
  }
}
