package com.ldv.shared.archetype;

import java.util.Iterator;
import java.util.Vector;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.ldv.shared.util.MiscellanousFcts;

public class LdvArchetype implements IsSerializable
{
	protected String                 _sIdentifier ;
	
	protected LdvArchetypeReferences _References = new LdvArchetypeReferences() ;
	protected LdvArchetypeNextDialog _NextDialog = new LdvArchetypeNextDialog() ;
	protected String                 _sPresentation ;
	protected String                 _sFunction ;

	protected Vector<LdvArchetypeDialogBox> _aDialogs = new Vector<LdvArchetypeDialogBox>() ;
	protected Vector<LdvArchetypeItem>      _aItems   = new Vector<LdvArchetypeItem>() ;
	
	/**
	 * Default constructor 
	 * 
	 **/
	public LdvArchetype() {
		init() ;
	}

	/**
	 * Copy constructor  
	 * 
	 * @param sourceNode Model node
	 * 
	 **/
	public LdvArchetype(LdvArchetype sourceNode) {
		initFromArchetype(sourceNode) ;
	}
	
	/**
	 * Reset all information
	 * 
	 **/
	public void init()
	{
		_References.init() ;
		_NextDialog.init() ;
		
		_sIdentifier   = "" ;
		_sPresentation = "" ;
		_sFunction     = "" ;
		
		_aItems.clear() ;
		_aDialogs.clear() ;
	}

	public String getIdentifier() {
		return _sIdentifier ;
	}
	
	public LdvArchetypeReferences getReferences() {
		return _References ;
	}
	
	public LdvArchetypeNextDialog getNextDialog() {
		return _NextDialog ;
	}
	
	public LdvArchetypeHead getHead(String sLang)
	{
		if (null == _References)
			return (LdvArchetypeHead) null ;
		
		return getHead(sLang) ;
	}

	public String getTitle(String sLang)
	{
		LdvArchetypeHead Head = getHead(sLang) ;
		
		if (null == Head)
			return "" ;
		
		return Head.getTitle() ;
	}
	
	public LdvArchetypeDialogBox getDialogBox(String sLang)
	{
		if ((null == _aDialogs) || _aDialogs.isEmpty())
			return (LdvArchetypeDialogBox) null ;
		
		LdvArchetypeDialogBox generic = (LdvArchetypeDialogBox) null ; 
		
		for (Iterator<LdvArchetypeDialogBox> itr = _aDialogs.iterator() ; itr.hasNext() ; )
		{
			LdvArchetypeDialogBox DialogBox = itr.next() ;
			if (DialogBox.getLang().equals(sLang))
				return DialogBox ;
			
			if ("".equals(DialogBox.getLang()) && (null == generic))
				generic = DialogBox ;
		}
		
		return generic ;
	}
	
	public Vector<LdvArchetypeItem> getRootItem() {
		return _aItems ;
	}
	
	/**
	 * Sets all information by copying other LdvArchetypeControl content   
	 * 
	 * @param other Model LdvArchetypeControl
	 * @return void
	 * 
	 **/
	public void initFromArchetype(final LdvArchetype other)
	{
		init() ;
		
		if (null == other)
			return ;
		
		_References.initFromArchetypeReferences(other._References) ;
		_NextDialog.initFromArchetypeNextDialog(other._NextDialog) ;
		
		_sIdentifier   = other._sIdentifier ;
		_sPresentation = other._sPresentation ;
		_sFunction     = other._sFunction ;
		
		if ((null != other._aItems) && (false == other._aItems.isEmpty()))
			for (Iterator<LdvArchetypeItem> itr = other._aItems.iterator() ; itr.hasNext() ; )
				_aItems.addElement(new LdvArchetypeItem(itr.next())) ;
		
		if ((null != other._aDialogs) && (false == other._aDialogs.isEmpty()))
			for (Iterator<LdvArchetypeDialogBox> itr = other._aDialogs.iterator() ; itr.hasNext() ; )
				_aDialogs.addElement(new LdvArchetypeDialogBox(itr.next())) ;
	}
	
	/**
	  * Determine whether another vector of LdvArchetypeControl contains the same controls<br>
	  * <br>
	  * Both vectors can be null or empty. If not, they must have the same size and
	  * every control in _aControls must be contained in the other vector 
	  * 
	  * @return true if all controls are the same, false if not
	  * @param otherVector Vector<LdvArchetypeControl> to compare to
	  * 
	  */
	protected boolean containsSameItems(Vector<LdvArchetypeItem> otherBoxes)
	{
		if ((null == _aItems) || _aItems.isEmpty())
		{
			if ((null == otherBoxes) || otherBoxes.isEmpty())
				return true ;
			return false ;
		}
		
		if ((null == otherBoxes) || otherBoxes.isEmpty())
			return false ;
	  	
		if (otherBoxes.size() != _aItems.size())
			return false ;
		
		for (Iterator<LdvArchetypeItem> itr = _aItems.iterator() ; itr.hasNext() ; )
	  	if (false == otherBoxes.contains(itr.next()))
	  		return false ;
		
		return true ;
	}
	
	/**
	  * Determine whether another vector of LdvArchetypeControl contains the same controls<br>
	  * <br>
	  * Both vectors can be null or empty. If not, they must have the same size and
	  * every control in _aControls must be contained in the other vector 
	  * 
	  * @return true if all controls are the same, false if not
	  * @param otherVector Vector<LdvArchetypeControl> to compare to
	  * 
	  */
	protected boolean containsSameDialogBoxes(Vector<LdvArchetypeDialogBox> otherBoxes)
	{
		if ((null == _aDialogs) || _aDialogs.isEmpty())
		{
			if ((null == otherBoxes) || otherBoxes.isEmpty())
				return true ;
			return false ;
		}
		
		if ((null == otherBoxes) || otherBoxes.isEmpty())
			return false ;
	  	
		if (otherBoxes.size() != _aDialogs.size())
			return false ;
		
		for (Iterator<LdvArchetypeDialogBox> itr = _aDialogs.iterator() ; itr.hasNext() ; )
	  	if (false == otherBoxes.contains(itr.next()))
	  		return false ;
		
		return true ;
	}
	
	/**
	  * Determine whether two LdvArchetype objects are exactly similar
	  * 
	  * @return true if all data are the same, false if not
	  * @param archetype LdvArchetype to compare to
	  * 
	  */
	public boolean equals(LdvArchetype archetype)
	{
		if (this == archetype) {
			return true ;
		}
		if (null == archetype) {
			return false ;
		}
		
		return (_References.equals(archetype._References) &&
						_NextDialog.equals(archetype._NextDialog) &&
						MiscellanousFcts.areIdenticalStrings(_sIdentifier, archetype._sIdentifier) &&
						MiscellanousFcts.areIdenticalStrings(_sPresentation, archetype._sPresentation) &&
						MiscellanousFcts.areIdenticalStrings(_sFunction, archetype._sFunction) &&
						containsSameItems(archetype._aItems) &&
						containsSameDialogBoxes(archetype._aDialogs)) ;
	}
   
	/**
	  * Determine whether two LdvArchetype objects are exactly similar
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

		final LdvArchetype archetype = (LdvArchetype) o ;

		return (this.equals(archetype)) ;
	}
}
