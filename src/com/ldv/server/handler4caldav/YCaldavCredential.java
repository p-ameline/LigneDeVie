/**
 * 
 */
package com.ldv.server.handler4caldav;

import org.osaf.caldav4j.credential.CaldavCredential;

public class YCaldavCredential extends CaldavCredential 
{
	public YCaldavCredential() 
	{
		this._host       = "caldav.calendar.yahoo.com" ;
		this._port       = 443 ;
		this._protocol   = "https" ;
		this._user       = "yahooid" ;
		this._home       = "/dav/" + this._user + "/Calendar/" ;
		this._password   = "password" ;
		this._collection = "collection/" ;
	}
}
