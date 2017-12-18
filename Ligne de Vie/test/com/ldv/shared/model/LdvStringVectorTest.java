package com.ldv.shared.model ;

import junit.framework.TestCase ;

public class LdvStringVectorTest extends TestCase
{

  public void test_findMinAndMax() 
  {
  	LdvStringVector vector = new LdvStringVector() ;
  	
  	vector.AddString("ram") ;
  	vector.AddString("amit") ;
  	vector.AddString("vikash") ;
  	vector.AddString("sahil") ;
  	vector.AddString("akash") ;
  	
  	String sMinFromRam = vector.GetStrictlyInferiorItem("ram") ;
  	String sMaxFromRam = vector.GetStrictlySuperiorItem("ram") ;
  	
  	assertTrue("amit".equals(sMinFromRam)) ;
  	assertTrue("sahil".equals(sMaxFromRam)) ;
  } ;  
}
