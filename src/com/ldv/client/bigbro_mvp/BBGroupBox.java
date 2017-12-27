package com.ldv.client.bigbro_mvp;

import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Label;

import com.ldv.client.bigbro.BBControl;
import com.ldv.client.bigbro.BBItem;
import com.ldv.client.bigbro.BBTransferInfo;
import com.ldv.client.util.ArchetypePanel;

import com.ldv.shared.archetype.LdvArchetypeControl;
import com.ldv.shared.graph.BBMessage;

/**
 * A group box widget controlled by BBItem
 * 
 * @author Philippe Ameline
 * 
 */
public class BBGroupBox extends SimplePanel
{
	protected BBWidget _controler ;
	
	protected String   _sCaption ; 
	protected String   _sType ;
	
	protected Label    _captionLabel ;
	
	public BBGroupBox(String sCaption)
	{
		super() ;
		
		_controler = new BBWidget() ;
		
		_sType     = "" ;
		_sCaption  = sCaption ;
		
		addStyleName("BBGroupBox") ;
		
		_captionLabel = null ;
		if (false == "".equals(_sCaption))
		{
			_captionLabel = new Label(_sCaption) ;
			_captionLabel.addStyleName("BBGroupBoxCaption") ;
			add(_captionLabel) ;
		}
		
		super.setTitle(_sCaption) ;
	}
	
	/** 
	 * Connect the control to the command chain via its BBControl and to the interface 
	 * 
	 * @param userInterface The panel the control belongs to
	 * @param bbItem        The BBItem that commands this control
	 * @param control       Description of this control from the archetype
	 * 
	 */
	public void createControlAndConnect(ArchetypePanel userInterface, BBItem bbItem, LdvArchetypeControl control)
	{
		_controler.setBBControl(new BBControl(bbItem, control)) ;
		_controler.setControl(this) ;
		_controler.setControlType(BBControl.WNDTYPE.isGroup) ;
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
	}
}
