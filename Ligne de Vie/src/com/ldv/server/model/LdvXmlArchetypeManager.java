package com.ldv.server.model;

import java.io.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.ldv.server.Logger;
import com.ldv.server.archetype.LdvArchetypeHandler;
import com.ldv.shared.archetype.LdvArchetype;

/**
 * A LdvXmlArchetypeManager manages Archetypes as a Dom Document
 * 
 **/
public class LdvXmlArchetypeManager
{	
	protected String                 _sFileName ;	
	
	public static String LINKS_ROOT_LABEL      = "links" ;
	public static String LINK_LABEL            = "link" ;
	
	public static String LINK_QUALIFIED_ATTR   = "qualified" ;
	public static String LINK_LINK_ATTR        = "semantic" ;
	public static String LINK_QUALIFIER_ATTR   = "qualifier" ;
	public static String LINK_TRANSACTION_ATTR = "transaction" ;
	
	public static String RIGHTS_ROOT_LABEL     = "rights" ;
	public static String RIGHT_LABEL           = "right" ;
	
	public static String RIGHT_DOCUMENT_ATTR   = "document" ;
	public static String RIGHT_NODE_ATTR       = "node" ;
	public static String RIGHT_ROSE_ATTR       = "rose" ;
	public static String RIGHT_VALUE_ATTR      = "value" ;
	
	public static String LABEL_DIALOGUE              = "dialog" ;
	public static String LABEL_REFERENCES            = "references" ;
	public static String LABEL_CONCERN               = "concern" ;
	public static String LABEL_HEAD                  = "head" ;
	
	public static String LABEL_CONTRAINTES           = "constraints" ;
	public static String LABEL_CONTRAINTE            = "constraint" ;
	public static String LABEL_REFERENTIEL           = "referential" ;
	public static String LABEL_GUIDELINE             = "guideline" ;
	public static String LABEL_PROPOSITION           = "proposition" ;
	public static String LABEL_VALIDITE              = "validity" ;
	public static String LABEL_VALIDATEUR            = "validator" ;
	public static String LABEL_FRIENDSGROUP          = "friendsgroup" ;
	public static String LABEL_EXCLUDE               = "excludes" ;
	public static String LABEL_NEED                  = "needs" ;
	public static String LABEL_FRIEND                = "friend" ;
	public static String LABEL_VARIABLE              = "var" ;
	public static String LABEL_ALIAS                 = "alias" ;
	public static String LABEL_LOCAL_ALIAS           = "local_alias" ;
	public static String LABEL_TREE                  = "tree" ;
	public static String LABEL_NODE                  = "node" ;
	public static String LABEL_GLOBALVARS            = "globalvars" ;
	
	public static String LABEL_DIALOGBOX_RCDATA      = "rcdata" ;
	public static String LABEL_DIALOGBOX_ONGLETS     = "tabcontrol" ;
	public static String LABEL_ONGLET                = "tab" ;


	
	public static String ATTRIBUT_GUIDELINE_NAME     = "name" ;
	public static String ATTRIBUT_DIALOGUE_NOM       = "name" ;
	public static String ATTRIBUT_DIALOGUE_DLL       = "dll" ;
	public static String ATTRIBUT_DIALOGUE_REF       = "ref" ;
	public static String ATTRIBUT_CONCERN_CODE       = "code" ;
	public static String ATTRIBUT_CONCERN_FRAME      = "frame" ;
	public static String ATTRIBUT_CONCERN_CATEGORY   = "category" ;
	public static String ATTRIBUT_CONCERN_SEVE       = "severity" ;
	public static String ATTRIBUT_CONCERN_AUTO       = "autocreate" ;
	public static String ATTRIBUT_HEAD_LANG          = "lang" ;
	public static String ATTRIBUT_HEAD_TITLE         = "title" ;
	public static String ATTRIBUT_HEAD_HELP          = "help_url" ;
	
	public static String ATTRIBUT_CONTR_NOM          = "name" ;
	public static String ATTRIBUT_CONTR_LABEL        = "label" ;
	public static String ATTRIBUT_CONTR_PUBLISH      = "publish" ;
	public static String ATTRIBUT_CONTR_CLASSIFY     = "classify" ;
	public static String ATTRIBUT_CONTR_TYPE         = "type" ;
	public static String ATTRIBUT_CONTR_LISTE        = "list" ;
	public static String ATTRIBUT_CONTR_EXP          = "expression" ;
	public static String ATTRIBUT_CONTR_VAR          = "var" ;
	public static String ATTRIBUT_CONTR_MIN          = "min" ;
	public static String ATTRIBUT_CONTR_MAX          = "max" ;
	public static String ATTRIBUT_REFER_TITLE        = "title" ;
	public static String ATTRIBUT_REFER_NAME         = "name" ;
	public static String ATTRIBUT_REFER_EVOLUTIONOF  = "evolution_of" ;
	public static String ATTRIBUT_PROP_NOM           = "name" ;
	public static String ATTRIBUT_PROP_GROUPE        = "group" ;
	public static String ATTRIBUT_PROP_HELP          = "help_url" ;
	public static String ATTRIBUT_PROP_ID            = "id" ;
	public static String ATTRIBUT_PROP_REPLACE       = "replace" ;
	public static String ATTRIBUT_PROP_SAMEAS        = "same_as" ;
	public static String ATTRIBUT_PROP_EVIDENCE_LVL  = "evidence_level" ;
	public static String ATTRIBUT_PROP_AUTOCHECK     = "autocheck" ;
	public static String ATTRIBUT_PROP_UNCHECK_ARCH  = "uncheck_archetype" ;
	public static String ATTRIBUT_VAR_NOM            = "name" ;
	public static String ATTRIBUT_VAR_LABEL          = "label" ;
	public static String ATTRIBUT_VAR_EXPRESSION     = "expression" ;
	public static String ATTRIBUT_VAR_UNIT           = "unit" ;
	public static String ATTRIBUT_VALIDATEUR_COND    = "condition" ;
	public static String ATTRIBUT_TREE_LOC           = "localisation" ;
	public static String ATTRIBUT_NODE_LOC           = "codeloc" ;
	public static String ATTRIBUT_NODE_TYPE          = "type" ;
	public static String ATTRIBUT_NODE_LEXIQUE       = "lexique" ;
	public static String ATTRIBUT_NODE_UNIT          = "unit" ;
	public static String ATTRIBUT_NODE_COMPLEMENT    = "complement" ;
	public static String ATTRIBUT_NODE_CERTITUDE     = "certitude" ;
	public static String ATTRIBUT_NODE_INTERET       = "interet" ;
	public static String ATTRIBUT_NODE_PLURIEL       = "pluriel" ;
	public static String ATTRIBUT_NODE_FREETEXT      = "freetext" ;
	public static String ATTRIBUT_FRIEND_ID          = "id" ;
	
	public static String ATTRIBUT_RCDATA_NSTITRE     = "nstitre" ;
	public static String ATTRIBUT_RCDATA_NSCONTRO    = "nscontro" ;
	
	public static String ATTRIBUT_ONGLET_TITRE       = "title" ;
	public static String ATTRIBUT_ONGLET_NUM         = "order" ;
	
	public static String VAL_ATTR_ITEM_CTRL_ACTIF    = "active" ;
	public static String VAL_ATTR_ITEM_CTRL_NONE     = "none" ;
	public static String VAL_ATTR_CONTR_TYPE_EXCLUS  = "exclusion" ;
	public static String VAL_ATTR_CONTR_TYPE_EXP     = "expression" ;
	public static String VAL_ATTR_CONTR_TYPE_COND    = "condition" ;
	public static String VAL_ATTR_CONTR_TYPE_EXIST   = "exist" ;
	public static String VAL_ATTR_CONTR_TYPE_SONSEX  = "sonsexist" ;
	public static String VAL_ATTR_CONTR_TYPE_NEEDED  = "needed" ;
	
	public LdvXmlArchetypeManager(final String sFileName)
	{
		_sFileName  = sFileName ;
	}
		
	/**
	 * Fills an array of links from file
	 * 
	 * @param sDirectory directory where to find file 
	 * @param aLinks array of LdvModelLink to fill
	 * @return true if everything went well
	 * 
	 **/
	public LdvArchetype openDocument()
	{
		FileInputStream fi ;
		try
		{
			fi = new FileInputStream(_sFileName) ;
		} 
		catch (FileNotFoundException e)
		{
			Logger.trace("LdvXmlArchetypeManager.openDocument: input stream exception for file " + _sFileName + " ; stackTrace:" + e.getStackTrace(), -1, Logger.TraceLevel.ERROR) ;
			return null ;
		}
		
		Document archetypeDocument = getDocumentFromInputSource(new InputSource(fi), false) ;
		if (null == archetypeDocument)
			return null ;
		
		LdvArchetype archetype = parseDocument(archetypeDocument) ;
		
    return archetype ;
	}
	
	/**
	* Creates a Document from an InputSource
	* 
	* @param inputSource input source content
	* @param mustValidate true if the xml content must be validated
	* 
	* @return a Document if everything went well, <code>null</code> if not
	* 
	**/
	public Document getDocumentFromInputSource(InputSource inputSource, boolean mustValidate)
	{
		if (null == inputSource) 
			return null ;
	
		// Get factory instance
		//
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance() ;
	
		if (false == mustValidate)
		{
			factory.setNamespaceAware(false) ;
			factory.setValidating(false) ;
			try
			{
				factory.setFeature("http://xml.org/sax/features/namespaces", false) ;
				factory.setFeature("http://xml.org/sax/features/validation", false) ;
				factory.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false) ;
				factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false) ;
			} 
			catch (ParserConfigurationException e)
			{
				Logger.trace("LdvXmlGraph.getDocumentFromInputSource: parser config exception for file \"" + _sFileName + "\" ; stackTrace:" + e.getStackTrace(), -1, Logger.TraceLevel.ERROR) ;
				e.printStackTrace();
			}
		}
	
		DocumentBuilder builder ;
		try
		{
			builder = factory.newDocumentBuilder() ;
		} 
		catch (ParserConfigurationException e)
		{
			Logger.trace("LdvXmlGraph.getDocumentFromInputSource: parser config exception when getting a document builder for file \"" + _sFileName + "\" ; stackTrace:" + e.getStackTrace(), -1, Logger.TraceLevel.ERROR) ;
			return null ;
		}
  
		// Create documents
		//
		Document document = null ;
  
		try
		{
			document = builder.parse(inputSource) ;
		} 
		catch (SAXException e) {
			Logger.trace("LdvXmlGraph.getDocumentFromInputSource: parser exception for file \"" + _sFileName + "\" ; stackTrace:" + e.getStackTrace(), -1, Logger.TraceLevel.ERROR) ;
			return null ;
		} 
		catch (IOException e) {
			Logger.trace("LdvXmlGraph.getDocumentFromInputSource: parser IO exception for file \"" + _sFileName + "\" ; stackTrace:" + e.getStackTrace(), -1, Logger.TraceLevel.ERROR) ;
			return null ;
		}
      
		return document ;
	}

	/**
	* Create an Archetype from the XML Document  
	* 
	* @param archetypeDocument the XML Dom Document to be parsed
	* 
	* @return a LdvArchetype if everything went well, <code>null</code> if not
	* 
	**/
	public LdvArchetype parseDocument(Document archetypeDocument)
	{
		if (null == archetypeDocument)
			return null ;
		
		LdvArchetypeHandler archetype = new LdvArchetypeHandler() ;
		if (archetype.initFromDocument(archetypeDocument))
			return (LdvArchetype) archetype ;
		
		return null ;
	}
}
