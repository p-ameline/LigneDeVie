package com.ldv.client.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;

import com.ldv.client.model.LdvModelConcern;
import com.ldv.client.model.LdvModelDemographics;
import com.ldv.client.model.LdvModelProject;
import com.ldv.client.model.LdvModelRosace;
import com.ldv.client.util.LdvLinksManager.traitDirection;
import com.ldv.shared.database.Lexicon;
import com.ldv.shared.graph.LdvGraphConfig;
import com.ldv.shared.graph.LdvGraphMapping;
import com.ldv.shared.graph.LdvGraphTools;
import com.ldv.shared.graph.LdvModelGraph;
import com.ldv.shared.graph.LdvModelLink;
import com.ldv.shared.graph.LdvModelModel;
import com.ldv.shared.graph.LdvModelModelArray;
import com.ldv.shared.graph.LdvModelNode;
import com.ldv.shared.graph.LdvModelProjectGraph;
import com.ldv.shared.graph.LdvModelRight;
import com.ldv.shared.graph.LdvModelRightArray;
import com.ldv.shared.graph.LdvModelTree;
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
		
		// Reset the object 
		//
		_projectsModels.clear() ;
		
		if (null != _demographics)
			_demographics.reset() ;
		
		_linksManager = new LdvLinksManager(this.getGraph()) ;
		_treesManager = new LdvTreesManager(this.getGraph()) ;
		
		if (null == _modelGraph)
			return ;
		
		// Take care to init the rosaces library before projects
		//
		initRosacesLibrary() ;
			
		initProjects() ;
		initDemographics() ;
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
	public LdvModelRosace getRosaceForProject(String sProjectRootNodeId)
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
		
		Vector<LdvModelProjectGraph> aProjects = _modelGraph.getProjects() ;
		if (aProjects.isEmpty())
			return ;
		
		for (Iterator<LdvModelProjectGraph> itr = aProjects.iterator() ; itr.hasNext() ; )
			initProjectFromRoot(itr.next()) ;
		
/*  Algorithm before projects were already stored inside LdvModelProjectGraph objects 
		
		String sRootId = _modelGraph.getRootID() ;
		if ((null == sRootId) || "".equals(sRootId))
			return ;
		
		// Get the Id of all project description trees
		//
		ArrayList<String> aProjectsRootNodes = new ArrayList<String>() ;
		_linksManager.getLinkedNodes(sRootId, LdvLinksManager.nodeLinkType.project , aProjectsRootNodes) ;
		
		if (aProjectsRootNodes.isEmpty())
			return ;
		
		// Initialize all 
		//
		for (Iterator<String> itr = aProjectsRootNodes.iterator() ; itr.hasNext() ; )
			initProjectFromRoot(itr.next()) ;
*/
	}
	
	/**
	 *  Parse trees inside _modelGraph in order to initialize a LdvModelProject
	 *  
	 *  @param sProjectRootNode Root node of project to initialize
	 *    
	 */
	void initProjectFromRoot(LdvModelProjectGraph projectGraph)
	{
		if ((null == projectGraph) || (false == isFunctionnal()))
			return ;
		
		String sProjectRootNode = projectGraph.getProjectID() ;
		
		LdvModelProject project = new LdvModelProject(this, projectGraph) ;
		
		project.setProjectUri(sProjectRootNode) ;
		project.initFromRoot() ;
		
		_projectsModels.add(project) ;
	}
	
	public void initializeNodeTitle(LdvModelConcern concern, final LdvModelNode node) 
	{
		if (null == node)
			return ;
		
		String sLexiconCode = node.getLexicon() ;
		
		// Case for free text 
		//
		if (LdvGraphConfig.FREE_TEXT_LEX.equals(sLexiconCode))
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
