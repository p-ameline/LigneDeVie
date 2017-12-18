package com.ldv.server;

import java.text.SimpleDateFormat;
import java.util.*;
import java.io.*;

import com.google.inject.Inject;

public class Logger 
{
	//PRIVATE //

	private static String _sTrace ; // = Ldv_DbParameters._sTrace ;
	
	public static enum TraceLevel { ERROR, WARNING, STEP, SUBSTEP, DETAIL, SUBDETAIL }
	
	@Inject
	public Logger(DbParameters dbParameters)
	{
		_sTrace = dbParameters.getTrace() ;
	}

	public static void trace(String sTraceText, int iUserId, TraceLevel iTraceLevel)
	{
		if (-1 == iUserId)
		{
			trace(sTraceText, "", iTraceLevel) ;
			return ;
		}
		
		trace(sTraceText, Integer.toString(iUserId), iTraceLevel) ;
	}
	
	public static void trace(String sTraceText, String sUserId, TraceLevel iTraceLevel)
	{
		if (null == sTraceText)
			return ;
		
		FileOutputStream out = null ;
		
		try
    {
			out = new FileOutputStream(_sTrace, true) ;
    } 
		catch (FileNotFoundException e1)
    {
	    e1.printStackTrace();
	    return ;
    }
		
		// Time stamp
		//
		Date dateNow = new Date() ;
		SimpleDateFormat ldvFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss") ;
		String sFormatedNow = ldvFormat.format(dateNow) ;
		
		// User Id
		//
		String sUserString = "?" ;
		if (false == "".equals(sUserId))
			sUserString = sUserId ;
		sUserString = " [" + sUserString + "] " ;
		
		// Trace level indicator
		//
		String sTraceLevel = " " ;
		switch (iTraceLevel)
		{
			case ERROR     : sTraceLevel = " Err " ;
			                 break ;
			case WARNING   : sTraceLevel = " ! " ;
                       break ;
			case STEP      : sTraceLevel = "\t" ;
                       break ;
			case SUBSTEP   : sTraceLevel = "\t\t" ;
                       break ;
			case DETAIL    : sTraceLevel = "\t\t\t" ;
                       break ;
			case SUBDETAIL : sTraceLevel = "\t\t\t\t" ;
                       break ;
		}
		
		String s = sFormatedNow + sUserString + sTraceLevel + sTraceText + "\n" ;
		byte data[] = s.getBytes() ;
		try
    {
	    out.write(data, 0, data.length) ;
    } 
		catch (IOException x)
    {
			System.err.println(x);
    }
		finally
		{		
			try
      {
	      out.flush() ;
      } 
			catch (IOException e)
      {
	      e.printStackTrace();
      }
			try
      {
	      out.close() ;
      } 
			catch (IOException e)
      {
	      e.printStackTrace();
      }
		}
	}	
}
