package com.ldv.client.bigbro;

import com.ldv.shared.util.MiscellanousFcts;

public class BBCmdMessage 
{
	public static final int NSDLG_EXIT_UP    = -4 ;
	public static final int NSDLG_EXIT_DOWN  = -3 ;
	public static final int NSDLG_RETURN 	   = -1 ;
	public static final int NSDLG_NEXT 	     =  0 ;

	//
	// To reinitialize archetypes
	//
	public static final int NSDLGSTATUS_REINIT = -1 ;
	public static final int NSDLGSTATUS_NORMAL =  0 ;

	//
	// Between NSDLG_SUITE and NSDLGRETURN_DIRECT: direct access to son at index
	//
	public static final int NSDLGRETURN_DIRECT    =  10 ;
	public static final int NSDLGRETURN_SEPARATOR =  20 ;  // (max) Size of a return set  
	public static final int NSDLGRETURN_END       =  60 ;
	public static final int NSDLGRETURN_START     =  65 ;
	public static final int NSDLGMULTI		        =  70 ;
	public static final int NSDLG_GO_TO   	      = 100 ;
	public static final int NSDLG_NONE 	          = 200 ;
	public static final int NSDLG_ERROR 	        = 201 ;

	//
	// Messages dedicated to multiple descriptions
	//
	public static final int NSDLGMULTI_PREC	 =  NSDLGMULTI+1 ;  // Previous description
	public static final int NSDLGMULTI_NEXT	 =  NSDLGMULTI+2 ;  // Next description
	public static final int NSDLGMULTI_END	 =  NSDLGMULTI+3 ;  // End
	public static final int NSDLGMULTI_SUPP	 =  NSDLGMULTI+4 ;  // Delete description
	public static final int NSDLGMULTI_NEW	 =  NSDLGMULTI+5 ;  // New description
	public static final int NSDLGMULTI_LAST	 =  NSDLGMULTI+6 ;  // Start of description specific messages

	protected int    _iCmd ;
	protected String _sMessage ;
  
	public BBCmdMessage() 
	{
		init() ;
	}
	
	public BBCmdMessage(int iCmd, String sMessage) 
	{
		_iCmd     = iCmd ;
		_sMessage = sMessage ;
	}
	
	public BBCmdMessage(final BBCmdMessage src) {
		initFromModel(src) ;
	}
	
  /**
	*  Equivalent of = operator
	*  
	*  @param src Object to initialize from 
	**/
	public void initFromModel(final BBCmdMessage src)
	{
		init() ;
		
		if (null == src)
			return ;
		
		_iCmd     = src._iCmd ;
		_sMessage = src._sMessage ;
	}
  
  public void init() 
  {
  	_iCmd     = 0 ;
  	_sMessage = "" ;
  }
  	
  public int getCmd() {         
  	return _iCmd ;
  }
  
  public String getMessage() {       
  	return _sMessage ;
  }
  
	/**
	  * Determine whether two BBCmdMessage are exactly similar
	  * 
	  * @return true if all data are the same, false if not
	  * @param  other BBCmdMessage to compare
	  * 
	  */
	public boolean equals(BBCmdMessage other)
	{
		if (this == other)
			return true ;
		
		if (null == other) 
			return false ;
		
		return (MiscellanousFcts.areIdenticalStrings(_sMessage, other._sMessage) && 
				    (_iCmd == other._iCmd)) ;
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

		final BBCmdMessage itemData = (BBCmdMessage) o ;

		return equals(itemData) ;
	}
}
