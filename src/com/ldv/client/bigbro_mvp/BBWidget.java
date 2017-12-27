package com.ldv.client.bigbro_mvp;

import com.google.gwt.user.client.ui.Widget;

import com.ldv.client.bigbro.BBControl;
import com.ldv.client.bigbro.BBDialogFunction;
import com.ldv.client.util.ArchetypePanel;

import com.ldv.shared.archetype.LdvArchetypeControl;
import com.ldv.shared.graph.BBMessage;

/**
 * Standard command interface that connects all BB controlled widgets to their BBControl
 * 
 * @author Philippe Ameline
 * 
 */
public class BBWidget
{
	protected BBControl            _control ;
	protected BBControl.CTRL_STATE _eState ;
	protected LdvArchetypeControl  _archetypeControl ;
	
	public BBWidget()
	{
		_control          = null ;
		_eState           = BBControl.CTRL_STATE.unchecked ;
		_archetypeControl = new LdvArchetypeControl() ;
	}
	
	public BBWidget(BBControl control, LdvArchetypeControl  archetypeControl)
	{
		_control          = control ;
		_eState           = BBControl.CTRL_STATE.unchecked ;
		_archetypeControl = new LdvArchetypeControl(archetypeControl) ;
	}
		
	/**
	 * If a function is referenced for the control, execute it for the given situation   
	 * 
	 * @param situation indicates a what step in dialogs life the function is to be executed
	 * @return 1 if other functions can be executed later ; 0 if not
	 * 
	 */
	public int executeFunction(BBDialogFunction.SITUATION_TYPE situation) 
	{
		if ((null == _control) || (null == _control.getDialogFunction()))
			return 1 ;
		
		return _control.getDialogFunction().execute(situation) ;
	}
	
	public void ctrlNotification(BBControl.CTRL_ACTIVATION etatInitial, BBControl.CTRL_ACTIVATION etatSouhaite, BBMessage message, int indexFils) 
	{
		if ((null == _control) || (null == _control.getTransferInfo()))
			return ;
  		
		_control.getTransferInfo().ctrlNotification(etatInitial, etatSouhaite, message, indexFils) ;
	}
	
	/**
	  * Tells if the control is disconnected from its controlling BBItem
	  * 
	  * @return true if disconnected, false if not
	  * 
	  */
	public boolean isDisconnected()
	{
		if (null == _control)
			return true ;
		
		return _control.isDisconnected() ;
	}
	
	public BBControl getBBControl() {
		return _control ;
	}
	public void setBBControl(BBControl control) {
		_control = control ;
	}
	
	public BBControl.CTRL_STATE getState() {
		return _eState ;
	}
	public void setState(BBControl.CTRL_STATE eState) {
		_eState = eState ;
	}
	
	public LdvArchetypeControl getArchetypeControl() {
		return _archetypeControl ;
	}
	public void setArchetypeControl(LdvArchetypeControl archetypeControl) {
		_archetypeControl = archetypeControl ;
	}
	
	public void setControl(Widget control) {
		if (null != _control)
			_control.setControl(control) ;
	}
	
	public void setControlType(BBControl.WNDTYPE iType) {
		if (null != _control)
			_control.setControlType(iType) ;
	}
	
	public void setUserInterface(ArchetypePanel userInterface) {
		if (null != _control)
			_control.setUserInterface(userInterface) ;
	}
}
