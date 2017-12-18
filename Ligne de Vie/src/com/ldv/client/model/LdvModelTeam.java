package com.ldv.client.model;

import java.util.ArrayList;

/** 
 * Team as a list of mandate pairs
 * 
 */
public class LdvModelTeam
{
	private ArrayList<LdvModelMandatePair> _aMandatePairsList = new ArrayList<LdvModelMandatePair>() ;
	
	public LdvModelTeam()
	{		
	}

	public ArrayList<LdvModelMandatePair> getTeam() {
		return _aMandatePairsList ;
	}

	public void addMandatePair(LdvModelMandatePair pair) {
		_aMandatePairsList.add(pair) ;
	}
}
