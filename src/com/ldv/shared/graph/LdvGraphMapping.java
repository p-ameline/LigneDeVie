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
	
	/**
	 * Default constructor
	 */
	public LdvGraphMapping() {
		reset() ;
	}
	
	/**
	 * Plain vanilla constructor 
	 */
	public LdvGraphMapping(final String sTempObjectID, final String sTempNodeID, final String sStoredObjectID, final String sStoredNodeID)
	{
		_sTemporaryNode_ID   = sTempNodeID ;
		_sStoredNode_ID      = sStoredNodeID ;
		
		_sTemporaryObject_ID = sTempObjectID ;
		_sStoredObject_ID    = sStoredObjectID ;
	}
	
	/**
	 * Copy constructor
	 * 
	 * @param other Model object
	 */
	public LdvGraphMapping(final LdvGraphMapping other)
	{
		reset() ;
		
		if (null == other)
			return ;
		
		initFromMapping(other) ;
	}
	
	/**
	 * Reset all information
	 */
	public void reset()
	{
		_sTemporaryNode_ID   = "" ;
		_sStoredNode_ID      = "" ;
		
		_sTemporaryObject_ID = "" ;
		_sStoredObject_ID    = "" ;
	}
	
	/**
	 * Initialize from another LdvGraphMapping object
	 * 
	 * @param other Object to initialize from
	 */
	public void initFromMapping(final LdvGraphMapping other)
	{
		if (null == other)
			return ;
		
		_sTemporaryNode_ID   = other._sTemporaryNode_ID ;
		_sStoredNode_ID      = other._sStoredNode_ID ;
		
		_sTemporaryObject_ID = other._sTemporaryObject_ID ;
		_sStoredObject_ID    = other._sStoredObject_ID ;
	}
	
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
	
	/**
	  * Determine whether two LdvGraphMapping have the same content
	  * 
	  * @param other LdvGraphMapping to compare to
	  * 
	  * @return <code>true</code> if Ok, <code>false</code> if not
	  */
	public boolean equals(final LdvGraphMapping other)
	{
		if (this == other) 
			return true ;
	
		if (null == other) 
			return false ;
		
		return _sTemporaryNode_ID.equals(other._sTemporaryNode_ID) &&
				   _sStoredNode_ID.equals(other._sStoredNode_ID) &&
				   _sTemporaryObject_ID.equals(other._sTemporaryObject_ID) &&
				   _sStoredObject_ID.equals(other._sStoredObject_ID) ;
	}
	
	/**
	  * Determine whether two objects are exactly similar
	  * 
	  * designed for ArrayList.contains(Obj) method
		* because by default, contains() uses equals(Obj) method of Obj class for comparison
	  * 
	  * @param o Object to compare to
	  * 
	  * @return true if all data are the same, false if not
	  */
	public boolean equals(final Object o) 
	{
		if (this == o) {
			return true ;
		}
		if (null == o || getClass() != o.getClass()) {
			return false;
		}

		final LdvGraphMapping other = (LdvGraphMapping) o ;

		return (this.equals(other)) ;
	}
}
