package com.ldv.shared.model;

import java.util.Iterator;
import java.util.Vector;

public class LdvNum
{
	protected Vector<LdvNumStorage> _aValues = new Vector<LdvNumStorage>() ;
	protected LdvDate               _RefDate = new LdvDate() ;

	public LdvNum()
	{
	}	
	
	/**
	 * Empty this object 
	 * 
	 **/
	public void reset() 
	{
		if (false == _aValues.isEmpty())
			_aValues.clear() ;
		_RefDate.reset() ;
	}

	/**
	 * Instantiate the Nth storage element 
	 * 
	 * @param sNum    information 
	 * @param sUnit   unit
	 * @param sFrmt   format
	 * @param iIndice place to store information
	 * 
	 **/
	public void instantiate(String sNum, String sUnit, String sFrmt, int iIndice)
	{
		LdvNumStorage storage = getStorageFromIndiceOrCreate(iIndice) ;
	  if (null == storage)
	  	return ;

	  storage.init() ;

	  storage.setUnit(sUnit) ;
	  storage.setFormat(sFrmt) ;
	  storage.setNumInformation(sNum) ;
	}

	/**
	 * Find the Nth storage element 
	 * 
	 * @param iIndice index of the storage element
	 * @return the Nth LdvNumStorage object if found, <code>null</code> if not
	 * 
	 **/
	LdvNumStorage getStorageFromIndice(int iIndice)
	{
		if (_aValues.isEmpty())
	    return null ;

	  int i = 0 ;
	  
	  Iterator<LdvNumStorage> storageIter = _aValues.iterator() ;
		while (storageIter.hasNext())
		{
			LdvNumStorage storage = storageIter.next() ;
			if (i == iIndice)
				return storage ;
		}

	  return null ;
	}
	
	/**
	 * Find the Nth storage element and, if not found, create a new one 
	 * 
	 * @param iIndice index of the storage element
	 * @return the Nth LdvNumStorage object if found or created, <code>null</code> if not
	 * 
	 **/
	LdvNumStorage getStorageFromIndiceOrCreate(int iIndice)
	{
		LdvNumStorage storage = getStorageFromIndice(iIndice) ;
	  if (null != storage)
	    return storage ;

	  storage = new LdvNumStorage() ;
	  
	  _aValues.add(storage) ;
	  
	  return _aValues.lastElement() ;
	}
	
	/**
	 * Return <code>true</code> if there is no exact information, no min information and no max information 
	 * 
	 * @param  iIndice index of the storage element to check for emptiness
	 * @return <code>true</code> if empty, <code>false</code> if not
	 * 
	 **/
	public boolean isEmpty(int iIndice)
	{
		LdvNumStorage storage = getStorageFromIndice(iIndice) ;
	  if (null == storage)
	    return true ;

		return storage.isEmpty() ;
	}
	
	/**
	*  Equivalent of = operator
	*  
	*  @param src Object to initialize from 
	**/
	public void initFromModel(LdvNum src)
	{
		reset() ;
		
		if (null == src)
			return ;
		
		setDate(src._RefDate) ;
		setValues(src._aValues) ;
	}
	
	/**
	 * Initialize the "normal" value 
	 * 
	 * @param  normal  object to initialize from
	 * @param  iIndice index of the storage element to initialize
	 * @return <code>true</code> if storage element was successfully found/created, <code>false</code> if not
	 * 
	 **/
	public boolean setNormal(LdvNum normal, int iIndice)
	{
		LdvNumStorage storage = getStorageFromIndiceOrCreate(iIndice) ;
	
		if (null == storage)
			return false ;
		
		storage.setNormalValue(normal) ;
		
    return true ;
  }
	
	/**
	 * Initialize the "lower normal" value 
	 * 
	 * @param  normal  object to initialize from
	 * @param  iIndice index of the storage element to initialize
	 * @return <code>true</code> if storage element was successfully found/created, <code>false</code> if not
	 * 
	 **/
	public boolean setLowerNormal(LdvNum normal, int iIndice)
	{
		LdvNumStorage storage = getStorageFromIndiceOrCreate(iIndice) ;
	
		if (null == storage)
			return false ;
		
		storage.setLowerNormalValue(normal) ;
		
    return true ;
  }
	
	/**
	 * Initialize the "upper normal" value 
	 * 
	 * @param  normal  object to initialize from
	 * @param  iIndice index of the storage element to initialize
	 * @return <code>true</code> if storage element was successfully found/created, <code>false</code> if not
	 * 
	 **/
	public boolean setUpperNormal(LdvNum normal, int iIndice)
	{
		LdvNumStorage storage = getStorageFromIndiceOrCreate(iIndice) ;
	
		if (null == storage)
			return false ;
		
		storage.setUpperNormalValue(normal) ;
		
    return true ;
  }

	public Vector<LdvNumStorage> getValues() {
		return _aValues ;
	}
	public void setValues(Vector<LdvNumStorage> src)
	{
		_aValues.clear() ;
		
		if ((null == src) || src.isEmpty())
	    return ;

	  Iterator<LdvNumStorage> storageIter = src.iterator() ;
		while (storageIter.hasNext())
		{
			LdvNumStorage storage = new LdvNumStorage(storageIter.next()) ;
			_aValues.add(storage) ;
		}
	}
	
	public boolean existValue(int iIndice)
	{
		LdvNumStorage firstStorage = getStorageFromIndice(iIndice) ;
		if (null == firstStorage)
			return false ;
		return firstStorage.isExact() ;
	}
	public boolean existValue() {
		return existValue(0) ;
	}
	
	public double getValue(int iIndice)
	{
		LdvNumStorage firstStorage = getStorageFromIndice(iIndice) ;
		if (null == firstStorage)
			return 0.0d ;
		return firstStorage.getValue() ;
	}
	public double getValue() {
		return getValue(0) ;
	}

	public LdvDate getDate() {
		return _RefDate ;
	}
	public void setDate(LdvDate RefDate)
	{
		_RefDate.initFromModel(RefDate) ;
	}
}
