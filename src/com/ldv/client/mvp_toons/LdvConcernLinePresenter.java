package com.ldv.client.mvp_toons;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import com.allen_sauer.gwt.log.client.Log;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.inject.Inject;

import com.ldv.client.canvas.LdvBoxSeverity;
import com.ldv.client.event.LdvConcernLineInitEvent;
import com.ldv.client.event.LdvConcernLineInitEventHandler;
import com.ldv.client.event.LdvDocumentInitEvent;
import com.ldv.client.event.LdvRedrawAllConcernLinesEvent;
import com.ldv.client.event.LdvRedrawAllConcernLinesEventHandler;
import com.ldv.client.event.LdvRedrawConcernLineEvent;
import com.ldv.client.event.LdvRedrawConcernLineEventHandler;
import com.ldv.client.model.LdvModelConcern;
import com.ldv.client.model.LdvModelConcernSeverityLevel;
import com.ldv.client.model.LdvModelDocument;
import com.ldv.client.mvp.LdvProjectWindowPresenter;
import com.ldv.client.util.LdvSupervisor;
import com.ldv.shared.model.LdvTime;

import net.customware.gwt.dispatch.client.DispatchAsync;
import net.customware.gwt.presenter.client.EventBus;
import net.customware.gwt.presenter.client.widget.WidgetDisplay;
import net.customware.gwt.presenter.client.widget.WidgetPresenter;

public class LdvConcernLinePresenter extends WidgetPresenter<LdvConcernLinePresenter.Display>
{
	public interface Display extends WidgetDisplay 
	{	
		public void setID(int linenumber) ;
		public void setWidth(int width) ;
		public void setTitle(String title) ;
		public void setPosition(int left, int top) ;
		public String getMainPanelId() ;
		public void addSeverityBox(LdvBoxSeverity boxSeverity) ;
		public AbsolutePanel getMainPanel() ;
		public ArrayList<LdvBoxSeverity> getSeverityBoxList() ;
		public void setSeverityBoxList(ArrayList<LdvBoxSeverity> boxSeverityList) ;
		public void ReAddSeverityBox() ;
	}

	private final LdvSupervisor _supervisor ;
	private final DispatchAsync _dispatcher ;
	
	private final int concernLineHeight = 32 ;
	private final int margin = 10 ;
	
	private ScheduledCommand 					  _pendingEvents = null ;
	protected LdvProjectWindowPresenter _project ;
	private 	AbsolutePanel							_projectPanel;
	private   LdvModelConcern						_model ; 
	private 	LdvTime										_startTime ;
	private   int 											_startPosition ;
	private   int 											_endPosition ;
	private 	int 											_width ;
	private 	int 											_top ;
	private 	int 											_lineNumber ;
	private   String										_title ;
	private 	int  								    	_projectWindowWidth ;
	private 	ArrayList<LdvModelConcernSeverityLevel> _severityArray ;
	private 	ArrayList<LdvTime> 				_dateSeverityArray ;
	private 	HashMap<Integer, String>	_severityColor;
	private 	String 										_levelOneColor	 = "#C6DCE1" ; //blue
	private   String										_levelTwoColor   = "#CFE7C2" ; //green
	private   String										_levelThreeColor = "#E6E267" ; //yellow
	private   String										_levelFourColor  = "#BC2838" ; //red
	private   ArrayList<LdvModelDocument> _documentArray ;
	
	@Inject
	public LdvConcernLinePresenter(final LdvProjectWindowPresenter project,
																 final Display display, 
			                           final EventBus eventBus,
			                           final DispatchAsync dispatcher,
			                           final LdvSupervisor supervisor) 
	{	
		super(display, eventBus) ;
		
		_dispatcher = dispatcher ;
		_supervisor = supervisor ;
		
		_project    = project ;
		
		bind() ;
		initSeverityColor() ;
	}

	@Override
	protected void onBind() 
	{			
		eventBus.addHandler(LdvConcernLineInitEvent.TYPE, new LdvConcernLineInitEventHandler() 
		{
			@Override
			public void onInitSend(LdvConcernLineInitEvent event) 
			{
				// Log.info("Handling LdvConcernLineInitEvent event");
				connectToProject(event) ;
			}
		});
		
		eventBus.addHandler(LdvRedrawConcernLineEvent.TYPE, new LdvRedrawConcernLineEventHandler() 
		{
			@Override
			public void onRedrawConcernLineSend(LdvRedrawConcernLineEvent event) 
			{
				// Log.info("Handling LdvRedrawConcernLineEvent event");
				redraw(event) ;
			}
		});
		
		eventBus.addHandler(LdvRedrawAllConcernLinesEvent.TYPE, new LdvRedrawAllConcernLinesEventHandler() 
		{
			@Override
			public void onRedrawAllConcernLinesSend(LdvRedrawAllConcernLinesEvent event) 
			{
				// Log.info("Handling LdvRedrawAllConcernLinesEvent event for " + _title) ;
				redraw() ;
			}
		});
	}
	
	public void initSeverityColor()
	{	
		_severityColor = new HashMap<Integer, String>() ;
		int severityValue ;
		
		//set level one color
		for (severityValue = 0 ; severityValue < 26 ; severityValue++)
			this._severityColor.put(severityValue, _levelOneColor) ;
		
		//set level two color
		for (severityValue = 26 ; severityValue < 51 ; severityValue++)
			this._severityColor.put(severityValue, _levelTwoColor) ;
		
		//set level three color
		for (severityValue = 51 ; severityValue < 76 ; severityValue++)
			this._severityColor.put(severityValue, _levelThreeColor) ;
		
		//set level four color
		for (severityValue = 76 ; severityValue < 101 ; severityValue++)
			this._severityColor.put(severityValue, _levelFourColor) ;
	}

	public void connectToProject(LdvConcernLineInitEvent event) 
	{	
		if (event.getTarget() != this)
			return ;
			
		_model = event.getModel() ;
		_lineNumber = event.getLineNumber() ;
				
		_startTime = _model.getBeginDate() ;
		_startPosition = Math.max(this.getProjectPosition(_startTime), 0) ;
		_endPosition   = this.getProjectPosition(_model.getEndDate()) ;
		_width = _endPosition - _startPosition ;
		_title = _model.getTitle() ;
		_top = margin + (concernLineHeight + margin) * _lineNumber ; 
		
		display.setID(_lineNumber) ;
		display.setWidth(_width) ;
		display.setTitle(_title) ;
		display.setPosition(_startPosition, _top) ;
		
		//set the severities boxes
		//
		_severityArray = (ArrayList<LdvModelConcernSeverityLevel>)_model.getSeverityArray() ;
		_dateSeverityArray = new ArrayList<LdvTime>() ;
		
		//get the start date of the severities
		for (Iterator<LdvModelConcernSeverityLevel> iter = _severityArray.iterator() ; iter.hasNext() ; )
		{
			LdvModelConcernSeverityLevel severity = iter.next() ; 
			LdvTime startDate = severity.getDate() ; 
			_dateSeverityArray.add(startDate) ;			
		}
		_dateSeverityArray.add(_model.getEndDate()) ;
		
		int indexDateSeverityArray = 0;
		for(Iterator<LdvModelConcernSeverityLevel> iter = _severityArray.iterator();iter.hasNext();)
		{	
			LdvModelConcernSeverityLevel severity = iter.next() ; 
			LdvTime startTime = _dateSeverityArray.get(indexDateSeverityArray) ;
			LdvTime endTime   = _dateSeverityArray.get(indexDateSeverityArray+1) ;
			
			LdvBoxSeverity boxSeverity = new LdvBoxSeverity(startTime, endTime) ;
		
			int startPosition = getBoxPosition(startTime) ;
			int endPosition   = getBoxPosition(endTime) ;
			int boxSeverityWidth = endPosition - startPosition ;
		
			int severityLevel = severity.getSeverityLevel() ;
						
			boxSeverity.setStartPosition(startPosition) ;
			boxSeverity.setEndPosition(endPosition) ;
			boxSeverity.setBoxWidth(boxSeverityWidth) ;
			boxSeverity.setColor(this._severityColor.get(severityLevel)) ;
			
			display.addSeverityBox(boxSeverity) ;	
			
			indexDateSeverityArray++ ;
		}
		
		//add the documents
		_documentArray = event.getModelDocumentArray() ;
		for (Iterator<LdvModelDocument> iter = _documentArray.iterator() ; iter.hasNext() ; )
		{
			LdvModelDocument documentModel = iter.next() ;
			
			LdvDocumentView view = new LdvDocumentView() ;
			LdvDocumentPresenter document = new LdvDocumentPresenter(view, eventBus) ;
			
			initDocument(document, documentModel);
		}
	 	
		_projectPanel = (AbsolutePanel) event.getProject() ; //ProjectWindow		
		_projectPanel.add(getDisplay().asWidget()) ;
	}
	
	public void initDocument(final LdvDocumentPresenter document, final LdvModelDocument documentModel) 
	{	
		if (false == eventBus.isEventHandled(LdvDocumentInitEvent.TYPE))
		{
			_pendingEvents = new ScheduledCommand() 
				{
					public void execute() {
						_pendingEvents = null ;
						initDocument(document, documentModel) ;
			     }
				};
			 Scheduler.get().scheduleDeferred(_pendingEvents) ;
		}
		else
		{	
			eventBus.fireEvent(new LdvDocumentInitEvent(display.getMainPanel(), document, this, documentModel)) ;
		}			
	}
	
	public void redraw(LdvRedrawConcernLineEvent event)
	{	
		//Log.info("entering redraw concern line.");
		if ((event.getTarget() != this) || (event.getFather() != _project))
			return ;
		
		redraw() ;
	}
	
	public void redraw()
	{	
		if (null == _model)
			return ;
		
		_startPosition = getProjectPosition(_model.getBeginDate()) ;
		_endPosition   = getProjectPosition(_model.getEndDate()) ;

		_projectWindowWidth = _project.getDisplay().getMainPanel().getElement().getOffsetWidth() ;
		
		//once startPosition equal to 0, we keep startPosition of the line 0
		//and decrease the width of the panel, to make the line seem to move.
		if (_startPosition < 0)
		{
			if (_endPosition < 0)
				return ;
			
			redrawBox() ;
			//redrawDocument() ;
			_startPosition = 0 ;
			_width = _endPosition ;
		}
		else if (_endPosition > _projectWindowWidth)
		{
			if (_startPosition > _projectWindowWidth)
				return ;
			
			redrawBox() ;
			_endPosition = _projectWindowWidth ;
			_width = _projectWindowWidth - _startPosition ;
		}
		
		display.setID(_lineNumber) ;
		display.setWidth(_width) ;
		display.setPosition(_startPosition, _top) ;
			
		_projectPanel.add(getDisplay().asWidget()) ;		
	}
	
	public void redrawBox()
	{
		ArrayList<LdvBoxSeverity> severityBoxList = display.getSeverityBoxList() ;
		if (severityBoxList.isEmpty())
			return ;
		
		ArrayList<LdvBoxSeverity> newSeverityBoxList = new ArrayList<LdvBoxSeverity>() ;
		
		for (Iterator<LdvBoxSeverity> iter = severityBoxList.iterator() ; iter.hasNext() ; )
		{
			LdvBoxSeverity severityBox = iter.next() ;
			LdvTime start_time = severityBox.getStartTime() ;
			LdvTime end_time   = severityBox.getEndTime() ;
			int start_Position = getProjectPosition(start_time) ;
			int end_Position   = getProjectPosition(end_time) ;
			
			if (end_Position <= 0)
			{	
				severityBox.setStartPosition(0) ;
				severityBox.setEndPosition(0) ;  //make the values more safe   
				severityBox.setBoxWidth(0) ;
			}
			else
			{
				// start_Position <= 0 and endPosition > 0
				if (start_Position <= 0)
				{
					severityBox.setStartPosition(0) ;
					severityBox.setEndPosition(getBoxPosition(end_time)) ;
					severityBox.setBoxWidth(end_Position) ;
				}
				else if (end_Position >= _projectWindowWidth)
				{
					if (start_Position >= _projectWindowWidth)
					{
						//todo: severityBox.setStartPosition(?) ; 
						//todo: severityBox.setEndPosition(?) ;
						severityBox.setBoxWidth(0) ;
					}
					else
					{
						//start_Position < _projectWindowWidth and endPosition > _projectWindowWidth
						severityBox.setBoxWidth(_projectWindowWidth - start_Position) ;
					}					
				}
				else
				{
					//startPosition > 0 and 0 < endPosition < _projectWindowWidth
					severityBox.setStartPosition(getBoxPosition(start_time)) ;
					severityBox.setEndPosition(getBoxPosition(end_time)) ;	
					severityBox.setBoxWidth(severityBox.getEndPosition() - severityBox.getStartPosition()) ;
				}
			}		
			newSeverityBoxList.add(severityBox) ;
		}
		
		display.setSeverityBoxList(newSeverityBoxList) ;	
		display.ReAddSeverityBox() ;
	}
	
	public void redrawDocument(){
		
	}
	
	public int getProjectPosition(LdvTime ldvTime){
		return _project.getInternalPhysicalPosition(ldvTime) ;
	}	
	
	/**
	 * Get left position of a date inside line's visible box
	 */
	public int getBoxPosition(LdvTime ldvTime)
	{
		if (null == ldvTime)
			return 0 ;
		
		if (ldvTime.isNoLimit())
			return Integer.MAX_VALUE ;
		
		//int lineOffsetLeft = DOM.getElementPropertyInt(display.getMainPanel().getElement(), "offsetLeft");
		
		// position of line's start inside project's window
		int lineOffsetLeft  = getProjectPosition(_startTime) ;
		// position of line's visible box
		int max = Math.max(lineOffsetLeft, 0) ;
		
		// position of the date inside project's window
		int projectPosition = this.getProjectPosition(ldvTime) ;
		
		int result = projectPosition - max ;
		return result ;
		
		//return (this.getProjectPosition(ldvTime) - Math.max(lineOffsetLeft, 0));
	}

	@Override
	protected void onUnbind() {
	}

	@Override
	public void revealDisplay() {
	}

	@Override
	protected void onRevealDisplay() {
		// TODO Auto-generated method stub
	}
}
