package com.ldv.client.model;

import com.ldv.shared.model.LdvTime;

/** 
 * Mandate: position in rosace, begin and end date
 * 
 */
public class LdvModelMandate implements Cloneable
{
	enum MandateType { user, root } ;
	
	protected String                  _sID ;
	protected String                  _sPersonLdvID ;
	
	protected String                  _sJob ;
	protected String                  _sSpeciality ;
	protected LdvModelMandatePosition _position = new LdvModelMandatePosition() ;
	protected LdvTime                 _dBeginDate ; 
	protected LdvTime                 _dEndDate ;  
	protected MandateType             _mandateType ;
	// protected LdvTeamRosaceIcon 			          _Icon;
	
	public LdvModelMandate()
	{
		reset() ; 
	}
	
	void reset()
	{	
		_sID          = "" ;
		_sPersonLdvID = "" ;
		_sJob         = "" ;
		_sSpeciality  = "" ;
		_position.reset() ;
		_dBeginDate   = new LdvTime(0) ;
		_dEndDate     = new LdvTime(0) ;
		_mandateType  = MandateType.user ;
	}

	/** 
	 * Similar to operator=
	 * 
	 * @param src model to initialize this object from
	 */
	public void deepCopy(LdvModelMandate src)
	{
		_sID          = src._sID ;
		_sPersonLdvID = src._sPersonLdvID ;
		_sJob         = src._sJob ;
		_sSpeciality  = src._sSpeciality ;
		_mandateType  = src._mandateType ;
		
		_dBeginDate.initFromLdvTime(src._dBeginDate) ;
		_dEndDate.initFromLdvTime(src._dEndDate) ;
		
		_position.deepCopy(src._position) ;
	}
	
	public boolean isEmpty()
	{
		if (_position.isEmpty() || _dBeginDate.isEmpty())
			return true ;
		return false ;
	}
	
	public String getID() {
		return _sID ;
	}
	public void setID(final String sID) {
		_sID = sID ;
	}
	
	public String getPersonLdvID() {
		return _sPersonLdvID ;
	}
	public void setPersonLdvID(final String sPersonLdvID) {
		_sPersonLdvID = sPersonLdvID ;
	}
	
	public String getJob() {
		return _sJob ;
	}
	public void setJob(String sJob) {
		_sJob = sJob ;
	}

	public String getSpeciality() {
		return _sSpeciality ;
	}
	public void setSpeciality(String sSpeciality) {
		_sSpeciality = sSpeciality ;
	}

	public LdvModelMandatePosition getPosition() {
		return _position ;
	}
	public void setPosition(LdvModelMandatePosition position) {
		_position.deepCopy(position) ;
	}

	public double getDistance() {
		return _position.getDistance() ;
	}
	public void setDistance(int iDistance) {
		_position.setDistance(iDistance) ;
	}

	public double getAngle() {
		return _position.getAngle() ;
	}
	public void setAngle(int iAngle) {
		_position.setAngle(iAngle) ;
	}
	
	public LdvTime getBeginDate() {
		return _dBeginDate ;
	}
	public void setBeginDate(final LdvTime dBeginDate) 
	{
		if (null == dBeginDate)
			return ;
		
		_dBeginDate.initFromLdvTime(dBeginDate) ;
	}

	public LdvTime getEndDate() {
		return _dEndDate ;	
	}
	public void setEndDate(final LdvTime dEndDate) 
	{
		if (null == dEndDate)
			return ;
		
		_dEndDate.initFromLdvTime(dEndDate) ;
	}

	public MandateType getMandateType() {
		return _mandateType ;
	}
	public void setMandateType(MandateType mandateType) {
		_mandateType = mandateType ;
	}
	
/*
	public LdvTeamRosaceIcon getIcon() {
		return _Icon ;
	}
	public void setIcon(LdvTeamRosaceCanvas canvas, int x, int y, int height) {
		_Icon = new LdvTeamRosaceIcon(canvas, x, y, height);
	}
	public void setIcon(LdvTeamRosaceCanvas canvas, LdvModelMandatePosition position, int height) {
		_Icon = new LdvTeamRosaceIcon(canvas, position, height);		
	}
*/
}
