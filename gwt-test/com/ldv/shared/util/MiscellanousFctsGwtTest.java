package com.ldv.shared.util ;

import junit.framework.TestCase ;

import com.ldv.shared.util.MiscellanousFcts;

public class MiscellanousFctsGwtTest extends TestCase
{

  public void testisValidMailAddress() 
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
  
}

