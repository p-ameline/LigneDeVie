package com.ldv.shared.model ;

import junit.framework.TestCase ;

public class LdvTimeTest extends TestCase
{

  public void testConstructor() 
  {
  	// Test default constructor
  	//
  	LdvTime ldvTime = new LdvTime(0) ; 
  	assertNotNull(ldvTime) ;
  	assertEquals(0, ldvTime.getHourTimeZone()) ;
  	assertTrue(ldvTime.isEmpty()) ;
  	assertFalse(ldvTime.isNoLimit()) ;
  } ;
  
  public void testDayOfWeek() 
  {
  	LdvTime ldvTime = new LdvTime(0) ; 
  	assertNotNull(ldvTime) ;
  	
  	ldvTime.initFromLocalDate("20170101") ;
  	assertEquals(7, ldvTime.getLocalDayOfWeek()) ;
  	
  	ldvTime.initFromLocalDate("20170721") ;
  	assertEquals(5, ldvTime.getLocalDayOfWeek()) ;
  	
  	ldvTime.initFromLocalDate("20180101") ;
  	assertEquals(1, ldvTime.getLocalDayOfWeek()) ;
  }
  
  public void testNumberOfWeeks() 
  {
  	// From https://en.wikipedia.org/wiki/ISO_week_date
  	// The following 71 years in a 400-year cycle have 53 weeks
  	//
  	int[] aiAre53 = { 4, 9, 15, 20, 26, 32, 37, 43, 48, 54, 60, 65, 71, 76, 82, 88, 93, 99,
  	                  105, 111, 116, 122, 128, 133, 139, 144, 150, 156, 161, 167, 172, 178, 184, 189, 195,
  	                  201, 207, 212, 218, 224, 229, 235, 240, 246, 252, 257, 263, 268, 274, 280, 285, 291, 296,
  	                  303, 308, 314, 320, 325, 331, 336, 342, 348, 353, 359, 364, 370, 376, 381, 387, 392, 398 };
  	
  	LdvTime ldvTime = new LdvTime(0) ;
  	ldvTime.initFromLocalDate("20000101") ;
  	
  	int iCurrent53Slot = 0 ;
  	
  	for (int i = 0 ; i < 400 ; i++)
  	{
  		ldvTime.putLocalFullYear(2000 + i) ;
  		
  		if (i == aiAre53[iCurrent53Slot])
  		{
  			if (iCurrent53Slot < aiAre53.length - 1)
  				iCurrent53Slot++ ;
  			
  			assertTrue(ldvTime.is53WeeksYear()) ;
  		}
  		else
  			assertFalse(ldvTime.is53WeeksYear()) ;
  	}
  }
  
  public void testWeekOfYear() 
  {
  	LdvTime ldvTime = new LdvTime(0) ;
  	ldvTime.initFromLocalDate("20170101") ;
  	assertEquals(52, ldvTime.getWeekOfYear()) ;
  	
  	ldvTime.initFromLocalDate("20170621") ;
  	assertEquals(25, ldvTime.getWeekOfYear()) ;
  	
  	ldvTime.initFromLocalDate("20171228") ;
  	assertEquals(52, ldvTime.getWeekOfYear()) ;
  	
  	ldvTime.initFromLocalDate("20180101") ;
  	assertEquals(1, ldvTime.getWeekOfYear()) ;
  }
  
  public void testAddSeconds()
  {
  	LdvTime ldvTime = new LdvTime(0) ;
  	ldvTime.initFromLocalDateTime("20170101235958") ;
  	ldvTime.addSeconds(2, true) ;
  	assertEquals("20170102000000", ldvTime.getLocalDateTime()) ;
  	
  	ldvTime.addSeconds(-2, true) ;
  	assertEquals("20170101235958", ldvTime.getLocalDateTime()) ;
  }
  
  public void testAddHours()
  {
  	LdvTime ldvTime = new LdvTime(0) ;
  	ldvTime.initFromLocalDateTime("20170101230000") ;
  	ldvTime.addHours(1, true) ;
  	assertEquals("20170102000000", ldvTime.getLocalDateTime()) ;
  	
  	ldvTime.addHours(-1, true) ;
  	assertEquals("20170101230000", ldvTime.getLocalDateTime()) ;
  	
  	ldvTime.addHours(-1, true) ;
  	assertEquals("20170101220000", ldvTime.getLocalDateTime()) ;
  }
  
  public void testIsAfter()
  {
  	LdvTime ldvTimeEarly = new LdvTime(0) ;
  	ldvTimeEarly.initFromLocalDateTime("20170101230000") ;
  	LdvTime ldvTimeLate = new LdvTime(0) ;
  	ldvTimeLate.initFromLocalDateTime("20170102000000") ;
  	
  	assertTrue(ldvTimeLate.isAfter(ldvTimeEarly)) ;
  	
  	LdvTime ldvTimeEarly2 = new LdvTime(-3600) ;
  	ldvTimeEarly2.initFromLocalDateTime("20170101230000") ;
  	LdvTime ldvTimeLate2 = new LdvTime(-3600) ;
  	ldvTimeLate2.initFromLocalDateTime("20170102000000") ;
  	
  	assertTrue(ldvTimeLate2.isAfter(ldvTimeEarly2)) ;
  	
  	LdvTime ldvTimeEarly3 = new LdvTime(-3600) ;
  	ldvTimeEarly3.initFromLocalDateTime("20170327210405") ;
  	LdvTime ldvTimeLate3 = new LdvTime(-3600) ;
  	ldvTimeLate3.initFromLocalDateTime("20170327230000") ;
  	
  	assertTrue(ldvTimeLate3.isAfter(ldvTimeEarly3)) ;
  }
  
  public void testIsEqual() // "18:30Z", "22:30+04", "1130−0700", and "15:00−03:30"
  {
  	LdvTime ldvTime1 = new LdvTime(0) ;
  	ldvTime1.initFromLocalDateTime("20170101183000") ;
  	
  	LdvTime ldvTime2 = new LdvTime(4) ;
  	ldvTime2.initFromLocalDateTime("20170101223000") ;
  	
  	LdvTime ldvTime3 = new LdvTime(-7) ;
  	ldvTime3.initFromLocalDateTime("20170101113000") ;
  	
  	LdvTime ldvTime4 = new LdvTime(0) ;
  	ldvTime4.setTimeZone(-3 * LdvTime.SECONDS_IN_HOUR - 30 * LdvTime.SECONDS_IN_MIN) ;
  	ldvTime4.initFromLocalDateTime("20170101150000") ;
  	
  	assertTrue(ldvTime1.equals(ldvTime2)) ;
  	assertTrue(ldvTime1.equals(ldvTime3)) ;
  	assertTrue(ldvTime1.equals(ldvTime4)) ;
  }
  
  public void testGetUTCFullDateTime()
  {
  	LdvTime ldvTime1 = new LdvTime(-1) ;
  	ldvTime1.initFromLocalDateTime("20170101230000") ;
  	assertTrue("20170102000000".equals(ldvTime1.getUTCDateTime())) ;
  }
}
