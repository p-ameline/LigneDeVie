package com.ldv.client.bigbro;

import java.util.Iterator;
import java.util.Vector;

import net.customware.gwt.dispatch.client.DispatchAsync;
import net.customware.gwt.presenter.client.EventBus;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;

import com.ldv.shared.graph.LdvModelNodeArray;
import com.ldv.shared.archetype.LdvArchetype;
import com.ldv.shared.archetype.LdvArchetypeNextDialog;
import com.ldv.shared.rpc.GetArchetypeAction;
import com.ldv.shared.rpc.GetArchetypeResult;

import com.ldv.client.event.OpenArchetypeEvent;
import com.ldv.client.util.LdvSupervisorBase;
import com.ldv.shared.filsguides.BBItemData;

/**
 * Object that commands the tree of BBItems and BBFilsItem that constitute the infrastructure
 * of trees elaboration using Archetypes or Fils Guides          
 *
 **/
public class BBSmallBrother 
{
	protected LdvSupervisorBase  _supervisor ;
	
	private final DispatchAsync  _dispatcher ;
	private final EventBus       _eventBus ;
	
	protected LdvModelNodeArray  _PatPathoArray ;
	protected LdvModelNodeArray  _TmpPathoArray = new LdvModelNodeArray() ;
	protected BBItem             _BBItem ;
	protected String             _sArchetypeID ;
	protected String             _sLanguage ;
	
	protected boolean 				   _bEditing ;
	protected boolean            _bUseFilsGuides ;  /* If no, then Archetypes leaves remain leaves without trying to dynamically expand them */ 
	
	protected Vector<BBCmdMessage> _aCmdMsgArray = new Vector<BBCmdMessage>() ;
	
	@Inject
	public BBSmallBrother(final String sLanguage, final DispatchAsync dispatcher, final EventBus eventBus, final LdvSupervisorBase supervisor)
	{
		init() ;
		
		_sLanguage     = sLanguage ;
		_dispatcher    = dispatcher ;
		_eventBus      = eventBus ;
		_supervisor    = supervisor ;
	}
	
	/**
	 * Set default values for all variables          
	 *
	 **/
	private void init()
	{
		_supervisor     = null ;
		_PatPathoArray  = null ;
		_BBItem         = null ;
		_sArchetypeID   = "" ;
		_sLanguage      = "" ;
		_bEditing       = false ;
		_bUseFilsGuides = false ;
	}
	
	/**
	 * Asks the server for archetype information in order to create proper BBItem and BBFilsItem structure          
	 *
	 * @param sArchetypeID Archetype identifier in the form domain.name.version.subversion
	 * @param FilsPere     BBFilsItem that opens this Archetype
	 * 
	 **/
	public void openArchetype(String sArchetypeID, BBFilsItem FilsPere)
	{
		_sArchetypeID = sArchetypeID ;
		
		// Creating the root BBItem
		//
		_BBItem = new BBItem(this, _supervisor) ;
		
		// Connect to a calling archetype, if any
		//
		_BBItem.setBBFatherFils(FilsPere) ;
		
		// Get archetype information from reference string
		//
		_dispatcher.execute(new GetArchetypeAction(_sArchetypeID, ""), new GetArchetypeCallback()) ;
	}
	
	/**
	 * Function called when a query for Archetype comes back from the server          
	 *
	 **/
	protected class GetArchetypeCallback implements AsyncCallback<GetArchetypeResult> 
	{
		public GetArchetypeCallback() {
			super() ;
		}

		@Override
		public void onFailure(final Throwable cause)
		{
			Log.error("Handle Failure:", cause) ;
		}

		@Override
		public void onSuccess(final GetArchetypeResult result)
		{
			// take the result from the server and notify client interested components
			if (true == result.wasSuccessful())
			{
				OpenArchetypePhaseTwo(result.getArchetype()) ;
			}
		}
	}
	
	/**
	 * Creates the BBItem and BBFilsItem structure needed to command the Archetype          
	 *
	 * @param archetype The Archetype as a LdvArchetype structure 
	 * 
	 **/
	public void OpenArchetypePhaseTwo(LdvArchetype archetype)
	{
		if (null == archetype)
			return ;
		
		// Setting data for root node
		//
		BBItemData Data = new BBItemData() ;
		Data.setOpenDialog("A") ;
		Data.setSons(_sArchetypeID) ;
		Data.setLevelShift("+00+00") ;
		
		_BBItem.setData(Data) ;
		
		// Get "dialog box" information 
		//
		LdvArchetypeNextDialog nextDialog = archetype.getNextDialog() ; 
		if (null != nextDialog)
		{
			_BBItem.setDialogName(nextDialog.getReference()) ;
		}
		
		_BBItem.createArchetype(archetype, false) ;
		
		// Command structures created, now time to create the user interface
		//
		_eventBus.fireEvent(new OpenArchetypeEvent(archetype, this, _sLanguage)) ;
	}
	
	/**
	 * Builds the patpatho from root item          
	 *
	 **/
	public void buildPatPatho()
	{
		if (null == _BBItem)
			return ;
		
		_TmpPathoArray.clear() ;
		
		buildPatPatho(_BBItem, _sArchetypeID) ;
		
		_TmpPathoArray.initFromModel(_BBItem.getCurrentPatPatho()) ;
	}
	
	/**
	 * Builds the patpatho from controls' actual content.
	 * They have been refreshed by a call to canWeClose at the BBItem           
	 *
	 * @param item the BBItem to start from (since the function is recursive)
	 * 
	 **/
	public void buildPatPatho(BBItem item, final String sArchetypeId)
	{
		if ((null == item) || (false == item.hasSons()))
			return ;
				
		// Browsing all sons
		//
		int iItemIndex = 0 ;
		BBFilsItem sonItem = item.getFilsItemAt(iItemIndex) ;
		while (null != sonItem)
		{
			// If this son is activated, we can add its content to the tree
			//
			if (sonItem.isActivated())
			{
				// If this son's patpatho is empty, then just add label node
				//
				if (sonItem.hasEmptyPatPatho())
					item.addLabel(sonItem, sArchetypeId, null, true) ;
				
				// If not empty, add label (if not +00+00) and patpatho(s) 
				//
				else
				{
					boolean bZeroShift = sonItem.isZeroShift() ;
					
					int iColForPpt = LdvModelNodeArray.ORIGINE_PATH_PATHO ;
					if (false == bZeroShift)
						iColForPpt++ ;
					
					BBVectFatheredPatPathoArray fatheredPPT = sonItem.getPatPatho() ;
					for (Iterator<BBFatheredPatPathoArray> iterPath = fatheredPPT.iterator() ; iterPath.hasNext() ; )
					{
						BBFatheredPatPathoArray fatheredPpt = iterPath.next() ; 
						
						if (false == bZeroShift)
							item.addLabel(sonItem, sArchetypeId, fatheredPpt.getFatherNode(), true) ;
						
						LdvModelNodeArray PPT = fatheredPpt.getPatPatho() ;
						item.addVector(PPT, iColForPpt) ;
					}
				}
			}
			
			iItemIndex++ ;
			sonItem = item.getFilsItemAt(iItemIndex) ;
		}
	}
	
	public LdvModelNodeArray getPatPatho() {
		return _PatPathoArray ;
	}
	
	public LdvModelNodeArray getTmpPatPatho() {
		return _TmpPathoArray ;
	}
	
	public BBItem getBBItem() {
		return _BBItem ;
	}
	
	public String getLanguage() {           
		return _sLanguage ;
	}
	
	public boolean isEditing() {				  
		return _bEditing ;
	}
	public void setEditionMode(boolean bEditMode) {
		_bEditing = bEditMode ;
	}
	
	public boolean shouldUseFilsGuides() {				  
		return _bUseFilsGuides ;
	}
	public void setUseFilsGuides(boolean bUseFilsGuides) {
		_bUseFilsGuides = bUseFilsGuides ;
	}
	
	public boolean hasEmptyMessageStack() {
		return _aCmdMsgArray.isEmpty() ; 
	}
	
	/**
	 * Erase first message in the command messages stack          
	 *
	 * @param cmdMsg either <code>null</code> or a BBCmdMessage to get erased information 
	 * @return the _iCmd information of erased message
	 * 
	 **/
	public int unstack(BBCmdMessage cmdMsg)
	{
		if (_aCmdMsgArray.isEmpty())
			return BBCmdMessage.NSDLG_NONE ;

		Iterator<BBCmdMessage> itr = _aCmdMsgArray.iterator() ;
		BBCmdMessage upStack = itr.next() ;
		
		int iCommand = upStack.getCmd() ;
		if (null != cmdMsg)
			cmdMsg.initFromModel(cmdMsg) ;

		_aCmdMsgArray.removeElementAt(0) ;

		return iCommand ;
	}

	/**
	 * Add a BBCmdMessage to the top of the stack          
	 *
	 * @param cmdMsg BBCmdMessage to get added as first message to be processed 
	 * 
	 **/
	public void stackAgain(BBCmdMessage cmdMsg)
	{
		if (null == cmdMsg)
			return ;

		_aCmdMsgArray.insertElementAt(new BBCmdMessage(cmdMsg), 0) ;
	}
}
