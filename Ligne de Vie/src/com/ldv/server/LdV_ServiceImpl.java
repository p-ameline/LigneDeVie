package com.ldv.server;

import com.ldv.client.LdVService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 */
public class LdV_ServiceImpl extends RemoteServiceServlet implements LdVService
{

	private static final long serialVersionUID = -2607137800060436185L;

	public String[] LdvServer(String sIdentifier, String sPass) {
		String serverInfo = getServletContext().getServerInfo();
		String userAgent = getThreadLocalRequest().getHeader("User-Agent");
		
		String[] aAnswer = new String[2] ;
		
		Ldv_ClientSocket clientSocket = new Ldv_ClientSocket() ;
		/* String sServerAnswer = */ clientSocket.runSocket(sIdentifier + sPass) ;
		
		aAnswer[1] = "1234" ;
		aAnswer[2] = "Hello, " + sIdentifier + "!<br><br>I am running " + serverInfo
				+ ".<br><br>It looks like you are using:<br>" + userAgent + " Status " + clientSocket.get_sStatus() ;
		
		return aAnswer ;
	}
}
