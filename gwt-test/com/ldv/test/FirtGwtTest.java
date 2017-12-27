package com.ldv.test;

import com.google.gwt.junit.client.GWTTestCase;
import com.ldv.client.mvp.LdvMainView;
import com.ldv.client.mvp.LdvView;


public class FirtGwtTest extends GWTTestCase{
	 
	@Override
	  public String getModuleName() 
	  {
	  	return "com.ldv.Ligne_de_Vie" ;
	  }
	 
	public void testSimple() {
	    final LdvMainView view = new LdvMainView();

	    assertNotNull(view.getSend());
	}
}
