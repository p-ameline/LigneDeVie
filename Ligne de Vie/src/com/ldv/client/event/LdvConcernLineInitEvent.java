package com.ldv.client.event;

import java.util.ArrayList;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.user.client.ui.Panel;
import com.ldv.client.model.LdvModelConcern;
import com.ldv.client.model.LdvModelDocument;
import com.ldv.client.mvp.LdvProjectWindowPresenter;
import com.ldv.client.mvp_toons.LdvConcernLinePresenter;

/** 
 * Concern line initialization event
 * 
 */
public class LdvConcernLineInitEvent extends GwtEvent<LdvConcernLineInitEventHandler> 
{	
	public static Type<LdvConcernLineInitEventHandler> TYPE = new Type<LdvConcernLineInitEventHandler>();
	
	private Panel                       _project ;
	private LdvConcernLinePresenter     _target ;
	private LdvProjectWindowPresenter   _father ;
	private LdvModelConcern						  _model ;
	private int  											  _lineNumber ;
	private ArrayList<LdvModelDocument>	_modelDocumentArray ;
	
	public static Type<LdvConcernLineInitEventHandler> getType() 
	{
		if (null == TYPE)
			TYPE = new Type<LdvConcernLineInitEventHandler>();
		return TYPE;
	}
	
	public LdvConcernLineInitEvent(Panel project, LdvConcernLinePresenter target, LdvProjectWindowPresenter father, LdvModelConcern model, int lineNumber, ArrayList<LdvModelDocument> modelDocumentArray)
	{
		_project            = project ;
		_target             = target ;
		_father             = father ;
		_model              = model ;
		_lineNumber         = lineNumber ;
		_modelDocumentArray = modelDocumentArray ;
	}
	
	public Panel getProject(){
		return _project ;
	}
	
	public LdvConcernLinePresenter getTarget(){
		return _target ;
	}
	
	public LdvProjectWindowPresenter getFather(){
		return _father ;
	}
	
	public LdvModelConcern getModel(){
		return _model ;
	}
	
	public int getLineNumber(){
		return _lineNumber ;
	}
	
	public ArrayList<LdvModelDocument> getModelDocumentArray(){
		return _modelDocumentArray ;
	}
	
	@Override
	protected void dispatch(LdvConcernLineInitEventHandler handler) {
		handler.onInitSend(this) ;
	}

	@Override
	public Type<LdvConcernLineInitEventHandler> getAssociatedType() {
		return TYPE ;
	}
}
