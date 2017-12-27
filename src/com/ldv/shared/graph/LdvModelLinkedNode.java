package com.ldv.shared.graph;

import com.google.gwt.user.client.rpc.IsSerializable;

import com.ldv.shared.graph.LdvGraphEnum.NODELINKDIRECTION;
import com.ldv.shared.graph.LdvGraphEnum.NODELINKTYPES;
import com.ldv.shared.graph.LdvModelNode;

public class LdvModelLinkedNode implements IsSerializable
{

	protected String            _sOtherNodeID ;
	protected LdvModelNode      _OtherTemporaryNode ;

	protected NODELINKTYPES     _iLinkType ;
  protected NODELINKDIRECTION _iLinkDirection ;
  protected boolean           _bLinkWithDoc ;
	
	public LdvModelLinkedNode() {
		init() ;
	}

	public LdvModelLinkedNode(String sNodeID, NODELINKTYPES iLkType, NODELINKDIRECTION iLkDirection, boolean bLkWithDoc)
	{
		_sOtherNodeID       = sNodeID ;
		_OtherTemporaryNode = null ;

		_iLinkType          = iLkType ;
	  _iLinkDirection     = iLkDirection ;
	  _bLinkWithDoc       = bLkWithDoc ;
	}
	
	public LdvModelLinkedNode(LdvModelNode otherNode, NODELINKTYPES iLkType, NODELINKDIRECTION iLkDirection, boolean bLkWithDoc)
	{
		_sOtherNodeID       = "" ;
		_OtherTemporaryNode = otherNode ;

		_iLinkType          = iLkType ;
	  _iLinkDirection     = iLkDirection ;
	  _bLinkWithDoc       = bLkWithDoc ;
	}
	
	public LdvModelLinkedNode(final LdvModelLinkedNode src) {
		initFromModel(src) ;
	}

  /**
	*  Equivalent of = operator
	*  
	*  @param src Object to initialize from 
	**/
	public void initFromModel(final LdvModelLinkedNode src)
	{
		init() ;
		
		if (null == src)
			return ;
		
		_sOtherNodeID       = src._sOtherNodeID ;
		_OtherTemporaryNode = src._OtherTemporaryNode ;

		_iLinkType          = src._iLinkType ;
	  _iLinkDirection     = src._iLinkDirection ;
	  _bLinkWithDoc       = src._bLinkWithDoc ;		
	}
  
  public void init() 
  {
  	_sOtherNodeID       = "" ;
		_OtherTemporaryNode = null ;

		_iLinkType          = LdvGraphEnum.NODELINKTYPES.badLink ;
	  _iLinkDirection     = LdvGraphEnum.NODELINKDIRECTION.dirFleche ;
	  _bLinkWithDoc       = false ;
  }
  
	public String getOtherNodeID() {
		return _sOtherNodeID ;
	}
	public void setOtherNodeID(String sOtherNodeID) {
		_sOtherNodeID = sOtherNodeID ;
	}

	public LdvModelNode getOtherTemporaryNode() {
		return _OtherTemporaryNode ;
	}
	public void setOtherTemporaryNode(LdvModelNode OtherTemporaryNode) {
		_OtherTemporaryNode = OtherTemporaryNode ;
	}

	public NODELINKTYPES getLinkType() {
		return _iLinkType ;
	}
	public void setLinkType(NODELINKTYPES iLinkType) {
		_iLinkType = iLinkType ;
	}

	public NODELINKDIRECTION getLinkDirection() {
		return _iLinkDirection ;
	}
	public void setLinkDirection(NODELINKDIRECTION iLinkDirection) {
		_iLinkDirection = iLinkDirection ;
	}

	public boolean isLinkedWithDoc() {
		return _bLinkWithDoc ;
	}
	public void setLinkedWithDoc(boolean bLinkWithDoc) {
		_bLinkWithDoc = bLinkWithDoc ;
	}

	/**
	  * Determine whether two BBMessage are exactly similar
	  * 
	  * @return true if all data are the same, false if not
	  * @param  otherLN BBLinkedNode to compare with
	  * 
	  */
	public boolean equals(LdvModelLinkedNode otherLN)
	{
		if (this == otherLN) {
			return true ;
		}
		if (null == otherLN) {
			return false ;
		}
		
		return (_sOtherNodeID.equals(otherLN._sOtherNodeID)  &&
						_OtherTemporaryNode.equals(otherLN._OtherTemporaryNode) &&
						(_iLinkType      == otherLN._iLinkType)      &&
						(_iLinkDirection == otherLN._iLinkDirection) &&
						(_bLinkWithDoc   == otherLN._bLinkWithDoc)) ;
	}

	/**
	  * Determine whether an object is exactly similar to this
	  * 
	  * @return true if all data are the same, false if not
	  * @param o Object to compare
	  * 
	  */
	public boolean equals(Object o) 
	{
		if (this == o) {
			return true ;
		}
		if (null == o || getClass() != o.getClass()) {
			return false;
		}

		final LdvModelLinkedNode itemData = (LdvModelLinkedNode) o ;

		return equals(itemData) ;
	}
}
