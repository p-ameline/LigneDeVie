package com.ldv.server.handler ;

import net.customware.gwt.dispatch.client.standard.StandardDispatchService;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.ldv.server.guice.ServerModule;
import com.ldv.shared.rpc.LdvRegisterUserAction;
import com.ldv.shared.rpc.LdvRegisterUserResult;
import com.ldv.shared.rpc.LdvValidatorUserAction;
import com.ldv.shared.rpc.LdvValidatorUserResult;

import junit.framework.TestCase ;

public class ValidateUserHandlerTest extends TestCase
{
	private StandardDispatchService testSvc ;
	private String                  sGeneratedId ;
		
	@Override
	protected void setUp() throws Exception
	{
		super.setUp();
		Injector inj = Guice.createInjector(new ServerModule(), new DispatchTestModule()) ;
		testSvc = inj.getInstance(StandardDispatchService.class) ;
	}
	
	public void testRegisterUser() throws Exception 
  {
		String sPseudo   = "toto" ;
		String sPassword = "totopass" ;
		String sEmail    = "philippe.ameline@free.fr" ;
		String sLanguage = "fr" ;
		
  	LdvRegisterUserAction action = new LdvRegisterUserAction(sPseudo, sPassword, sEmail, sLanguage) ;
  	
  	LdvRegisterUserResult registerResult = (LdvRegisterUserResult) testSvc.execute(action) ;
  	assertTrue(registerResult.wasSuccessful()) ;
  	
  	sGeneratedId = registerResult.getGeneratedId();
  }
	
  public void testValidateUser() throws Exception 
  {
  	String sPassword = "totopass" ;
  	String sKey      = "GtU93Ol*TH";
  	
  	LdvValidatorUserAction action = new LdvValidatorUserAction(sGeneratedId, sPassword, sKey) ;
  	
  	LdvValidatorUserResult validateResult = (LdvValidatorUserResult) testSvc.execute(action) ;
  	assertTrue(validateResult.wasSuccessful()) ;
  }
}
