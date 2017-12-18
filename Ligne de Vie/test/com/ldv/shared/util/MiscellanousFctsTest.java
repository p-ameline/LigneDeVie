package com.ldv.shared.util ;

import junit.framework.TestCase ;

import com.ldv.shared.util.MiscellanousFcts;
import com.ldv.shared.util.MiscellanousFcts.STRIP_DIRECTION;

public class MiscellanousFctsTest extends TestCase
{
  public void testIsValidMailAddress() 
  {
  	// Valid
  	//
  	assertTrue(MiscellanousFcts.isValidMailAddress("mkyong@yahoo.com")) ;
  	assertTrue(MiscellanousFcts.isValidMailAddress("mkyong-100@yahoo.com")) ;
  	assertTrue(MiscellanousFcts.isValidMailAddress("mkyong.100@yahoo.com")) ;
  	assertTrue(MiscellanousFcts.isValidMailAddress("mkyong111@mkyong.com")) ;
  	assertTrue(MiscellanousFcts.isValidMailAddress("mkyong-100@mkyong.net")) ;
  	assertTrue(MiscellanousFcts.isValidMailAddress("mkyong.100@mkyong.com.au")) ;
  	assertTrue(MiscellanousFcts.isValidMailAddress("mkyong@1.com")) ;
  	assertTrue(MiscellanousFcts.isValidMailAddress("mkyong@gmail.com.com")) ;
  	
  	// Bad
  	//
  	assertFalse(MiscellanousFcts.isValidMailAddress("mkyong")) ;                  // no @
  	assertFalse(MiscellanousFcts.isValidMailAddress("mkyong@.com.my")) ;          // tld can not start with dot '.'
  	assertFalse(MiscellanousFcts.isValidMailAddress("mkyong123@gmail.a")) ;       // ".a" is not a valid tld, last tld must contains at least 2 characters
  	assertFalse(MiscellanousFcts.isValidMailAddress("mkyong123@.com")) ;          // tld can not start with dot '.'
  	assertFalse(MiscellanousFcts.isValidMailAddress("mkyong123@.com.com")) ;      // tld can not start with dot '.'
  	assertFalse(MiscellanousFcts.isValidMailAddress(".mkyong@mkyong.com")) ;      // email’s first character can not start with dot '.'
  	assertFalse(MiscellanousFcts.isValidMailAddress("mkyong()*@gmail.com")) ;     // email’s is only allow character, digit, underscore and dash
  	assertFalse(MiscellanousFcts.isValidMailAddress("mkyong@%*.com")) ;           // email’s tld is only allow character and digit
  	assertFalse(MiscellanousFcts.isValidMailAddress("mkyong..2002@gmail.com")) ;  // double dots '.' are not allowed
  	assertFalse(MiscellanousFcts.isValidMailAddress("mkyong.@gmail.com")) ;       // email’s last character can not end with dot '.'
  	assertFalse(MiscellanousFcts.isValidMailAddress("mkyong@mkyong@gmail.com")) ; // double '@' is not allowed
  	assertFalse(MiscellanousFcts.isValidMailAddress("mkyong@gmail.com.1a")) ;     // email’s tld which has 2 characters can not contains digit 
  }
  
  public void testFindLastAndFirstNotOf() 
  {
  	// find_first_not_of
  	//
  	assertEquals(4, MiscellanousFcts.find_first_not_of("aaaabcd", 'a')) ;
  	assertEquals(0, MiscellanousFcts.find_first_not_of("bcd", 'a')) ;
  	assertEquals(4, MiscellanousFcts.find_first_not_of("aaaa", 'a')) ;
  	assertEquals(-1, MiscellanousFcts.find_first_not_of(null, 'a')) ;
  	
  	// find_last_not_of
   	//
   	assertEquals(4, MiscellanousFcts.find_last_not_of("aabcdaaa", 'a')) ;
   	assertEquals(2, MiscellanousFcts.find_last_not_of("bcd", 'a')) ;
   	assertEquals(-1, MiscellanousFcts.find_last_not_of("aaaa", 'a')) ;
   	assertEquals(-1, MiscellanousFcts.find_last_not_of(null, 'a')) ;
  }
  
  public void testStrip() 
  {
  	// regular
  	//
  	assertEquals("bcd", MiscellanousFcts.strip("aaaabcdaa", STRIP_DIRECTION.stripBoth, 'a')) ;
  	assertEquals("bcdaa", MiscellanousFcts.strip("aaaabcdaa", STRIP_DIRECTION.stripLeft, 'a')) ;
  	assertEquals("aaaabcd", MiscellanousFcts.strip("aaaabcdaa", STRIP_DIRECTION.stripRight, 'a')) ;
  	
  	// Edge cases
   	//
  	assertEquals("", MiscellanousFcts.strip("aaaaaa", STRIP_DIRECTION.stripBoth, 'a')) ;
  	assertEquals("", MiscellanousFcts.strip("aaaaaa", STRIP_DIRECTION.stripLeft, 'a')) ;
  	assertEquals("", MiscellanousFcts.strip("aaaaaa", STRIP_DIRECTION.stripRight, 'a')) ;
  	
  	assertEquals("", MiscellanousFcts.strip("", STRIP_DIRECTION.stripBoth, 'a')) ;
  	assertEquals("", MiscellanousFcts.strip("", STRIP_DIRECTION.stripLeft, 'a')) ;
  	assertEquals("", MiscellanousFcts.strip("", STRIP_DIRECTION.stripRight, 'a')) ;
  	
  	assertEquals(null, MiscellanousFcts.strip(null, STRIP_DIRECTION.stripBoth, 'a')) ;
  	assertEquals(null, MiscellanousFcts.strip(null, STRIP_DIRECTION.stripLeft, 'a')) ;
  	assertEquals(null, MiscellanousFcts.strip(null, STRIP_DIRECTION.stripRight, 'a')) ;
  }
  
  public void testGetNChars() 
  {
  	// regular
  	//
  	assertEquals("aaaa", MiscellanousFcts.getNChars(4, 'a')) ;
  	assertEquals("      ", MiscellanousFcts.getNChars(6, ' ')) ;
  	
  	// Edge cases
   	//
  	assertEquals("", MiscellanousFcts.getNChars(0, 'a')) ;
  	assertEquals("", MiscellanousFcts.getNChars(-1, ' ')) ;
  }
  
  public void testRegex() 
  {
  	assertTrue("yyyy".matches("y{4}")) ;
  	assertFalse("yyyyy".matches("y{4}")) ;
  	
  	assertTrue("MM".matches("M{2}")) ;
  	assertFalse("mM".matches("M{2}")) ;
  	assertFalse("MMM".matches("M{2}")) ;
  	assertFalse("MMab".matches("M{2}")) ;
  	
  	assertEquals("02/12/2015", "02/12/yyyy".replace("yyyy", "2015")) ;
  }
  
  public void testDateAndTimeFormating() 
  {
  	// ------- NativeToFormated
  	//
  	// regular
  	//
  	assertEquals("02/12/2015", MiscellanousFcts.dateFromNativeToFormated("20151202", "dd/MM/yyyy")) ;
  	assertEquals("12/02/2015", MiscellanousFcts.dateFromNativeToFormated("20151202", "MM/dd/yyyy")) ;
  	//
  	// Edge cases
   	//
  	assertEquals("00/00/0000", MiscellanousFcts.dateFromNativeToFormated(null, "MM/dd/yyyy")) ;
  	assertEquals("00/00/0000", MiscellanousFcts.dateFromNativeToFormated("", "MM/dd/yyyy")) ;
  	
  	// ------- FormatedToNative
   	//
   	// regular
   	//
   	assertEquals("20151202", MiscellanousFcts.dateFromFormatedToNative("02/12/2015", "dd/MM/yyyy")) ;
   	assertEquals("20151202", MiscellanousFcts.dateFromFormatedToNative("12/02/2015", "MM/dd/yyyy")) ;
   	//
   	// Edge cases
    //
   	assertEquals("", MiscellanousFcts.dateFromFormatedToNative(null, "MM/dd/yyyy")) ;
   	assertEquals("", MiscellanousFcts.dateFromFormatedToNative("", "MM/dd/yyyy")) ;
   	assertEquals("", MiscellanousFcts.dateFromFormatedToNative("12/02/2015", "mm/dd/yyyy")) ; // lower case 'm'
   	assertEquals("", MiscellanousFcts.dateFromFormatedToNative("12/dd/2015", "mm/dd/yyyy")) ;
   	assertEquals("", MiscellanousFcts.dateFromFormatedToNative("00/00/0000", "mm/dd/yyyy")) ; // lower case 'm'
  }
  
  public void testIsDigit() 
  {
  	// regular
  	//
  	assertTrue(MiscellanousFcts.isDigits("20151202")) ;
  	assertTrue(MiscellanousFcts.isDigits("1")) ;
  	assertTrue(MiscellanousFcts.isDigits("0123456789")) ;
  	
  	// Edge cases
   	//
  	assertFalse(MiscellanousFcts.isDigits(null)) ;
  	assertFalse(MiscellanousFcts.isDigits("")) ;
  	assertFalse(MiscellanousFcts.isDigits("aa123bb")) ;
  	assertFalse(MiscellanousFcts.isDigits("aa123")) ;
  	assertFalse(MiscellanousFcts.isDigits("123bb")) ;
  	assertFalse(MiscellanousFcts.isDigits("foo")) ;
  }
}

