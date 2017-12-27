package com.ldv.client.bigbro;

import com.google.gwt.user.client.ui.Widget;

import com.ldv.client.bigbro_mvp.BBGroupBox;
import com.ldv.client.bigbro_mvp.BBRadioButton;
import com.ldv.client.bigbro_mvp.BBStatic;
import com.ldv.client.bigbro_mvp.BBTextBox;
import com.ldv.client.util.ArchetypePanel;

import com.ldv.shared.archetype.LdvArchetypeControl;
import com.ldv.shared.graph.BBMessage;
import com.ldv.shared.util.MiscellanousFcts;

public class BBControl 
{
	//
	// Enumeration of interface controls
	//
	public enum WNDTYPE { isUndefined, isDlg, isGroup, isEdit, isBtn, isRadioBtn, isCaseACocher,
                        isFunct, isOnglet, isTreeNode, isTreeWindow, isEditLexique,
                        isEditLexiqueDerive, isEditNoLexique, isNSCSVue, isStatic,
                        isEditDate, isEditDateHeure, isAdrListWindow,
                        isHistoryListWindow, isComboClassif, isComboSemantique,
                        isComboList, isCorListWindow, isHistoryValListWindow } ;
                 
  public enum CTRL_STATE      { checked, unchecked, grayed, hidden } ;
  public enum CTRL_ACTIVATION { checked, unchecked, delete } ;
	
	
	protected String        _sIdentity ;  // Code du contr�le

  protected ArchetypePanel	 _UserInterface ;	  
  // NSBBMUEView*    _pMUEView ;

  protected BBTransferInfo   _Transfert ;	    // Data collecting object
  
      /*S -filling on setup window; F - filling on focus  */
  protected String	         _sFilling ;	        // type du cochage du controle
  protected String           _sFillingFilter ;    // element de recherche pour le filling : le filling inclut ce qui est apr�s
  protected String           _sFillingStarter ;   // element de d�part pour le filling : le filling se fait � partir de cet �l�ment
  
  // NSSearchStruct  _searchStruct ;
  // string          _sPathControl ;      // chemin du controle ma�tre associ� � une date

  protected String	         _sHelpText ;	        

  protected Widget			     _Control ;	          // Physical instance as a widget
  protected WNDTYPE				   _iType ;		          // Control's type

  protected boolean          _bSelfManageMultiple ; // This control manages multiple descriptions on its own (without the need for a multi dialog)

  protected BBDialogFunction _dlgFct ;	          // Fonction de dialogue rattach�e

  protected boolean          _bVisible ;
  protected boolean          _bDisconnected ;
	
	public BBControl(BBItem item, LdvArchetypeControl control) 
	{
		init() ;
		
		if (null == control)
			return ;
		
		_sIdentity = control.getDataIdentity() ;
		_sHelpText = control.getHelpText() ;
		
		if (null != item)
		{
			linkTransfert(item) ;
		
			if (false == "".equals(control.getDataFunction()))
				_dlgFct = new BBDialogFunction(item.getBigBoss(), item, control.getDataFunction(), this) ;
		}
	}
	
	public BBControl(final BBControl src) {
		initFromModel(src) ;
	}
	
  /**
	*  Equivalent of = operator
	*  
	*  @param src Object to initialize from 
	**/
	public void initFromModel(final BBControl src)
	{
		init() ;
		
		if (null == src)
			return ;
		
	}
  
  public void init() 
  {
  	_sIdentity        = "" ;
  	_UserInterface    = null ;
  	_Transfert        = null ;
  	_dlgFct           = null ;
  	
  	_Control          = null ;
  	_iType            = WNDTYPE.isUndefined ;
  	
  	_sFilling         = "" ;	  
    _sFillingFilter   = "" ; 
    _sFillingStarter  = "" ;
    
    _sHelpText        = "" ;
    
    _bSelfManageMultiple = false ;
    _bVisible            = true ;
    _bDisconnected       = false ;
  }
  	
  public BBTransferInfo getTransferInfo() {         
  	return _Transfert ;
  }
  public void setTransferInfo(BBTransferInfo transfert) {         
  	_Transfert = transfert ;
  }
  
  public String getIdentity() {       
  	return _sIdentity ;
  }
  
  public ArchetypePanel getUserInterface() {	 
  	return _UserInterface ;
  }
  public void setUserInterface(ArchetypePanel userInterface) {	 
  	_UserInterface = userInterface ;
  }
  
  public Widget	getControl() {		     
  	return _Control ;
  }
  public void	setControl(Widget control) {		     
  	_Control = control ;
  }
  
  public WNDTYPE getControlType() {
  	return _iType ;
  }
  public void setControlType(WNDTYPE iType) {
  	_iType = iType ;
  }
  
  public BBDialogFunction getDialogFunction() {
  	return _dlgFct ;
  }
  
  public boolean doesSelfManageMultiple() {
  	return _bSelfManageMultiple ;
  }
  
  public String getHelpText() { 
  	return _sHelpText ;
  }
  public void setHelpText(String sHelpText) { 
  	_sHelpText = sHelpText ;
  }
  
  /**
	  * Connects the user interface side and the description side by means of the BBTransferInfo object according to the schema:<br>
	  * BBItem -> BBFilsItem -> BBTransferInfo <- BBControl <- Interface
	  * 
	  * @param  item BBItem to connect to
	  * 
	  * @return <code>true</code> if connection was possible, <code>false</code> if not
	  *
	  */
  public boolean linkTransfert(BBItem item)
  {
  	if (null == item)
  		return false ;

  	// It is not possible to connect an undefined control 
  	//
  	if ("".equals(_sIdentity) || "#".equals(_sIdentity))
  		return false ;
  	
		// Asking the BBItem in control of the panel to find the BBFilsItem with proper identity
		//
 		BBFilsItem sonItem = item.getSonInPanel(_sIdentity, "") ;
 		
 		if (null == sonItem)
 		{
 			// string errmess = "WARNING !!! Orphan control" ;
 			_Transfert = null ;
 			return false ;
 		}
 		
 		// If the BBFilsItem has been found, we connect its 
 		//
 		_Transfert = sonItem.getTransfert() ;
 		_Transfert.setControl(this) ;
 		
 		if (sonItem.isActivated())
 			activateControl(CTRL_ACTIVATION.checked, null) ;
 		
 		return true ;
	}

  public void activateControl(CTRL_ACTIVATION activation, BBMessage message)
  {
  	if (null == _Control)
  		return ;
  	
  	switch (_iType)
  	{
  		case isRadioBtn : 
  			BBRadioButton radio = (BBRadioButton) _Control ;
  			radio.activateControl(activation, message) ;
  			return  ;
  		case isEdit :
  			BBTextBox textBox = (BBTextBox) _Control ;
  			textBox.activateControl(activation, message) ;
  			return  ;
  		case isStatic :
  			BBStatic staticCtrl = (BBStatic) _Control ;
  			staticCtrl.activateControl(activation, message) ;
  			return  ;
  		case isGroup :
  			BBGroupBox group = (BBGroupBox) _Control ;
  			group.activateControl(activation, message) ;
  			return  ;
  	}  	
  }
  
  public int Transfer(BBTransferInfo.TRANSFER_DIRECTION direction)
  {
  	if (null == _Control)
  		return 0 ;
  	
  	switch (_iType)
  	{
  		case isRadioBtn : 
  			BBRadioButton radio = (BBRadioButton) _Control ;
  			return radio.Transferer(direction, _Transfert) ;
  		case isEdit :
  			BBTextBox textBox = (BBTextBox) _Control ;
  			return textBox.Transferer(direction, _Transfert) ;
  		case isStatic :
  			BBStatic staticCtrl = (BBStatic) _Control ;
  			return staticCtrl.Transferer(direction, _Transfert) ;
  		case isGroup :
  			BBGroupBox group = (BBGroupBox) _Control ;
  			return group.Transferer(direction, _Transfert) ;
  	}
  	
  	return 1 ;
  }
  
	/**
	  * Determine whether two BBItemData are exactly similar
	  * 
	  * @return true if all data are the same, false if not
	  * @param  itemData BBItemData to compare
	  * 
	  */
	public boolean equals(BBControl other)
	{
		if (this == other)
			return true ;
		
		if (null == other) 
			return false ;
		
		return (MiscellanousFcts.areIdenticalStrings(_sIdentity, other._sIdentity)) ;
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

		final BBControl itemData = (BBControl) o ;

		return equals(itemData) ;
	}
	
	/**
	  * Tells if a control is disconnected from its controlling BBItem
	  * 
	  * @return true if disconnected, false if not
	  * 
	  */
	public boolean isDisconnected() {       
		return _bDisconnected ;
	}
}
