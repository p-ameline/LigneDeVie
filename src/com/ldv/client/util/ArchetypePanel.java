package com.ldv.client.util;

import java.util.Iterator;
import java.util.Vector;

import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Widget;

import com.ldv.shared.archetype.LdvArchetypeControl;
import com.ldv.shared.graph.LdvGraphConfig;
import com.ldv.shared.graph.LdvModelNode;

import com.ldv.client.bigbro.BBItem;
import com.ldv.client.bigbro_mvp.BBButton;
import com.ldv.client.bigbro_mvp.BBGroupBox;
import com.ldv.client.bigbro_mvp.BBRadioButton;
import com.ldv.client.bigbro_mvp.BBStatic;
import com.ldv.client.bigbro_mvp.BBTextBox;

/**
 * An AbsolutePanel panel that contains all Archetype commanded controls 
 * 
 * @author Philippe Ameline
 * 
 */
public class ArchetypePanel extends AbsolutePanel
{	
	private Vector<Widget> _aControls = new Vector<Widget>() ;
	
	private String         _sUnit ;
	private int            _iBaseUnitY ;
	private int            _iBaseUnitX ;
	
	private String         _sCurrentGroupName ;
	
	public class actionButton 
	{
		protected BBButton _button ;
		protected String   _sID ;
		
		public actionButton(BBButton button, String sID)
		{
			_button = button ;
			_sID    = sID ;
		}
		
		public BBButton getButton() {
			return _button ;
		}
		
		public String getId() {
			return _sID ;
		}
	}
	
	/** 
	 * Information to take into account that some controls may have borders and paddings that must lead to setting smaller dimensions 
	 * 
	 */
	public class Offsets
	{
		private int _iOffsetTop ; 
		private int _iOffsetBottom ;
		private int _iOffsetLeft ;
		private int _iOffsetRight ;
		
		public Offsets(final int iOffsetTop, final int iOffsetBottom, final int iOffsetLeft, final int iOffsetRight)
		{
			_iOffsetTop    = iOffsetTop ;
			_iOffsetBottom = iOffsetBottom ;
			_iOffsetLeft   = iOffsetLeft ;
			_iOffsetRight  = iOffsetRight ;
		}
		
		public int getOffsetTop() {
			return _iOffsetTop ;
		}
		
		public int getOffsetBottom() {
			return _iOffsetBottom ;
		}
		
		public int getOffsetLeft() {
			return _iOffsetLeft ;
		}
		
		public int getOffsetRight() {
			return _iOffsetRight ;
		}
	}
	
	private Vector<actionButton> _aActionButtons = new Vector<actionButton>() ;
	
	public ArchetypePanel() 
	{
		super() ;
		
		_sUnit             = "px" ;
		_sCurrentGroupName = "" ;
		
		addStyleName("archetypePannel") ;
	}
	
	/** 
	 * Clear all controls 
	 * 
	 */
	public void reset()
	{
		_aActionButtons.clear() ;
		_aControls.clear() ;
	}
	
	/** 
	 * Add a control widget in the panel 
	 * 
	 * @param control Description of the control from the archetype
	 * @param bbItem  Command object for this control
	 * 
	 */
	public void addWidget(LdvArchetypeControl control, BBItem bbItem)
	{
		if (null == control)
			return ;
		
		String sType     = control.getType() ;
		String sIdentity = control.getDataIdentity() ;
		
		if ("SYSTREEVIEW32".equalsIgnoreCase(sType)) {
			return ;
		}
		if ("SYSLISTVIEW32".equalsIgnoreCase(sType)) {
			return ;
		}
		if ("VALLISTVIEW32".equalsIgnoreCase(sType)) {
			return ;
		}
		//
		// Static control
		//
		if ("STATIC".equalsIgnoreCase(sType) || "BORSTATIC".equalsIgnoreCase(sType)) {
			addStaticWidget(control, bbItem) ;
			return ;
		}
		//
		// Radio button control
		//
		if ("BORRADIO".equalsIgnoreCase(sType)) {
			addRadioButtonWidget(control, bbItem) ;
			return ;
		}
		//
		// Edit control
		//
		if ("EDIT".equalsIgnoreCase(sType)) 
		{
			// Get the format as last element of identity path
			//
			String sIdentityFormat = "" ;
			int    iIdentityLen    = sIdentity.length() ; 
			
			int iLastSepar = sIdentity.lastIndexOf(LdvGraphConfig.pathSeparationMARK) ;
			if (-1 == iLastSepar)
				sIdentityFormat = sIdentity ;
			else if (iLastSepar < iIdentityLen - 1)
				sIdentityFormat = sIdentity.substring(iLastSepar + 1, iIdentityLen) ;
			
			if (LdvModelNode.startsWithPound(sIdentityFormat))
			{
				String sFollowPound = LdvModelNode.followsPound(sIdentityFormat) ;
				
				// Edit connected to the Lexicon
				//
				if ("C;".equalsIgnoreCase(sFollowPound))
				{
					addRadioButtonWidget(control, bbItem) ;
					return ;
				}
				//
				// Edit exclusively connected to the Lexicon
				//
				if ("CX".equalsIgnoreCase(sFollowPound))
				{
					addRadioButtonWidget(control, bbItem) ;
					return ;
				}
				//
				// Edit not connected to the Lexicon and that stores it information into Complement
				//
				if ("CC".equalsIgnoreCase(sFollowPound))
				{
					addTextBoxWidget(control, bbItem, "C") ;
					return ;
				}
				//
				// Edit not connected to the Lexicon and that stores it information into Free text
				//
				if ("CL".equalsIgnoreCase(sFollowPound))
				{
					addTextBoxWidget(control, bbItem, "L") ;
					return ;
				}
				
				//
				// Other modes
				//
				if (false == "".equals(sFollowPound))
				{
					char cEditType = sFollowPound.charAt(0) ;
						
					if ((BBTextBox.charMARK == cEditType) || 
							(BBTextBox.codeMARK == cEditType) ||
							(BBTextBox.nbMARK   == cEditType))
						addTextBoxWidget(control, bbItem, sFollowPound) ;
				}
			}
			//
			// Other modes
			//
			else
			{
				String sEditType = "" ;
				
				int iPound = sIdentity.indexOf(String.valueOf(LdvGraphConfig.POUND_CHAR)) ;
				if ((iPound >= 0) && (iPound < sIdentity.length() - 1))
					sEditType = sIdentity.substring(iPound + 1, iPound + 2) ;
				
				if (false == "".equals(sEditType))
				{
					char cEditType = sEditType.charAt(0) ;
					
					if ((BBTextBox.charMARK == cEditType) || 
						  (BBTextBox.codeMARK == cEditType) ||
						  (BBTextBox.nbMARK   == cEditType))
						addTextBoxWidget(control, bbItem, sEditType) ;
				}
			}
			return ;
		}
		//
		// GroupBox control
		//
		if ("GROUP".equalsIgnoreCase(sType) || "BORSHADE".equalsIgnoreCase(sType)) {
			addGroupBoxWidget(control, bbItem) ;
			return ;
		}
		//
		// Button control
		//
		if ("BUTTON".equalsIgnoreCase(sType) || "BORBUTTON".equalsIgnoreCase(sType)) {
			addButtonWidget(control, bbItem) ;
			return ;
		}
	}

	public void addStaticWidget(LdvArchetypeControl control, BBItem bbItem)
	{
		if (null == control)
			return ;
		
		BBStatic staticText = new BBStatic(control.getCaption()) ;
		staticText.createControlAndConnect(this, bbItem, control) ;
		
		addWidget(staticText, control, null) ;
		
		_aControls.addElement(staticText) ;
	}
	
	public void addRadioButtonWidget(LdvArchetypeControl control, BBItem bbItem)
	{
		if (null == control)
			return ;
		
		BBRadioButton button = new BBRadioButton(_sCurrentGroupName, control.getCaption()) ;
		button.createControlAndConnect(this, bbItem, control) ;

		addWidget(button, control, null) ;
		
		_aControls.addElement(button) ;
	}
	
	public void addTextBoxWidget(LdvArchetypeControl control, BBItem bbItem, String sType)
	{
		if (null == control)
			return ;
		
		BBTextBox editControl = new BBTextBox(sType) ;
		editControl.createControlAndConnect(this, bbItem, control) ;
		
		// For texts, border is 1 on each sides, padding is 4 on left & right and 5 on top & bottom
		//
		Offsets offsets = new Offsets(6, 6, 5, 5) ;
		
		addWidget(editControl, control, offsets) ;
		
		_aControls.addElement(editControl) ;
	}
	
	/** 
	 * Add a group box in the panel 
	 * 
	 * @param control Description of the control from the archetype
	 * @param bbItem  Command object for this control
	 * 
	 */
	public void addGroupBoxWidget(LdvArchetypeControl control, BBItem bbItem)
	{
		if (null == control)
			return ;
		
		BBGroupBox groupBox = new BBGroupBox(control.getCaption()) ;
		groupBox.createControlAndConnect(this, bbItem, control) ;
		
		// Offsets offsets = new Offsets(2, 2, 2, 2) ;
		
		addWidget(groupBox, control, null) ;
		
		_aControls.addElement(groupBox) ;
	}
	
	public void addButtonWidget(LdvArchetypeControl control, BBItem bbItem)
	{
		if (null == control)
			return ;
		
		// Create and connect the button control
		//
		BBButton button = new BBButton(control.getCaption()) ;
		button.createControlAndConnect(this, bbItem, control) ;
		
		// Offsets offsets = new Offsets(8, 8, 6, 6) ;
		
		addWidget(button, control, null) ;
		
		_aControls.addElement(button) ;
		
		// If the button is an action button, put it in the proper list 
		//
		if (("IDOK".equals(control.getCtrlId())) || ("IDCANCEL".equals(control.getCtrlId()))) 
			_aActionButtons.addElement(new actionButton(button, control.getCtrlId())) ;
	}
	
	/**
	 * According to control's information, sets widget's height, width and visibility, then
	 * add it to the panel at the specified location  
	 *
	 * @param widget  interface element to add
	 * @param control LdvArchetypeControl to get positioning information from
	 * 
	 **/
	protected void addWidget(Widget widget, LdvArchetypeControl control, final Offsets offsets)
	{
		if ((null == widget) || (null == control))
			return ;
		
		int iOffsetTop    = 0 ;
		int iOffsetBottom = 0 ;
		int iOffsetLeft   = 0 ;
		int iOffsetRight  = 0 ;
		
		if (null != offsets)
		{
			iOffsetTop    = offsets.getOffsetTop() ; 
			iOffsetBottom = offsets.getOffsetBottom() ; 
			iOffsetLeft   = offsets.getOffsetLeft() ; 
			iOffsetRight  = offsets.getOffsetRight() ; 
		}
		
		widget.setHeight(getControlHeight(control, iOffsetTop, iOffsetBottom) + _sUnit) ;
		widget.setWidth(getControlWidth(control, iOffsetLeft, iOffsetRight) + _sUnit) ;
		
		if (control.getTab() > 1)
			widget.setVisible(false) ;
		
		add(widget, getLocalX(control.getX()) - iOffsetLeft, getLocalY(control.getY()) /* - iOffsetTop */) ;
	}
	
	/**
	 * Get control's height as a String  
	 * 
	 * @param control LdvArchetypeControl to get height of
	 * @param iOffsetTop
	 * @param iOffsetBottom
	 * @return <code>""</code> if it went wrong, a String if not
	 * 
	 **/
	private String getControlHeight(LdvArchetypeControl control, int iOffsetTop, int iOffsetBottom) 
	{
		if (null == control)
			return "" ;
		
		// When setting height, what we set is the internal area, without margin, border and padding
		// -> we have to remove offsets if visible control is to have desired height
		//
		return Integer.toString(getLocalY(control.getCoordinates().getH()) - iOffsetTop - iOffsetBottom) ;
	}
	
	/**
	 * Get control's width as a String  
	 * 
	 * @param control LdvArchetypeControl to get width of
	 * @return <code>""</code> if it went wrong, a String if not
	 * 
	 **/
	private String getControlWidth(LdvArchetypeControl control, int iOffsetLeft, int iOffsetRight) 
	{
		if (null == control)
			return "" ;
		
		// When setting width, what we set is the internal area, without margin, border and padding
		// -> we have to remove offsets if visible control is to have desired with
		//
		return Integer.toString(getLocalX(control.getCoordinates().getW()) - iOffsetLeft - iOffsetRight) ;
	}
	
	public int getLocalX(int iArchetypeX) {
		return iArchetypeX * _iBaseUnitX ;
	}
	
	public int getLocalY(int iArchetypeY) {
		return iArchetypeY * _iBaseUnitY ;
	}
	
	public BBButton getButtonWithId(String sID)
	{
		if ((null == sID) || _aActionButtons.isEmpty())
			return null ;
		
		for (Iterator<actionButton> bttnItr = _aActionButtons.iterator() ; bttnItr.hasNext() ; )
		{
			actionButton item = bttnItr.next() ;
  		if (item.getId().equals(sID))
  			return item.getButton() ;
		}
		
		return null ;
	}
	
	public String getUnit() {
		return _sUnit ;
	}
	public void setUnit(String sUnit) {        
		_sUnit = sUnit ;
	}
	
	/**
	 * Set the multiplier to switch from point to pixel in the Y direction (initialized by ArchetypeDialogView)  
	 * 
	 **/
	public void setBaseUnitY(int iBaseUnitY) {           
		_iBaseUnitY = iBaseUnitY;
	}
	
	/**
	 * Set the multiplier to switch from point to pixel in the X direction (initialized by ArchetypeDialogView)  
	 * 
	 **/
	public void setBaseUnitX(int iBaseUnitX) {
		_iBaseUnitX = iBaseUnitX ;
	}
	
	@Override
	public Widget asWidget() {
		return this ;
	}
}
