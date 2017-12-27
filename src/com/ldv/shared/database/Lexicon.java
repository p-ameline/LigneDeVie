package com.ldv.shared.database ;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;

import com.google.gwt.user.client.rpc.IsSerializable ;
import com.ldv.shared.model.LdvModelLexicon;

/**
 * Lexicon.java
 *
 * The Lexicon class represents object type information in database
 * 
 * Created: 25 Dec 2011
 *
 * Author: PA
 * 
 */
public class Lexicon implements IsSerializable 
{
	private String _sLabel ;
	private String _sCode ;
	private String _sGrammar ;
	private String _sFrequency ;
	
	public enum LabelType   { rawLabel, selectionLabel, displayLabel} ;
	public enum Declination { nullDeclination, singular, plural } ;
	public enum Gender      { nullGender, MSGender, FSGender, NSGender, MPGender, FPGender, NPGender, ITGender } ;
	
	//
	//
	public Lexicon() {
		reset() ;
	}
		
	/**
	 * Standard constructor
	 */
	public Lexicon(final String sLabel, final String sCode, final String sGrammar, final String sFrequency) 
	{
		_sLabel     = sLabel ;
		_sCode      = sCode ;
		_sGrammar   = sGrammar ;
		_sFrequency = sFrequency ;
	}
	
	/**
	 * Copy constructor
	 */
	public Lexicon(final Lexicon model) 
	{
		reset() ;
		
		if (null == model)
			return ;
		
		_sLabel     = model._sLabel ;
		_sCode      = model._sCode ;
		_sGrammar   = model._sGrammar ;
		_sFrequency = model._sFrequency ;
	}
			
	public Lexicon(final LdvModelLexicon model) 
	{
		reset() ;
		
		if (null == model)
			return ;
		
		_sLabel     = model.getLabel() ;
		_sCode      = model.getCode() ;
		_sGrammar   = model.getGrammar() ;
		_sFrequency = model.getFrequency() ;
	}
	
	public void reset() 
	{
		_sLabel     = "" ;
		_sCode      = "" ;
		_sGrammar   = "" ;
		_sFrequency = "" ;
	}

	public String getLabel(LabelType iType, Declination iDeclination, final String sLanguage)
	{
		switch(iType)
		{
			case rawLabel       : return getLabel() ;
			case selectionLabel : return getSelectionLabel() ;
			case displayLabel   : return getDisplayLabel(iDeclination, sLanguage) ;
		}
		return getLabel() ;
	}

	/**
	*  Get a label, initially in the form "Crohn [maladie|s|][de]{détails}" in the form "maladie de Crohn"
	*  
	*  @param  iGender Grammatical gender for flexion
	*  @return Modified label if Ok, empty String if not
	**/
	public String getDisplayLabel(Declination iDeclination, final String sLanguage)
	{
		if ((null == _sLabel) || _sLabel.equals(""))
			return "" ;
		
		// Flexion information is located between ||, separated by /
	  // ex : "disjoint|t/te/ts/tes|"
	  //
	  // Three cases can be met:
	  // 1) No || -> invariable word
	  // 2) A single element between || (ex test|s|) that provides first flexion
	  //    with root being the whole word example: test|s| gives "test" and "tests" 
	  // 3) Multiple elements between || (ex animal|al/aux|), first element (0)
	  //    sets the root and next ones indicate flexions (1, 2...)

		int iBracketsLevel = -1 ;

		ArrayList<String> qualifiers = new ArrayList<String>() ;
	  String currentQualifier = "" ;
	  String sStartingWord    = "" ;
	  String sEndingWord      = "" ;
	  String sOrthography     = "" ;
		
	  // Label is separated in different parts:
	  //
	  // sStartingWord = "Crohn"
	  // sEndingWord   = "{détails}"
	  // qualifiers[0] = "maladie"
	  // qualifiers[1] = "de"
	  //
	  for (int k = 0 ; k < _sLabel.length() ; k++)
	  {
	  	// Start of a qualifier
	  	//
	    if ('[' == _sLabel.charAt(k))
	    {
	    	iBracketsLevel++ ;
	    	
	    	if (false == currentQualifier.equals(""))
	      	qualifiers.add(currentQualifier) ;
	    	
	    	currentQualifier = "" ;
	    	
	      k++ ;
	      while ((k < _sLabel.length()) && (']' != _sLabel.charAt(k)))
	      {
	      	// Start of flexion information
	      	//
	      	if ('|' == _sLabel.charAt(k))
	        {
	      		sOrthography = "" ;
	          k++ ;
	          while ((k < _sLabel.length()) && ('|' != _sLabel.charAt(k)))
	          {
	          	sOrthography += _sLabel.charAt(k) ;
	            k++ ;
	          }
	          if ((k < _sLabel.length()) && ('|' == _sLabel.charAt(k)))
	          	currentQualifier = applyOrthography(currentQualifier, sOrthography, iDeclination, sLanguage) ;
	          else
	            return "" ;
	        }
	        else
	        	currentQualifier += _sLabel.charAt(k) ;
	        k++ ;
	      }
	    }
	    else
	    {
	    	// A string starting by {| contains a preposition
	    	//
	      if      ((k < _sLabel.length() - 1) && ('{' == _sLabel.charAt(k)) && ('|' == _sLabel.charAt(k+1)))
	      {
	        while ((k < _sLabel.length()) && ('}' != _sLabel.charAt(k)))
	          k++ ;
	      }
	      else if ('|' == _sLabel.charAt(k))
	      {
	        k++ ;
	        sOrthography = "" ;
	        while ((k < _sLabel.length()) && ('|' != _sLabel.charAt(k)))
	        {
	        	sOrthography += _sLabel.charAt(k) ;
	          k++ ;
	        }
	        if ((k < _sLabel.length()) && ('|' == _sLabel.charAt(k)))
	        {
	          if (-1 == iBracketsLevel)
	          	sStartingWord = applyOrthography(sStartingWord, sOrthography, iDeclination, sLanguage) ;
	          else
	          	sEndingWord = applyOrthography(sEndingWord, sOrthography, iDeclination, sLanguage) ;
	        }
	        else
	          return "" ;
	      }
	      else
	      {
	        if (-1 == iBracketsLevel)
	        	sStartingWord += _sLabel.charAt(k) ;
	        else
	        	sEndingWord   += _sLabel.charAt(k) ;
	      }
	    }
	  }

	  if (false == currentQualifier.equals(""))
    	qualifiers.add(currentQualifier) ;
	  
	  // Building new label
	  //
	  char firstChar = '\0' ;
	  char lastChar  = '\0' ;

	  if (false == sStartingWord.equals(""))
	  	sStartingWord = sStartingWord.trim() ;

	  if (false == sEndingWord.equals(""))
	  	sEndingWord = sEndingWord.trim() ;
	  
	  String sResultLabel = "" ;
	  
	  if (false == qualifiers.isEmpty())
	  {
	  	Iterator<String> it = qualifiers.iterator() ;
	  	String sFirstQualifier = it.next() ;
	  	
	  	sResultLabel += sFirstQualifier ;
	  	lastChar = sFirstQualifier.charAt(sFirstQualifier.length() - 1) ;
	  	
	  	if (it.hasNext())
	  	{
	  		String sSecondQualifier = it.next() ;
	  		firstChar = sSecondQualifier.charAt(0) ;

	  		if ((' ' != firstChar) && ('-' != lastChar) && ('\'' != lastChar) && (' ' != lastChar))
	  			sResultLabel += " " ;

	  		sResultLabel += sSecondQualifier ;

	  		lastChar = sSecondQualifier.charAt(sSecondQualifier.length() - 1) ;
	  	}
	  }

	  if (false == sStartingWord.equals(""))
	  {
	  	firstChar = sStartingWord.charAt(0) ;

	    if (false == sResultLabel.equals(""))
	      if ((' ' != firstChar) && ('-' != lastChar) && ('\'' != lastChar) && (' ' != lastChar))
	      	sResultLabel += " " ;

	    sResultLabel += sStartingWord ;
	  }

	  // Omitting leading and trailing blanks
	  sResultLabel = sResultLabel.trim() ;
	  sResultLabel = removeTrailingComments(sResultLabel) ;
	  sResultLabel = removeUselessBlanks(sResultLabel) ;
	  sResultLabel = sResultLabel.trim() ;
	  
	  return sResultLabel ;
	}
	
	/**
	*  Get a label, initially in the form "Crohn [maladie|s|][de]{détails}" in the form "Crohn (maladie){détails}"
	*  
	*  @return Modified label if Ok, empty String if not
	**/
	public String getSelectionLabel()
	{
		if ((null == _sLabel) || _sLabel.equals(""))
			return "" ;
		
		// First, remove flexing information
		//
		String sNewLabel = getLabelWithoutFlexInfo() ;
		
		int iBracketsLevel = -1 ;

		ArrayList<String> qualifiers = new ArrayList<String>() ;
	  String currentQualifier = "" ;
	  String sStartingWord    = "" ;
	  String sEndingWord      = "" ;

	  // On sépare le libellé sous la forme :
	  // startingWord = "Crohn"
	  // endingWord   = "{détails}"
	  // qualifier[0] = "maladie"
	  // qualifier[1] = "de"
	  //
	  int iLabelLen = sNewLabel.length() ;
	  
	  for (int i = 0 ; i < iLabelLen ; i++)
	  {
	  	char currentChar = sNewLabel.charAt(i) ;
	    
	  	// Opening brackets: start of a qualifier
	  	//
	  	if ('[' == currentChar)
	    {
	    	iBracketsLevel++ ;
	      i++ ;
	      
	      if (false == currentQualifier.equals(""))
	      	qualifiers.add(currentQualifier) ;
	      
	      currentQualifier = "" ;

	      while ((']' != sNewLabel.charAt(i)) && (i < iLabelLen))
	      {
	      	currentQualifier += sNewLabel.charAt(i) ;
	        i++ ;
	      }
	    }
	    else
	    {
	      if (-1 == iBracketsLevel)
	      	sStartingWord += sNewLabel.charAt(i) ;
	      else
	      	sEndingWord += sNewLabel.charAt(i) ;
	    }
	  }

	  if (false == currentQualifier.equals(""))
    	qualifiers.add(currentQualifier) ;
	  
	  // Building new label
	  String sResultLabel = sStartingWord ;
	  if (false == qualifiers.isEmpty())
	  {
	  	Iterator<String> it = qualifiers.iterator() ;
	  	sResultLabel += "(" + it.next() + ")" ;
	  }
	  sResultLabel += sEndingWord ;

		// return sNewLabel ;
	  return sResultLabel ;
	}
	
	/**
	*  Flex information are between '|' - we have to remove it
	*  
	*  @return Modified label if Ok, empty String if not
	**/
	public String getLabelWithoutFlexInfo() 
	{
		if ((null == _sLabel) || _sLabel.equals(""))
			return "" ;
		
		// Flex information are between '|' - we have to remove it
		//
		String sNewLabel = "" ;
		boolean bInsidePipes = false ;
		for (int i = 0 ; i < _sLabel.length() ; i++)
		{
			char currentChar = _sLabel.charAt(i) ;
			if ('|' == currentChar)
				bInsidePipes = !bInsidePipes ;
			else if (false == bInsidePipes)
				sNewLabel += currentChar ;
		}
		
		sNewLabel = removeUselessBlanks(sNewLabel) ;
		
		return sNewLabel ;
  }
	
	/**
	*  Return a String without trailing comments -> mean it cuts at first '{'
	*  
	*  @param  sLabel String to be processed
	*  @return Modified string if Ok, empty String if not
	**/
	public static String removeTrailingComments(String sLabel) 
	{
		if ((null == sLabel) || sLabel.equals(""))
			return "" ;
		
		int iTouchingStart = sLabel.indexOf('{') ;
		int iSeparateStart = sLabel.indexOf(" {") ;
		
		if (-1 != iSeparateStart)
			return sLabel.substring(0, iSeparateStart) ;
		if (-1 != iTouchingStart)
			return sLabel.substring(0, iTouchingStart) ;
		
		return sLabel ;		
  }
	
	/**
	*  Return a String without useless separating blanks
	*  
	*  @param  sLabel String to be processed
	*  @return Modified string if Ok, empty String if not
	**/
	public static String removeUselessBlanks(String sLabel)
	{
		if ((null == sLabel) || sLabel.equals(""))
			return "" ;
		
		int iFirstBlank = sLabel.indexOf(' ') ;
		if (-1 == iFirstBlank)
			return sLabel ;
		
		while (-1 != iFirstBlank)
	  {
	    int iStart = iFirstBlank ;

	    int iLabelSize = sLabel.length() ;
	    if ((iStart < iLabelSize - 1) && (' ' == sLabel.charAt(iStart + 1)))
	    {
	      int iEnd = iStart + 1 ;
	      while ((iEnd < iLabelSize - 1) && (' ' == sLabel.charAt(iEnd + 1)))
	      	iEnd++ ;

	      sLabel = sLabel.substring(0, iStart) + " " + sLabel.substring(iEnd + 1, iLabelSize) ;
	    }

	    iStart++ ;
	    iFirstBlank = sLabel.indexOf(' ', iStart) ;
	  }
		
		return sLabel ;		
  }
	
	/**
	*  Return a String without useless separating blanks
	*  
	*  @param  sLabel String to be processed
	*  @param  sFlexInformation Information for flexion
	*  @param  iFlexIndex Index of flexion to be applied from sFlexInformation
	*  @return Modified string if Ok, empty String if not
	**/
	public String applyOrthography(String sLabel, String sFlexInformation, Declination iDeclination, String sLanguage)
	{
		if ((null == sLabel) || sLabel.equals(""))
			return "" ;
		
		// make certain undesired blanks don't get inserted between root and flexion
		//
		sLabel = sLabel.trim() ;

		// Get index for declination
	  //
	  int iFlexIndex = indexForDeclination(iDeclination, sLanguage) ;
	  if (-1 == iFlexIndex)
	  	return sLabel ;
		
	  // If flexion index is "basic", then nothing to do
	  //
	  if (0 == iFlexIndex)
	    return sLabel ;
	  
	  if ((null == sFlexInformation) || sFlexInformation.equals(""))
			return sLabel ;

	  Vector<String> aFlexInformation = new Vector<String>() ;
	  
	  int iFlexLen = sFlexInformation.length() ;
	  int iNbOfFlexElmnt = 0 ;
	  String sOrtho = "" ;
	  for (int i = 0 ; i < iFlexLen ; i++)
	  {
	    if ('/' == sFlexInformation.charAt(i))
	    {
	    	aFlexInformation.add(sOrtho) ;
	      sOrtho = "" ;
	      iNbOfFlexElmnt++ ;
	    }
	    else
	      sOrtho += sFlexInformation.charAt(i) ;
	  }

	  aFlexInformation.add(sOrtho) ;
    iNbOfFlexElmnt++ ;

    String sRootFlexion = aFlexInformation.elementAt(0) ;
    
	  // If there is a single flexion data :
	  //
	  //      if iFlexIndex == 0, nothing to do
	  //      if iFlexIndex == 1, flexion is added to the word
	  if (1 == iNbOfFlexElmnt)
	  {
	  	String sResultLabel = sLabel ;
	    if (1 == iFlexIndex)
		  	sResultLabel += sRootFlexion ;
	    return sResultLabel ;
	  }

	  // If multiple flexion data exist :
	  //      first we find the root from element 0
	  //      then we add asked flexion

	  if (iFlexIndex > aFlexInformation.size() - 1)
	  	return sLabel ;
	  
	  String sSelectedFlexData = aFlexInformation.elementAt(iFlexIndex) ;
	  
	  int iRootLen = sLabel.length() - sRootFlexion.length() ;
	  if (iRootLen <= 0)
	    return sSelectedFlexData ;

	  return sLabel.substring(0, iRootLen) + sSelectedFlexData ;
  }
	
	/**
	*  Return the flexion index for a noon in a given language and declination 
	*  
	*  @param  iDeclination Declination (singular or plural)
	*  @param  sLanguage Language
	*  @return The index of flexion choices if Ok, -1 if not
	**/
	private int indexForDeclination(Declination iDeclination, String sLanguage)
	{
		if ((null == sLanguage) || sLanguage.equals(""))
			return -1 ;
		
		if (false == this.isNoon())
			return -1 ;
		
		// For French and English languages, a noon is just singular or plural
		//
		if ((sLanguage.length() >= 2) && (sLanguage.substring(0, 2).equals("fr") ||
				                              sLanguage.substring(0, 2).equals("en")))
		{
			switch(iDeclination)
			{
				case nullDeclination : return -1 ;
				case singular        : return 0 ; 
				case plural          : return 1 ; 
			}
			return -1 ;
		}
		
		return -1 ;
	}
	
	public String getLabel() {
  	return _sLabel ;
  }
	public void setLabel(String sLabel) {
  	_sLabel = sLabel ;
  }

	public String getCode() {
  	return _sCode ;
  }
	public void setCode(String sCode) {
  	_sCode = sCode ;
  }

	public String getGrammar() {
  	return _sGrammar ;
  }
	public void setGrammar(String sGrammar) {
		_sGrammar = sGrammar ;
  }

	public String getFrequency() {
  	return _sFrequency ;
  }
	public void setFrequency(String sFrequency) {
		_sFrequency = sFrequency ;
  }
	
	/**
	  * Is this Lexicon entry a noon?
	  * 
	  * @return true if yes, false if not
	  */
	public boolean isNoon()
	{
		char cFirstChar = getGrammarFirstChar() ;
		return (('M' == cFirstChar) || ('F' == cFirstChar) || ('N' == cFirstChar)) ;
	}
	
	/**
	  * Is this Lexicon entry a verb?
	  * 
	  * @return true if yes, false if not
	  */
	public boolean isVerb() {
		return ('V' == getGrammarFirstChar()) ;
	}
	
	/**
	  * Is this Lexicon entry invariable?
	  * 
	  * @return true if yes, false if not
	  */
	public boolean isInvariable() {
		return ('I' == getGrammarFirstChar()) ;
	}
	
	/**
	  * Is this Lexicon entry an adjective?
	  * 
	  * @return true if yes, false if not
	  */
	public boolean isAdjective()
	{
		if (null == _sGrammar)
			return false ;
		return (_sGrammar.equals("ADJ")) ;
	}
	
	/**
	  * Is this Lexicon entry an adverb?
	  * 
	  * @return true if yes, false if not
	  */
	public boolean isAdverb()
	{
		if (null == _sGrammar)
			return false ;
		return (_sGrammar.equals("ADV")) ;
	}
	
	/**
	  * Is this Lexicon entry of male gender?
	  * 
	  * @return true if yes, false if not
	  */
	public boolean isMaleGender()
	{
		if (null == _sGrammar)
			return false ;
		return (_sGrammar.equals("MS") || _sGrammar.equals("MP")) ;
	}
	
	/**
	  * Is this Lexicon entry of female gender?
	  * 
	  * @return true if yes, false if not
	  */
	public boolean isFemaleGender()
	{
		if (null == _sGrammar)
			return false ;
		return (_sGrammar.equals("FS") || _sGrammar.equals("FP")) ;
	}
	
	/**
	  * Is this Lexicon entry of neutral gender?
	  * 
	  * @return true if yes, false if not
	  */
	public boolean isNeutralGender()
	{
		if (null == _sGrammar)
			return false ;
		return (_sGrammar.equals("NS") || _sGrammar.equals("NP")) ;
	}
	
	/**
	  * Is this Lexicon entry of singular declination?
	  * 
	  * @return true if yes, false if not
	  */
	public boolean isSingular()
	{
		if (null == _sGrammar)
			return false ;
		return (_sGrammar.equals("MS") || _sGrammar.equals("FS") || _sGrammar.equals("NS")) ;
	}
	
	/**
	  * Is this Lexicon entry of plural declination?
	  * 
	  * @return true if yes, false if not
	  */
	public boolean isPlural()
	{
		if (null == _sGrammar)
			return false ;
		return (_sGrammar.equals("MP") || _sGrammar.equals("FP") || _sGrammar.equals("NP")) ;
	}
	
	/**
	  * Get first char of the Grammar information
	  * 
	  * @return A char if Ok, <code>'\0'</code> if not
	  */
	public char getGrammarFirstChar()
	{
		if ((null == _sGrammar) || _sGrammar.equals(""))
			return '\0' ;
		
		return (_sGrammar.charAt(0)) ;
	}
	
	/**
	  * Determine whether two Lexicon objects are exactly similar
	  * 
	  * @return true if all data are the same, false if not
	  * @param  lexicon Other Lexicon to compare to
	  * 
	  */
	public boolean equals(Lexicon lexicon)
	{
		if (this == lexicon) {
			return true ;
		}
		if (null == lexicon) {
			return false ;
		}
		
		return (_sLabel.equals(lexicon._sLabel) && 
				    _sCode.equals(lexicon._sCode) &&
				    _sGrammar.equals(lexicon._sGrammar) &&
				    _sFrequency.equals(lexicon._sFrequency)) ;
	}
  
	/**
	  * Determine whether an object is exactly similar to this Lexicon object
	  * 
	  * designed for ArrayList.contains(Obj) method
		* because by default, contains() uses equals(Obj) method of Obj class for comparison
	  * 
	  * @return true if all data are the same, false if not
	  * @param node LdvModelNode to compare to
	  * 
	  */
	public boolean equals(Object o) 
	{
		if (this == o) {
			return true ;
		}
		if (null == o || getClass() != o.getClass()) {
			return false;
		}

		final Lexicon lexicon = (Lexicon) o ;

		return (this.equals(lexicon)) ;
	}
}
