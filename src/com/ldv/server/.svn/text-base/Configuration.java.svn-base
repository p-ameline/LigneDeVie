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

package com.ldv.server ;

/**
 * The Configuration class contains configuration attributes that are used by
 * other classes of the package.
 */
public interface Configuration
{
	public static int		LEXIQUE_LEN      = 6 ;
	public static int		LEXIQUE_SENS_LEN = 5 ;
	
	public static int		PERSON_ID_LEN    =  7 ;
	public static int		DOCUMENT_ID_LEN  =  6 ;
	public static int		NODE_ID_LEN      =  5 ;
	public static int		OBJECT_ID_LEN    = 13 ;
	
	public static String	FILE_DEBUG_LINE   = "debug" ;
	public static String	DEFAUT_FILE_DEBUG = "errorPilot.log" ;
	public static String	LEVEL_DEBUG       = "level" ;
	
	public static int		LOCAL_SERVER             = 0 ;
	public static int		COLLECTIVE_SERVER        = 1 ;
	public static int		DIRECT_COLLECTIVE_SERVER = 2 ;
	public static int		GROUP_SERVER             = 3 ;
	public static int		DIRECT_GROUP_SERVER      = 4 ;
	public static int[]	SERVER_TYPE	= {LOCAL_SERVER, COLLECTIVE_SERVER, 
					                           DIRECT_COLLECTIVE_SERVER, GROUP_SERVER, DIRECT_GROUP_SERVER} ;

	public static int		USER_UNCHANGE             = -1 ;
	public static int		PASSWORD_CHANGE           =  1 ;
	public static int		LOGIN_AND_PASSWORD_CHANGE =  2 ;
	public static int		PROPS_CHANGE              =  4 ;

	//data mark. Used in tree modification 
	public static int NOT_MARKED       = 0 ;
  public static int SAME_LOC         = 1 ;
  public static int NEW_LOC          = 2 ;
  public static int MARKED           = 3 ;
  public static int NEW_MARKED       = 4 ;
  public static int EXCEPTION_MARKED = 5 ;
    
  public static String OBJECT_CHAR = "$" ;

	public static String[] UNKNOWN_USER    = {"#______", "-______", "-______", "~______", "~______"} ;
	public static String[] UNKNOWN_ROOTDOC = {"#_____", "-_____", "-_____", "~_____", "~_____"} ;
	
	//{local, collective, direct_collective, group, direct group}
	public static String[]	FIRST_USER					= {"-000000", "A000000", "A000000", "~000000","~000000"} ;

	public static String[]	UNKNOWN_OBJECT				= {"$#___________", "$-___________", "~00000000000"} ;
	public static String[]	FIRST_OBJECT				= {"$-00000000000", "$000000000000", "$000000000000",
										 "$~00000000000", "$~00000000000"} ;

	public static String[]	FIRST_NODE					= {"-0000", "00000", "00000", "~0000", "~0000"} ;
	public static String[]	FIRST_DOCUMENT				= { "-00000", "000000", "000000", "~00000", "~00000"} ;
	public static String	FIRST_TRANSACTION			= "000000" ;
	public static String	ERROR_TRANSACTION			= "-_____" ;
	public static int		START						= 1 ;
	public static int		END							= 2 ;
	public static int		CONTAIN						= 3 ;
	public static int		EQUAL						= 0 ;
	
	public static int[]		TYPE_RESEARCH				= {START, END, CONTAIN, EQUAL}  ;

	public static char		LOCAL_CHAR					= '-' ;					// person/tree has been stored in a local DB
	public static char		MEMORY_CHAR					= '#' ;					// person/tree has never been stored (created only in memory)
	public static char		GROUP_CHAR					= '~' ;
	
	
	public static char[]	TEMP_LOCAL_CHAR				= {'-'} ;					
	public static char[]	TEMP_MEMORY_CHAR			= {'#'} ;
	//the collective server know the group serveur and/or local server					
	public static char[]	TEMP_GROUP_CHAR				= {'~', '-'} ;
	
//	public static char[]	TEMPORARY_CHAR				= {'#', '~', '#', '-'} ;
	//l'ordre d'interpretation pour le temp_char est local, collective, direct_collective, group server, direct group
	public static char[][]	TEMPORARY_CHAR				= {TEMP_MEMORY_CHAR, TEMP_GROUP_CHAR, TEMP_MEMORY_CHAR, 
												TEMP_LOCAL_CHAR, TEMP_MEMORY_CHAR} ;

	public static String	DAYS_NB_TAGNAME           = "daysNB" ;			// nb validities days for the password
	public static String	USER_TYPE_TAGNAME         = "userType" ;			// user role : U, A, UA
	public static String	TYPE_PASSWD_TAGNAME       = "typePassword" ;		// type user password (fixe, temporary...)
	public static String	START_DATE_TAGNAME        = "startDate" ;			// start validity date from a passwd
	public static String	LOGIN_TAGNAME             = "login" ;
	public static String	PASSWORD_TAGNAME          = "password" ;
	public static String	USER_TAGNAME              = "user" ;
	public static String	PATIENT_TAGNAME           = "patient" ;
	public static String	CORRESPONDENT_TAGNAME     = "correspondent" ;
	public static String	FULLNAME_TAGNAME          = "fullName" ;
	public static String	PERSON_ROLE_TAGNAME       = "role" ;				// patient, user, correspondent
	public static String	FIRSTNAME_TAGNAME         = "firstName" ;
	public static String	LASTNAME_TAGNAME          = "lastName" ;
	public static String	ADVANCED_RESEARCH_TAGNAME = "advancedResearch" ;

	public static String	CORRESPONDENT_ROLE = "C" ;
	public static String	PATIENT_ROLE       = "P" ;
	public static String	USER_ROLE          = "U" ;
	public static String	ADMIN_ROLE         = "A" ;
	public static String	ALL_ROLE           = "L" ; // all roles : patient + user + correspondent

	public static String	EXPIRED_RIGHTS			= "Expired" ;
	public static String	USER_RIGHTS					= "UserRights" ;
	public static String	ADMIN_RIGHTS				= "AdminRights" ;
	public static String	NOT_RIGHTS					= "NotRights" ;

	public static String	PASSWORD_MUST_CHANGE    = "T" ;
	public static String	PASSWORD_MUST_CHANGE_IN = "J" ;					//password must change in X days
	public static String	PASSWORD_FIXE           = "F" ;
	public static String	USER_PRIVILEGE          = "G" ;
	public static String	LOCKED_USER             = "V" ;
	public static String	DESEABLED_USER          = "D" ;

	public static String	OPERATOR_TAGNAME			= "operator" ;
	public static String	PERSON_TAGNAME				= "person" ;
	public static String	CONSOLE_TAGNAME				= "console" ;
	public static String	INSTANCE_TAGNAME			= "instance" ;
	public static String	OBJECT_TAGNAME				= "object" ;
	
	
	public static String ACTIF_TAGNAME  = "actif" ;	//actif ou inactif
	public static int    ACTIF_PERSON   = 1 ;	
	public static int    INACTIF_PERSON = 0 ;
	
	public static String STATUS_TAGNAME  = "status" ; // decede, perdu de vue, fusione
	public static int    DECEASED_STATUS = 2 ;				// deceased	0001
	public static int    LOST_STATUS     = 3 ;				// lost		0010
	public static int    FUSION_STATUS   = 1 ;				// fusion	0100
	
	public static String[]	PERSON_TYPE					= {ACTIF_TAGNAME, STATUS_TAGNAME };

	//Service errors
	public static String	ERR_BAD_PARAMS          = "ERR_BAD_PARAMS" ;
  public static String	ERR_TRAITS              = "ERR_TRAITS" ;
	public static String	ERR_WRITE               = "ERR_WRITE" ;
	public static String	ERR_NOT_USER            = "ERR_NOT_USER" ;
	public static String	ERR_NOT_RIGHT           = "ERR_NOT_RIGHT" ;
	public static String	ERR_NOT_MODIFY          = "ERR_NOT_MODIFY" ;

	//Warnings
	public static String	WAR_NOT_RIGHTS          = "insufficientUserRights" ;
	public static String	WAR_NOT_DATA            = "noData" ;
	public static String	WAR_RESEARCH_DATA       = "WAR_RESEARCH_DATA" ;
	public static String	WAR_GET_CONNECTION      = "WAR_GET_CONNECTION" ;
	public static String	WAR_NO_HEALTH_TEAM_TREE = "noHealthTeamTree" ;
	public static String	WAR_NO_COMPTE_ADM       = "noPatientAccountAdministrator" ;

	//user errors
	public static String	PERSON_ID_MISSING        = "PersonIdMissing" ;
	public static String	USER_ID_MISSING          = "UserIdMissing" ;
	public static String	CONSOLE_ID_MISSING       = "ConsoleIdMissing" ;
	public static String	INSTANCE_ID_MISSING      = "InstanceIdMissing" ;
	public static String	OBJECT_ID_MISSING        = "ObjectIdMissing" ;
	public static String	WRONG_GRAPH_STRUCTURE		 = "WrongGraphStructure" ;
	public static String	WRONG_RESOURCE_STRUCTURE = "WrongResourceStructure" ;

	
	//--------------------------------------------------------------------------
	//the links sens
	public static int FORWARD     = 0 ;
	public static int BACK        = 1 ;
	public static int DOUBLE_SENS = 2 ;
	
	public static String PERSISTENT_CONNECT = "persistent" ;
}
