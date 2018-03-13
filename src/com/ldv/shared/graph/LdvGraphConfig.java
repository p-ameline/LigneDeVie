/*
// -----------------------------------------------------------------------------
// class Configuration
// package nautilus
// -----------------------------------------------------------------------------
// Configuration : variables needed when we work with Nautilus.
// -----------------------------------------------------------------------------
// $Revision: 1.2 $
// $Author: pameline $
// $Date: 2010/05/25 14:31:33 $
// -----------------------------------------------------------------------------
// FLP - september 2004
// CH  - august 2004
// -----------------------------------------------------------------------------
*/

package com.ldv.shared.graph ;

/**
 * The Configuration class contains configuration attributes that are used by
 * other classes of the package.
 */
public interface LdvGraphConfig
{
	// Size of Lexique identifiers
	//
	public static int		LEXI_LEN        = 6 ;
	public static int		LEXI_SENS_LEN   = 5 ;
	public static int   FORMAT_SENS_LEN = 3 ;
	
	// Size of stored information identifiers
	//
	public static int		PERSON_ID_LEN    =  7 ;
	public static int		DOCUMENT_ID_LEN  =  6 ;
	public static int		OBJECT_ID_LEN    = 13 ;  // Tree ID: OBJECT_ID_LEN = PERSON_ID_LEN + DOCUMENT_ID_LEN
	
	public static int		NODE_ID_LEN      =  5 ;
	
	// Separators for semantic paths
	//
	public static String nodeSeparationMARK      = "|" ;
	public static String pathSeparationMARK      = "/" ;
	public static String intranodeSeparationMARK = "." ;
	
	// Server types
	//
	public static int		UNKNOWN_SERVER           = -1 ;
	public static int		LOCAL_SERVER             =  0 ;
	public static int		COLLECTIVE_SERVER        =  1 ;
	public static int		DIRECT_COLLECTIVE_SERVER =  2 ;
	public static int		GROUP_SERVER             =  3 ;
	public static int		DIRECT_GROUP_SERVER      =  4 ;
	
	// Utility chars
	//
	public static char   POUND_CHAR    = '\u00a3' ;
	public static String FREE_TEXT_LEX = String.valueOf(POUND_CHAR) + "?????" ;
	public static String FREE_TEXT_SEM = String.valueOf(POUND_CHAR) + "??" ;
	
/*
	public static int[] SERVER_TYPE = {LOCAL_SERVER, COLLECTIVE_SERVER, 
					                           DIRECT_COLLECTIVE_SERVER, GROUP_SERVER, DIRECT_GROUP_SERVER} ;
*/
    
	// Chars that, when found as first (or second) char of and ID, provide information about object type
	//
  public static char    OBJECT_CHAR         = '$' ;

	public static char		LOCAL_CHAR					= '-' ;					// person/tree has been stored in a local DB
	public static char		MEMORY_CHAR					= '#' ;					// person/tree has never been stored (created only in memory)
	public static char		GROUP_CHAR					= '~' ;
	
	public static String  SYSTEM_USER         = "0000000" ;
	
/*
	public static char[]	TEMP_LOCAL_CHAR				= {'-'} ;					
	public static char[]	TEMP_MEMORY_CHAR			= {'#'} ;
	//the collective server know the group serveur and/or local server					
	public static char[]	TEMP_GROUP_CHAR				= {'~', '-'} ;
	
//	public static char[]	TEMPORARY_CHAR				= {'#', '~', '#', '-'} ;
	//l'ordre d'interpretation pour le temp_char est local, collective, direct_collective, group server, direct group
	public static char[][]	TEMPORARY_CHAR				= {TEMP_MEMORY_CHAR, TEMP_GROUP_CHAR, TEMP_MEMORY_CHAR, 
												TEMP_LOCAL_CHAR, TEMP_MEMORY_CHAR} ;
	
	public static String[] UNKNOWN_USER    = {"#______", "-______", "-______", "~______", "~______"} ;
	public static String[] UNKNOWN_ROOTDOC = {"#_____", "-_____", "-_____", "~_____", "~_____"} ;
	public static String[] UNKNOWN_OBJECT  = {"#____________", "-____________", "-____________", "~____________", "~____________"} ;
*/
}
