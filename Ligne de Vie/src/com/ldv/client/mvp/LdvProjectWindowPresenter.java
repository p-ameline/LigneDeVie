package com.ldv.client.mvp;

import java.util.ArrayList;
import java.util.Iterator;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.HasMouseDownHandlers;
import com.google.gwt.event.dom.client.HasMouseMoveHandlers;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.inject.Inject;

import com.ldv.client.canvas.LdvProjectTab;
import com.ldv.client.event.LdvBaseLineInitEvent;
import com.ldv.client.event.LdvBirthSeparatorInitEvent;
import com.ldv.client.event.LdvConcernLineInitEvent;
import com.ldv.client.event.LdvNowSeparatorInitEvent;
import com.ldv.client.event.LdvProjectInitEvent;
import com.ldv.client.event.LdvProjectInitEventHandler;
import com.ldv.client.event.LdvProjectSetZPosEvent;
import com.ldv.client.event.LdvProjectsEventEvent;
import com.ldv.client.event.LdvProjectsEventEventHandler;
import com.ldv.client.event.LdvRedrawAllConcernLinesEvent;
import com.ldv.client.event.LdvRedrawAllProjectsWindowEvent;
import com.ldv.client.event.LdvRedrawAllProjectsWindowEventHandler;
import com.ldv.client.event.LdvRedrawProjectRecursiveEvent;
import com.ldv.client.event.LdvRedrawProjectRecursiveEventHandler;
import com.ldv.client.event.LdvRedrawProjectWindowEvent;
import com.ldv.client.event.LdvRedrawProjectWindowEventHandler;
import com.ldv.client.event.LdvTeamRosaceInitEvent;
import com.ldv.client.model.LdvModelConcern;
import com.ldv.client.model.LdvModelDemographics;
import com.ldv.client.model.LdvModelDocument;
import com.ldv.client.model.LdvModelProject;
import com.ldv.client.model.LdvModelRosace;
import com.ldv.client.model.LdvModelTeam;
import com.ldv.client.mvp_toons.LdvBaseLinePresenter;
import com.ldv.client.mvp_toons.LdvBaseLineView;
import com.ldv.client.mvp_toons.LdvBirthSeparatorPresenter;
import com.ldv.client.mvp_toons.LdvBirthSeparatorView;
import com.ldv.client.mvp_toons.LdvConcernLinePresenter;
import com.ldv.client.mvp_toons.LdvConcernLineView;
import com.ldv.client.mvp_toons.LdvNowSeparatorPresenter;
import com.ldv.client.mvp_toons.LdvNowSeparatorView;
import com.ldv.client.util.LdvGraphManager;
import com.ldv.client.util.LdvPoint;
import com.ldv.client.util.LdvSupervisor;
import com.ldv.client.util.LdvTimeZoomLevel;
import com.ldv.shared.database.Lexicon;
import com.ldv.shared.model.LdvTime;
import com.ldv.shared.rpc4ontology.GetLexiconAction;
import com.ldv.shared.rpc4ontology.GetLexiconResult;

import net.customware.gwt.dispatch.client.DispatchAsync;
import net.customware.gwt.presenter.client.EventBus;
import net.customware.gwt.presenter.client.widget.WidgetDisplay;
import net.customware.gwt.presenter.client.widget.WidgetPresenter;

/** 
 * The Presenter object for a Project
 * 
 */
public class LdvProjectWindowPresenter extends WidgetPresenter<LdvProjectWindowPresenter.Display>
{
	private LdvModelProject                    _projectModel ;
	private String                             _sProjectString ;

	private int                                _iTimerInterval ;  // in milliseconds
	
	private int                                _iZOrder ;
	
	private LdvTimeControlledAreaPresenter     _timeControlledArea ;
	private boolean                            _isBound              = false ;
	private ScheduledCommand 							     _pendingEvents        = null ;
	private ScheduledCommand 							     _pendingEvents2       = null ;
	private ScheduledCommand 							     _pendingEvents3       = null ;
	private ScheduledCommand 							     _pendingEvents4       = null ;
	private ScheduledCommand 							     _pendingEvents5       = null ;
	private AbsolutePanel 								     _workspace ;
	private LdvTeamRosacePresenter 			       _teamRosace           = null ;
	private LdvNowSeparatorPresenter 					 _nowSeparator         = null ;
	private LdvBirthSeparatorPresenter 				 _birthSeparator       = null ;
	private LdvBaseLinePresenter 					     _baseLine             = null ;
	private ArrayList<LdvModelConcern> 		     _concernsModelsArray  = null ; 
	private ArrayList<LdvModelDocument>		     _documentsModelsArray = null ;	
	private ArrayList<LdvConcernLinePresenter> _concernLinesArray    = null ;
	
	private boolean												     _isCurrentProject     = false ;
	private double 												     _projectScrollAreaWidthRatio ;
	private boolean 											     _onTopDrag            = false ;
	private boolean												     _onBottomDrag         = false ;
	private boolean 											     _onLeftDrag           = false ;
	private boolean												     _onRightDrag          = false ;
	private boolean												     _onMoveDrag           = false ;	
  private int 													     _startRelativeY ;
  private int 													     _startRelativeX ;
  private int 													     _projectTop ;
  private int 													     _projectBottom ;
  private int 													     _projectLeft ;
  private int														     _projectRight ;
  private int 													     _projectHeight ;
  private int 													     _projectWidth ;
  
  /**
	 * Information that control the context menu (icons) popup
	 */
  private boolean												     _areToolsDisplayed = false ;
  private int 													     _iToolsCenterX ; // x-position within the browser window's client area
  private int 													     _iToolsCenterY ; // y-position within the browser window's client area
  private int 													     _iToolsRadius ;
  
  /** 
	 * Methods expected from a Project display
	 */
	public interface Display extends WidgetDisplay 
	{	
		public AbsolutePanel        getMainPanel() ;
		public AbsolutePanel        getBaseLinePanel() ;
		public AbsolutePanel        getWorkSpacePanel() ;
		public FocusPanel           getTopFocusPanel() ;
		public FocusPanel           getBottomFocusPanel() ;
		public FocusPanel 	        getLeftFocusPanel() ;
		public FocusPanel 	        getRightFocusPanel() ;
		public FocusPanel           getBarFocusPanel() ;
		public LdvProjectTab        getTabFocusPanel() ;
		public void                 setZorder(int iZorder) ;
		public void 			          setHeight(int height) ;
		public void 				        setTop(int top) ;
		public void 				        setBottom(int bottom) ;
		public void                 setBottomForZorder(int iZorder, int iMaxZorder, int iProjectsAreaBottom) ;
		public void 				        setLeft(int left) ;
		public void 				        setRight(int right) ;
		public void                 setProjectTitle(String sTitle) ;
		public void                 showZoomSlider(MouseDownEvent event) ;
		public void                 showZoomSlider(int iX, int iY) ;
		public void                 hideZoomSlider() ;
		public int                  getContextIconsRadius() ;
		public HasMouseDownHandlers getMouseDownHandler() ;
		public HasMouseMoveHandlers getMouseMoveHandler() ;
		public HasClickHandlers     getZoomPlusClickHandler() ;
		public HasClickHandlers     getZoomSmallPlusClickHandler() ;
		public HasClickHandlers     getZoomNoneClickHandler() ;
		public HasClickHandlers     getZoomSmallMinusClickHandler() ;
		public HasClickHandlers     getZoomMinusClickHandler() ;
		public HasClickHandlers     getNewLineClickHandler() ;
		public int                  getViewAbsoluteTop() ;
		public int                  getViewAbsoluteLeft() ;	
		public int                  getClickEventRelativeX(ClickEvent event) ;
		public int                  getClickEventRelativeY(ClickEvent event) ;
		public void                 showNewConcernDialog(java.util.Date clickDate) ;
		public void                 hideNewConcernDialog() ;
		public HasClickHandlers     getNewConcernDialogOkClickHandler() ;
		public HasClickHandlers     getNewConcernDialogCancelHandler() ;
		public boolean              isPointInsideWorkspace(int iX, int iY) ;
	}
	
	private final LdvSupervisor _supervisor ;
	private final DispatchAsync _dispatcher ;

	/** 
	 * Constructor
	 */
	@Inject
	public LdvProjectWindowPresenter(final Display       display, 
			                             final EventBus      eventBus,
			                             final DispatchAsync dispatcher,
			                             final LdvSupervisor supervisor) 
	{	
		super(display, eventBus) ;
		
		_iTimerInterval = 60 * 1000 ; // milliseconds
		
		_dispatcher     = dispatcher ;
		_supervisor     = supervisor ;
		_sProjectString = "" ;
		_iZOrder        = -1 ;
		
		_iToolsRadius   = 20 ;
		
		bind() ;
		_isBound        = true ;	
	}
	  
	/** 
	 * What happens when Presenter and Display get connected
	 */
	@Override
	protected void onBind() 
	{			
		eventBus.addHandler(LdvProjectInitEvent.TYPE, new LdvProjectInitEventHandler() 
		{
			@Override
			public void onInitSend(LdvProjectInitEvent event) 
			{
				// Log.info("Handling LdvProjectInitEvent event");
				initComponents(event) ;
			}
		});
		
		eventBus.addHandler(LdvRedrawProjectWindowEvent.TYPE, new LdvRedrawProjectWindowEventHandler() 
		{
			@Override
			public void onRedrawProjectWindowSend(LdvRedrawProjectWindowEvent event) 
			{
				// Log.info("Handling LdvRedrawProjectWindowEvent event");
				redrawComponents(event) ;
			}
		});
		
		eventBus.addHandler(LdvRedrawAllProjectsWindowEvent.TYPE, new LdvRedrawAllProjectsWindowEventHandler() 
		{
			@Override
			public void onRedrawAllProjectsWindowSend(LdvRedrawAllProjectsWindowEvent event) 
			{
				// Log.info("Handling LdvRedrawAllProjectsWindowEvent event for " + _sProjectString) ;
				redrawComponents() ;
			}
		});
		
		// TimeControlledAreaWindows sends MouseUp in case ProjectWindows could have missed it
		//
		eventBus.addHandler(LdvProjectsEventEvent.TYPE, new LdvProjectsEventEventHandler() 
		{
			@Override
			public void onProjectsEventSend(LdvProjectsEventEvent event) 
			{
				if (event.getEventType() == LdvProjectsEventEvent.EventType.eventMouseUp)
				{
					resetAllDrags() ;
				}
			}
		});
		
		// TimeControlledAreaWindows sends MouseUp in case ProjectWindows could have missed it
		//
		eventBus.addHandler(LdvRedrawProjectRecursiveEvent.TYPE, new LdvRedrawProjectRecursiveEventHandler() 
		{
			@Override
			public void onRedrawProjectRecursiveSend(LdvRedrawProjectRecursiveEvent event) 
			{
				if (event.getTargetZorder() == _iZOrder) {
					redrawRecursive() ;
				}
			}
		});
		
		// Move the top edge
		//
		topEdgeBinder() ;
		
		// Move the bottom edge
		//
		bottomEdgeBinder() ;
		
		// Move the left edge
		//
		leftEdgeBinder() ;
		
		// Move the right edge
		//
		rightEdgeBinder() ;
		
		//Move the project
		//
		titleBarBinder() ;
		
		/**
		 * The tab focus panel has been clicked, we ask to move this project front
		 */
		display.getTabFocusPanel().addMouseDownHandler(new MouseDownHandler()
		{
			@Override
			public void onMouseDown(MouseDownEvent event) {	
				eventBus.fireEvent(new LdvProjectSetZPosEvent(getProjectUri(), LdvProjectSetZPosEvent.ZPosMovement.moveFront)) ;
			}
		});
		
		display.getMouseDownHandler().addMouseDownHandler(new MouseDownHandler()
		{
			@Override
			public void onMouseDown(MouseDownEvent event) {
				Log.info("LdvProjectWindowPresenter Handling MouseDown event") ;
				onWorkspaceMouseDown(event) ;
			}
		});
		
		display.getMouseMoveHandler().addMouseMoveHandler(new MouseMoveHandler()
		{
			@Override
			public void onMouseMove(MouseMoveEvent event) {	
				onWorkspaceMouseMove(event) ;
			}
		});
		
		zoomButtonsBinder() ;
		objectsButtonsBinder() ;
		
		newConcernButtonsBinder() ;
	}

	/** 
	 * Mouse button down inside workspace
	 * 
	 * @param event MouseDownEvent
	 */
	private void onWorkspaceMouseDown(MouseDownEvent event) 
	{
		if (false == display.isPointInsideWorkspace(event.getClientX(), event.getClientY()))
			return ;
		
		if (event.getNativeButton() == NativeEvent.BUTTON_LEFT)
		{
			if (false == _areToolsDisplayed)
			{
				Log.info("LdvProjectWindowPresenter::onWorkspaceMouseDown Will display tools on left button down") ;
				
				_iToolsCenterX = event.getClientX() ;
				_iToolsCenterY = event.getClientY() ;
			
				display.showZoomSlider(event) ;
				_iToolsRadius = display.getContextIconsRadius() ;
			
				_areToolsDisplayed = true ;
			}
			else
				Log.info("LdvProjectWindowPresenter::onWorkspaceMouseDown Tools are already displayed on left button down") ;
		}
	}
	
	/** 
	 * Mouse move inside workspace
	 * 
	 * @param event MouseMoveEvent
	 */
	public void onWorkspaceMouseMove(MouseMoveEvent event) 
	{
		int iPosX = event.getClientX() ;
		int iPosY = event.getClientY() ;
		
		//
		if (_areToolsDisplayed)
		{
			if (getDistance(iPosX, iPosY, _iToolsCenterX, _iToolsCenterY) > _iToolsRadius)
			{
				display.hideZoomSlider() ;
				
				_areToolsDisplayed = false ;
			}
		}
	}
	
	/** 
	 * Initialize all Project components (lines, etc)
	 * 
	 * @param event Event that contains all needed information for initialization
	 * 
	 */
	protected void initComponents(LdvProjectInitEvent event)
	{
		// Check if we are the target of this event
		//
		if (event.getTarget() != this)
			return ;
		
		_projectModel = event.getModelProject() ;
		if (null == _projectModel)
			return ;
		
		Log.info("Initializing components for project " + _projectModel.getProjectUri()) ;
		
		// Add this project display to TimeControlledArea view
		//
		_workspace = (AbsolutePanel) event.getWorkspace() ;
		_workspace.add(display.asWidget()) ;
		
		_timeControlledArea = event.getFather() ;
		
		// Create team rosace
		//
		LdvTeamRosaceView rosaceView = new LdvTeamRosaceView() ;
		_teamRosace = new LdvTeamRosacePresenter(rosaceView, eventBus) ;
		initTeamRosace(_teamRosace) ;
		
		// Create the "now" separator
		//
		LdvNowSeparatorView nowSeparatorView = new LdvNowSeparatorView() ;
		_nowSeparator = new LdvNowSeparatorPresenter(nowSeparatorView, eventBus) ;
		initNowSeparator(_nowSeparator) ;
		
		// Create the "birthday" separator
		//
		LdvBirthSeparatorView birthView = new LdvBirthSeparatorView() ;
		_birthSeparator = new LdvBirthSeparatorPresenter(birthView, eventBus) ;
		initBirthSeparator(_birthSeparator) ;
		
		// Create baseline
		//
		LdvBaseLineView baseLineView = new LdvBaseLineView() ;
		_baseLine = new LdvBaseLinePresenter(baseLineView, eventBus) ;
		_baseLine.setProject(this) ;
		initBaseLine(_baseLine) ;
		
		// Create concern lines
		//
		_concernsModelsArray  = _projectModel.getConcerns() ;
		_documentsModelsArray = _projectModel.getDocuments() ;
		_concernLinesArray    = new ArrayList<LdvConcernLinePresenter>() ;
		ArrayList<LdvModelDocument> lineDocumentArray = new ArrayList<LdvModelDocument>() ;
		
		if ((null != _concernsModelsArray) && (false == _concernsModelsArray.isEmpty()))
		{
			int lineNumber = 0 ;
			for (Iterator<LdvModelConcern> iter = _concernsModelsArray.iterator() ; iter.hasNext() ; ) 
			{
				LdvModelConcern concernModel = iter.next() ;
				
				LdvConcernLineView view = new LdvConcernLineView() ;
				LdvConcernLinePresenter concernLine = new LdvConcernLinePresenter(view, eventBus, _dispatcher, _supervisor) ;
				
				_concernLinesArray.add(concernLine) ;
				 
				// Add the documents
				String ID = concernModel.getID() ;
				 
				if (null != _documentsModelsArray)
				{
					for (Iterator<LdvModelDocument> iter2 = _documentsModelsArray.iterator() ; iter2.hasNext() ; )
					{
						LdvModelDocument document = iter2.next() ;
						String lineID = document.getLineID() ;
						if (lineID.equals(ID))
							lineDocumentArray.add(document) ;
					}
				}
				 				 
				if (null != concernLine)
				{
					initConcernLine(concernLine, concernModel, lineNumber, lineDocumentArray) ;
					lineNumber++ ;
				}
			}
		}
		
		//reset the thumb in TimeControlledArea
		_timeControlledArea.resetThumb() ;
		
		getProjectLabel() ;
		
		initTimerObjects() ;
	}
	
	/** 
	 * Initialize Project's separator for "now"
	 * 
	 * @param nowSeparator Separator between the past and the future
	 * 
	 */
	public void initTeamRosace(final LdvTeamRosacePresenter teamRosace) 
	{			
		if (false == eventBus.isEventHandled(LdvTeamRosaceInitEvent.TYPE))
		{
			if (null == _pendingEvents) 
			{
				_pendingEvents = new ScheduledCommand() 
				{
					public void execute() {
						_pendingEvents = null ;
						initTeamRosace(teamRosace) ;
			     }
				};
				Scheduler.get().scheduleDeferred(_pendingEvents) ;
			}
		}
		else
			eventBus.fireEvent(new LdvTeamRosaceInitEvent(display.getWorkSpacePanel(), teamRosace, this)) ;
	}
	
	/** 
	 * Initialize Project's separator for "now"
	 * 
	 * @param nowSeparator Separator between the past and the future
	 * 
	 */
	public void initNowSeparator(final LdvNowSeparatorPresenter nowSeparator) 
	{			
		if (false == eventBus.isEventHandled(LdvNowSeparatorInitEvent.TYPE))
		{
			if (null == _pendingEvents2) 
			{
				_pendingEvents2 = new ScheduledCommand() 
				{
					public void execute() {
						_pendingEvents2 = null ;
						initNowSeparator(nowSeparator) ;
			     }
				};
				Scheduler.get().scheduleDeferred(_pendingEvents2) ;
			}
		}
		else
			eventBus.fireEvent(new LdvNowSeparatorInitEvent(display.getWorkSpacePanel(), nowSeparator, getNowXPosition(), this)) ;
	}
	
	/** 
	 * Initialize Project's separator for "now"
	 * 
	 * @param birthSeparator Separator located at birth date
	 * 
	 */
	public void initBirthSeparator(final LdvBirthSeparatorPresenter birthSeparator) 
	{			
		if (false == eventBus.isEventHandled(LdvBirthSeparatorInitEvent.TYPE))
		{
			if (null == _pendingEvents3) 
			{
				_pendingEvents3 = new ScheduledCommand() 
				{
					public void execute() {
						_pendingEvents3 = null ;
						initBirthSeparator(birthSeparator) ;
			     }
				};
				Scheduler.get().scheduleDeferred(_pendingEvents3) ;
			}
		}
		else
			eventBus.fireEvent(new LdvBirthSeparatorInitEvent(display.getWorkSpacePanel(), birthSeparator, getBirthXPosition(), this)) ;
	}
	
	/** 
	 * Initialize Project's baseline
	 * 
	 * @param baseLine Base line presenter
	 * 
	 */
	public void initBaseLine(final LdvBaseLinePresenter baseLine) 
	{			
		if (false == eventBus.isEventHandled(LdvBaseLineInitEvent.TYPE))
		{
			if (null == _pendingEvents4) 
			{
				_pendingEvents4 = new ScheduledCommand() 
				{
					public void execute() {
						_pendingEvents4 = null ;
						initBaseLine(baseLine) ;
			     }
				};
				Scheduler.get().scheduleDeferred(_pendingEvents4) ;
			}
		}
		else
			eventBus.fireEvent(new LdvBaseLineInitEvent(display.getBaseLinePanel(), baseLine, this)) ;
	}
	
	/** 
	 * Initialize a Concern line
	 * 
	 * @param concernLine       Concern line presenter
	 * @param concernModel      Concern information
	 * @param lineDocumentArray Concern related documents
	 * 
	 */
	public void initConcernLine(final LdvConcernLinePresenter concernLine, final LdvModelConcern concernModel, final int numberLine, final ArrayList<LdvModelDocument> lineDocumentArray) 
	{	
		if (false == eventBus.isEventHandled(LdvConcernLineInitEvent.TYPE))
		{
			// if (null == _pendingEvents5) 
			// {
				_pendingEvents5 = new ScheduledCommand() 
				{
					public void execute() {
						_pendingEvents5 = null ;
						initConcernLine(concernLine, concernModel, numberLine, lineDocumentArray) ;
			     }
				};
				Scheduler.get().scheduleDeferred(_pendingEvents5) ;
			// }
		}
		else
			eventBus.fireEvent(new LdvConcernLineInitEvent(display.getWorkSpacePanel(), concernLine, this, concernModel, numberLine, lineDocumentArray)) ;
	}

	/**
	 * Redraw this project and ask the new one to redraw
	 */
	public void redrawRecursive()
	{
		setZOrder(_iZOrder) ;
		
		display.setBottomForZorder(_iZOrder, _timeControlledArea.getProjectsCount(), _timeControlledArea.getProjectsAreaBottom()) ;
		
		redrawComponents() ;
		
		if (1 != _iZOrder)
			eventBus.fireEvent(new LdvRedrawProjectRecursiveEvent(_iZOrder - 1)) ;
	}
	
	public void redrawComponents(LdvRedrawProjectWindowEvent event)
	{	
		if (event.getTarget() != this)
			return ;
		
		redrawComponents() ;		
	}
	
	/**
	 * Redraw all internal components (concern lines, etc)
	 */
	public void redrawComponents()
	{	
		// display.getWorkSpacePanel().clear() ;
		
		//redraw the baseline
/*		if (null == _baseLine)
		{	
			return ;
		}
		eventBus.fireEvent(new LdvBaseLineInitEvent(display.getBaseLinePanel(), _baseLine, this)) ;
*/
		
		redrawConcernLines() ;		
	}
	
	/**
	 * Redraw all concern lines (clears workspace and sends LdvRedrawAllConcernLinesEvent)
	 */
	public void redrawConcernLines()
	{
		if (_concernLinesArray.isEmpty())
			return ;
		
		// Clear workspace
		//
		display.getWorkSpacePanel().clear() ;
		
		// Redraw the "now" separator
		//
		drawNowSeparator() ;
		
		// Redraw the "now" separator
		//
		drawBirthSeparator() ;
		
		// This event is handled by LdvConcernLinePresenter and signal that redrawing is needed
		//
		eventBus.fireEvent(new LdvRedrawAllConcernLinesEvent(this)) ;
	}
	
	public void setTimeControlledArea(LdvTimeControlledAreaPresenter timeControlledArea) {
		_timeControlledArea = timeControlledArea ;
	}
	
	/**
	 * Returns X value (from left) inside project from browser's client area X value
	 * 
	 * @param iBrowserClientX X value inside browser's client area
	 * @return X value inside project (from left)
	 */
	public int getInternalXfromBrowserClientX(int iBrowserClientX)
	{			
		int iAbsoluteLeft = display.getMainPanel().getAbsoluteLeft() ;
		return iBrowserClientX - iAbsoluteLeft ;
	}
	
	/**
	 * Returns the position of a date from the left side of the Project window in pixel
	 * i.e. get left position of a date inside the window 
	 * 
	 * @param ldvTime date to get left position of inside window
	 * @return pixels count as <code>int</code>
	 */
	public int getInternalPhysicalPosition(LdvTime ldvTime)
	{			
		// if (ldvTime.isNoLimit())
		//	return Integer.MAX_VALUE ;
		
		// Get X position inside time controlled area
		int iXPosInTimeControlledArea = _timeControlledArea.getInternalPhysicalPosition(ldvTime) ;
		
		// Get offset of project inside time controlled area
		int iLeftOffsetForProject     = _timeControlledArea.getProjectLeftOffset(this) ;
		
		return (iXPosInTimeControlledArea - iLeftOffsetForProject) ;
	}
	
	/**
	 * Get the LdvTime from the position (from left) inside project
	 * 
	 * @param  iX position (from left) inside project
	 * @return LdvTime of this position
	 */
	public LdvTime getLdvTimeFromPosition(int iX)
	{	
		int iLeftProjectFromRightArea = _timeControlledArea.getProjectBeginPositionFromRight(this) ;
		return _timeControlledArea.getLdvTimeFromPosition(iLeftProjectFromRightArea - iX) ;
	}

	public boolean IsBound() {                       
		return _isBound ; 
	}
	
	public boolean getIsCurrentProject(){
		return this._isCurrentProject ;
	}
	
	public void setIsCurrentProject(boolean isCurrentProject){
		this._isCurrentProject = isCurrentProject ;
	}
	
	public double getProjectScrollAreaWidthRatio(){
		return this._projectScrollAreaWidthRatio ;
	}
	
	public void setProjectScrollAreaWidthRatio(double ratio){
		this._projectScrollAreaWidthRatio = ratio ;
	}
	
	public void getProjectLabel()
	{
		if (null == _projectModel)
			return ;
		
		String sProjectType = _projectModel.getProjectType() ;
		if (sProjectType.equals(""))
			return ;
		
		// Get lexicon label
		//
		Lexicon lexicon = _supervisor.getLexiconFromCode(sProjectType) ;
		if (null != lexicon)
		{
			setNewTitle(lexicon.getDisplayLabel(Lexicon.Declination.singular, _supervisor.getUserLanguage())) ;
			return ;
		}
		
		_dispatcher.execute(new GetLexiconAction(sProjectType, ""), new GetLexiconCallback());
	}
	
	public String getProjectUri()
	{
		if (null == _projectModel)
			return "" ;
		
		return _projectModel.getProjectUri() ;
	}
	
	public int getZOrder() {
		return _iZOrder ;
	}
	public void setZOrder(int iZOrder) 
	{
		_iZOrder = iZOrder ;
		
		if (IsBound())
			display.setZorder(100 - iZOrder) ;
	}
	
	/**
	*  Asynchronous callback function for calls to GetLexiconFromCodeHandler
	**/
	public class GetLexiconCallback implements AsyncCallback<GetLexiconResult> 
	{
		public GetLexiconCallback() {
			super();
		}

		@Override
		public void onFailure(final Throwable cause)
		{
			Log.error("Handle Failure:", cause) ;    				
		}

		@Override
		public void onSuccess(final GetLexiconResult result)
		{
			// take the result from the server
			if (true == result.wasSuccessful())
			{
				Lexicon lexicon = result.getLexicon() ;
				if (null != lexicon)
				{
					_supervisor.addLexicon(lexicon) ;					
					setNewTitle(lexicon.getDisplayLabel(Lexicon.Declination.singular, _supervisor.getUserLanguage())) ;
				}
			}
		}
	}
	
	private void setNewTitle(String sNewTitle)
	{
		_sProjectString = sNewTitle ;
		
		display.setProjectTitle(_sProjectString) ;
	}
	
	/**
	*  Reset all booleans that indicate an ongoing drag
	**/
	private void resetAllDrags()
	{
		_onTopDrag    = false ;
		_onBottomDrag = false ;
		_onLeftDrag   = false ;
		_onRightDrag  = false ;
		_onMoveDrag   = false ;	
	}

	/**
	*  Start a global move operation (when dragging title bar)
	*  
	*  @param event MouseDownEvent that initiates the dragging operation
	**/
	public void startMove(final MouseDownEvent event)
	{
		if (null == event)
			return ;
		
		_projectHeight = display.getMainPanel().getOffsetHeight() ;
		_projectWidth = display.getMainPanel().getOffsetWidth() ;
		_startRelativeX = event.getX() ;
		_startRelativeY = event.getY() ;
		_projectLeft = display.getMainPanel().getElement().getOffsetLeft() ;
		_projectTop = display.getMainPanel().getElement().getOffsetTop() ;
	}
	
	public void mouseMoveOverTitleBar(final MouseMoveEvent event)
	{
		if (true == _onMoveDrag)
		{	
			int relativeX = event.getX() ;
			int relativeY = event.getY() ;
			
			int deltaX = relativeX - _startRelativeX ;
			int deltaY = relativeY - _startRelativeY ;
			
			display.getMainPanel().getElement().getStyle().setLeft((_projectLeft + deltaX), Style.Unit.PX) ;
			display.getMainPanel().getElement().getStyle().setTop((_projectTop + deltaY), Style.Unit.PX) ;
			String strHeight = Integer.toString(_projectHeight) + "px" ;
			String strWidth = Integer.toString(_projectWidth) + "px" ;
			display.getMainPanel().setHeight(strHeight) ;
			display.getMainPanel().setWidth(strWidth) ;
			
		  //redraw the concern lines
			display.getWorkSpacePanel().clear() ;
			redrawConcernLines() ;		
		}		
	}
	
	/**
	*  Bind handlers for Top Edge control 
	**/
	private void topEdgeBinder()
	{
		display.getTopFocusPanel().addMouseDownHandler(new MouseDownHandler()
		{
			@Override
			public void onMouseDown(final MouseDownEvent event)
			{
				_onTopDrag = true ;
				_startRelativeY = event.getRelativeY(_timeControlledArea.getDisplay().getMainPanel().getElement()) ;
			  _projectTop = display.getMainPanel().getElement().getOffsetTop() ;
			}
		});
		
		display.getTopFocusPanel().addMouseUpHandler(new MouseUpHandler()
		{
			@Override
			public void onMouseUp(final MouseUpEvent event){
				_onTopDrag = false ;	
			}
		});
		
		display.getTopFocusPanel().addMouseMoveHandler(new MouseMoveHandler()
		{		
			@Override
			public void onMouseMove(MouseMoveEvent event)
			{	
				if (true == _onTopDrag)
				{
					int relativeY = event.getRelativeY(_timeControlledArea.getDisplay().getMainPanel().getElement()) ;
					int deltaDrag = relativeY - _startRelativeY ;
					int newTop = _projectTop + deltaDrag ;
					display.setTop(newTop) ;
					redrawComponents() ;
				}
			}
		});
	}
	
	/**
	*  Bind handlers for Bottom Edge control 
	**/
	private void bottomEdgeBinder()
	{
		display.getBottomFocusPanel().addMouseDownHandler(new MouseDownHandler()
		{
			@Override
			public void onMouseDown(final MouseDownEvent event)
			{
				_onBottomDrag = true ;
				_startRelativeY = event.getRelativeY(_timeControlledArea.getDisplay().getMainPanel().getElement()) ;
				int projectAbsoluteBottom = display.getMainPanel().getElement().getAbsoluteBottom() ;
				int controlledAreaBottom = _timeControlledArea.getDisplay().getMainPanel().getElement().getAbsoluteBottom() ;
				_projectBottom = controlledAreaBottom - projectAbsoluteBottom ;	
			}
		});
		
		display.getBottomFocusPanel().addMouseUpHandler(new MouseUpHandler(){
			@Override
			public void onMouseUp(final MouseUpEvent event){
				_onBottomDrag = false ;	
			}
		});
		
		display.getBottomFocusPanel().addMouseMoveHandler(new MouseMoveHandler()
		{		
			@Override
			public void onMouseMove(MouseMoveEvent event)
			{	
				if (true == _onBottomDrag)
				{
					int relativeY = event.getRelativeY(_timeControlledArea.getDisplay().getMainPanel().getElement()) ;
					int deltaDrag = relativeY - _startRelativeY ;
					int newBottom = _projectBottom - deltaDrag ;
					display.setBottom(newBottom) ;
					redrawComponents() ;
				}				
			}
		});
	}
	
	/**
	*  Bind handlers for Left Edge control 
	**/
	private void leftEdgeBinder()
	{
		display.getLeftFocusPanel().addMouseDownHandler(new MouseDownHandler(){
			@Override
			public void onMouseDown(final MouseDownEvent event)
			{
				_onLeftDrag = true ;
				_startRelativeX = event.getRelativeX(_timeControlledArea.getDisplay().getMainPanel().getElement()) ;
				_projectLeft = display.getMainPanel().getElement().getOffsetLeft() ;				
			}
		});
		
		display.getLeftFocusPanel().addMouseUpHandler(new MouseUpHandler(){
			@Override
			public void onMouseUp(final MouseUpEvent event){
				_onLeftDrag = false ;	
			}
		});
		
		display.getLeftFocusPanel().addMouseMoveHandler(new MouseMoveHandler(){		
			@Override
			public void onMouseMove(MouseMoveEvent event)
			{	
				if (true == _onLeftDrag)
				{
					int relativeX = event.getRelativeX(_timeControlledArea.getDisplay().getMainPanel().getElement()) ;
					int deltaDrag = relativeX - _startRelativeX ;
					int newLeft = _projectLeft + deltaDrag ;
					display.setLeft(newLeft) ;
					
					//redraw the concern lines
					display.getWorkSpacePanel().clear() ;
					redrawComponents() ;
				}				
			}
		});
	}
	
	/**
	*  Bind handlers for Right Edge control 
	**/
	private void rightEdgeBinder()
	{
		display.getRightFocusPanel().addMouseDownHandler(new MouseDownHandler()
		{
			@Override
			public void onMouseDown(final MouseDownEvent event)
			{
				_onRightDrag = true ;
				_startRelativeX = event.getRelativeX(_timeControlledArea.getDisplay().getMainPanel().getElement()) ;
				int projectAbsoluteRight = display.getMainPanel().getElement().getAbsoluteRight() ;
				int ControlledAreaRight = _timeControlledArea.getDisplay().getMainPanel().getElement().getAbsoluteRight() ;
				_projectRight = ControlledAreaRight - projectAbsoluteRight ;		
			}
		});		
		
		display.getRightFocusPanel().addMouseUpHandler(new MouseUpHandler()
		{
			@Override
			public void onMouseUp(final MouseUpEvent event)
			{
				_onRightDrag = false ;	
			}
		});
		
		display.getRightFocusPanel().addMouseMoveHandler(new MouseMoveHandler()
		{
			@Override
			public void onMouseMove(MouseMoveEvent event)
			{	
				if (true == _onRightDrag)
				{
					int relativeX = event.getRelativeX(_timeControlledArea.getDisplay().getMainPanel().getElement()) ;
					int deltaDrag = relativeX - _startRelativeX ;
					int newRight = _projectRight - deltaDrag ;
					display.setRight(newRight) ;
					redrawComponents() ;
				}				
			}
		});
	}
	
	/**
	*  Bind handlers for Title Bar control 
	**/
	private void titleBarBinder()
	{
		display.getBarFocusPanel().addMouseDownHandler(new MouseDownHandler()
		{
			@Override
			public void onMouseDown(final MouseDownEvent event) { 
				startMove(event) ;
			}
		});		
		
		display.getBarFocusPanel().addMouseUpHandler(new MouseUpHandler()
		{
			@Override
			public void onMouseUp(final MouseUpEvent event) {
				_onMoveDrag = false ;	
			}
		});
		
		display.getBarFocusPanel().addMouseMoveHandler(new MouseMoveHandler()
		{
			@Override
			public void onMouseMove(MouseMoveEvent event) {	
				mouseMoveOverTitleBar(event) ;				
			}
		});
	}

	/**
	*  Bind handlers for Zoom control buttons 
	**/
	private void zoomButtonsBinder()
	{
		display.getZoomPlusClickHandler().addClickHandler(new ClickHandler()
		{
			@Override
			public void onClick(ClickEvent event) {	
				changePixUnit(LdvTimeZoomLevel.pixUnit.pixMinute) ;
			}
		});
		
		display.getZoomSmallPlusClickHandler().addClickHandler(new ClickHandler()
		{
			@Override
			public void onClick(ClickEvent event) {	
				changePixUnit(LdvTimeZoomLevel.pixUnit.pixHour) ;
			}
		});
		
		display.getZoomNoneClickHandler().addClickHandler(new ClickHandler()
		{
			@Override
			public void onClick(ClickEvent event) {	
				changePixUnit(LdvTimeZoomLevel.pixUnit.pixDay) ;
			}
		});
		
		display.getZoomSmallMinusClickHandler().addClickHandler(new ClickHandler()
		{
			@Override
			public void onClick(ClickEvent event) {
				changePixUnit(LdvTimeZoomLevel.pixUnit.pixWeek) ;
			}
		});
		
		display.getZoomMinusClickHandler().addClickHandler(new ClickHandler()
		{
			@Override
			public void onClick(ClickEvent event) {	
				changePixUnit(LdvTimeZoomLevel.pixUnit.pixMonth) ;
			}
		});		
	}
	
	/**
	*  Ask  TimeControlledArea to change pix unit
	*   
	**/
	private void changePixUnit(LdvTimeZoomLevel.pixUnit pixUnit)
	{
		display.hideZoomSlider() ;
		_areToolsDisplayed = false ;
		
		_timeControlledArea.changePixUnit(_iToolsCenterX, pixUnit) ;
	}
	
	/**
	*  Bind handlers for Objects control buttons 
	**/
	private void objectsButtonsBinder()
	{
		display.getNewLineClickHandler().addClickHandler(new ClickHandler()
		{
			@Override
			public void onClick(ClickEvent event) {
				LdvTime clickedTime = getLdvTimeFromPosition(_iToolsCenterX) ;
				display.hideZoomSlider() ;
				display.showNewConcernDialog(clickedTime.toJavaDate()) ;
			}
		});
	}
	
	private void newConcernButtonsBinder()
	{
		display.getNewConcernDialogCancelHandler().addClickHandler(new ClickHandler()
		{
			@Override
			public void onClick(ClickEvent event) {	
				display.hideNewConcernDialog() ;
			}
		});
		
		display.getNewConcernDialogOkClickHandler().addClickHandler(new ClickHandler()
		{
			@Override
			public void onClick(ClickEvent event) {	
				display.hideNewConcernDialog() ;
			}
		});
	}
	
	private void initTimerObjects()
	{
		updateTimerObjects() ;
		
		// Create a new timer that calls Wi
    Timer t = new Timer() {
      @Override
      public void run() {
      	updateTimerObjects() ;
      }
    };

    // Schedule the timer
    t.schedule(_iTimerInterval) ;
	}
	
	private void updateTimerObjects()
	{
		int iLeftPosForNow = getNowXPosition() ;
		
		if (null != _nowSeparator)
			_nowSeparator.setXPos(iLeftPosForNow) ;
	}
	
	public void drawNowSeparator() {	
		_nowSeparator.draw(display.getWorkSpacePanel(), getNowXPosition()) ;
	}
	
	public void drawBirthSeparator() {	
		_birthSeparator.draw(display.getWorkSpacePanel(), getBirthXPosition()) ;
	}
	
	private LdvPoint getPointForClickEvent(ClickEvent event) 
	{
		if (null == event)
			return (LdvPoint) null ;
		
		LdvPoint clickEventPoint = new LdvPoint() ;
		
		int iClickCenterX = display.getClickEventRelativeX(event) ;
		LdvTime clickLdvTime = getLdvTimeFromPosition(iClickCenterX) ;
		
		int iClickCenterY = display.getClickEventRelativeY(event) ;
		
		clickEventPoint.setY(iClickCenterY) ;
		clickEventPoint.setX(clickLdvTime) ;
		
		return clickEventPoint ;
	}
	
	private int getDistance(int pt1_X, int pt1_Y, int pt2_X, int pt2_Y) {
		return (int) Math.sqrt(Math.pow(pt2_X - pt1_X, 2) + Math.pow(pt2_Y - pt1_Y, 2)) ;
	}
	
	private int getNowXPosition()
	{
		LdvTime TimeNow = new LdvTime(0) ;
		TimeNow.takeTime() ;
		return getInternalPhysicalPosition(TimeNow) ;
	}
	
	private int getBirthXPosition()
	{
		LdvGraphManager displayedGraph = _supervisor.getDisplayedGraph() ;
		if (null == displayedGraph)
			return -1 ;
		
		LdvModelDemographics demographics = displayedGraph.getDemographics() ; 
		if (null == demographics)
			return -1 ;
		
		LdvTime BirthDate = demographics.getBirthDate() ;
	
		return getInternalPhysicalPosition(BirthDate) ;
	}

	public LdvModelRosace getRosace() 
	{
		if (null == _projectModel)
			return null ;
		
		return _projectModel.getRosace() ;
	}
	
	public LdvModelTeam getTeam() 
	{
		if (null == _projectModel)
			return null ;
		
		return _projectModel.getTeam() ;
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
