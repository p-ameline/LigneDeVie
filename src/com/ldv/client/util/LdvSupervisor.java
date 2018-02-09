package com.ldv.client.util;

import java.util.Vector;

import com.ldv.client.gin.LdvGinjector;
import com.ldv.shared.database.Person;
import com.ldv.shared.graph.LdvGraphMapping;
import com.ldv.shared.graph.LdvModelGraph;

public class LdvSupervisor extends LdvSupervisorBase 
{
	boolean                 _bIsDisplayedGraphUsers ;
	
	private LdvGraphManager _userGraph ;
	
	private Person          _other ;
	private LdvGraphManager _otherGraph ;
 
	public LdvSupervisor()
	{
		super() ;
		
		_bIsDisplayedGraphUsers = true ;  // By default, displayed graph is user's
		
		_userGraph           = null ;
		_other               = null ;
		_otherGraph          = null ;
		
		_injector            = null ;
	}
	
	public LdvGinjector getInjector() {
		return (LdvGinjector) _injector;
	}
	public void setInjector(LdvGinjector injector) {
		_injector = injector;
	}
	
	public String getSessionToken() {
		return _sSessionToken ;
	}
	public void setSessionToken(String sSessionToken) {
		_sSessionToken = sSessionToken ;
	}
	
	public boolean isDisplayedGraphUsers() {
		return _bIsDisplayedGraphUsers ;
	}
	
	public Person getDisplayedPerson() 
	{
		if (_bIsDisplayedGraphUsers)
			return _user ;
		
		return _other ;
	}
		
	public LdvGraphManager getDisplayedGraph() 
	{
		if (false == _bIsDisplayedGraphUsers)
			return _otherGraph ;
		
		return _userGraph ;
	}
	
	public LdvGraphManager getUserGraph() {
		return _userGraph ;
	}
	
	public LdvGraphManager getOtherGraph() {
		return _otherGraph ;
	}
	
	public void initializeDisplayedGraph(final LdvModelGraph modelGraph) 
	{
		if (_bIsDisplayedGraphUsers)
			initializeUserGraph(modelGraph) ;
		else
			initializeOtherGraph(modelGraph) ;
	}
	
	public void initializeUserGraph(final LdvModelGraph modelGraph)
	{
		if (null == _userGraph)
			_userGraph = new LdvGraphManager(this) ; 
		
		_userGraph.initFromModel(modelGraph) ;
	}
	
	public void initializeOtherGraph(final LdvModelGraph modelGraph) 
	{
		if (null == _otherGraph)
			_otherGraph = new LdvGraphManager(this) ; 
		
		_otherGraph.initFromModel(modelGraph) ;
	}
	
	/**
	 * Update current graph from a subgraph that just has been created or modified 
	 */
	public void injectModifiedGraph(final LdvModelGraph modelGraph, final Vector<LdvGraphMapping> aMappings)
	{
		if (_bIsDisplayedGraphUsers)
			injectModifiedUserGraph(modelGraph, aMappings) ;
		else
			injectModifiedOtherGraph(modelGraph, aMappings) ;
	}
	
	/**
	 * Update current user's graph from a subgraph that just has been created or modified 
	 */
	public void injectModifiedUserGraph(LdvModelGraph modelGraph, final Vector<LdvGraphMapping> aMappings)
	{
		if (null == _userGraph)
			_userGraph = new LdvGraphManager(this) ; 
		
		_userGraph.injectModified(modelGraph, aMappings) ;
	}
	
	/**
	 * Update current other's graph from a subgraph that just has been created or modified 
	 */
	public void injectModifiedOtherGraph(LdvModelGraph modelGraph, final Vector<LdvGraphMapping> aMappings)
	{
		if (null == _otherGraph)
			_otherGraph = new LdvGraphManager(this) ; 
		
		_otherGraph.injectModified(modelGraph, aMappings) ;
	}
}
