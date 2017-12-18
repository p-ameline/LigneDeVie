package com.ldv.client.mvp ;

import java.util.ArrayList;
import java.util.Iterator;

import net.customware.gwt.dispatch.client.DispatchAsync;
import net.customware.gwt.presenter.client.EventBus;
import net.customware.gwt.presenter.client.widget.WidgetDisplay;
import net.customware.gwt.presenter.client.widget.WidgetPresenter;

import com.allen_sauer.gwt.log.client.Log;

import com.ldv.client.event.GoToLdvAgendaEvent;
import com.ldv.client.event.GoToLdvMainEvent;
import com.ldv.client.event.LdvManagementPageEvent;
import com.ldv.client.event.LdvManagementPageEventHandler;
import com.ldv.client.model.LdvModelConcern;
import com.ldv.client.model.LdvModelProject;
import com.ldv.client.util.LdvGraphManager;
import com.ldv.client.util.LdvSupervisor;
import com.ldv.shared.database.Lexicon;
import com.ldv.shared.graph.LdvModelNode;
import com.ldv.shared.model.LdvStatus;
import com.ldv.shared.rpc.GetGraphAction;
import com.ldv.shared.rpc.GetGraphResult;
import com.ldv.shared.rpc4ontology.GetLexiconAction;
import com.ldv.shared.rpc4ontology.GetLexiconResult;
import com.ldv.shared.util.MiscellanousFcts;
import com.ldv.shared.rpc.LdvGetStatusAction;
import com.ldv.shared.rpc.LdvGetStatusResult;
import com.ldv.shared.rpc.OpenGraphAction;
import com.ldv.shared.rpc.OpenGraphResult;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.inject.Inject;

/**
 * LdvPresenter manages the Management page: remove or upload project from server, open LdV, etc 
 */
public class LdvManagementPresenter extends WidgetPresenter<LdvManagementPresenter.Display>
{	
    /**
     * The message displayed to the user when the server cannot be reached or
     * returns an error.
     */
	private static final String SERVER_ERROR = "An error occurred while "
			+ "attempting to contact the server. Please check your network "
			+ "connection and try again.";
	
	public interface Display extends WidgetDisplay
	{
		void initPanels(FlowPanel workspace, FlowPanel header) ;
    void initStatus(LdvStatus.AvailabilityLevel status) ;
    
    HasClickHandlers getRemove() ; 
  	HasClickHandlers getDownload() ; 
  	HasClickHandlers getOpen() ; 
  	HasClickHandlers getClose() ; 
  	HasClickHandlers getUpload() ; 
	}

	private final DispatchAsync _dispatcher ;
	private final LdvSupervisor _supervisor ;
    
	@Inject
	public LdvManagementPresenter(final Display       display, 
    		                        final EventBus      eventBus, 
    		                        final DispatchAsync dispatcher,
    		                        final LdvSupervisor supervisor)
	{
		super(display, eventBus) ;
		
		_dispatcher = dispatcher ;
		_supervisor = supervisor ;
		    
		bind() ;
	}
	      
	@Override
	protected void onBind() 
	{
		eventBus.addHandler(LdvManagementPageEvent.TYPE, new LdvManagementPageEventHandler() {
			public void onManagement(LdvManagementPageEvent event)
			{
				Log.info("Loading Management Header") ;
				initDisplay(event) ;
			}
		});		
				
		// Open button handler
		//
		display.getOpen().addClickHandler(new ClickHandler() 
		{
			public void onClick(final ClickEvent event) 
			{
				doOpen() ;
			}
		});
	}

	private void initDisplay(LdvManagementPageEvent event)
	{
		display.initPanels(event.getWorkspace(), event.getHeader()) ;
				
		InitFromStatus() ;
	}
	
	/**
	 * Set the proper command mode depending on status
	 * 1st step: ask status from server
	 */
	public void InitFromStatus()
  {
  	Log.info("Calling getStatus") ;

  	String sLdvId = _supervisor.getUser().getLdvId() ;
  	String sToken = _supervisor.getSessionToken() ;
  	_dispatcher.execute(new LdvGetStatusAction(sLdvId, sLdvId, sToken), new GetStatusCallback()) ;
  }
	
	/**
	 * Callback object used when status comes back from server
	 *
	 */
	public class GetStatusCallback implements AsyncCallback<LdvGetStatusResult> 
	{
		public GetStatusCallback() {
			super();
		}

		@Override
		public void onFailure(final Throwable cause)
		{
			Log.error("Handle Failure:", cause) ;
    				
			Window.alert(SERVER_ERROR) ;
		}

		@Override
		public void onSuccess(final LdvGetStatusResult result)
		{
			// take the result from the server and notify client interested components
			if (true == result.isSuccessful())
			{
				LdvStatus.AvailabilityLevel level = result.getStatus().getAvailabilityLevel() ;
				display.initStatus(level) ;
				if (LdvStatus.AvailabilityLevel.OPEN == level)
					getGraph() ;
			}
		}
	}
	
  public void doOpen()
  {
  	Log.info("Calling doOpen") ;

  	_dispatcher.execute(new OpenGraphAction(_supervisor.getUserLdvId(), _supervisor.getUserLdvId(), _supervisor.getSessionToken(), _supervisor.getUserCipherKey()), new openCallback());
  }
  
  /**
	 * Get the graph from the server
	 */
	public void getGraph()
  {
  	Log.info("Calling getGraph") ;

  	String sLdvId = _supervisor.getDisplayedPerson().getLdvId() ;
  	String sToken = _supervisor.getSessionToken() ;
  	
  	_dispatcher.execute(new GetGraphAction(sLdvId, sLdvId, sToken), new GetGraphCallback()) ;
  }
	
	/**
	 * Callback object used when graph comes back from server
	 *
	 */
	public class GetGraphCallback implements AsyncCallback<GetGraphResult> 
	{
		public GetGraphCallback() {
			super() ;
		}

		@Override
		public void onFailure(final Throwable cause)
		{
			Log.error("Handle Failure:", cause) ;
    				
			Window.alert(SERVER_ERROR) ;
		}

		@Override
		public void onSuccess(final GetGraphResult result)
		{
			// take the result from the server and notify client interested components
			if (true == result.wasSuccessful())
			{
				_supervisor.initializeDisplayedGraph(result.getGraph()) ;
				
				nameGraph() ;
				
				// TODO remove this comment to display the Ligne de Vie
				//
				eventBus.fireEvent(new GoToLdvMainEvent()) ;
				
				// getAppointments() ;
			}
		}
	}
	
	/**
	 * In order to display the graph, we must get all labels to display from the ontology
	 *
	 */
	private void nameGraph()
	{
		LdvGraphManager ldvGraphManager = _supervisor.getDisplayedGraph() ;
		if (null == ldvGraphManager)
			return ;
		
		ArrayList<LdvModelProject> projects = ldvGraphManager.getProjects() ;
		if ((null == projects) || projects.isEmpty())
			return ;
		
		for (Iterator<LdvModelProject> itr = projects.iterator() ; itr.hasNext() ; )
		{
			LdvModelProject project = itr.next() ;
			nameProject(project, ldvGraphManager) ;
		}
	}
	
	/**
	 * In order to display concerns, we must get their label from the ontology
	 *
	 */
	private void nameProject(LdvModelProject project, LdvGraphManager ldvGraphManager)
	{
		if (null == project)
			return ;
		
		ArrayList<LdvModelConcern> concerns = project.getConcerns() ;
		if ((null == concerns) || concerns.isEmpty())
			return ;
		
		for (Iterator<LdvModelConcern> itr = concerns.iterator() ; itr.hasNext() ; )
		{
			LdvModelConcern concern = itr.next() ;
			
			nameConcern(concern, ldvGraphManager) ;
		}
	}
	
	/**
	 * Concerns may already have a label (if a free text or a lexicon item already buffered at parsing time)
	 * 
	 * This function only treats missing labels 
	 *
	 */
	private void nameConcern(LdvModelConcern concern, LdvGraphManager ldvGraphManager)
	{
		if (null == concern)
			return ;
		
		// If title is already set, nothing to do 
		//
		if (false == "".equals(concern.getTitle()))
			return ;
		
		LdvModelNode node = ldvGraphManager.getNodeFromId(concern.getID()) ;
		if (null == node)
			return ;
		
		// Try to set title from free text of buffered Lexicon 
		//
		ldvGraphManager.initializeNodeTitle(concern, node) ;
		if (false == "".equals(concern.getTitle()))
			return ;
		
		// Asynchronous call to server
		//
		_dispatcher.execute(new GetLexiconAction(node.getLexicon(), concern.getID()), new GetLexiconCallback());
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
				if (null == lexicon)
					return ;
				
				_supervisor.addLexicon(lexicon) ;
					
				LdvGraphManager ldvGraphManager = _supervisor.getDisplayedGraph() ;
				if (null == ldvGraphManager)
					return ;
					
				LdvModelConcern concern = ldvGraphManager.getConcernById(result.getNodeId()) ;
				if (null == concern)
					return ;
					
				String sLexiconLabel = lexicon.getDisplayLabel(Lexicon.Declination.singular, _supervisor.getUserLanguage()) ;
				concern.setTitle(MiscellanousFcts.upperCaseFirstChar(sLexiconLabel)) ;
			}
		}
	}
		
	protected void getAppointments()
	{
		eventBus.fireEvent(new GoToLdvAgendaEvent()) ;

/*		
		String sUserId = _supervisor.getUser().getLdvId() ;
		String sLdvId  = _supervisor.getDisplayedPerson().getLdvId() ;
  	String sToken  = _supervisor.getSessionToken() ;
  	
  	LdvTime tNow = new LdvTime(0) ;
  	tNow.takeTime() ;
		
		_dispatcher.execute(new GetPlannedEventsAction(tNow.getSimpleDate(), "", sLdvId, sUserId, sToken), new GetAppointmentsCallback()) ;
*/	
	}
	
	/**
	*  Asynchronous callback function for calls to GetLexiconFromCodeHandler
	**/
/*
	public class GetAppointmentsCallback implements AsyncCallback<GetPlannedEventsResult> 
	{
		public GetAppointmentsCallback() {
			super();
		}

		@Override
		public void onFailure(final Throwable cause)
		{
			Log.error("Cannot get appointments:", cause) ;
			eventBus.fireEvent(new GoToLdvAgendaEvent()) ;
		}

		@Override
		public void onSuccess(final GetPlannedEventsResult result)
		{
			// take the result from the server
			if (true == result.wasSuccessful())
			{
				eventBus.fireEvent(new GoToLdvAgendaEvent()) ;
			}
		}
	}
*/
	
	@Override
	protected void onUnbind() 
	{
		// Add unbind functionality here for more complex presenters.
	}

	public void refreshDisplay() 
	{
		// This is called when the presenter should pull the latest data
		// from the server, etc. In this case, there is nothing to do.
	}

	public void revealDisplay() 
	{
		// Nothing to do. This is more useful in UI which may be buried
		// in a tab bar, tree, etc.
	}

	protected void SwitchToWorking()
	{
	}
	    	
	public class openCallback implements AsyncCallback<OpenGraphResult> 
	{

		public openCallback() {
			super() ;
		}

		@Override
		public void onFailure(final Throwable cause)
		{
			Log.error("Handle Failure:", cause) ;
    				
			Window.alert(SERVER_ERROR) ;
		}

		@Override
		public void onSuccess(final OpenGraphResult result)
		{
			// take the result from the server and notify client interested components
			if (true == result.getSuccess())
			{
				String sLdvId = _supervisor.getUser().getLdvId() ;
		  	String sToken = _supervisor.getSessionToken() ;
		  	_dispatcher.execute(new LdvGetStatusAction(sLdvId, sLdvId, sToken), new GetStatusCallback()) ;
			}
		}
	}

	@Override
	protected void onRevealDisplay() {
		// TODO Auto-generated method stub
		
	}
}
