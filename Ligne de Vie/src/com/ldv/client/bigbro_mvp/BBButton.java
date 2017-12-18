package com.ldv.client.bigbro_mvp;

import com.google.gwt.user.client.ui.Button;

import com.ldv.shared.archetype.LdvArchetypeControl;
import com.ldv.shared.graph.BBMessage;
import com.ldv.client.bigbro.BBControl;
import com.ldv.client.bigbro.BBItem;
import com.ldv.client.bigbro.BBTransferInfo;
import com.ldv.client.util.ArchetypePanel;

/**
 * A button widget controlled by BBItem
 * 
 * @author Philippe Ameline
 * 
 */
public class BBButton extends Button
{
	protected BBWidget _controler ;
	
	protected String   _sCaption ; 
	protected String   _sType ;
	
	public BBButton(String sCaption)
	{
		super() ;
		
		_controler = new BBWidget() ;
		
		_sType     = "" ;
		_sCaption  = sCaption ;
		
		super.setText(_sCaption) ;
	}
	
	public void createControlAndConnect(ArchetypePanel userInterface, BBItem bbItem, LdvArchetypeControl control)
	{
		_controler.setBBControl(new BBControl(bbItem, control)) ;
		_controler.setControl(this) ;
		_controler.setControlType(BBControl.WNDTYPE.isBtn) ;
		_controler.setUserInterface(userInterface) ;
	}

	public void activateControl(BBControl.CTRL_ACTIVATION activation, BBMessage Message)
	{ 
	}
	
	/**
	 * Function called by the command layer (BBItem, etc) when it want to 
	 * set/get information to/from this control
	 * 
	 * It does nothing here since a GroupBox doesn't contain any information 
	 * 
	 * @param direction whether tdSetData or tdGetData to set or get information
	 * @param transfer object that contain information to be collected or displayed 
	 * 
	 * @return <code>1</code> if all went well, <code>0</code> if not
	 * 
	 **/
	public int Transferer(BBTransferInfo.TRANSFER_DIRECTION direction, BBTransferInfo transfer)
	{
		return 1 ;
	}
	
	public BBControl getControl() {
		return _controler.getBBControl() ;
	}
	public void setControl(BBControl control) {
		_controler.setBBControl(control) ;
	}
	
	public String getCaption() {
		return _sCaption ;
	}
	public void setCaption(String sCaption) {
		_sCaption = sCaption ;
		setText(_sCaption) ;
	}
}
