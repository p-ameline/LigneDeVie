package com.ldv.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("ligne_de_vie")
public interface LdVService extends RemoteService {
	String[] LdvServer(String sLogin, String sPassword) ;
}
