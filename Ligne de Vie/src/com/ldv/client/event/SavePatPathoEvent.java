package com.ldv.client.event;

import com.google.gwt.event.shared.GwtEvent;
import com.ldv.client.bigbro.BBSmallBrother;

public class SavePatPathoEvent extends GwtEvent<SavePatPathoEventHandler> 
{	
	public static Type<SavePatPathoEventHandler> TYPE = new Type<SavePatPathoEventHandler>();
	
	private final BBSmallBrother _BigBoss ;
	
	public static Type<SavePatPathoEventHandler> getType() 
	{
		if (null == TYPE)
			TYPE = new Type<SavePatPathoEventHandler>();
		return TYPE;
	}
	
	public SavePatPathoEvent(final BBSmallBrother BigBoss) 
	{
		_BigBoss = BigBoss ;
	}
		
	public BBSmallBrother getBigBoss() {
		return _BigBoss ;
	}
	
	@Override
	protected void dispatch(SavePatPathoEventHandler handler) {
		handler.onSavePatPatho(this);
	}

	@Override
	public Type<SavePatPathoEventHandler> getAssociatedType() {
		// TODO Auto-generated method stub
		return TYPE;
	}

}
