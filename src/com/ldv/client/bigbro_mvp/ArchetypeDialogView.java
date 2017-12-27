package com.ldv.client.bigbro_mvp;

import com.allen_sauer.gwt.log.client.Log;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import com.ldv.shared.archetype.LdvArchetypeControl;

import com.ldv.client.bigbro.BBItem;
import com.ldv.client.util.ArchetypePanel;

public class ArchetypeDialogView extends DialogBox implements ArchetypeDialogPresenter.Display  // was Composite
{	
	private ArchetypePanel _panel ;
	
	private int            _iBaseUnitY ;
	private int            _iBaseUnitX ;
	
	private int            _iHeight ;
	private int            _iWidth ;
	
	public ArchetypeDialogView()
	{
		// Call DialogBox(boolean autoHide, boolean modal)
		//
		super(false, true) ;
		setAnimationEnabled(true) ;
		
		Log.debug("ArchetypeDialogView constructor") ;
		
		_panel = new ArchetypePanel() ;
		
		setBaseUnits() ;
	}
	
	@Override
	public void showArchetype()
	{
		setWidget(_panel) ;
		show() ;
		setRealDimensions(_iWidth, _iHeight) ;
	}
	
	/** 
	 * Initialize the dialog before it is filled with controls
	 * 
	 */
	@Override
	public void initialize() 
	{
		clear() ;
		_panel.reset() ;
	}
	
	/** 
	 * Sets the text inside the caption
	 * 
	 * @param the archetype dialog's new text
	 * 
	 */
	@Override
	public void setCaption(String sCaption) {
		setText(sCaption) ;
	}
	
	/** 
	 * Sets the popup's position relative to the browser's client area. The popup's position may be set before calling show().
	 * 
	 * @param iLeft the left position, in pixels
	 * @param iTop  the top position, in pixels
	 * 
	 */
	@Override
	public void setPosition(int iLeft, int iTop) {
		setPopupPosition(iLeft, iTop) ;
	}
	
	/** 
	 * Sets the popup's client area dimensions. Since it is a dialog box, dimensions units are in <code>em</code> 
	 * 
	 * @param iWidth  the width, in em
	 * @param iHeight the height, in em
	 * 
	 */
	@Override
	public void setDimensions(int iWidth, int iHeight)
	{
		_iHeight = iHeight ;
		_iWidth  = iWidth ;
	}
	
	/** 
	 * Sets the popup's client area dimensions. Since it is a dialog box, dimensions units are in <code>em</code> 
	 * 
	 * @param iWidth  the width, in em
	 * @param iHeight the height, in em
	 * 
	 */
	public void setRealDimensions(int iWidth, int iHeight)
	{
		_panel.setWidth(Integer.toString(_panel.getLocalX(iWidth)) + _panel.getUnit()) ; 
		_panel.setHeight(Integer.toString(_panel.getLocalY(iHeight)) + _panel.getUnit()) ;
	}
	
	/** 
	 * Calculate base units from the size of a label that contains all letters, set them in the panel 
	 * 
	 */
	protected void setBaseUnits()
	{
		Label emToPixCalcLabel = new Label("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz") ;
		emToPixCalcLabel.setVisible(false) ;
		
		_panel.add(emToPixCalcLabel) ;
		
		setWidget(_panel) ;
		
		int iTextHeight = emToPixCalcLabel.getOffsetHeight() ;
		if (0 == iTextHeight)
			// iTextHeight = 15 ;
			iTextHeight = 18 ;
		
		int iTextWidth  = emToPixCalcLabel.getOffsetWidth() ;
		if (0 == iTextWidth)
			// iTextWidth = 6240 ;
			iTextWidth = 7000 ;
		
		// Dialog boxes' units are "em"
		//
		_iBaseUnitX = (iTextWidth / 26 + 1) / 2 / 48 ;
		_iBaseUnitY = iTextHeight / 7 ;
		
		_panel.setUnit("px") ;
		_panel.setBaseUnitX(_iBaseUnitX) ;
		_panel.setBaseUnitY(_iBaseUnitY) ;
	}
	
	/** 
	 * Add a control widget in the dialog box 
	 * 
	 * @param control Description of the control from the archetype
	 * @param bbItem  Command object for this control
	 * 
	 */
	@Override
	public void addWidget(LdvArchetypeControl control, BBItem bbItem) {
		_panel.addWidget(control, bbItem) ;
	}
	
	@Override
	public void setZIndex(int iZIndex) {
		getElement().getStyle().setZIndex(iZIndex) ;
	}

	@Override
	public Widget asWidget() {
		return this ;
	}
	
	@Override
	public void close() {
		hide() ;
	}
	
	@Override
	public HasClickHandlers getOk() {
		return getButtonWithId("IDOK") ;
	}
	
	@Override
	public HasClickHandlers getCancel() {
		return getButtonWithId("IDCANCEL") ;
	}
	
	protected HasClickHandlers getButtonWithId(String sID)
	{
		if (null == _panel)
			return null ;
		
		return _panel.getButtonWithId(sID) ;
	}
}
