package com.ldv.client.bigbro;

import com.ldv.shared.graph.BBMessage;
import com.ldv.client.bigbro.BBControl.CTRL_ACTIVATION;

public class BBTransferInfo 
{
	public enum CTRL_ACTIVITY      { disabledCtrl, inactiveCtrl, activeCtrl } ;
	public enum TRANSFER_DIRECTION { tdGetData, tdSetData } ;
	
	protected BBFilsItem	                _BBFilsItem ;		    // BBItem in command of control

  protected CTRL_ACTIVITY               _iActive ;          // 0, 1 or -1 for grayed
  protected BBMessage                   _TransfertMessage = new BBMessage() ; // donn�es patpatho complement, certitude,...

  protected CTRL_ACTIVITY               _iTmpActive ;        // Informations temps r�el

  protected BBVectFatheredPatPathoArray _TransPatpatho = new BBVectFatheredPatPathoArray() ; // Patpatho "vraie"
  protected BBVectFatheredPatPathoArray _TempoPatpatho = new BBVectFatheredPatPathoArray() ; // Patpatho temporaire

  protected BBControl                   _Control ;
	
	public BBTransferInfo(BBFilsItem filsItem) 
	{
		init() ;
		
		_BBFilsItem = filsItem ;
	}
	
	public BBTransferInfo(final BBTransferInfo src) {
		initFromModel(src) ;
	}
	
  /**
	*  Equivalent of = operator
	*  
	*  @param src Object to initialize from 
	**/
	public void initFromModel(final BBTransferInfo src)
	{
		init() ;
		
		if (null == src)
			return ;
		
	}
  
  public void init() 
  {
  	_BBFilsItem = null ;
  	_Control    = null ;
  	
  	_iActive    = CTRL_ACTIVITY.inactiveCtrl ;
  	_iTmpActive = CTRL_ACTIVITY.inactiveCtrl ;
  }
  
  /**
	*  A control wants to change its state and notice the BBItem it is the UI of
	*  
	*  @param currentState state the control wants to switch from
	*  @param wishedState  state the control wants to switch to
	*  @param message      control content
	*  @param sonIndex     
	*  
	**/
  public void ctrlNotification(BBControl.CTRL_ACTIVATION currentState, BBControl.CTRL_ACTIVATION wishedState, BBMessage message, int sonIndex)
  {
  	if (null == _BBFilsItem)
  		return ;
  	
  	BBItem father = _BBFilsItem.getFatherItem() ;
  	if (null == father)
  		return ;
  	
  	father.ctrlNotification(_BBFilsItem, wishedState, message, sonIndex) ;
  }
  
  public int Transfer(TRANSFER_DIRECTION direction)
  {
  	if (null != _Control)
  		return _Control.Transfer(direction) ;
  	return 1 ;
  }
  
  public void UpdateMessagePostTransfert()
  {
  	if ((null == getFilsItem()) || (null == _TransfertMessage))
  		return ;
  	
  	String sLabel = getFilsItem().getLabel() ;
    _TransfertMessage.UpdateCertitudeAndPlural(sLabel) ;
  }
  
  public BBMessage getTransfertMessage() {         
  	return _TransfertMessage ;
  }
  
  public BBControl getControl() {
  	return _Control ;
  }
  public void setControl(BBControl control) {
  	_Control = control ;
  }
  
  public BBFilsItem	getFilsItem() {      
  	return _BBFilsItem ;
  }
  
  public BBVectFatheredPatPathoArray getTransPatpatho() {
  	return _TransPatpatho ;
  }
  
  public CTRL_ACTIVITY getActivationStatus() {
  	return _iActive ;
  }
  public void setActivationStatus(CTRL_ACTIVITY status) {
  	_iActive = status ;
  }
  
  public void activateControl(CTRL_ACTIVATION activation, BBMessage message)
  {
  	if (null == _Control)
  		return ;
  	
  	_Control.activateControl(activation, message) ;
  }
  
  /**
	  * Cut the link with the control layer
	  * 
	  */
  public void unlinkFromControl()
  {
  	if (null == _Control)
  		return ;
  	
  	// First detach the user interface control from this object
  	//
  	_Control.setTransferInfo(null) ;
  	
  	// Then unlink from the user interface control
  	//
  	_Control = null ;
  }
  
	/**
	  * Determine whether two BBTransferInfo are exactly similar
	  * 
	  * @return true if all data are the same, false if not
	  * @param  itemData BBTransferInfo to compare
	  * 
	  */
	public boolean equals(BBTransferInfo itemData)
	{
		if (this == itemData) {
			return true ;
		}
		if (null == itemData) {
			return false ;
		}
		
		return (_BBFilsItem.equals(itemData._BBFilsItem) &&
				    _Control.equals(itemData._Control)) ;
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

		final BBTransferInfo itemData = (BBTransferInfo) o ;

		return equals(itemData) ;
	}
	
	public void Activate() {
		_iActive = CTRL_ACTIVITY.activeCtrl ;
	}
	
	public void Unactivate() {
		_iActive = CTRL_ACTIVITY.inactiveCtrl ;
	}
}
