package com.ldv.shared.calendar;

import java.util.ArrayList;
import java.util.Iterator;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Defines a list of numbers.
 */
public class NumberList extends ArrayList<Integer> implements IsSerializable 
{
	private static final long serialVersionUID = 6986077591829479905L;
	
	private int     _iMinValue ;
	private int     _iMaxValue ;
	private boolean _bAllowsNegativeValues ;
    
	/**
	 * Default constructor.
	 */
	public NumberList() {
		this(Integer.MIN_VALUE, Integer.MAX_VALUE, true) ;
	}

	/**
	 * Constructor with limits.
	 * @param minValue the minimum allowable value
	 * @param maxValue the maximum allowable value
	 * @param allowsNegativeValues indicates whether negative values are allowed
	 */
	public NumberList(int minValue, int maxValue, boolean allowsNegativeValues)
	{
		_iMinValue = minValue ;
		_iMaxValue = maxValue ;
		_bAllowsNegativeValues = allowsNegativeValues ;
	}

	/**
	 * Constructor.
	 * @param aString a string representation of a number list
	 */
	public NumberList(final String aString) {
		this(aString, Integer.MIN_VALUE, Integer.MAX_VALUE, true) ;
	}
    
	/**
	 * @param aString a string representation of a number list separated by ','
	 * @param minValue the minimum allowable value
	 * @param maxValue the maximum allowable value
	 * @param allowsNegativeValues indicates whether negative values are allowed
	 */
	public NumberList(final String aString, int iMinValue, int iMaxValue, boolean allowsNegativeValues)
	{
		this(iMinValue, iMaxValue, allowsNegativeValues) ;
		
		if ((null == aString) || "".equals(aString))
			return ;
		
		String[] parsedStrings = aString.split(",") ;
		for (int i = 0 ; i < parsedStrings.length ; i++)
		{
			try {
				final int value = Integer.parseInt(parsedStrings[i]) ;
				add(value) ;
			} catch(NumberFormatException e) {
			}
		}
	}

	/**
	 * Copy constructor.
	 */
	public NumberList(final NumberList model)
	{
		this(Integer.MIN_VALUE, Integer.MAX_VALUE, true) ;
		
		if (null == model)
			return ;
		
		InitializeFromNumberList(model) ;
	}
	
	/**
	 * Initialize this object as a clone of a model object 
	 */
	public void InitializeFromNumberList(final NumberList model)
	{
		reset() ;
		
		if (null == model)
			return ;
		
		InitializeFromData((ArrayList<Integer>) model, model._iMinValue, model._iMaxValue, model._bAllowsNegativeValues) ;
	}
		
	/**
	 * Initialize this object from a set of information 
	 */
	public void InitializeFromData(final ArrayList<Integer> aIntegers, int iMinValue, int iMaxValue, boolean allowsNegativeValues)
	{
		reset() ;
		
		_iMinValue = iMinValue ;
		_iMaxValue = iMaxValue ;
		_bAllowsNegativeValues = allowsNegativeValues ;
		
		fill(aIntegers) ;
	}
	
	/**
	 * Fill this object from an array of Integer 
	 */
	public void fill(final ArrayList<Integer> aIntegers)
	{
		clear() ;
		
		if ((null == aIntegers) || aIntegers.isEmpty())
			return ;
		
		for (Iterator<Integer> it = aIntegers.iterator() ; it.hasNext() ; )
			add(it.next()) ;
	}
	
	/**
	 * Initialize this object with default values 
	 */
	protected void reset()
	{
		_iMinValue = Integer.MIN_VALUE ;
		_iMaxValue = Integer.MAX_VALUE ;
		_bAllowsNegativeValues = true ;
		
		clear() ;
	}
	
	/**
	 * @param aNumber a number to add to the list
	 * @return true if the number was added, otherwise false
	 */
	public final boolean add(final Integer iNumber)
	{
		int abs = iNumber ;
		if ((abs >> 31 | -abs >>> 31) < 0)
		{
			if (false == _bAllowsNegativeValues)
				throw new IllegalArgumentException("Negative value not allowed: " + iNumber) ;
            
			abs = Math.abs(abs) ;
		}
		if ((abs < _iMinValue) || (abs > _iMaxValue))
			throw new IllegalArgumentException("Value not in range [" + _iMinValue + ".." + _iMaxValue + "]: " + iNumber) ;
    	
		// Don't add it twice
		if (contains(iNumber))
			return true ;
		
		return super.add(iNumber) ;
	}

	/**
	 * {@inheritDoc}
	 */
	public final String toString()
	{
		if (isEmpty())
			return "" ;
		
		final StringBuilder b = new StringBuilder();
		for (final Iterator<Integer> i = iterator() ; i.hasNext() ; )
		{
			b.append(i.next()) ;
			if (i.hasNext()) 
				b.append(',') ;
		}
		return b.toString() ;
	}
	
	/**
	 * Determine whether two NumberList objects are exactly similar
	 * 
	 * @param  other Other NumberList to compare to
	 * 
	 * @return <code>true</code> if all data are the same, <code>false</code> if not
	 */
	public boolean equals(final NumberList other)
	{
		if (this == other) {
			return true ;
		}
		if (null == other) {
			return false ;
		}
		
		if ((_iMinValue != other._iMinValue) ||
		    (_iMaxValue != other._iMaxValue) ||
		    (_bAllowsNegativeValues != other._bAllowsNegativeValues))
			return false ;
		
		// Check sizes
		//
		if (this.size() != other.size())
			return false ;
		
		// Once sizes are equal, just need to check that all elements in this are also contained in other
		//
		for (Iterator<Integer> it = this.iterator() ; it.hasNext() ; )
			if (false == other.contains(it.next()))
				return false ;
		
		return true ;
	}
	
	/**
	  * Determine whether an object is exactly similar to this NumberList object
	  * 
	  * designed for ArrayList.contains(Obj) method
		* because by default, contains() uses equals(Obj) method of Obj class for comparison
	  * 
	  * @param o NumberList to compare to
	  *
	  * @return <code>true</code> if all data are the same, <code>false</code> if not
	  */
	public boolean equals(Object o) 
	{
		if (this == o) {
			return true ;
		}
		if (null == o || getClass() != o.getClass()) {
			return false ;
		}

		final NumberList other = (NumberList) o ;

		return (this.equals(other)) ;
	}
}