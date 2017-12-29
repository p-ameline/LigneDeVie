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
		return new DbParameters("database",
                            "ontology database (nsontology)",
                            "database_user",
                            "database_pass",
                            "database_url (can be localhost)",
                            "database port (3306 for MySQL)",
                            "/usr/local/tomcat/logs/ldv.log",
                            "/home/ldvData/",
                            "/home/ldvData/Objects/",
                            "/home/ldvData/Archetypes/",
                            File.separator) ;
	}
}
