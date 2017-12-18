package com.ldv.shared.filsguides;

import java.util.Iterator;
import java.util.Vector;

/**
 *  Set of data that contains a document label information
 * 
 **/
public class LdvStringVectorVector extends Vector<LdvStringVector>
{
	private static final long serialVersionUID = 3937020495772371642L;

	public LdvStringVectorVector() 
	{
	}

	/**
	 * Copy constructor   
	 * 
	 * @param other LdvStringVector to initialize from
	 * 
	 **/
	public LdvStringVectorVector(final LdvStringVectorVector other)
	{
		clear() ;
		
		if ((null == other) || (other.isEmpty()))
	  	return ;
	  
	  for (Iterator<LdvStringVector> itr = other.iterator() ; itr.hasNext() ; )
	  	addElement(new LdvStringVector(itr.next())) ;
	}
	
  /**
	 * Sets all information by copying other LdvStringVectorVector content   
	 * 
	 * @param other LdvStringVectorVector to initialize from
	 * @return void
	 * 
	 **/
	public void initFromStringVectorVector(final LdvStringVectorVector other)
	{
		clear() ;
		
		if ((null == other) || (other.isEmpty()))
	  	return ;
	  
	  for (Iterator<LdvStringVector> itr = other.iterator() ; itr.hasNext() ; )
	  	addElement(new LdvStringVector(itr.next())) ;
	}
}
