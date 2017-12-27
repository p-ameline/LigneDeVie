package com.ldv.client.bigbro;

import com.google.gwt.user.client.ui.AbsolutePanel;

public class BBArchetypePanel extends AbsolutePanel
{
	protected BBItem _BBItem ;

	public BBArchetypePanel() {
		init() ;
	}

	public BBArchetypePanel(final BBArchetypePanel src) {
		initFromModel(src) ;
	}

  /**
	*  Equivalent of = operator
	*  
	*  @param src Object to initialize from 
	**/
	public void initFromModel(final BBArchetypePanel src)
	{
		init() ;
		
		if (null == src)
			return ;

	}
	
	public void init() 
  {
		_BBItem = null ;
  }
	
	public boolean isOpen() 
  {
		return false ;
  }
	
	/**
	  * Determine whether two BBMessage are exactly similar
	  * 
	  * @return true if all data are the same, false if not
	  * @param  otherMessage BBMessage to compare with
	  * 
	  */
	public boolean equals(BBArchetypePanel otherMessage)
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

		final BBArchetypePanel itemData = (BBArchetypePanel) o ;

		return equals(itemData) ;
	}
}
