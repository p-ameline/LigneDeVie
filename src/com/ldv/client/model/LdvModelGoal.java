package com.ldv.client.model;

import java.util.ArrayList;
import java.util.List;

public class LdvModelGoal extends LdvModelGenericBox
{
	protected String          _sGoalID ;
	protected String          _sReferentialID ;

	protected String      	  _sLexique ;
	protected String      	  _sConcernId ;
	protected String      	  _sReference ;
	protected String          _sCertitude ;          //presence ou l'absence de l'objectif
	protected String          _sComplementText ;     //en texte libre

  enum NSGOALTYPE { medicament, biology, traitement, exam, other } ;
  enum RYTHMETYPE { ponctual, cyclic, permanent } ; // (dur�e ?)
  protected RYTHMETYPE      _iRythme ;

//  bool            bCyclic ;

  protected LdvModelGoalCyclicCalendar _cyclicCalendar ;
  protected LdvModelGoalStaticCalendar _staticCalendar ;
  
  protected boolean                    _bHasValue ;
  protected LdvModelGoalValues         _values ;

  private List<LdvModelGoalSegment> _jalons     = new ArrayList<LdvModelGoalSegment>() ;
  private List<LdvModelGoalSegment> _metaJalons = new ArrayList<LdvModelGoalSegment>() ;

  protected String _sOpenEventNode ;     // Opening event node
  protected String _sCloseEventNode ;    // Closing event node 

  protected LdvModelGoalEventInfo _openingEvent ;
  protected LdvModelGoalEventInfo _closingEvent ;
  protected boolean 						  _isSelected ;    				//objectif selectionn� pour l'ordonnance (si medicament) ou process view
  
  public LdvModelGoal()
	{
  	super() ;
  	globalInit() ; 
	}
	
	void globalInit()
	{
		initBox() ;
		
		_sGoalID         = "" ;
		_sReferentialID  = "" ;

		_sLexique        = "" ;
		_sConcernId      = "" ;
		_sReference      = "" ;
		_sCertitude      = "" ;  
		_sComplementText = "" ;
		
		_iRythme         = RYTHMETYPE.ponctual ;
		
		_cyclicCalendar  = new LdvModelGoalCyclicCalendar() ;
		_staticCalendar  = new LdvModelGoalStaticCalendar()  ;
		
		_bHasValue       = false ;
	  _values          = new LdvModelGoalValues() ;
		
		_sOpenEventNode  = "" ;
	  _sCloseEventNode = "" ;
	  
	  _openingEvent    = new LdvModelGoalEventInfo()  ;
	  _closingEvent    = new LdvModelGoalEventInfo()  ;
	  
	  _isSelected      = false ;
	}

	public void init(String sOpenDate, String sCloseDate, String sOpenEvent, String sCloseEvent)
	{
	  if (false == sOpenDate.equals(""))
	  	_dBeginDate.initFromLocalDate(sOpenDate) ;
	  if (false == sCloseDate.equals(""))
	  	_dEndDate.initFromLocalDate(sCloseDate) ;

	  _sOpenEventNode  = sOpenEvent ;
	  _sCloseEventNode = sCloseEvent ;

	  // init() ;
	}
	
	public String getGoalID()
  {
  	return _sGoalID;
  }
	public void setGoalID(String sGoalID)
  {
  	_sGoalID = sGoalID;
  }

	public String getReferentialID()
  {
  	return _sReferentialID;
  }
	public void setReferentialID(String sReferentialID)
  {
  	_sReferentialID = sReferentialID;
  }

	public String getLexique()
  {
  	return _sLexique;
  }
	public void setLexique(String sLexique)
  {
  	_sLexique = sLexique;
  }

	public String getConcernId()
  {
  	return _sConcernId;
  }
	public void setConcernId(String sConcernId)
  {
  	_sConcernId = sConcernId;
  }

	public String getReference()
  {
  	return _sReference;
  }
	public void setReference(String sReference)
  {
  	_sReference = sReference;
  }

	public String getCertitude()
  {
  	return _sCertitude;
  }
	public void setCertitude(String sCertitude)
  {
  	_sCertitude = sCertitude;
  }

	public String getComplementText()
  {
  	return _sComplementText;
  }
	public void setComplementText(String sComplementText)
  {
  	_sComplementText = sComplementText;
  }

	public RYTHMETYPE getRythme()
  {
  	return _iRythme;
  }
	public void setRythme(RYTHMETYPE iRythme)
  {
  	_iRythme = iRythme;
  }

	public LdvModelGoalCyclicCalendar getCyclicCalendar()
  {
  	return _cyclicCalendar;
  }
	public void setCyclicCalendar(LdvModelGoalCyclicCalendar cyclicCalendar)
  {
  	_cyclicCalendar = cyclicCalendar;
  }

	public LdvModelGoalStaticCalendar getStaticCalendar()
  {
  	return _staticCalendar;
  }
	public void setStaticCalendar(LdvModelGoalStaticCalendar staticCalendar)
  {
  	_staticCalendar = staticCalendar;
  }

	public boolean hasValue()
  {
  	return _bHasValue;
  }
	public void setHasValue(boolean bValue)
  {
		_bHasValue = bValue;
  }

	public LdvModelGoalValues getValues()
  {
  	return _values;
  }
	public void setValues(LdvModelGoalValues values)
  {
  	_values = values;
  }

	public List<LdvModelGoalSegment> getJalons()
  {
  	return _jalons;
  }
	public void setJalons(List<LdvModelGoalSegment> jalons)
  {
  	_jalons = jalons;
  }

	public List<LdvModelGoalSegment> getMetaJalons()
  {
  	return _metaJalons;
  }
	public void setMetaJalons(List<LdvModelGoalSegment> metaJalons)
  {
  	_metaJalons = metaJalons;
  }

	public String getOpenEventNode()
  {
  	return _sOpenEventNode;
  }
	public void setOpenEventNode(String sOpenEventNode)
  {
  	_sOpenEventNode = sOpenEventNode;
  }

	public String getCloseEventNode()
  {
  	return _sCloseEventNode;
  }
	public void setCloseEventNode(String sCloseEventNode)
  {
  	_sCloseEventNode = sCloseEventNode;
  }

	public LdvModelGoalEventInfo getOpeningEvent()
  {
  	return _openingEvent;
  }
	public void setOpeningEvent(LdvModelGoalEventInfo openingEvent)
  {
  	_openingEvent = openingEvent;
  }

	public LdvModelGoalEventInfo getClosingEvent()
  {
  	return _closingEvent;
  }
	public void setClosingEvent(LdvModelGoalEventInfo closingEvent)
  {
  	_closingEvent = closingEvent;
  }

	public boolean isSelected()
  {
  	return _isSelected;
  }
	public void setSelected(boolean isSelected)
  {
  	_isSelected = isSelected;
  }
	
	
}
