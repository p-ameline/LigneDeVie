package com.ldv.client.model;

/** 
 * Mandate pair: a team member and a mandate
 * 
 */
public class LdvModelMandatePair
{
	protected LdvModelTeamMember _Member  = new LdvModelTeamMember() ;
	protected LdvModelMandate    _Mandate = new LdvModelMandate() ;

	public LdvModelMandatePair()
	{		
	}

	public void deepCopy(LdvModelMandatePair src)
	{
		setMember(src.getMember()) ;
		setMandate(src.getMandate()) ;
	}

	public LdvModelTeamMember getMember() {
		return _Member ;
	}
	public void setMember(LdvModelTeamMember member) {
		_Member.deepCopy(member) ;
	}
	public void setMemberNoMandates(LdvModelTeamMember member) {
		_Member.deepCopyButMandates(member) ;
	}

	public LdvModelMandate getMandate() {
		return _Mandate ;
	}
	public void setMandate(LdvModelMandate mandate) {
		_Mandate.deepCopy(mandate) ;
	}
}
