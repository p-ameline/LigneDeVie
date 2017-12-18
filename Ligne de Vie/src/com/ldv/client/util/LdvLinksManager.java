package com.ldv.client.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;

import com.ldv.shared.graph.LdvModelGraph;
import com.ldv.shared.graph.LdvModelLink;
import com.ldv.shared.model.LdvArrayUtilities;

public class LdvLinksManager 
{
	public enum nodeLinkType 
	{ 
		badLink,
		project,
		problemRelatedTo, problemContactElement,
    soapRelatedTo, problemGoals,
    goalOpener, goalReseter, goalAlarm, goalCloser,
    archetype, refCreator, referentialOf, guidelineOf,
    drugOf, treatmentOf, prescriptionElement,
    personDocument, personHealthIndex, personSocialIndex,
    personSynthesis, personIndexExtension,
    personAdminData, personHealthTeam, personContribution,
    personFolderLibrary, personHealthProData, personRiskManager,
    docData, docFolder, docPilot, contribElement,
    indexConcerns, indexGoals,
    docComposition, compositionTag,
    processWaitingFor, processResultFrom, objectIn, hiddenBy,
    contributionAdded, contributionModified, contributionOpened,
    copyOf,
    fonctionalUnitStay, stayContribution, personIdentifiers,
    OCRizedDocument, semantizedDocument, viewOfDocument, letterOfDocument, externViewOfDocument, structuredVersionOfDocument,
    appointment,
    rosacesLibrary, rosaceModel
  };
  
  public enum traitDirection { arrow, reverse } ;
	
	private Vector<LdvModelLink> _aLinks ;
	
	public LdvLinksManager(LdvModelGraph graph)
	{
		if (null != graph)
			_aLinks = graph.getLinks() ;
		else
			_aLinks = null ;
	}
	
	/**
	*  Get all nodes that are linked to the reference node with a given type in the regular direction 
	*  
	* @param sReferenceNode   The node result nodes must be linked to
	* @param iRelation        Relation of links to be considered
	* @param aResultNodes     Array of result nodes
	*
	**/
	public void getLinkedNodes(String sReferenceNode, nodeLinkType iRelation, ArrayList<String> aResultNodes) {
		getLinkedNodes(sReferenceNode, iRelation, aResultNodes, traitDirection.arrow) ;
	}
	
	/**
	*  Get all nodes that are linked to the reference node with a given type in a given direction 
	*  
	* @param sReferenceNode   The node result nodes must be linked to
	* @param iRelation        Relation of links to be considered
	* @param aResultNodes     Array of result nodes
	* @param iSearchDirection Direction of links to be considered
	*
	**/
	public void getLinkedNodes(String sReferenceNode, nodeLinkType iRelation, ArrayList<String> aResultNodes, traitDirection iSearchDirection)
	{
		if ((null == sReferenceNode) || sReferenceNode.equals("") || (null == aResultNodes))
			return ;
		
		if ((null == _aLinks) || _aLinks.isEmpty())
			return ;
		
		// Looking for the code corresponding to this link type
		String sLinkCode = getLinkTypeAsString(iRelation) ;
		if (sLinkCode.equals(""))
			return ;
		
		// Browsing through all links
		//
		for (Iterator<LdvModelLink> itr = _aLinks.iterator() ; itr.hasNext() ; )
		{
			LdvModelLink link = itr.next() ;
			
	  	if (sLinkCode.equals(link.getLink()) &&
	        (((traitDirection.arrow   == iSearchDirection) && sReferenceNode.equals(link.getQualified())) ||
	         ((traitDirection.reverse == iSearchDirection) && sReferenceNode.equals(link.getQualifier())))
	       )
	    {
	  		String sResult = link.getQualifier() ;
	  		if (traitDirection.reverse == iSearchDirection)
	  			sResult = link.getQualified() ;
	  		
	      // Checking that node doesn't already exist in results array
	  		//
	  		if (false == LdvArrayUtilities.isStringInArray(sResult, aResultNodes))
	  			aResultNodes.add(sResult) ;	  			
	    }
	  }
	}
	
	/**
	*  Get the Id of tree containing data from Id of document's label tree 
	*  
	* @param  sLabelId Id of label tree
	* @return Id of data tree or ""
	*
	**/
	public String getDataIdFromLabelId(String sLabelId)
	{
		if (null == sLabelId)
			return "" ;
		
		ArrayList<String> aDataIndex = new ArrayList<String>() ;
		getLinkedNodes(sLabelId, nodeLinkType.docData , aDataIndex) ;
		if (aDataIndex.isEmpty())
			return "" ;
		
		Iterator<String> itr = aDataIndex.iterator() ;
		
		return itr.next() ;
	}
	
	/**
	*  Get the Lexique semantic code that corresponds to a type 
	*  
	* @param  iRelation Type of a graph link
	* @return Lexique semantic code
	*
	**/
	public static String getLinkTypeAsString(nodeLinkType iRelation)
	{
		switch (iRelation)
		{
	  	case project                     : return "0PROJ" ;
	  	case problemRelatedTo            : return "0PRO1" ;
			case problemContactElement       : return "0PREL" ;
			case soapRelatedTo               : return "0PRCL" ;
			case problemGoals	               : return "0OBJE" ;
			case goalOpener                  : return "0OJOP" ;
			case goalReseter                 : return "0OJRE" ;
			case goalAlarm                   : return "0OJAL" ;
			case goalCloser	                 : return "0OJCL" ;
			case archetype	                 : return "0ARCH" ;
			case refCreator		               : return "0DOCR" ;
			case guidelineOf			           : return "0ARCR" ;
	    case referentialOf			         : return "0RECR" ;
			case drugOf					             : return "N0000" ;
			case treatmentOf						     : return "GTRAI" ;
	    case prescriptionElement         : return "0SOA3" ;
			case personHealthIndex           : return "ZPOMR" ;
	    case personSocialIndex           : return "ZPSOC" ;
			case personSynthesis				     : return "ZSYNT" ;
	    case personIndexExtension        : return "0EXIX" ;
			case personAdminData             : return "ZADMI" ;
			case personDocument              : return "ZDOCU" ;
			case personFolderLibrary	       : return "0LIBC" ;
			case personHealthProData	     	 : return "DPROS" ;
	    case personRiskManager           : return "ORISK" ;
			case personHealthTeam		     		 : return "LEQUI" ;
			case docData					     		   : return "ZDATA" ;
			case docFolder                   : return "0CHEM" ;
			case docPilot                    : return "OSERV" ;
			case personContribution          : return "LCTRI" ;
			case contribElement              : return "" ; // non utilisé
	    case indexConcerns               : return "0PRO1" ;
	    case indexGoals                  : return "0OBJE" ;
			case docComposition              : return "ZPRES" ;
			case compositionTag              : return "0TAGC" ;
			case processWaitingFor           : return "0PRWA" ;
			case processResultFrom           : return "0PRRE" ;
			case objectIn                    : return "OCOMP" ;
			case hiddenBy                    : return "" ; // non utilisé
			case contributionAdded           : return "0CTCR" ;
			case contributionModified        : return "0CTMO" ;
			case contributionOpened          : return "0CTCO" ;
			case copyOf                      : return "0COPY" ;
			case fonctionalUnitStay          : return "LSEJO" ;
			case stayContribution            : return "0CTSE" ;
			case personIdentifiers           : return "0LIBI" ;
	    case OCRizedDocument             : return "ZDOCR" ;
	    case semantizedDocument          : return "ZDSEM" ;
	    case viewOfDocument              : return "ZDODE" ;
	    case externViewOfDocument        : return "ZDINT" ;
	    case structuredVersionOfDocument : return "ZDSTR" ;
	    case letterOfDocument            : return "ZLETT" ;
	    case appointment                 : return "" ;
	    case rosacesLibrary              : return "0LIBR" ;
	    case rosaceModel                 : return "0MORO" ;
		  case badLink                     : return "" ;
	  }

	  return "" ;
	}


	/**
	*  Get the type that corresponds to a Lexique semantic code 
	*  
	* @param  sRelation Lexique semantic code
	* @return Corresponding type
	*
	**/
	public static nodeLinkType getLinkTypeFromCode(String sRelation)
	{
		if (sRelation.length() != 5)
	  	return nodeLinkType.badLink ;

		if ("0PROJ" == sRelation) return nodeLinkType.project ;
		if ("0PRO1" == sRelation) return nodeLinkType.problemRelatedTo ;
		if ("0PREL" == sRelation) return nodeLinkType.problemContactElement ;
		if ("0PRCL" == sRelation) return nodeLinkType.soapRelatedTo ;
		if ("0OBJE" == sRelation) return nodeLinkType.problemGoals ;
		if ("0OJOP" == sRelation) return nodeLinkType.goalOpener	;
		if ("0OJRE" == sRelation) return nodeLinkType.goalReseter ;
		if ("0OJAL" == sRelation) return nodeLinkType.goalAlarm ;
		if ("0OJCL" == sRelation) return nodeLinkType.goalCloser	;
		if ("0ARCH" == sRelation) return nodeLinkType.archetype ;
		if ("0DOCR" == sRelation) return nodeLinkType.refCreator	;
		if ("0ARCR" == sRelation) return nodeLinkType.guidelineOf ;
	  if ("0RECR" == sRelation) return nodeLinkType.referentialOf ;
		if ("N0000" == sRelation) return nodeLinkType.drugOf	;
		if ("GTRAI" == sRelation) return nodeLinkType.treatmentOf ;
	  if ("0SOA3" == sRelation) return nodeLinkType.prescriptionElement ;
		if ("ZPOMR" == sRelation) return nodeLinkType.personHealthIndex ;
	  if ("ZPSOC" == sRelation) return nodeLinkType.personSocialIndex ;
		if ("ZSYNT" == sRelation) return nodeLinkType.personSynthesis ;
	  if ("0EXIX" == sRelation) return nodeLinkType.personIndexExtension ;
		if ("ZADMI" == sRelation) return nodeLinkType.personAdminData ;
		if ("ZDOCU" == sRelation) return nodeLinkType.personDocument	;
		if ("0LIBC" == sRelation) return nodeLinkType.personFolderLibrary ;
		if ("DPROS" == sRelation) return nodeLinkType.personHealthProData ;
	  if ("ORISK" == sRelation) return nodeLinkType.personRiskManager ;
		if ("LEQUI" == sRelation) return nodeLinkType.personHealthTeam	;
		if ("ZDATA" == sRelation) return nodeLinkType.docData ;
		if ("0CHEM" == sRelation) return nodeLinkType.docFolder ;
		if ("OSERV" == sRelation) return nodeLinkType.docPilot ;
	  if ("0PRO1" == sRelation) return nodeLinkType.indexConcerns ;
	  if ("0OBJE" == sRelation) return nodeLinkType.indexGoals ;
		if ("LCTRI" == sRelation) return nodeLinkType.personContribution ;
		if ("ZPRES" == sRelation) return nodeLinkType.docComposition ;
		if ("0TAGC" == sRelation) return nodeLinkType.compositionTag ;
		if ("0PRWA" == sRelation) return nodeLinkType.processWaitingFor ;
		if ("0PRRE" == sRelation) return nodeLinkType.processResultFrom ;
		if ("OCOMP" == sRelation) return nodeLinkType.objectIn ;
		if ("0CTCR" == sRelation) return nodeLinkType.contributionAdded ;
		if ("0CTMO" == sRelation) return nodeLinkType.contributionModified ;
		if ("0CTCO" == sRelation) return nodeLinkType.contributionOpened ;
		if ("0COPY" == sRelation) return nodeLinkType.copyOf ;
		if ("LSEJO" == sRelation) return nodeLinkType.fonctionalUnitStay ;
		if ("0CTSE" == sRelation) return nodeLinkType.stayContribution ;
		if ("0LIBI" == sRelation) return nodeLinkType.personIdentifiers ;
	  if ("ZDOCR" == sRelation) return nodeLinkType.OCRizedDocument ;
	  if ("ZDSEM" == sRelation) return nodeLinkType.semantizedDocument ;
	  if ("ZDODE" == sRelation) return nodeLinkType.viewOfDocument ;
	  if ("ZDINT" == sRelation) return nodeLinkType.externViewOfDocument ;
	  if ("ZDSTR" == sRelation) return nodeLinkType.structuredVersionOfDocument ;
	  if ("ZLETT" == sRelation) return nodeLinkType.letterOfDocument ;
	  if ("0LIBR" == sRelation) return nodeLinkType.rosacesLibrary ;
	  if ("0MORO" == sRelation) return nodeLinkType.rosaceModel ;

		return nodeLinkType.badLink ;
	}
}
