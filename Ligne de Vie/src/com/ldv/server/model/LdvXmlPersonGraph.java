package com.ldv.server.model;

import java.util.Vector;

import org.w3c.dom.Document;

public class LdvXmlPersonGraph
{	
	protected Document         _aTechnicalData ;
	protected Vector<Document> _aTrees ;
	
	public LdvXmlPersonGraph()
	{
		_aTrees = new Vector<Document>() ;
	}
}
