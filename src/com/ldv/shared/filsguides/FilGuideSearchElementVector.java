package com.ldv.shared.filsguides;

import java.util.Iterator;
import java.util.Vector;

import com.ldv.shared.util.MiscellanousFcts;

/**
 *  Set of data that contains a document label information
 * 
 **/
public class FilGuideSearchElementVector extends Vector<FilGuideSearchElement>
{
	private static final long serialVersionUID = 66821500581313249L;

	public FilGuideSearchElementVector() 
	{
	}

	/**
	 * Copy constructor   
	 * 
	 * @param other LdvStringVector to initialize from
	 * 
	 **/
	public FilGuideSearchElementVector(final FilGuideSearchElementVector other)
	{
		clear() ;
		
		if ((null == other) || (other.isEmpty()))
	  	return ;
	  
	  for (Iterator<FilGuideSearchElement> itr = other.iterator() ; itr.hasNext() ; )
	  	addElement(new FilGuideSearchElement(itr.next())) ;
	}
	
	/**
	 * Add a new FilGuideSearchElement to the vector   
	 * 
	 **/
	public void AddSearchElement(final FilGuideSearchElement sStr) {
		this.addElement(new FilGuideSearchElement(sStr)) ;
	}
	
	/**
	 * Add a new label to be found into the vector. If this label is either a free text or a
	 * final concept (say num value or date, etc) the label is marked as found
	 * 
	 * @param sLabel Label to add to the search vector
	 * 
	 **/
	public void AddLabel(String sLabel)
	{
		boolean bFound = false ;
		
		if (sLabel.contains("�"))
			bFound = true ;
		
		this.addElement(new FilGuideSearchElement(sLabel, bFound)) ;
	}
	
	/**
	 * Is there a FilGuideSearchElement that still remains to be found in the vector?     
	 * 
	 **/
	public boolean AreRemainingUnfoundElements()
	{
		if (isEmpty())
			return false ;
		
		for (Iterator<FilGuideSearchElement> itr = iterator() ; itr.hasNext() ; )
			if (false == itr.next().wasFound())
				return true ;
		
		return false ;
	}
	
	/**
	 * Is this String a in FilGuideSearchElement that still remains to be found?     
	 * 
	 * @param iLevel depth to look for inside the sorted equivalents vector
	 * @param sItem  String to look for
	 * 
	 **/
  public boolean IsItemInVector(int iLevel, final String sItem)
  {
  	if (isEmpty())
			return false ;
  	
  	for (Iterator<FilGuideSearchElement> itr = iterator() ; itr.hasNext() ; )
  	{
  		FilGuideSearchElement elmt = itr.next() ;
  		if (false == elmt.wasFound())
  		{
  			Iterator<LdvStringVector> jtr = elmt.getSortedEquivalents().iterator() ;
  			for (int iCur = 1 ; (iCur < iLevel) && jtr.hasNext() ; )
  				if (jtr.next().IsItemInVector(sItem))
          	return true ;
  		}
  	}
  	
  	return false ;
  }
  
  /**
	 * Find the lowest String at a given depth inside still to be found sorted equivalent vectors     
	 * 
	 * @param iLevel depth to look for inside the sorted equivalents vector
	 * 
	 **/
  public String MinElementForLevel(int iLevel)
  {
  	if (isEmpty())
			return "" ;
  	
  	String sMinElement = "" ;
  	
  	for (Iterator<FilGuideSearchElement> itr = iterator() ; itr.hasNext() ; )
  	{
  		FilGuideSearchElement elmt = itr.next() ;
  		if (false == elmt.wasFound())
  		{
  			Iterator<LdvStringVector> jtr = elmt.getSortedEquivalents().iterator() ;
  			
  			LdvStringVector strVector = jtr.next() ;
  			
  			int iCur = 1 ;
  			for ( ; (iCur < iLevel) && jtr.hasNext() ; )
  				strVector = jtr.next() ;
  				
  			if ((iCur == iLevel) && (false == strVector.isEmpty()))
  			{
  				String sLabel = strVector.iterator().next() ;
  				if ("".equals(sMinElement) || (sLabel.compareTo(sMinElement) > 0))
  					sMinElement = sLabel ;
  			}
  		}
  	}
  	
  	return sMinElement ;
  }
  
  public String MinElement()
  {
  	if (isEmpty())
			return "" ;
  	
  	String sMinElement = "" ;
  	
/*
  	for (Iterator<FilGuideSearchElement> itr = iterator() ; itr.hasNext() ; )
  	{
  		FilGuideSearchElement elmt = itr.next() ;
  		if (false == elmt.wasFound())
  		{
  			Iterator<LdvStringVector> jtr = elmt.getSortedEquivalents().iterator() ;
  			
  			LdvStringVector strVector = jtr.next() ;
  	
  			String sLocalPath = strVector.iterator().next() ;
  			
  			for ( ; jtr.hasNext() ; )
  			{
  				strVector  = jtr.next() ;
  				sLocalPath += strVector.iterator().next() ;
  			}
  				
  			if ((iCur == iLevel) && (false == strVector.isEmpty()))
  			{
  				String sLabel = strVector.iterator().next() ;
  				if ("".equals(sMinElement) || (sLabel.compareTo(sMinElement) > 0))
  					sMinElement = sLabel ;
  			}
  		}
  	}
*/
  	
  	return sMinElement ;
  }
  
/*
  string ItemStrictementSuperieur(int Rang, string sItem);
  string ItemStrictementInferieur(int Rang, string sItem);
  
  string ChaineStrictementSuperieur(VecteurString* pVectItemCible, string sDernierItemPere);
  void AjouteEtiquette(string sEtiquette);
*/
  
  /**
	 * Fill the Fil Guide information found for a label    
	 * 
	 * @param sLabel   label which information we want to get
	 * @param ItemData structure that will be filled with Fil Guide information
	 * 
	 * @return <code>true</code> if a Fil Guide was found for this label, <code>false</code> if not
	 * 
	 **/
  public boolean SetData(String sLabel, BBItemData ItemData)
  {
  	if (this.isEmpty() || (null == sLabel))
  		return false ;
  	
  	for (Iterator<FilGuideSearchElement> itr = iterator() ; itr.hasNext() ; )
  	{
  		FilGuideSearchElement elmt = itr.next() ;
  		
  		if (MiscellanousFcts.areIdenticalStrings(sLabel, elmt.getLabel()))
  		{
  			if (null != ItemData)
  				ItemData.initFromModel(elmt.getData()) ;
  			
  			return elmt.wasFound() ;
  		}
  	}
  	
  	return false ;
  }
	  
  /**
	 * Sets all information by copying other LdvStringVector content   
	 * 
	 * @param other LdvStringVector to initialize from
	 * @return void
	 * 
	 **/
	public void initFromSearchElementVector(final FilGuideSearchElementVector other)
	{
		clear() ;
		
		if ((null == other) || (other.isEmpty()))
	  	return ;
	  
	  for (Iterator<FilGuideSearchElement> itr = other.iterator() ; itr.hasNext() ; )
	  	addElement(new FilGuideSearchElement(itr.next())) ;
	}
}
