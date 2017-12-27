package com.ldv.client.model;

import com.ldv.shared.model.LdvTime;

import junit.framework.TestCase;

public class LdvModelGoalTest extends TestCase
{

  public void testConstructor() 
  {
  	LdvModelGoal ldvGoal = new LdvModelGoal() ; 
  	assertNotNull(ldvGoal) ;
  	
  	LdvTime referenceTime = new LdvTime(0) ;
  	assertNotNull(referenceTime) ;
  	
  	// Inherited from LdvModelGenericBox
  	//
  	assertEquals("", ldvGoal.getTitle()) ;
  	assertTrue(referenceTime.equals(ldvGoal.getBeginDate())) ;
  	assertTrue(referenceTime.equals(ldvGoal.getEndDate())) ;
  	
  	// Own data
  	//
  	assertEquals("", ldvGoal.getGoalID()) ;
  	assertEquals("", ldvGoal.getReferentialID()) ;
  	
  	assertEquals("", ldvGoal.getLexique()) ;
  	assertEquals("", ldvGoal.getConcernId()) ;
  	assertEquals("", ldvGoal.getReference()) ;
  	assertEquals("", ldvGoal.getCertitude()) ;
  	assertEquals("", ldvGoal.getComplementText()) ;
  	
  	assertTrue(ldvGoal.getRythme() == LdvModelGoal.RYTHMETYPE.ponctual) ;
  	
  	assertNotNull(ldvGoal.getCyclicCalendar()) ;
  	assertNotNull(ldvGoal.getStaticCalendar()) ;
  	
  	assertFalse(ldvGoal.hasValue()) ;
  	assertNotNull(ldvGoal.getValues()) ;
  	
  	assertEquals("", ldvGoal.getOpenEventNode()) ;
  	assertEquals("", ldvGoal.getCloseEventNode()) ;
  	
  	assertNotNull(ldvGoal.getOpeningEvent()) ;
  	assertNotNull(ldvGoal.getClosingEvent()) ;
  	
  	assertFalse(ldvGoal.isSelected()) ;
  }
}
