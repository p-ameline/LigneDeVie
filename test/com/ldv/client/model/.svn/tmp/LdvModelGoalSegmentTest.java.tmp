package com.ldv.client.model;

import com.ldv.shared.model.LdvTime;

import junit.framework.TestCase;

public class LdvModelGoalSegmentTest extends TestCase
{

  public void testInit() 
  {
  	LdvModelGoalSegment ldvGoalSegment = new LdvModelGoalSegment() ; 
  	assertNotNull(ldvGoalSegment) ;
  	
  	LdvTime referenceTime = new LdvTime(0) ;
  	assertNotNull(referenceTime) ;
  	
  	// Inherited from LdvModelGenericBox
  	//
  	assertEquals("", ldvGoalSegment.getTitle()) ;
  	assertTrue(referenceTime.equals(ldvGoalSegment.getBeginDate())) ;
  	assertTrue(referenceTime.equals(ldvGoalSegment.getEndDate())) ;
  	
  	// Own data
  	//
  	assertTrue(ldvGoalSegment.getJalonType()      == LdvModelGoalSegment.jalonType.jalonNotype) ;
  	assertTrue(ldvGoalSegment.getJalonEventType() == LdvModelGoalSegment.jalonEventType.jalonNoEvent) ;
  	assertTrue(ldvGoalSegment.getTimeLevel()      == LdvModelGoalSegment.jalonLevel.NoColor) ;
  	assertTrue(ldvGoalSegment.getValueLevel()     == LdvModelGoalSegment.jalonLevel.NoColor) ;
  	assertTrue(ldvGoalSegment.getLevel()          == LdvModelGoalSegment.jalonLevel.NoColor) ;
  	
  	assertEquals("", ldvGoalSegment.getCode()) ;
  	assertTrue(ldvGoalSegment.getdValue() == 0) ;
  	assertEquals("", ldvGoalSegment.getsValue()) ;
  	assertEquals("", ldvGoalSegment.getUnit()) ;
  }
  
  public void testBeforeAndAfterColors() 
  {
  	LdvModelGoalSegment ldvGoalSegment = new LdvModelGoalSegment() ; 
  	assertNotNull(ldvGoalSegment) ;
  	
  	assertTrue(ldvGoalSegment.getPreviousColor(LdvModelGoalSegment.jalonLevel.NoColor) == LdvModelGoalSegment.jalonLevel.NoColor) ;
  	assertTrue(ldvGoalSegment.getNextColor(LdvModelGoalSegment.jalonLevel.NoColor) == LdvModelGoalSegment.jalonLevel.NoColor) ;
  	
  	assertTrue(ldvGoalSegment.getPreviousColor(LdvModelGoalSegment.jalonLevel.RedBefore) == LdvModelGoalSegment.jalonLevel.NoColor) ;
  	assertTrue(ldvGoalSegment.getNextColor(LdvModelGoalSegment.jalonLevel.RedBefore) == LdvModelGoalSegment.jalonLevel.YellowBefore) ;
  	
  	assertTrue(ldvGoalSegment.getPreviousColor(LdvModelGoalSegment.jalonLevel.YellowBefore) == LdvModelGoalSegment.jalonLevel.RedBefore) ;
  	assertTrue(ldvGoalSegment.getNextColor(LdvModelGoalSegment.jalonLevel.YellowBefore) == LdvModelGoalSegment.jalonLevel.GreenBefore) ;
  	
  	assertTrue(ldvGoalSegment.getPreviousColor(LdvModelGoalSegment.jalonLevel.GreenBefore) == LdvModelGoalSegment.jalonLevel.YellowBefore) ;
  	assertTrue(ldvGoalSegment.getNextColor(LdvModelGoalSegment.jalonLevel.GreenBefore) == LdvModelGoalSegment.jalonLevel.Blue) ;
  	
  	assertTrue(ldvGoalSegment.getPreviousColor(LdvModelGoalSegment.jalonLevel.Blue) == LdvModelGoalSegment.jalonLevel.GreenBefore) ;
  	assertTrue(ldvGoalSegment.getNextColor(LdvModelGoalSegment.jalonLevel.Blue) == LdvModelGoalSegment.jalonLevel.GreenAfter) ;
  	
  	assertTrue(ldvGoalSegment.getPreviousColor(LdvModelGoalSegment.jalonLevel.GreenAfter) == LdvModelGoalSegment.jalonLevel.Blue) ;
  	assertTrue(ldvGoalSegment.getNextColor(LdvModelGoalSegment.jalonLevel.GreenAfter) == LdvModelGoalSegment.jalonLevel.YellowAfter) ;
  	
  	assertTrue(ldvGoalSegment.getPreviousColor(LdvModelGoalSegment.jalonLevel.YellowAfter) == LdvModelGoalSegment.jalonLevel.GreenAfter) ;
  	assertTrue(ldvGoalSegment.getNextColor(LdvModelGoalSegment.jalonLevel.YellowAfter) == LdvModelGoalSegment.jalonLevel.RedAfter) ;
  	
  	assertTrue(ldvGoalSegment.getPreviousColor(LdvModelGoalSegment.jalonLevel.RedAfter) == LdvModelGoalSegment.jalonLevel.YellowAfter) ;
  	assertTrue(ldvGoalSegment.getNextColor(LdvModelGoalSegment.jalonLevel.RedAfter) == LdvModelGoalSegment.jalonLevel.NoColor) ;
  }
}
