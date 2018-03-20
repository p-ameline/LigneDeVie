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
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TextBox;
import com.google.inject.Inject;

import com.ldv.client.canvas.LdvProjectTab;
import com.ldv.client.event.LdvConcernLineInitEvent;
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
import com.ldv.client.widgets.LdvDateBox;
import com.ldv.client.widgets.LexiqueTextBox;
import com.ldv.client.widgets.LexiqueTextBoxManager;
import com.ldv.shared.database.Lexicon;
import com.ldv.shared.graph.LdvGraphConfig;
import com.ldv.shared.graph.LdvModelGraph;
import com.ldv.shared.graph.LdvModelTree;
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
	private ScheduledCommand 							     _initComponentsEvent  = null ;
	private AbsolutePanel 								     _workspace ;
	private LdvTeamRosacePresenter 			       _teamRosace           = null ;
	private LdvNowSeparatorPresenter 					 _nowSeparator         = null ;
	private LdvBirthSeparatorPresenter 				 _birthSeparator       = null ;
	private LdvBaseLinePresenter 					     _baseLine             = null ;
	private ArrayList<LdvModelConcern> 		     _concernsModelsArray  = null ; 
	private ArrayList<LdvModelDocument>		     _documentsModelsArray = null ;	
	private ArrayList<LdvConcernLinePresenter> _concernLinesArray    = null ;
	private boolean                            _bMouseCaptured ;
	
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
  
  private LexiqueTextBoxManager              _lexiqueTextBoxManager ;
  
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
		public AbsolutePanel        getMainTimeControlledPanel() ;
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
		
		public HorizontalPanel      getMainPanel() ;
		public void                 addTeamRosaceView(LdvTeamRosaceView teamRosaceView) ;
		
		public void                 showNewConcernDialog(java.util.Date clickDate) ;
		public void                 hideNewConcernDialog() ;
		public HasClickHandlers     getNewConcernDialogOkClickHandler() ;
		public HasClickHandlers     getNewConcernDialogCancelHandler() ;
		public TextBox              getNewConcernLabelTextBox() ;
		public LexiqueTextBox       getNewConceptTextBox() ;
		public RadioButton          getEndingDateRadioButton() ;
		public LdvDateBox           getNewConcernEndingDateBox() ;
		public LdvDateBox           getNewConcernStartingDateBox() ;
		
		public void                 popupWarningMessage(String sMessage) ;
		public void                 closeWarningDialog() ;
		public HasClickHandlers     getWarningOk() ;
		
		public boolean              isPointInsideWorkspace(int iX, int iY) ;
		
		public HasClickHandlers     getShowRosaceClickHandler() ;
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
		
		_lexiqueTextBoxManager = new LexiqueTextBoxManager() ;
		
		_bMouseCaptured = false ;
		
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
		
		/**
		 * Mouse down
		 */
		display.getMouseDownHandler().addMouseDownHandler(new MouseDownHandler()
		{
			@Override
			public void onMouseDown(MouseDownEvent event) {
				// Log.info("LdvProjectWindowPresenter Handling MouseDown event") ;
				onWorkspaceMouseDown(event) ;
			}
		});
		
		/**
		 * Mouse move
		 */
		display.getMouseMoveHandler().addMouseMoveHandler(new MouseMoveHandler()
		{
			@Override
			public void onMouseMove(MouseMoveEvent event) {	
				onWorkspaceMouseMove(event) ;
			}
		});
		
		display.getWarningOk().addClickHandler(new ClickHandler()
		{
			@Override
			public void onClick(ClickEvent event) {	
				display.closeWarningDialog() ;
			}
		});
		
		display.getShowRosaceClickHandler().addClickHandler(new ClickHandler()
		{
			@Override
			public void onClick(ClickEvent event) {	
				showRosace() ;
			}
		});
		
		zoomButtonsBinder() ;
		objectsButtonsBinder() ;
		
		newConcernButtonsBinder() ;
		
		connectLexiqueTextBoxes() ;
	}
	
	/**
	 * Connect the Lexique powered text boxes to the Lexique query manager
	 */
	private void connectLexiqueTextBoxes()
	{
		// Text entered in the "change concept" text box changed. Refresh proposed list.
		//
		display.getNewConceptTextBox().addKeyUpHandler(new KeyUpHandler() {
			public void onKeyUp(KeyUpEvent event) {
				TermChanged(event) ;
			}
		});
		
		_lexiqueTextBoxManager.addLexiqueTextBoxToBuffer(display.getNewConceptTextBox(), _supervisor.getUserLanguage()) ;
	}
	
	private void TermChanged(KeyUpEvent event) 
	{
		boolean bMustRefresh = display.getNewConceptTextBox().processKeyUp(event) ;
		if (bMustRefresh)
			_lexiqueTextBoxManager.initLexiqueBoxList(display.getNewConceptTextBox(), _supervisor.getUserLdvId(), _dispatcher) ;
	}

	/** 
	 * Mouse button down inside workspace
	 * 
	 * @param event MouseDownEvent
	 */
	private void onWorkspaceMouseDown(MouseDownEvent event) 
	{
		if (_bMouseCaptured)
		{
			_bMouseCaptured = false ;
			return ;
		}
		
		if (null == event)
			return ;
		
		int iX = event.getClientX() ;
		int iY = event.getClientY() ;
		
		// If the Rosace is visible, is the click inside it
		//
		if ((null != _teamRosace) && _teamRosace.isVisible() && _teamRosace.isPointInsideRosace(iX, iY))
			return ;
		
		// Is the click inside the project control bar
		//
		
		
		if (false == display.isPointInsideWorkspace(iX, iY))
			return ;
		
		if (event.getNativeButton() == NativeEvent.BUTTON_LEFT)
		{
			if (false == _areToolsDisplayed)
			{
				// Log.info("LdvProjectWindowPresenter::onWorkspaceMouseDown Will display tools on left button down") ;
				
				_iToolsCenterX = iX ;
				_iToolsCenterY = iY ;
			
				display.showZoomSlider(event) ;
				_iToolsRadius = display.getContextIconsRadius() ;
			
				_areToolsDisplayed = true ;
			}
			// else
			// 	Log.info("LdvProjectWindowPresenter::onWorkspaceMouseDown Tools are already displayed on left button down") ;
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
		
		// Log.info("Initializing components for project " + _projectModel.getProjectUri()) ;
		
		// Add this project display to TimeControlledArea view
		//
		_workspace = (AbsolutePanel) event.getWorkspace() ;
		_workspace.add(display.asWidget()) ;
		
		_timeControlledArea = event.getFather() ;
		
		// Create team rosace
		//
		LdvTeamRosaceView rosaceView = new LdvTeamRosaceView() ;
		_teamRosace = new LdvTeamRosacePresenter(this, rosaceView, eventBus) ;
		_teamRosace.setVisible(true) ;
		_teamRosace.updateZOrder() ;
		display.addTeamRosaceView(rosaceView) ;
		
		// Create the "now" separator
		//
		LdvNowSeparatorView nowSeparatorView = new LdvNowSeparatorView() ;
		_nowSeparator = new LdvNowSeparatorPresenter(this, nowSeparatorView, eventBus) ;
		
		// Create the "birthday" separator
		//
		LdvBirthSeparatorView birthView = new LdvBirthSeparatorView() ;
		_birthSeparator = new LdvBirthSeparatorPresenter(this, birthView, eventBus) ;
		
		// Create baseline
		//
		LdvBaseLineView baseLineView = new LdvBaseLineView() ;
		_baseLine = new LdvBaseLinePresenter(this, baseLineView, eventBus) ;
		
		// Create concern lines
		//
		_concernsModelsArray  = _projectModel.getConcerns() ;
		_documentsModelsArray = _projectModel.getDocuments() ;
		_concernLinesArray    = new ArrayList<LdvConcernLinePresenter>() ;
		
		if ((null != _concernsModelsArray) && (false == _concernsModelsArray.isEmpty()))
		{
			int lineNumber = 0 ;
			for (Iterator<LdvModelConcern> iter = _concernsModelsArray.iterator() ; iter.hasNext() ; ) 
			{
				LdvModelConcern concernModel = iter.next() ;
				
				// Create line's MVP objects
				//
				LdvConcernLineView view = new LdvConcernLineView() ;
				LdvConcernLinePresenter concernLine = new LdvConcernLinePresenter(this, view, eventBus, _dispatcher, _supervisor) ;
				
				_concernLinesArray.add(concernLine) ;
				 
				// Add the documents
				String ID = concernModel.getID() ;
				
				ArrayList<LdvModelDocument> lineDocumentArray = new ArrayList<LdvModelDocument>() ;
				
				if ((null != _documentsModelsArray) && (false == _documentsModelsArray.isEmpty()))
				{
					for (Iterator<LdvModelDocument> iterDoc = _documentsModelsArray.iterator() ; iterDoc.hasNext() ; )
					{
						LdvModelDocument document = iterDoc.next() ;
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
		
		_initComponentsEvent = new ScheduledCommand() 
		{
			public void execute() {
				_initComponentsEvent = null ;
				initComponents() ;
	     }
		};
		Scheduler.get().scheduleDeferred(_initComponentsEvent) ;
	}
	
	protected void initComponents()
	{
		_teamRosace.initComponents(display.getMainPanel()) ;
		_baseLine.connectToProject(display.getBaseLinePanel()) ;
		_nowSeparator.draw(display.getWorkSpacePanel(), getNowXPosition()) ;
		_birthSeparator.draw(display.getWorkSpacePanel(), getBirthXPosition()) ;
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
		
		if (null != _teamRosace)
			_teamRosace.updateZOrder() ;
		
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
		int iAbsoluteLeft = display.getMainTimeControlledPanel().getAbsoluteLeft() ;
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
		
		_dispatcher.execute(new GetLexiconAction(sProjectType, _supervisor.getUserLanguage(), ""), new GetLexiconCallback());
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
			display.setZorder(getZOrderForDisplay()) ;
		
		if (null != _teamRosace)
			_teamRosace.updateZOrder() ;
	}
	
	/**
	 * Get the z-index to set display at
	 * 
	 * @return
	 */
	public int getZOrderForDisplay() {
		return 100 - _iZOrder ;
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
		
		AbsolutePanel mainTimeControlledPanel = display.getMainTimeControlledPanel() ;
		if (null == mainTimeControlledPanel)
			return ;
		
		_projectHeight  = mainTimeControlledPanel.getOffsetHeight() ;
		_projectWidth   = mainTimeControlledPanel.getOffsetWidth() ;
		_startRelativeX = event.getX() ;
		_startRelativeY = event.getY() ;
		_projectLeft    = mainTimeControlledPanel.getElement().getOffsetLeft() ;
		_projectTop     = mainTimeControlledPanel.getElement().getOffsetTop() ;
	}
	
	public void mouseMoveOverTitleBar(final MouseMoveEvent event)
	{
		if (true == _onMoveDrag)
		{	
			int relativeX = event.getX() ;
			int relativeY = event.getY() ;
			
			int deltaX = relativeX - _startRelativeX ;
			int deltaY = relativeY - _startRelativeY ;
			
			AbsolutePanel mainTimeControlledPanel = display.getMainTimeControlledPanel() ;
			if (null == mainTimeControlledPanel)
				return ;
			
			mainTimeControlledPanel.getElement().getStyle().setLeft((_projectLeft + deltaX), Style.Unit.PX) ;
			mainTimeControlledPanel.getElement().getStyle().setTop((_projectTop + deltaY), Style.Unit.PX) ;
			String strHeight = Integer.toString(_projectHeight) + "px" ;
			String strWidth = Integer.toString(_projectWidth) + "px" ;
			mainTimeControlledPanel.setHeight(strHeight) ;
			mainTimeControlledPanel.setWidth(strWidth) ;
			
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
			  _projectTop = display.getMainTimeControlledPanel().getElement().getOffsetTop() ;
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
				int projectAbsoluteBottom = display.getMainTimeControlledPanel().getElement().getAbsoluteBottom() ;
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
				_projectLeft = display.getMainTimeControlledPanel().getElement().getOffsetLeft() ;				
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
				int projectAbsoluteRight = display.getMainTimeControlledPanel().getElement().getAbsoluteRight() ;
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
				validateNewConcern() ;
			}
		});
		
		/**
		 * Enable ending date for new concern
		 * */
		display.getEndingDateRadioButton().addValueChangeHandler(new ValueChangeHandler<java.lang.Boolean>(){
			public void onValueChange(final ValueChangeEvent<java.lang.Boolean> event)
			{
				java.lang.Boolean bValueAboutToBeSet = event.getValue() ;
			  if (true == bValueAboutToBeSet)
			  	enableNewConcernEndingDate(true) ; 
			  else
			  	enableNewConcernEndingDate(false) ;
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
	
	/**
	 * The button "ending date" in the "new concern" dialog box is about to change state
	 * 
	 * @param bValueAboutToBeSet New state to be set
	 */
	protected void enableNewConcernEndingDate(boolean bValueAboutToBeSet)
	{
		// The button "ending date" is about to be set
		// 
		if (bValueAboutToBeSet)
		{
			LdvTime tNewConcernStartingDate = display.getNewConcernStartingDateBox().getLdvTime() ;
			
			if (null == tNewConcernStartingDate)
				return ;
			
			display.getNewConcernEndingDateBox().setLdvTime(tNewConcernStartingDate) ;
		}
	}
	
	protected void validateNewConcern()
	{
		// Check if all needed information were entered
		//
		String sConcernLabel   = display.getNewConcernLabelTextBox().getValue() ;
		String sConcernConcept = display.getNewConceptTextBox().getCode() ;
		
		if (("".equals(sConcernLabel)) && ("".equals(sConcernConcept)))
		{
			display.popupWarningMessage("ERROR_NEWCONCERN_NOLABEL") ;
			return ;
		}
		
		LdvTime tConcernStartDate = display.getNewConcernStartingDateBox().getLdvTime() ;
		
		if ((null == tConcernStartDate) || tConcernStartDate.isEmpty() || tConcernStartDate.isNoLimit())
		{
			display.popupWarningMessage("ERROR_NEWCONCERN_NOSTARTINGDATE") ;
			return ;
		}
		
		LdvTime tConcernEndingDate = null ; 
		boolean bIsThereAnEndingDate = display.getEndingDateRadioButton().getValue() ;
		if (bIsThereAnEndingDate)
		{
			tConcernEndingDate = display.getNewConcernEndingDateBox().getLdvTime() ;
			
			if ((null == tConcernEndingDate) || tConcernEndingDate.isEmpty() || tConcernEndingDate.isNoLimit())
			{
				display.popupWarningMessage("ERROR_NEWCONCERN_NOENDINGDATE") ;
				return ;
			}
			if (tConcernEndingDate.isBefore(tConcernStartDate) || tConcernEndingDate.equals(tConcernStartDate))
			{
				display.popupWarningMessage("ERROR_NEWCONCERN_BEGINAFTERENDING") ;
				return ;
			}
		}
		
		display.hideNewConcernDialog() ;
		
		saveNewConcern(sConcernLabel, sConcernConcept, tConcernStartDate, tConcernEndingDate) ;
	}
	
	protected void saveNewConcern(final String sConcernLabel, final String sConcernConcept, final LdvTime tConcernStartDate, final LdvTime tConcernEndingDate)
	{
		LdvModelConcern newConcern = new LdvModelConcern() ;
		
		newConcern.setLexicon(sConcernConcept) ;
		newConcern.setTitle(sConcernLabel) ;
		newConcern.setBeginDate(tConcernStartDate) ;
		newConcern.setEndDate(tConcernEndingDate) ;
		
		// Ask the graph manager to add the new concern to the proper tree
		// 
		LdvGraphManager graphManager = _timeControlledArea.getGraphManager() ;
		String sRootNode = graphManager.insertNewConcern(newConcern, _projectModel) ;
		
		if ((null == sRootNode) || (sRootNode.length() < LdvGraphConfig.OBJECT_ID_LEN))
			return ;
		
		// Get the tree the new concern was inserted in, in order to ask the server to save the modifications 
		//
		String sDocId = sRootNode.substring(0, LdvGraphConfig.OBJECT_ID_LEN) ;
		if ("".equals(sDocId))
			return ;
		
		// Get the tree
		//
		LdvModelTree modifiedTree = graphManager.getTree(sDocId) ;
		if (null == modifiedTree)
			return ;
		
		// Create a graph to send to the server
		//
		LdvModelGraph saveCapsule = new LdvModelGraph() ;
		saveCapsule.addTree(modifiedTree, "", sDocId) ;

		_timeControlledArea.saveGraph(saveCapsule) ;		
	}

	/**
	 * Show the Rosace window
	 */
	protected void showRosace()
	{
		if (_teamRosace.isVisible())
			return ;
		
		_teamRosace.setVisible(true) ;
		_teamRosace.updateZOrder() ;
	}
	
	public void setMouseCaptured(boolean bBool) {
		_bMouseCaptured = bBool ;
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
