package com.ldv.shared.archetype;

import java.util.Iterator;
import java.util.Vector;

import com.google.gwt.user.client.rpc.IsSerializable;

public class LdvArchetypeReferences implements IsSerializable
{
  protected Vector<LdvArchetypeConcern> _aConcerns = new Vector<LdvArchetypeConcern>() ;
  protected Vector<LdvArchetypeHead>    _aHeads    = new Vector<LdvArchetypeHead>() ;
  
	/**
	 * Default constructor 
	 * 
	 **/
	public LdvArchetypeReferences() {
		init() ; 
	}
	
	/**
	 * Copy constructor 
	 * 
	 **/
	public LdvArchetypeReferences(final LdvArchetypeReferences rv) {
		initFromArchetypeReferences(rv) ; 
	}
				
	/**
	 * Reset all information
	 * 
	 **/
	public void init()
	{
		_aConcerns.clear() ;
		_aHeads.clear() ;
	}

	/**
	 * Sets all information by copying other node content   
	 * 
	 * @param other LdvArchetypeDialog to initialize from
	 * @return void
	 * 
	 **/
	public void initFromArchetypeReferences(final LdvArchetypeReferences other)
	{
		init() ;
		
		if (null == other)
			return ;
		
	  if ((null != other._aConcerns) || (false == other._aConcerns.isEmpty()))
	  	for (Iterator<LdvArchetypeConcern> itr = other._aConcerns.iterator() ; itr.hasNext() ; )
	  		_aConcerns.addElement(new LdvArchetypeConcern(itr.next())) ;
	  
	  if ((null != other._aHeads) || (false == other._aHeads.isEmpty()))
	  	for (Iterator<LdvArchetypeHead> itr = other._aHeads.iterator() ; itr.hasNext() ; )
	  		_aHeads.addElement(new LdvArchetypeHead(itr.next())) ;
	}

	/**
	  * Add a copy of a LdvArchetypeConcern to the list
	  * 
	  * @param concern LdvArchetypeConcern to store a copy of
	  */
	public void addCopyOfConcern(LdvArchetypeConcern concern) {
		_aConcerns.add(new LdvArchetypeConcern(concern)) ;
	}
	
	/**
	  * Add a LdvArchetypeConcern (and not a copy of it) to the list
	  * 
	  * @param concern LdvArchetypeConcern to store a copy of
	  */
	public void addConcern(LdvArchetypeConcern concern) {
		_aConcerns.add(concern) ;
	}
	
	/**
	  * Add a copy of a LdvArchetypeConcern to the list
	  * 
	  * @param concern LdvArchetypeConcern to store a copy of
	  */
	public void addCopyOfHead(LdvArchetypeHead head) {
		_aHeads.add(new LdvArchetypeHead(head)) ;
	}
	
	/**
	  * Add a LdvArchetypeConcern (and not a copy of it) to the list
	  * 
	  * @param concern LdvArchetypeConcern to store a copy of
	  */
	public void addHead(LdvArchetypeHead head) {
		_aHeads.add(head) ;
	}
	
	/**
	  * Get the first LdvArchetypeConcern from the list
	  * 
	  * @return a LdvArchetypeConcern is the list is not empty, <code>null</code> if it is
	  * 
	  */
	public LdvArchetypeConcern getFirstConcern() 
	{
		if ((null == _aConcerns) || _aConcerns.isEmpty())
			return (LdvArchetypeConcern) null ;
		
		Iterator<LdvArchetypeConcern> itr = _aConcerns.iterator() ;
		
  	return itr.next() ;
	}
	
	/**
	  * Get the LdvArchetypeConcern that is next to otherControl in the list
	  * 
	  * @return a LdvArchetypeConcern is it exists, <code>null</code> if not
	  */
	public LdvArchetypeConcern getNextControl(LdvArchetypeConcern otherConcern) 
	{
		if ((null == _aConcerns) || _aConcerns.isEmpty())
			return (LdvArchetypeConcern) null ;
		
		Iterator<LdvArchetypeConcern> itr = _aConcerns.iterator() ;
		for ( ; itr.hasNext() ; )
			if (otherConcern.equals(itr.next()))
				break ;
		
		if (itr.hasNext())
			return itr.next() ;
		
		return (LdvArchetypeConcern) null ;
	}
	
	/**
	  * Get the count of LdvArchetypeConcern in the list
	  * 
	  * @return vector size
	  */
  public int getNbConcern() {
  	return _aConcerns.size() ;
  }
	
  /**
	  * Get the first LdvArchetypeHead for a given language<br>
	  * <br>
	  * If a LdvArchetypeHead with this language exists, it is returned<br>
	  * If not, and a LdvArchetypeHead with not language specified exists, it is returned<br>
	  * 
	  * @return a LdvArchetypeHead if a specific or generic one is found, <code>null</code> if not
	  * 
	  */
	public LdvArchetypeHead getHead(String sLang) 
	{
		if ((null == _aHeads) || _aHeads.isEmpty())
			return (LdvArchetypeHead) null ;
		
		LdvArchetypeHead generic = (LdvArchetypeHead) null ; 
		
		for (Iterator<LdvArchetypeHead> itr = _aHeads.iterator() ; itr.hasNext() ; )
		{
			LdvArchetypeHead Head = itr.next() ;
			if (Head.getLang().equals(sLang))
				return Head ;
			
			if ("".equals(Head.getLang()) && (null == generic))
				generic = Head ;
		}
		
		return generic ;
	}
  
	/**
	  * Get the help URL for a given language<br>
	  * <br>
	  * Find a LdvArchetypeHead for this language and return its help URL if it exists<br>
	  * If this process fails, try to return a help url that is not language specific
	  * 
	  * @return a URL if a head is found, <code>""</code> if not
	  * 
	  */
	public String getHelpUrl(String sLang) 
	{
		LdvArchetypeHead Head = getHead(sLang) ;
		
		if (null == Head)
			Head = getHead("") ;
		
		if (null == Head)
			return "" ;
		
		return Head.getHelpUrl() ;
	}
	
	/**
	  * Determine whether two lexicon objects are exactly similar
	  * 
	  * @return true if all data are the same, false if not
	  * @param node LdvModelLexique to compare to
	  * 
	  */
	public boolean equals(LdvArchetypeReferences other)
	{
		if (this == other) {
			return true ;
		}
		if (null == other) {
			return false ;
		}
		
		return (containsSameConcerns(other._aConcerns) &&
						containsSameHeads(other._aHeads)) ;
	}

	/**
	  * Determine whether another vector of LdvArchetypeConcern contains the same objects<br>
	  * <br>
	  * Both vectors can be null or empty. If not, they must have the same size and
	  * every concern in this vector must be contained in the other vector 
	  * 
	  * @return true if all controls are the same, false if not
	  * @param otherVector Vector&lt;LdvArchetypeConcern&gt; to compare to
	  * 
	  */
	protected boolean containsSameConcerns(Vector<LdvArchetypeConcern> otherVector)
	{
		if ((null == _aConcerns) || _aConcerns.isEmpty())
		{
			if ((null == otherVector) || otherVector.isEmpty())
				return true ;
			return false ;
		}
		
		if ((null == otherVector) || otherVector.isEmpty())
			return false ;
	  	
		if (otherVector.size() != _aConcerns.size())
			return false ;
		
		for (Iterator<LdvArchetypeConcern> itr = _aConcerns.iterator() ; itr.hasNext() ; )
	  	if (false == otherVector.contains(itr.next()))
	  		return false ;
		
		return true ;
	}
	
	/**
	  * Determine whether another vector of LdvArchetypeHead contains the same objects<br>
	  * <br>
	  * Both vectors can be null or empty. If not, they must have the same size and
	  * every head in this vector must be contained in the other vector 
	  * 
	  * @return true if all controls are the same, false if not
	  * @param otherVector Vector&lt;LdvArchetypeHead&gt; to compare to
	  * 
	  */
	protected boolean containsSameHeads(Vector<LdvArchetypeHead> otherVector)
	{
		if ((null == _aHeads) || _aHeads.isEmpty())
		{
			if ((null == otherVector) || otherVector.isEmpty())
				return true ;
			return false ;
		}
		
		if ((null == otherVector) || otherVector.isEmpty())
			return false ;
	  	
		if (otherVector.size() != _aHeads.size())
			return false ;
		
		for (Iterator<LdvArchetypeHead> itr = _aHeads.iterator() ; itr.hasNext() ; )
	  	if (false == otherVector.contains(itr.next()))
	  		return false ;
		
		return true ;
	}

	/**
	  * Determine whether two lexicon objects are exactly similar
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

		final LdvArchetypeReferences dlg = (LdvArchetypeReferences) o ;

		return (this.equals(dlg)) ;
	}
}
