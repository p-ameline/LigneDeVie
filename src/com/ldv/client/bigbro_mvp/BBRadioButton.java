package com.ldv.client.bigbro_mvp;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.RadioButton;

import com.ldv.client.bigbro.BBControl;
import com.ldv.client.bigbro.BBDialogFunction;
import com.ldv.client.bigbro.BBItem;
import com.ldv.client.bigbro.BBTransferInfo;
import com.ldv.client.util.ArchetypePanel;

import com.ldv.shared.archetype.LdvArchetypeControl;
import com.ldv.shared.graph.BBMessage;

/**
 * A radio button widget controlled by 
 * 
 * @author Philippe Ameline
 * 
 */
public class BBRadioButton extends RadioButton 
{
	protected BBWidget _controler ;
	
	public BBRadioButton(String sGroupName, String sLabel) 
	{
		super(sGroupName, sLabel) ;
		
		_controler = new BBWidget() ;
		
		// WARNING: onClick is fired after the button change states (for example gets physically clicked)
		//
		addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				clicked() ;
			}
		});
	}
	
	public void createControlAndConnect(ArchetypePanel userInterface, BBItem bbItem, LdvArchetypeControl control)
	{
		_controler.setBBControl(new BBControl(bbItem, control)) ;
		_controler.setControl(this) ;
		_controler.setControlType(BBControl.WNDTYPE.isRadioBtn) ;
		_controler.setUserInterface(userInterface) ;
	}
	
	public void activateControl(BBControl.CTRL_ACTIVATION activation, BBMessage Message)
	{ 
		switch (activation)
		{
			case checked :
				setValue(true) ;
				_controler.setState(BBControl.CTRL_STATE.checked) ;
      	break ;
			case unchecked :
				setValue(false) ;
				_controler.setState(BBControl.CTRL_STATE.unchecked) ;
      	break ;
		}
	}
	
	/**
	 * When the control is clicked, we must ask the command layer (BBItem, etc) the state 
	 * to switch to/remain in
	 * 
	 **/
	protected void clicked() 
	{
		// If disconnected, don't react
		//
	  if (_controler.isDisconnected())
	    return ;
	  
	  int iNotify = 1 ;
	  
	  BBMessage msg = new BBMessage() ;
	  
	  // If already checked, ask if allowed to become unchecked
	  //
	  // if (getValue()) getValue is of no help since it
	  if (BBControl.CTRL_STATE.checked == _controler.getState())
	  {
	  	_controler.ctrlNotification(BBControl.CTRL_ACTIVATION.checked, BBControl.CTRL_ACTIVATION.unchecked, msg, -1) ;
	  }
	  //
	  // If unchecked, ask to get checked
	  //
	  else if (BBControl.CTRL_STATE.unchecked == _controler.getState())
	  {
	    iNotify = _controler.executeFunction(BBDialogFunction.SITUATION_TYPE.Execute) ;
	    _controler.ctrlNotification(BBControl.CTRL_ACTIVATION.unchecked, BBControl.CTRL_ACTIVATION.checked, msg, -1) ;
	  }
	  
	  // If a function is linked to this control, let's execute it
		//
		if (0 != iNotify)
			_controler.executeFunction(BBDialogFunction.SITUATION_TYPE.PostExec) ;
	}

	/**
	 * Function called by the command layer (BBItem, etc) when it want to 
	 * set/get information to/from this control 
	 * 
	 * @param direction whether tdSetData or tdGetData to set or get information
	 * @param transfer object that contain information to be collected or displayed 
	 * 
	 * @return <code>1</code> if all went well, <code>0</code> if not
	 * 
	 **/
	public int Transferer(BBTransferInfo.TRANSFER_DIRECTION direction, BBTransferInfo transfer)
	{
		if (BBTransferInfo.TRANSFER_DIRECTION.tdSetData == direction)
		{
	  	switch (transfer.getActivationStatus())
	    {
	    	case inactiveCtrl :
	    		setEnabled(true) ;
	    		setValue(false) ;
	    		_controler.setState(BBControl.CTRL_STATE.unchecked) ; 
	    		break ;
	      case activeCtrl   :
	      	setEnabled(true) ;
	      	setValue(true) ;
	      	_controler.setState(BBControl.CTRL_STATE.checked) ;
	      	break ;
	      default           : 
	      	setEnabled(false) ;
	      	_controler.setState(BBControl.CTRL_STATE.grayed) ;
			}
		}
		else if (BBTransferInfo.TRANSFER_DIRECTION.tdGetData == direction)
		{
			if (null == transfer)
				return 0 ;
			
			if (false == isEnabled())
				transfer.setActivationStatus(BBTransferInfo.CTRL_ACTIVITY.disabledCtrl) ;
			else
			{
				if (BBControl.CTRL_STATE.checked == _controler.getState())
					transfer.setActivationStatus(BBTransferInfo.CTRL_ACTIVITY.activeCtrl) ;
				else
					transfer.setActivationStatus(BBTransferInfo.CTRL_ACTIVITY.inactiveCtrl) ;
			}

	    transfer.UpdateMessagePostTransfert() ;
		}
		
		return 1 ;
	}
	
	public BBControl getControl() {
		return _controler.getBBControl() ;
	}
	public void setControl(BBControl control) {
		_controler.setBBControl(control) ;
	}
	
	public BBControl.CTRL_STATE getState() {
		return _controler.getState() ;
	}
	public void setState(BBControl.CTRL_STATE eState) {
		_controler.setState(eState) ;
	}
}
