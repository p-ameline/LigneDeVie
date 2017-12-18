package com.ldv.shared.graph;

public class LdvGraphEnum {

	//
	// Execution stage
	//
	enum SITUATION_TYPE 
	{ 
		NSDLGFCT_CREATION,
		NSDLGFCT_REINIT,
		NSDLGFCT_EXECUTE,
		NSDLGFCT_POSTEXEC,
		NSDLGFCT_PREFERME,
		NSDLGFCT_FERMETURE,
		NSDLGFCT_CTRL_KEY
	} ;

  //
  // Control types
	//
	enum WNDTYPE 
	{ 
		isUndefined, isDlg, isGroup, isEdit, isBtn, isRadioBtn, isCaseACocher,
		isFunct, isOnglet, isTreeNode, isTreeWindow, isEditLexique,
		isEditLexiqueDerive, isEditNoLexique, isNSCSVue, isStatic,
		isEditDate, isEditDateHeure, isAdrListWindow,
		isHistoryListWindow, isComboClassif, isComboSemantique,
		isComboList, isCorListWindow, isHistoryValListWindow 
	} ;

	enum NODELINKDIRECTION { dirFleche, dirEnvers } ;
	
	enum NODELINKTYPES 
	{	
		badLink,
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
    appointment 
	} ;
}
