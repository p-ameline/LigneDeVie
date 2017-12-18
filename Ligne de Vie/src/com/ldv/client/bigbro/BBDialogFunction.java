package com.ldv.client.bigbro;

import com.ldv.shared.util.MiscellanousFcts;

/**
 * This object controls the functions that can be executed inside dialogs 
 * 
 * @author Philippe Ameline
 * 
 */
public class BBDialogFunction 
{
	protected BBSmallBrother _BigBoss ;
	
	protected BBControl      _Control ;
	protected String         _sFunctionName ;
  protected BBItem         _bbItem ;
  
  public enum SITUATION_TYPE { Creation, Reinit, Execute, PostExec, PreClose, Close, CtrlKey } ;
	
	public BBDialogFunction(BBSmallBrother bigBoss, BBItem item, String sFunctionName, BBControl control) 
	{
		_BigBoss       = bigBoss ;
		_sFunctionName = sFunctionName ;
		_bbItem        = item ;
		_Control       = control ;
	}
	
	public BBDialogFunction(final BBDialogFunction src) {
		initFromModel(src) ;
	}
	
  /**
	*  Equivalent of = operator
	*  
	*  @param src Object to initialize from 
	**/
	public void initFromModel(final BBDialogFunction src)
	{
		init() ;
		
		if (null == src)
			return ;
		
		_BigBoss       = src._BigBoss ;
		_Control       = src._Control ;
		_sFunctionName = src._sFunctionName ;
		_bbItem        = src._bbItem ;
	}
  
  public void init() 
  {
  	_BigBoss       = null ;
  	_Control       = null ;
  	_sFunctionName = "" ;
  	_bbItem        = null ;
  }
  
  /**
	 * Execute a function     
	 * 
	 * @param situation indicates a what step in dialogs life the function is to be executed
	 * @return 1 if other functions can be executed later ; 0 if not
	 * 
	 **/
  public int execute(SITUATION_TYPE situation)
  {
  	boolean bLetDefaultProcessing = true ;
  	
  	if (bLetDefaultProcessing)
  		return 1 ;
  	else
  		return 0 ;
  }
  	
  public BBItem getBBItem() {         
  	return _bbItem ;
  }
  
  public String getFunctionName() {       
  	return _sFunctionName ;
  }
  
	/**
	  * Determine whether two BBItemData are exactly similar
	  * 
	  * @return true if all data are the same, false if not
	  * @param  itemData BBItemData to compare
	  * 
	  */
	public boolean equals(BBDialogFunction other)
	{
		if (this == other)
			return true ;
		
		if (null == other) 
			return false ;
		
		return (MiscellanousFcts.areIdenticalStrings(_sFunctionName, other._sFunctionName) && 
				    (_bbItem == other._bbItem)) ;
	}

	/**
	  * Determine whether an object is exactly similar to this
	  * 
	  * @return true if all data are the same, false if not
	  * @param o Object to compare
	  * 
	  */
	public boolean equals(Object o) 
	{
		if (this == o) 
			return true ;

		if (null == o || getClass() != o.getClass()) 
			return false ;

		final BBDialogFunction itemData = (BBDialogFunction) o ;

		return equals(itemData) ;
	}
}
