package com.ldv.client.event;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.user.client.ui.Panel;

import com.ldv.shared.archetype.LdvArchetype;

import com.ldv.client.bigbro.BBSmallBrother;
import com.ldv.client.bigbro_mvp.ArchetypeDialogPresenter;

public class ArchetypeDialogPresenterInitEvent extends GwtEvent<ArchetypeDialogPresenterInitEventHandler> 
{	
	public static Type<ArchetypeDialogPresenterInitEventHandler> TYPE = new Type<ArchetypeDialogPresenterInitEventHandler>();
	
	private ArchetypeDialogPresenter _targetArchetypePresenter ;
	private Panel                    _FatherPanel ;
	private LdvArchetype             _Archetype ;
	private BBSmallBrother           _bbSmallBrother ;
	private String                   _sLang ;
	
	public static Type<ArchetypeDialogPresenterInitEventHandler> getType() 
	{
		if (null == TYPE)
			TYPE = new Type<ArchetypeDialogPresenterInitEventHandler>();
		return TYPE;
	}
	
	public ArchetypeDialogPresenterInitEvent(final ArchetypeDialogPresenter targetArchetypePresenter, final Panel mainPanel, final LdvArchetype archetype, final BBSmallBrother bbSmallBrother, final String sLang) 
	{
		_targetArchetypePresenter = targetArchetypePresenter ;
		_FatherPanel              = mainPanel ;
		_Archetype                = archetype ;
		_bbSmallBrother           = bbSmallBrother ;
		_sLang                    = sLang ;
	}
	
	public Panel getFatherPanel(){
		return _FatherPanel ;
	}
	
	public ArchetypeDialogPresenter getTargetArchetypePresenter() {
		return _targetArchetypePresenter ;
	}
	
	public LdvArchetype getArchetype() {            
		return _Archetype ;
	}
	
	public BBSmallBrother getSmallBrother() {
		return _bbSmallBrother ;
	}
	
	public String getLang() {
		return _sLang ;
	}
		
	@Override
	protected void dispatch(ArchetypeDialogPresenterInitEventHandler handler) {
		handler.onInitArchetypePresenter(this);
	}

	@Override
	public Type<ArchetypeDialogPresenterInitEventHandler> getAssociatedType() {
		// TODO Auto-generated method stub
		return TYPE;
	}
}
