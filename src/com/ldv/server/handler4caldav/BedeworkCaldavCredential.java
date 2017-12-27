package com.ldv.server.handler4caldav;

import org.osaf.caldav4j.credential.CaldavCredential;

public class BedeworkCaldavCredential extends CaldavCredential 
{
	public BedeworkCaldavCredential()
	{
		this._host       = "bedework.example.com" ;
		this._port       = 443 ;
		this._protocol   = "https" ;
		this._user       = "vbede" ;
		this._home       = "/ucaldav/user/" + this._user + "/" ;
		this._password   = "password" ;
		this._collection = "collection/" ;
	}
}