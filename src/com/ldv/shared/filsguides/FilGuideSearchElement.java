package com.ldv.shared.filsguides;

import java.util.Iterator;
import java.util.Vector;

import com.google.gwt.user.client.rpc.IsSerializable;

import com.ldv.shared.util.MiscellanousFcts;

// 
//
public class FilGuideSearchElement implements IsSerializable
{
	protected String                _sLabel ;
	protected boolean               _bFound ;
	
	protected BBItemData            _data                = new BBItemData() ;
	protected Vector<BBItemData>    _dataVector          = new Vector<BBItemData>() ; 
	
	protected LdvStringVectorVector _SortedEquivalents   = new LdvStringVectorVector() ;
	protected LdvStringVectorVector _UnsortedEquivalents = new LdvStringVectorVector() ;
	
	/**
	 * Default constructor 
	 * 
	 **/
	public FilGuideSearchElement() {
		init() ;
	}

	/**
	 * Usual constructor 
	 * 
	 **/
	public FilGuideSearchElement(final String sLabel, final boolean bFound)
	{
		init() ;
		
		_sLabel = sLabel ;
		_bFound = bFound ;
	}
	
	/**
	 * Copy constructor  
	 * 
	 * @param sourceNode Model node
	 * 
	 **/
	public FilGuideSearchElement(FilGuideSearchElement sourceNode) {
		initFromSearchElement(sourceNode) ;
	}
	
	/**
	 * Reset all information
	 * 
	 **/
	public void init()
	{
		_sLabel = "" ;
		_bFound = false ;
		
		_data.init() ;
		_dataVector.clear() ;
		
		_SortedEquivalents.clear() ;
		_UnsortedEquivalents.clear() ;
	}
	
	/**
	 * Sets all information by copying other FilGuideSearchElement content   
	 * 
	 * @param other Model FilGuideSearchElement
	 * @return void
	 * 
	 **/
	public void initFromSearchElement(final FilGuideSearchElement other)
	{
		init() ;
		
		if (null == other)
			return ;
		
		_sLabel = other._sLabel ;
		_bFound = other._bFound ;
		
		_data.initFromModel(other._data) ;
		
		if ((null != other._dataVector) || (false == other._dataVector.isEmpty()))
			for (Iterator<BBItemData> itr = other._dataVector.iterator() ; itr.hasNext() ; )
				_dataVector.addElement(new BBItemData(itr.next())) ;
		
		_SortedEquivalents.initFromStringVectorVector(other._SortedEquivalents) ;
		_UnsortedEquivalents.initFromStringVectorVector(other._UnsortedEquivalents) ;
	}
		
	public String getLabel() {
		return _sLabel ;
	}
	
	public BBItemData getData() {
		return _data ;
	}
	
	public boolean wasFound() {
		return _bFound ; 
	}
	
	public LdvStringVectorVector getSortedEquivalents() {
		return _SortedEquivalents ;
	}
	
	/**
	  * Determine whether two lexicon objects are exactly similar
	  * 
	  * @return true if all data are the same, false if not
	  * @param node LdvModelLexique to compare to
	  * 
	  */
	public boolean equals(FilGuideSearchElement other)
	{
		if (this == other) {
			return true ;
		}
		if (null == other) {
			return false ;
		}
		
		return (MiscellanousFcts.areIdenticalStrings(_sLabel, other._sLabel)) ;
	}
   
	/**
	  * Determine whether two FilGuideSearchElement objects are exactly similar
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

		final FilGuideSearchElement node = (FilGuideSearchElement) o ;

		return (this.equals(node)) ;
	}
}
