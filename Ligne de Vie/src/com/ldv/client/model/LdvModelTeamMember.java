package com.ldv.client.model;

import java.util.Iterator;
import java.util.Vector;

/** 
 * Team member: person Id, label and vector of mandates for this person
 * 
 */
public class LdvModelTeamMember implements Cloneable
{
	enum MemberType { person, team } ;
	
	protected String                  _sPersonId ;
	protected String                  _sLabel ;
	protected Vector<LdvModelMandate> _aMandates = new Vector<LdvModelMandate>() ;
	protected MemberType						  _MemberType ;
		
	public LdvModelTeamMember()
	{
		reset() ; 
	}
	
	void reset()
	{	
		_sPersonId  = "" ;
		_sLabel     = "" ;
		_MemberType = MemberType.person ;
	}
	
	/** 
	 * Copy local informations and duplicate mandates 
	 * 
	 * @param src model to initialize this object from
	 * 
	 */
	public void deepCopy(LdvModelTeamMember src)
	{
		_sPersonId  = src._sPersonId ;
		_sLabel     = src._sLabel ;
		_MemberType = src._MemberType ;
	
		setMandates(src.getMandates()) ;
	}
	
	/** 
	 * Copy local informations and except for mandates that are left empty 
	 * 
	 * @param src model to initialize this object from
	 * 
	 */
	public void deepCopyButMandates(LdvModelTeamMember src)
	{
		_sPersonId  = src._sPersonId ;
		_sLabel     = src._sLabel ;
		_MemberType = src._MemberType ;
	
		_aMandates.clear() ;
	}
	
	public boolean isEmpty()
	{
		if (("".equals(_sPersonId)) || _aMandates.isEmpty())
			return true ;
		return false ;
	}

	public String getPersonId() {
  	return _sPersonId ;
  }
	public void setPersonId(String sPersonId) {
  	_sPersonId = sPersonId ;
  }

	public String getLabel() {
  	return _sLabel ;
  }
	public void setLabel(String sLabel) {
  	_sLabel = sLabel ;
  }

	public Vector<LdvModelMandate> getMandates() {
  	return _aMandates ;
  }
	public void addMandate(LdvModelMandate mandate)
  {
		if (null == mandate)
			return ;
		
		_aMandates.add(mandate) ;
  }
	/** 
	 * Initialize mandates by duplicating the content of the source vector 
	 * 
	 * @param Mandates vector of mandates to initialize local ones
	 * 
	 */
  public void setMandates(Vector<LdvModelMandate> Mandates)
  {
		_aMandates.clear() ;
		
		Iterator<LdvModelMandate> itr = Mandates.iterator() ; 
		while (itr.hasNext()) 
		{
			LdvModelMandate copyMandate = new LdvModelMandate() ; 
			copyMandate.deepCopy(itr.next()) ; 
			_aMandates.add(copyMandate) ;
		}
  }

	public MemberType getMemberType() {
  	return _MemberType ;
  }
	public void setMemberType(MemberType sMemberType) {
		_MemberType = sMemberType ;
  }
}
