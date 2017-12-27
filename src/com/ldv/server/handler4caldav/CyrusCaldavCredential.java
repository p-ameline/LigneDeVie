/**
 * 
 */
package com.ldv.server.handler4caldav;

import org.osaf.caldav4j.credential.CaldavCredential;

/**
* Base credential class for CalDAV4j connection to Cyrus
*/
public class CyrusCaldavCredential extends CaldavCredential 
{
	
	/**
	* Constructor
	* 
	* @param sHost     Hostname à la "www.myserver.com"
	* @param sUser     User ID for Cyrus
	* @param sPassword Password (what else?) 
	* 
	*/
	public CyrusCaldavCredential(final String sHost, final String sUser, final String sPassword) 
	{
		// From https://cyrusimap.org/docs/cyrus-imapd/2.5.3/install-http.php#CalDAV
		//
		// Enter a URL of the following form as the Location: https://<servername>/dav/calendars/user/<userid>/<calendar>/
		// Cyrus will auto-provision a calendar with name "Default" which can be used in the URL above.
		
		this._host       = sHost ;
		this._port       = 8008 ;
		this._protocol   = "http" ;
		this._user       = sUser ;
		this._home       = "/dav/calendars/user/" + this._user + "/Default/" ;
		this._password   = sPassword ;
		this._collection = "events/" ;
		
		this._bAddUserToRoot = false ;  // for user eve path should be "/dav/calendars/user/eve/Default/"
		                                // and not "/dav/calendars/user/eve/Default/eve/"
	}
}
