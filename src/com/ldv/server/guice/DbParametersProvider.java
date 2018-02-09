package com.ldv.server.guice;

import java.io.File;

import com.google.inject.Provider;
import com.google.inject.Singleton;

import com.ldv.server.DbParameters;

@Singleton
public class DbParametersProvider implements Provider<DbParameters>
{
	@Override
	public DbParameters get()
	{
		return new DbParameters("ldv_admin",
        "nsontology",
				"root",
        "pameline",
        "localhost",
        "3306",
        "C:\\Users\\Philippe\\git\\LigneDeVie\\war\\ldv_manager.log",
        "C:" + File.separator + "Episodus" + File.separator,
        "C:" + File.separator + "Episodus" + File.separator + "objects" + File.separator,
        "C:" + File.separator + "Episodus" + File.separator + "archetypes" + File.separator,
        File.separator) ;
/*
return new DbParameters("ldv_admin",
        "nsontology",
				"root",
        "clrdsmp2010",
        "localhost",
        "3306",
        "/usr/local/tomcat/logs/ldv.log",
        "/home/ldvData/",
        "/home/ldvData/Objects/",
        "/home/ldvData/Archetypes/",
        File.separator) ;
*/
	}
}
