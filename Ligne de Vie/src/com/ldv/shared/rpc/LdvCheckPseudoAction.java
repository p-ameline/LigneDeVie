package com.ldv.shared.rpc;

import net.customware.gwt.dispatch.shared.Action;

public class LdvCheckPseudoAction implements Action<LdvCheckPseudoResult> 
{	
	private String _sPseudo ;
	
	public LdvCheckPseudoAction() {
		super() ;
		
		_sPseudo = "" ;
	}
	
	public LdvCheckPseudoAction(String sPseudo) 
	{
		_sPseudo = sPseudo ;
	}
	
	public String getPseudo() {
		return _sPseudo ;
	}
	public void setPseudo(String sPseudo) {
		_sPseudo = sPseudo ;
	}
}
