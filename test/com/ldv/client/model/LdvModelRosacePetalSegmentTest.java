package com.ldv.client.model;

import com.ldv.shared.graph.LdvModelNode;
import com.ldv.shared.graph.LdvModelTree;

import junit.framework.TestCase;

public class LdvModelRosacePetalSegmentTest extends TestCase
{
  public void testConstructor() 
  {
  	LdvModelRosacePetalSegment ldvSegment = new LdvModelRosacePetalSegment() ; 
  	assertNotNull(ldvSegment) ;
  	
  	// Check that data are properly initialized
   	//
   	assertTrue(isProperlyInitialized(ldvSegment)) ;
   	assertTrue(ldvSegment.isEmpty()) ;
  	
   	ldvSegment.setColor("red") ;
   	ldvSegment.setRadius(2) ;
   	
   	assertFalse(ldvSegment.isEmpty()) ;
   	
   	LdvModelRosacePetalSegment ldvSegmentCopy = new LdvModelRosacePetalSegment(ldvSegment) ; 
  	assertNotNull(ldvSegmentCopy) ;
  	
  	assertEquals("red", ldvSegmentCopy.getColor()) ;
   	assertEquals(2,     ldvSegmentCopy.getRadius()) ;
   	assertFalse(ldvSegmentCopy.isEmpty()) ;
  }
  
  public void testWeirdOperations() 
  {
  	// Copy constructor from a null object
  	//
  	LdvModelRosacePetalSegment nullPetalSegment = null ;
  	
  	LdvModelRosacePetalSegment copyPetalSegment = new LdvModelRosacePetalSegment(nullPetalSegment) ; 
  	assertNotNull(copyPetalSegment) ;
  	assertTrue(isProperlyInitialized(copyPetalSegment)) ;
  	
  	// Properly initialized object, then deep copy from a null object
   	//
  	LdvModelTree ldvTree = getRosacePetalSegmentTree() ; 
  	assertNotNull(ldvTree) ;
  	assertNotNull(ldvTree.getNodes()) ;
  	  	
  	assertTrue(copyPetalSegment.initFromTree(ldvTree, ldvTree.getRootNode())) ;
  	
  	copyPetalSegment.deepCopy(nullPetalSegment) ;
  	assertTrue(isProperlyInitialized(copyPetalSegment)) ;
  }
  
  public void testInitFromTree() 
  {
  	LdvModelRosacePetalSegment petalSegment = new LdvModelRosacePetalSegment() ; 
  	assertNotNull(petalSegment) ;
  	
  	LdvModelTree ldvTree = getRosacePetalSegmentTree() ; 
  	assertNotNull(ldvTree) ;
  	assertNotNull(ldvTree.getNodes()) ;
  	
  	assertTrue(petalSegment.initFromTree(ldvTree, ldvTree.getRootNode())) ;
  	
  	assertEquals("red", petalSegment.getColor()) ;
   	assertEquals(2,     petalSegment.getRadius()) ;
  }
  
  protected boolean isProperlyInitialized(final LdvModelRosacePetalSegment petal)
  {
  	if (("".equals(petal.getColor()))           &&
  			(-1 == petal.getRadius()))
  		return true ;

   	return false ; 
  }
  
  protected LdvModelTree getRosacePetalSegmentTree()
  {
  	LdvModelTree ldvTree = new LdvModelTree() ;
  	
  	ldvTree.addNode(new LdvModelNode("0ROSE1"), 0) ;
  	ldvTree.addNode(new LdvModelNode("VDIPA1"), 1) ;
  	ldvTree.addNode(new LdvModelNode("£N0;03", "2", "200001"), 2) ;
  	ldvTree.addNode(new LdvModelNode("0COUL1"), 1) ; 
  	ldvTree.addNode(new LdvModelNode("£C;020", "red", true), 2) ;
  	
  	return ldvTree ;
  }
  	
}
