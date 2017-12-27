package com.ldv.client.mvp;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

import org.gwtbootstrap3.extras.datetimepicker.client.ui.DateTimePicker;
import org.gwtbootstrap3.extras.datetimepicker.client.ui.base.constants.DateTimePickerFormatViewType;
import org.gwtbootstrap3.extras.datetimepicker.client.ui.base.constants.DateTimePickerView;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TabBar;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;

import com.ldv.client.loc.AgendaConstants;
import com.ldv.client.loc.LdvConstants;
import com.ldv.client.loc.MiscellanousLocFcts;
import com.ldv.client.mvp.LdvAgendaPresenter.DisplayMode;
import com.ldv.shared.calendar.Event;
import com.ldv.shared.model.LdvTime;

public class LdvAgendaView extends SimplePanel implements LdvAgendaPresenter.Display
{	
	private final LdvConstants    constants       = GWT.create(LdvConstants.class) ;
	private final AgendaConstants agendaConstants = GWT.create(AgendaConstants.class) ;
	
	private LdvTime         _currentTime = new LdvTime(0) ;
	private DisplayMode     _displayMode ;
	
	/**
	 * The Panel containing the control panel on left, and the display panel on right
	 */
	private final FlowPanel _mainPanel ;
	
	/**
	 * The VerticalPanel containing the month selection panel on top, and the generic control panel on bottom
	 */
	private VerticalPanel   _controlPanel ;
	private VerticalPanel   _monthSelectionPanel ;
	private SimplePanel     _genericControlPanel ;
	
	private HorizontalPanel _monthSelectionHeaderPanel ;
	private Grid            _monthSelectionDisplayPanel ;
	private LdvTime         _monthSelectionDisplayedTime = new LdvTime(0) ;
	
	private Label           _monthSelectionMonthLabel ;
	private Button          _monthSelectionPreviousMonth ;
	private Button          _monthSelectionNextMonth ;
	private Button          _monthSelectionCurrentMonth ;
	
	private Button          _availabilityEditButton ;
	
	/**
	 * The VerticalPanel containing the display panel
	 */
	private FlowPanel       _displayPanel ;
	private FlowPanel       _displayControlPanel ;
	private SimplePanel     _displayWorkspacePanel ;
	
	private FlowPanel       _displayAreaForDays ;    // used for displays (details or summary)
	
	private FlowPanel       _titleAreaForDays ;
	private FlowPanel       _titleAreaForDaysLabels ;
	private FlowPanel       _workAreaForDays ;            // ruler on left and one or several days on right (scrolls)
	private SimplePanel     _timeControlSpace ;           // panel for ruler
	private AbsolutePanel   _workAreaForDaysWorkspaces ;  // panel for days ()
	private FlowPanel   		_workAreaForWeeksWorkspaces ; // panel for weeks ()
	
	private Button          _dateSelectionPreviousPeriod ;
	private Button          _dateSelectionNextPeriod ;
	private Button          _dateSelectionToday ;
	private Label           _dateSelectionLabel4Current ;
	
	private Label           _modeDependantLabel ;
	private TabBar          _modeSelector ;
	
	/**
	 * The display panel controls (to scroll and to change modes)
	 */
	private HorizontalPanel _PreviousNextPanel ;
	private HorizontalPanel _ModeSwitcherPanel ;
	
	private int             _iDayDisplayHeight ;
	
	public class AgendaRectangle
	{
		private int _iWidth ;
		private int _iHeight ;
		
		public AgendaRectangle(int iWidth, int iHeight)
		{
			_iWidth  = iWidth ;
			_iHeight = iHeight ; 
		}
		
		private int getWidth() { 
			return _iWidth ;
		}
		private int getHeight() { 
			return _iHeight ;
		}
	}
	
	/**
	 * Predefined variables
	 */
	private final int iLinesInMonthSelector = 6 ; 
	
	/**
	 * Everything needed to display events for a day (day, caption panel and content panel)
	 */
	public class DailyComponents
	{
		private LdvTime     _day ;
		private SimplePanel _labelPanel ;
		private FocusPanel  _containerPanel ;
		
		public DailyComponents(LdvTime day)
		{
			_day = new LdvTime(0) ;
			_day.initFromLdvTime(day) ;
			
			_labelPanel     = new SimplePanel() ;
			_containerPanel = new FocusPanel() ;
		}
		
		public LdvTime getDay() {
			return _day ;
		}
		
		public SimplePanel getLabelPanel() {
			return _labelPanel ;
		}
		
		public FocusPanel getContainerPanel() {
			return _containerPanel ; 
		}
	}
	
	private ArrayList<DailyComponents> _aDailyComponents = new ArrayList<DailyComponents>() ;
	
	/**
	 * Event edition dialog box
	 */
	protected DialogBox      _EditEventDialogBox ;
	protected Button         _EditEventOkButton ;
	protected Button         _EditEventCancelButton ;
	protected TextBox        _SummaryTextBox ;
	protected DateBox        _FromDateBox ;
	protected DateBox        _ToDateBox ;
	protected DateTimePicker _FromTimeBox ;
	protected DateTimePicker _ToTimeBox ;
	protected TextArea       _DescriptionTextBox ;
	
	public LdvAgendaView()
	{	
		super() ;
		
		this.addStyleName("ldvAgendaWorkspaceContainer") ;
		
		FlowPanel bootstrapPanel = new FlowPanel() ;
		bootstrapPanel.addStyleName("b:Panel") ;
		
		_mainPanel = new FlowPanel() ;
		// _mainPanel.addStyleName("ldvAgendaWorkspace") ;
		_mainPanel.addStyleName("b:Row") ;
		
		_currentTime = new LdvTime(0) ;
		_currentTime.takeTime() ;
		
		_displayMode = DisplayMode.DAY ;
		
		initControlPanel() ;
		initDisplayPanel() ;
		
		displayControlPanel() ;
		displayDisplayPanel() ;
		
		add(_mainPanel) ;
		
		initEditDialogBox() ;
	}

	/**
	  * This method is called when the dimensions of the parent element change.
	  * Subclasses should override this method as needed.
	  * 
	  * @param width the new client width of the element
	  * @param height the new client height of the element
	  */
	public void onResize(int iWidth, int iHeight) 
	{  
		_displayPanel.setWidth(getDisplayPanelWidth() + "px") ;
		
		if ((DisplayMode.DAY == _displayMode) || (DisplayMode.WEEK == _displayMode))
			setDaysContainersWidths() ;
	}
	
	/**
	 * Initialize the control panel: month/day selector on top of a space with miscellaneous controls
	 */
	protected void initControlPanel()
	{
		_controlPanel = new VerticalPanel() ;
		_controlPanel.addStyleName("AgendaControlPanel b:Column") ;
		
		// Month/year selector
		//
		_monthSelectionPanel = new VerticalPanel()  ;
		
		_monthSelectionHeaderPanel   = new HorizontalPanel() ;
		
		_monthSelectionMonthLabel    = new Label("") ;
		_monthSelectionMonthLabel.addStyleName("AgendaControlCurrentMonth") ;
		
		_monthSelectionPreviousMonth = new Button("<") ;
		_monthSelectionPreviousMonth.addStyleName("ldvScrollBarLefttButton") ;
		_monthSelectionCurrentMonth  = new Button("O") ;
		_monthSelectionCurrentMonth.addStyleName("ldvScrollBarLefttButton") ;
		_monthSelectionNextMonth     = new Button(">") ;
		_monthSelectionNextMonth.addStyleName("ldvScrollBarLefttButton") ;
		
		_monthSelectionHeaderPanel.add(_monthSelectionMonthLabel) ;
		_monthSelectionHeaderPanel.add(_monthSelectionPreviousMonth) ;
		_monthSelectionHeaderPanel.add(_monthSelectionCurrentMonth) ;
		_monthSelectionHeaderPanel.add(_monthSelectionNextMonth) ;
		
		// Day selector as a grid
		//
		_monthSelectionDisplayPanel  = new Grid(iLinesInMonthSelector + 1, 8) ;
		
		_monthSelectionPanel.add(_monthSelectionHeaderPanel) ;
		_monthSelectionPanel.add(_monthSelectionDisplayPanel) ;
		
		_genericControlPanel = new SimplePanel() ;
		
		_availabilityEditButton = new Button(agendaConstants.availabilityDefine()) ;
		_availabilityEditButton.addStyleName("AgendaDefineAvailabilityButton") ;
		_genericControlPanel.add(_availabilityEditButton) ;
		
		_controlPanel.add(_monthSelectionPanel) ;
		_controlPanel.add(_genericControlPanel) ;
		
		_mainPanel.add(_controlPanel) ;
		
		_monthSelectionDisplayedTime.initFromLdvTime(_currentTime) ;
		
		prepareMonthSelDisplayTable() ;
	}
	
	/**
	 * Display the list of events
	 */
	public void displayEvents(final Vector<Event> aEvents)
	{
		switch(_displayMode) 
		{
			case DAY        : 
				displayEventsForDay(aEvents) ;
				break ;
			case WEEK       : return ;
			case MULTI_WEEK : return ;
			case MONTH      : return ;
			default         : return ;
		}
	}
	
	/**
	 * Display the list of events
	 */
	public void displayEventsForDay(final Vector<Event> aEvents)
	{
		
	}
	
	public void clearDisplayWorkspace()
	{
		_displayWorkspacePanel.clear() ;
		_aDailyComponents.clear() ;
	}
	
	/**
	 * Set the display area in the day mode (single day or multiple days) and return the time control panel
	 * 
	 * @param iHourHeightInPixels Height in pixel for an hour, in order to set time area and worspace to 24 * this height
	 * 
	 * @return The panel created to host the time control widget (the time ruler)
	 */
	@Override
	public Panel prepareWorkspaceForDayMode(int iHourHeightInPixels)
	{
		// Clear everything that could already be there
		//
		clearDisplayWorkspace() ;
		
		_iDayDisplayHeight = iHourHeightInPixels * 24 ;
		
		// Set the width of the display panel (using all the space on the right of the control panel) 
		//
		int iDisplayPanelWidth = getDisplayPanelWidth() ;
		_displayPanel.setWidth(iDisplayPanelWidth + "px") ;
		
		// Create the display area for days (containing titles on top and display areas beneath)
		//
		_displayAreaForDays = new FlowPanel() ;
		_displayAreaForDays.addStyleName("AgendaDisplayAreaForDay") ;
		
		// Create containers for titles and for display areas
		//
		_titleAreaForDays   = new FlowPanel() ;
		_titleAreaForDays.addStyleName("AgendaTitleAreaForDay") ;
		_workAreaForDays    = new FlowPanel() ;
		_workAreaForDays.addStyleName("AgendaWorkAreaForDay") ;
		
		// Create a container for the empty place in the title area that stands above the ruler
		//
		SimplePanel timeControlTitleSpace = new SimplePanel() ;
		timeControlTitleSpace.addStyleName("agenda-dayDisplay-voidTitle") ;
		_titleAreaForDays.add(timeControlTitleSpace) ;
		
		// Create a container for the titles
		//
		_titleAreaForDaysLabels = new FlowPanel() ;
		_titleAreaForDaysLabels.addStyleName("agenda-dayDisplay-titlesContainer") ;
		_titleAreaForDays.add(_titleAreaForDaysLabels) ;
		
		// Create a container for the ruler
		//
		_timeControlSpace = new SimplePanel() ;
		_timeControlSpace.addStyleName("agenda-dayDisplay-timeControl") ;
		_timeControlSpace.setHeight(_iDayDisplayHeight + "px") ;
		_workAreaForDays.add(_timeControlSpace) ;
		
		// Create a container for the day displays
		//
		_workAreaForDaysWorkspaces = new AbsolutePanel() ;
		_workAreaForDaysWorkspaces.addStyleName("agenda-dayDisplay-daysContainer") ;
		_workAreaForDaysWorkspaces.setHeight(_iDayDisplayHeight + "px") ;
		_workAreaForDays.add(_workAreaForDaysWorkspaces) ;
		
		_displayAreaForDays.add(_titleAreaForDays) ;
		_displayAreaForDays.add(_workAreaForDays) ;
		
		_displayWorkspacePanel.add(_displayAreaForDays) ;
		
		return _timeControlSpace ;
	}
	
	/**
	 * Set the display area in the multi-weeks mode (4 weeks or month) and return the time control panel
	 */
	@Override
	public void prepareWorkspaceForWeeksMode()
	{
		// Clear everything that could already be there
		//
		clearDisplayWorkspace() ;
		
		// Set the width of the display panel (using all the space on the right of the control panel) 
		//
		int iDisplayPanelWidth = getDisplayPanelWidth() ;
		_displayPanel.setWidth(iDisplayPanelWidth + "px") ;
		
		// Create the display area for days (containing titles on top and display areas beneath)
		//
		_displayAreaForDays = new FlowPanel() ;
		_displayAreaForDays.addStyleName("AgendaDisplayAreaForDay") ;
		
		// Create containers for titles and for display areas
		//
		_titleAreaForDays   = new FlowPanel() ;
		_titleAreaForDays.addStyleName("AgendaTitleAreaForDay") ;
		_workAreaForDays    = new FlowPanel() ;
		_workAreaForDays.addStyleName("AgendaWorkAreaForDay") ;
		
		// Create a container for the titles (days of week names)
		//
		_titleAreaForDaysLabels = new FlowPanel() ;
		_titleAreaForDaysLabels.addStyleName("agenda-dayDisplay-titlesContainer") ;
		_titleAreaForDays.add(_titleAreaForDaysLabels) ;
		
		// Create a container for the weeks displays
		//
		_workAreaForWeeksWorkspaces = new FlowPanel() ;
		_workAreaForWeeksWorkspaces.addStyleName("agenda-dayDisplay-weeksContainer") ;
		_workAreaForDays.add(_workAreaForWeeksWorkspaces) ;
		
		_displayAreaForDays.add(_titleAreaForDays) ;
		_displayAreaForDays.add(_workAreaForDays) ;
		
		_displayWorkspacePanel.add(_displayAreaForDays) ;
		
		setDayOfWeekAsHeader() ;
	}
	
	protected void setDayOfWeekAsHeader()
	{
		for (int iDow = 1 ; iDow < 8 ; iDow++)
		{
			SimplePanel panel = new SimplePanel() ;
			panel.setStyleName("AgendaDayLabelPanel") ;
			panel.setWidth((_workAreaForWeeksWorkspaces.getOffsetWidth() / 7) - 2 + "px") ;
			
			Label dayLabel = new Label(MiscellanousLocFcts.getDayOfWeekLabel(iDow)) ;
			dayLabel.addStyleName("AgendaDayLabel") ;
			
			panel.add(dayLabel) ;
			_titleAreaForDaysLabels.add(panel) ;
		}
	}
	
	/**
	 * Create and return a new container panel for a given day 
	 */
	@Override
	public FocusPanel createWorkspaceForDay(LdvTime tDay)
	{
		DailyComponents components = new DailyComponents(tDay) ;
		components.getLabelPanel().setStyleName("AgendaDayLabelPanel") ;
		components.getContainerPanel().addStyleName("AgendaDayContainerPanel") ;
		
		_titleAreaForDaysLabels.add(components.getLabelPanel()) ;
		_workAreaForDaysWorkspaces.add(components.getContainerPanel()) ;
		
		Label dayLabel = new Label(getDayLabel(tDay, false)) ;
		dayLabel.addStyleName("AgendaDayLabel") ;
		components.getLabelPanel().add(dayLabel) ;
		
		_aDailyComponents.add(components) ;
		
		setDaysContainersWidths() ;
		
		return components.getContainerPanel() ;
	}
		
	/**
	 * Create and return a new container panel for a given day summary 
	 */
	@Override
	public FocusPanel createWorkspaceForDaySummary(LdvTime tDay, int iWeeksCount, boolean bDisplayMonth)
	{
		DailyComponents components = new DailyComponents(tDay) ;
		components.getLabelPanel().setStyleName("AgendaDaySummaryLabelPanel") ;
		components.getContainerPanel().addStyleName("AgendaDaySummaryContainerPanel") ;
		
		FlowPanel displayPanel = new FlowPanel() ;
		displayPanel.setStyleName("AgendaDaySummary") ;
		displayPanel.add(components.getLabelPanel()) ;
		displayPanel.add(components.getContainerPanel()) ;
		
		_workAreaForWeeksWorkspaces.add(displayPanel) ;
		
		Label dayLabel = new Label(getDaySummaryLabel(tDay, bDisplayMonth)) ;
		dayLabel.addStyleName("AgendaDayLabel") ;
		components.getLabelPanel().add(dayLabel) ;
		
		_aDailyComponents.add(components) ;
		
		displayPanel.setWidth((_workAreaForWeeksWorkspaces.getOffsetWidth() / 7) - 2 + "px") ;
		displayPanel.setHeight((_workAreaForWeeksWorkspaces.getOffsetHeight() / iWeeksCount) - 2 + "px") ;
		
		return components.getContainerPanel() ;
	}
	
	/**
	 * Get the dimension of the day display area, depending on the number of hours to be displayed   
	 * 
	 */
	@Override
	public AgendaRectangle getDaysContainerSize(int iVisibleHoursCount)
	{
		int iReferenceWidth = _workAreaForDays.getOffsetWidth() ;
		int iContainerWidth = iReferenceWidth - _timeControlSpace.getOffsetWidth() ;
		
		int iReferenceHeight = _workAreaForDays.getOffsetHeight() ;
		int iContainerHeight = iReferenceHeight * iVisibleHoursCount / 24 ; 
		
		return new AgendaRectangle(iContainerWidth, iContainerHeight) ;
	}
	
	/**
	 * Set the width of the containers for days (titles and display areas) 
	 * in order that they use all the space available on the right of the time control ruler
	 */
	@Override
	public void setDaysContainersWidths()
	{
		// Make certain that the display control panel is high enough for 
		//
		int iButtonsHeight = _PreviousNextPanel.getOffsetHeight() + 5 ;
		_displayControlPanel.setHeight(iButtonsHeight + "px") ;
		
		// Workspace height for days as workspace height less days titles area height 
		//
		int iWorkAreaHeight = _displayWorkspacePanel.getOffsetHeight() - _titleAreaForDays.getOffsetHeight() ;
		_workAreaForDays.setHeight(iWorkAreaHeight + "px");
		
		int iReferenceWidth = _workAreaForDays.getOffsetWidth() ;
		int iScrollWidth    = _workAreaForDays.getElement().getScrollWidth() ;
		
		int iHoursPanelWidth = _timeControlSpace.getOffsetWidth() ;
		
		int iWorkAreaWidth  = iReferenceWidth - iHoursPanelWidth ;
		// int iRemainingWidth = iWorkAreaWidth - _workAreaForDays.getElement().getScrollWidth() ;
		int iRemainingWidth = iScrollWidth - iHoursPanelWidth ;
		
		_titleAreaForDaysLabels.setWidth(iWorkAreaWidth + "px") ;
		_workAreaForDaysWorkspaces.setWidth(iRemainingWidth + "px") ;
		_titleAreaForDaysLabels.setWidth(iRemainingWidth + "px") ;
		
		if (_aDailyComponents.isEmpty())
			return ;
			
		int iDisplayedDaysCount = _aDailyComponents.size() ;
		
		double dayWidth = iRemainingWidth / iDisplayedDaysCount ;
		while (dayWidth * iDisplayedDaysCount > iRemainingWidth)
			dayWidth-- ;
		
		// Taking into account the offset
		//
		int iWorkspaceOffset = 2 ; // offset : border + margin
		dayWidth = dayWidth - iWorkspaceOffset ; 
		
		for (Iterator<DailyComponents> it = _aDailyComponents.iterator() ; it.hasNext() ;)
		{
			DailyComponents components = it.next() ;
			
			String sBorderStyle = components.getContainerPanel().getElement().getStyle().getBorderWidth() ;
			
			components.getLabelPanel().setWidth(dayWidth + "px") ;
			components.getContainerPanel().setWidth(dayWidth + "px") ;
		}
	}
	
	protected void prepareMonthSelDisplayTable()
	{
		// Fill the days titles (Mo, Tu...) row
		//
		for (int iDow = 1 ; iDow < 8 ; iDow++)
		{
			String s2lLabel = MiscellanousLocFcts.getDayOfWeekTwoCharsLabel(iDow, true) ;
			if (false == "".equals(s2lLabel))
			{
				Label dayOfWeekLabel = new Label(s2lLabel) ;
				dayOfWeekLabel.addStyleName("agenda-monthSelector-current") ;
			
				_monthSelectionDisplayPanel.setWidget(0, iDow, dayOfWeekLabel) ;
			}
		}
	}
	
	/**
	 * Display the Month/Day selection panel
	 */
	protected void displayControlPanel()
	{
		displayMonthSelectionMonthLabel() ;
		displayMonthSelectionDisplayPanel() ;
		displayTimeControlLabel() ;
	}
	
	/**
	 * Display the month and year label (à la "March 2018")
	 */
	protected void displayMonthSelectionMonthLabel()
	{
		String sMonthLabel = MiscellanousLocFcts.getMonthLabel(_monthSelectionDisplayedTime.getLocalMonth()) ;
		
		String sSimpleDate = _monthSelectionDisplayedTime.getUTCSimpleDate() ;
		String sYear = "" ;
		if (false == "".equals(sSimpleDate))
			sYear = sSimpleDate.substring(0, 4) ;
		
		String sLabel = sMonthLabel + " " + sYear ;
		
		_monthSelectionMonthLabel.setText(sLabel) ;
	}
	
	/**
	 * Fill current month's days inside a table
	 */
	protected void displayMonthSelectionDisplayPanel()
	{
		LdvTime tNow = new LdvTime(0) ;
		tNow.takeTime() ;
		
		// Get the first day of the month as a reference 
		//
		LdvTime tFirstDayOfMonth = new LdvTime(0) ;
		tFirstDayOfMonth.initFromLdvTime(_monthSelectionDisplayedTime) ;
		tFirstDayOfMonth.putLocalDate(1) ;
		
		// Display the Week of year for each weeks of this month on the left column
		//
		int iWoY = tFirstDayOfMonth.getWeekOfYear() ;
		for (int i = 0 ; i < iLinesInMonthSelector ; i++)
		{
			int iWeekToDisplay = iWoY + i ;
			_monthSelectionDisplayPanel.setText(1 + i, 0, "" + iWeekToDisplay) ;
		}
		
		// Display each day in the proper cell 
		//
		int iCurrentLine = 1 ;
		
		int iSlot = tFirstDayOfMonth.getLocalDayOfWeek() ;
		
		// First, complete the left part of the first line with ending days of the previous month
		//
		if (1 != iSlot)
		{
			LdvTime tPreviousMonthDay = new LdvTime(0) ;
			tPreviousMonthDay.initFromLdvTime(tFirstDayOfMonth) ;
			for (int i = iSlot ; i > 1 ; i--)
			{
				tPreviousMonthDay.addDays(-1, true) ;
				
				Label daySelectorLabel = new Label("" + tPreviousMonthDay.getLocalDate()) ;
				
				if      (tPreviousMonthDay.deltaDaysLocal(_currentTime) == 0)
					daySelectorLabel.addStyleName("agenda-monthSelector-selected") ;
				else if (tPreviousMonthDay.deltaDaysLocal(tNow) == 0)
					daySelectorLabel.addStyleName("agenda-monthSelector-now") ;
				else
					daySelectorLabel.addStyleName("agenda-monthSelector-previous") ;
				
				daySelectorLabel.getElement().setId("day_" + tPreviousMonthDay.getLocalSimpleDate()) ;
				
				_monthSelectionDisplayPanel.setWidget(iCurrentLine, i - 1, daySelectorLabel) ;
			}
		}
		
		// Then display all days for current month
		//
		LdvTime tCurrent = new LdvTime(0) ;
		tCurrent.initFromLdvTime(tFirstDayOfMonth) ;
		
		for (int iDay = 1 ; iDay <= tFirstDayOfMonth.daysCountWithinMonth() ; iDay++)
		{
			if (iCurrentLine <= iLinesInMonthSelector)
			{
				Label daySelectorLabel = new Label("" + tCurrent.getLocalDate()) ;
				
				if      (tCurrent.deltaDaysLocal(_currentTime) == 0)
					daySelectorLabel.addStyleName("agenda-monthSelector-selected") ;
				else if (tCurrent.deltaDaysLocal(tNow) == 0)
					daySelectorLabel.addStyleName("agenda-monthSelector-now") ;
				else
					daySelectorLabel.addStyleName("agenda-monthSelector-current") ;
			
				daySelectorLabel.getElement().setId("day_" + tCurrent.getLocalSimpleDate()) ;
				
				_monthSelectionDisplayPanel.setWidget(iCurrentLine, iSlot, daySelectorLabel) ;
				tCurrent.addDays(1, true) ;
			}
			
			iSlot++ ;
			if (iSlot > 7)
			{
				iSlot = 1 ;
				iCurrentLine++ ;
			}
		}
		
		// Finally, complete with days for next month
		//
		if ((iCurrentLine <= iLinesInMonthSelector) || (iSlot < 7))
		{
			while (iCurrentLine <= iLinesInMonthSelector)
			{
				Label daySelectorLabel = new Label("" + tCurrent.getLocalDate()) ;
				
				if      (tCurrent.deltaDaysLocal(_currentTime) == 0)
					daySelectorLabel.addStyleName("agenda-monthSelector-selected") ;
				else if (tCurrent.deltaDaysLocal(tNow) == 0)
					daySelectorLabel.addStyleName("agenda-monthSelector-now") ;
				else
					daySelectorLabel.addStyleName("agenda-monthSelector-next") ;
				
				daySelectorLabel.getElement().setId("day_" + tCurrent.getLocalSimpleDate()) ;
				
				_monthSelectionDisplayPanel.setWidget(iCurrentLine, iSlot, daySelectorLabel) ;
				tCurrent.addDays(1, true) ;
				
				iSlot++ ;
				if (iSlot > 7)
				{
					iSlot = 1 ;
					iCurrentLine++ ;
				}
			}
		}
	}
	
	/**
	 * Display the time control widgets
	 */
	protected void displayTimeControlLabel()
	{
		String sMonthLabel = MiscellanousLocFcts.getMonthLabel(_monthSelectionDisplayedTime.getLocalMonth()) ;
		
		String sSimpleDate = _monthSelectionDisplayedTime.getUTCSimpleDate() ;
		String sYear = "" ;
		if (false == "".equals(sSimpleDate))
			sYear = sSimpleDate.substring(0, 4) ;
		
		String sLabel = sMonthLabel + " " + sYear ;
		
		_monthSelectionMonthLabel.setText(sLabel) ;
	}
	
	/**
	 * Initialize the control panel: month/day selector on top of a space with miscellaneous controls
	 */
	protected void initDisplayPanel()
	{
		_displayPanel = new FlowPanel() ;
		_displayPanel.addStyleName("AgendaDisplayPanel b:Column") ;
		
		// The display control panel contains the control and navigation widgets 
		//
		_displayControlPanel = new FlowPanel() ;
		_displayControlPanel.addStyleName("AgendaDisplayControlPanel") ;
		
		_PreviousNextPanel   = new HorizontalPanel() ;
		_PreviousNextPanel.addStyleName("AgendaPreviousNextControlPanel") ;
		
		_dateSelectionPreviousPeriod = new Button("<") ;
		_dateSelectionPreviousPeriod.addStyleName("ldvScrollBarLefttButton") ;
		_dateSelectionToday          = new Button(constants.generalToday()) ;
		_dateSelectionToday.addStyleName("ldvScrollBarLefttButton") ;
		_dateSelectionNextPeriod     = new Button(">") ;
		_dateSelectionNextPeriod.addStyleName("ldvScrollBarLefttButton") ;
		
		_dateSelectionLabel4Current  = new Label("") ;
		_dateSelectionLabel4Current.addStyleName("AgendaPreviousNextCurrent") ;
		
		_PreviousNextPanel.add(_dateSelectionPreviousPeriod) ;
		_PreviousNextPanel.add(_dateSelectionToday) ;
		_PreviousNextPanel.add(_dateSelectionNextPeriod) ;
		_PreviousNextPanel.add(_dateSelectionLabel4Current) ;
		
		_ModeSwitcherPanel   = new HorizontalPanel() ;
		_ModeSwitcherPanel.addStyleName("AgendaModeSwitcher") ;
		_modeDependantLabel  = new Label("") ;
		_modeSelector        = new TabBar() ;
		_modeSelector.addTab(constants.generalDay()) ;
		_modeSelector.addTab(constants.generalWeek()) ;
		_modeSelector.addTab(constants.generalMultiWeek()) ;
		_modeSelector.addTab(constants.generalMonth()) ;
		
		_ModeSwitcherPanel.add(_modeDependantLabel) ;
		_ModeSwitcherPanel.add(_modeSelector) ;
		
		_displayControlPanel.add(_PreviousNextPanel) ;
		_displayControlPanel.add(_ModeSwitcherPanel) ;
		
		// The display area contains time displays and events
		//
		_displayWorkspacePanel = new SimplePanel() ;
		_displayWorkspacePanel.addStyleName("AgendaDisplayWorkspacePanel") ;
		
		_displayPanel.add(_displayControlPanel) ;
		_displayPanel.add(_displayWorkspacePanel) ;
		
		_mainPanel.add(_displayPanel) ;
	}
	
	/**
	 * Display the display panel
	 */
	protected void displayDisplayPanel()
	{
		displaySelectedDayLabel() ;
		displayModeSelector() ;
	}
	
	/**
	 * Display the selected day label (à la "Th 17 Aug 2017")
	 */
	@Override
	public void displaySelectedDayLabel() {
		_dateSelectionLabel4Current.setText(getDayLabel(_currentTime, true)) ;
	}
	
	/**
	 * Display the selected week label (à la "11-17 August 2017")
	 */
	@Override
	public void displaySelectedTimeIntervalLabel(final LdvTime dayFrom, final LdvTime dayTo) {
		_dateSelectionLabel4Current.setText(getTimeIntervalLabel(dayFrom, dayTo, true)) ;
	}
	
	/**
	 * Return the label for a given day (à la "Th 17 Aug 2017")
	 * 
	 * @param tDay         the day to get the label of
	 * @param bIncludeYear if <code>true</code> return the year in the label
	 * 
	 * @return A string in the for <code>Th 17 Aug 2017<code> or <code>Th 17 Aug<code> 
	 */
	protected String getDayLabel(final LdvTime tDay, boolean bIncludeYear)
	{
		if ((null == tDay) || tDay.isEmpty())
			return "?" ;
		if (tDay.isNoLimit())
			return "9999" ;
		
		String sLabel = MiscellanousLocFcts.getDayOfWeekTwoCharsLabel(tDay.getLocalDayOfWeek(), true) ;
		sLabel += " " + tDay.getLocalDate() ;
		sLabel += " " +  MiscellanousLocFcts.getMonthThreeCharsLabel(tDay.getLocalMonth(), false) ;
		
		if (bIncludeYear)
			sLabel += " " + _currentTime.getLocalFullYear() ;
		
		return sLabel ;
	}
	
	/**
	 * Return the label for a given day (à la "17 Aug")
	 * 
	 * @param tDay         the day to get the label of
	 * @param bIncludeYear if <code>true</code> return the month in the label
	 * 
	 * @return A string in the for <code>17 Aug<code> or <code>17<code> 
	 */
	protected String getDaySummaryLabel(final LdvTime tDay, boolean bIncludeMonth)
	{
		if ((null == tDay) || tDay.isEmpty())
			return "?" ;
		if (tDay.isNoLimit())
			return "9999" ;
		
		String sLabel = tDay.getLocalDate() + "" ;
		
		if (bIncludeMonth)
			sLabel += " " +  MiscellanousLocFcts.getMonthThreeCharsLabel(tDay.getLocalMonth(), false) ;
		
		return sLabel ;
	}
	
	/**
	 * Return the label for a given time interval (à la "11-17 September 2017")
	 * 
	 * @param tDay         the day to get the label of
	 * @param bIncludeYear if <code>true</code> return the year in the label
	 * 
	 * @return A string in the for <code>Th 17 Aug 2017<code> or <code>Th 17 Aug<code> 
	 */
	protected String getTimeIntervalLabel(final LdvTime dayFrom, final LdvTime dayTo, boolean bIncludeYear)
	{
		if ((null == dayFrom) || dayFrom.isEmpty() || (null == dayTo) || dayTo.isEmpty())
			return "?" ;
		if (dayFrom.isNoLimit())
			return "9999" ;
		
		String sLabel = "" ;
		
		// First major case, day from and day to belong to the same year
		//
		if (dayFrom.getLocalFullYear() == dayTo.getLocalFullYear())
		{
			// First case: day from and day to belong to the same month -> dayfrom - dayto month year
			//
			if (dayFrom.getLocalMonth() == dayTo.getLocalMonth())
			{
				sLabel = dayFrom.getLocalDate() + " - " + dayTo.getLocalDate() ;
				sLabel += " " +  MiscellanousLocFcts.getMonthLabel(dayFrom.getLocalMonth()) ;
			}
			//
			// Both days don't belong to the same month -> dayfrom monthfrom - dto monthto year
			//
			else
			{
				sLabel = dayFrom.getLocalDate() + " " + MiscellanousLocFcts.getMonthLabel(dayFrom.getLocalMonth()) + " - " ; 
				sLabel += dayTo.getLocalDate() + " " + MiscellanousLocFcts.getMonthLabel(dayTo.getLocalMonth()) ;
			}
			
			if (bIncludeYear)
				sLabel += " " + dayFrom.getLocalFullYear() ;
		}
		//
		// Second major case, day from and day to don't belong to the same year -> years are always mentioned
		//
		else
		{
			sLabel = dayFrom.getLocalDate() + " " + MiscellanousLocFcts.getMonthLabel(dayFrom.getLocalMonth()) + " " + dayFrom.getLocalFullYear() + " - " ; 
			sLabel += dayTo.getLocalDate() + " " + MiscellanousLocFcts.getMonthLabel(dayTo.getLocalMonth()) + " " + dayTo.getLocalFullYear() ;
		}

		return sLabel ;
	}
	
	/**
	 * Display the mode selector
	 */
	protected void displayModeSelector()
	{
		selectTab4Mode() ;
	}
	
	/**
	 * Select the proper tab for current mode
	 */
	protected void selectTab4Mode()
	{
		int iIndex = getTab4Mode(_displayMode) ;
		
		if (iIndex >= 0)
			_modeSelector.selectTab(iIndex, false) ; 
	}
	
	/**
	 * Get the tab index for a given mode
	 */
	protected int getTab4Mode(DisplayMode mode)
	{
		switch (mode) {
			case DAY        : return  0 ;
			case WEEK       : return  1 ;
			case MULTI_WEEK : return  2 ;
			case MONTH      : return  3 ;
		  default         : return -1 ;
		}
	}
	
	/**
	 * Get the mode of the currently selected tab
	 */
	protected DisplayMode getSelectedMode() {
		return getModeForPosition(_modeSelector.getSelectedTab()) ;
	}
	
	/**
	 * Get the mode for a given tab index
	 * 
	 * @param iTabIndex Index in tab, zero based (in the 0-3 interval)
	 */
	@Override
	public DisplayMode getModeForPosition(int iTabIndex)
	{
		if (0 == iTabIndex)
			return DisplayMode.DAY ;
		if (1 == iTabIndex)
			return DisplayMode.WEEK ;
		if (2 == iTabIndex)
			return DisplayMode.MULTI_WEEK ;
		if (3 == iTabIndex)
			return DisplayMode.MONTH ;
		
		return DisplayMode.NONE ;
	}

	/**
	 * Get the width of the display panel so that it takes all the space on the left of the control panel
	 * 
	 */
	protected int getDisplayPanelWidth() 
	{
		int iMainWidth    = _mainPanel.getOffsetWidth() ;
		int iControlWidth = _controlPanel.getOffsetWidth() ;
		
		return iMainWidth - iControlWidth - 5 ;
	}
	
	@Override
	public Panel getMainPanel() {
		return _mainPanel ;
	}
	
	@Override
	public void setSelectedDay(final LdvTime tToSet)
	{
		if ((null == tToSet) || (_currentTime.deltaDaysLocal(tToSet) == 0))
			return ;
		
		_currentTime.initFromLdvTime(tToSet) ;
		
		setmonthSelectionDisplayedTime(_currentTime) ;
	}
	
	/**
	 * Erase all information in the days area
	 * 
	 */
	public void resetDayPanels()
	{
		_titleAreaForDaysLabels.clear() ;
		_workAreaForDaysWorkspaces.clear() ;
		_workAreaForWeeksWorkspaces.clear() ;
		
		_aDailyComponents.clear() ;
	}
	
	/** 
	 * Initialize event creation/editing dialog box
	 */
	protected void initEditDialogBox()
	{
		_EditEventDialogBox = new DialogBox() ;
		_EditEventDialogBox.setPopupPosition(100, 200) ;
		_EditEventDialogBox.setText(agendaConstants.newEvent()) ;
		_EditEventDialogBox.setAnimationEnabled(true) ;
    
		_EditEventOkButton = new Button(constants.generalOk()) ;
		_EditEventOkButton.setSize("70px", "30px") ;
			
		_EditEventCancelButton = new Button(constants.generalCancel()) ;
		_EditEventCancelButton.setSize("70px", "30px") ;
    
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
    ButtonsTable.setWidget(0, 0, _EditEventOkButton) ;
    ButtonsTable.setWidget(0, 1, new Label(" ")) ;
    ButtonsTable.setWidget(0, 2, _EditEventCancelButton) ;
    
    dialogVPanel.add(DialogBoxTable) ;
//    dialogVPanel.add(ExtensionsPanel) ;
//    dialogVPanel.add(IatrogenicPanel) ;
    dialogVPanel.add(ButtonsTable) ;
    
    _EditEventDialogBox.add(dialogVPanel) ;
	}
	
	@Override
	public void showEventEditDialog(final Event editedEvent) 
	{
		resetEventDialogDialog() ;
		initEventDialogDialog(editedEvent) ;
		
		_EditEventDialogBox.show() ;
	}
	
	public void resetEventDialogDialog()
	{
		_DescriptionTextBox.setText("") ; 
		_SummaryTextBox.setText("") ;
	}
	
	/**
	 * Initialize the Event edition dialog box controls from a given Event object
	 * 
	 * @param editedEvent Event object to initialize the dialog box from
	 */
	public void initEventDialogDialog(final Event editedEvent)
	{
		_DescriptionTextBox.setText(editedEvent.getDescription()) ; 
		_SummaryTextBox.setText(editedEvent.getSummary()) ;
		
		LdvTime tStart = editedEvent.getDateStart() ;
		_FromDateBox.setValue(tStart.toJavaDate()) ;
		_FromTimeBox.setValue(tStart.toJavaDate()) ;
		LdvTime tEnd = editedEvent.getDateEnd() ;
		_ToDateBox.setValue(tEnd.toJavaDate()) ;
		_ToTimeBox.setValue(tEnd.toJavaDate()) ;
	}
	
	/**
	 * Set/Update a given Event object from information in the Event edition dialog box
	 */
	@Override
	public void getEditedEvent(Event editedEvent)
	{
		if (null == editedEvent)
			return ;
		
		editedEvent.setSummary(_SummaryTextBox.getValue()) ;
		
		LdvTime tStartDate = new LdvTime(0) ;
		tStartDate.initFromJavaDate(_FromDateBox.getValue()) ;
		initTimeFromJavaDate(tStartDate, _FromTimeBox.getValue()) ;
		editedEvent.setDateStart(tStartDate) ;
		
		LdvTime tEndDate = new LdvTime(0) ;
		tEndDate.initFromJavaDate(_ToDateBox.getValue()) ;
		initTimeFromJavaDate(tEndDate, _ToTimeBox.getValue()) ;
		editedEvent.setDateEnd(tEndDate) ;
		
		editedEvent.setDescription(_DescriptionTextBox.getValue()) ;
	}
	
	static public void initTimeFromJavaDate(LdvTime tDate, final Date javaDate)
	{
		tDate.putLocalHours(javaDate.getHours()) ;
		tDate.putLocalMinutes(javaDate.getMinutes()) ;
		tDate.putLocalSeconds(javaDate.getSeconds()) ;
		tDate.putLocalMilliseconds(0) ;
	}
	
	@Override
	public void hideEventEditDialog() {
		_EditEventDialogBox.hide() ;
	}
	
	public HasClickHandlers getEditEventOk() {
		return _EditEventOkButton ;
	}
	
	public HasClickHandlers getEditEventCancel() {
		return _EditEventCancelButton ;
	}
	
	@Override
	public Panel getDisplayWorkspacePanel() {
		return _displayWorkspacePanel ;
	}
	
	@Override
	public HasClickHandlers getDisplayedMonthPreviousButton() {
		return _monthSelectionPreviousMonth ;
	}
	
	@Override
	public HasClickHandlers getDisplayedMonthNextButton() {
		return _monthSelectionNextMonth ;
	}
		
	@Override
	public HasClickHandlers getDisplayedMonthNowButton() {
		return _monthSelectionCurrentMonth ;
	}
	
	@Override
	public HasSelectionHandlers<Integer> getModeSelectionTab() {
		return _modeSelector ;
	}
	
	@Override
	public HasClickHandlers getDisplayedPeriodPreviousButton() {
		return _dateSelectionPreviousPeriod ;
	}
	
	@Override
	public HasClickHandlers getDisplayedPeriodNextButton() {
		return _dateSelectionNextPeriod ;
	}
	
	@Override
	public HasClickHandlers getDisplayedPeriodNowButton() {
		return _dateSelectionToday ;
	}
	
	@Override
	public LdvTime getmonthSelectionDisplayedTime() {
		return _monthSelectionDisplayedTime ;
	}
	
	@Override
	public void setmonthSelectionDisplayedTime(final LdvTime tToSet) 
	{
		_monthSelectionDisplayedTime.initFromLdvTime(tToSet) ;
		displayControlPanel() ;
	}
	
	/**
	 * Change the style of selected day's header (in order to have selected day's label different from other days)
	 * 
	 */
	@Override
	public void selectDisplayedTime()
	{
		if (_aDailyComponents.isEmpty())
			return ;
		
		LdvTime tNow = new LdvTime(0) ;
		tNow.takeTime() ;
		
		for (Iterator<DailyComponents> it = _aDailyComponents.iterator() ; it.hasNext() ;)
		{
			DailyComponents component = it.next() ;
			
			LdvTime     tComponentDay = component.getDay() ;
			SimplePanel panel         = component.getLabelPanel() ;
			
			Label label = (Label) panel.getWidget() ;
			
			if      (tComponentDay.deltaDaysLocal(_currentTime) == 0)
			{
				setStyle(panel, "AgendaDayLabelPanelSelected", "AgendaDayLabelPanelNow", "AgendaDayLabelPanel") ;
				
				if (null != label)
					setStyle(label, "AgendaDayLabelSelected", "AgendaDayLabelNow", "AgendaDayLabel") ;
			}
			else if (tComponentDay.deltaDaysLocal(tNow) == 0)
			{
				setStyle(panel, "AgendaDayLabelPanelNow", "AgendaDayLabelPanelSelected", "AgendaDayLabelPanel") ;
				
				if (null != label)
					setStyle(label, "AgendaDayLabelNow", "AgendaDayLabelSelected", "AgendaDayLabel") ;
			}
			else
			{
				setStyle(panel, "AgendaDayLabelPanel", "AgendaDayLabelPanelSelected", "AgendaDayLabelPanelNow") ;
				
				if (null != label)
					setStyle(label, "AgendaDayLabel", "AgendaDayLabelSelected", "AgendaDayLabelNow") ;
			}
		}
	}

	/**
	 * Change the style of selected day's header (in order to have selected day's label different from other days)
	 * 
	 */
	@Override
	public void selectDisplayedTimeForSummary()
	{
		if (_aDailyComponents.isEmpty())
			return ;
		
		LdvTime tNow = new LdvTime(0) ;
		tNow.takeTime() ;
		
		// Iterate through all information elements (every displayed days)
		//
		for (Iterator<DailyComponents> it = _aDailyComponents.iterator() ; it.hasNext() ;)
		{
			DailyComponents component = it.next() ;
			
			// Get the components: the label panel and the events panel
			//
			LdvTime     tComponentDay = component.getDay() ;
			SimplePanel labelPanel    = component.getLabelPanel() ;
			SimplePanel contentPanel  = component.getContainerPanel() ;
			
			Label label = (Label) labelPanel.getWidget() ;
			
			// Resize the events panel
			//
			Widget container = contentPanel.getParent() ;
			contentPanel.setHeight(container.getOffsetHeight() - labelPanel.getOffsetHeight() + "px") ;
			
			// Set styles according to day being the selected one, or now, or a regular day
			//
			if      (tComponentDay.deltaDaysLocal(_currentTime) == 0)
			{
				setStyle(labelPanel, "AgendaDaySummaryLabelPanelSelected", "AgendaDaySummaryLabelPanelNow", "AgendaDaySummaryLabelPanel") ;
				setStyle(contentPanel, "AgendaDaySummaryContainerPanelSelected", "AgendaDaySummaryContainerPanel", "AgendaDaySummaryContainerPanelNow") ;
				
				if (null != label)
					setStyle(label, "AgendaDayLabelSelected", "AgendaDayLabelNow", "AgendaDayLabel") ;
			}
			else if (tComponentDay.deltaDaysLocal(tNow) == 0)
			{
				setStyle(labelPanel, "AgendaDaySummaryLabelPanelNow", "AgendaDaySummaryLabelPanelSelected", "AgendaDaySummaryLabelPanel") ;
				setStyle(contentPanel, "AgendaDaySummaryContainerPanelNow", "AgendaDaySummaryContainerPanelSelected", "AgendaDaySummaryContainerPanel") ;
				
				if (null != label)
					setStyle(label, "AgendaDayLabelNow", "AgendaDayLabelSelected", "AgendaDayLabel") ;
			}
			else
			{
				setStyle(labelPanel, "AgendaDaySummaryLabelPanel", "AgendaDaySummaryLabelPanelSelected", "AgendaDaySummaryLabelPanelNow") ;
				setStyle(contentPanel, "AgendaDaySummaryContainerPanel", "AgendaDaySummaryContainerPanelSelected", "AgendaDaySummaryContainerPanelNow") ;
				
				if (null != label)
					setStyle(label, "AgendaDayLabel", "AgendaDayLabelSelected", "AgendaDayLabelNow") ;
			}
		}
	}
	
	/**
	 * Set the style of a widget by removing some styles and setting another one
	 * 
	 */
	protected void setStyle(Widget widget, final String sStyleToSet, final String sStyleToRemove1, final String sStyleToRemove2)
	{
		if (null == widget)
			return ;
		
		if ((null != sStyleToRemove1) && (false == "".equals(sStyleToRemove1)))
			widget.removeStyleName(sStyleToRemove1) ;
		if ((null != sStyleToRemove2) && (false == "".equals(sStyleToRemove2)))
			widget.removeStyleName(sStyleToRemove2) ;
		
		if ((null != sStyleToSet) && (false == "".equals(sStyleToSet)))
			widget.setStyleName(sStyleToSet) ;
	}
	
	@Override
	public void setDisplayMode(DisplayMode displayMode) {
		_displayMode = displayMode ;
	}
	
	@Override
	public DisplayMode getDisplayMode() {
		return _displayMode ;
	}
	
	@Override
	public String getDayForMonthGridCell(int iRow, int iCol)
	{
		if ((iRow < 0) || (iRow > iLinesInMonthSelector) || (iCol < 0) || (iCol > 7))
			return "" ;
		
		return "" ;
	}
	
	@Override
	public HasClickHandlers getDisplayedMonthGrid() {
		return _monthSelectionDisplayPanel ;
	}
	
	@Override
	public Grid getDaySelectorGrid() {
		return _monthSelectionDisplayPanel ;
	}

	@Override
	public ArrayList<DailyComponents> getDailyComponents() {
		return _aDailyComponents ;
	}
	
	@Override
	public HasClickHandlers getAvailabilityEditButton() {
		return _availabilityEditButton ;
	}
	
	/**
	 * Get the Y coordinate inside the day display panel from the Y position within the browser window's client area
	 * 
	 * @return <code>-1</code> if iClientYInBrowser is not inside the day workspace, the Y coordinate if it is
	 */
	@Override
	public int getClientYInDay(int iClientYInBrowser)
	{
		int iDayWorkspaceTop = _workAreaForDaysWorkspaces.getAbsoluteTop() ;
		if (iDayWorkspaceTop > iClientYInBrowser)
			return -1 ;
		
		int iYPosInWorkspace = iClientYInBrowser - iDayWorkspaceTop ;
		
		return iYPosInWorkspace ; 
	}
	
	@Override
	public Widget asWidget() {
		return this ;
	}
}
