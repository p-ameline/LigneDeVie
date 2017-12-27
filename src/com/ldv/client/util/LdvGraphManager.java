package com.ldv.client.util;

import java.util.ArrayList;
import java.util.Iterator;

import com.ldv.client.model.LdvModelConcern;
import com.ldv.client.model.LdvModelDemographics;
import com.ldv.client.model.LdvModelMandate;
import com.ldv.client.model.LdvModelMandatePair;
import com.ldv.client.model.LdvModelMandatePosition;
import com.ldv.client.model.LdvModelProject;
import com.ldv.client.model.LdvModelRosace;
import com.ldv.client.model.LdvModelTeam;
import com.ldv.client.model.LdvModelTeamMember;
import com.ldv.client.util.LdvLinksManager.traitDirection;
import com.ldv.shared.database.Lexicon;
import com.ldv.shared.graph.LdvModelGraph;
import com.ldv.shared.graph.LdvModelNode;
import com.ldv.shared.graph.LdvModelTree;
import com.ldv.shared.model.LdvNum;
import com.ldv.shared.model.LdvTime;

public class LdvGraphManager 
{
	private LdvModelGraph              _modelGraph ;
	private ArrayList<LdvModelProject> _projectsModels ;
	private ArrayList<LdvModelRosace>  _rosacesLibrary ;
	
	private LdvModelDemographics       _demographics ;
	
	private LdvLinksManager            _linksManager ;
	private LdvTreesManager            _treesManager ;
	
	private final LdvSupervisor        _supervisor ;
	
	/**
	*  Default constructor
	**/
	public LdvGraphManager(final LdvSupervisor supervisor)
	{
		_linksManager   = null ;
		_treesManager   = null ;
		_modelGraph     = null ;
		_projectsModels = new ArrayList<LdvModelProject>() ;
		_rosacesLibrary = new ArrayList<LdvModelRosace>() ; 
		_demographics   = null ;
		
		_supervisor     = supervisor ;
	}
	
	/**
	*  Get all nodes that are linked to the reference node with a given type in a given direction 
	*  
	* @param    sReferenceNode   The node result nodes must be linked to
	* @param    iRelation        Relation of links to be considered
	* @param    aResultNodes     Array of result nodes
	* @param    iSearchDirection Direction of links to be considered
	*
	**/
	public void initFromModel(final LdvModelGraph modelGraph)
	{
		_modelGraph = modelGraph ;
		_projectsModels.clear() ;
		
		if (null != _demographics)
			_demographics.reset() ;
		
		_linksManager = new LdvLinksManager(this.getGraph()) ;
		_treesManager = new LdvTreesManager(this.getGraph()) ;
		
		if (null != _modelGraph)
		{
			// Take care to init the rosaces library before projects
			//
			initRosacesLibrary() ;
			
			initProjects() ;
			initDemographics() ;
		}
	}
	
	/**
	*  Parse trees inside _modelGraph in order to initialize _rosacesLibrary
	**/
	void initRosacesLibrary()
	{
		if (false == isFunctionnal())
			return ;
		
		String sRootId = _modelGraph.getRootID() ;
		if ((null == sRootId) || "".equals(sRootId))
			return ;
		
		// Get the Id of all project description trees
		//
		ArrayList<String> aRosacesRootNodes = new ArrayList<String>() ;
		_linksManager.getLinkedNodes(sRootId, LdvLinksManager.nodeLinkType.rosacesLibrary , aRosacesRootNodes) ;
		
		if (aRosacesRootNodes.isEmpty())
			return ;
		
		for (Iterator<String> itr = aRosacesRootNodes.iterator() ; itr.hasNext() ; )
			initRosacesFromRoot(itr.next()) ;
	}
	
	/**
	*  Parse a tree in order to initialize the rosaces it contains
	*  
	*  @param sRosacesRootNode Root node of rosaces library to initialize
	*  
	**/
	protected void initRosacesFromRoot(final String sRosacesRootNode)
	{
		if ((null == sRosacesRootNode) || "".equals(sRosacesRootNode) || (false == isFunctionnal()))
			return ;
		
		// Getting data tree 
		//
		String sDataId = _linksManager.getDataIdFromLabelId(sRosacesRootNode) ;
			
		if ((null == sDataId) || "".equals(sDataId))
			return ;
			
		LdvModelTree rosacesLibraryTree = _treesManager.getTree(sDataId) ;
		if ((null == rosacesLibraryTree) || rosacesLibraryTree.isEmpty())
			return ;
			
		// Looking for first level branches
		// 
		LdvModelNode rootNode = rosacesLibraryTree.getRootNode() ;
		if (null == rootNode)
			return ;
			
		LdvModelNode sonNode = rosacesLibraryTree.findFirstSon(rootNode) ;
		while (null != sonNode)
		{
			String sSemanticConcept = sonNode.getSemanticLexicon() ;
				
			if (sSemanticConcept.equals("0ROSA"))
			{
				LdvModelRosace newRosace = new LdvModelRosace() ;
				newRosace.initFromTree(rosacesLibraryTree, sonNode) ;
				
				if (false == newRosace.isEmpty())
					_rosacesLibrary.add(newRosace) ;
			}
				
			sonNode = rosacesLibraryTree.findFirstBrother(sonNode) ;
		}
	}
	
	/**
	*  Find, in the rosaces library, the rosace with a given root node ID
	*  
	*  @param  sRootNodeId Root node ID of rosace to search for
	*  @return the rosace if found or null
	*    
	**/
	protected LdvModelRosace getRosaceByRootNodeID(String sRootNodeId)
	{
		if (_rosacesLibrary.isEmpty())
			return null ;
		
		for (Iterator<LdvModelRosace> itr = _rosacesLibrary.iterator() ; itr.hasNext() ; )
		{
			LdvModelRosace rosace = itr.next() ;
			if (rosace.getID().equals(sRootNodeId))
				return rosace ;
		}
		
		return null ;
	}
	
	/**
	*  Find, the rosace linked to a given project
	*  
	*  @param  sProjectRootNodeId Root node ID of the project
	*  @return the rosace if found or null
	*    
	**/
	protected LdvModelRosace getRosaceForProject(String sProjectRootNodeId)
	{
		// Get the Id of all rosaces linked to this project (only the first valid one is used)
		//
		ArrayList<String> aRosacesRootNodes = new ArrayList<String>() ;
		_linksManager.getLinkedNodes(sProjectRootNodeId, LdvLinksManager.nodeLinkType.rosaceModel , aRosacesRootNodes, traitDirection.reverse) ;
			
		if (aRosacesRootNodes.isEmpty())
			return null ;
			
		for (Iterator<String> itr = aRosacesRootNodes.iterator() ; itr.hasNext() ; )
		{
			String sRosaceRootNodeId = itr.next() ;
			LdvModelRosace rosace = getRosaceByRootNodeID(sRosaceRootNodeId) ;
			if (null != rosace)
				return rosace ;
		}
		
		return null ;
	}
		
	/**
	*  Parse trees inside _modelGraph in order to initialize _projectsModels
	*    
	**/
	void initProjects()
	{
		if (false == isFunctionnal())
			return ;
		
		String sRootId = _modelGraph.getRootID() ;
		if ((null == sRootId) || "".equals(sRootId))
			return ;
		
		// Get the Id of all project description trees
		//
		ArrayList<String> aProjectsRootNodes = new ArrayList<String>() ;
		_linksManager.getLinkedNodes(sRootId, LdvLinksManager.nodeLinkType.project , aProjectsRootNodes) ;
		
		if (aProjectsRootNodes.isEmpty())
			return ;
		
		for (Iterator<String> itr = aProjectsRootNodes.iterator() ; itr.hasNext() ; )
			initProjectFromRoot(itr.next()) ;
	}
	
	/**
	*  Parse trees inside _modelGraph in order to initialize a LdvModelProject
	*  
	*  @param sProjectRootNode Root node of project to initialize
	*    
	**/
	void initProjectFromRoot(final String sProjectRootNode)
	{
		if ((null == sProjectRootNode) || "".equals(sProjectRootNode) || (false == isFunctionnal()))
			return ;
		
		LdvModelProject project = new LdvModelProject() ;
		project.setProjectUri(sProjectRootNode) ;
		
		// Getting label tree in order to initialize project's meta data
		//
		LdvModelTree labelTree = _treesManager.getTree(sProjectRootNode) ;
		if (null != labelTree)
			initProjectLabel(project, labelTree) ;
		
		// Setting project's rosace
		//
		LdvModelRosace rosace = getRosaceForProject(sProjectRootNode) ;
		project.setRosace(rosace) ;
		
		// Loading health team
		//  
		ArrayList<String> aProjectTeam = new ArrayList<String>() ;
		_linksManager.getLinkedNodes(sProjectRootNode, LdvLinksManager.nodeLinkType.personHealthTeam, aProjectTeam) ;
		if (false == aProjectTeam.isEmpty())
		{
			for (Iterator<String> itr = aProjectTeam.iterator() ; itr.hasNext() ; )
			{
				String sDataId = _linksManager.getDataIdFromLabelId(itr.next()) ;
				initTeamElements(project, sDataId) ;
			}
		}
		
		// Loading folders
		//
		
		// Loading concerns
		//
		ArrayList<String> aProjectIndex = new ArrayList<String>() ;
		_linksManager.getLinkedNodes(sProjectRootNode, LdvLinksManager.nodeLinkType.personHealthIndex, aProjectIndex) ;
		if (false == aProjectIndex.isEmpty())
		{
			for (Iterator<String> itr = aProjectIndex.iterator() ; itr.hasNext() ; )
			{
				String sDataId = _linksManager.getDataIdFromLabelId(itr.next()) ;
				initIndexElements(project, sDataId) ;
			}
		}
		
		_projectsModels.add(project) ;
	}
	
	/**
	*  Parse the label tree in order to initialize project's meta data
	*  
	*  @param project    Project to add concerns into
	*  @param tree       Label tree
	*  
	**/
	void initProjectLabel(LdvModelProject project, final LdvModelTree tree)
	{
		if ((null == project) || (null == tree) || (false == isFunctionnal()))
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
					project.setProjectType(secondLevelNode.getLexicon()) ;
			}
			
			firstLevelNode = tree.findFirstBrother(firstLevelNode) ;
		}
	}
	
	/**
	*  Parse an index tree in order to initialize concerns, goals and actions
	*  
	*  @param project        Project to add objects into
	*  @param sIndexRootNode ID of index tree to process
	*  
	**/
	void initIndexElements(LdvModelProject project, final String sIndexRootNode)
	{
		if ((null == sIndexRootNode) || "".equals(sIndexRootNode) || (false == isFunctionnal()))
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
				initConcerns(project, indexTree, sonNode) ;
			
			sonNode = indexTree.findFirstBrother(sonNode) ;
		}
	}
	
	/**
	*  Parse the concerns branch of a tree in order to add concerns into a project
	*  
	*  @param project    Project to add concerns into
	*  @param tree       Index tree
	*  @param fatherNode Concerns father node
	*  
	**/
	void initConcerns(LdvModelProject project, final LdvModelTree tree, final LdvModelNode fatherNode)
	{
		if ((null == project) || (null == tree) || (null == fatherNode) || (false == isFunctionnal()))
			return ;
		
		LdvModelNode sonNode = tree.findFirstSon(fatherNode) ;
		while (null != sonNode)
		{
			initConcern(project, tree, sonNode) ;
			
			sonNode = tree.findFirstBrother(sonNode) ;
		}
	}
	
	/**
	*  Parse branches of a concern inside a tree in order to add this concern into a project
	*  
	*  @param project    Project to add this concern into
	*  @param tree       Index tree
	*  @param fatherNode Concern root node
	*  
	**/
	void initConcern(LdvModelProject project, final LdvModelTree tree, final LdvModelNode rootNode)
	{
		if ((null == project) || (null == tree) || (null == rootNode) || (false == isFunctionnal()))
			return ;
		
		LdvModelConcern newConcern = new LdvModelConcern() ;
		
		newConcern.setID(rootNode.getNodeURI()) ;
		
		LdvModelNode sonNode = tree.findFirstSon(rootNode) ;
		while (null != sonNode)
		{
			String sSemanticConcept = sonNode.getSemanticLexicon() ;
			
			if     (sSemanticConcept.equals("KOUVR"))
			{
				LdvTime beginDate = tree.getDate(sonNode) ;
				if (null != beginDate)
					newConcern.setBeginDate(beginDate) ;
			}
			else if (sSemanticConcept.equals("KFERM"))
			{
				LdvTime closeDate = tree.getDate(sonNode) ;
				if (null != closeDate)
					newConcern.setEndDate(closeDate) ;
			}	
			else if (sSemanticConcept.equals("6CISP"))
			{
				newConcern.setContinuityCode(sonNode.getComplement()) ;
			}	
			
			sonNode = tree.findFirstBrother(sonNode) ;
		}
		
		initializeNodeTitle(newConcern, rootNode) ;
		
		// If end date was not specified, set it to "no limit"
		//
		if (newConcern.getEndDate().isEmpty())
			newConcern.getEndDate().setNoLimit() ;
		
		project.addConcern(newConcern) ;
	}
	
	public void initializeNodeTitle(LdvModelConcern concern, final LdvModelNode node) 
	{
		if (null == node)
			return ;
		
		String sLexiconCode = node.getLexicon() ;
		
		// Case for free text 
		//
		if ("�?????" == sLexiconCode)
		{
			concern.setTitle(node.getFreeText()) ;
			return ;
		}
		
		// Get lexicon label
		//
		Lexicon lexicon = _supervisor.getLexiconFromCode(sLexiconCode) ;
		if (null != lexicon)
		{
			String sLexiconLabel = lexicon.getDisplayLabel(Lexicon.Declination.singular, _supervisor.getUserLanguage()) ;
			concern.setTitle(sLexiconLabel) ;
			return ;
		}
	}
	
	/**
	*  Get the concern object whose root node has a given node Id
	*  
	*  @param  sConcernId Node Id to look for
	*  @return A LdvModelConcern object if found of <code>null</code> if not
	**/
	public LdvModelConcern getConcernById(final String sConcernId)
	{
		if ((null == sConcernId) || "".equals(sConcernId) || _projectsModels.isEmpty())
			return (LdvModelConcern) null ;
		
		for (Iterator<LdvModelProject> itr = _projectsModels.iterator() ; itr.hasNext() ; )
		{
			LdvModelProject project = itr.next() ;
			LdvModelConcern concern = project.getConcernById(sConcernId) ;
			if (null != concern)
				return concern ;
		}
		
		return (LdvModelConcern) null ;
	}
	
	/**
	*  Parse a team tree in order to initialize mandate pairs
	*  
	*  @param project        Project to add objects into
	*  @param sIndexRootNode ID of team tree to process
	**/
	void initTeamElements(LdvModelProject project, final String sIndexRootNode)
	{
		if ((null == sIndexRootNode) || "".equals(sIndexRootNode) || (false == isFunctionnal()))
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
			
			if (sSemanticConcept.equals("HMEMB"))
				initTeamMember(project, teamTree, sonNode) ;
			
			sonNode = teamTree.findFirstBrother(sonNode) ;
		}
	}
	
	/**
	*  Parse branches of a concern inside a tree in order to add this concern into a project
	*  
	*  @param project    Project to add this concern into
	*  @param tree       Index tree
	*  @param fatherNode Concern root node
	**/
	void initTeamMember(LdvModelProject project, final LdvModelTree tree, final LdvModelNode rootNode)
	{
		if ((null == project) || (null == tree) || (null == rootNode) || (false == isFunctionnal()))
			return ;
		
		// No need to work if there is no team object to store the result
		//
		LdvModelTeam team = project.getTeam() ;
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
				initTeamMandate(project, tree, sonNode, mandate) ;
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
	*  Parse branches of a team member mandate inside a tree in order to add this mandate to this memeber's array of mandates 
	*  
	*  @param project    Involved project 
	*  @param tree       Index tree
	*  @param fatherNode Mandate root node
	*  @param mandate    Mandate to be filled from tree information
	**/
	void initTeamMandate(LdvModelProject project, final LdvModelTree tree, final LdvModelNode rootNode, final LdvModelMandate mandate)
	{
		if ((null == project) || (null == tree) || (null == rootNode) || (false == isFunctionnal()) || (null == mandate))
			return ;
		
		LdvModelNode sonNode = tree.findFirstSon(rootNode) ;
		while (null != sonNode)
		{
			String sSemanticConcept = sonNode.getSemanticLexicon() ;
			
			if     (sSemanticConcept.equals("KOUVR"))
			{
				LdvTime beginDate = tree.getDate(sonNode) ;
				if (null != beginDate)
					mandate.setMandateBeginDate(beginDate.getLocalDateTime()) ;
			}
			else if (sSemanticConcept.equals("KFERM"))
			{
				LdvTime closeDate = tree.getDate(sonNode) ;
				if (null != closeDate)
					mandate.setMandateEndDate(closeDate.getLocalDateTime()) ;
			}	
			else if (sSemanticConcept.equals("LPOSI"))
			{
				initTeamMandatePosition(project, tree, sonNode, mandate.getPosition()) ;
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
	**/
	void initTeamMandatePosition(LdvModelProject project, final LdvModelTree tree, final LdvModelNode rootNode, LdvModelMandatePosition position)
	{
		if ((null == project) || (null == tree) || (null == rootNode) || (false == isFunctionnal()) || (null == position))
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
	*  Parse trees inside _modelGraph in order to initialize demographics
	*    
	**/
	private void initDemographics()
	{
		if (null != _demographics)
			_demographics.reset() ;
		
		if (false == isFunctionnal())
			return ;
		
		String sRootId = _modelGraph.getRootID() ;
		if ((null == sRootId) || "".equals(sRootId))
			return ;
		
		ArrayList<String> aDemographicsNodes = new ArrayList<String>() ;
		_linksManager.getLinkedNodes(sRootId, LdvLinksManager.nodeLinkType.personAdminData , aDemographicsNodes) ;
		
		if (aDemographicsNodes.isEmpty())
			return ;
		
		for (Iterator<String> itr = aDemographicsNodes.iterator() ; itr.hasNext() ; )
			initDemographicsFromRoot(itr.next()) ;
	}
	
	/**
	*  Parse trees inside _modelGraph in order to initialize a LdvModelProject
	*  
	*  @param sProjectRootNode Root node of project to initialize
	*    
	**/
	private void initDemographicsFromRoot(final String sDemographicInformationRootNode)
	{
		if ((null == sDemographicInformationRootNode) || "".equals(sDemographicInformationRootNode) || (false == isFunctionnal()))
			return ;
		
		// Getting data tree 
		//
		String sDataId = _linksManager.getDataIdFromLabelId(sDemographicInformationRootNode) ;
		
		if ((null == sDataId) || "".equals(sDataId))
			return ;
		
		LdvModelTree dataTree = _treesManager.getTree(sDataId) ;
		if ((null == dataTree) || dataTree.isEmpty())
			return ;
		
		// Looking for first level branches
		// 
		LdvModelNode rootNode = dataTree.getRootNode() ;
		if (null == rootNode)
			return ;
		
		if (false == "ZADMI".equals(rootNode.getSemanticLexicon()))
			return ;
		
		if (null == _demographics)
			_demographics = new LdvModelDemographics() ;
		
		LdvModelNode sonNode = dataTree.findFirstSon(rootNode) ;
		while (null != sonNode)
		{
			String sSemanticConcept = sonNode.getSemanticLexicon() ;
			
			if     ("LIDET".equals(sSemanticConcept))
			{
				LdvModelNode littleSonNode = dataTree.findFirstSon(sonNode) ;
				
				while (null != littleSonNode)
				{
					sSemanticConcept = littleSonNode.getSemanticLexicon() ;
					
					if     ("LNOM0".equals(sSemanticConcept))
					{
						String sName = dataTree.getFreeText(littleSonNode) ;
						if ((null != sName) && (false == "".equals(sName)))
							_demographics.setName(sName) ;
					}
					else if ("LNOM2".equals(sSemanticConcept))
					{
						String sFirstName = dataTree.getFreeText(littleSonNode) ;
						if ((null != sFirstName) && (false == "".equals(sFirstName)))
							_demographics.setFirstName(sFirstName) ;
					}
					else if ("LNOM3".equals(sSemanticConcept))
					{
						String sMiddleName = dataTree.getFreeText(littleSonNode) ;
						if ((null != sMiddleName) && (false == "".equals(sMiddleName)))
							_demographics.setMiddleName(sMiddleName) ;
					}
					else if ("LNOM1".equals(sSemanticConcept))
					{
						String sMaidenName = dataTree.getFreeText(littleSonNode) ;
						if ((null != sMaidenName) && (false == "".equals(sMaidenName)))
							_demographics.setMaidenName(sMaidenName) ;
					}
					else if ("KNAIS".equals(sSemanticConcept))
					{
						LdvTime birthDate = dataTree.getDate(littleSonNode) ;
						if (null != birthDate)
							_demographics.setBirthDate(birthDate) ;
					}
					
					littleSonNode = dataTree.findFirstBrother(littleSonNode) ;
				}
			}

			sonNode = dataTree.findFirstBrother(sonNode) ;
		}
	}
	
	private boolean isFunctionnal() {
		if ((null == _modelGraph) || (null == _linksManager))
			return false ;
		
		return true ;
	}
	
	/**
	* Get a LdvModelNode from its node ID
	* 
	* @param sNodeId ID of the node we are looking for
	* @return the node if found, <code>null</code> if not
	* 
	**/
	public LdvModelNode getNodeFromId(final String sNodeId)
	{
		if ((null == sNodeId) || "".equals(sNodeId))
			return null ;
		
		if (null == _modelGraph)
			return null ;
		
		return _modelGraph.getNodeFromId(sNodeId) ;
	}
	
	public ArrayList<LdvModelProject> getProjects() {
		return _projectsModels ;
	}
	
	public LdvModelGraph getGraph() {
		return _modelGraph ;
	}
	
	public LdvModelDemographics getDemographics() {
		return _demographics ;
	}
}
