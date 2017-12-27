package com.ldv.shared.archetype ;

import junit.framework.TestCase ;

public class LdvArchetypeHeadTest extends TestCase
{
  public void test_Constructors() 
  {
  	// Test default constructor
   	//
  	LdvArchetypeHead head0 = new LdvArchetypeHead() ;
  	
  	assertTrue(head0.getTitle().equals("")) ;
  	assertTrue(head0.getHelpUrl().equals("")) ;
  	assertTrue(head0.getLang().equals("")) ;
  	assertTrue(head0.getGroup().equals("")) ;
  	
  	// Test standard constructor
   	//
  	LdvArchetypeHead head = new LdvArchetypeHead("Title", "HelpUrl", "Lang", "Group") ;
  	
  	assertTrue(head.getTitle().equals("Title")) ;
  	assertTrue(head.getHelpUrl().equals("HelpUrl")) ;
  	assertTrue(head.getLang().equals("Lang")) ;
  	assertTrue(head.getGroup().equals("Group")) ;
  	
  	// Test copy constructor
   	//
  	LdvArchetypeHead head2 = new LdvArchetypeHead(head) ;
  	
  	assertTrue(head2.getTitle().equals("Title")) ;
  	assertTrue(head2.getHelpUrl().equals("HelpUrl")) ;
  	assertTrue(head2.getLang().equals("Lang")) ;
  	assertTrue(head2.getGroup().equals("Group")) ;
  	
  	// Test equals operator
   	//
  	assertTrue(head2.equals(head)) ;
  	
  	head2.setTitle("Other title") ;
  	
  	assertFalse(head2.equals(head)) ;
  } ;  
}
