package com.ldv.client.bigbro;

import java.util.Iterator;
import java.util.Vector;

import com.google.gwt.user.client.ui.Panel;
import com.google.inject.Inject;

import com.ldv.client.bigbro.BBControl.CTRL_ACTIVATION;
import com.ldv.client.bigbro.BBTransferInfo.TRANSFER_DIRECTION;
import com.ldv.client.util.LdvSupervisorBase;

import com.ldv.shared.archetype.LdvArchetype;
import com.ldv.shared.archetype.LdvArchetypeItem;
import com.ldv.shared.archetype.LdvArchetypeItemConstraint;
import com.ldv.shared.filsguides.BBItemData;
import com.ldv.shared.filsguides.FilGuideSearchElementVector;
import com.ldv.shared.graph.BBMessage;
import com.ldv.shared.graph.LdvGraphConfig;
import com.ldv.shared.graph.LdvModelNode;
import com.ldv.shared.graph.LdvModelNodeArray;
import com.ldv.shared.graph.LdvModelNodeArrayArray;
import com.ldv.shared.util.MiscellanousFcts;

public class BBItem 
{
	protected LdvSupervisorBase  _supervisor ;
	
	protected BBArchetypePanel   _archetypeView ;
	protected BBDialogBox        _dialogBox ;
	
	protected BBItemData         _Data ;
	protected LdvArchetype       _Archetype ;
	protected String             _sLocalization ;
	protected String             _sDialogName ;
	protected String             _sArchetypeId ;
	
	protected LdvModelNodeArray  _CurrentPatPatho ;  // usually a "pointer" to the proper ppt in the BBVectFatheredPatPathoArray controlled by _BBFatherFils  
	protected LdvModelNodeArray  _MergePatPatho ;
	
	protected BBFilsItem         _BBFatherFils ;
	protected BBSmallBrother     _BigBoss ;
	
	protected Vector<BBFilsItem> _aBBItemFils = new Vector<BBFilsItem>() ;
	protected int                _iFocusedFilItem ;  // Id of currently focused BBFilsItem
	
	protected BBFunction         _bbFunction ;
	
	protected int                _iDlgReturn ;	 	// Return code from dialog
	protected boolean            _bHideDialog ;
	
	@Inject
	public BBItem(BBSmallBrother Big, final LdvSupervisorBase supervisor)
	{
		init() ;
		
		_supervisor = supervisor ;
		_BigBoss    = Big ;
		
		if (null != Big)
			_CurrentPatPatho = Big.getPatPatho() ;
	}
	
	@Inject
	public BBItem(BBFilsItem Father, BBSmallBrother Big, LdvModelNodeArray CurrentActivePPT, final LdvSupervisorBase supervisor)
	{
		init() ;
		
		_supervisor      = supervisor ;
		_BBFatherFils    = Father ;
		_BigBoss         = Big ;
		_CurrentPatPatho = CurrentActivePPT ;
	}
	
	protected void init() 
	{
		_supervisor      = null ;
		_dialogBox       = null ;
		_archetypeView   = null ;
		
		_Data            = null ;
		_Archetype       = null ;
		_CurrentPatPatho = null ;
		_BBFatherFils    = null ;
		_bbFunction      = null ;
		
		_sLocalization   = "" ;
		_sDialogName     = "" ;
		_sArchetypeId    = "" ;
		
		_bHideDialog     = false ;
		_iDlgReturn      = 0 ;
		
		_iFocusedFilItem = 0 ;
	}
	
	/**
	 * Create the archetype structure as a hierarchy of BBItem and BBFilsItem objects    
	 * 
	 * @param archetypeItem archetype to manage
	 * @param bDontDispach  if <code>true</code>, don't dispatch the PatPatho 
	 * 
	 **/
	public void createArchetype(final LdvArchetype archetype, final boolean bDontDispatch)
	{
		_Archetype = archetype ;
		
		if (null == _Archetype)
			return ;
		
		_sArchetypeId = _Archetype.getIdentifier() ;
		_sDialogName  = _sArchetypeId ;
		
		Vector<LdvArchetypeItem> archetypeItems = _Archetype.getRootItem() ;
		
		if ((null == archetypeItems) || archetypeItems.isEmpty())
			return ;
		
		createArchetype(archetypeItems, null, bDontDispatch) ;
	}
	
	/**
	 * Create one level of BBItem and BBFilsItem objects from the archetype structure    
	 * 
	 * @param archetypeItems array of LdvArchetypeItem to build managers for
	 * @param bDontDispach   if <code>true</code>, don't dispatch the PatPatho 
	 * 
	 **/
	public void createArchetype(final Vector<LdvArchetypeItem> archetypeItems, 
			                        final Vector<LdvArchetypeItemConstraint> archetypeItemConstraints, 
			                        final boolean bDontDispatch)
	{
		if ((null == archetypeItems) || archetypeItems.isEmpty())
			return ;
		
		FilGuideSearchElementVector SearchVector = new FilGuideSearchElementVector() ;
		
		// Create a BBFilsItem for each son 
		//
		for (Iterator<LdvArchetypeItem> itr = archetypeItems.iterator() ; itr.hasNext() ; )
		{
			LdvArchetypeItem item = itr.next() ;
			
			BBFilsItem FilsItem = new BBFilsItem(this, _BigBoss, _supervisor) ;
			FilsItem.setArchetypeItem(item) ;
			FilsItem.setLabel(item.getCode()) ;
			
			String sLevelShift = item.getShiftLevel() ;
			if ("".equals(sLevelShift))
				sLevelShift = "+01+01" ;
			FilsItem.getItemData().setLevelShift(sLevelShift) ;
			
			// If a leaf, and neither free text or statically connected as another archetype opener,
			// then add it to the list of fils guides to look for
			//
			if (item.isLeaf())
			{
				if ((false == FilsItem.isFreeText()) && ("".equals(item.getArchetype()))) 
				{
					String sCodeSens = LdvModelNode.getSemanticCode(FilsItem.getLabel()) ;
					SearchVector.AddLabel(sCodeSens) ;
				}
			}
			
			_aBBItemFils.addElement(FilsItem) ;
		}
		
		// Initialize the exclusion String in _Data from Archetype's constraints  
		//
		if (null != archetypeItemConstraints)
		{
			String sExclusions = "" ;
		
			for (Iterator<LdvArchetypeItemConstraint> itr = archetypeItemConstraints.iterator() ; itr.hasNext() ; )
			{
				LdvArchetypeItemConstraint constraint = itr.next() ;
			
				if (LdvArchetypeItemConstraint.VAL_ATTR_CONTR_TYPE_EXCLUS.equals(constraint.getType()))
					sExclusions += constraint.getList() ;
			}
		
			_Data.setExclusions(sExclusions) ;
		}
		
		// Next step
		//
		if (SearchVector.isEmpty())
			createArchetypeSecondStep(SearchVector, bDontDispatch) ;
		else
			getFilsGuides(SearchVector, bDontDispatch, "createArchetype") ;
	}
	
	/**
	 * Second step of archetype creation, after Fils Guides have been queried for leaves    
	 * 
	 * @param SearchVector vector containing leaves labels and related Fils Guides (when found)
	 * @param bDontDispach  if <code>true</code>, don't dispatch the PatPatho
	 * 
	 **/
	public void createArchetypeSecondStep(final FilGuideSearchElementVector SearchVector, final boolean bDontDispatch)
	{
		initSonsInArchetype(SearchVector) ;
	
		// sMsg = "creerArchetype() : MAJ des patpatho" ;
		// _pSuper->trace(&sMsg, 1, NSSuper::trSubDetails) ;

		// M.A.J des patpatho (sauf mode Consultation)
		//
		if ((false == bDontDispatch) && (null != _CurrentPatPatho))
			DispatchPatPatho() ;

		// sMsg = "creerArchetype() : Appel de creerFilsArchetype" ;
		// _pSuper->trace(&sMsg, 1, NSSuper::trSubDetails) ;

		createExclusionStructure() ;
		createSonsInArchetype() ;
	}
	
	protected void initSonsInArchetype(final FilGuideSearchElementVector SearchVector)
	{
		if (_aBBItemFils.isEmpty())
			return ;
			
		
		// Iterating BBFilsItem sons in order to set the IsExpandable status
		// It is expected that each son is linked to a LdvArchetypeItem
	  //
		for (Iterator<BBFilsItem> itr = _aBBItemFils.iterator() ; itr.hasNext() ; )
		{
			BBFilsItem son = itr.next() ;
			LdvArchetypeItem archetypeItem = son.getArchetypeItem() ;
				
			// if a leaf inside the archetype 
			//
			if (archetypeItem.isLeaf())
			{
				// free texts are managed specifically
				//
				if (son.isFreeText())
				{
					BBItemData SonData = son.getItemData() ;
					if (null != SonData)
					{
	          SonData.init() ;
	          SonData.setPath("~****/#####") ;
	          SonData.setSons("#TLI#1") ;
	          SonData.setDialogName("TEXTLIB") ;
	          SonData.setOpenDialog(true) ;
	          SonData.setLevelShift("+00+00") ;
	          SonData.setDialogFile("NSBB") ;
	          SonData.setActiveWhenEmpty(false) ;
					}

					son.setIsExpandable(true) ;
				}
				//
	      // leaves that are not a free text
				//
				else
				{
					String sArchetype = archetypeItem.getArchetype() ;

	        // If this leaf points to another archetype, we fill a virtual Fil Guide 
					//
	        if (false == "".equals(sArchetype))
	        {
	        	BBItemData SonData = son.getItemData() ;
	        	if (null != SonData)
	        	{
	            SonData.init() ;
	            SonData.setPath(son.getLabel()) ;
	            SonData.setSons(sArchetype) ;
	            SonData.setOpenArchetype(true) ;

	            String sDecal = archetypeItem.getShiftLevel() ;
	            if ("".equals(sDecal)) // if shift level not specified...
	            	sDecal = "+01+01" ;  // ... set default value

	            SonData.setLevelShift(sDecal) ;
	            SonData.setActiveWhenEmpty(false) ;
	        	}

	        	son.setIsExpandable(true) ;
	        }
	        //
	        // If this leaf doesn't point to another archetype, we check if we found a Fil Guide
	        //
	        else
	        {
	        	String sCodeSens = LdvModelNode.getSemanticCode(son.getLabel()) ;

	        	boolean bFound = false ;
	        	if (null != SearchVector)
	            bFound = SearchVector.SetData(sCodeSens, son.getItemData()) ;
	            
						if (bFound)
						{
							// si feuille

							// cas particulier : �tiquette = #C���1 : pas de fils mais
							// le consid�rer comme prolongeable
							BBItemData ItemData = son.getItemData() ;

							// Archetypes can only be extended by fils guides that
							// open a dialog or another Archetype
							//
							if (ItemData.hasSons() && (ItemData.OpensDialog() || ItemData.OpensArchetype()))
								son.setIsExpandable(true) ;
							else
								son.setIsExpandable(false) ;
						}
						else
							son.setIsExpandable(false) ;
					}
				}
			}
			else // not a leaf => consider as expendable
				son.setIsExpandable(true) ;
		} 
	}
	
	/**
	 * Get fils guides for a set of paths    
	 * 
	 * @param SearchVector vector containing leaves labels and related Fils Guides (when found)
	 * @param bDontDispach  if <code>true</code>, don't dispatch the PatPatho
	 * 
	 **/
	public void getFilsGuides(final FilGuideSearchElementVector SearchVector, final boolean bDontDispatch, final String sWhereToGoNext)
	{
		// So far, we don't do anything
		//
		if ("createArchetype".equals(sWhereToGoNext))
		{
			createArchetypeSecondStep(SearchVector, bDontDispatch) ;
			return ;
		}
		if ("create".equals(sWhereToGoNext))
		{
			createArchetypeSecondStep(SearchVector, bDontDispatch) ;
			return ;
		}
		if ("updatePatpatho".equals(sWhereToGoNext))
		{
			updatePatpathoSecondStep(SearchVector) ;
			return ;
		}
	}
	
	/**
	 * Providing sons with their own PatPatho by taking the sub-PatPatho being their label.    
	 * 
	 **/
	public void DispatchPatPatho()
	{
		if ((null == _CurrentPatPatho) || (_CurrentPatPatho.isEmpty()))
	    return ;
		
		LdvModelNode rootNode = _CurrentPatPatho.getFirstRootNode() ;
		
		while (null != rootNode)
		{
			String sLexicon = rootNode.getLexicon() ;
			
			BBMessage msg = new BBMessage() ;
			msg.initIdsFromNode(rootNode) ;
			
			// Specific issue : if label contains "�C" get information from next node
      // In such case, the control is "virtual", as, for example, a free text among radio buttons
      // Example in Echocardiography : Chemotherapy
			//
      if (sLexicon.contains(String.valueOf(LdvGraphConfig.POUND_CHAR) + "C;") || 
      		sLexicon.contains("/" + String.valueOf(LdvGraphConfig.POUND_CHAR) + "C;") || 
      		sLexicon.contains(String.valueOf(LdvGraphConfig.POUND_CHAR) + "CX"))
      {
      	LdvModelNode rootSonNode = _CurrentPatPatho.getFirstSon(rootNode) ;
        if (null != rootSonNode)
        {
          msg.initIdsFromNode(rootSonNode) ;
          msg.setNodeID(msg.getNodeID() + LdvGraphConfig.pathSeparationMARK + rootSonNode.getNodeID()) ;
        }
      }
      else
        msg.initFromNode(rootNode) ;
      
      String sLabel = msg.getLabel() ;
      BBFilsItem son = getSonFromLabel(sLabel, msg.getComplement()) ;
      
      // If a son has been found, then activate it and provide it with information
      //
      if (null != son)
      {
      	BBMessage tranfertMsg = son.getTransfertMessage() ;
      	if (null != tranfertMsg)
      		tranfertMsg.initFromModel(msg) ;
      }
      
      rootNode = _CurrentPatPatho.getNextBrother(rootNode) ;
		}
		
	}
	
	protected void DispatcherPatPathoForZeroSon(BBFilsItem son, final LdvModelNode rootNode)
	{
		if ((null == son) || (null == rootNode))
			return ;
		
		son.Activate() ;
	}
	
	/**
	 * Get the BBFilsItem from it's identity inside the panel controlled by <code>this</code>     
	 * 
	 * @param sIdentity      label to compare with sons' ones
	 * @param sComplement the complement in case it is specified in sons' labels
	 * 
	 * @return a BBFilsItem if found, <code>null</code> if not 
	 * 
	 **/
	public BBFilsItem getSonInPanel(final String sIdentity, final String sExploredPath)
	{
		if ((null == sIdentity) || "".equals(sIdentity) || _aBBItemFils.isEmpty())
			return null ;
		
		String sSemIdentity = LdvModelNode.getSemanticLabel(sIdentity) ;
		String sSemPath     = LdvModelNode.getSemanticLabel(sExploredPath) ;
		
		for (Iterator<BBFilsItem> itr = _aBBItemFils.iterator() ; itr.hasNext() ; )
		{
			BBFilsItem son = itr.next() ;
			
			String sSonSemLabel = LdvModelNode.getSemanticLabel(son.getLabel()) ;
			
			String sCandidatePath = "" ;
			if ("".equals(sSemPath))
				sCandidatePath = sSonSemLabel ;
			else
				sCandidatePath = sSemPath + LdvGraphConfig.pathSeparationMARK + sSonSemLabel ;
			
			// If the proper son was found, and is not already connected, then we are done
			//
			if (sCandidatePath.equals(sSemIdentity))
			{
				if (null == son.getTransfert().getControl())
					return son ;
			}
			else if (sCandidatePath.length() < sSemIdentity.length())
			{
				// Are we in the proper "branch"?
				//
				String sRootPath = sSemIdentity.substring(0, sCandidatePath.length()) ;
				if (sCandidatePath.equals(sRootPath))
				{
					if (false == son.getSons().isEmpty())
					{
						for (Iterator<BBItem> itemItr = son.getSons().iterator() ; itemItr.hasNext() ; )
						{
							BBItem item = itemItr.next() ;
							
							BBFilsItem foundSon = item.getSonInPanel(sIdentity, sCandidatePath) ;
							if (null != foundSon)
								return foundSon ;
						}
					}
				}
			}
		}
		
		return null ;
	}
	
	/**
	 * Get the BBFilsItem whose label is sLabel     
	 * 
	 * @param sLabel      label to compare with sons' ones
	 * @param sComplement the complement in case it is specified in sons' labels
	 * 
	 * @return a BBFilsItem if found, <code>null</code> if not 
	 * 
	 **/
	public BBFilsItem getSonFromLabel(final String sLabel, final String sComplement)
	{
		if ((null == sLabel) || "".equals(sLabel) || _aBBItemFils.isEmpty())
			return null ;
		
		String sSemLabel = LdvModelNode.getSemanticLabel(sLabel) ;
		
		for (Iterator<BBFilsItem> itr = _aBBItemFils.iterator() ; itr.hasNext() ; )
		{
			BBFilsItem son = itr.next() ;
			
			String sSonSemLabel = LdvModelNode.getSemanticLabel(son.getLabel()) ;
			if (sSemLabel.equals(sSonSemLabel))
				return son ;
			
			if (sSonSemLabel.contains(sSemLabel))
			{
				// TODO check that WCEA0 is the only difference
				if (sSonSemLabel.contains("WCEA0"))
					return son ;
				
				if (false == "".equals(sComplement))
				{
					if (sSonSemLabel.equals(sSemLabel + LdvGraphConfig.pathSeparationMARK + "$" + sComplement))
						return son ;
					
					if (sSonSemLabel.equals(sSemLabel + LdvGraphConfig.pathSeparationMARK + "$" + getLexiconSizedComplement(sComplement)))
						return son ;
				}
			}
		}
		
		return null ;
	}
	
//Description  : R�ponse � la notification d'activation d'un contr�le
//Arguments    : sIdentite 		-> coordonn�es du contr�le
//						    etatInitial		-> �tat du contr�le avant activation
//						    etatSouhaite  -> �tat final d�sir�
//						    message				-> informations compl�mentaires
//               indexFils     -> index du fils de pFilsItem � d�velopper
//Returns      : Rien
//-----------------------------------------------------------------------------
	
	/**
	 * Reacts to a control being activated     
	 * 
	 * @param filsItem    the son being activated
	 * @param wishedState the wished state (for example, wants to be activated)
	 * @param message      
	 * @param sonIndex     
	 * 
	 **/
	void ctrlNotification(BBFilsItem filsItem, BBControl.CTRL_ACTIVATION wishedState, BBMessage message, int sonIndex)
	{
		if (null == filsItem)
			return ;

		// TRAITEMENT DES TREEVIEW

		if (BBControl.CTRL_ACTIVATION.delete == wishedState) //  TreeView
		{
			// for each son BBItem
			filsItem.manualDestruction(message) ;
			
			// remove filsItem from sons list
			//
			if (false == _aBBItemFils.isEmpty())
			{
				for (Iterator<BBFilsItem> itr = _aBBItemFils.iterator() ; itr.hasNext() ; )
				{
	  			if (itr.next() == filsItem)
	  			{
	  				itr.remove() ;
	  				break ;
	  			}
				}
			}
			
			// if the father (this) has no more son, it must be deactivated
			//
			if (_aBBItemFils.isEmpty())
				if (false == isActiveWhenEmpty())
					_BBFatherFils.Unactivate() ;
       
      return ;
		}

		// If the son is "final", the control is free to do what it wants
		//
		if (false == filsItem.isExpandable())
		{
			BBTransferInfo transfer = filsItem.getTransfert() ;
			if (null != transfer)
			{
				// Let the control do what it wants
				transfer.activateControl(wishedState, message) ;

				// Then update its transfer structure
				transfer.Transfer(TRANSFER_DIRECTION.tdGetData) ;
			}

			// If the control wanted to get checked, we need to deactivate its excluded brothers
			//
			if (filsItem.isActivated())
				filsItem.Activate() ;
			else
				filsItem.Unactivate() ;

			return ;
		}

		// If the son is not "final", we have to analyse its context to know what to do
		//
		// First case: the control wants to get checked
		//
		if (BBControl.CTRL_ACTIVATION.checked == wishedState)
		{
			// Expanding the branch or maybe the sub-branch pointed to by sonIndex (if sonIndex >= 0)
			//
			int k = expand(filsItem, null, sonIndex) ;

			// If this control is father to another control located in the same Panel
			// expand returns 3. In this case, this control must be managed "as a leaf" since
			// it is up to the user to check or not its son(s)
			//
			if ((3 == k) && (null != filsItem.getTransfert()))
			{
				filsItem.getTransfert().activateControl(wishedState, message) ;
				filsItem.getTransfert().Transfer(TRANSFER_DIRECTION.tdGetData) ;
			}

			// If this control was successfully activated, take the proper actions (kill excluded, etc)
			//
			if (filsItem.isActivated())
				filsItem.Activate() ;
			else
				filsItem.Unactivate() ;
		}

		// Other case: the control wished to be unchecked
		// If it is a "ON/OFF" control (radiobutton, checkbox), it simply means that it was
		// in a checked state and the user clicked it. There are two cases depending on the
		// patpatho :
		//        1) the element has no son and we let it get uncheked
		//        2) the element has sons and it should be expanded
		//
		if (BBControl.CTRL_ACTIVATION.unchecked == wishedState)
		{
			if (filsItem.hasEmptyPatPatho())
			{
				filsItem.Unactivate() ;
				if (null != filsItem.getTransfert())
					filsItem.getTransfert().activateControl(wishedState, message) ;
			}
			else
			{
				BBDialogFunction fct = null ;

				// In such situation, the control cannot guess if it will be activated
				// or unchecked, so it doesn't fire its functions and we must do it there
				//
				if ((null != filsItem.getTransfert()) && (null != filsItem.getTransfert().getControl()))
					fct = filsItem.getTransfert().getControl().getDialogFunction() ;

				if (null != fct)
					fct.execute(BBDialogFunction.SITUATION_TYPE.Execute) ;

				// On d�veloppe la branche
				int k = expand(filsItem, null, 0) ;
				
				// If this control is father to another control located in the same Panel
				// expand returns 3. In this case, this control must be managed "as a leaf" since
				// it is up to the user to check or not its son(s)
				//
				if ((3 == k) && (null != filsItem.getTransfert()))
				{
					filsItem.getTransfert().activateControl(wishedState, message) ;
					filsItem.getTransfert().Transfer(TRANSFER_DIRECTION.tdGetData) ;
				}

				// If this control was successfully activated, take the proper actions (kill excluded, etc)
				//
				if (filsItem.isActivated())
					filsItem.Activate() ;
				else
					filsItem.Unactivate() ;

				if (null != fct)
					fct.execute(BBDialogFunction.SITUATION_TYPE.PostExec) ;
			}
		}

		// Setting a state to the control
		//
		if (null != filsItem.getTransfert())
		{
			if (filsItem.isActivated())
				filsItem.getTransfert().activateControl(BBControl.CTRL_ACTIVATION.checked, message) ;
			else
				filsItem.getTransfert().activateControl(BBControl.CTRL_ACTIVATION.unchecked, message) ;
		}
	}

//-----------------------------------------------------------------------------
//Function     : int BBItem::developper(BBFilsItem* pFilsIt)
//Arguments    : BBFilsItem* pFils -> pointeur sur le fils � d�velopper
//Description  : Demande � l'arborescence de se prolonger (usuellement pour
//               ouvrir une nouvelle boite de dialogue).
//               Il faut alors : - cr�er un BBItem � partir du fils choisi
//                               - lancer BBItem.creer()
//                               - lancer BBItem.activer()
//Returns      : 0 si tout a bien fonctionn�
//						    1 si on a eu un probl�me
//						    2 si la branche n'a pas de prolongation
//						    3 si la branche a d�j� �t� prolong�e
//-----------------------------------------------------------------------------
	
	/**
	 * Asks the tree to expand (usually to open a new window or dialog box)<br><br>
	 * 
	 *      It is usually done by:<br> - creating a BBItem to expand selected son<br>
	 *                             - execute BBItem.create()<br>
	 *                             - execute BBItem.activate()<br>
	 * 
	 * @param filsIt   the son being expanded
	 * @param PPT      
	 * @param sonIndex index of BBFilsItem to operate on, or -1 
	 * 
	 **/
	protected int expand(BBFilsItem filsIt, LdvModelNodeArray PPT, int sonIndex)
	{
		// If sons already exist, there is nothing to do
		//
		if ((null == filsIt) || filsIt.hasSons())
			return 3 ;

		// Is this branch expandable?
		//
	//if (!(pFilsIt->estProlongeable()))  //PENSER A RETOURNER 1 EN CAS DE PB FICHIER DANS ESTPRO...
		if (false == filsIt.isExpandable())
			return 2 ;

		// on/off switch: if filsIt is activated but is empty, we just unactivate it
		//
		if (filsIt.isActivated() && filsIt.getTransfert().getTransPatpatho().isEmpty())
		{
			// Sauf si on est en mode correction
			if (false == filsIt.isInEditMode())
			{
				filsIt.Unactivate() ; 
				return 0 ; // iRetourDlg =0
			}
		}

		String sLabel = filsIt.getLabel() ;

		filsIt.createSons(sonIndex) ;  

		if (false == filsIt.hasSons())
			return 0 ;
		
		int iRetActiv = 0 ;
		
		// Multi-dialog in edit mode
		//
		if ((false == filsIt.IsUnique()) && filsIt.isInEditMode() && (null != PPT))
		{
			// Look, among all BBItem, for the one whose _CurrentPatPatho is PPT
			// Its interface will be displayed while its brothers' will be hidden
			//
			for (Iterator<BBItem> itr = filsIt.getSons().iterator() ; itr.hasNext() ; )
			{
				BBItem son = itr.next() ;
					
				son._iDlgReturn = 0 ;
					
				if (false == PPT.isSamePpt(son.getCurrentPatPatho()))
					son._bHideDialog = true ;
			}
		}

		for (Iterator<BBItem> itr = filsIt.getSons().iterator() ; itr.hasNext() ; )
		{
			BBItem son = itr.next() ;
			
			// Give the new element its heritage
			setDNA(son, sLabel) ;
			
			// Asking the new element to create itself (and to create its sons that fit in the same panel)
			int i = son.create(false) ;
			
			if (0 == i)
		  {
				iRetActiv = son.activate() ;
/*
		    if ((pNewFils->_iProfondeur < _pBigBoss->getSeuilSauve()) && (pNewFils->_pPPTEnCours))
		    	_pBigBoss->sauvegarde() ;
*/
		  }
		}
		
		// Show the multiple description controller 
		//
		if (null != filsIt.getMultipleDialogPresenter())
		 	filsIt.getMultipleDialogPresenter().show() ;


		// Demande � l'�ventuelle boite de dialogue de r�percuter les modifs
		// TODO find the proper place for this
		// if (_pNSDialog)
		// 	_pNSDialog->rafraichitControles() ;

		return iRetActiv ;
	}
	
//-----------------------------------------------------------------------------
//Function     : int BBItem::activer()
//Description  : Demande � la BBItem de se lancer : cr�er la boite de dialogue
//               ou lancer un processus
//						    Il serait tentant de g�rer � ce niveau la g�n�ration/mise �
//						    jour/destruction des l�sions lorsque le BBItem est cr�ateur.
//						    La diversit� de traitement des l�sions (unique/multiples...)
//						    nous oblige � les traiter plus bas (bonne id�e ?).
//Returns      : 1 si aucune branche ne poss�de cette �tiquette valeur de
//               retour de BBItem::developper(int numFils) sinon
//-----------------------------------------------------------------------------
	
	/**
	 * Asks this object to act: whether open an interface or execute a process 
	 * 
	 **/
	protected int activate()
	{
		// in edit mode //on est pas au bout du chemin
		//
		if (_BigBoss.isEditing())
		{
			if (activateWhileEditing())
				return 0 ;
		}

		// If this BBItem is connected to a dialog element, it is created
		//
		if (linkedToUI())
		{
			if (null != _BBFatherFils)
				_BBFatherFils.executeMultipleDialogs() ;

			return createUserInterface() ;
		}

		// If this BBItem is linked to a function, the function is executed
		//
		if (isLinkedToFunction())
			return activateForFunction() ;

		// If there, it means that the BBItem is neither connected to a dialog element 
		// nor to a function.
		//
		// It should activate its first branch if in "automatic" mode or ask user to select
		// among sons if in "manual" mode
		//
		if (_aBBItemFils.isEmpty())
			return 0 ;

		// Better check if at least one son is "expandable" or we may enter an infinite loop
		//
		boolean bFoundExpandable = false ;
		
		for (Iterator<BBFilsItem> itr = _aBBItemFils.iterator() ; itr.hasNext() && (false == bFoundExpandable) ; )
			if (itr.next().isExpandable())
				bFoundExpandable = true ;
		
		// If no expandable son, simulate a down exit
		//
		if (false == bFoundExpandable)
		{
			activateFromEnd() ;
			return 0 ;
		}
		
		BBCmdMessage BBMsg = new BBCmdMessage() ;

		_iFocusedFilItem = 1 ;
		
		boolean bKeepOn = true ;
		int     iNext   = 0 ;

		// Step with no interface and no function, only controlled by flags
		//
		while (bKeepOn)
		{
			if (_BigBoss.hasEmptyMessageStack())
			{
				iNext = expand(getFilsItemAt(_iFocusedFilItem - 1), null, 0) ;
				updatePatpatho() ;
			}
			else
				iNext = _BigBoss.unstack(BBMsg) ;

			switch (iNext)
			{
				case BBCmdMessage.NSDLG_NEXT : // go to next step

					_iFocusedFilItem++ ;
					
					// If no next BBFilsItem, go out down
					if (null == getFocusedFilsItem())
					{
						activateFromEnd() ;
						return 0 ;
					}
					
					break ;

				case BBCmdMessage.NSDLG_RETURN : // go back to previous step

					_iFocusedFilItem-- ;
					
					// If we were already on first step, exit up
					if (0 == _iFocusedFilItem)
						return -1 ;
					
					break ;

				case BBCmdMessage.NSDLG_EXIT_DOWN : // Exit down

					activateFromEnd() ;
					return 0 ;

				case BBCmdMessage.NSDLG_EXIT_UP : // Exit up

					return -1 ;

				case BBCmdMessage.NSDLG_GO_TO :

					if (activateGoTo(BBMsg))
						return 0 ;
					
					break ;

				default :

					if ((iNext >= BBCmdMessage.NSDLGRETURN_DIRECT) && (iNext < BBCmdMessage.NSDLGRETURN_END))
					{
						// Does this message targets a lower level node
						//
						if (iNext >= BBCmdMessage.NSDLGRETURN_DIRECT + BBCmdMessage.NSDLGRETURN_SEPARATOR)
						{
							iNext -= BBCmdMessage.NSDLGRETURN_SEPARATOR ;
							return iNext ;
						}

						_iFocusedFilItem = iNext - BBCmdMessage.NSDLGRETURN_DIRECT + 1 ;
					}
					else if ((iNext > 0) && (null != getFilsItemAt(iNext-1)))
						_iFocusedFilItem = iNext ;
			}
		}
		return 1 ;
	}


//Returns true if activated
//
	protected boolean activateWhileEditing()
	{
		LdvModelNodeArray PPT = new LdvModelNodeArray() ;

		// trouver le fils ayant dans sa patpatho l'�l�ment d'ID  = 1
		//
		BBFilsItem filsItem = findSonToEdit() ;

		if (null != filsItem)
		{
			// si d�calge nul alors ne pas d�velopper Iter, mais attribuer true � son bCorriger
			int k = expand(filsItem, null, -1) ;
			if (3 == k)
			{
				if (filsItem.isExpandable())
				{
					if (false == filsItem.getSons().isEmpty())
					{
						BBItem bbitem = filsItem.getSons().iterator().next() ;
						// Iter en tant que BBItem
						if (null != bbitem)
							bbitem.activate() ;
					}
				}
			}
			updatePatpatho() ;
			return true ;
		}
		
		for (Iterator<BBFilsItem> itr = _aBBItemFils.iterator() ; itr.hasNext() ; )
		{	
			BBFilsItem itrFilsItem = itr.next() ;

			if (itrFilsItem.isInEditMode())
	    {
	      _BigBoss.setEditionMode(false) ;
	      PPT.clear() ;
	      if ((false == itrFilsItem.IsUnique()) && (null != _CurrentPatPatho))
	      {
	      	LdvModelNode nodeToEdit = null ;
	      	
	      	boolean bFoundEditNode = false ;
	      	for (Iterator<LdvModelNode> pptIt = _CurrentPatPatho.iterator() ; pptIt.hasNext() && (false == bFoundEditNode) ; )
	      	{
	      		nodeToEdit = pptIt.next() ; 
	      		if (1 == nodeToEdit.getLocalID())
	      			bFoundEditNode = true ;
	      	}

	      	if (bFoundEditNode)
	      		_CurrentPatPatho.extractPatPatho(nodeToEdit, PPT) ;
	      }

	      int m = expand(itrFilsItem, PPT, -1) ;
	      if (0 != m)
	      {
	        boolean bLookForFatherOD = true ;
	        BBItem fatherItem = itrFilsItem.getFatherItem() ;
	        while (bLookForFatherOD)
	        {
	          if (null == fatherItem)
	          	bLookForFatherOD = false ;
	          else
	          {
	            if (fatherItem.linkedToUI())
	            	bLookForFatherOD = false ;
	            else
	            {
	              if (null != fatherItem.getBBFatherFils())
	              	fatherItem = fatherItem.getBBFatherFils().getFatherItem() ;
	              else
	              	fatherItem = null ;
	            }
	          }
	        }
	        if (null != fatherItem)
	        	fatherItem.activate() ;
	      }
	      updatePatpatho() ;
	      return true ;
	    }
	  }
		
		return false ;
	}

	/**
	 * Find the son whose patpatho contains the element with ID = 1
	 * 
	 **/
	protected BBFilsItem findSonToEdit()
	{
		if (_aBBItemFils.isEmpty())
			return null ;
		
		for (Iterator<BBFilsItem> itr = _aBBItemFils.iterator() ; itr.hasNext() ; )
		{
			BBFilsItem son = itr.next() ;
		
			if (false == son.hasEmptyPatPatho())
			{
				BBVectFatheredPatPathoArray aVFPpt = son.getPatPatho() ;
				if ((null != aVFPpt) && (false == aVFPpt.isEmpty()))
				{
					for (Iterator<BBFatheredPatPathoArray> iterPath = aVFPpt.iterator() ; iterPath.hasNext() ; )
					{
						LdvModelNodeArray PPT = iterPath.next().getPatPatho() ;
						if ((null != PPT) && (false == PPT.isEmpty()))
							for (Iterator<LdvModelNode> pptIt = PPT.iterator() ; pptIt.hasNext() ; )
								if (1 == pptIt.next().getLocalID())
									return son ;
					}
				}
			}
		}
		
		return null ;
	}
	
	protected int activateForFunction()
	{
	  // The function can either behave as a dialog box (and it returns 0), or like a  comme un aiguillage, et elle choisi le premier fils actif
	  //
	  boolean bKeepRunning = true ;
	  if (_BigBoss.hasEmptyMessageStack())
	  	bKeepRunning = _bbFunction.execute(BBFunction.ACTIONTYPE.fctActivate, _iFocusedFilItem) ;

	  BBCmdMessage BBMsg = null ;

	  int i = 0 ;
	  while (bKeepRunning)
	  {
	    if (_BigBoss.hasEmptyMessageStack())
	    {
	      i = expand(getFilsItemAt(_iFocusedFilItem - 1), null, -1) ;
	      bKeepRunning = _bbFunction.execute(BBFunction.ACTIONTYPE.fctSwitch, i) ;

	      updatePatpatho() ;
	    }
	    else
	      i = _BigBoss.unstack(BBMsg) ;

	    if (bKeepRunning)
	    {
	      switch (i)
	      {
	        case BBCmdMessage.NSDLG_NEXT : // go to next step 

	        	_iFocusedFilItem++ ;
	        	
	          // If there is no next FilsItem, going out down
	          if (null == getFocusedFilsItem())
	          {
	            activateFromEnd() ;
	            return 0 ;
	          }
	          
	          break ;

	        case BBCmdMessage.NSDLG_RETURN : // go to previous step

	        	_iFocusedFilItem-- ;
	        	
	          // If we already were at beginning, going out up
	          if (0 == _iFocusedFilItem)
	            return -1 ;
	          
	          break ;

	        case BBCmdMessage.NSDLG_EXIT_DOWN : // going out down

	          activateFromEnd() ;
	          return 0 ;

	        case BBCmdMessage.NSDLG_EXIT_UP  : // going out up

	          return -1 ;

	        case BBCmdMessage.NSDLG_GO_TO : // Processing message to get location and label to reach

	          if (activateGoTo(BBMsg))
	            return 0 ;
	          break ;

	        default :

	          if ((i >= BBCmdMessage.NSDLGRETURN_DIRECT) && (i < BBCmdMessage.NSDLGRETURN_END))
	          {
	            // iFilsActif = i - NSDLGRETOUR_DIRECT + 1 ;
	          	bKeepRunning = false ;
	          }
	          else if ((i > 0) && (null != getFilsItemAt(i-1)))
	          	_iFocusedFilItem = i ;
	      }
	    }
	    else
	      activateFromEnd() ;
	  }
	  return i ;
	}
	
	protected void activateFromEnd()
	{
	  if (null == _BBFatherFils)
	  	return ;
	  
	  if (_BBFatherFils.hasEmptyPatPatho())
	  {
	  	if (isActiveWhenEmpty())
	  		_BBFatherFils.Activate() ;
	  	else
	  		_BBFatherFils.Unactivate() ;
	  }
	  else
	  	_BBFatherFils.Activate() ;
	}
	
	protected boolean activateGoTo(final BBCmdMessage BBMsg)
	{
	  if ((null == BBMsg) || _aBBItemFils.isEmpty())
	    return false ;

	  String sMessage = BBMsg.getMessage() ;

	  int pos = sMessage.indexOf(LdvGraphConfig.nodeSeparationMARK) ;
	  if ((-1 == pos) || (pos >= sMessage.length()))
	    return false ;

	  String sLocalis = sMessage.substring(0, pos) ;
	  String sEtiquet = sMessage.substring(pos + 1, sMessage.length()) ;

	  Iterator<BBFilsItem> ItFils = _aBBItemFils.iterator() ;
	  
	  boolean bPathFound = false ;

	  // 3 cases :
	  // 1) sLocalisation equals sLocalis: we set the focus to the BBFilsItem with proper label
	  // 2) sLocalis starts with sLocalisation: we activate the label that goes in proper direction
	  // 3) finally: we go down one step
	  //
	  if (_sLocalization.equals(sLocalis))
	  {
	    int iBonFils = 1 ;
	    
	    for (Iterator<BBFilsItem> itr = _aBBItemFils.iterator() ; itr.hasNext() && (false == bPathFound) ; )
	    {
	    	if (itr.next().getLabel() == sEtiquet)
	    		bPathFound = true ;
	    	else
	    		iBonFils++ ;
	    }

	    if (true == bPathFound)
	    	_iFocusedFilItem = iBonFils ;
	  }
	  else if ((_sLocalization.length() < sLocalis.length()) && _sLocalization.equals(sLocalis.substring(0, _sLocalization.length())))
	  {
	    int iBonFils = 1 ;

	    // Looking for the label that goes in the good direction
	    //
	    for (Iterator<BBFilsItem> itr = _aBBItemFils.iterator() ; itr.hasNext() && (false == bPathFound) ; )
	    {
	      iBonFils++ ;
	      String sGag = _sLocalization + itr.next().getLabel() ;
	      if ((sGag.length() <= sLocalis.length()) && sGag.equals(sLocalis.substring(0, sGag.length())))
	      	bPathFound = true ;
	    }
	    if (true == bPathFound)
	    {
	    	_iFocusedFilItem = iBonFils ;
	      _BigBoss.stackAgain(BBMsg) ;
	    }
	  }

	  // Re-stacking and going out down
	  //
	  if (false == bPathFound)
	  {
	  	_BigBoss.stackAgain(BBMsg) ;
	    activateFromEnd() ;
	    return true ;
	  }

	  return false ;
	}

	
//-----------------------------------------------------------------------------
//mise � jour du patpatho dans les cas :
//   - Si le BBItem est rattach� purement � une fonction
//		- Si le BBItem n'est pas rattach� � une boite de dialogue, il active sa
//			premi�re branche si il est en mode "automatique", il propose ses fils si
//			il est en mode "manuel"
//-----------------------------------------------------------------------------
	protected void updatePatpatho()
	{
		if (null == _CurrentPatPatho)
			return ;

		_CurrentPatPatho.clear() ;

		if (_aBBItemFils.isEmpty())
			return ;

		FilGuideSearchElementVector SearchVector = new FilGuideSearchElementVector() ;
		boolean bMustSearch = false ;
		
		for (Iterator<BBFilsItem> itr = _aBBItemFils.iterator() ; itr.hasNext() ; )
		{
			BBFilsItem son = itr.next() ;
			
			if ((null == son.getItemData()) || ("".equals(son.getItemData().getPath())))
			{
				String sSemanticCode = LdvModelNode.getSemanticCode(son.getLabel()) ;
				SearchVector.AddLabel(sSemanticCode) ;
				bMustSearch = true ;
			}
		}
		
		if (SearchVector.isEmpty())
			updatePatpathoSecondStep(SearchVector) ;
		else
			getFilsGuides(SearchVector, false, "updatePatpatho") ;
	}

	protected void updatePatpathoSecondStep(final FilGuideSearchElementVector SearchVector)
	{
		boolean bSearched = false ;
		if ((null != SearchVector) && (false == SearchVector.isEmpty()))
			bSearched = true ;
		
		for (Iterator<BBFilsItem> itr = _aBBItemFils.iterator() ; itr.hasNext() ; )
		{
			BBFilsItem son = itr.next() ;
		
			boolean bFound = false ;
			
			if (bSearched && ((null == son.getItemData()) || ("".equals(son.getItemData().getPath()))))
			{
				String sSemanticCode = LdvModelNode.getSemanticCode(son.getLabel()) ;
				bFound = SearchVector.SetData(sSemanticCode, son.getItemData()) ;
			}
			else
				bFound = true ;

			if (bFound)
			{
				if (son.isActivated())
				{
					int iColumn = 0 ;
					BBVectFatheredPatPathoArray patpathoItem = son.getPatPatho() ;
					
					// Not a multiple description
					//
					if (son.IsUnique())
					{
						// If this element doesn't have a zero shift, it adds its label
						//
						if (false == son.isZeroShift())
						{
							iColumn = 1 ;
							addLabel(son, "") ;
						}
						//
						// Then it adds its patpathos (there should be only one)
						//
						if ((null != patpathoItem) && (false == patpathoItem.isEmpty()))
						{
							for (Iterator<BBFatheredPatPathoArray> iterPath = patpathoItem.iterator() ; iterPath.hasNext() ; )
							{
								_CurrentPatPatho.addVector(iterPath.next().getPatPatho(), LdvModelNodeArray.ORIGINE_PATH_PATHO + iColumn) ;
								iColumn = 1 ;
							}
						}
					}
					//
					// Multiple description
					//
					else
					{
						// In case of a multiple description, the label must be duplicated for each instance 
		        // 
		        // For example :
		        //  Iter = PINQ9 (multiple description)
		        //    with two patpathos : 1) PIVG(0,0)
		        //												 2) PIVD(0,0)
		        //	then the final patpatho will be  
		        //     PINQ9(0,0)
		        //        PIVG(1,1)
		        //     PINQ9(2,0)
						//	      PIVD(3,1)
						//
						if (false == son.isZeroShift())
						{
							if ((null != patpathoItem) && (false == patpathoItem.isEmpty()))
							{
								for (Iterator<BBFatheredPatPathoArray> iterPath = patpathoItem.iterator() ; iterPath.hasNext() ; )
								{
									BBFatheredPatPathoArray fatheredPPT = iterPath.next() ; 
									
									addLabel(son, "", fatheredPPT.getFatherNode(), true) ;
									_CurrentPatPatho.addVector(fatheredPPT.getPatPatho(), LdvModelNodeArray.ORIGINE_PATH_PATHO + 1) ;
								}
							}
						}
						//
						// If this element has a zero shift, it doesn't add its label
						//
						else
						{
							iColumn = 0 ;
							if ((null != patpathoItem) && (false == patpathoItem.isEmpty()))
							{
								for (Iterator<BBFatheredPatPathoArray> iterPath = patpathoItem.iterator() ; iterPath.hasNext() ; )
								{
									_CurrentPatPatho.addVector(iterPath.next().getPatPatho(), LdvModelNodeArray.ORIGINE_PATH_PATHO + iColumn) ;
									iColumn = 1 ;
								}
							}
						}
					}
				}
			}
		}	
	}

	/**
		* Rebuild the patpathoInfo of the child node from it's transfert data <br>
		* Then add the pathpatoInfo to _CurrentPatPatho 
		*
		* @param son           child node to get information from
		* @param sArc          archetype identifier
		* @param previousNode  the node in _CurrentPatPatho that the child node is editing
		* @param bForceEmptyId if <code>true</code> then empty tree and node Id if no previousNode
		* 
		**/
	public void addLabel(final BBFilsItem son, final String sArc, final LdvModelNode previousNode, boolean bForceEmptyId)
	{
		if (null == _CurrentPatPatho)
			_CurrentPatPatho = new LdvModelNodeArray() ;
		
		addLabelToPpt(_CurrentPatPatho, son, sArc, previousNode, bForceEmptyId) ;
		
		/*
		if ((null == son) || (null == son.getTransfertMessage()))
			return ;

		BBMessage tranfertMsg = son.getTransfertMessage() ;
		if (null == tranfertMsg)
			return ;

		// Creating a BBMessage from BBFilsItem's transfert message and setting it's tree and node IDs  
		//
		BBMessage Msg = new BBMessage(tranfertMsg) ;

		if (null != previousNode)
		{
			Msg.setTreeID(previousNode.getTreeID()) ;
			Msg.setNodeID(previousNode.getNodeID()) ;

			if (null != previousNode.getTemporaryLinks())
				Msg.SetTemporaryLinks(previousNode.getTemporaryLinks()) ;
		}
		else if (bForceEmptyId)
		{
			Msg.setTreeID("") ;
			Msg.setNodeID("") ;
		}

		// Interest is "A" by default
		//
		if ("".equals(Msg.getInterest()))
		{
			Msg.setInterest("A") ;
			tranfertMsg.setInterest("A") ;
		}

		// Setting archetype ID
		//
		if ((false == "".equals(sArc)) && (false == sArc.equals(Msg.getArchetype())))
		{
			Msg.setArchetype(sArc) ;
			tranfertMsg.setArchetype(sArc) ;
		}

		String sLabel = son.getLabel() ;

		// Free text: label is �?????
		//
		if (LdvModelNode.isFreeText(sLabel))
		{
			if (null != _CurrentPatPatho)
				_CurrentPatPatho.addNode(sLabel, Msg, LdvModelNodeArray.ORIGINE_PATH_PATHO + 0) ;
			return ;
		}

		// Parse the label using a BBMessage 
		//
		BBMessage labelMsg = new BBMessage() ;
		labelMsg.initFromLabel(sLabel) ;
		
		// If there is a complement and it is a numerical value, then removing useless '0'
		//
		String sComplValue = labelMsg.getComplement() ;
		if (false == "".equals(sComplValue))
		{
			String sFollowPound = LdvModelNode.followsPound(labelMsg.getLexique()) ;
			if ((false == "".equals(sFollowPound)) && ('N' == sFollowPound.charAt(0)))
			{
				MiscellanousFcts.strip(sComplValue, MiscellanousFcts.STRIP_DIRECTION.stripLeft, '0') ;
				if ("".equals(sComplValue))
					sComplValue = "0" ;
			}
			
			tranfertMsg.setComplement(sComplValue) ;
			Msg.setComplement(sComplValue) ;
		}
		
		tranfertMsg.setUnit(labelMsg.getUnit()) ;
		Msg.setUnit(labelMsg.getUnit()) ;
		
		tranfertMsg.setPlural(labelMsg.getPlural()) ;
		Msg.setPlural(labelMsg.getPlural()) ;
		
		// WCEA0 in certainty means 100% and is the default value, don't take it into account
		//
		String sCertitude = labelMsg.getCertitude() ;
		if (false == "".equals(sCertitude))
		{
			if ((sCertitude.length() >= 5) && "WCEA0".equals(sCertitude.substring(0, 5)))
				sCertitude = "" ;
			
			tranfertMsg.setCertitude(labelMsg.getCertitude()) ;
			Msg.setCertitude(labelMsg.getCertitude()) ;
		}
		
		_CurrentPatPatho.addNode(labelMsg.getLexique(), Msg, LdvModelNodeArray.ORIGINE_PATH_PATHO + 0) ;
		*/		
	}
		
	/**
	* Rebuild the patpathoInfo of the child node from it's transfert data <br>
	* Then add the pathpatoInfo to _CurrentPatPatho 
	*
	* @param son           child node to get information from
	* @param sArc          archetype identifier
	* @param previousNode  the node in _CurrentPatPatho that the child node is editing
	* @param bForceEmptyId if <code>true</code> then empty tree and node Id if no previousNode
	* 
	**/
	protected void addLabelToPpt(LdvModelNodeArray patPatho, final BBFilsItem son, final String sArc, final LdvModelNode previousNode, boolean bForceEmptyId)
	{
		if ((null == patPatho) || (null == son) || (null == son.getTransfertMessage()))
			return ;

		BBMessage tranfertMsg = son.getTransfertMessage() ;
		if (null == tranfertMsg)
			return ;

		// Creating a BBMessage from BBFilsItem's transfert message and setting it's tree and node IDs  
		//
		BBMessage Msg = new BBMessage(tranfertMsg) ;

		// If this node already existed (tree being edited), keep its previous identifiers
		// so it is not considered new
		//
		if (null != previousNode)
		{
			Msg.setTreeID(previousNode.getTreeID()) ;
			Msg.setNodeID(previousNode.getNodeID()) ;

			if (null != previousNode.getTemporaryLinks())
				Msg.SetTemporaryLinks(previousNode.getTemporaryLinks()) ;
		}
		else if (bForceEmptyId)
		{
			Msg.setTreeID("") ;
			Msg.setNodeID("") ;
		}

		// Interest is "A" by default
		//
		if ("".equals(Msg.getInterest()))
		{
			Msg.setInterest("A") ;
			tranfertMsg.setInterest("A") ;
		}

		// Setting archetype ID
		//
		if ((false == "".equals(sArc)) && (false == sArc.equals(Msg.getArchetype())))
		{
			Msg.setArchetype(sArc) ;
			tranfertMsg.setArchetype(sArc) ;
		}

		String sLabel = son.getLabel() ;

		// Free text: label is �?????
		//
		if (LdvModelNode.isFreeText(sLabel))
		{
			patPatho.addNode(sLabel, Msg, LdvModelNodeArray.ORIGINE_PATH_PATHO + 0) ;
			return ;
		}

		// Parse the label using a BBMessage 
		//
		BBMessage labelMsg = new BBMessage() ;
		labelMsg.initFromLabel(sLabel) ;
	
		// If there is a complement and it is a numerical value, then removing useless '0'
		//
		String sComplValue = labelMsg.getComplement() ;
		if (false == "".equals(sComplValue))
		{
			String sFollowPound = LdvModelNode.followsPound(labelMsg.getLexique()) ;
			if ((false == "".equals(sFollowPound)) && ('N' == sFollowPound.charAt(0)))
			{
				MiscellanousFcts.strip(sComplValue, MiscellanousFcts.STRIP_DIRECTION.stripLeft, '0') ;
				if ("".equals(sComplValue))
					sComplValue = "0" ;
			}
		
			tranfertMsg.setComplement(sComplValue) ;
			Msg.setComplement(sComplValue) ;
		}
	
		tranfertMsg.setUnit(labelMsg.getUnit()) ;
		Msg.setUnit(labelMsg.getUnit()) ;
	
		tranfertMsg.setPlural(labelMsg.getPlural()) ;
		Msg.setPlural(labelMsg.getPlural()) ;
	
		// WCEA0 in certainty means 100% and is the default value, don't take it into account
		//
		String sCertitude = labelMsg.getCertitude() ;
		if (false == "".equals(sCertitude))
		{
			if ((sCertitude.length() >= 5) && "WCEA0".equals(sCertitude.substring(0, 5)))
				sCertitude = "" ;
		
			tranfertMsg.setCertitude(labelMsg.getCertitude()) ;
			Msg.setCertitude(labelMsg.getCertitude()) ;
		}
	
		patPatho.addNode(labelMsg.getLexique(), Msg, LdvModelNodeArray.ORIGINE_PATH_PATHO) ;		
	}
	
	/**
	* Add the label at end, without forcing identifiers to empty 
	* 
	**/
	public void addLabel(final BBFilsItem son, final String sArc) {
		addLabel(son, sArc, null, false) ;
	}

	/**
	* Add a tree to the end of _CurrentPatPatho, with a cloumn shift of iColShift 
	* 
	**/
	public void addVector(final LdvModelNodeArray pptToInsert, int iColShift)
	{
		if ((null == pptToInsert) || (pptToInsert.isEmpty()))
	    return ;
		
		if (null == _CurrentPatPatho)
			_CurrentPatPatho = new LdvModelNodeArray() ;
		
		_CurrentPatPatho.addVector(pptToInsert, iColShift) ;
	}
	
	/**
	 * Asks the BBItem to get ready. It creates a new BBFilsItem for each of its labels, then
	 * stores them in _aBBItemFils and, finally, execute createSons to iterate the creation of
	 * command elements for the whole Panel
	 * 
	 * @param bDontDispatch If the element controlled by this BBItem is a tree window, there is no need to call dispatch since it will do it itself
	 * 
	 **/
	protected int create(boolean bDontDispatch)
	{
		// If this element has a function attached to it, we create it
		//
		if (isLinkedToFunction())
		{
			if (null != _bbFunction)
				_bbFunction = null ;

			_bbFunction = new BBFunction(this, _Data.getFunctionName()) ;
		}

		// If this BBItem commands an Archetype
		//
		if (OpensArchetype())
		{
			String sArchetype = _Data.getSons() ;
/*
			switch(iArcType)
			{
				case NSArcManager::archetype :
					return createFromArchetype(Consultation, sArchetypeFile) ;
				case NSArcManager::decisionTree :
					return createFromDecisionTree(Consultation, sArchetypeFile) ;
			}
*/
		}

		// If this item has no son, we can leave
		if (false == _Data.hasSons())
			return 0 ;

		// Parsing sons String in order to create a BBFilsItem for each label
		//
		String sSonsList = _Data.getSons().trim() ;
		int    iSonsSize = sSonsList.length() ; 
		
		FilGuideSearchElementVector SearchVector = new FilGuideSearchElementVector() ;
		
		for (int i = 0 ; i < iSonsSize ; i++)
		{
			// Creating a BBFilsItem
			BBFilsItem filsItem = new BBFilsItem(this, _BigBoss, _supervisor) ;

			// Getting label
			String sNewLabel = "" ;
			for ( ; (i < iSonsSize) && (LdvGraphConfig.nodeSeparationMARK.charAt(0) != sSonsList.charAt(i)) ; i++)
				sNewLabel += sSonsList.charAt(i) ;
			filsItem.setLabel(sNewLabel) ;

			// Building a path for the new element

			// traiter le cas des textes libres � part
			if (false == filsItem.isFreeText())
			{
				String sCodeSens = LdvModelNode.getSemanticCode(filsItem.getLabel()) ;
				// VecteurSelonCritere.AjouteEtiquette(sCodeSens) ;
				SearchVector.AddLabel(sCodeSens) ;
			}

			// Adding the new element to the array of sons 
			_aBBItemFils.addElement(filsItem) ;
		}
		
		if (SearchVector.isEmpty())
			createSecondStep(SearchVector, bDontDispatch) ;
		else
			getFilsGuides(SearchVector, bDontDispatch, "create") ;
		
		return 0 ;
	}
	
	private void createSecondStep(FilGuideSearchElementVector SearchVector, boolean bDontDispatch)
	{
		if (false == _aBBItemFils.isEmpty())
		{
			for (Iterator<BBFilsItem> itr = _aBBItemFils.iterator() ; itr.hasNext() ; )
			{
				BBFilsItem filsItem = itr.next() ;
				
				// Dedicated treatment for Free text
				if (filsItem.isFreeText())
				{
					BBItemData data = filsItem.getItemData() ;
					if (null != data)
					{
						data.init() ;
						data.setPath("~****/#####") ;
						data.setSons("#TLI#1") ;
						data.setDialogName("TEXTLIB") ;
						data.setOpenDialog(true) ;
						data.setLevelShift("+00+00") ;
						data.setDialogFile("NSBB") ;
						data.setActiveWhenEmpty(false) ;
					}
					filsItem.setIsExpandable(true) ;
				}
				else
				{
					String sCodeSens = LdvModelNode.getSemanticCode(filsItem.getLabel()) ;
					
					if ((null != SearchVector) && SearchVector.SetData(sCodeSens, filsItem.getItemData()))
					{
						BBItemData data = filsItem.getItemData() ;
						if ((null == data) || "".equals(data.getSons().trim()))
							filsItem.setIsExpandable(false) ;
						else
							filsItem.setIsExpandable(true) ;
					}
					else
						filsItem.setIsExpandable(false) ;
				}
			}
		}

		// Don't do that before DispatcherPatPatho() because it can lead to
		// potential active item excluding the good ones
		// createExclusionStructure() ;

		// M.A.J des patpatho
		//
		if (false == bDontDispatch)
			if (null != _CurrentPatPatho)
				DispatchPatPatho() ;

		createExclusionStructure() ;
		createSons() ;
	}
	
	private int createSonsInArchetype(LdvArchetypeItem archetypeItem, boolean bDontDispatch)
	{
		if (null == archetypeItem)
			return 0 ;

		// string sMsg = "creerArchetype() : Entr�e" ;
		// _pSuper->trace(&sMsg, 1, NSSuper::trSubDetails) ;

	  Vector<LdvArchetypeItem> aSubItems = archetypeItem.getSubItems() ;

		// If current element has no sons, there is nothing to do
	  //
		if ((null == aSubItems) || (aSubItems.isEmpty()))
			return 0 ;

		// Vector of leaves to have continuation been found in Fils Guides 
		//
	  FilGuideSearchElementVector SearchVector = new FilGuideSearchElementVector() ;

		// Browsing direct sons
	  //
		for (Iterator<LdvArchetypeItem> itemItr = aSubItems.iterator() ; itemItr.hasNext() ; )
		{
			LdvArchetypeItem sonItem = itemItr.next() ;
			
			BBFilsItem filsItem = new BBFilsItem(this, _BigBoss, _supervisor) ;
			
			// Storing ArchetypeItem in the BBFilsItem
			//
			filsItem.setArchetypeItem(sonItem) ; 
			
			// Getting the label
			filsItem.setLabel(sonItem.getCode()) ;

			// sMsg = "creerArchetype() : Nouveau fils " + pFilsItem->getItemLabel() ;
			// _pSuper->trace(&sMsg, 1, NSSuper::trSubDetails) ;

			// If the shift level is not specified, we set it to the default value
			//
			String sShift = sonItem.getShiftLevel() ;
			if ("".equals(sShift))           
				sShift = "+01+01" ;
			//
			// then we initialize 
			//
			filsItem.setLevelShift(sShift) ;

			// If a leave (and not a free text)
			//
			if ((false == filsItem.isFreeText()) && sonItem.getSubItems().isEmpty())
			{
				// If this leaf doesn't statically point to an Archetype... and Fils Guides are to be used
				//
				if ("".equals(sonItem.getArchetype()) && _BigBoss.shouldUseFilsGuides())
				{
					// on ajoute au vecteur de recherche
					String sCodeSens = LdvModelNode.getSemanticCode(filsItem.getLabel()) ;
					SearchVector.AddLabel(sCodeSens) ;
				}
			}

			// Adding the BBFilsItem to the array of sons
			_aBBItemFils.addElement(filsItem) ;
		}
		
		// Browsing constraints
		//
		Vector<LdvArchetypeItemConstraint> aConstraints = archetypeItem.getConstraints() ;
		
		if (false == aConstraints.isEmpty())
		{
			for (Iterator<LdvArchetypeItemConstraint> constraintItr = aConstraints.iterator() ; constraintItr.hasNext() ; )
			{
				LdvArchetypeItemConstraint constraint = constraintItr.next() ;
				if (LdvArchetypeItemConstraint.VAL_ATTR_CONTR_TYPE_EXCLUS == constraint.getType())
					_Data.setExclusions(constraint.getList()) ;
			}
		}
		
		if (SearchVector.isEmpty())
			createArchetypeSecondStep(SearchVector, bDontDispatch) ;
		else
			getFilsGuides(SearchVector, bDontDispatch, "createArchetype") ;
		
		return 0 ;
	}
	
	protected int createUserInterface()
	{
		return 0 ;
	}

	public BBDialogBox getDialog() {
		return _dialogBox ;
	}
	public void setDialog(BBDialogBox dialog) {
		_dialogBox = dialog ;
	}

	public BBItemData getData() {
		return _Data ;
	}
	public void setData(BBItemData Data) {
		_Data = Data ;
	}
	
	public boolean isPlusZero() {
	  return ("+00+00".equals(_Data.getLevelShift())) ;
	}

	public String getLocalization() {
		return _sLocalization ;
	}
	public void setLocalization(String sLocalization) {
		_sLocalization = sLocalization ;
	}
	
	public String getDialogName() {
		return _sDialogName ;
	}
	public void setDialogName(String sDialogName) {
		_sDialogName = sDialogName ;
	}

	public boolean linkedToUI()
	{
		if ("__CROS".equals(_sDialogName))
			return false ;

		if (false == "".equals(_sDialogName.trim()))
			return true ;

	  String sLang = "" ;
	  if (null != _BigBoss)
	  	sLang = _BigBoss.getLanguage() ;

	  return (existArchetypeDialog(sLang)) ;
	}
	
	public boolean existArchetypeDialog(String sLang)
	{
		if (null == _Archetype)
			return false ;
		
		return (null != _Archetype.getDialogBox(sLang)) ;
	}
	
	public LdvModelNodeArray getCurrentPatPatho() {
		return _CurrentPatPatho ;
	}
	public void setCurrentPatPatho(LdvModelNodeArray CurrentPatPatho) {
		_CurrentPatPatho = CurrentPatPatho ;
	}

	public BBFilsItem getBBFatherFils() {
		return _BBFatherFils ;
	}
	public void setBBFatherFils(BBFilsItem BBFilsPere) {
		_BBFatherFils = BBFilsPere ;
	}
	
	public BBSmallBrother getBigBoss() {    
		return _BigBoss ;
	}
	
	public boolean isLinkedToFunction()
	{
		if (null == _Data)
			return false ;
		
		return (false == "".equals(_Data.getFunctionName().trim())) ;
	}
	
	public boolean isActiveWhenEmpty() {
		return _Data.IsActiveWhenEmpty() ;
	}
	
	public boolean OpensArchetype() {
		return _Data.OpensArchetype() ;
	}
	
	public boolean isUnique() {
		return _Data.IsUnique() ;
	}
	
	/**
	 * Get the complement as a string "homogeneous with" a Lexicon (same length) starting with a '$'<br>
	 * examples: 1.5 -> $001.5 and 1 -> $00001      
	 * 
	 * @param sComplement the complement to be processed
	 * 
	 * @return An empty string if the complement cannot be processed (empty or too large), the processed value if possible
	 * 
	 **/
	protected String getLexiconSizedComplement(String sComplement)
	{
		if ((null == sComplement) || "".equals(sComplement))
			return "" ;
		
		int iCompLen = sComplement.length() ;
		
		if (iCompLen > LdvGraphConfig.LEXI_LEN - 1)
			return "" ;
		
		String sFiller = "$" ;
	
		for (int i = 1 ; i < LdvGraphConfig.LEXI_LEN - iCompLen ; i++)
			sFiller += "0" ;
		
		return sFiller + sComplement ;
	}

	public void ActivateFather(BBFilsItem son)
	{
		if (null == son)
			return ;
		
		KillsExcluded(son) ;
		
		if (null == _BBFatherFils)
			return ;
		
		// Attention : si on ne teste que dansMemeDialogue, il y a un bug
		// (retour vers pr�ambule dans �cho-coeur efface les donn�es descriptives)
		if (_sDialogName.equals(_BBFatherFils.getFatherItem()._sDialogName) ||
	                            isInSamePanel(_BBFatherFils.getFatherItem()))
		{
			BBTransferInfo transfert = _BBFatherFils.getTransfert() ; 
			
			if (null != transfert)
				transfert.Activate() ;
			if (null != _BBFatherFils.getFatherItem())
				_BBFatherFils.getFatherItem().ActivateFather(_BBFatherFils) ;

			if ((null != transfert) &&
	      		(null != transfert.getControl()))
				transfert.activateControl(CTRL_ACTIVATION.checked, _BBFatherFils.getTransfertMessage()) ;
		}
	}
	
	/**
	 * Get the complement as a string "homogeneous with" a Lexicon (same length) starting with a '$'<br>
	 * examples: 1.5 -> $001.5 and 1 -> $00001      
	 * 
	 * @param sComplement the complement to be processed
	 * 
	 * @return An empty string if the complement cannot be processed (empty or too large), the processed value if possible
	 * 
	 **/
	protected int createSons() 
	{
		// If not linked to any user interface or no existing son, nothing to do
		//
		if ((false == linkedToUI()) || _aBBItemFils.isEmpty())
			return 0 ;
		
		// Browsing all declared sons
		//
		for (Iterator<BBFilsItem> itr = _aBBItemFils.iterator() ; itr.hasNext() ; )
		{
			BBFilsItem son = itr.next() ;

			if (son.isExpandable())
			{
	      // A new BBItem is only created if it belongs to the same user interface and is not a leaf
				//
	      String sUIName = son.getDialogName() ;

	      if (son.isDialogNameIdem() ||
	              sUIName.equals(_Data.getDialogName()) &&
	                          (false == son.isDialogNameAuto())
	         )
	      {
	        // Creating a BBItem
	        son.createSons(-1) ;

	        // Initializing the BBItem
	        // set it the proper patpthoarray if its label can be found
	        //
	        String sLabel = son.getLabel() ;
	        if (false == son.getSons().isEmpty())
	        {
	        	for (Iterator<BBItem> iter = son.getSons().iterator() ; iter.hasNext() ; )
	          {
	          	BBItem sonItem = iter.next() ;
	          	
	            setDNA(sonItem, sLabel) ;

	            // On demande au BBItem de se cr�er
	            sonItem.create(false) ;
	          }
	        }
	      }
	    }
	  }
		return 0 ;
	}
	
	/**
	 * Unactivate all the sons that are excluded by another one       
	 * 
	 * @param son the son that excludes others
	 * 
	 **/
	public void KillsExcluded(BBFilsItem son)
	{
		if (null == son)
			return ;
		
		// Special processing for the BBFilsItem that command a EditLexique
	  // they must ask their creating father (of the �C0;020 kind) to deactivate their brothers
		//
		BBItem sonsFatherItem = son.getFatherItem() ;
		if (null != sonsFatherItem)
		{
			BBFilsItem littleSon = sonsFatherItem.getBBFatherFils() ;
			if (null != littleSon)
			{
				String sIdentite = littleSon.getLabel() ;
				if (sIdentite.contains(String.valueOf(LdvGraphConfig.POUND_CHAR) + "C;") || 
						sIdentite.contains("/" + String.valueOf(LdvGraphConfig.POUND_CHAR) + "C;") || 
						sIdentite.contains(String.valueOf(LdvGraphConfig.POUND_CHAR) + "CX"))
				{
					BBItem grandFatherItem = littleSon.getFatherItem() ;
					if (null != grandFatherItem)
						grandFatherItem.KillsExcluded(littleSon) ;
				}
			}
		}
		
		if (son.getExcluded().isEmpty())
			return ;
		
		String sSonLabel = son.getLabel() ;
		
		// Browsing through the list of labels that are mutually exclusive with this BBFilsItem 
		//
		for (Iterator<String> stringItr = son.getExcluded().iterator() ; stringItr.hasNext() ; )
		{
			String sExcluded = stringItr.next() ;
			
			// BBFilsItem's label can appear in the list... take care not to mutually exclude with thyself
			//
			if (false == sExcluded.equals(sSonLabel))
			{
				// Looking for the son whose label is excluded
				//
				if (false == _aBBItemFils.isEmpty())
				{
					BBFilsItem foundItem = null ;
					boolean    bFound    = false ;
					
					Iterator<BBFilsItem> itr = _aBBItemFils.iterator() ;
					for ( ; itr.hasNext() ; )
					{
						foundItem = itr.next() ;
						if (sExcluded.equals(foundItem.getLabel()))
						{
							bFound = true ;
							break ;
						}
					}
					
					if (bFound && (null != foundItem) && (false == "".equals(foundItem.getLabel())))
						foundItem.fullyUnactivate() ;
				}
			}
		}
	}

	/**
	 * Are this BBItem and the other one referenced for the same panel?       
	 * 
	 * @param otherItem the other BBItem to be checked
	 * 
	 * @return <code>true</code> if both BBItem are referenced for the same interface element, <code>false</code> if one or the both of them are not attached to an interface element or if they are, but not to the same one
	 * 
	 **/
	public boolean isInSamePanel(BBItem otherItem)
	{
		if (null == otherItem)
			return false ;
		
		if ((false == "".equals(_sArchetypeId)) && (false == "".equals(otherItem._sArchetypeId)) && _sArchetypeId.equals(otherItem._sArchetypeId))
			return true ;
		
		if ((false == "".equals(_sDialogName)) && (false == "".equals(otherItem._sDialogName)) && _sDialogName.equals(otherItem._sDialogName))
			return true ;
		
		return false ;
	}
	
	/**
	 * Iterate on all BBFilsItem sons asking them to deactivate       
	 * 
	 **/
	public void UnactivateSons()
  {
		if (_aBBItemFils.isEmpty())
			return ;
		
		for (Iterator<BBFilsItem> itr = _aBBItemFils.iterator() ; itr.hasNext() ; )
			itr.next().fullyUnactivate() ;
  }
	
	/**
	 * Unactivate fathers that are located in the same panel and don't command a control       
	 * 
	 **/
	public void UnactivateFakeFathers(BBFilsItem son)
  {
		if (null == son)
			return ;
		
		BBTransferInfo transfert = son.getTransfert() ;
		if (null != transfert)
			transfert.Unactivate() ;
		
		if (false == isInPanel())
			return ;
		
		if (null == _BBFatherFils)
			return ;
		
		BBItem grandFather = _BBFatherFils.getFatherItem() ;
		if (null == grandFather)
			return ;
		
		BBTransferInfo fathersTransfert = _BBFatherFils.getTransfert() ;
		if (null == fathersTransfert)
			return ;
		
		if (isInSamePanel(grandFather) && (null != fathersTransfert.getControl()))
			grandFather.UnactivateFakeFathers(_BBFatherFils) ;
  }
	
	/**
	 * Get the Archetype view or the dialog box, if any, this BBItem is attached to        
	 * 
	 * @return a Panel (as common subclass of BBArchetypePanel and BBDialogBox) or <code>null</code> if this BBItem is not attached to a panel
	 * 
	 **/
	public Panel getPanel()
	{
		// If this element is the root of all BBItems, it depends on the main window
		//
		if (null == _BBFatherFils)
		{
			if ((null != _archetypeView) && _archetypeView.isOpen())
				return _archetypeView ;
			else
				return null ;
		}
		
		BBItem fatherItem = _BBFatherFils.getFatherItem() ;
		if (null == fatherItem)
			return null ;
		
		if (null != fatherItem._dialogBox)
			return fatherItem._dialogBox ;
		
		// Finally, search recursively on father BBItem
		//
		return fatherItem.getPanel() ;
	}
	
	/**
	 * Is this BBItem is attached to a panel?        
	 * 
	 * @return <code>true</code> if this BBItem is involved in a currently active panel (archetype or dialog box), <code>false</code> if not
	 * 
	 **/
	public boolean isInPanel() {
		return (null != getPanel()) ;
	}
	
	/**
	 * From the exclusion string found in BBItemData, initialize the exclusion structure in sons          
	 * 
	 * @return <code>true</code> if the exclusion string can be properly parsed, <code>false</code> if not
	 * 
	 **/
	public boolean createExclusionStructure()
	{
		if (null == _Data)
			return false ;

		if (_aBBItemFils.isEmpty())
			return true ;
		
		// First, clear all existing exclusion information
		//
		for (Iterator<BBFilsItem> itr = _aBBItemFils.iterator() ; itr.hasNext() ; )
			itr.next().clearExcluded() ;
		
		String sExclusions = _Data.getExclusions().trim() ;
		if ("".equals(sExclusions))
			return true ;
		
		// Specific case of full exclusion: add every other son to the exclusion list of each son
		//
		if (sExclusions.contains("()"))
		{
			for (Iterator<BBFilsItem> itr = _aBBItemFils.iterator() ; itr.hasNext() ; )
			{
				BBFilsItem son = itr.next() ;
				String sSonLabel = son.getLabel() ;
				
				for (Iterator<BBFilsItem> itrIn = _aBBItemFils.iterator() ; itrIn.hasNext() ; )
				{
					BBFilsItem otherSon = itrIn.next() ;
					String sOtherSonLabel = otherSon.getLabel() ;
					
					if (false == sSonLabel.equals(sOtherSonLabel))
						son.addExcluded(sOtherSonLabel) ;
				}
			}
			
			return true ;
		}
		
		// Parsing the exclusion string
		//
		int iStringLen = sExclusions.length() ;
		
		for (int i = 0 ; i < iStringLen ; i++)
		{
			// Looking for an opening parenthesis
			//
			for ( ; (i < iStringLen) && ('(' != sExclusions.charAt(i)) ; i++) ;
			if (i >= iStringLen)
				return true ;
			
			// The string next to the parenthesis and before a minus provides the label of
			// the item that excludes (some) others
			//
			String sExcludingItemLabel = "" ;
			for ( ; (i < iStringLen) && (')' != sExclusions.charAt(i)) && ('-' != sExclusions.charAt(i)) ; i++)
				sExcludingItemLabel += sExclusions.charAt(i) ;
			sExcludingItemLabel = sExcludingItemLabel.trim() ;
			
			// Looking for the excluding son
			//
			BBFilsItem excludingSon = null ;
			for (Iterator<BBFilsItem> itr = _aBBItemFils.iterator() ; itr.hasNext() ; )
			{
				BBFilsItem son = itr.next() ;
				if (sExcludingItemLabel.equals(son.getLabel()))
					excludingSon = son ;
			}
			
			if (null == excludingSon)
				return false ;
			
			// The label of the excluding son is always followed by a minus sign
			//
			if ('-' != sExclusions.charAt(i))
				return false ;
			
			i++ ;
			
			// If there is a closing parenthesis next to the minus sign, it means that the excluding
			// son excludes all others
			//
			if (')' == sExclusions.charAt(i))
			{
				String sExcludingLabel = excludingSon.getLabel() ;
				
				// Adds the excluding label to the list of all other nodes, and all other labels
				// to the list of the excluding node
				//
				for (Iterator<BBFilsItem> itrIn = _aBBItemFils.iterator() ; itrIn.hasNext() ; )
				{
					BBFilsItem otherSon = itrIn.next() ;
					String sOtherSonLabel = otherSon.getLabel() ;
					
					if (false == sExcludingLabel.equals(sOtherSonLabel))
					{
						excludingSon.addExcluded(sOtherSonLabel) ;
						otherSon.addExcluded(sExcludingLabel) ;
					}
				}
			}
			else
			{
				// All excluded labels are separated by a '|' until the closing parenthesis is reached
				//
				for ( ; (i < iStringLen) && (')' != sExclusions.charAt(i)) ; i++)
				{
					String sExcludedLabel = "" ;
					for ( ; (i < iStringLen) && (')' != sExclusions.charAt(i)) && ('|' != sExclusions.charAt(i)) ; i++)
						sExcludedLabel += sExclusions.charAt(i) ;
					sExcludedLabel = sExcludedLabel.trim() ;
					
					// Looking for the excluded son
					//
					BBFilsItem excludedSon = null ;
					for (Iterator<BBFilsItem> itr = _aBBItemFils.iterator() ; itr.hasNext() ; )
					{
						BBFilsItem son = itr.next() ;
						if (sExcludedLabel.equals(son.getLabel()))
							excludedSon = son ;
					}
					
					if (null == excludedSon)
						return false ;
					
					// Adding the excluded label to the exclusion and vice versa
					//
					excludedSon.addExcluded(sExcludingItemLabel) ;
					excludingSon.addExcluded(sExcludedLabel) ;
					
					// Going forward
					//
					if ('|' == sExclusions.charAt(i))
						i++ ;
				}
			}
			if (i >= iStringLen)
				return true ;
		}
		
		return true ;
	}

	/**
	 * @return <code>true</code> if the exclusion string can be properly parsed, <code>false</code> if not
	 * 
	 **/
	public boolean createSonsInArchetype()
	{
		if (_aBBItemFils.isEmpty())
			return true ;
		
		for (Iterator<BBFilsItem> itr = _aBBItemFils.iterator() ; itr.hasNext() ; )
		{
			BBFilsItem son = itr.next() ;
			
			// If this node is not a leaf in the archetype
			//
			LdvArchetypeItem archetypeItem = son.getArchetypeItem() ;
			if ((null != archetypeItem) && (false == archetypeItem.getSubItems().isEmpty()))
			{
				son.createSons(-1) ;
					
				if (false == son.getSons().isEmpty())
				{
					String sLabel = son.getLabel() ;
					
					for (Iterator<BBItem> itemItr = son.getSons().iterator() ; itemItr.hasNext() ; )
					{
						BBItem sonItem = itemItr.next() ;
						
						setDNA(sonItem, sLabel) ;
						
						// Asking the BBItem to create itself from son's Citem
						//
						LdvArchetypeItem sonArchetypeItem = son.getArchetypeItem() ;
						if (null != sonArchetypeItem)
							sonItem.createArchetype(sonArchetypeItem.getSubItems(), sonArchetypeItem.getConstraints(), false) ;
					}
				}
			}
		}
		
		return true ;
	}
	
	/**
	 * Provide a son BBItem with all its inherited information
	 * 
	 * @param newSon BBItem to set information to
	 * @param sLabel son's label 
	 * 
	 **/
	public void setDNA(BBItem newSon, final String sLabel)
	{
		if (null == newSon)
			return ;
		
		// Initialization dedicated to Fils guides
	  //
	  String sSonDlg = newSon.getDialogName() ;
	  if ((sSonDlg.length() >= 6) && (sSonDlg.substring(0, 6) == "__IDEM"))
	  	newSon._sDialogName = _sDialogName ;
	  else
	  	newSon._sDialogName = sSonDlg ;
	  
	  // When attached to an archetype, and without a Fil guide, the new son remains in the same panel
	  //
	  if ("".equals(newSon.getDialogName()) && (false == "".equals(_sArchetypeId)))
	  	newSon._sDialogName = _sDialogName ;
	  
	  // Inheritance of the archetype Id (since setDNA is called for each son in createSonsInArchetype())
	  //
	  newSon._sArchetypeId = _sArchetypeId ;
	  
	  // Setting the new localization
	  //
	  String sSemLabel = LdvModelNode.getSemanticLabel(sLabel) ;
	  
	  String sFatherLabel = "" ;
	  if (null != _BBFatherFils)
	  	sFatherLabel = LdvModelNode.getSemanticLabel(_BBFatherFils.getLabel()) ;
	  
	  if (isPlusZero())
	  {
	  	if (_sLocalization.length() > sFatherLabel.length())
	  		newSon.setLocalization(_sLocalization.substring(0, _sLocalization.length() - sFatherLabel.length())) ;
	  	else
	  		newSon.setLocalization("") ;
	  	
	  	if ((false == "".equals(newSon.getLocalization())) && "".equals(sFatherLabel))
	  		newSon.setLocalization(newSon.getLocalization() + LdvGraphConfig.pathSeparationMARK) ;
	  	
	  	newSon.setLocalization(newSon.getLocalization() + sSemLabel) ;
	  }
	  else
	  	newSon.setLocalization(_sLocalization + LdvGraphConfig.pathSeparationMARK + sSemLabel) ;
	}
	
	/**
	 * Function called when a UI wants to close   
	 * 
	 * @param bUnlinkFromControl if true, detach the command layer from widgets
	 * @param bGetInformation    if true, then get information from user interface
	 * 
	 **/
  public boolean canWeClose(boolean bGetInformation, boolean bUnlinkFromControl)
  {
  	boolean bReturn = true ;
  	
  	String sXmlFile = "" ;
  	if (_Data.OpensArchetype())
  		sXmlFile = _Data.getSons() ; 
  		
  	if (true == bGetInformation)
  	{
  		if (null != _CurrentPatPatho)
  			_CurrentPatPatho.clear() ;
  		
  		if (false == _aBBItemFils.isEmpty())
  		{
  			for (Iterator<BBFilsItem> itr = _aBBItemFils.iterator() ; itr.hasNext() ; )
  			{
  				BBFilsItem son = itr.next() ;
  				
  				// Recursive call on sons
  				//
  				if (son.canWeClose(bGetInformation, bUnlinkFromControl))
  				{
  					if (son.isActivated())
  					{
  						// specific case: if label contains �C --> empty the complement
  						//
  						String sSearch1 = String.valueOf(LdvGraphConfig.POUND_CHAR) + "C;" ;
  						String sSearch2 = "�C;" ;
  						
              if (son.getLabel().contains(sSearch1) || son.getLabel().contains(sSearch2))
                son.getTransfertMessage().setComplement("") ;
  						
              // if vector is empty, just add the label
              // 
              if (son.hasEmptyPatPatho())
                addLabel(son, sXmlFile, null, false) ;
              
              else if (null != _CurrentPatPatho)
              {
              	BBVectFatheredPatPathoArray sonPpt = son.getPatPatho() ;
              	
              	if (false == sonPpt.isEmpty())
              	{
              		// If not a +00+00 we must insert a label before each vector
              		//
              		boolean bZeroShift = son.isZeroShift() ;
              		
              		int iVectorInjectCol = LdvModelNodeArray.ORIGINE_PATH_PATHO ;
              		if (false == bZeroShift)
              			iVectorInjectCol++ ;
              		
              		for (Iterator<BBFatheredPatPathoArray> fpptItr = sonPpt.iterator() ; fpptItr.hasNext() ; )
              		{
              			BBFatheredPatPathoArray FPP = fpptItr.next() ;
              		
              			if (false == bZeroShift)
              				addLabel(son, sXmlFile, FPP.getFatherNode(), true) ;
              			
              			_CurrentPatPatho.addVector(FPP.getPatPatho(), iVectorInjectCol) ;
              		}	
              	}
              }
  					}
  				}
  			}
  		}
  	}
  	
  	mergeCurrentPpt(bUnlinkFromControl) ;
  	
  	return bReturn ;
  }

  /**
	 * Blocks from _MergePatPatho replace similar blocks in _CurrentPatPatho    
	 * 
	 * @param bKillMerge if true, kills _MergePatPatho when done 
	 * 
	 **/
  protected void mergeCurrentPpt(boolean bKillMerge)
  {
  	if (null == _MergePatPatho)
  		return ;
  	
  	if (_MergePatPatho.isEmpty())
  	{
  		_MergePatPatho = null ;
  		return ;
  	}
  	
  	if (_CurrentPatPatho.isEmpty())
  	{
  		_CurrentPatPatho.initFromModel(_MergePatPatho) ;
  		_MergePatPatho = null ;
  		return ;
  	}
  	
  	LdvModelNode rootNode = _MergePatPatho.getFirstRootNode() ;
  	
  	// Get all the blocks in _MergePatPatho
  	//
  	LdvModelNodeArrayArray Vect = new LdvModelNodeArrayArray() ;
  	_MergePatPatho.extractVectorOfBrothersPatPatho(rootNode, Vect) ;
  	
  	int iBaseCol = rootNode.getCol() ;
  	
  	// For each block, see if there is a similar block to be replaced in _CurrentPatPatho
   	//
  	Iterator<LdvModelNodeArray> pptItr = Vect.iterator() ;
  	while (pptItr.hasNext())
  	{
  		LdvModelNodeArray currentMergePpt = pptItr.next() ; 
  		
  		// The type of this block is given by its root's Lexicon
      //
  		LdvModelNode currentRootNode = currentMergePpt.getFirstRootNode() ;

      String sSemLex = currentRootNode.getSemanticLexicon() ;

      // Now we delete all blocks with same type in _CurrentPatPatho
      //
      LdvModelNode sameTypeNode = _CurrentPatPatho.findItem(sSemLex) ;
      
      // Keep track of the last deleted line, in order to insert merged ppt in the same place
      //
      int iLastDeletedLine = -1 ;
      
      while (null != sameTypeNode)
      {
      	// If located at root, delete
      	//
      	if (sameTypeNode.getCol() == iBaseCol)
      	{
      		iLastDeletedLine = sameTypeNode.getLine() ;
      		
      		_CurrentPatPatho.deleteNode(sameTypeNode) ;
      		
      		sameTypeNode = _CurrentPatPatho.findItem(sSemLex) ;
        }
      	// Not at root, search again, from this node
      	//
        else
        	sameTypeNode = _CurrentPatPatho.findItem(sSemLex, true, sameTypeNode) ;
      }
      
      // Finaly we insert the merge patpatho
      //
      LdvModelNode nodeToInsertBefore = null ;
      
      if (iLastDeletedLine >= 0)
      	nodeToInsertBefore = _CurrentPatPatho.findNodeForLine(iLastDeletedLine) ; 
      
      if (null == nodeToInsertBefore)
      	_CurrentPatPatho.addVector(currentMergePpt, 0) ;
      else
      	_CurrentPatPatho.insertVector(nodeToInsertBefore, currentMergePpt, 0, true, false) ;
  	}
  }
	
	/**
		* This element, for example a treeview node, has been manually destroyed by the user   
		* 
		**/
	public void manualDestruction(BBMessage message)
	{
		if (false == _aBBItemFils.isEmpty())
			for (Iterator<BBFilsItem> itr = _aBBItemFils.iterator() ; itr.hasNext() ; )
				itr.next().manualDestruction(message) ;
	}
	
	/**
	* Remove useless zeros in a number instantiation string<br>
	* For example, 00023 becomes 23 and 00.23 becomes 0.23
	* 
	**/
	protected String RemoveZeros(final String sDollar)
	{
		if (null == sDollar)
			return "" ;

		if ("".equals(sDollar))
			return "" ;

		if ("0".equals(sDollar))
			return "0" ;

		boolean bIsNegative = false ;

		String sPositiveValue = sDollar ;
		
		// There is a minus sign
		//
		int iPos = sDollar.indexOf('-') ;
		if (-1 != iPos)
		{
			// If it isn't the first char, that's bad
			//
			if (iPos > 0)
				return "" ;

			// If the entry is limited to this minus char, that's bad too
			//
			if (sDollar.length() == 1)
				return "" ;

			// If there are multiple minus char, that's also bad
			//
			iPos = sDollar.indexOf('-', 1) ;
			if (-1 != iPos)
				return "" ;

			bIsNegative = true ;
			sPositiveValue = sDollar.substring(1, sDollar.length()) ;
		}

		char cZero = sPositiveValue.charAt(0) ;
		while (('0' == cZero) && ('.' != sPositiveValue.charAt(1)))
		{
			sPositiveValue = sPositiveValue.substring(1, sPositiveValue.length()) ;
			cZero = sPositiveValue.charAt(0) ;
		}

		// If only '0', return "0"
		//
		if ("".equals(sPositiveValue))
			return "0" ;

		if (bIsNegative)
			sPositiveValue = "-" + sPositiveValue ;

		if ("-0".equals(sPositiveValue))
			return "0" ;

		return sPositiveValue ;
	}

	/**
	* Returns the BBFilsItem at the specified index
	* 
	**/
	protected BBFilsItem getFilsItemAt(int iRank)
	{
		if ((iRank < 0) || (iRank >= _aBBItemFils.size()))
			return null ;
		
		return _aBBItemFils.elementAt(iRank) ;
	}
	
	/**
	* Returns true if sons array is not empty, false if it is
	* 
	**/
	public boolean hasSons() {
		return (false == _aBBItemFils.isEmpty()) ;
	}
	
	/**
	* Returns the currently focused BBFilsItem, ie the one at index _iFocusedFilItem
	* 
	**/
	protected BBFilsItem getFocusedFilsItem()
	{
		return getFilsItemAt(_iFocusedFilItem - 1) ;
	}
}
