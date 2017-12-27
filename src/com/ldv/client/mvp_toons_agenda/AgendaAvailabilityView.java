package com.ldv.client.mvp_toons_agenda;

import java.util.Iterator;

import org.gwtbootstrap3.extras.datetimepicker.client.ui.DateTimePicker;
import org.gwtbootstrap3.extras.datetimepicker.client.ui.base.constants.DateTimePickerFormatViewType;
import org.gwtbootstrap3.extras.datetimepicker.client.ui.base.constants.DateTimePickerView;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasChangeHandlers;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.IntegerBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.gwt.widgetideas.client.ResizableWidget;

import com.ldv.client.loc.AgendaConstants;
import com.ldv.client.loc.LdvConstants;
import com.ldv.client.loc.MiscellanousLocFcts;
import com.ldv.client.mvp.LdvAgendaView;
import com.ldv.shared.calendar.Available;
import com.ldv.shared.calendar.CalendarRecur;
import com.ldv.shared.calendar.NumberList;
import com.ldv.shared.model.LdvTime;

public class AgendaAvailabilityView extends Composite implements ResizableWidget, AgendaAvailabilityPresenter.Display
{
	private final LdvConstants    constants       = GWT.create(LdvConstants.class) ;
	private final AgendaConstants agendaConstants = GWT.create(AgendaConstants.class) ;
	
	private final FlowPanel _mainPanel ;
	
	private       Label     _summaryLabel ;
	private       Button    _editButton ;
	private       Button    _deleteButton ;
	
	private       FlowPanel _availablesPanel ;
	private       Label     _availablesCaptionLabel ;
	private       Button    _addAvailableButton ;
	
	/**
	 * Available edition dialog box
	 */
	protected DialogBox      _EditAvailableDialogBox ;
	protected Button         _EditAvailableOkButton ;
	protected Button         _EditAvailableCancelButton ;
	protected TextBox        _AvailSummaryTextBox ;
	protected TextArea       _AvailDescripTextBox ;
	protected DateBox        _AvailFromDateBox ;
	protected DateBox        _AvailToDateBox ;
	protected DateTimePicker _AvailFromTimeBox ;
	protected DateTimePicker _AvailToTimeBox ;
	
	protected ListBox        _AvailFrequency ;
	protected FlowPanel      _FrequencyPanel ;
	protected IntegerBox     _AvailEvery ;
	protected ToggleButton   _ButtonMonday ;
	protected ToggleButton   _ButtonTuesday ;
	protected ToggleButton   _ButtonWednesday ;
	protected ToggleButton   _ButtonThursday ;
	protected ToggleButton   _ButtonFriday ;
	protected ToggleButton   _ButtonSaturday ;
	protected ToggleButton   _ButtonSunday ;
	
	/**
	 * "Are you certain you want to delete?" dialog
	 */
	protected DialogBox      _AreYouCertainDialogBox ;
	protected Label          _AreYouCertainLabel ;
	protected Button         _AreYouCertainOkButton ;
	protected Button         _AreYouCertainCancelButton ;
	
	public AgendaAvailabilityView()
	{	
		super() ;
		
		_mainPanel = new FlowPanel() ;
		_mainPanel.addStyleName("AgendaDisplayAreaForAvailability") ;
				
		createControlComponents() ;
		createAvailableWorkspace() ;
		
		initAvailableEditDialogBox() ;
		initWarningDialogBox() ;
		
		initWidget(_mainPanel) ;
	}	
	
	protected void createControlComponents()
	{
		_editButton = new Button(constants.generalEdit()) ;
		_editButton.addStyleName("AgendaAvailabilityEditButton") ;
		_mainPanel.add(_editButton) ;
		
		_summaryLabel = new Label("") ;
		_summaryLabel.addStyleName("AgendaAvailabilitySummary") ;
		_mainPanel.add(_summaryLabel) ;
		
		_deleteButton = new Button(constants.generalDelete()) ;
		_deleteButton.addStyleName("AgendaAvailabilityDeleteButton") ;
		_mainPanel.add(_deleteButton) ;
	}
	
	protected void createAvailableWorkspace()
	{
		FlowPanel availablesWorkspace = new FlowPanel() ;
		availablesWorkspace.addStyleName("AgendaDisplayAvailableAreaInAvailability") ;
		_mainPanel.add(availablesWorkspace) ;
		
		_availablesCaptionLabel = new Label(agendaConstants.availablesArea()) ;
		_availablesCaptionLabel.addStyleName("AgendaAvailableWorkspaceCaption") ;
		availablesWorkspace.add(_availablesCaptionLabel) ;
		
		_addAvailableButton = new Button(agendaConstants.availablesAdd()) ;
		_addAvailableButton.addStyleName("AgendaAvailableAddButton") ;
		availablesWorkspace.add(_addAvailableButton) ;
		
		_availablesPanel = new FlowPanel() ;
		_availablesPanel.addStyleName("AgendaDisplayAvailableWorkspaceInAvailability") ;
		availablesWorkspace.add(_availablesPanel) ;
	}
	
	/**
	 * Remove all child widgets 
	 * 
	 */
	@Override
	public void clearAll() 
	{
		_summaryLabel.setText("") ;
		_availablesPanel.clear() ;
	}
	
	/**
	 * Add a visual component for an Available component  
	 * 
	 * @param event Event to display in created widget
	 */
	@Override
	public FlowPanel createWorkspaceForAvailable()
	{
		FlowPanel eventPanel = new FlowPanel() ;
		eventPanel.addStyleName("AgendaDisplayAvailableSlot") ;
		
		_availablesPanel.add(eventPanel) ;
		
		return eventPanel ;
	}
	
	/** 
	 * Initialize Available creation/editing dialog box
	 */
	protected void initAvailableEditDialogBox()
	{
		_EditAvailableDialogBox = new DialogBox() ;
		_EditAvailableDialogBox.setPopupPosition(100, 200) ;
		_EditAvailableDialogBox.setText(agendaConstants.newAvailable()) ;
		_EditAvailableDialogBox.setAnimationEnabled(true) ;
    
		_EditAvailableOkButton = new Button(constants.generalOk()) ;
		_EditAvailableOkButton.setSize("70px", "30px") ;
			
		_EditAvailableCancelButton = new Button(constants.generalCancel()) ;
		_EditAvailableCancelButton.setSize("70px", "30px") ;
    
		_AvailSummaryTextBox     = new TextBox() ;
		_AvailSummaryTextBox.addStyleName("agendaSummaryText") ;
		_AvailDescripTextBox = new TextArea() ;
		_AvailDescripTextBox.addStyleName("agendaDescriptionText") ;
		
		
		DateTimeFormat dateFormat = DateTimeFormat.getFormat(constants.systemDateFormat()) ;
		_AvailFromDateBox = new DateBox() ;
		_AvailFromDateBox.setFormat(new DateBox.DefaultFormat(dateFormat)) ;
		_AvailToDateBox = new DateBox() ;
		_AvailToDateBox.setFormat(new DateBox.DefaultFormat(dateFormat)) ;
		
		_AvailFromTimeBox = new DateTimePicker() ;
		_AvailFromTimeBox.setFormat("hh:ii") ;
		_AvailFromTimeBox.setStartView(DateTimePickerView.DAY) ;
		_AvailFromTimeBox.setMinView(DateTimePickerView.HOUR) ;
		_AvailFromTimeBox.setMaxView(DateTimePickerView.DAY) ;
		_AvailFromTimeBox.setFormatViewType(DateTimePickerFormatViewType.TIME) ;
		_AvailToTimeBox   = new DateTimePicker() ;
		_AvailToTimeBox.setFormat("hh:ii") ;
		_AvailToTimeBox.setStartView(DateTimePickerView.DAY) ;
		_AvailToTimeBox.setMinView(DateTimePickerView.HOUR) ;
		_AvailToTimeBox.setMaxView(DateTimePickerView.DAY) ;
		_AvailToTimeBox.setFormatViewType(DateTimePickerFormatViewType.TIME) ;
		
    final VerticalPanel dialogVPanel = new VerticalPanel() ;
    
    Label labelDescription = new Label(agendaConstants.agendaDescription()) ;
    labelDescription.addStyleName("agendaEditEventLabel") ;
    Label labelFrom        = new Label(agendaConstants.agendaStart()) ;
    labelFrom.addStyleName("agendaEditEventLabel") ;
    Label labelTo          = new Label(agendaConstants.agendaEnd()) ;
    labelTo.addStyleName("agendaEditEventLabel") ;
    Label labelSummary     = new Label(agendaConstants.agendaSummary()) ;
    labelSummary.addStyleName("agendaEditEventLabel") ;
    
    FlexTable DialogBoxTable = new FlexTable();
    
    DialogBoxTable.setWidget(0, 0, labelSummary) ;
    DialogBoxTable.setWidget(0, 1, new Label(" ")) ;
    DialogBoxTable.setWidget(0, 2, _AvailSummaryTextBox) ;
    DialogBoxTable.setWidget(1, 0, labelFrom) ;
    DialogBoxTable.setWidget(1, 1, new Label(" ")) ;
    FlexTable FromTable = new FlexTable();
    FromTable.setWidget(0, 0, _AvailFromDateBox) ;
    FromTable.setWidget(0, 1, _AvailFromTimeBox) ;
    DialogBoxTable.setWidget(1, 2, FromTable) ;
    DialogBoxTable.setWidget(2, 0, labelTo) ;
    DialogBoxTable.setWidget(2, 1, new Label(" ")) ;
    FlexTable ToTable = new FlexTable();
    ToTable.setWidget(0, 0, _AvailToDateBox) ;
    ToTable.setWidget(0, 1, _AvailToTimeBox) ;
    DialogBoxTable.setWidget(2, 2, ToTable) ;
    DialogBoxTable.setWidget(3, 0, labelDescription) ;
    DialogBoxTable.setWidget(3, 1, new Label(" ")) ;
    DialogBoxTable.setWidget(3, 2, _AvailDescripTextBox) ;
    
    Label labelFrequency = new Label(agendaConstants.frequency()) ;
    labelFrequency.addStyleName("agendaEditEventLabel") ;

    _AvailFrequency = new ListBox() ;
    initFrequencyListBox(_AvailFrequency) ;
    _AvailFrequency.setItemSelected(1, true) ;
    
    _FrequencyPanel = new FlowPanel() ;
    
    DialogBoxTable.setWidget(4, 0, labelFrequency) ;
    DialogBoxTable.setWidget(4, 1, new Label(" ")) ;
    DialogBoxTable.setWidget(4, 2, _AvailFrequency) ;
    DialogBoxTable.setWidget(5, 0, new Label(" ")) ;
    DialogBoxTable.setWidget(5, 1, new Label(" ")) ;
    DialogBoxTable.setWidget(5, 2, _FrequencyPanel) ;
    
    initFrequencyPanel("WEEKLY") ;
    
/*    
    FlowPanel ExtensionsPanel = new FlowPanel() ;
    ExtensionsPanel.add(_AssesmentNewButton) ;
    ExtensionsPanel.add(_AssesmentOldButton) ;
    
    FlowPanel IatrogenicPanel = new FlowPanel() ;
    IatrogenicPanel.add(_AssesmentNotIatrogenicButton) ;
    IatrogenicPanel.add(_AssesmentIatrogenicButton) ;
    IatrogenicPanel.add(_CISPIatrogenicListBox) ;
*/    
    FlexTable ButtonsTable = new FlexTable();
    ButtonsTable.setWidget(0, 0, _EditAvailableOkButton) ;
    ButtonsTable.setWidget(0, 1, new Label(" ")) ;
    ButtonsTable.setWidget(0, 2, _EditAvailableCancelButton) ;
    
    dialogVPanel.add(DialogBoxTable) ;
    dialogVPanel.add(ButtonsTable) ;
    
    _EditAvailableDialogBox.add(dialogVPanel) ;
	}
	
	/** 
	 * initWarningDialogBox - Initialize warning dialog box
	 * 
	 * @param    nothing
	 * @return   nothing  
	 */
	private void initWarningDialogBox()
	{
		_AreYouCertainDialogBox = new DialogBox() ;
		_AreYouCertainDialogBox.setPopupPosition(100, 200) ;
		_AreYouCertainDialogBox.setText(constants.generalWarning()) ;
		_AreYouCertainDialogBox.setAnimationEnabled(true) ;
		
		_AreYouCertainLabel = new Label(agendaConstants.askDeleteAvailability()) ;
		_AreYouCertainLabel.addStyleName("warningDialogLabel") ;
    
		_AreYouCertainOkButton = new Button(constants.generalOk()) ;
		_AreYouCertainOkButton.setSize("70px", "30px") ;
		_AreYouCertainOkButton.getElement().setId("okbutton") ;
		
		_AreYouCertainCancelButton = new Button(constants.generalCancel()) ;
		_AreYouCertainCancelButton.setSize("70px", "30px") ;
		_AreYouCertainCancelButton.getElement().setId("cancelbutton") ;
		
		FlowPanel warningPannel = new FlowPanel() ;
		warningPannel.add(_AreYouCertainLabel) ;
		
		FlexTable ButtonsTable = new FlexTable();
    ButtonsTable.setWidget(0, 0, _AreYouCertainOkButton) ;
    ButtonsTable.setWidget(0, 1, new Label(" ")) ;
    ButtonsTable.setWidget(0, 2, _AreYouCertainCancelButton) ;
    
    warningPannel.add(ButtonsTable) ;
		
    _AreYouCertainDialogBox.add(warningPannel) ;
	}

	/**
	 * Fill a ListBox with proper content 
	 */
	protected void initFrequencyListBox(ListBox frequencyListBox)
	{
		if (null == frequencyListBox)
			return ;

		frequencyListBox.addItem(agendaConstants.frequencyDaily()) ;
		frequencyListBox.addItem(agendaConstants.frequencyWeekly()) ;
		frequencyListBox.addItem(agendaConstants.frequencyMonthly()) ;
		frequencyListBox.addItem(agendaConstants.frequencyYearly()) ;
		
		frequencyListBox.setVisibleItemCount(1) ;
	}

	/**
	 * Return the frequency string that fit currently selected item in listbox
	 */
	public String getSelectedFrequency(final ListBox frequencyListBox)
	{
		if (null == frequencyListBox)
			return "" ;
		
		int iSelected = frequencyListBox.getSelectedIndex() ;
		
		if (0 == iSelected)
			return "DAILY" ; 
		if (1 == iSelected)
			return "WEEKLY" ; 
		if (2 == iSelected)
			return "MONTHLY" ; 
		if (3 == iSelected)
			return "YEARLY" ;
		
		return "" ;
	}
	
	/**
	 * Return the frequency string that fit currently selected item in listbox
	 */
	@Override
	public String getSelectedFrequency() {
		return getSelectedFrequency(_AvailFrequency) ;
	}
	
	/**
	 * Fill the frequency panel with the controls that describe a given frequency
	 * 
	 * @param sFrequency Frequency: either "DAILY", "WEEKLY", "MONTHLY" or "YEARLY"
	 */
	@Override
	public void initFrequencyPanel(final String sFrequency)
	{
		_FrequencyPanel.clear() ;
		
		if ((null == sFrequency) || "".equals(sFrequency))
			return ;
		
		if ("DAILY".equals(sFrequency))
		{
			FlexTable DailyTable = new FlexTable() ;
			
			Label onceEvery = new Label(agendaConstants.frequencyEvery4Day()) ;
			onceEvery.addStyleName("agendaEditEventLabel") ;
			
			_AvailEvery = new IntegerBox() ;
			_AvailEvery.addStyleName("numberBox") ;
			
			Label onceEveryUnit = new Label(agendaConstants.frequencyEveryU4Day()) ;
			onceEveryUnit.addStyleName("agendaEditEventLabel") ;
			
			HorizontalPanel OncePanel = new HorizontalPanel() ;
			OncePanel.add(_AvailEvery) ;
			OncePanel.add(onceEveryUnit) ;
			
			DailyTable.setWidget(0, 0, onceEvery) ;
			DailyTable.setWidget(0, 1, new Label("")) ;
			DailyTable.setWidget(0, 2, OncePanel) ;
			
			_FrequencyPanel.add(DailyTable) ;
			
			return ;
		}
		
		if ("WEEKLY".equals(sFrequency))
		{
			FlexTable WeeklyTable = new FlexTable() ;
			
			// Once every...
			//
			Label onceEvery = new Label(agendaConstants.frequencyEvery4Week()) ;
			onceEvery.addStyleName("agendaEditEventLabel") ;
			
			_AvailEvery = new IntegerBox() ;
			_AvailEvery.addStyleName("numberBox") ;
			
			Label onceEveryUnit = new Label(agendaConstants.frequencyEveryU4Week()) ;
			onceEveryUnit.addStyleName("agendaEditEventLabel") ;
			
			HorizontalPanel OncePanel = new HorizontalPanel() ;
			OncePanel.add(_AvailEvery) ;
			OncePanel.add(onceEveryUnit) ;
			
			WeeklyTable.setWidget(0, 0, onceEvery) ;
			WeeklyTable.setWidget(0, 1, new Label("")) ;
			WeeklyTable.setWidget(0, 2, OncePanel) ;
			
			// On which day(s) of week
			//
			Label onThe = new Label(agendaConstants.frequencyOnThe()) ;
			onThe.addStyleName("agendaEditEventLabel") ;
			
			HorizontalPanel DoWPanel = new HorizontalPanel() ;
			_ButtonMonday    = new ToggleButton(MiscellanousLocFcts.getDayOfWeekThreeCharsLabel(1, true)) ;
			DoWPanel.add(_ButtonMonday) ;
			_ButtonTuesday   = new ToggleButton(MiscellanousLocFcts.getDayOfWeekThreeCharsLabel(2, true)) ;
			DoWPanel.add(_ButtonTuesday) ;
			_ButtonWednesday = new ToggleButton(MiscellanousLocFcts.getDayOfWeekThreeCharsLabel(3, true)) ;
			DoWPanel.add(_ButtonWednesday) ;
			_ButtonThursday  = new ToggleButton(MiscellanousLocFcts.getDayOfWeekThreeCharsLabel(4, true)) ;
			DoWPanel.add(_ButtonThursday) ;
			_ButtonFriday    = new ToggleButton(MiscellanousLocFcts.getDayOfWeekThreeCharsLabel(5, true)) ;
			DoWPanel.add(_ButtonFriday) ;
			_ButtonSaturday  = new ToggleButton(MiscellanousLocFcts.getDayOfWeekThreeCharsLabel(6, true)) ;
			DoWPanel.add(_ButtonSaturday) ;
			_ButtonSunday    = new ToggleButton(MiscellanousLocFcts.getDayOfWeekThreeCharsLabel(7, true)) ;
			DoWPanel.add(_ButtonSunday) ;
			
			WeeklyTable.setWidget(1, 0, onThe) ;
			WeeklyTable.setWidget(1, 1, new Label("")) ;
			WeeklyTable.setWidget(1, 2, DoWPanel) ;
			
			_FrequencyPanel.add(WeeklyTable) ;
						
			return ;
		}
		
		if ("MONTHLY".equals(sFrequency))
		{
			FlexTable MonthlyTable = new FlexTable() ;
			
			Label onceEvery = new Label(agendaConstants.frequencyEvery4Month()) ;
			onceEvery.addStyleName("agendaEditEventLabel") ;
			
			_AvailEvery = new IntegerBox() ;
			_AvailEvery.addStyleName("numberBox") ;
			
			Label onceEveryUnit = new Label(agendaConstants.frequencyEveryU4Month()) ;
			onceEveryUnit.addStyleName("agendaEditEventLabel") ;
			
			HorizontalPanel OncePanel = new HorizontalPanel() ;
			OncePanel.add(_AvailEvery) ;
			OncePanel.add(onceEveryUnit) ;
			
			MonthlyTable.setWidget(0, 0, onceEvery) ;
			MonthlyTable.setWidget(0, 1, new Label("")) ;
			MonthlyTable.setWidget(0, 2, OncePanel) ;
			
			_FrequencyPanel.add(MonthlyTable) ;
			
			return ;
		}
		
		if ("YEARLY".equals(sFrequency))
		{
			FlexTable YearlyTable = new FlexTable() ;
			
			Label onceEvery = new Label(agendaConstants.frequencyEvery4Year()) ;
			onceEvery.addStyleName("agendaEditEventLabel") ;
			
			_AvailEvery = new IntegerBox() ;
			_AvailEvery.addStyleName("numberBox") ;
			
			Label onceEveryUnit = new Label(agendaConstants.frequencyEveryU4Year()) ;
			onceEveryUnit.addStyleName("agendaEditEventLabel") ;
			
			HorizontalPanel OncePanel = new HorizontalPanel() ;
			OncePanel.add(_AvailEvery) ;
			OncePanel.add(onceEveryUnit) ;
			
			YearlyTable.setWidget(0, 0, onceEvery) ;
			YearlyTable.setWidget(0, 1, new Label("")) ;
			YearlyTable.setWidget(0, 2, OncePanel) ;
			
			_FrequencyPanel.add(YearlyTable) ;
			
			return ;
		}
	}
	
	/**
	 * Return the value of the Every X period control as a string
	 */
	@Override
	public String getEvery()
	{
		if (false == isActive(_AvailEvery))
			return "" ;
		
		if ("".equals(_AvailEvery.getText()))
			return "" ;
		
		return "" + _AvailEvery.getValue() ;
	}
	
	/**
	 * Set the value of the Every X period control
	 */
	@Override
	public void setEvery(final int iInterval)
	{
		if (false == isActive(_AvailEvery))
			return ;
		
		_AvailEvery.setValue(iInterval) ;
	}
	
	/**
	 * Return a NumberList containing values in [1-7] interval depending on "by day buttons" states 
	 */
	public void getByDayList(NumberList aByDayList)
	{
		if (null == aByDayList)
			return ;
		
		aByDayList.clear() ;
		
		addToByDayList(_ButtonMonday,    1, aByDayList)  ;
		addToByDayList(_ButtonTuesday,   2, aByDayList)  ;
		addToByDayList(_ButtonWednesday, 3, aByDayList)  ;
		addToByDayList(_ButtonThursday,  4, aByDayList)  ;
		addToByDayList(_ButtonFriday,    5, aByDayList)  ;
		addToByDayList(_ButtonSaturday,  6, aByDayList)  ;
		addToByDayList(_ButtonSunday,    7, aByDayList)  ;
	}
	
	public void addToByDayList(final ToggleButton button, int iButtonIndex, NumberList aByDayList)
	{
		// Check that the button is active
		//
		if (false == isActive(button))
			return ;
				
		if (false == button.isDown())
			return ;
		
		aByDayList.add(iButtonIndex) ;
	}
	
	/**
	 * Initialize the recurrence command controls from a CalendarRecur object  
	 */
	@Override
	public void initializeFromCalRecur(final CalendarRecur calRecur)
	{
		resetAllByDayButtons() ;
		
		if (null == calRecur)
			return ;
		
		String sFrequency = calRecur.getFrequency() ;
		
		if ("WEEKLY".equals(sFrequency))
		{
			initializeFromDaysList(calRecur.getDayList()) ;
		}
	}
	
	/**
	 * Initialize the day list buttons from NumberList with elements in the [1-7] range
	 */
	public void initializeFromDaysList(final NumberList aByDayList)
	{
		resetAllByDayButtons() ;
		
		if ((null == aByDayList) || aByDayList.isEmpty())
			return ;
		
		for (Iterator<Integer> it = aByDayList.iterator() ; it.hasNext() ; )
		{
			Integer iIndex = it.next() ;
			if      (1 == iIndex)
				_ButtonMonday.setDown(true) ;
			else if (2 == iIndex)
				_ButtonTuesday.setDown(true) ;
			else if (3 == iIndex)
				_ButtonWednesday.setDown(true) ;
			else if (4 == iIndex)
				_ButtonThursday.setDown(true) ;
			else if (5 == iIndex)
				_ButtonFriday.setDown(true) ;
			else if (6 == iIndex)
				_ButtonSaturday.setDown(true) ;
			else if (7 == iIndex)
				_ButtonSunday.setDown(true) ;
		}
	}
	
	/**
	 * Set all "by day buttons" to the up status
	 */
	protected void resetAllByDayButtons()
	{
		setButtonDown(_ButtonMonday,    false) ;
		setButtonDown(_ButtonTuesday,   false) ;
		setButtonDown(_ButtonWednesday, false) ;
		setButtonDown(_ButtonThursday,  false) ;
		setButtonDown(_ButtonFriday,    false) ;
		setButtonDown(_ButtonSaturday,  false) ;
		setButtonDown(_ButtonSunday,    false) ;
	}
	
	/**
	 * Set a button up or down
	 * 
	 * @param button Button to toggle
	 * @param down   <code>true</code> if the button is to be set down, <code>false</code> if it is to be set up
	 */
	protected void setButtonDown(ToggleButton button, boolean down)
	{
		if (false == isActive(button))
			return ;
		
		button.setDown(down) ;
	}
	
	@Override
	public void showDeleteAvailabilityWarningDialog() {
		_AreYouCertainDialogBox.show() ;
	}
	
	@Override
	public void hideDeleteAvailabilityWarningDialog() {
		_AreYouCertainDialogBox.hide() ;
	}
	
	@Override
	public void showAvailableEditDialog(final Available editedAvailable) 
	{
		resetAvailableDialogDialog() ;
		initAvailableDialogDialog(editedAvailable) ;
		
		CalendarRecur calRecur = editedAvailable.getRecurrence() ;
		if (null != calRecur)
		{
			String sFrequency = calRecur.getFrequency() ;
			if ("".equals(sFrequency))
				sFrequency = "WEEKLY" ;
			
			initFrequencyPanel(sFrequency) ;
			initializeFromCalRecur(calRecur) ;
		}
		else
			initFrequencyPanel("WEEKLY") ;
		
		_EditAvailableDialogBox.show() ;
	}
	
	public void resetAvailableDialogDialog()
	{
		_AvailDescripTextBox.setText("") ; 
		_AvailSummaryTextBox.setText("") ;
	}
	
	/**
	 * Initialize the Available edition dialog box controls from a given Available object
	 * 
	 * @param editedAvailable Available object to initialize the dialog box from
	 */
	public void initAvailableDialogDialog(final Available editedAvailable)
	{
		_AvailDescripTextBox.setText(editedAvailable.getDescription()) ; 
		_AvailSummaryTextBox.setText(editedAvailable.getSummary()) ;
		
		LdvTime tStart = editedAvailable.getDateStart() ;
		_AvailFromDateBox.setValue(tStart.toJavaDate()) ;
		_AvailFromTimeBox.setValue(tStart.toJavaDate()) ;
		LdvTime tEnd = editedAvailable.getDateEnd() ;
		_AvailToDateBox.setValue(tEnd.toJavaDate()) ;
		_AvailToTimeBox.setValue(tEnd.toJavaDate()) ;
	}
	
	/**
	 * Set/Update a given Available object from information in the Available edition dialog box
	 */
	@Override
	public void getEditedAvailable(Available editedAvailable)
	{
		if (null == editedAvailable)
			return ;
		
		// Global parameters
		//
		editedAvailable.setSummary(_AvailSummaryTextBox.getValue()) ;
		
		LdvTime tStartDate = new LdvTime(0) ;
		tStartDate.initFromJavaDate(_AvailFromDateBox.getValue()) ;
		LdvAgendaView.initTimeFromJavaDate(tStartDate, _AvailFromTimeBox.getValue()) ;
		editedAvailable.setDateStart(tStartDate) ;
		
		LdvTime tEndDate = new LdvTime(0) ;
		tEndDate.initFromJavaDate(_AvailToDateBox.getValue()) ;
		LdvAgendaView.initTimeFromJavaDate(tEndDate, _AvailToTimeBox.getValue()) ;
		editedAvailable.setDateEnd(tEndDate) ;
		
		editedAvailable.setDescription(_AvailDescripTextBox.getValue()) ;
		
		// RRule
		//
		getEditedAvailableRecurrence(editedAvailable) ;
	}
	
	/**
	 * 
	 */
	public void getEditedAvailableRecurrence(Available editedAvailable)
	{
		if (null == editedAvailable)
			return ;
		
		CalendarRecur calRecur = editedAvailable.getRecurrence() ;
		if (null == calRecur)
			return ;
		
		calRecur.reset() ;
		
		String sFrequency = getSelectedFrequency() ;
		calRecur.setFrequency(sFrequency) ;
		
		if ("WEEKLY".equals(sFrequency))
		{
			getByDayList(calRecur.getDayList()) ;
		}
	}
		
	@Override
	public void hideAvailableEditDialog() {
		_EditAvailableDialogBox.hide() ;
	}
				
	@Override
	public FlowPanel getMainPanel() {
		return _mainPanel ;
	}
	
	@Override
	public HasClickHandlers getEditAvailableOk() {
		return _EditAvailableOkButton ;
	}
	
	@Override
	public HasClickHandlers getEditAvailableCancel() {
		return _EditAvailableCancelButton ;
	}
	
	@Override
	public HasClickHandlers getNewAvailableButton() {
		return _addAvailableButton ;
	}

	@Override
	public HasClickHandlers getDeleteButton() {
		return _deleteButton ;
	}
	
	@Override
	public HasClickHandlers getDeleteAvailabilityOk() {
		return _AreYouCertainOkButton ;
	}
	
	@Override
	public HasClickHandlers getDeleteAvailabilityCancel() {
		return _AreYouCertainCancelButton ;
	}

	public HasChangeHandlers getFrequencyChanged() {
		return _AvailFrequency ;
	}
	
	public void setSummary(final String sSummary) {
		_summaryLabel.setText(sSummary) ;
	}
	
	protected boolean isActive(final Widget widget)
	{
		if (null == widget)
			return false ;
		
		if ((false == widget.isAttached()) || (false == widget.isVisible()))
			return false ;
		
		return true ;
	}
	
	@Override
	public Widget asWidget() {
		return this ;
	}

	@Override
	public void onResize(int width, int height) {
		// TODO Auto-generated method stub		
	}
}
