package com.ldv.server;

import java.io.File;

public class DbParameters 
{	
	// Server
	//
	public static String _sUser ;
	public static String _sPass ;
	public static String _sIP ;
	public static String _sPort ;
	public static String _sTrace ;
	
	//Database
	//
	public static String _sBase ;
	public static String _sBaseOntology ;
	
	public static String _sFilesDir ;
	public static String _sObjectsFilesDir ;
	public static String _sArchetypesDir ;
	public static String _sDirSeparator ;
	
	public DbParameters(final String sBase, final String sBaseOntology, final String sUser, final String sPass, final String sIP, final String sPort, final String sTraceFile, final String sFilesDir, final String sObjectsFilesDir, final String sArchetypesDir, final String sDirSeparator)
	{
		_sBase            = sBase ;
		_sBaseOntology    = sBaseOntology ;
		
		_sUser            = sUser ;
		_sPass            = sPass ;
		_sIP              = sIP ;
		_sPort            = sPort ;
		_sTrace           = sTraceFile ;
		
		_sFilesDir        = sFilesDir ;
		_sObjectsFilesDir = sObjectsFilesDir ;
		_sArchetypesDir   = sArchetypesDir ;
		_sDirSeparator    = sDirSeparator ;
	}	
	
	public String getBase() {
		return _sBase ;
	}
	
	public String getBaseOntology() {
		return _sBaseOntology ;
	}
	
	public String getUser() {
		return _sUser ;
	}
	
	public String getPass() {
		return _sPass ;
	}
	
	public String getIP() {
		return _sIP ;
	}
	
	public String getPort() {
		return _sPort ;
	}
	
	public String getTrace() {
		return _sTrace ;
	}
	
	public String getFilesDir() {
		return _sFilesDir ;
	}
	
	public String getObjectsFilesDir() {
		return _sObjectsFilesDir ;
	}
	
	public String getArchetypeDir() {
		return _sArchetypesDir ;
	}
	
	public String getDirSeparator() {
		return _sDirSeparator ;
	}
}
