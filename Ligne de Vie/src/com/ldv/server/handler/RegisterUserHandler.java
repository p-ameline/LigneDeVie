package com.ldv.server.handler;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.ActionException;

import org.apache.commons.logging.Log;

import com.ldv.server.DBConnector;
import com.ldv.server.EMailer;
import com.ldv.server.Logger;
import com.ldv.shared.rpc.LdvRegisterUserAction;
import com.ldv.shared.rpc.LdvRegisterUserResult;

import com.google.inject.Inject;
import com.google.inject.Provider;

public class RegisterUserHandler extends LdvActionHandler<LdvRegisterUserAction, LdvRegisterUserResult>
{
	@Inject
	public RegisterUserHandler(final Log logger,
                             final Provider<ServletContext> servletContext,       
                             final Provider<HttpServletRequest> servletRequest)
	{
		super(logger, servletContext, servletRequest) ;
	}

	/**
	  * Constructor dedicated to unit tests 
	  */
	public RegisterUserHandler()
	{
		super() ;
	}
	
	@Override
	public LdvRegisterUserResult execute(final LdvRegisterUserAction action,
       					                       final ExecutionContext context) throws ActionException 
  {	
		try 
		{			
			DBConnector dbConnector = new DBConnector(true, -1) ;
			
			String sRandomString = buildRandomString(dbConnector) ;
			
			// Record information in the clients table
			//
			int iClientId = insertFuture(action, sRandomString, dbConnector) ;
			if (-1 == iClientId)
				return new LdvRegisterUserResult(false) ;
			
			boolean bMailSuccess = sendValidationMail(action, sRandomString) ;
			if (false == bMailSuccess)
			{
				// TODO remove client
				return new LdvRegisterUserResult(false) ;
			}
			
			return new LdvRegisterUserResult(true) ;
		}
		catch (Exception cause) 
		{
			_logger.error("Unable to know if pseudo exists", cause) ;
   
			throw new ActionException(cause);
		}
  }

	/**
	  * Add information in clients database and returns the corresponding client Id
	  */
	private int insertFuture(LdvRegisterUserAction action, String sRandomString, DBConnector dbConnector)
	{
		if ((null == action) || (null == dbConnector))
			return -1 ;
		
		// Get formated current date
		//
		Date dateNow = new Date() ;
		SimpleDateFormat ldvFormat = new SimpleDateFormat("yyyyMMddHHmmss") ;
		String sFormatedNow = ldvFormat.format(dateNow) ;
		
		// Prepare sql query
		//
		String sQuery = "INSERT INTO futures (pseudo, email, password, language, datetimeRegister, registerId)"
			                        + " VALUES (?, ?, ?, ?, ?, ?)" ; 
		              
		dbConnector.prepareStatememt(sQuery, Statement.RETURN_GENERATED_KEYS) ;
		if (null == dbConnector.getPreparedStatement())
		{
			Logger.trace("SendRegisterHandler.insertFuture: cannot get Statement", -1, Logger.TraceLevel.ERROR) ;
			return -1 ;
		}

		dbConnector.setStatememtString(1, action.getPseudo()) ; 
		dbConnector.setStatememtString(2, action.getEmail()) ;
		dbConnector.setStatememtString(3, action.getPassword()) ;
		dbConnector.setStatememtString(4, action.getLanguage()) ;
		dbConnector.setStatememtString(5, sFormatedNow) ;
		dbConnector.setStatememtString(6, sRandomString) ;
		
		// Execute query 
		//
		int iNbAffectedRows = dbConnector.executeUpdatePreparedStatement(true) ;
		if (-1 == iNbAffectedRows)
		{
			Logger.trace("SendRegisterHandler.insertFuture: failed query " + sQuery, -1, Logger.TraceLevel.ERROR) ;
			dbConnector.closePreparedStatement() ;
			return -1 ;
		}

		ResultSet rs = dbConnector.getResultSet() ;
		int iNewTrajectId = -1 ;
		try
    {
	    if (rs.next())
	    	iNewTrajectId = rs.getInt(1) ;
	    else
	    	return -1 ;
    } 
		catch (SQLException e) {
    	Logger.trace("StackTrace:" + e.getStackTrace(), -1, Logger.TraceLevel.ERROR) ;
    }
		
		dbConnector.closeResultSet() ;
		dbConnector.closePreparedStatement() ;
		
		return iNewTrajectId ;
	}
		
	/**
	  * Generate a random String do be used as a way to distinguish future users
	  */
	private String buildRandomString(DBConnector dbConnector)
	{
		if (null == dbConnector)
			return "" ;
		
		String sResult = "" ;
		
    try
    {
    	// Initialize SecureRandom
      // This is a lengthy operation, to be done only upon initialization of the application
    	SecureRandom prng = SecureRandom.getInstance("SHA1PRNG") ;
    
    	boolean bDoesExist = true ;
    	while (bDoesExist)
    	{
    		// generate a random number
    		String randomNum = new Integer(prng.nextInt()).toString() ;

    		//get its digest
    		MessageDigest sha = MessageDigest.getInstance("SHA-1") ;
    		byte[] byteResult = sha.digest(randomNum.getBytes()) ;
    	
    		sResult = hexEncode(byteResult) ;
    		
    		bDoesExist = ExistsRandomString(sResult, dbConnector) ;
    	}
    } 
    catch (NoSuchAlgorithmException e)
    {
	    // TODO Auto-generated catch block
	    e.printStackTrace() ;
	    return sResult ;
    }
    
    return sResult ;
	}
	
	/**
	  * The byte[] returned by MessageDigest does not have a nice
	  * textual representation, so some form of encoding is usually performed.
	  *
	  * This implementation follows the example of David Flanagan's book
	  * "Java In A Nutshell", and converts a byte array into a String
	  * of hex characters.
	  *
	  * Another popular alternative is to use a "Base64" encoding.
	  */
	static private String hexEncode( byte[] aInput)
	{
		StringBuilder result = new StringBuilder() ;
		char[] digits = {'0', '1', '2', '3', '4','5','6','7','8','9','a','b','c','d','e','f'} ;
		for (int idx = 0; idx < aInput.length; ++idx) 
		{
			byte b = aInput[idx] ;
			result.append( digits[ (b&0xf0) >> 4 ] );
			result.append( digits[ b&0x0f] );
		}
		return result.toString() ;
	}

	/**
	  * Check if the random string already exists in future database
	  */
	private boolean ExistsRandomString(String sRandomString, DBConnector dbConnector)
	{
		if ((null == dbConnector) || (null == sRandomString) || sRandomString.equals(""))
			return false ;

		String sQuery = "SELECT * FROM futures WHERE registerId = '" + sRandomString + "'" ;
    
		Statement dbStatement = dbConnector.getStatement() ;
		if (null == dbStatement)
			return false ;

		// Execute query 
		//
		boolean bSuccess = dbConnector.executeQuery(sQuery) ;
		if (false == bSuccess)
			return false ;
		
		ResultSet rs = dbConnector.getResultSet() ;
		if (null == rs)
			return false ;
		
		boolean bResult ;
		try
    {
	    if (rs.next())
	    	bResult = true ;
	    else
	    	bResult = false ;
    } catch (SQLException e)
    {
	    // TODO Auto-generated catch block
	    e.printStackTrace() ;
	    bResult = false ;
    }
		
    dbConnector.closeResultSet() ;
    
		return bResult ;
	}
	
	/**
	  * Send the registering validation mail
	  */
	public boolean sendValidationMail(LdvRegisterUserAction action, String sRandomString)
	{
		if ((null == action) || (null == sRandomString))
			return false ;
		
		String sToAddr   = action.getEmail() ;
		String sLanguage = action.getLanguage() ; 
		
		Logger.trace("Entering RegisterUserHandler.sendValidationMail: mail= " + sToAddr + " language= " + sLanguage + " string= " + sRandomString, -1, Logger.TraceLevel.DETAIL) ;
		
		String sRealPath = _servletContext.get().getRealPath("") ;
		
		// Get message title
		//
		String sMailTitle = getMailTitle(sRealPath, sLanguage) ;
		
		// Get message body and insert random string in it
		//
		String sMailBody  = getMailBody(sRealPath, sLanguage) ;
		if (sMailBody.contains("%s"))
			sMailBody = sMailBody.replaceAll("%s", sRandomString) ;
		
		EMailer mailer = new EMailer(sRealPath) ;
		boolean bMailSent = mailer.sendEmail("", sToAddr, sMailTitle, sMailBody) ;
		
		return bMailSent ;
	}
	
	/**
	  * Get registering validation mail's title from file
	  */
	protected String getMailTitle(String sRealPath, String sLanguage)
	{
		return getLanguageDependentFile("registerHeader", sRealPath, sLanguage) ;
	}
	
	/**
	  * Get registering validation mail's body from file
	  */
	protected String getMailBody(String sRealPath, String sLanguage)
	{
		return getLanguageDependentFile("registerBody", sRealPath, sLanguage) ;
	}
	
	/**
	  * Get the content of a language dependent message from file
	  */
	protected String getLanguageDependentFile(String sPrefix, String sRealPath, String sLanguage)
	{
		String sFullFileName = sRealPath + "/WEB-INF/" + sPrefix ;
		if (false == sLanguage.equals(""))
			sFullFileName += "_" + sLanguage ;
		sFullFileName += ".txt" ;
			
		String sResult = "" ;
		
		InputStream input = null ;
    try 
    {
    	input = new FileInputStream(sFullFileName) ;
    	if (null != input)
    	{
        String NL = System.getProperty("line.separator") ;
        Scanner scanner = new Scanner(input, "UTF-8") ;
        try {
          while (scanner.hasNextLine()){
          	sResult += scanner.nextLine() + NL ;
          }
        }
        finally{
          scanner.close();
        }
    	}
    }
    catch ( IOException ex ){
      System.err.println("Cannot open and load " + sPrefix + " file for language " + sLanguage) ;
    }
    finally {
      try {
        if (input != null) 
        	input.close() ;
      }
      catch ( IOException ex ){
        System.err.println( "Cannot close " + sPrefix + " file for language " + sLanguage) ;
      }
    }
    return sResult ;
	}
	
	@Override
	public void rollback(final LdvRegisterUserAction action,
        							 final LdvRegisterUserResult result,
        final ExecutionContext context) throws ActionException
  {
		// Nothing to do here
  }
 
	@Override
	public Class<LdvRegisterUserAction> getActionType()
	{
		return LdvRegisterUserAction.class ;
	}
}
