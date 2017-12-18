package com.ldv.client.bigbro;

import com.google.gwt.user.client.ui.DialogBox;

public class BBDialogBox extends DialogBox
{
	protected BBItem _BBItem ;

	public BBDialogBox() {
		init() ;
	}

	public BBDialogBox(final BBDialogBox src) {
		initFromModel(src) ;
	}

  /**
	*  Equivalent of = operator
	*  
	*  @param src Object to initialize from 
	**/
	public void initFromModel(final BBDialogBox src)
	{
		init() ;
		
		if (null == src)
			return ;

	}
	
	public void init() 
  {
		_BBItem = null ;
  }
	
	/**
	  * Determine whether two BBMessage are exactly similar
	  * 
	  * @return true if all data are the same, false if not
	  * @param  otherMessage BBMessage to compare with
	  * 
	  */
	public boolean equals(BBDialogBox otherMessage)
	{
		if (this == otherMessage) {
			return true ;
		}
		if (null == otherMessage) {
			return false ;
		}
		
		return true ;
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
		if (this == o) {
			return true ;
		}
		if (null == o || getClass() != o.getClass()) {
			return false;
		}

		final BBDialogBox itemData = (BBDialogBox) o ;

		return equals(itemData) ;
	}
}
