package com.ldv.client.bigbro_mvp;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.user.client.ui.TextBox;

import com.ldv.client.bigbro.BBControl;
import com.ldv.client.bigbro.BBDialogFunction;
import com.ldv.client.bigbro.BBItem;
import com.ldv.client.bigbro.BBTransferInfo;
import com.ldv.client.loc.LdvConstants;
import com.ldv.client.util.ArchetypePanel;

import com.ldv.shared.archetype.LdvArchetypeControl;
import com.ldv.shared.graph.BBMessage;
import com.ldv.shared.util.MiscellanousFcts;

/**
 * A text box widget controlled by BBItem
 * 
 * @author Philippe Ameline
 * 
 */
public class BBTextBox extends TextBox
{
	private final LdvConstants constants = GWT.create(LdvConstants.class) ;
	
	// Values appearing in _sEditType
	//
	public static char nbMARK       = 'N' ;  // Number
	public static char charMARK     = 'C' ;  // Chars
	public static char codeMARK     = 'O' ;  // Obfuscated chars
	public static char dateMARK     = 'D' ;  // Date
	public static char dateTimeMARK = 'T' ;  // Date and time
	public static char timeMARK     = 'H' ;  // Time
	
	// Values appearing in _sType
	//
	public static char LibreMARK2    = 'L' ;   // label
	public static char codMARK2      = 'C' ;   // code
	
	protected BBWidget _controler ;
	
	protected String   _sEditType ;
	
	protected String   _sRawContent ; 
	protected String   _sTransferableContent ; 
	protected String   _sType ;
	
	public BBTextBox(String sType) 
	{
		super() ;
		
		_sEditType = sType ;
		
		_controler = new BBWidget() ;
		
		_sType       = "" ;
		_sRawContent = "" ;
		
		// Called when TextBox losts focus
		//
		addBlurHandler(new BlurHandler() {
	    @Override
	    public void onBlur(BlurEvent event) {
	    	lostFocus() ;
	    }
		});
	}
	
	public void createControlAndConnect(ArchetypePanel userInterface, BBItem bbItem, LdvArchetypeControl control)
	{
		_controler.setBBControl(new BBControl(bbItem, control)) ;
		_controler.setControl(this) ;
		_controler.setControlType(BBControl.WNDTYPE.isEdit) ;
		_controler.setUserInterface(userInterface) ;
	}

	protected void setAndDisplayContent()
	{
		buildRawContent() ;
		setText(_sRawContent) ;
	}
	
	protected void lostFocus()
	{
		// Get entered text
		//
		String sContent = getText() ;
		
		// Update internal information
		//
		updateObject(sContent) ;
	}
	
	// Initialize the internal information from a raw value (for example what was really entered) 
	//
	// @param sContent Information to initialize internal information from
	//
	public void updateObject(final String sContent)
	{
		if (null == sContent)
			return ;
		
		_sRawContent = sContent ;
		buildTransferableContent() ;
	}
	
	/**
	 * Takes native information (as stored in system) and build user editable information<br>   
	 * aka build _sRawContent from _sTransferableContent
	 */
	protected void buildRawContent()
	{
		_sRawContent = _sTransferableContent ;
		
		if ("".equals(_sEditType))
			return ;

		// Text value, nothing to do 
		//
		if ((_sEditType.charAt(0) == charMARK) || (_sEditType.charAt(0) == codeMARK))
			return ;
		
		// Numeric value 
		//
		if (_sEditType.charAt(0) == nbMARK)
		{
			_sRawContent = replaceFirstDotByLocale(_sTransferableContent) ;
      return ;
		}
		
		// Date value, go from the native yyyyMMdd format to something like dd/MM/yyyy 
		//
		if (_sEditType.charAt(0) == dateMARK)
		{
			String sFormatString = constants.systemDateFormat() ;
			_sRawContent = MiscellanousFcts.dateFromNativeToFormated(_sRawContent, sFormatString) ;
			return ;
		}
	}
	
	/**
	 * Takes user editable information and build native information (to be stored in system)<br>   
	 * aka build _sTransferableContent from _sRawContent 
	 */
	protected void buildTransferableContent()
	{
		_sTransferableContent = _sRawContent.trim() ;
		
		if ("".equals(_sEditType))
			return ;

		// Text value, nothing to do 
		//
		if ((_sEditType.charAt(0) == charMARK) || (_sEditType.charAt(0) == codeMARK))
			return ;
		
		// Numeric value 
		//
		if (_sEditType.charAt(0) == nbMARK)
		{
			_sTransferableContent = getTransferableNumber(_sTransferableContent) ;
      return ;
		}
		
		// Date value, go from the formated string (something like dd/MM/yyyy) to the native format (yyyyMMdd) 
		//
		if (_sEditType.charAt(0) == dateMARK)
		{
			String sFormatString = constants.systemDateFormat() ;
			_sTransferableContent = MiscellanousFcts.dateFromFormatedToNative(_sTransferableContent, sFormatString) ;
			return ;
		}
	}
	
	public void activateControl(BBControl.CTRL_ACTIVATION activation, BBMessage Message)
	{ 
		switch (activation)
		{
			case checked :
				
				// If content doesn't change there is nothing to do
				//
				if (_sTransferableContent.equals(Message.getFreeText()))
					return ;
				
				_sTransferableContent = Message.getFreeText() ;
				_sType                = Message.getType() ;
				
				setAndDisplayContent() ;
				
				_controler.setState(BBControl.CTRL_STATE.checked) ;
				
	      // If a function is linked to this control, let's execute it
	      //
				_controler.executeFunction(BBDialogFunction.SITUATION_TYPE.PostExec) ;
				
      	break ;
      	
			case unchecked :
				
				_sTransferableContent = "" ;
				setAndDisplayContent() ;
				
				_controler.setState(BBControl.CTRL_STATE.unchecked) ;
      	
      	break ;
      	
      default:
      	
      	_controler.executeFunction(BBDialogFunction.SITUATION_TYPE.PostExec) ;
		}
	}
	
	/**
	 * Function called by the command layer (BBItem, etc) when it want to 
	 * set/get information to/from this control 
	 * 
	 * @param direction whether tdSetData or tdGetData to set or get information
	 * @param transfer object that contain information to be collected or displayed 
	 * 
	 * @return <code>1</code> if all went well, <code>0</code> if not
	 */
	public int Transferer(BBTransferInfo.TRANSFER_DIRECTION direction, BBTransferInfo transfer)
	{
		if (BBTransferInfo.TRANSFER_DIRECTION.tdSetData == direction)
		{
	  	switch (transfer.getActivationStatus())
	    {
	    	case inactiveCtrl :
	    		_sTransferableContent = "" ;
					setAndDisplayContent() ;
					_controler.setState(BBControl.CTRL_STATE.unchecked) ; 
	    		break ;
	    		
	      case activeCtrl   :
	      	BBMessage message = transfer.getTransfertMessage() ;
	      	if (null != message)
	      	{
	      		_sTransferableContent = message.getFreeText() ;
	      		_sType                = message.getType() ;
	      	}
	      	setAndDisplayContent() ;
	      	_controler.setState(BBControl.CTRL_STATE.checked) ;
	      	break ;
	      	
	      default           : 
	      	_sTransferableContent = "" ;
					setAndDisplayContent() ;
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
				_sRawContent = getText() ;
				buildTransferableContent() ;
			
				if ("".equals(_sTransferableContent))
					transfer.setActivationStatus(BBTransferInfo.CTRL_ACTIVITY.inactiveCtrl) ;
				else
					transfer.setActivationStatus(BBTransferInfo.CTRL_ACTIVITY.activeCtrl) ;
				
				BBMessage message = transfer.getTransfertMessage() ;
      	if (null != message)
      	{
      		message.setFreeText(_sTransferableContent) ;
      		message.setType(_sType) ;
      		
      		transfer.UpdateMessagePostTransfert() ;
      	}
			}
		}

		return 1 ;
	}

	/**
	 * Replaces the dot as a decimal separator by local separator<br>For example 23.6 becomes 23,6 in French   
	 * 
	 * @param sNum Numerical value to be processed
	 * 
	 * @return The modified String
	 */
	protected String replaceFirstDotByLocale(final String sNum) 
	{
		if ("".equals(sNum))
			return "" ;
		
		String sLocalForDot = constants.systemDecimalSeparatorFormat() ;
		if (".".equals(sLocalForDot))
			return sNum ;
		
		String sReturn = sNum ;
		
    // Replace first '.' by the local decimal separator
    //
    int iDot = sNum.indexOf(".") ;
    if (-1 != iDot)
    {
    	// .357 -> 0,357
    	if (0 == iDot)
    		sReturn = "0" + constants.systemDecimalSeparatorFormat() + sNum.substring(1) ;
    	// 357. -> 357
    	else if (sNum.length() - 1 == iDot)
    		sReturn = sNum.substring(0, sNum.length() - 1) ;
    	// 357.23 -> 357,23 
    	else
    		sReturn = sNum.substring(0, iDot) + constants.systemDecimalSeparatorFormat() + sNum.substring(iDot + 1, _sRawContent.length()) ;
    }
    
    return sReturn ;
	}
	
	/**
	 * From a user edited numerical value, get an information that is ready to store in system    
	 * 
	 * @param sNum Numerical user edited value to be processed
	 * 
	 * @return The modified String in system format
	 */
	protected String getTransferableNumber(final String sNum) 
	{
		if (null == sNum)
			return "" ;
		
		String sReturn = sNum.trim() ;
		
		if ("".equals(sReturn))
			return "" ;
		
		boolean bIsNegative = false ;
		
		// If there is a minus sign, it must be the first char, and it must be single
		//
		int iMinus = sReturn.indexOf("-") ;
		if (iMinus > 0)
			return "" ;
		
		// If first char is a minus, remove it now and put it back later
		//
		if (0 == iMinus)
		{
			bIsNegative = true ;
			sReturn = sReturn.substring(1) ;
			
			iMinus = sReturn.indexOf("-") ;
			if (iMinus > 0)
				return "" ;
		}
		
		// Get local decimal separator
		//
		String sLocalForDot = constants.systemDecimalSeparatorFormat() ;
		if ("".equals(sLocalForDot))
		{
			if (bIsNegative)
				return "-" + sReturn ;
			return sReturn ;
		}
		
		// Replace local decimal separator by a dot
		//
		int iDot = sReturn.indexOf(sLocalForDot) ;
		if (-1 != iDot)
		{
			int iSeparLen = sLocalForDot.length() ;  // Ok, should always be 1, but who knows?
			
			// ",25" in FR -> "0.25"
			if (0 == iDot)
				sReturn = "0." + sReturn.substring(iSeparLen) ;
			// "25," in FR -> "25"
			else if (sReturn.length() - iSeparLen == iDot)  
				sReturn = sReturn.substring(0, iDot) ;
			// Usual case: "25,3" in FR -> "25.3"
			else
				sReturn = sReturn.substring(0, iDot) + "." + sReturn.substring(iDot + iSeparLen, sReturn.length()) ;
		}
		
		// Find first char that is not 0 (in order to treat "00005.2" as "5.2")
		// We do it after decimal separator switching in order to assume it is now simply '.'
		//
		int iFirstNonZero = MiscellanousFcts.find_first_not_of(sReturn, '0') ;
		
		// Only zeros: return "0" (and not "")
		//
		if (-1 == iFirstNonZero)
			return "0" ;
		
		// First case, the first non zero is the decimal separator, keep at least one heading '0'
		//
		if (sReturn.charAt(iFirstNonZero) == '.')
		{
			if (iFirstNonZero > 1)
				sReturn = sReturn.substring(iFirstNonZero - 1) ;
		}
		// Second case, of the kind "0000053.7", remove all heading zeros
		//
		else
			sReturn = sReturn.substring(iFirstNonZero) ;
		
		// TODO Check sReturn against a RegEx
		//
		// if (false == sReturn.matches("[0-9].[0-9]"))
		//	return "" ;
		
		if (bIsNegative)
			return "-" + sReturn ;
    return sReturn ;
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
