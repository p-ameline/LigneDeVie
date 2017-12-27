package com.ldv.server.handler ;

import net.customware.gwt.dispatch.client.standard.StandardDispatchService;
import net.customware.gwt.dispatch.server.Dispatch;
import net.customware.gwt.dispatch.shared.Action;
import net.customware.gwt.dispatch.shared.DispatchException;
import net.customware.gwt.dispatch.shared.Result;

import com.google.inject.Inject;

public class DispatchTestService implements StandardDispatchService
{
	private Dispatch dispatch;

	@Inject
	public DispatchTestService(Dispatch dispatch)
	{
		this.dispatch = dispatch;
	}

	@Override
	public Result execute(Action<?> action)
	{
		Result result = null ;
    try
    {
	    result = dispatch.execute(action);
    } catch (DispatchException e)
    {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
    }
		return result;
	}
}
