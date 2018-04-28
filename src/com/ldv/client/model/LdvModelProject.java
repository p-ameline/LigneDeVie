package com.ldv.client.model;

import java.util.ArrayList;
import java.util.Iterator;

import com.ldv.client.util.LdvGraphManager;
import com.ldv.client.util.LdvLinksManager;
import com.ldv.client.util.LdvTreesManager;
import com.ldv.shared.graph.BBMessage;
import com.ldv.shared.graph.LdvGraphConfig;
import com.ldv.shared.graph.LdvModelNode;
import com.ldv.shared.graph.LdvModelNodeArray;
import com.ldv.shared.graph.LdvModelProjectGraph;
import com.ldv.shared.graph.LdvModelTree;
import com.ldv.shared.model.LdvNum;
import com.ldv.shared.model.LdvTime;

/** 
 * Project information (team, concerns, goals, documents...)
 * 
 */
public class LdvModelProject
{
	private LdvGraphManager             _graphManager ;
	private LdvModelProjectGraph        _SourceProject ;
	
	private String                      _sProjectURI ;          // Root node of the tree
	private String                      _sProjectTypeLexicon ;
	
	private LdvLinksManager             _linksManager ;
	private LdvTreesManager             _treesManager ;
	
	private LdvModelRosace              _Rosace         = null ;
	private LdvModelTeam                _Team           = new LdvModelTeam() ;
	
	private ArrayList<LdvModelConcern>  _concernsArray  = new ArrayList<LdvModelConcern>() ;	// Concerns
	private ArrayList<LdvModelGoal>     _goalsArray     = new ArrayList<LdvModelGoal>() ;			// Goals
	
	private ArrayList<LdvModelDocument> _documentsArray = new ArrayList<LdvModelDocument>() ;	// Documents
	private ArrayList<LdvModelEvent>    _eventsArray    = new ArrayList<LdvModelEvent>() ;		// Events
	
	public LdvModelProject(LdvGraphManager graphManager, LdvModelProjectGraph projectGraph)
	{
		_graphManager        = graphManager ;
		_SourceProject       = projectGraph ;
		
		_sProjectURI         = "" ;
		_sProjectTypeLexicon = "" ;
		
		_treesManager = new LdvTreesManager(projectGraph) ;
		_linksManager = new LdvLinksManager(projectGraph) ;
	}
	
	/**
	*  Get the concern object whose root node has a given node Id
	*  
	*  @param  sConcernId Node Id to look for
	*  @return A LdvModelConcern object if found of <code>null</code> if not
	**/
	public LdvModelConcern getConcernById(String sConcernId)
	{
		if ((null == sConcernId) || sConcernId.equals("") || _concernsArray.isEmpty())
			return (LdvModelConcern) null ;
		
		for (Iterator<LdvModelConcern> itr = _concernsArray.iterator() ; itr.hasNext() ; )
		{
			LdvModelConcern concern = itr.next() ;
			if (concern.getID().equals(sConcernId))
				return concern ;
		}
		
		return (LdvModelConcern) null ;
	}
	
	public void initFromRoot()
	{
		// Getting label tree in order to initialize project's meta data
		//
		LdvModelTree labelTree = _treesManager.getTree(_sProjectURI) ;
		if (null != labelTree)
			initLabel(labelTree) ;
			
		// Setting rosace
		//
		LdvModelRosace rosace = _graphManager.getRosaceForProject(_sProjectURI) ;
		setRosace(rosace) ;
			
		// Loading health team
		//  
		ArrayList<String> aProjectTeam = new ArrayList<String>() ;
		_linksManager.getLinkedNodes(_sProjectURI, LdvLinksManager.nodeLinkType.projectTeam, aProjectTeam) ;
		if (false == aProjectTeam.isEmpty())
		{
			for (Iterator<String> itr = aProjectTeam.iterator() ; itr.hasNext() ; )
			{
				String sDataId = _linksManager.getDataIdFromLabelId(itr.next()) ;
				initTeamElements(sDataId) ;
			}
		}
			
		// Loading folders
		//
			
		// Loading concerns
		//
		ArrayList<String> aProjectIndex = new ArrayList<String>() ;
		_linksManager.getLinkedNodes(_sProjectURI, LdvLinksManager.nodeLinkType.projectIndex, aProjectIndex) ;
		if (false == aProjectIndex.isEmpty())
		{
			for (Iterator<String> itr = aProjectIndex.iterator() ; itr.hasNext() ; )
			{
				String sDataId = _linksManager.getDataIdFromLabelId(itr.next()) ;
				initIndexElements(sDataId) ;
			}
		}
	}
	
	/**
	 *  Parse the label tree in order to initialize project's meta data
	 *  
	 *  @param project    Project to add concerns into
	 *  @param tree       Label tree
	 */
	protected void initLabel(final LdvModelTree tree)
	{
		if (null == tree)
			return ;
		
		LdvModelNode rootNode = tree.getRootNode() ;
		if (null == rootNode)
			return ;
		
		LdvModelNode firstLevelNode = tree.findFirstSon(rootNode) ;
		while (null != firstLevelNode)
		{
			String sSemanticLexicon = firstLevelNode.getSemanticLexicon() ;
			
			// Project type
			//
			if (sSemanticLexicon.equals("0TYPC"))
			{
				LdvModelNode secondLevelNode = tree.findFirstSon(firstLevelNode) ;
				if (null != secondLevelNode)
					setProjectType(secondLevelNode.getLexicon()) ;
			}
			
			firstLevelNode = tree.findFirstBrother(firstLevelNode) ;
		}
	}
	
	/**
	 *  Parse a team tree in order to initialize mandate pairs
	 *  
	 *  @param project        Project to add objects into
	 *  @param sIndexRootNode ID of team tree to process
	 */
	void initTeamElements(final String sIndexRootNode)
	{
		if ((null == sIndexRootNode) || "".equals(sIndexRootNode))
			return ;
		
		// Getting tree
		//
		LdvModelTree teamTree = _treesManager.getTree(sIndexRootNode) ;
		if (null == teamTree)
			return ;
		
		// Looking for first level branches
		// 
		LdvModelNode rootNode = teamTree.getRootNode() ;
		if (null == rootNode)
			return ;
		
		LdvModelNode sonNode = teamTree.findFirstSon(rootNode) ;
		while (null != sonNode)
		{
			String sSemanticConcept = sonNode.getSemanticLexicon() ;
			
			// Team member
			if      ("HMEMB".equals(sSemanticConcept))
				initTeamMember(teamTree, sonNode) ;
			else if ("0ROSA".equals(sSemanticConcept))
			{
				LdvModelRosace rosace = new LdvModelRosace() ;
				rosace.initFromTree(teamTree, sonNode) ;
				if (false == rosace.isEmpty())
					setRosace(rosace) ;
			}
			
			sonNode = teamTree.findFirstBrother(sonNode) ;
		}
	}
	
	/**
	 *  Parse branches of a team member inside a tree in order to add this member into a project's health team
	 *  
	 *  @param project    Project to add this concern into
	 *  @param tree       Index tree
	 *  @param fatherNode Concern root node
	 */
	void initTeamMember(final LdvModelTree tree, final LdvModelNode rootNode)
	{
		if ((null == tree) || (null == rootNode))
			return ;
		
		// No need to work if there is no team object to store the result
		//
		LdvModelTeam team = getTeam() ;
		if (null == team)
			return ;
		
		LdvModelTeamMember teamMember = new LdvModelTeamMember() ;
		
		// Get information about member and mandates
		//
		LdvModelNode sonNode = tree.findFirstSon(rootNode) ;
		while (null != sonNode)
		{
			String sSemanticConcept = sonNode.getSemanticLexicon() ;
			
			// Member's ID
			//
			if (sonNode.startsWithPound() && "SP".equals(sonNode.followsPound()))
				teamMember.setPersonId(sonNode.getComplement()) ;

			// Mandate
			//
			else if (sSemanticConcept.equals("LMAND"))
			{
				LdvModelMandate mandate = new LdvModelMandate() ;
				mandate.setPersonLdvID(teamMember.getPersonId()) ;
				
				initTeamMandate(tree, sonNode, mandate) ;
				if (false == mandate.isEmpty())
					teamMember.addMandate(mandate) ;
			}
				
			sonNode = tree.findFirstBrother(sonNode) ;
		}
		
		if (teamMember.isEmpty())
			return ;
		
		// Build mandate pairs
		//
		Iterator<LdvModelMandate> itr = teamMember.getMandates().iterator() ; 
		while (itr.hasNext()) 
		{
			LdvModelMandatePair mandatePair = new LdvModelMandatePair() ;
			mandatePair.setMemberNoMandates(teamMember) ;
			mandatePair.setMandate(itr.next()) ;
			
			team.addMandatePair(mandatePair) ;
		}
	}
	
	/**
	 *  Parse branches of a team member mandate inside a tree in order to add this mandate to this member's array of mandates 
	 *  
	 *  @param project    Involved project 
	 *  @param tree       Index tree
	 *  @param fatherNode Mandate root node
	 *  @param mandate    Mandate to be filled from tree information
	 */
	void initTeamMandate(final LdvModelTree tree, final LdvModelNode rootNode, final LdvModelMandate mandate)
	{
		if ((null == tree) || (null == rootNode) || (null == mandate))
			return ;
		
		LdvModelNode sonNode = tree.findFirstSon(rootNode) ;
		while (null != sonNode)
		{
			String sSemanticConcept = sonNode.getSemanticLexicon() ;
			
			// Mandate's begin date
			//
			if     (sSemanticConcept.equals("KOUVR"))
			{
				LdvTime beginDate = tree.getDate(sonNode) ;
				if (null != beginDate)
					mandate.setBeginDate(beginDate) ;
			}
			// Mandate's ending date
			//
			else if (sSemanticConcept.equals("KFERM"))
			{
				LdvTime closeDate = tree.getDate(sonNode) ;
				if (null != closeDate)
					mandate.setEndDate(closeDate) ;
			}
			// Position inside the rose
			//
			else if (sSemanticConcept.equals("LPOSI"))
			{
				initTeamMandatePosition(tree, sonNode, mandate.getPosition()) ;
			}	
			
			sonNode = tree.findFirstBrother(sonNode) ;
		}
	}

	/**
	 *  Parse branches of a team member mandate inside a tree in order to add this mandate to this memeber's array of mandates 
	 *  
	 *  @param project    Involved project 
	 *  @param tree       Index tree
	 *  @param fatherNode Mandate root node
	 *  @param mandate    Mandate to be filled from tree information
	 */
	void initTeamMandatePosition(final LdvModelTree tree, final LdvModelNode rootNode, LdvModelMandatePosition position)
	{
		if ((null == tree) || (null == rootNode) || (null == position))
			return ;
		
		LdvModelNode sonNode = tree.findFirstSon(rootNode) ;
		while (null != sonNode)
		{
			String sSemanticConcept = sonNode.getSemanticLexicon() ;
			
			if     (sSemanticConcept.equals("VDIPA"))
			{
				LdvNum distance = new LdvNum() ;
				tree.getNum(sonNode, distance) ;
				if (distance.existValue())
					position.setDistance(distance.getValue()) ;
			}
			else if (sSemanticConcept.equals("VANPA1"))
			{
				LdvNum angle = new LdvNum() ;
				tree.getNum(sonNode, angle) ;
				if (angle.existValue())
					position.setDistance(angle.getValue()) ;
			}	
			
			sonNode = tree.findFirstBrother(sonNode) ;
		}		
	}
	
	/**
	 *  Parse an index tree in order to initialize concerns, goals and actions
	 *  
	 *  @param project        Project to add objects into
	 *  @param sIndexRootNode ID of index tree to process
	 */
	protected void initIndexElements(final String sIndexRootNode)
	{
		if ((null == sIndexRootNode) || "".equals(sIndexRootNode))
			return ;
		
		// Getting tree
		//
		LdvModelTree indexTree = _treesManager.getTree(sIndexRootNode) ;
		if (null == indexTree)
			return ;
		
		// Looking for first level branches
		// 
		LdvModelNode rootNode = indexTree.getRootNode() ;
		if (null == rootNode)
			return ;
		
		LdvModelNode sonNode = indexTree.findFirstSon(rootNode) ;
		while (null != sonNode)
		{
			String sSemanticConcept = sonNode.getSemanticLexicon() ;
			
			if (sSemanticConcept.equals("0PRO1"))
				initConcerns(indexTree, sonNode) ;
			
			sonNode = indexTree.findFirstBrother(sonNode) ;
		}
	}
	
	/**
	 *  Parse the concerns branch of a tree in order to add concerns into a project
	 *  
	 *  @param project    Project to add concerns into
	 *  @param tree       Index tree
	 *  @param fatherNode Concerns father node
	 */
	protected void initConcerns(final LdvModelTree tree, final LdvModelNode fatherNode)
	{
		if ((null == tree) || (null == fatherNode))
			return ;
		
		LdvModelNode sonNode = tree.findFirstSon(fatherNode) ;
		while (null != sonNode)
		{
			initConcern(tree, sonNode) ;
			
			sonNode = tree.findFirstBrother(sonNode) ;
		}
	}
	
	/**
	 *  Parse branches of a concern inside a tree in order to add this concern into a project
	 *  
	 *  @param project    Project to add this concern into
	 *  @param tree       Index tree
	 *  @param fatherNode Concern root node
	 */
	protected void initConcern(final LdvModelTree tree, final LdvModelNode rootNode)
	{
		if ((null == tree) || (null == rootNode))
			return ;
		
		LdvModelConcern newConcern = new LdvModelConcern() ;
		
		newConcern.setID(rootNode.getNodeURI()) ;
		
		_graphManager.initializeNodeTitle(newConcern, rootNode) ;
		
		LdvModelNode sonNode = tree.findFirstSon(rootNode) ;
		while (null != sonNode)
		{
			String sSemanticConcept = sonNode.getSemanticLexicon() ;
			
			if     ("KOUVR".equals(sSemanticConcept))
			{
				LdvTime beginDate = tree.getDate(sonNode) ;
				if (null != beginDate)
					newConcern.setBeginDate(beginDate) ;
			}
			else if ("KFERM".equals(sSemanticConcept))
			{
				LdvTime closeDate = tree.getDate(sonNode) ;
				if (null != closeDate)
					newConcern.setEndDate(closeDate) ;
			}	
			else if ("6CISP".equals(sSemanticConcept)) {
				newConcern.setContinuityCode(sonNode.getComplement()) ;
			}
			else if (LdvGraphConfig.FREE_TEXT_SEM.equals(sSemanticConcept)) {
				newConcern.setTitle(sonNode.getFreeText()) ;
			}
			
			sonNode = tree.findFirstBrother(sonNode) ;
		}
		
		// If end date was not specified, set it to "no limit"
		//
		if (newConcern.getEndDate().isEmpty())
			newConcern.getEndDate().setNoLimit() ;
		
		addConcern(newConcern) ;
	}
	
	/**
	 * Create a new concern line
	 * 
	 * @param newConcern   Description of new concern to be created
	 * 
	 * @return The node ID of the root node for the new concern
	 */
	public String insertNewConcern(LdvModelConcern newConcern)
	{
		if (null == newConcern)
			return "" ;
		
		// Check if the concern is "valid" (at least a Lexicon or a title, and a starting date)
		//
		if ("".equals(newConcern.getLexicon()) && "".equals(newConcern.getTitle()))
			return "" ;
		if (newConcern.getBeginDate().isEmpty())
			return "" ;
		
		// Get the data tree ID for this project's index
		//
		String sProjectDataId = getConcernsDataTreeId() ;
		if ("".equals(sProjectDataId))
			return "" ;
		
		// Get the tree that contains information for this project
		//
		LdvModelTree projectTree = _SourceProject.getTreeFromId(sProjectDataId) ;
		
		if (null == projectTree)
			return "" ;
		
		// The root node contains project's type
		//
		LdvModelNodeArray tree = projectTree.getNodes() ;
		if (null == tree)
			return "" ;
		
		LdvModelNode rootNodeForConcerns = tree.getFirstRootNode() ;
		
		// Find the concerns' library node (by the Lexicon "0PRO11")
		//
		// LdvModelNode rootNodeForConcerns = tree.findItem("0PRO1", true, null) ;
		
		// TODO create a "0PRO11" node if not found
		//
		if (null == rootNodeForConcerns)
			return "" ;
		
		// Create the tree that represents the new concern
		//
		LdvModelNodeArray newConcernTree = new LdvModelNodeArray() ;
		createTreeForConcern(newConcern, newConcernTree) ;
		if (newConcernTree.isEmpty())
			return "" ;
		
		// Insert this tree as a new son of the concerns' library node 
		//
		int iFirstNodeLine = tree.insertVectorAsDaughter(rootNodeForConcerns, newConcernTree, true, false) ;
		
		if (-1 == iFirstNodeLine)
			return "" ;
		
		// Provide new nodes with a in-memory ID
		//
		projectTree.provideNewNodesWithInMemoryId() ;
		
		LdvModelNode newConcernRootNode = tree.findNodeForLine(iFirstNodeLine) ;
		if (null == newConcernRootNode)
			return "" ;
		
		return newConcernRootNode.getTreeID() ;
	}
	
	/**
	 * Create a tree from a LdvModelConcern
	 * 
	 * @param newConcern     Model
	 * @param newConcernTree Resulting tree
	 */
	protected void createTreeForConcern(LdvModelConcern newConcern, LdvModelNodeArray newConcernTree)
	{
		if ((null == newConcern) || (null == newConcernTree))
			return ;
		
		// Root node
		//
		LdvModelNode concernRootNode = null ;
		if (false == "".equals(newConcern.getLexicon()))
			concernRootNode = new LdvModelNode(newConcern.getLexicon()) ; 
		else
			concernRootNode = new LdvModelNode(LdvGraphConfig.FREE_TEXT_LEX, newConcern.getTitle(), true) ;
		concernRootNode.setAsRoot() ;			
		newConcernTree.addNode(concernRootNode) ;
			
		// If there is a type and a title, add the title
		//
		if ((false == "".equals(newConcern.getLexicon())) && (false == "".equals(newConcern.getTitle())))
		{
			BBMessage msg = new BBMessage() ;
			msg.setLexique(LdvGraphConfig.FREE_TEXT_LEX) ;
			msg.setFreeText(newConcern.getTitle()) ;
			newConcernTree.addNode(LdvGraphConfig.FREE_TEXT_LEX, msg, 1) ;
		}
			
		// Begin date
		//
		if ((null != newConcern.getBeginDate()) && (false == newConcern.getBeginDate().isEmpty()))
		{
			newConcernTree.addNode("KOUVR1", 1) ;
				
			BBMessage msg = new BBMessage() ;
			msg.setUnit("2DA021") ;
			msg.setComplement(newConcern.getBeginDate().getLocalDateTime()) ;
			newConcernTree.addNode(String.valueOf(LdvGraphConfig.POUND_CHAR) + "T0;19", msg, 2) ;
		}
			
		// End date
		//
		if ((null != newConcern.getEndDate()) && (false == newConcern.getEndDate().isEmpty()))
		{
			newConcernTree.addNode("KFERM1", 1) ;
					
			BBMessage msg = new BBMessage() ;
			msg.setUnit("2DA021") ;
			msg.setComplement(newConcern.getEndDate().getLocalDateTime()) ;
			newConcernTree.addNode(String.valueOf(LdvGraphConfig.POUND_CHAR) + "T0;19", msg, 2) ;
		}
	}

	/**
	 * Create a new mandate
	 * 
	 * @param newMandate   Description of new mandate to be created
	 * 
	 * @return The node ID of the root node for the new mandate
	 */
	public String insertNewMandate(final LdvModelMandate newMandate)
	{
		if (null == newMandate)
			return "" ;
		
		// Check if the mandate is "valid" (at least a person and a starting date)
		//
		if ("".equals(newMandate.getPersonLdvID()) || newMandate.isEmpty())
			return "" ;
		
		// Get the data tree ID for the team description
		//
		String sTeamDataId = getTeamDataTreeId() ;
		if ("".equals(sTeamDataId))
			return "" ;
		
		// Get the tree that contains information for this project
		//
		LdvModelTree teamTree = _SourceProject.getTreeFromId(sTeamDataId) ;
		
		if (null == teamTree)
			return "" ;
		
		// The root node contains project's type
		//
		LdvModelNodeArray tree = teamTree.getNodes() ;
		if (null == tree)
			return "" ;
		
		// Create the tree that will represent either the new mandate of the mandates folder  
		//
		LdvModelNodeArray newMandateTree = new LdvModelNodeArray() ;
		
		// Look for an existing mandates folder for the person whose mandate is to be saved
		//
		LdvModelNode memberRootNode = findTeamMemberRootNode(tree, newMandate.getPersonLdvID()) ;
		
		int iFirstNodeLine = -1 ;
		
		// If the mandated person already owns a mandates folder for this project, we simply add a new mandate
		//
		if (null != memberRootNode)
		{
			createTreeForMandate(newMandate, newMandateTree) ;
			
			if (newMandateTree.isEmpty())
				return "" ;
			
			// Insert the mandate tree as a new son of the person's mandates folder 
			//
			iFirstNodeLine = tree.insertVectorAsDaughter(memberRootNode, newMandateTree, true, false) ;
		}
		
		// If the mandated person doesn't already own a mandates folder for this project, we have to create one (with the mandate included)
		//
		else
		{
			createTreeForFolderAndMandate(newMandate, newMandateTree) ;
			
			if (newMandateTree.isEmpty())
				return "" ;
			
			// Insert the mandates folder tree as a new son of the root node 
			//
			iFirstNodeLine = tree.insertVectorAsDaughter(tree.getFirstRootNode(), newMandateTree, true, false) ;
		}
		
		if (-1 == iFirstNodeLine)
			return "" ;
		
		// Provide new nodes with a in-memory ID
		//
		teamTree.provideNewNodesWithInMemoryId() ;
		
		LdvModelNode newMandateRootNode = tree.findNodeForLine(iFirstNodeLine) ;
		if (null == newMandateRootNode)
			return "" ;
		
		return newMandateRootNode.getTreeID() ;
	}
	
	/**
	 * Create a mandate tree from a LdvModelMandate
	 * 
	 * @param newMandate     Model
	 * @param newMandateTree Resulting tree
	 */
	protected void createTreeForMandate(final LdvModelMandate newMandate, LdvModelNodeArray newMandateTree)
	{
		if ((null == newMandate) || (null == newMandateTree))
			return ;
		
		// Root node
		//
		LdvModelNode mandateRootNode = new LdvModelNode("LMAND1") ; 
		mandateRootNode.setAsRoot() ;			
		newMandateTree.addNode(mandateRootNode) ;
			
		// If there is location?
		//
		LdvModelMandatePosition location = newMandate.getPosition() ;
		if (false == location.isEmpty())
		{
			newMandateTree.addNode("LPOSI1", 1) ;
			
			if (location.isValidDistance())
			{
				newMandateTree.addNode("VDIPA1", 2) ;
				
				BBMessage msg = new BBMessage() ;
				msg.setUnit("200001") ;
				msg.setComplement(Double.toString(location.getDistance())) ;
				newMandateTree.addNode(String.valueOf(LdvGraphConfig.POUND_CHAR) + "N0;02", msg, 3) ;
			}
			if (location.isValidAngle())
			{
				newMandateTree.addNode("VANPA1", 2) ;
				
				BBMessage msg = new BBMessage() ;
				msg.setUnit("2DEGA2") ;
				msg.setComplement(Double.toString(location.getAngle())) ;
				newMandateTree.addNode(String.valueOf(LdvGraphConfig.POUND_CHAR) + "N0;03", msg, 3) ;
			}
		}
			
		// Begin date
		//
		if ((null != newMandate.getBeginDate()) && (false == newMandate.getBeginDate().isEmpty()))
		{
			newMandateTree.addNode("KOUVR1", 1) ;
				
			BBMessage msg = new BBMessage() ;
			msg.setUnit("2DA021") ;
			msg.setComplement(newMandate.getBeginDate().getLocalDateTime()) ;
			newMandateTree.addNode(String.valueOf(LdvGraphConfig.POUND_CHAR) + "T0;19", msg, 2) ;
		}
			
		// End date
		//
		if ((null != newMandate.getEndDate()) && (false == newMandate.getEndDate().isEmpty()))
		{
			newMandateTree.addNode("KFERM1", 1) ;
					
			BBMessage msg = new BBMessage() ;
			msg.setUnit("2DA021") ;
			msg.setComplement(newMandate.getEndDate().getLocalDateTime()) ;
			newMandateTree.addNode(String.valueOf(LdvGraphConfig.POUND_CHAR) + "T0;19", msg, 2) ;
		}
	}
	
	/**
	 * Build a mandates folder tree including a mandate created from a LdvModelMandate
	 * 
	 * @param newMandate     Mandate model
	 * @param newMandateTree Resulting tree
	 */
	protected void createTreeForFolderAndMandate(final LdvModelMandate newMandate, LdvModelNodeArray newFolderTree)
	{
		if ((null == newMandate) || (null == newFolderTree))
			return ;
		
		// Insert mandates folder's root node
		//
		LdvModelNode folderRootNode = new LdvModelNode("HMEMB1") ; 
		folderRootNode.setAsRoot() ;			
		newFolderTree.addNode(folderRootNode) ;
			
		// Insert the person's LdvId
		//
		BBMessage msg = new BBMessage() ;
		msg.setComplement(newMandate.getPersonLdvID()) ;
		newFolderTree.addNode(String.valueOf(LdvGraphConfig.POUND_CHAR) + "SPID1", msg, 1) ;
		
		// Build the mandate tree
		//
		LdvModelNodeArray newMandateTree = new LdvModelNodeArray() ;
		createTreeForMandate(newMandate, newMandateTree) ;
		newFolderTree.insertVectorAsDaughter(folderRootNode, newMandateTree, false, false) ;
	}
	
	/**
	 * Find, if it exists, the "HMEMB1" node for a given person
	 * 
	 * @param teamTree  Tree to look into
	 * @param sMemberId Identifier of the person to look for
	 * 
	 * @return a LdvModelNode if found, <code>null</code> if not 
	 */
	protected LdvModelNode findTeamMemberRootNode(final LdvModelNodeArray teamTree, final String sMemberLdvId)
	{
		if ((null == teamTree) || teamTree.isEmpty())
			return null ;
		if ((null == sMemberLdvId) || "".equals(sMemberLdvId))
			return null ;
		
		// Exploring the nodes of first column (either "HMEMB1" that contains all mandates for a given person or 
		//                                             "0ROSA1" that describes the rosace)
		//
		LdvModelNode rootNodeForTeam = teamTree.getFirstRootNode() ;
		if (null == rootNodeForTeam)
			return null ;
		
		LdvModelNode colOneNode = teamTree.getFirstSon(rootNodeForTeam) ;
		while (null != colOneNode)
		{
			if ("HMEMB1".equals(colOneNode.getLexicon()))
			{
				// Exploring the nodes of second column (either "£SPID1" that contains the person's LdvId or 
				//                                              "LMAND1" that describes a mandate)
				//
				LdvModelNode colTwoNode = teamTree.getFirstSon(colOneNode) ;
				while (null != colTwoNode)
				{
					if (colTwoNode.startsWithPound() && "SPID1".equals(colTwoNode.followsPound()) && sMemberLdvId.equals(colTwoNode.getComplement()))
						return colOneNode ;
					
					colTwoNode = teamTree.getNextBrother(colTwoNode) ;
				}
			}
			
			colOneNode = teamTree.getNextBrother(colOneNode) ;
		}
		
		return null ;
	}
	
	/**
	 * Get the ID of the label document for project's index
	 * 
	 * @return The tree ID of project's index if found, <code>""</code> if not found
	 */
	public String getIndexLabelTreeId()
	{
		// Get index' label tree using the linksManager 
		//
		ArrayList<String> aResultNodes = new ArrayList<String>() ; 
		_linksManager.getLinkedNodes(_sProjectURI, LdvLinksManager.nodeLinkType.projectIndex, aResultNodes) ;
		
		if (aResultNodes.isEmpty())
			return "" ;
		
		Iterator<String> itr = aResultNodes.iterator() ;
		return itr.next() ;
	}
	
	/**
	 * Get the ID of the label document for project's concerns
	 * 
	 * @return The tree ID of project's concerns if found, <code>""</code> if not
	 */
	public String getConcernsLabelTreeId()
	{
		// Get project's index label tree Id
		//
		String sIndexLabelTreeId = getIndexLabelTreeId() ;
		
		if ("".equals(sIndexLabelTreeId))
			return "" ;
		
		// Get concerns' label tree using the linksManager 
		//
		ArrayList<String> aResultNodes = new ArrayList<String>() ; 
		_linksManager.getLinkedNodes(sIndexLabelTreeId, LdvLinksManager.nodeLinkType.projectConcerns, aResultNodes) ;
		
		if (aResultNodes.isEmpty())
			return "" ;
		
		Iterator<String> itr = aResultNodes.iterator() ;
		return itr.next() ;
	}
	
	/**
	 * Get the ID of the label document for project's team
	 * 
	 * @return The tree ID of project's team if found, <code>""</code> if not
	 */
	public String getTeamLabelTreeId()
	{
		// Get team's label tree using the linksManager 
		//
		ArrayList<String> aResultNodes = new ArrayList<String>() ; 
		_linksManager.getLinkedNodes(_sProjectURI, LdvLinksManager.nodeLinkType.projectTeam, aResultNodes) ;
		
		if (aResultNodes.isEmpty())
			return "" ;
		
		Iterator<String> itr = aResultNodes.iterator() ;
		return itr.next() ;
	}
	
	/**
	 * Get the ID of the data document for project's index
	 * 
	 * @return The tree ID of project's concerns data if found, <code>""</code> if not found
	 */
	public String getConcernsDataTreeId()
	{
		// Get the label tree ID for this project
		//
		String sProjectLabelId = getConcernsLabelTreeId() ;
		if ("".equals(sProjectLabelId))
			return "" ;
		
		// Return the data ID from label ID 
		//
		return _linksManager.getDataIdFromLabelId(sProjectLabelId) ;
	}
	
	/**
	 * Get the ID of the data document for project's team
	 * 
	 * @return The tree ID of project's team if found, <code>""</code> if not found
	 */
	public String getTeamDataTreeId()
	{
		// Get the label tree ID for this project
		//
		String sTeamLabelId = getTeamLabelTreeId() ;
		if ("".equals(sTeamLabelId))
			return "" ;
		
		// Return the data ID from label ID 
		//
		return _linksManager.getDataIdFromLabelId(sTeamLabelId) ;
	}

	public void addConcern(LdvModelConcern concern) {
		_concernsArray.add(concern) ;
	}
	
	public void addGoal(LdvModelGoal goal) {
		_goalsArray.add(goal) ;
	}
	
	public void addDocument(LdvModelDocument doc) {
		_documentsArray.add(doc) ;
	}
	
	public void addEvent(LdvModelEvent event) {
		_eventsArray.add(event) ;
	}
	
	public void addMandatePair(LdvModelMandatePair pair) {
		_Team.addMandatePair(pair) ;
	}
	
	public void setProjectType(String sProjectType) { 
		_sProjectTypeLexicon = sProjectType ; 
	}
	public String getProjectType() { 
		return _sProjectTypeLexicon ; 
	}
	
	public String getProjectUri() {
		return _sProjectURI ;
	}
	public void setProjectUri(String sProjectURI) {
		_sProjectURI = sProjectURI ;
	}
	
	public ArrayList<LdvModelConcern> getConcerns() { 
		return _concernsArray ; 
	}
	public int getConcernsCount() {
		return _concernsArray.size() ;
	}
	
	public ArrayList<LdvModelGoal> getGoals() { 
		return _goalsArray ; 
	}
	
	public ArrayList<LdvModelDocument> getDocuments() { 
		return _documentsArray ; 
	}
	
	public LdvModelRosace getRosace() {
		return _Rosace ;
	}
	public void setRosace(LdvModelRosace rosace) {
		_Rosace = rosace ;
	}
	
	public LdvModelTeam getTeam() { 
		return _Team ; 
	}
	
	/**
	 * Get a tree from its ID 
	 *  
	 * @param  sTreeId ID of tree to be found
	 * 
	 * @return The tree if found, or null if not
	 */
	public LdvModelTree getTree(String sDocumentId) {
		return _treesManager.getTree(sDocumentId) ;
	}
}
