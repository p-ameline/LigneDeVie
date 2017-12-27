package com.ldv.client.model;

import java.util.ArrayList;
import java.util.Iterator;

/** 
 * Project information (team, concerns, goals, documents...)
 * 
 */
public class LdvModelProject
{
	private String                      _sProjectURI ;
	private String                      _sProjectTypeLexicon ;
	
	private LdvModelRosace              _Rosace         = null ;
	private LdvModelTeam                _Team           = new LdvModelTeam() ;
	
	private ArrayList<LdvModelConcern>  _concernsArray  = new ArrayList<LdvModelConcern>() ;	// Concerns
	private ArrayList<LdvModelGoal>     _goalsArray     = new ArrayList<LdvModelGoal>() ;			// Goals
	
	private ArrayList<LdvModelDocument> _documentsArray = new ArrayList<LdvModelDocument>() ;	// Documents
	private ArrayList<LdvModelEvent>    _eventsArray    = new ArrayList<LdvModelEvent>() ;		// Events
	
	public LdvModelProject()
	{
		super() ;
		
		_sProjectURI         = "" ;
		_sProjectTypeLexicon = "" ;
	}
	
	/**
	*  Get the concern object whose root node has a given node Id
	*  
	*  @param  sConcernId Node Id to look for
	*  @return A LdvModelConcern object if found of <code>null</code> if not
	**/
	public LdvModelConcern getConcernById(String sConcernId)
	{
		if ((null == sConcernId) || sConcernId.equals("") || _concernsArray.isEmpty())
			return (LdvModelConcern) null ;
		
		for (Iterator<LdvModelConcern> itr = _concernsArray.iterator() ; itr.hasNext() ; )
		{
			LdvModelConcern concern = itr.next() ;
			if (concern.getID().equals(sConcernId))
				return concern ;
		}
		
		return (LdvModelConcern) null ;
	}
	
	public void addConcern(LdvModelConcern concern) {
		_concernsArray.add(concern) ;
	}
	
	public void addGoal(LdvModelGoal goal) {
		_goalsArray.add(goal) ;
	}
	
	public void addDocument(LdvModelDocument doc) {
		_documentsArray.add(doc) ;
	}
	
	public void addEvent(LdvModelEvent event) {
		_eventsArray.add(event) ;
	}
	
	public void addMandatePair(LdvModelMandatePair pair) {
		_Team.addMandatePair(pair) ;
	}
	
	public void setProjectType(String sProjectType) { 
		_sProjectTypeLexicon = sProjectType ; 
	}
	public String getProjectType() { 
		return _sProjectTypeLexicon ; 
	}
	
	public String getProjectUri() {
		return _sProjectURI ;
	}
	public void setProjectUri(String sProjectURI) {
		_sProjectURI = sProjectURI ;
	}
	
	public ArrayList<LdvModelConcern> getConcerns() { 
		return _concernsArray ; 
	}
	public int getConcernsCount() {
		return _concernsArray.size() ;
	}
	
	public ArrayList<LdvModelGoal> getGoals() { 
		return _goalsArray ; 
	}
	
	public ArrayList<LdvModelDocument> getDocuments() { 
		return _documentsArray ; 
	}
	
	public LdvModelRosace getRosace() {
		return _Rosace ;
	}
	public void setRosace(LdvModelRosace rosace) {
		_Rosace = rosace ;
	}
	
	public LdvModelTeam getTeam() { 
		return _Team ; 
	}
}
