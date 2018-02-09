package com.ldv.client.model;

import java.util.ArrayList;
import java.util.List;

public class LdvModelLine extends LdvModelGenericBox
{
	private List<LdvModelDocument> _documentsArray = new ArrayList<LdvModelDocument>() ;
	private List<LdvModelEvent>    _eventsArray    = new ArrayList<LdvModelEvent>() ;
	// private List<LdvModelDocument> _documentsArray = new ArrayList<LdvModelDocument>() ;
	
	public LdvModelLine()
	{
		super() ;
	}
	
	public void addDocument(LdvModelDocument doc) {
		_documentsArray.add(doc) ;
	}
	
	public void addEvent(LdvModelEvent event) {
		_eventsArray.add(event) ;
	}
}
