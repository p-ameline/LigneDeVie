/**
 * 
 */
package com.ldv.server.handler4caldav;

import org.osaf.caldav4j.credential.CaldavCredential;

public class GCaldavCredential extends CaldavCredential 
{
	public GCaldavCredential() 
	{
		this._host       = "www.google.com" ;
		this._port       = 443 ;
		this._protocol   = "https" ;
		this._user       = "caldav4j@gmail.com" ;
		this._home       = "/calendar/dav/" + this._user + "/" ;
		this._password   = "caldav4j" ;
		this._collection = "events/" ;
	}
}
