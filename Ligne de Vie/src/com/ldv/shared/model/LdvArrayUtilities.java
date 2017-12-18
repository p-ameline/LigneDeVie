package com.ldv.shared.model;

import java.util.ArrayList;

public class LdvArrayUtilities
{
	/**
	 * Is a given String already in Array ?
	 * 
	 * @param sModel   String to look for
	 * @param aStrings Array to look into
	 * @return true is sModel exists in aStrings, false if not 
	 * 
	 **/
	public static boolean isStringInArray(String sModel, ArrayList<String> aStrings)
	{
		if ((null == aStrings) || aStrings.isEmpty() || (null == sModel) || sModel.equals("")) 
			return false ;
		
		return aStrings.contains(sModel) ;
	}
}
