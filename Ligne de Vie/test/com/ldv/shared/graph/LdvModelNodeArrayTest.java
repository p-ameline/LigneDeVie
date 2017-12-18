package com.ldv.shared.graph ;

import java.util.Iterator;

import junit.framework.TestCase ;

public class LdvModelNodeArrayTest extends TestCase
{
  public void testConstructor() 
  { 	
  	// Test comprehensive constructor
  	//
  	LdvModelNodeArray patPatho = new LdvModelNodeArray() ;
  	initializePatPatho(patPatho) ;
  	
  	// Get root node
  	//
  	LdvModelNode rootNode = patPatho.getFirstRootNode() ;
  	assertNotNull(rootNode) ;
  	assertTrue("ZPOMR1".equals(rootNode.getLexicon())) ;
  	
  	// The root node shouldn't have a brother
  	//
  	LdvModelNode rootBrother = patPatho.getNextBrother(rootNode) ;
  	assertNull(rootBrother) ;
  	
  	// There should be a son
   	//
   	LdvModelNode firstRootSon = patPatho.getFirstSon(rootNode) ;
   	assertNotNull(firstRootSon) ;
   	assertTrue("0PRO11".equals(firstRootSon.getLexicon())) ;
   	
   	// With 2 brothers
   	//
   	LdvModelNode firstRootSonBro = patPatho.getNextBrother(firstRootSon) ;
   	assertNotNull(firstRootSonBro) ;
   	assertTrue("0OBJE1".equals(firstRootSonBro.getLexicon())) ;
   	
   	LdvModelNode secondRootSonBro = patPatho.getNextBrother(firstRootSonBro) ;
   	assertNotNull(secondRootSonBro) ;
   	assertTrue("N00001".equals(secondRootSonBro.getLexicon())) ;
   	
   	// But not 3
   	//
   	LdvModelNode thirdRootSonBro = patPatho.getNextBrother(secondRootSonBro) ;
   	assertNull(thirdRootSonBro) ;
  }
  
  public void testExtractPatPatho() 
  { 	
  	// Test comprehensive constructor
  	//
  	LdvModelNodeArray patPatho = new LdvModelNodeArray() ;
  	initializePatPatho(patPatho) ;
  	
  	// Find node "PDI401"
  	//
  	LdvModelNode diabNode = null ;
  	
  	boolean bFoundNode = false ;
  	for (Iterator<LdvModelNode> iter = patPatho.iterator() ; iter.hasNext() && (false == bFoundNode) ; )
  	{
  		diabNode = iter.next() ;
  		if ("PDI401".equals(diabNode.getLexicon()))
  			bFoundNode = true ;
  	}
		
  	if (false == bFoundNode)
  		return ;
  	
  	LdvModelNodeArray sonsPatPatho = new LdvModelNodeArray() ;
  	patPatho.extractPatPatho(diabNode, sonsPatPatho) ;
  	
  	LdvModelNodeArray comparePatPatho = new LdvModelNodeArray() ;
  	comparePatPatho.addNode("KOUVR1", null, 0) ;
  	BBMessage dateMsg = new BBMessage() ;
   	dateMsg.setComplement("20020403131356") ;
   	dateMsg.setUnit("2DA021") ;
   	comparePatPatho.addNode("£T0;19", dateMsg, 1) ;
   	comparePatPatho.addNode("VQUAN1", null, 0) ;
   	comparePatPatho.addNode("KDAT01", null, 1) ;
   	comparePatPatho.addNode("£T0;19", dateMsg, 2) ;
   	comparePatPatho.addNode("VIGRA1", null, 1) ;
   	BBMessage severity = new BBMessage() ;
   	severity.setComplement("052") ;
   	severity.setUnit("200001") ;
   	comparePatPatho.addNode("£N2;04", severity, 2) ;
   	
   	assertTrue(sonsPatPatho.hasSameContent(comparePatPatho)) ;
  }
  
  public void testFindItem() 
  {
  	LdvModelNodeArray testPatPatho = new LdvModelNodeArray() ;
  	initializePatPatho(testPatPatho) ;
  	
  	LdvModelNode foundNode = testPatPatho.findItem("VQUAN1") ;
  	assertNotNull(foundNode) ;
   	assertTrue("VQUAN1".equals(foundNode.getLexicon())) ;
   	
   	LdvModelNode notFoundNode = testPatPatho.findItem("VTEST1") ;
  	assertNull(notFoundNode) ;
  }
  
  public void testExtractVectorOfBrothersPatPatho() 
  {
  	LdvModelNodeArray testPatPatho = new LdvModelNodeArray() ;
  	initializePatPatho(testPatPatho) ;
  	
  	LdvModelNode foundNode = testPatPatho.findItem("PHTA01") ;
  	assertNotNull(foundNode) ;
   	assertTrue("PHTA01".equals(foundNode.getLexicon())) ;
   	
   	LdvModelNodeArrayArray vect = new LdvModelNodeArrayArray() ; 
   	
   	testPatPatho.extractVectorOfBrothersPatPatho(foundNode, vect) ;
   	
   	assertFalse(vect.hasEmptyContent()) ;
   	assertTrue(3 == vect.size()) ;
  }
  
  public void testInsertVector()
  {
  	LdvModelNodeArray testPatPatho = new LdvModelNodeArray() ;
  	initializePatPatho(testPatPatho) ;
  	
  	LdvModelNodeArray insertPatPatho = new LdvModelNodeArray() ;
  	insertPatPatho.addNode("PAI011", null, 0) ;
  	insertPatPatho.addNode("KOUVR1", null, 1) ;
   	BBMessage dateMsg = new BBMessage() ;
   	dateMsg.setComplement("20160405132754") ;
   	insertPatPatho.addNode("£T0;19", dateMsg, 2) ;
   	insertPatPatho.addNode("KFERM1", null, 1) ;
   	dateMsg.setComplement("20160417132754") ;
   	insertPatPatho.addNode("£T0;19", dateMsg, 2) ;
   	insertPatPatho.addNode("VQUAN1", null, 1) ;
   	insertPatPatho.addNode("KDAT01", null, 2) ;
   	dateMsg.setComplement("20160405132754") ;
   	insertPatPatho.addNode("£T0;19", dateMsg, 3) ;
   	insertPatPatho.addNode("VIGRA1", null, 2) ;
   	BBMessage severity = new BBMessage() ;
   	severity.setComplement("020") ;
   	insertPatPatho.addNode("£N2;04", severity, 3) ;
   	
   	LdvModelNode insertBeforeNode = testPatPatho.findItem("0OBJE1") ;
  	assertNotNull(insertBeforeNode) ;
   	assertTrue("0OBJE1".equals(insertBeforeNode.getLexicon())) ;
   	
   	testPatPatho.insertVector(insertBeforeNode, insertPatPatho, insertBeforeNode.getCol() + 1, false) ;
   	
   	LdvModelNode angineNode = testPatPatho.findItem("PAI011") ;
  	assertNotNull(angineNode) ;
   	assertEquals(angineNode.getLine(), 28) ;
   	assertEquals(angineNode.getCol(), 2) ;
  }
  
  public void testRefreshLineNumbers() 
  {
  	LdvModelNodeArray testPatPatho = new LdvModelNodeArray() ;
  	initializePatPatho(testPatPatho) ;
  	
  	// Test refresh all
  	//
  	for (Iterator<LdvModelNode> iter = testPatPatho.iterator() ; iter.hasNext() ; )
  		iter.next().setLine(0) ;
  	
  	testPatPatho.refreshLines(null) ;
  	
  	int iLine = LdvModelNodeArray.ORIGINE_PATH_PATHO ;
  	for (Iterator<LdvModelNode> iter = testPatPatho.iterator() ; iter.hasNext() ; )
  		assertEquals(iter.next().getLine(), iLine++) ;
  	
  	// Test refresh from node
  	// 
  	LdvModelNode diabNode = testPatPatho.findItem("PDI401") ;
  	assertNotNull(diabNode) ;
  	Iterator<LdvModelNode> iterFromDiab = testPatPatho.getIteratorAfterNode(diabNode) ;
  	assertNotNull(iterFromDiab) ;
  	
  	for ( ; iterFromDiab.hasNext() ; )
  		iterFromDiab.next().setLine(0) ;
  	
  	testPatPatho.refreshLines(diabNode) ;
  	
  	iLine = LdvModelNodeArray.ORIGINE_PATH_PATHO ;
  	for (Iterator<LdvModelNode> iter = testPatPatho.iterator() ; iter.hasNext() ; )
  		assertEquals(iter.next().getLine(), iLine++) ;
  }
  
  public void testDeleteNodes() 
  {
  	LdvModelNodeArray testPatPatho = new LdvModelNodeArray() ;
  	initializePatPatho(testPatPatho) ;
  	
  	LdvModelNode foundNode = testPatPatho.findItem("0PRO11") ;
  	assertNotNull(foundNode) ;
   	assertTrue("0PRO11".equals(foundNode.getLexicon())) ;
   	
   	testPatPatho.deleteNode(foundNode) ;
   	
   	LdvModelNodeArray comparePatPatho = new LdvModelNodeArray() ;
   	comparePatPatho.addNode("ZPOMR1", null, 0) ;
   	comparePatPatho.addNode("0OBJE1", null, 1) ;
   	comparePatPatho.addNode("N00001", null, 1) ;
   	comparePatPatho.addNode("IACAL1", null, 2) ;
   	comparePatPatho.addNode("KOUVR1", null, 3) ;
   	BBMessage dateMsg = new BBMessage() ;
   	dateMsg.setComplement("19981115131155") ;
   	dateMsg.setUnit("2DA021") ;
   	comparePatPatho.addNode("£T0;19", dateMsg, 4) ;
   	comparePatPatho.addNode("0MEDF1", null, 3) ;
   	comparePatPatho.addNode("2MG001", null, 4) ;
   	comparePatPatho.addNode("KPHAT1", null, 3) ;
   	comparePatPatho.addNode("KOUVR1", null, 4) ;
   	dateMsg.setComplement("20111115131155") ;
   	comparePatPatho.addNode("£T0;19", dateMsg, 5) ;
   	comparePatPatho.addNode("KCYTR1", null, 4) ;
   	comparePatPatho.addNode("KRYTH1", null, 5) ;
   	
   	assertTrue(comparePatPatho.hasSameContent(testPatPatho)) ;
  }
  
  public void testGetNodeForExactPath() 
  {
  	LdvModelNodeArray testPatPatho = new LdvModelNodeArray() ;
  	initializePatPatho(testPatPatho) ;
  	
  	LdvModelNode foundNode = testPatPatho.getNodeForExactPath("ZPOMR/0PRO1/PDI40/VQUAN/VIGRA", "/") ;
  	assertNotNull(foundNode) ;
   	assertTrue("VIGRA1".equals(foundNode.getLexicon())) ;
   	
   	LdvModelNode notFoundNode = testPatPatho.getNodeForExactPath("ZPOMR/0PRO1/PDI40/VIGRA", "/") ;
  	assertNull(notFoundNode) ;
  }
  
  public void initializePatPatho(LdvModelNodeArray patPatho)
  {
  	if (null == patPatho)
  		return ;
  	
  	patPatho.addNode("ZPOMR1", null, 0) ;
  	
  	// Concerns
  	//
  	patPatho.addNode("0PRO11", null, 1) ;
  	
  	// First concern
  	//
  	patPatho.addNode("PHTA01", null, 2) ;
  	patPatho.addNode("KOUVR1", null, 3) ;
  	BBMessage dateMsg = new BBMessage() ;
  	dateMsg.setComplement("19980615130952") ;
  	dateMsg.setUnit("2DA021") ;
  	patPatho.addNode("£T0;19", dateMsg, 4) ;
  	patPatho.addNode("VQUAN1", null, 3) ;
  	patPatho.addNode("KDAT01", null, 4) ;
  	patPatho.addNode("£T0;19", dateMsg, 5) ;
  	patPatho.addNode("VIGRA1", null, 4) ;
  	BBMessage severity = new BBMessage() ;
  	severity.setComplement("010") ;
  	severity.setUnit("200001") ;
  	patPatho.addNode("£N2;04", severity, 5) ;
  	
  	// Second concern
   	//
   	patPatho.addNode("PDI401", null, 2) ;
   	patPatho.addNode("KOUVR1", null, 3) ;
   	dateMsg.setComplement("20020403131356") ;
   	patPatho.addNode("£T0;19", dateMsg, 4) ;
   	patPatho.addNode("VQUAN1", null, 3) ;
   	patPatho.addNode("KDAT01", null, 4) ;
   	patPatho.addNode("£T0;19", dateMsg, 5) ;
   	patPatho.addNode("VIGRA1", null, 4) ;
   	severity.setComplement("052") ;
   	patPatho.addNode("£N2;04", severity, 5) ;
   	
   	// Third concern
   	//
   	patPatho.addNode("PINEV1", null, 2) ;
   	patPatho.addNode("KOUVR1", null, 3) ;
   	dateMsg.setComplement("20100305132754") ;
   	patPatho.addNode("£T0;19", dateMsg, 4) ;
   	patPatho.addNode("KFERM1", null, 3) ;
   	dateMsg.setComplement("20100315132754") ;
   	patPatho.addNode("£T0;19", dateMsg, 4) ;
   	patPatho.addNode("VQUAN1", null, 3) ;
   	patPatho.addNode("KDAT01", null, 4) ;
   	dateMsg.setComplement("20100305132754") ;
   	patPatho.addNode("£T0;19", dateMsg, 5) ;
   	patPatho.addNode("VIGRA1", null, 4) ;
   	severity.setComplement("090") ;
   	patPatho.addNode("£N2;04", severity, 5) ;
   	
   	// Targets
   	//
   	patPatho.addNode("0OBJE1", null, 1) ;
   	
   	// Treatments
   	//
   	patPatho.addNode("N00001", null, 1) ;
   	
   	// First treatment
   	//
   	patPatho.addNode("IACAL1", null, 2) ;
   	patPatho.addNode("KOUVR1", null, 3) ;
   	dateMsg.setComplement("19981115131155") ;
   	patPatho.addNode("£T0;19", dateMsg, 4) ;
   	patPatho.addNode("0MEDF1", null, 3) ;
   	patPatho.addNode("2MG001", null, 4) ;
   	patPatho.addNode("KPHAT1", null, 3) ;
   	patPatho.addNode("KOUVR1", null, 4) ;
   	dateMsg.setComplement("20111115131155") ;
   	patPatho.addNode("£T0;19", dateMsg, 5) ;
   	patPatho.addNode("KCYTR1", null, 4) ;
   	patPatho.addNode("KRYTH1", null, 5) ;
  }
}
