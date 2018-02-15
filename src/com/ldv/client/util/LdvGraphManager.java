package com.ldv.client.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;

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
import com.ldv.shared.graph.BBMessage;
import com.ldv.shared.graph.LdvGraphConfig;
import com.ldv.shared.graph.LdvGraphMapping;
import com.ldv.shared.graph.LdvGraphTools;
import com.ldv.shared.graph.LdvModelGraph;
import com.ldv.shared.graph.LdvModelLink;
import com.ldv.shared.graph.LdvModelModel;
import com.ldv.shared.graph.LdvModelModelArray;
import com.ldv.shared.graph.LdvModelNode;
import com.ldv.shared.graph.LdvModelNodeArray;
import com.ldv.shared.graph.LdvModelRight;
import com.ldv.shared.graph.LdvModelRightArray;
import com.ldv.shared.graph.LdvModelTree;
import com.ldv.shared.model.LdvNum;
import com.ldv.shared.model.LdvTime;

/**
 * A graph, originated from the server and parsed as high level objects (projects, rosaces, demographic information...)
 *
 */
public class LdvGraphManager 
{
	private int                        _iServerType ;
	
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
		_iServerType    = -1 ;
		_linksManager   = null ;
		_treesManager   = null ;
		_modelGraph     = null ;
		_projectsModels = new ArrayList<LdvModelProject>() ;
		_rosacesLibrary = new ArrayList<LdvModelRosace>() ; 
		_demographics   = null ;
		
		_supervisor     = supervisor ;
	}
	
	/**
	 *  Initialize from a graph 
	 *  
	 * @param modelGraph Graph to initialize from
	 */
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
	 *  Update current graph from a modified block 
	 *  
	 * @param modelSubGraph Sub-graph to update inside current graph
	 * @param aMappings     Mapping objects
	 */
	public void injectModified(final LdvModelGraph modelSubGraph, final Vector<LdvGraphMapping> aMappings)
	{
		applyMappings(modelSubGraph, aMappings) ;
	}
	
	/**
	 *  Apply mappings to the graph
	 *  
	 * @param modelSubGraph Sub-graph to apply mappings to
	 * @param aMappings     Mapping objects
	 */
	public void applyMappings(final LdvModelGraph modelSubGraph, final Vector<LdvGraphMapping> aMappings)
	{
		if ((null == modelSubGraph) || (null == aMappings) || aMappings.isEmpty())
			return ;
		
		// Browse trees, links, models and rights to apply mappings
		//
		// At this moment, the current graph (_modelGraph) has already been changed (deletions, insertions, modifications).
		// We only have to provide inserted object with the identifiers that were attributed by the server  
		//
		applyMappingsForTrees(modelSubGraph, aMappings) ;
		applyMappingsForLinks(modelSubGraph, aMappings) ;
		applyMappingsForModels(modelSubGraph, aMappings) ;
		applyMappingsForRights(modelSubGraph, aMappings) ;
	}
	
	/**
	 *  Apply mappings to graph's trees
	 *  
	 * @param modelSubGraph Sub-graph to know to what trees the mapping must be applied to
	 * @param aMappings     Mapping objects
	 */
	protected void applyMappingsForTrees(final LdvModelGraph modelSubGraph, final Vector<LdvGraphMapping> aMappings)
	{
		if ((null == modelSubGraph) || (null == aMappings) || aMappings.isEmpty())
			return ;
		
		Vector<LdvModelTree> aTrees = modelSubGraph.getTrees() ;
		
		if ((null == aTrees) || aTrees.isEmpty())
			return ;
		
		for (Iterator<LdvModelTree> itr = aTrees.iterator() ; itr.hasNext() ; )
		{
			LdvModelTree tree = itr.next() ;
		
			// Find this tree in current graph
			//
			String sTreeId = tree.getTreeID() ;	
			LdvModelTree graphTree = _modelGraph.getTreeFromId(sTreeId) ;
			
			if (null != graphTree)
			{
				// Is it a new tree?
				//
				LdvGraphMapping mappingForTree = getMappingForId(aMappings, sTreeId, "") ;	
				if (null != mappingForTree)
					graphTree.setTreeID(mappingForTree.getStoredObject_ID()) ;
			
				if (false == graphTree.isEmpty())
				{
					// Apply mappings to nodes
					//
					for (Iterator<LdvModelNode> nodeItr = graphTree.getNodes().iterator() ; nodeItr.hasNext() ; )
					{
						LdvModelNode node = nodeItr.next() ;
					
						LdvGraphMapping mappingForNode = getMappingForId(aMappings, node.getDocumentId(), node.getNodeID()) ;	
						if (null != mappingForNode)
						{
							node.setTreeID(mappingForNode.getStoredObject_ID()) ;
							node.setNodeID(mappingForNode.getStoredNode_ID()) ;
						}
					}
				}
			}
		}
	}
	
	/**
	 *  Apply mappings to graph's links
	 *  
	 * @param modelSubGraph Sub-graph to know to what links the mapping must be applied to
	 * @param aMappings     Mapping objects
	 */
	protected void applyMappingsForLinks(final LdvModelGraph modelSubGraph, final Vector<LdvGraphMapping> aMappings)
	{
		if ((null == modelSubGraph) || (null == aMappings) || aMappings.isEmpty())
			return ;
		
		Vector<LdvModelLink> aLinks = modelSubGraph.getLinks() ;
		
		if ((null == aLinks) || aLinks.isEmpty())
			return ;
		
		for (Iterator<LdvModelLink> itr = aLinks.iterator() ; itr.hasNext() ; )
		{
			LdvModelLink link = itr.next() ;
			
			// Get the corresponding link inside current graph
			//
			LdvModelLink graphLink = _modelGraph.getLinkFromModel(link) ;
			
			if (null != graphLink)
			{
				// Update the qualified "pointer"
				//
				LdvGraphMapping mappingForQualified = getMappingForComposite(aMappings, graphLink.getQualified()) ;
				if (null != mappingForQualified)
					graphLink.setQualified(mappingForQualified.getStoredObject_ID() + mappingForQualified.getStoredNode_ID()) ;
				
				// Update the qualifier "pointer"
				//
				LdvGraphMapping mappingForQualifier = getMappingForComposite(aMappings, graphLink.getQualifier()) ;
				if (null != mappingForQualifier)
					graphLink.setQualifier(mappingForQualifier.getStoredObject_ID() + mappingForQualifier.getStoredNode_ID()) ;
			}
		}
	}
	
	/**
	 *  Apply mappings to graph's models
	 *  
	 * @param modelSubGraph Sub-graph to know to what links the mapping must be applied to
	 * @param aMappings     Mapping objects
	 */
	protected void applyMappingsForModels(final LdvModelGraph modelSubGraph, final Vector<LdvGraphMapping> aMappings)
	{
		if ((null == modelSubGraph) || (null == aMappings) || aMappings.isEmpty())
			return ;
		
		LdvModelModelArray aModels = modelSubGraph.getModels() ;
		
		if ((null == aModels) || aModels.isEmpty())
			return ;
		
		for (Iterator<LdvModelModel> itr = aModels.iterator() ; itr.hasNext() ; )
		{
			LdvModelModel model = itr.next() ;
			
			// Get the corresponding model inside current graph
			//
			LdvModelModel graphModel = _modelGraph.getModelForNode(model.getObject(), model.getNode()) ;
			
			if (null != graphModel)
			{
				// Find the proper mapping and apply it
				//
				LdvGraphMapping mappingForModel = getMappingForId(aMappings, graphModel.getObject(), graphModel.getNode()) ;	
				if (null != mappingForModel)
				{
					graphModel.setObject(mappingForModel.getStoredObject_ID()) ;
					graphModel.setNode(mappingForModel.getStoredNode_ID()) ;
				}
			}
		}
	}
	
	/**
	 *  Apply mappings to graph's rights
	 *  
	 * @param modelSubGraph Sub-graph to know to what rights the mapping must be applied to
	 * @param aMappings     Mapping objects
	 */
	protected void applyMappingsForRights(final LdvModelGraph modelSubGraph, final Vector<LdvGraphMapping> aMappings)
	{
		if ((null == modelSubGraph) || (null == aMappings) || aMappings.isEmpty())
			return ;
		
		LdvModelRightArray aRights = modelSubGraph.getRights() ;
		
		if ((null == aRights) || aRights.isEmpty())
			return ;
		
		LdvModelRightArray aGraphRights = _modelGraph.getRights() ;
		
		for (Iterator<LdvModelRight> itr = aRights.iterator() ; itr.hasNext() ; )
		{
			LdvModelRight right = itr.next() ;
			
			// Get the corresponding right inside current graph
			//
			LdvModelRight graphRight = aGraphRights.getRightForModel(right) ;
			
			if (null != graphRight)
			{
				// Find the proper mapping and apply it
				//
				LdvGraphMapping mappingForRight = getMappingForComposite(aMappings, graphRight.getNode()) ;	
				if (null != mappingForRight)
					graphRight.setNode(mappingForRight.getStoredObject_ID() + mappingForRight.getStoredNode_ID()) ;
			}
		}
	}
	
	/**
	 * Get the mapping for a temporary tree or node
	 * 
	 * @param aMappings Mappings to look into
	 * @param sTreeId   Tree Id to be found
	 * @param sNodeId   Node Id to be found (can be null if only a tree Id is looked for)
	 * 
	 * @return The proper mapping if found, <code>null</code> if not
	 */
	protected LdvGraphMapping getMappingForId(final Vector<LdvGraphMapping> aMappings, final String sTreeId, final String sNodeId)
	{
		if ((null == aMappings) || aMappings.isEmpty() || (null == sTreeId) || "".equals(sTreeId))
			return null ;
		
		boolean bJustTree = (null == sNodeId) || "".equals(sNodeId) ;
		
		for (Iterator<LdvGraphMapping> itr = aMappings.iterator() ; itr.hasNext() ; )
		{
			LdvGraphMapping mapping = itr.next() ;
			
			if ((sTreeId.equals(mapping.getTemporaryObject_ID())) && (bJustTree || sNodeId.equals(mapping.getTemporaryNode_ID())))
				return mapping ;
		}
		
		return null ;
	}
	
	/**
	 * Get the mapping for a temporary link (tree Id or tree Id + node Id)
	 * 
	 * @param aMappings  Mappings to look into
	 * @param sComposite Tree Id or (tree Id + node Id) to be found
	 * 
	 * @return The proper mapping if found, <code>null</code> if not
	 */
	protected LdvGraphMapping getMappingForComposite(final Vector<LdvGraphMapping> aMappings, final String sComposite)
	{
		if ((null == aMappings) || aMappings.isEmpty() || (null == sComposite))
			return null ;
		
		int iCompositeLen = sComposite.length() ;
		if (iCompositeLen < LdvGraphConfig.OBJECT_ID_LEN)
			return null ;
		
		String sTreeId = sComposite.substring(0, LdvGraphConfig.OBJECT_ID_LEN) ;
		String sNodeId = LdvGraphTools.getNodeNodeId(sComposite) ;
		
		return getMappingForId(aMappings, sTreeId, sNodeId) ;
	}
	
	/**
	 *  Parse trees inside _modelGraph in order to initialize _rosacesLibrary
	 */
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
	 */
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
	 */
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
		_linksManager.getLinkedNodes(sProjectRootNode, LdvLinksManager.nodeLinkType.projectIndex, aProjectIndex) ;
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
	 */
	protected void initProjectLabel(LdvModelProject project, final LdvModelTree tree)
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
	 */
	protected void initIndexElements(LdvModelProject project, final String sIndexRootNode)
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
	 */
	protected void initConcerns(LdvModelProject project, final LdvModelTree tree, final LdvModelNode fatherNode)
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
	 */
	protected void initConcern(LdvModelProject project, final LdvModelTree tree, final LdvModelNode rootNode)
	{
		if ((null == project) || (null == tree) || (null == rootNode) || (false == isFunctionnal()))
			return ;
		
		LdvModelConcern newConcern = new LdvModelConcern() ;
		
		newConcern.setID(rootNode.getNodeURI()) ;
		
		initializeNodeTitle(newConcern, rootNode) ;
		
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
			else if (sSemanticConcept.equals("6CISP")) {
				newConcern.setContinuityCode(sonNode.getComplement()) ;
			}
			else if (sSemanticConcept.equals("�?????")) {
				newConcern.setTitle(sonNode.getFreeText()) ;
			}
			
			sonNode = tree.findFirstBrother(sonNode) ;
		}
		
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
		if ("�?????".equals(sLexiconCode))
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
	 * Get the ID of the label document for project's index
	 * 
	 * @param projectModel Project to get index ID of
	 * 
	 * @return The tree ID of project's index if found, <code>""</code> if not found
	 */
	public String getProjectIndexLabel(LdvModelProject projectModel)
	{
		if (null == projectModel)
			return "" ;
		
		// Get the label tree ID for this project
		//
		String sProjectId = projectModel.getProjectUri() ;
		if ("".equals(sProjectId))
			return "" ;
		
		// Get the document(s) using 
		//
		ArrayList<String> aResultNodes = new ArrayList<String>() ; 
		_linksManager.getLinkedNodes(sProjectId, LdvLinksManager.nodeLinkType.projectIndex, aResultNodes) ;
		
		if (aResultNodes.isEmpty())
			return "" ;
		
		Iterator<String> itr = aResultNodes.iterator() ;
		return itr.next() ;
	}
	
	/**
	 * Get the ID of the data document for project's index
	 * 
	 * @param projectModel Project to get index ID of
	 * 
	 * @return The tree ID of project's index if found, <code>""</code> if not found
	 */
	public String getProjectIndexData(LdvModelProject projectModel)
	{
		if (null == projectModel)
			return "" ;
		
		// Get the label tree ID for this project
		//
		String sProjectLabelId = getProjectIndexLabel(projectModel) ;
		if ("".equals(sProjectLabelId))
			return "" ;
		
		// Return the data ID from label ID 
		//
		return getDataIdFromLabelId(sProjectLabelId) ;
	}
	
	/**
	 * Create a new concern line
	 * 
	 * @param newConcern   Description of new concern to be created
	 * @param projectModel Project to add it to
	 * 
	 * @return The node ID of the root node for the new concern
	 */
	public String insertNewConcern(LdvModelConcern newConcern, LdvModelProject projectModel)
	{
		if ((null == newConcern) || (null == projectModel))
			return "" ;
		
		// Check if the concern is "valid" (at least a Lexicon or a title, and a starting date)
		//
		if ("".equals(newConcern.getLexicon()) && "".equals(newConcern.getTitle()))
			return "" ;
		if (newConcern.getBeginDate().isEmpty())
			return "" ;
		
		// Get the data tree ID for this project's index
		//
		String sProjectDataId = getProjectIndexData(projectModel) ;
		if ("".equals(sProjectDataId))
			return "" ;
		
		// Get the tree that contains information for this project
		//
		LdvModelTree projectTree = _modelGraph.getTreeFromId(sProjectDataId) ;
		
		if (null == projectTree)
			return "" ;
		
		// The root node contains project's type
		//
		LdvModelNodeArray tree = projectTree.getNodes() ;
		if (null == tree)
			return "" ;
		
		// LdvModelNode projectRootNode = tree.getFirstRootNode() ;
		
		// Find the concerns' library node (by the Lexicon "0PRO11")
		//
		LdvModelNode rootNodeForConcerns = tree.findItem("0PRO1", true, null) ;
		
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
			concernRootNode = new LdvModelNode("�?????", newConcern.getTitle(), true) ;
		concernRootNode.setAsRoot() ;			
		newConcernTree.addNode(concernRootNode) ;
			
		// If there is a type and a title, add the title
		//
		if ((false == "".equals(newConcern.getLexicon())) && (false == "".equals(newConcern.getTitle())))
		{
			BBMessage msg = new BBMessage() ;
			msg.setLexique("�?????") ;
			msg.setFreeText(newConcern.getTitle()) ;
			newConcernTree.addNode("�?????", msg, 1) ;
		}
			
		// Begin date
		//
		if ((null != newConcern.getBeginDate()) && (false == newConcern.getBeginDate().isEmpty()))
		{
			newConcernTree.addNode("KOUVR1", 1) ;
				
			BBMessage msg = new BBMessage() ;
			msg.setUnit("2DA021") ;
			msg.setComplement(newConcern.getBeginDate().getLocalDateTime()) ;
			newConcernTree.addNode("�T0;19", msg, 2) ;
		}
			
		// End date
		//
		if ((null != newConcern.getEndDate()) && (false == newConcern.getEndDate().isEmpty()))
		{
			newConcernTree.addNode("KFERM1", 1) ;
					
			BBMessage msg = new BBMessage() ;
			msg.setUnit("2DA021") ;
			msg.setComplement(newConcern.getEndDate().getLocalDateTime()) ;
			newConcernTree.addNode("�T0;19", msg, 2) ;
		}
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
	 */
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
	 */
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
	
	/**
	 *  Get the Id of tree containing data from Id of document's label tree 
	 *  
	 * @param  sLabelId Id of label tree
	 * 
	 * @return Id of data tree or ""
	 */
	public String getDataIdFromLabelId(final String sLabelId) {
		return _linksManager.getDataIdFromLabelId(sLabelId) ;
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
