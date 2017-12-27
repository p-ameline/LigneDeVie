package com.ldv.client.mvp;

import java.util.Date;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.HasMouseDownHandlers;
import com.google.gwt.event.dom.client.HasMouseMoveHandlers;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DatePicker;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.gwt.widgetideas.client.ResizableWidget;

import com.ldv.client.canvas.LdvProjectTab;
import com.ldv.client.loc.LdvConstants;
import com.ldv.client.ui.LdvResources;
import com.ldv.client.widgets.LexiqueTextBox;
import com.ldv.shared.util.MiscellanousFcts;

public class LdvProjectWindowView extends FocusPanel implements ResizableWidget, /*SourcesChangeEvents,*/ LdvProjectWindowPresenter.Display
{	
	private final LdvConstants constants = GWT.create(LdvConstants.class) ;
	
	/**
	 * The FlowPanel containing the TimeWidget and the composite
	 */
	private final AbsolutePanel _mainPanel ;
	private final AbsolutePanel _nowSeparator ;
	private final AbsolutePanel _baseLinePanel ;
	private final AbsolutePanel _workSpacePanel ;
	
	private final FocusPanel    _topFocusPanel ;
	private final FocusPanel    _bottomFocusPanel ;
	private final FocusPanel    _leftFocusPanel ;
	private final FocusPanel    _rightFocusPanel ;
	
	private final FocusPanel    _barFocusPanel ;

	private final LdvProjectTab _tabFocusPanel ;
	
	// private final LdvSliderBar  _zoomSliderBar ;
	
	private final PushButton    _zoomPlusButton ;
	private final PushButton    _zoomSmallPlusButton ;
	private final PushButton    _zoomNoneButton ;
	private final PushButton    _zoomSmallMinusButton ;
	private final PushButton    _zoomMinusButton ;
	
	private final PushButton    _newLineButton ;
	
	private       Image         _zoomPlusImage       = new Image() ;
	private       Image         _zoomSmallPlusImage  = new Image() ;
	private       Image         _zoomNoneImage       = new Image() ;
	private       Image         _zoomSmallMinusImage = new Image() ;
	private       Image         _zoomMinusImage      = new Image() ;
	
	private       Image         _newLineImage        = new Image() ;
	
	private        int          _iContextIconsBlockRadius ;
	private static int          _iTabHeight = 5 ;
	
	/**
	 * New concern dialog box
	 */
	
	private DialogBox       _NewConcernDialogBox ;
	private AbsolutePanel   _NewConcernAbsolutePanel ;
	private TextBox         _NewConcernDialogConcernLabel ;
	private LexiqueTextBox  _NewConcernDialogLexiqueBox ;
	private Label           _NewConcernLabelLabel ;
	
	private DateBox         _NewConcernStartingDateBox ;
	
	private RadioButton     _NewConcernNeverEnding ;
	private RadioButton     _NewConcernEndingDate ;
	private DateBox         _NewConcernEndingDateBox ;
	private TextBox         _NewConcernDurationValue ;
	private ListBox         _NewConcernDurationUnit ;
	private RadioButton     _NewConcernDurationTotal ;
	private RadioButton     _NewConcernDurationFromNow ;
	
	private Button          _NewConcernDialogBoxCancelButton ;
	private Button          _NewConcernDialogBoxOkButton ;
	
	public LdvProjectWindowView()
	{	
		super() ;
		
		_mainPanel        = new AbsolutePanel() ;
		
		_workSpacePanel   = new AbsolutePanel() ;
		_nowSeparator     = new AbsolutePanel() ; 
		_baseLinePanel    = new AbsolutePanel() ;
		_topFocusPanel    = new FocusPanel() ;
		_bottomFocusPanel = new FocusPanel() ;
		_leftFocusPanel   = new FocusPanel() ;
		_rightFocusPanel  = new FocusPanel() ;
		_barFocusPanel    = new FocusPanel() ;
		_tabFocusPanel    = new LdvProjectTab() ;
		
		_zoomPlusImage.setResource(LdvResources.INSTANCE.zoomPlus()) ;
		_zoomSmallPlusImage.setResource(LdvResources.INSTANCE.zoomSmallPlus()) ;
		_zoomNoneImage.setResource(LdvResources.INSTANCE.zoomNone()) ;
		_zoomSmallMinusImage.setResource(LdvResources.INSTANCE.zoomSmallMinus()) ;
		_zoomMinusImage.setResource(LdvResources.INSTANCE.zoomMinus()) ;
		_newLineImage.setResource(LdvResources.INSTANCE.newLine()) ;
		
		_zoomPlusButton       = new PushButton(_zoomPlusImage) ;
		_zoomPlusButton.addStyleName("button") ;
		_zoomSmallPlusButton  = new PushButton(_zoomSmallPlusImage) ;
		_zoomSmallPlusButton.addStyleName("button") ;
		_zoomNoneButton       = new PushButton(_zoomNoneImage) ;
		_zoomNoneButton.addStyleName("button") ;
		_zoomSmallMinusButton = new PushButton(_zoomSmallMinusImage) ;
		_zoomSmallMinusButton.addStyleName("button") ;
		_zoomMinusButton      = new PushButton(_zoomMinusImage) ;
		_zoomMinusButton.addStyleName("button") ;
		
		_newLineButton        = new PushButton(_newLineImage) ;
		_newLineButton.addStyleName("button") ;
		
/*
		_zoomSliderBar    = new LdvSliderBar(1, 5, new LdvSliderBar.LabelFormatter() { 
			public String formatLabel(LdvSliderBar slider, double value) { return "" + value ; }
		}) ;
*/

		_mainPanel.addStyleName("ldvProjectWorkspace") ;
		setPanelPositionAbsolute(_mainPanel) ;

		_nowSeparator.addStyleName("ldvProjectWorkspace-NowSeparatorPanel") ;
		setPanelPositionAbsolute(_nowSeparator) ;
		
		addPanel(_workSpacePanel,   "ldvProjectWorkspace-WorkSpacePanel") ;
		addPanel(_baseLinePanel,    "ldvProjectWorkspace-BaseLinePanel") ;
		
/* Currently, we don't include the resize panels 
		addPanel(_topFocusPanel,    "ldvProject-TopFocusPanel") ;
		addPanel(_bottomFocusPanel, "ldvProject-BottomFocusPanel") ;
		addPanel(_leftFocusPanel,   "ldvProject-LeftFocusPanel") ;
		addPanel(_rightFocusPanel,  "ldvProject-RightFocusPanel") ;
*/	
		
		addPanel(_barFocusPanel,    "ldvProject-BarFocusPanel") ;
		addPanel(_tabFocusPanel,    "ldvProject-TabFocusPanel") ;

		_tabFocusPanel.setHeight(_iTabHeight + "px") ;
		
		this.add(_mainPanel) ;
		
		_iContextIconsBlockRadius = 0 ;
		
		initDialogBoxComponents() ;
	}	
	
	/** 
	 * Initialize Concerns creation/editing dialog box
	 */
	private void initDialogBoxComponents()
	{
		_NewConcernDialogBox = new DialogBox() ;
		// _NewConcernDialogBox.setSize("60em", "20em") ;
		_NewConcernDialogBox.setWidth("50em") ;
		_NewConcernDialogBox.setPopupPosition(150, 200) ;
		_NewConcernDialogBox.setText(constants.newConcern()) ;
		_NewConcernDialogBox.setAnimationEnabled(true) ;
		
		_NewConcernAbsolutePanel = new AbsolutePanel() ;
		_NewConcernDialogBox.add(_NewConcernAbsolutePanel) ;
		
		final VerticalPanel dialogVPanel = new VerticalPanel() ;
		
		// Concern description
		//
		_NewConcernDialogConcernLabel = new TextBox() ;
		_NewConcernDialogConcernLabel.addStyleName("NewConcernDialogConcernLabel") ;
		
		_NewConcernDialogLexiqueBox   = new LexiqueTextBox("", _NewConcernAbsolutePanel) ;
		_NewConcernDialogLexiqueBox.addStyleName("NewConcernDialogLexiqueBox") ;
		
		FlexTable ConcernDescriptionTable = new FlexTable() ;
		ConcernDescriptionTable.setWidget(0, 0, new Label(constants.newConcernConcernLabel())) ;
		ConcernDescriptionTable.setWidget(0, 1, new HTML("&nbsp;")) ;
		ConcernDescriptionTable.setWidget(0, 2, _NewConcernDialogConcernLabel) ;
		ConcernDescriptionTable.setWidget(1, 0, new Label(constants.newConcernConcernType())) ;
		ConcernDescriptionTable.setWidget(1, 1, new HTML("&nbsp;")) ;
		ConcernDescriptionTable.setWidget(1, 2, _NewConcernDialogLexiqueBox) ;
    
		// Concern timing
		//
		final HorizontalPanel timingHPanel = new HorizontalPanel() ;
		
		// - starting date
		//
		FlexTable ConcernStartTable = new FlexTable() ;
		
		DateTimeFormat dateFormat = DateTimeFormat.getFormat(constants.systemDateFormat()) ;
		_NewConcernStartingDateBox = new DateBox() ;
		_NewConcernStartingDateBox.setFormat(new DateBox.DefaultFormat(dateFormat)) ;
		
		// - ending date
		//
		final VerticalPanel endingDateVPanel = new VerticalPanel() ;
		
		_NewConcernNeverEnding = new RadioButton("ending", constants.newConcernNeverEnding()) ;
		_NewConcernEndingDate  = new RadioButton("ending", constants.newConcernEndingDate()) ;
		
		_NewConcernEndingDateBox = new DateBox() ;
		_NewConcernEndingDateBox.setFormat(new DateBox.DefaultFormat(dateFormat)) ;
		
		_NewConcernEndingDateBox.addValueChangeHandler(new ValueChangeHandler<Date>() {
      	@Override
      	public void onValueChange(ValueChangeEvent<Date> event) 
      	{
      		Date date = _NewConcernEndingDateBox.getValue() ;
      		if (null != date)
      		{
      			_NewConcernNeverEnding.setValue(false) ;
      			_NewConcernEndingDate.setValue(true) ;
      		}
      	}
		});
		
		FlexTable ConcernEndTable = new FlexTable() ;
		
		ConcernEndTable.setWidget(0, 0, _NewConcernNeverEnding) ;
		ConcernEndTable.setWidget(0, 1, new HTML("&nbsp;")) ;
		ConcernEndTable.setWidget(0, 2, new HTML("&nbsp;")) ;
		ConcernEndTable.setWidget(1, 0, _NewConcernEndingDate) ;
		ConcernEndTable.setWidget(1, 1, new HTML("&nbsp;")) ;
		ConcernEndTable.setWidget(1, 2, _NewConcernEndingDateBox) ;
		
		timingHPanel.add(_NewConcernStartingDateBox) ;
		timingHPanel.add(new HTML("&nbsp;")) ;
		timingHPanel.add(ConcernEndTable) ;
		
		ConcernDescriptionTable.setWidget(2, 0, new Label(constants.newConcernStartDate())) ;
		ConcernDescriptionTable.setWidget(2, 1, new HTML("&nbsp;")) ;
		ConcernDescriptionTable.setWidget(2, 2, timingHPanel) ;
		
		// Buttons
		//
		_NewConcernDialogBoxOkButton = new Button(constants.generalOk()) ;
		_NewConcernDialogBoxOkButton.setSize("70px", "30px") ;
		_NewConcernDialogBoxOkButton.getElement().setId("okbutton") ;
			
		_NewConcernDialogBoxCancelButton = new Button(constants.generalCancel()) ;
		_NewConcernDialogBoxCancelButton.setSize("70px", "30px") ;
		_NewConcernDialogBoxCancelButton.getElement().setId("cancelbutton") ;
		
		FlexTable ButtonsTable = new FlexTable() ;
		ButtonsTable.setWidth("100%") ;
    ButtonsTable.setWidget(0, 0, _NewConcernDialogBoxOkButton) ;
    ButtonsTable.setWidget(0, 1, new Label(" ")) ;
    ButtonsTable.setWidget(0, 2, _NewConcernDialogBoxCancelButton) ;
		
    dialogVPanel.add(ConcernDescriptionTable) ;
    // dialogVPanel.add(timingHPanel) ;
    dialogVPanel.add(ButtonsTable) ;
    
    _NewConcernAbsolutePanel.add(dialogVPanel) ;
	}
	
	/** 
	 * Reset Concerns creation dialog box
	 */
	private void resetDialogBox()
	{
		_NewConcernDialogConcernLabel.setValue("") ;
		_NewConcernDialogLexiqueBox.setValue("") ;
		_NewConcernStartingDateBox.setValue(null) ;
		_NewConcernNeverEnding.setValue(true) ;
		_NewConcernEndingDate.setValue(false) ;
		_NewConcernEndingDateBox.setValue(null) ;
	}
	
	@Override
	public AbsolutePanel getMainPanel() {
		return _mainPanel ;
	}
	
	@Override
	public AbsolutePanel getBaseLinePanel() {
		return _baseLinePanel ;
	}

	@Override
	public AbsolutePanel getWorkSpacePanel() {
		return _workSpacePanel ;
	}
	
	@Override
	public FocusPanel getTopFocusPanel() {
		return _topFocusPanel ;
	}
	
	@Override
	public FocusPanel getBottomFocusPanel() {
		return _bottomFocusPanel ;
	}
	
	@Override
	public FocusPanel getLeftFocusPanel() {
		return _leftFocusPanel ;
	}
	
	@Override
	public FocusPanel getRightFocusPanel() {
		return _rightFocusPanel ;
	}
	
	@Override
	public FocusPanel getBarFocusPanel() {
		return _barFocusPanel ;
	}
	
	@Override
	public LdvProjectTab getTabFocusPanel() {
		return _tabFocusPanel ;
	}
	
	/**
	*  Set element ZIndex in CSS
	*  
	* @param iZorder value to be set as ZIndex   
	**/
	@Override
	public void setZorder(int iZorder) {
		_mainPanel.getElement().getStyle().setZIndex(iZorder) ;
	} 
	
	/**
	* Get element ZIndex from CSS
	*  
	* @return 
	**/
	public int getZorder()
	{
		String sZorder = _mainPanel.getElement().getStyle().getZIndex() ;
		try {
			return Integer.parseInt(sZorder) ;
		}
		catch (NumberFormatException cause) {
			return -1 ;
		}
	}
	
	@Override
	public void setHeight(int height)
	{
		String strHeight = Integer.toString(height) + "px" ;
		_mainPanel.setHeight(strHeight) ;
	}
	
	@Override
	public void setTop(int top) {
		_mainPanel.getElement().getStyle().setTop(top, Style.Unit.PX) ;
	}
	
	@Override
	public void setBottom(int bottom) {
		_mainPanel.getElement().getStyle().setBottom(bottom, Style.Unit.PX) ;
	}
	
	/**
	 * Set the position of the bottom of the panel, so that its tabFocusPanel is at the proper ZOrder location 
	 */
	@Override
	public void setBottomForZorder(int iZorder, int iMaxZorder, int iProjectsAreaBottom)
	{
		// int iTabHeight = _tabFocusPanel.getOffsetHeight() ;
		int iBottom    = iProjectsAreaBottom + (iMaxZorder - iZorder) * _iTabHeight ;
		
		// Log.info("LdvProjectWindowView::setBottomForZorder iZorder=" + iZorder + " iMaxZorder=" + iMaxZorder + " iProjectsAreaBottom=" + iProjectsAreaBottom + " iTabHeight=" + _iTabHeight + " iBottom=" + iBottom) ;
		
		setBottom(iBottom) ;
		
		_tabFocusPanel.setHeight(_iTabHeight + "px") ;
	}
	
	@Override
	public void setLeft(int left) {
		_mainPanel.getElement().getStyle().setLeft(left, Style.Unit.PX) ;
	}
	
	@Override
	public void setRight(int right) {
		_mainPanel.getElement().getStyle().setRight(right, Style.Unit.PX) ;
	}
	
	@Override
	public void setProjectTitle(String sTitle)
	{
		_barFocusPanel.clear() ;
		if (sTitle.equals(""))
			return ;
		
		HTML content = new HTML(MiscellanousFcts.upperCaseFirstChar(sTitle)) ;
		_barFocusPanel.add(content) ;
		
		_tabFocusPanel.setTooltipText(sTitle) ;
	}
	
	@Override
	public int getClickEventRelativeX(ClickEvent event) {
		if (null == event)
			return 0 ;
		return event.getRelativeX(this.getElement()) ;
	}
	
	@Override
	public int getClickEventRelativeY(ClickEvent event) {
		if (null == event)
			return 0 ;
		return event.getRelativeY(this.getElement()) ;
	}
	
	@Override
	public void showZoomSlider(MouseDownEvent event)
	{
		int iClickCenterX = event.getRelativeX(this.getElement()) ;
		int iClickCenterY = event.getRelativeY(this.getElement()) ;
		
		showZoomSlider(iClickCenterX, iClickCenterY) ;
	}
	
	@Override
	public void showZoomSlider(int iX, int iY)
	{
/*
		_workSpacePanel.add(_zoomSliderBar) ;
		DOM.setStyleAttribute(_zoomSliderBar.getElement(), "position", "absolute") ;
		DOM.setStyleAttribute(_zoomSliderBar.getElement(), "left", iX + "px") ;
		DOM.setStyleAttribute(_zoomSliderBar.getElement(), "top", iY + "px") ;
		_zoomSliderBar.setCurrentValue(2) ;
*/
		int iImageWidth     = _zoomNoneImage.getWidth() ;
		int iImageHeight    = _zoomNoneImage.getHeight() ;
		int iImageSeparator = 2 ;
		
		_iContextIconsBlockRadius = (int) (2 * iImageSeparator + 2.5 * iImageWidth) ;
		
		int iCenterX = Math.max(iX, _iContextIconsBlockRadius) ;
		int iCenterY = iY ;
		
		// Draw zoom control icons
		//
		int iStartX = iCenterX - _iContextIconsBlockRadius ;
		int iStartY = iCenterY - iImageHeight ;
		
		_workSpacePanel.add(_zoomMinusButton) ;
		DOM.setStyleAttribute(_zoomMinusButton.getElement(), "position", "absolute") ;
		DOM.setStyleAttribute(_zoomMinusButton.getElement(), "left", iStartX + "px") ;
		DOM.setStyleAttribute(_zoomMinusButton.getElement(), "top", iStartY + "px") ;
		
		iStartX += iImageSeparator + iImageWidth ;
		
		_workSpacePanel.add(_zoomSmallMinusButton) ;
		DOM.setStyleAttribute(_zoomSmallMinusButton.getElement(), "position", "absolute") ;
		DOM.setStyleAttribute(_zoomSmallMinusButton.getElement(), "left", iStartX + "px") ;
		DOM.setStyleAttribute(_zoomSmallMinusButton.getElement(), "top", iStartY + "px") ;
		
		iStartX += iImageSeparator + iImageWidth ;
		
		_workSpacePanel.add(_zoomNoneButton) ;
		DOM.setStyleAttribute(_zoomNoneButton.getElement(), "position", "absolute") ;
		DOM.setStyleAttribute(_zoomNoneButton.getElement(), "left", iStartX + "px") ;
		DOM.setStyleAttribute(_zoomNoneButton.getElement(), "top", iStartY + "px") ;
		
		iStartX += iImageSeparator + iImageWidth ;
		
		_workSpacePanel.add(_zoomSmallPlusButton) ;
		DOM.setStyleAttribute(_zoomSmallPlusButton.getElement(), "position", "absolute") ;
		DOM.setStyleAttribute(_zoomSmallPlusButton.getElement(), "left", iStartX + "px") ;
		DOM.setStyleAttribute(_zoomSmallPlusButton.getElement(), "top", iStartY + "px") ;
		
		iStartX += iImageSeparator + iImageWidth ;
		
		_workSpacePanel.add(_zoomPlusButton) ;
		DOM.setStyleAttribute(_zoomPlusButton.getElement(), "position", "absolute") ;
		DOM.setStyleAttribute(_zoomPlusButton.getElement(), "left", iStartX + "px") ;
		DOM.setStyleAttribute(_zoomPlusButton.getElement(), "top", iStartY + "px") ;
		
		// Draw objects management icons
		//
		iStartX = iCenterX ;
		iStartY = iCenterY - _iContextIconsBlockRadius ;
			
		_workSpacePanel.add(_newLineButton) ;
		DOM.setStyleAttribute(_newLineButton.getElement(), "position", "absolute") ;
		DOM.setStyleAttribute(_newLineButton.getElement(), "left", iStartX + "px") ;
		DOM.setStyleAttribute(_newLineButton.getElement(), "top", iStartY + "px") ;
	}
	
	@Override
	public void hideZoomSlider() {
		// _workSpacePanel.remove(_zoomSliderBar) ;
		_workSpacePanel.remove(_zoomPlusButton) ;
		_workSpacePanel.remove(_zoomSmallPlusButton) ;
		_workSpacePanel.remove(_zoomNoneButton) ;
		_workSpacePanel.remove(_zoomSmallMinusButton) ;
		_workSpacePanel.remove(_zoomMinusButton) ;
		
		_workSpacePanel.remove(_newLineButton) ;
	}
	
	@Override
	public int getContextIconsRadius() {
		return _iContextIconsBlockRadius ;
	}
	
	@Override
	public HasMouseDownHandlers getMouseDownHandler() {
		return this ;
	}
	
	@Override
	public HasMouseMoveHandlers getMouseMoveHandler() {
		return this ;
	}
	
	@Override
	public HasClickHandlers getZoomPlusClickHandler() {
		return _zoomPlusButton ;
	}
	
	@Override
	public HasClickHandlers getZoomSmallPlusClickHandler() {
		return _zoomSmallPlusButton ;
	}
	
	@Override
	public HasClickHandlers getZoomNoneClickHandler() {
		return _zoomNoneButton ;
	}
	
	@Override
	public HasClickHandlers getZoomSmallMinusClickHandler() {
		return _zoomSmallMinusButton ;
	}
	
	@Override
	public HasClickHandlers getZoomMinusClickHandler() {
		return _zoomMinusButton ;
	}
	
	@Override
	public HasClickHandlers getNewLineClickHandler() {
		return _newLineButton ;
	}
	
	/** 
	 * Show New Concern dialog box (setting a proper Z Index so that it is visible)
	 */
	public void showNewConcernDialog(java.util.Date clickDate)
	{
		// set dialog box z-index
		// 
		setDialogZIndex() ;
		
		// Init dialog box
		//
		resetDialogBox() ;
		_NewConcernStartingDateBox.setValue(clickDate) ;
		
		_NewConcernDialogBox.show() ;
	}
	
	/** 
	 * Set z-index for new concern dialog box and its controls
	 */
	private void setDialogZIndex()
	{
		// Get project's Z Index and set dialog box above
		// 
		int iCurrentZIndex = getZorder() ;
		_NewConcernDialogBox.getElement().getStyle().setZIndex(iCurrentZIndex + 1) ;
		
		_NewConcernAbsolutePanel.getElement().getStyle().setZIndex(iCurrentZIndex + 2) ;
		
		// Set DatePickers Z Index
		//
		DatePicker startDatePicker = _NewConcernStartingDateBox.getDatePicker() ;
		if (null != startDatePicker)
			startDatePicker.getElement().getStyle().setZIndex(iCurrentZIndex + 3) ;
		
		DatePicker endDatePicker = _NewConcernEndingDateBox.getDatePicker() ;
		if (null != endDatePicker)
			endDatePicker.getElement().getStyle().setZIndex(iCurrentZIndex + 2) ;
		
		_NewConcernDialogLexiqueBox.getElement().getStyle().setZIndex(iCurrentZIndex + 2) ;
	}
	
	public void hideNewConcernDialog() {
		_NewConcernDialogBox.hide() ;
	}
	
	public HasClickHandlers getNewConcernDialogOkClickHandler() {
		return _NewConcernDialogBoxOkButton ;
	}
	
	public HasClickHandlers getNewConcernDialogCancelHandler() {
		return _NewConcernDialogBoxCancelButton ;
	}
	
	@Override
	public int getViewAbsoluteTop() {
		return getAbsoluteTop() ;
	}
	
	@Override
	public int getViewAbsoluteLeft() { 
		return getAbsoluteLeft() ;
	}
	
	@Override
	public Widget asWidget() {
		return this ;
	}

	@Override
	public void onResize(int width, int height) {
		// TODO Auto-generated method stub		
	}
	
	@Override
	public LexiqueTextBox getNewConceptTextBox() {
		return _NewConcernDialogLexiqueBox ;
	}
	
	@Override
	public boolean isPointInsideWorkspace(int iX, int iY)
	{
		if ((iX < _workSpacePanel.getAbsoluteLeft()) || (iX > _workSpacePanel.getAbsoluteLeft() + _workSpacePanel.getOffsetWidth()))
			return false ;
		
		if ((iY < _workSpacePanel.getAbsoluteTop()) || (iY > _workSpacePanel.getAbsoluteTop() + _workSpacePanel.getOffsetHeight()))
			return false ;
		
		return true ;
	}
	
	protected void addPanel(Panel panel, final String sStyle)
	{
		if (null == panel)
			return ;
		
		panel.addStyleName(sStyle) ;
		setPanelPositionAbsolute(panel) ;
		_mainPanel.add(panel) ;
	}
	
	/**
	 * Set a panel position mode to Absolute 
	 */
	protected void setPanelPositionAbsolute(Panel panel)
	{
		if (null != panel)
			panel.getElement().getStyle().setPosition(Position.ABSOLUTE) ;
	}
}
