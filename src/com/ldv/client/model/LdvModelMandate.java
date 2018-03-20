package com.ldv.client.model;

/** 
 * Mandate: position in rosace, begin and end date
 * 
 */
public class LdvModelMandate implements Cloneable
{
	enum MandateType { user, root } ;
	
	protected String                  _sJob ;
	protected String                  _sSpeciality ;
	protected LdvModelMandatePosition _position = new LdvModelMandatePosition() ;
	protected String                  _sMandateBeginDate ; // format AAAAMMDDhhmmss
	protected String                  _sMandateEndDate ;   // format AAAAMMDDhhmmss
	protected MandateType             _mandateType ;
	// protected LdvTeamRosaceIcon 			          _Icon;
	
	public LdvModelMandate()
	{
		reset() ; 
	}
	
	void reset()
	{	
		_sJob              = "" ;
		_sSpeciality       = "" ;
		_position.reset() ;
		_sMandateBeginDate = "" ;
		_sMandateEndDate   = "" ;
		_mandateType       = MandateType.user ;
	}

	/** 
	 * Similar to operator=
	 * 
	 * @param src model to initialize this object from
	 */
	public void deepCopy(LdvModelMandate src)
	{
		_sJob              = src._sJob ;
		_sSpeciality       = src._sSpeciality ;
		_sMandateBeginDate = src._sMandateBeginDate ;
		_sMandateEndDate   = src._sMandateEndDate ;
		_mandateType       = src._mandateType ;
		
		_position.deepCopy(src._position) ;
	}
	
	public boolean isEmpty()
	{
		if (_position.isEmpty() || "".equals(_sMandateBeginDate))
			return true ;
		return false ;
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
	
	public String getMandateBeginDate() {
		return _sMandateBeginDate ;
	}
	public void setMandateBeginDate(String sMandateBeginDate) {
		_sMandateBeginDate = sMandateBeginDate ;
	}

	public String getMandateEndDate() {
		return _sMandateEndDate ;	
	}
	public void setMandateEndDate(String sMandateEndDate) {
		_sMandateEndDate = sMandateEndDate ;
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
