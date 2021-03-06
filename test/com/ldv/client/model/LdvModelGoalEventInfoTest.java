package com.ldv.client.model;

import com.ldv.shared.model.LdvTime;

import junit.framework.TestCase;

public class LdvModelGoalEventInfoTest extends TestCase
{

  public void testInit() 
  {
  	LdvModelGoalEventInfo ldvGoalInfo = new LdvModelGoalEventInfo() ; 
  	assertNotNull(ldvGoalInfo) ;
  	
  	LdvTime referenceTime = new LdvTime(0) ;
  	assertNotNull(referenceTime) ;
  	
  	assertTrue(referenceTime.equals(ldvGoalInfo.getDate())) ;
  	assertEquals("", ldvGoalInfo.getsValue()) ;
  	assertEquals("", ldvGoalInfo.getUnit()) ;
  	assertTrue(0 == ldvGoalInfo.getdValue()) ;
  	assertFalse(ldvGoalInfo.isValue()) ;
  }
}
