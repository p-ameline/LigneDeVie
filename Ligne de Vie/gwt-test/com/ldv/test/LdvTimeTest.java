package com.ldv.test;

import java.util.Date;

import com.google.gwt.junit.client.GWTTestCase;
import com.ldv.shared.model.LdvTime;

public class LdvTimeTest extends GWTTestCase
{
  @Override
  public String getModuleName() 
  {
  	return "com.ldv.Ligne_de_Vie" ;
  }
 
	@SuppressWarnings("deprecation")
  public void testInit() 
  {
  	LdvTime ldvTime1 = new LdvTime(0) ; 
  	assertNotNull(ldvTime1) ;
  	
  	assertEquals("00000000000000000", ldvTime1.getFullDateTime()) ;
  	assertTrue(ldvTime1.isEmpty()) ;
  	assertFalse(ldvTime1.isNoLimit()) ;
  	
  	ldvTime1.setNoLimit() ;
  	assertEquals("99990000000000000", ldvTime1.getFullDateTime()) ;
  	assertFalse(ldvTime1.isEmpty()) ;
  	assertTrue(ldvTime1.isNoLimit()) ;
  	assertEquals(-1, ldvTime1.getFullYear()) ;
  	assertEquals(-1, ldvTime1.getMonth()) ;     
  	assertEquals(-1, ldvTime1.getDate()) ;      
  	assertEquals(-1, ldvTime1.getHours()) ;     
  	assertEquals(-1, ldvTime1.getMinutes()) ;   
  	assertEquals(-1, ldvTime1.getSeconds()) ;   
  	assertEquals(-1, ldvTime1.getMilliseconds()) ;
  	
  	ldvTime1.init() ;
  	assertEquals("00000000000000000", ldvTime1.getFullDateTime()) ;
  	assertTrue(ldvTime1.isEmpty()) ;
  	assertFalse(ldvTime1.isNoLimit()) ;
  	
  	Date testDate = new Date(110, 1, 18, 16, 6, 45) ;
  	ldvTime1.initFromJavaDate(testDate) ;
  	assertFalse(ldvTime1.isNoLimit()) ;
  	assertFalse(ldvTime1.isEmpty()) ;
  	assertEquals("20100218160645000", ldvTime1.getFullDateTime()) ;
  	assertEquals("20100218160645",    ldvTime1.getDateTime()) ;
  	assertEquals("20100218",          ldvTime1.getSimpleDate()) ;
  	assertEquals("160645000",         ldvTime1.getFullHour()) ;
  	assertEquals("160645",            ldvTime1.getHour()) ;
  	assertEquals(2010, ldvTime1.getFullYear()) ;
  	assertEquals(2,    ldvTime1.getMonth()) ;     
  	assertEquals(18,   ldvTime1.getDate()) ;      
  	assertEquals(16,   ldvTime1.getHours()) ;     
  	assertEquals(6,    ldvTime1.getMinutes()) ;   
  	assertEquals(45,   ldvTime1.getSeconds()) ;   
  	assertEquals(0,    ldvTime1.getMilliseconds()) ;
  	
  	assertTrue(ldvTime1.initFromDateTime("20100218081252")) ;
  	assertEquals("20100218081252000", ldvTime1.getFullDateTime()) ;
  	assertFalse(ldvTime1.initFromDateTime("20100218")) ;
  	assertFalse(ldvTime1.initFromDateTime("20100218hh1252")) ;
  	assertEquals("20100218081252000", ldvTime1.getFullDateTime()) ;
  	assertTrue(ldvTime1.initFromDate("20100218")) ;
  	assertEquals("20100218000000000", ldvTime1.getFullDateTime()) ;
  	
  	assertTrue(ldvTime1.initFromDateTime("20110318114821")) ;
  	Date javaDate = ldvTime1.toJavaDate() ;
  	assertEquals(111, javaDate.getYear()) ;
  	assertEquals(2, javaDate.getMonth()) ;
  	assertEquals(18, javaDate.getDate()) ;
  	assertEquals(11, javaDate.getHours()) ;
  	assertEquals(48, javaDate.getMinutes()) ;
  	assertEquals(21, javaDate.getSeconds()) ;
  	
  	LdvTime ldvTime2 = new LdvTime(0) ; 
  	assertNotNull(ldvTime2) ;
  	ldvTime2.initFromJavaDate(javaDate) ;
  	
  	assertTrue(ldvTime2.equals(ldvTime1)) ;
  	
  	LdvTime ldvTime3 = new LdvTime(0, 1998, 12, 4, 14, 12, 55) ; 
  	assertNotNull(ldvTime3) ;
  	assertEquals("19981204141255000", ldvTime3.getFullDateTime()) ;
  	
  	// TimeZone oriented tests
  	//
  	LdvTime ldvTime4 = new LdvTime(8) ; 
  	assertNotNull(ldvTime4) ;
  	assertEquals(8, ldvTime4.getHourTimeZone()) ;
  	assertEquals(8*3600, ldvTime4.getTimeZone()) ;
  	assertTrue(ldvTime4.initFromDateTime("20110318114821")) ;
  	
  	LdvTime ldvTime5 = new LdvTime(0) ;
  	assertNotNull(ldvTime5) ;
  	ldvTime5.initFromLdvTime(ldvTime4) ;
  	assertEquals(8, ldvTime5.getHourTimeZone()) ;
  }
	
  public void testJulianFunctions() 
  {
  	LdvTime ldvTime1 = new LdvTime(0) ;
  	assertNotNull(ldvTime1) ;
  	
  	LdvTime ldvTime2 = new LdvTime(0) ;
  	assertNotNull(ldvTime2) ;
  	
  	assertTrue(ldvTime1.initFromDate("19680523")) ;
  	assertTrue(ldvTime1.getJulianDay() == 2440000) ;
  	
  	assertTrue(ldvTime2.initFromJulian(2440000)) ;
  	assertTrue(ldvTime2.equals(ldvTime1)) ;
  	
  	assertTrue(ldvTime1.initFromDate("19700101")) ;
  	assertTrue(ldvTime1.getJulianDay() == 2440588) ;
  	
  	assertTrue(ldvTime2.initFromJulian(2440588)) ;
  	assertTrue(ldvTime2.equals(ldvTime1)) ;
  }
  
  public void testTimeFunctions() 
  {
  	LdvTime ldvTime1 = new LdvTime(0) ;
  	assertNotNull(ldvTime1) ;
  	
  	assertTrue(ldvTime1.checkProperDateString("20100218")) ;
  	assertFalse(ldvTime1.checkProperDateString("yyyymmdd")) ;
  }
  
  @SuppressWarnings("deprecation")
  public void testTakeTime() 
  {
  	LdvTime ldvTime1 = new LdvTime(0) ;
  	assertNotNull(ldvTime1) ;
  	
  	ldvTime1.takeTime() ;
  	Date tNow = new Date() ;
  	assertEquals(tNow.getYear() + 1900, ldvTime1.getFullYear()) ;
  	assertEquals(tNow.getMonth() + 1,   ldvTime1.getMonth()) ;     
  	assertEquals(tNow.getDate(),    ldvTime1.getDate()) ;      
  	assertEquals(tNow.getHours(),   ldvTime1.getHours()) ;     
  	assertEquals(tNow.getMinutes(), ldvTime1.getMinutes()) ;
  	assertEquals(tNow.getSeconds(), ldvTime1.getSeconds()) ;
  }
  
  public void testTimePutFunctions() 
  {
  	LdvTime ldvTime1 = new LdvTime(0) ;
  	assertNotNull(ldvTime1) ;
  	
  	String sInsertTest = new String("") ;
  	sInsertTest = ldvTime1.insertIntAtPos(10, sInsertTest, 0, 2) ;
  	assertEquals(sInsertTest, "") ;
  	sInsertTest = "toto" ;
  	sInsertTest = ldvTime1.insertIntAtPos(10, sInsertTest, 0, 5) ;
  	assertEquals(sInsertTest, "toto") ;
  	sInsertTest = "tata" ;
  	sInsertTest = ldvTime1.insertIntAtPos(10, sInsertTest, 2, 0) ;
  	assertEquals(sInsertTest, "tata") ;
  	sInsertTest = "tatito" ;
  	sInsertTest = ldvTime1.insertIntAtPos(10, sInsertTest, 2, 2) ;
  	assertEquals(sInsertTest, "ta10to") ;
  	sInsertTest = "titi" ;
  	sInsertTest = ldvTime1.insertIntAtPos(10, sInsertTest, 2, 3) ;
  	assertEquals(sInsertTest, "titi") ;
  	sInsertTest = ldvTime1.insertIntAtPos(10, sInsertTest, 0, 2) ;
  	assertEquals(sInsertTest, "10ti") ;
  	sInsertTest = ldvTime1.insertIntAtPos(10, sInsertTest, 2, 2) ;
  	assertEquals(sInsertTest, "1010") ;
  	sInsertTest = ldvTime1.insertIntAtPos(10, sInsertTest, 0, 4) ;
  	assertEquals(sInsertTest, "0010") ;
  	
  	ldvTime1.init() ;
  	
  	ldvTime1.putFullYear(2010) ;
  	ldvTime1.putMonth(2) ;
  	ldvTime1.putDate(28) ;
  	ldvTime1.putHours(17) ;
  	ldvTime1.putMinutes(28) ;
  	ldvTime1.putSeconds(30) ;
  	ldvTime1.putMilliseconds(2) ;
  	
  	assertEquals("20100228172830002", ldvTime1.getFullDateTime()) ;
  }
  
  public void testNormalizeFunctions() 
  {
  	LdvTime ldvTime1 = new LdvTime(0) ;
  	assertNotNull(ldvTime1) ;
 
  	assertTrue(ldvTime1.isLeapYear(2004)) ;
  	assertFalse(ldvTime1.isLeapYear(1900)) ;
  	assertTrue(ldvTime1.isLeapYear(2000)) ;
  	assertTrue(ldvTime1.isLeapYear(1996)) ;
  	assertTrue(ldvTime1.isLeapYear(2008)) ;
  	assertFalse(ldvTime1.isLeapYear(2009)) ;
  	assertFalse(ldvTime1.isLeapYear(2010)) ;
  	
  	ldvTime1.initFromDateTime("20100229081252") ;
  	ldvTime1.normalize() ;
  	assertEquals("20100228081252000", ldvTime1.getFullDateTime()) ;
  	
  	ldvTime1.initFromDateTime("20100134081252") ;
  	ldvTime1.normalize() ;
  	assertEquals("20100131081252000", ldvTime1.getFullDateTime()) ;
  	
  	ldvTime1.initFromDateTime("20100434081252") ;
  	ldvTime1.normalize() ;
  	assertEquals("20100430081252000", ldvTime1.getFullDateTime()) ;
  	
  	ldvTime1.init() ;
  	ldvTime1.normalize() ;
  	assertEquals("19000101000000000", ldvTime1.getFullDateTime()) ;
  }
  
  public void testAddFunctions() 
  {
  	LdvTime ldvTime1 = new LdvTime(0) ;
  	assertNotNull(ldvTime1) ;
 
  	ldvTime1.initFromDateTime("19101122081252") ;
  	ldvTime1.addDays(5, true) ;
  	assertEquals("19101127081252000", ldvTime1.getFullDateTime()) ;
  	ldvTime1.addHours(6, true) ;
  	assertEquals("19101127141252000", ldvTime1.getFullDateTime()) ;
  	ldvTime1.addMinutes(20, true) ;
  	assertEquals("19101127143252000", ldvTime1.getFullDateTime()) ;
  	ldvTime1.addMinutes(210, true) ;
  	assertEquals("19101127180252000", ldvTime1.getFullDateTime()) ;
  	ldvTime1.addSeconds(28, true) ;
  	assertEquals("19101127180320000", ldvTime1.getFullDateTime()) ;
  	
  	ldvTime1.initFromDateTime("20050331081252") ;
  	ldvTime1.addMonths(37, true) ;
  	assertEquals("20080430081252000", ldvTime1.getFullDateTime()) ;
  	
  	ldvTime1.initFromDateTime("20040229081252") ;
  	ldvTime1.addYears(50, true) ;
  	assertEquals("20540228081252000", ldvTime1.getFullDateTime()) ;
  	
  	ldvTime1.initFromDateTime("20050331081252") ;
  	ldvTime1.addMonths(13, false) ;
  	assertEquals("20060431081252000", ldvTime1.getFullDateTime()) ;  	
  	ldvTime1.addMonths(-13, false) ;
  	assertEquals("20050331081252000", ldvTime1.getFullDateTime()) ;
  	
  	ldvTime1.initFromDateTime("20051231235959200") ;
  	ldvTime1.addMilliseconds(1000, false) ;
  	
  	// Doesn't work because milliseconds are not par of java Date object
  	// assertEquals("20060101000000200", ldvTime1.getFullDateTime()) ;
  	assertEquals("20060101000000", ldvTime1.getDateTime()) ;
  }

  public void testTimeZoneFunctions() 
  {
  	LdvTime ldvTime1 = new LdvTime(1) ;
  	assertNotNull(ldvTime1) ;
	
  	LdvTime ldvTime2 = new LdvTime(-1) ;
  	assertNotNull(ldvTime2) ;
	
  	assertTrue(ldvTime1.initFromDate("19680523183020252")) ;
  	assertTrue(ldvTime2.initFromDate("19680523203020252")) ;
	
  	assertEquals("19680523193020252", ldvTime1.getFullUTCDateTime()) ;
  	assertTrue(ldvTime2.equals(ldvTime1)) ;
  	
  	LdvTime ldvTime3 = new LdvTime(0) ;
  	assertNotNull(ldvTime3) ;
  	ldvTime3.initFromLdvTime(ldvTime2) ;
  	assertEquals(-1, ldvTime3.getHourTimeZone()) ;
  }
  
  public void testDiffFunctions() 
  {
  	LdvTime ldvTime1 = new LdvTime(1) ;
  	assertNotNull(ldvTime1) ;
	
  	LdvTime ldvTime2 = new LdvTime(-1) ;
  	assertNotNull(ldvTime2) ;
	
  	assertTrue(ldvTime1.initFromDate("19680523183020252")) ;
  	assertTrue(ldvTime2.initFromDate("19680523183020252")) ;
	
  	assertTrue(ldvTime2.isBefore(ldvTime1)) ;
  	assertFalse(ldvTime2.isAfter(ldvTime1)) ;
  	assertFalse(ldvTime1.isBefore(ldvTime2)) ;
  	assertTrue(ldvTime1.isAfter(ldvTime2)) ;
  	
  	LdvTime ldvTime3 = new LdvTime(2) ;
  	assertNotNull(ldvTime3) ;
  	
  	assertTrue(ldvTime3.isBefore(ldvTime1)) ;
  	ldvTime3.setNoLimit() ;
  	assertTrue(ldvTime1.isBefore(ldvTime3)) ;
  }
}
