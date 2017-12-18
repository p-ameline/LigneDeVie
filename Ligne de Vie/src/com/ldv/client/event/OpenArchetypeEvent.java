package com.ldv.client.event;

import com.google.gwt.event.shared.GwtEvent;
import com.ldv.client.bigbro.BBSmallBrother;
import com.ldv.shared.archetype.LdvArchetype;

public class OpenArchetypeEvent extends GwtEvent<OpenArchetypeEventHandler> 
{	
	public static Type<OpenArchetypeEventHandler> TYPE = new Type<OpenArchetypeEventHandler>();
	
	private LdvArchetype   _archetype ;
	private BBSmallBrother _BBSmallBrother ;
	private final String   _sLang ;
	
	public static Type<OpenArchetypeEventHandler> getType() 
	{
		if (null == TYPE)
			TYPE = new Type<OpenArchetypeEventHandler>();
		return TYPE;
	}
	
	public OpenArchetypeEvent(LdvArchetype archetype, BBSmallBrother bbSmallBrother, final String sLang) 
	{
		_sLang          = sLang ;
		_BBSmallBrother = bbSmallBrother ;
		_archetype      = archetype ;
	}
	
	public LdvArchetype getArchetype() {
		return _archetype ;
	}
	
	public BBSmallBrother getBBSmallBrother() {
		return _BBSmallBrother ;
	}
	
	public String getLang() {
		return _sLang ;
	}
	
	@Override
	protected void dispatch(OpenArchetypeEventHandler handler) {
		handler.onOpenArchetype(this) ;		
	}

	@Override
	public Type<OpenArchetypeEventHandler> getAssociatedType() {
		// TODO Auto-generated method stub
		return TYPE ;
	}
}
