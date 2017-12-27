package com.ldv.shared.archetype;

import java.util.Iterator;
import java.util.Vector;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.ldv.shared.util.MiscellanousFcts;

public class LdvArchetypeItem implements IsSerializable
{
	protected String _sCode ;
	protected String _sShiftLevel ;
	protected String _sText ;
	protected String _sArchetype ;
	
	protected Vector<LdvArchetypeItem>           _aSubItems    = new Vector<LdvArchetypeItem>() ;
	protected Vector<LdvArchetypeItemConstraint> _aConstraints = new Vector<LdvArchetypeItemConstraint>() ;
	
	/**
	 * Default constructor 
	 * 
	 **/
	public LdvArchetypeItem() {
		init() ; 
	}
	
	/**
	 * Standard constructor 
	 * 
	 **/
	public LdvArchetypeItem(String sCode, String sShiftLevel, String sArchetype)
	{
		init() ; 
		
		_sCode       = sCode ;
		_sShiftLevel = sShiftLevel ;
		_sArchetype  = sArchetype ;
	}
	
	/**
	 * Copy constructor 
	 * 
	 **/
	public LdvArchetypeItem(final LdvArchetypeItem rv) {
		initFromArchetypeItem(rv) ;
	}

	/**
	 * Reset all information
	 * 
	 **/
	public void init()
	{
		_sCode       = "" ;
		_sShiftLevel = "" ;
		_sText       = "" ;
		_sArchetype  = "" ;
		
		_aSubItems.clear() ;
		_aConstraints.clear() ;
	}

	/**
	 * Sets all information by copying other node content   
	 * 
	 * @param otherNode Model node
	 * @return void
	 * 
	 **/
	public void initFromArchetypeItem(final LdvArchetypeItem other)
	{
		init() ;
		
		if (null == other)
			return ;
		
		_sCode       = other._sCode ;
		_sShiftLevel = other._sShiftLevel ;
		_sText       = other._sText ;
		_sArchetype  = other._sArchetype ;
		
		if ((null != other._aSubItems) && (false == other._aSubItems.isEmpty()))
			for (Iterator<LdvArchetypeItem> itr = other._aSubItems.iterator() ; itr.hasNext() ; )
				_aSubItems.addElement(new LdvArchetypeItem(itr.next())) ;
		
		if ((null != other._aConstraints) && (false == other._aConstraints.isEmpty()))
			for (Iterator<LdvArchetypeItemConstraint> itr = other._aConstraints.iterator() ; itr.hasNext() ; )
				_aConstraints.addElement(new LdvArchetypeItemConstraint(itr.next())) ;
	}
	
	public String getCode() {
		return _sCode ;
	}
	public void setCode(String sCode) {
		_sCode = sCode ;
	}

	public String getShiftLevel() {
		return _sShiftLevel ;
	}
	public void setShiftLevel(String sShiftLevel) {
		_sShiftLevel = sShiftLevel ;
	}

	public String getText() {
		return _sText ;
	}
	public void setText(String sText) {
		_sText = sText ;
	}

	public String getArchetype() {
		return _sArchetype ;
	}
	public void setArchetype(String sArchetype) {
		_sArchetype = sArchetype ;
	}

	public Vector<LdvArchetypeItem> getSubItems() {
		return _aSubItems ;
	}
	
	public Vector<LdvArchetypeItemConstraint> getConstraints() {
		return _aConstraints ;
	}
	
	public boolean isLeaf() {
		return _aSubItems.isEmpty() ;
	}

	/**
	  * Get the first LdvArchetypeItem from the list
	  * 
	  * @return a LdvArchetypeItem is the list is not empty, <code>null</code> if it is
	  * 
	  */
	public LdvArchetypeItem getFirstItem() 
	{
		if ((null == _aSubItems) || _aSubItems.isEmpty())
			return (LdvArchetypeItem) null ;
		
		Iterator<LdvArchetypeItem> itr = _aSubItems.iterator() ;
		
		return itr.next() ;
	}
	
	/**
	  * Get the LdvArchetypeItem that is next to otherItem in the list
	  * 
	  * @return a LdvArchetypeItem is it exists, <code>null</code> if not
	  */
	public LdvArchetypeItem getNextItem(LdvArchetypeItem otherItem) 
	{
		if ((null == _aSubItems) || _aSubItems.isEmpty())
			return (LdvArchetypeItem) null ;
		
		Iterator<LdvArchetypeItem> itr = _aSubItems.iterator() ;
		for ( ; itr.hasNext() ; )
			if (otherItem.equals(itr.next()))
				break ;
		
		if (itr.hasNext())
			return itr.next() ;
		
		return (LdvArchetypeItem) null ;
	}
	
	/**
	  * Get the count of LdvArchetypeItem in the list
	  * 
	  * @return vector size
	  */
	public int getItemsCount() {
		return _aSubItems.size() ;
	}
	
	/**
	  * Get the first LdvArchetypeItemConstraint from the list
	  * 
	  * @return a LdvArchetypeItemConstraint is the list is not empty, <code>null</code> if it is
	  * 
	  */
	public LdvArchetypeItemConstraint getFirstConstraint() 
	{
		if ((null == _aConstraints) || _aConstraints.isEmpty())
			return (LdvArchetypeItemConstraint) null ;
		
		Iterator<LdvArchetypeItemConstraint> itr = _aConstraints.iterator() ;
		
		return itr.next() ;
	}
	
	/**
	  * Get the LdvArchetypeItemConstraint that is next to otherConstraint in the list
	  * 
	  * @return a LdvArchetypeItemConstraint is it exists, <code>null</code> if not
	  */
	public LdvArchetypeItemConstraint getNextItem(LdvArchetypeItemConstraint otherConstraint) 
	{
		if ((null == _aConstraints) || _aConstraints.isEmpty())
			return (LdvArchetypeItemConstraint) null ;
		
		Iterator<LdvArchetypeItemConstraint> itr = _aConstraints.iterator() ;
		for ( ; itr.hasNext() ; )
			if (otherConstraint.equals(itr.next()))
				break ;
		
		if (itr.hasNext())
			return itr.next() ;
		
		return (LdvArchetypeItemConstraint) null ;
	}
	
	/**
	  * Get the count of LdvArchetypeItemConstraint in the list
	  * 
	  * @return vector size
	  */
	public int getConstraintsCount() {
		return _aConstraints.size() ;
	}
	
	/**
	  * Determine whether two LdvArchetypeItem objects are exactly similar
	  * 
	  * @return true if all data are the same, false if not
	  * @param other LdvArchetypeItem to compare to
	  * 
	  */
	public boolean equals(LdvArchetypeItem other)
	{
		if (this == other) {
			return true ;
		}
		if (null == other) {
			return false ;
		}
		
		return (MiscellanousFcts.areIdenticalStrings(_sCode,       other._sCode)       &&
						MiscellanousFcts.areIdenticalStrings(_sShiftLevel, other._sShiftLevel) &&
						MiscellanousFcts.areIdenticalStrings(_sText,       other._sText)       &&
						MiscellanousFcts.areIdenticalStrings(_sArchetype,  other._sArchetype)  &&
						containsSameItems(other._aSubItems) &&
						containsSameConstraints(other._aConstraints)) ;
	}

	/**
	  * Determine whether another vector of LdvArchetypeItem contains the same items<br>
	  * <br>
	  * Both vectors can be null or empty. If not, they must have the same size and
	  * every control in _aSubItems must be contained in the other vector 
	  * 
	  * @return <code>true</code> if all items are the same, <code>false</code> if not
	  * @param otherVector Vector&lt;LdvArchetypeItem&gt; to compare to
	  * 
	  */
	protected boolean containsSameItems(Vector<LdvArchetypeItem> otherVector)
	{
		if ((null == _aSubItems) || _aSubItems.isEmpty())
		{
			if ((null == otherVector) || otherVector.isEmpty())
				return true ;
			return false ;
		}
		
		if ((null == otherVector) || otherVector.isEmpty())
			return false ;
	  	
		if (otherVector.size() != _aSubItems.size())
			return false ;
		
		for (Iterator<LdvArchetypeItem> itr = _aSubItems.iterator() ; itr.hasNext() ; )
	  	if (false == otherVector.contains(itr.next()))
	  		return false ;
		
		return true ;
	}
	
	/**
	  * Determine whether another vector of LdvArchetypeItemConstraint contains the same constraints<br>
	  * <br>
	  * Both vectors can be null or empty. If not, they must have the same size and
	  * every control in _aConstraints must be contained in the other vector 
	  * 
	  * @return <code>true</code> if all constraints are the same, <code>false</code> if not
	  * @param otherVector Vector&lt;LdvArchetypeItemConstraint&gt; to compare to
	  * 
	  */
	protected boolean containsSameConstraints(Vector<LdvArchetypeItemConstraint> otherVector)
	{
		if ((null == _aConstraints) || _aConstraints.isEmpty())
		{
			if ((null == otherVector) || otherVector.isEmpty())
				return true ;
			return false ;
		}
		
		if ((null == otherVector) || otherVector.isEmpty())
			return false ;
	  	
		if (otherVector.size() != _aConstraints.size())
			return false ;
		
		for (Iterator<LdvArchetypeItemConstraint> itr = _aConstraints.iterator() ; itr.hasNext() ; )
	  	if (false == otherVector.contains(itr.next()))
	  		return false ;
		
		return true ;
	}
	
	/**
	  * Determine whether two LdvArchetypeItem objects are exactly similar
	  * 
	  * designed for ArrayList.contains(Obj) method
		* because by default, contains() uses equals(Obj) method of Obj class for comparison
	  * 
	  * @return true if all data are the same, false if not
	  * @param o Object to compare to
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

		final LdvArchetypeItem node = (LdvArchetypeItem) o ;

		return (this.equals(node)) ;
	}
}
