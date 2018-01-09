package com.ldv.shared.graph;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * When a client sends a (sub)graph for its changes to be recorded by the server,
 * the server answers with a set of LdvGraphMapping that indicate the IDs that were given to new nodes
 * 
 * @author PA
 *
 */
public class LdvGraphMapping implements IsSerializable 
{
	protected String _sTemporaryNode_ID;
	protected String _sStoredNode_ID ;
	
	protected String _sTemporaryObject_ID ;
	protected String _sStoredObject_ID ;
	
	public String getTemporaryNode_ID() {
		return _sTemporaryNode_ID ;
	}
	public void setTemporaryNode_ID(final String sTemporaryNode_ID) {
		_sTemporaryNode_ID = sTemporaryNode_ID ;
	}
	
	public String getStoredNode_ID() {
		return _sStoredNode_ID ;
	}
	public void setStoredNode_ID(final String sStoredNode_ID) {
		_sStoredNode_ID = sStoredNode_ID ;
	}
	
	public String getTemporaryObject_ID() {
		return _sTemporaryObject_ID ;
	}
	public void setTemporaryObject_ID(final String sTemporaryObject_ID) {
		_sTemporaryObject_ID = sTemporaryObject_ID ;
	}
	
	public String getStoredObject_ID() {
		return _sStoredObject_ID ;
	}
	public void setStoredObject_ID(final String sStoredObject_ID) {
		_sStoredObject_ID = sStoredObject_ID ;
	}
}
