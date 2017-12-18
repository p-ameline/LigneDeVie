package com.ldv.client ;

import java.util.HashMap;
import java.util.Map;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import net.customware.gwt.dispatch.client.DispatchAsync;
import net.customware.gwt.dispatch.shared.Action;
import net.customware.gwt.dispatch.shared.Result;

/**
 * Dispatcher which support caching of data in memory
 * 
 */
public class LdVCachingDispatchAsynch implements DispatchAsync
{
	// private static final LdV_CachingDispatchAsynch _dispatcher = GWT.create( AppEngineDispatchService.class );
	
	
  private DispatchAsync               _dispatcher ;
  private Map<Action<Result>, Result> _cache = new HashMap<Action<Result>, Result>() ;
  private String                      _sessionId ;

	@Inject
  public LdVCachingDispatchAsynch(final DispatchAsync dispatcher)
  {
    _dispatcher = dispatcher ;
  }

  /*
   * (non-Javadoc)
   * @see net.customware.gwt.dispatch.client.DispatchAsync#execute(A, com.google.gwt.user.client.rpc.AsyncCallback)
   */
  public <A extends Action<R>, R extends Result> void execute(final A action, final AsyncCallback<R> callback) 
  {
    _dispatcher.execute(action, callback) ;
  }

  /**
   * Execute the give Action. If the Action was executed before it will get fetched from the cache
   * 
   * @param <A> Action implementation
   * @param <R> Result implementation
   * @param action the action
   * @param callback the callback
   */
  @SuppressWarnings("unchecked")
  public <A extends Action<R>, R extends Result> void executeWithCache(final A action, final AsyncCallback<R> callback)
  {
    final Result r = _cache.get(action) ;
		
    if (null != r) 
    {
      callback.onSuccess((R) r) ;
    }
    else 
    {
    	// _dispatcher.execute(_sessionId, action, new AsyncCallback<R>()
    	_dispatcher.execute(action, new AsyncCallback<R>()
    	{
    		public void onFailure(Throwable caught)
    		{
    			callback.onFailure(caught) ;
    		}

    		public void onSuccess(R result)
    		{
    			_cache.put((Action) action, (Result) result) ;
    			callback.onSuccess(result) ;
    		}
    	});
    }
  }

  /**
   * Clear the cache
   */
  public void clear()
  {
    _cache.clear() ;
  }
  
  /**
   * Manage Session Id
   */
  public String get_sessionId()
  {
  	return _sessionId ;
  }

	public void set_sessionId(String sessionId)
  {
  	_sessionId = sessionId ;
  }
}
