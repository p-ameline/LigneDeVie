package com.ldv.shared.filsguides;

import java.util.Iterator;
import java.util.Vector;

/**
 *  Set of data that contains a document label information
 * 
 **/
public class LdvStringVector extends Vector<String>
{
	private static final long serialVersionUID = 1665543135780090678L;
	
	public LdvStringVector() 
	{
	}

	/**
	 * Copy constructor   
	 * 
	 * @param other LdvStringVector to initialize from
	 * 
	 **/
	public LdvStringVector(final LdvStringVector other)
	{
		clear() ;
		
		if ((null == other) || (other.isEmpty()))
	  	return ;
	  
	  for (Iterator<String> itr = other.iterator() ; itr.hasNext() ; )
	  	addElement(new String(itr.next())) ;
	}
	
	/**
	 * Add a new String to the vector, it is ok to have the same String being present multiple times   
	 * 
	 **/
	public void AddString(final String sStr) {
		this.addElement(new String(sStr)) ;
	}
	
	/**
	 * Remove all similar Strings found in the vector   
	 * 
	 **/
  public void RemoveString(final String sStr)
  {
  	if (this.isEmpty())
  		return ;
  	
  	for (Iterator<String> itr = iterator() ; itr.hasNext() ; )
  		if (sStr.equals(itr.next()))
  			itr.remove() ;
  }
  
  /**
	 * Is this String inside the vector   
	 * 
	 **/
  public boolean IsItemInVector(final String sItem) {
  	return this.contains(sItem) ;
  }
  
  /**
	 * Returns the greatest item in vector that is inferior to an item   
	 * 
	 **/
  public String GetStrictlyInferiorItem(final String sItem)
  {
  	if (this.isEmpty())
  		return "" ;
  	
  	String sCandidate = "" ;
  	
  	for (Iterator<String> itr = iterator() ; itr.hasNext() ; )
  	{
  		String sStr = itr.next() ;
  		if ((sStr.compareTo(sItem) < 0) && (sStr.compareTo(sCandidate) > 0))
  			sCandidate = sStr ;
  	}
  	
  	return sCandidate ;
  }
  
  /**
	 * Returns the lowest item in vector that is superior to an item   
	 * 
	 **/
  public String GetStrictlySuperiorItem(final String sItem)
  {
  	if (this.isEmpty())
  		return "" ;
  	
  	String sCandidate = "" ;
  	
  	for (Iterator<String> itr = iterator() ; itr.hasNext() ; )
  	{
  		String sStr = itr.next() ;
  		if ((sStr.compareTo(sItem) > 0) && ("".equals(sCandidate) || (sStr.compareTo(sCandidate) < 0)))
  			sCandidate = sStr ;
  	}
  	
  	return sCandidate ;
  }

  /**
	 * Return the count of items that exist at the start of both vectors   
	 * 
	 **/
  public int GetCommonStartingItemsCount(final LdvStringVector otherVector)
  {
  	int iCount = 0 ;
  	
  	Iterator<String> itr = iterator() ;
  	Iterator<String> jtr = otherVector.iterator() ;
  	
  	for ( ; itr.hasNext() && jtr.hasNext() ; )
  		if (itr.next().equals(jtr.next()))
  			iCount++ ;
  		else
  			return iCount ;
  			
  	return iCount ;
  }
  
  /**
	 * Sets all information by copying other LdvStringVector content   
	 * 
	 * @param other LdvStringVector to initialize from
	 * @return void
	 * 
	 **/
	public void initFromStringVector(final LdvStringVector other)
	{
		clear() ;
		
		if ((null == other) || (other.isEmpty()))
	  	return ;
	  
	  for (Iterator<String> itr = other.iterator() ; itr.hasNext() ; )
	  	addElement(new String(itr.next())) ;
	}
}
