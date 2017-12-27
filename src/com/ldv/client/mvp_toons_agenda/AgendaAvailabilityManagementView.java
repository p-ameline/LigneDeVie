package com.ldv.client.mvp_toons_agenda;

import org.gwtbootstrap3.extras.datetimepicker.client.ui.DateTimePicker;
import org.gwtbootstrap3.extras.datetimepicker.client.ui.base.constants.DateTimePickerFormatViewType;
import org.gwtbootstrap3.extras.datetimepicker.client.ui.base.constants.DateTimePickerView;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.gwt.widgetideas.client.ResizableWidget;
import com.ldv.client.loc.AgendaConstants;
import com.ldv.client.loc.LdvConstants;
import com.ldv.client.mvp.LdvAgendaView;
import com.ldv.shared.calendar.Availability;
import com.ldv.shared.calendar.Available;
import com.ldv.shared.model.LdvTime;

/**
 * View (in the MVP model) to manage iCalendar Availability components that represent available time slots 
 * 
 */
public class AgendaAvailabilityManagementView extends Composite implements ResizableWidget, AgendaAvailabilityManagementPresenter.Display
{
	private final LdvConstants    constants       = GWT.create(LdvConstants.class) ;
	private final AgendaConstants agendaConstants = GWT.create(AgendaConstants.class) ;
	
	private final FlowPanel _mainPanel ;
	
	private       FlowPanel _availabilitiesPanel ;
	private       Label     _availabilitiesCaptionLabel ;
	private       Button    _addAvailabilityButton ;
	
	/**
	 * Availability edition dialog box
	 */
	protected DialogBox      _EditAvailabilityDialogBox ;
	protected Button         _EditAvailabilityOkButton ;
	protected Button         _EditAvailabilityCancelButton ;
	protected TextBox        _SummaryTextBox ;
	protected TextArea       _DescriptionTextBox ;
	protected DateBox        _FromDateBox ;
	protected DateBox        _ToDateBox ;
	protected DateTimePicker _FromTimeBox ;
	protected DateTimePicker _ToTimeBox ;
	
	public AgendaAvailabilityManagementView()
	{	
		super() ;
		
		_mainPanel = new FlowPanel() ;
		_mainPanel.addStyleName("AgendaDisplayAvailabilityWorkspace") ;

		createElements() ;
		
		initAvailabilityEditDialogBox() ;

		initWidget(_mainPanel) ;
	}

	protected void createElements()
	{
		_availabilitiesCaptionLabel = new Label(agendaConstants.availabilityArea()) ;
		_availabilitiesCaptionLabel.addStyleName("AgendaAvailabilityWorkspaceCaption") ;
		_mainPanel.add(_availabilitiesCaptionLabel) ;
		
		_addAvailabilityButton = new Button(agendaConstants.availabilityAdd()) ;
		_addAvailabilityButton.addStyleName("AgendaAvailabilityAddButton") ;
		_mainPanel.add(_addAvailabilityButton) ;
		
		_availabilitiesPanel = new FlowPanel() ;
		_availabilitiesPanel.addStyleName("AgendaDisplayAvailabilityArea") ;
		_mainPanel.add(_availabilitiesPanel) ;
	}
	
	/**
	 * Remove all child widgets 
	 * 
	 */
	@Override
	public void clearAll() {
		_availabilitiesPanel.clear() ;
	}
	
	/**
	 * Add a visual component for an Availability component  
	 * 
	 * @return The newly created panel
	 */
	@Override
	public FlowPanel createWorkspaceForAvailability()
	{
		FlowPanel availabilityPanel = new FlowPanel() ;
		availabilityPanel.addStyleName("AgendaDisplayAvailabilitySlot") ;
		
		_availabilitiesPanel.add(availabilityPanel) ;
		
		return availabilityPanel ;
	}
	
	/** 
	 * Initialize Availability creation/editing dialog box
	 */
	protected void initAvailabilityEditDialogBox()
	{
		_EditAvailabilityDialogBox = new DialogBox() ;
		_EditAvailabilityDialogBox.setPopupPosition(100, 200) ;
		_EditAvailabilityDialogBox.setText(agendaConstants.newAvailability()) ;
		_EditAvailabilityDialogBox.setAnimationEnabled(true) ;
    
		_EditAvailabilityOkButton = new Button(constants.generalOk()) ;
		_EditAvailabilityOkButton.setSize("70px", "30px") ;
			
		_EditAvailabilityCancelButton = new Button(constants.generalCancel()) ;
		_EditAvailabilityCancelButton.setSize("70px", "30px") ;
    
		_SummaryTextBox     = new TextBox() ;
		_SummaryTextBox.addStyleName("agendaSummaryText") ;
		_DescriptionTextBox = new TextArea() ;
		_DescriptionTextBox.addStyleName("agendaDescriptionText") ;
		
		
		DateTimeFormat dateFormat = DateTimeFormat.getFormat(constants.systemDateFormat()) ;
		_FromDateBox = new DateBox() ;
		_FromDateBox.setFormat(new DateBox.DefaultFormat(dateFormat)) ;
		_ToDateBox = new DateBox() ;
		_ToDateBox.setFormat(new DateBox.DefaultFormat(dateFormat)) ;
		
		_FromTimeBox = new DateTimePicker() ;
		_FromTimeBox.setFormat("HH:ii") ;
		_FromTimeBox.setStartView(DateTimePickerView.DAY) ;
		_FromTimeBox.setMinView(DateTimePickerView.HOUR) ;
		_FromTimeBox.setMaxView(DateTimePickerView.DAY) ;
		_FromTimeBox.setFormatViewType(DateTimePickerFormatViewType.TIME) ;
		_ToTimeBox   = new DateTimePicker() ;
		_ToTimeBox.setFormat("HH:ii") ;
		_ToTimeBox.setStartView(DateTimePickerView.DAY) ;
		_ToTimeBox.setMinView(DateTimePickerView.HOUR) ;
		_ToTimeBox.setMaxView(DateTimePickerView.DAY) ;
		_ToTimeBox.setFormatViewType(DateTimePickerFormatViewType.TIME) ;
		
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
    DialogBoxTable.setWidget(0, 2, _SummaryTextBox) ;
    DialogBoxTable.setWidget(1, 0, labelFrom) ;
    DialogBoxTable.setWidget(1, 1, new Label(" ")) ;
    FlexTable FromTable = new FlexTable();
    FromTable.setWidget(0, 0, _FromDateBox) ;
    FromTable.setWidget(0, 1, _FromTimeBox) ;
    DialogBoxTable.setWidget(1, 2, FromTable) ;
    DialogBoxTable.setWidget(2, 0, labelTo) ;
    DialogBoxTable.setWidget(2, 1, new Label(" ")) ;
    FlexTable ToTable = new FlexTable();
    ToTable.setWidget(0, 0, _ToDateBox) ;
    ToTable.setWidget(0, 1, _ToTimeBox) ;
    DialogBoxTable.setWidget(2, 2, ToTable) ;
    DialogBoxTable.setWidget(3, 0, labelDescription) ;
    DialogBoxTable.setWidget(3, 1, new Label(" ")) ;
    DialogBoxTable.setWidget(3, 2, _DescriptionTextBox) ;
    
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
    ButtonsTable.setWidget(0, 0, _EditAvailabilityOkButton) ;
    ButtonsTable.setWidget(0, 1, new Label(" ")) ;
    ButtonsTable.setWidget(0, 2, _EditAvailabilityCancelButton) ;
    
    dialogVPanel.add(DialogBoxTable) ;
    dialogVPanel.add(ButtonsTable) ;
    
    _EditAvailabilityDialogBox.add(dialogVPanel) ;
	}
	
	@Override
	public void showAvailabilityEditDialog(final Availability editedAvailability) 
	{
		resetAvailabilityDialogDialog() ;
		initAvailabilityDialogDialog(editedAvailability) ;
		
		_EditAvailabilityDialogBox.show() ;
	}
	
	public void resetAvailabilityDialogDialog()
	{
		_DescriptionTextBox.setText("") ; 
		_SummaryTextBox.setText("") ;
	}
	
	/**
	 * Initialize the Event edition dialog box controls from a given Event object
	 * 
	 * @param editedEvent Event object to initialize the dialog box from
	 */
	public void initAvailabilityDialogDialog(final Availability editedAvailability)
	{
		_DescriptionTextBox.setText(editedAvailability.getDescription()) ; 
		_SummaryTextBox.setText(editedAvailability.getSummary()) ;
		
		LdvTime tStart = editedAvailability.getDateStart() ;
		_FromDateBox.setValue(tStart.toJavaDate()) ;
		_FromTimeBox.setValue(tStart.toJavaDate()) ;
		LdvTime tEnd = editedAvailability.getDateEnd() ;
		_ToDateBox.setValue(tEnd.toJavaDate()) ;
		_ToTimeBox.setValue(tEnd.toJavaDate()) ;
	}
	
	/**
	 * Set/Update a given Event object from information in the Event edition dialog box
	 */
	@Override
	public void getEditedAvailability(Availability editedAvailability)
	{
		if (null == editedAvailability)
			return ;
		
		editedAvailability.setSummary(_SummaryTextBox.getValue()) ;
		
		LdvTime tStartDate = new LdvTime(0) ;
		tStartDate.initFromJavaDate(_FromDateBox.getValue()) ;
		LdvAgendaView.initTimeFromJavaDate(tStartDate, _FromTimeBox.getValue()) ;
		editedAvailability.setDateStart(tStartDate) ;
		
		LdvTime tEndDate = new LdvTime(0) ;
		tEndDate.initFromJavaDate(_ToDateBox.getValue()) ;
		LdvAgendaView.initTimeFromJavaDate(tEndDate, _ToTimeBox.getValue()) ;
		editedAvailability.setDateEnd(tEndDate) ;
		
		editedAvailability.setDescription(_DescriptionTextBox.getValue()) ;
	}
	
	@Override
	public void hideAvailabilityEditDialog() {
		_EditAvailabilityDialogBox.hide() ;
	}
	
	@Override
	public FlowPanel getMainPanel() {
		return _mainPanel ;
	}
	
	@Override
	public HasClickHandlers getNewAvailabilityButton() {
		return _addAvailabilityButton ;
	}
	
	@Override
	public HasClickHandlers getEditAvailabilityOk() {
		return _EditAvailabilityOkButton ;
	}
	
	@Override
	public HasClickHandlers getEditAvailabilityCancel() {
		return _EditAvailabilityCancelButton ;
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
