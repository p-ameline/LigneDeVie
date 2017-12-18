package com.ldv.server.handler4caldav;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import net.customware.gwt.dispatch.server.ActionHandler;
import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.ActionException;

import org.apache.commons.logging.Log;
import org.osaf.caldav4j.credential.CaldavCredential;

import com.ldv.server.model.AgendaCredentialManager;
import com.ldv.shared.rpc4caldav.DeleteComponentAction;
import com.ldv.shared.rpc4caldav.DeleteComponentResult;

import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * Delete a component identified by its UID 
 * 
 **/	
public class DeleteAvailabilityHandler implements ActionHandler<DeleteComponentAction, DeleteComponentResult>
{
	protected final Log                          _logger ;
	protected final Provider<ServletContext>     _servletContext ;
	protected final Provider<HttpServletRequest> _servletRequest ;
	
	@Inject
	public DeleteAvailabilityHandler(final Log logger,
                                    final Provider<ServletContext> servletContext,
                                    final Provider<HttpServletRequest> servletRequest)
	{
		super() ;
		
		_logger         = logger ;
		_servletContext = servletContext ;
		_servletRequest = servletRequest ;
	}
	
	/**
	  * Constructor dedicated to unit tests 
	  */
	public DeleteAvailabilityHandler()
	{
		super() ;
		
		_logger         = null ;
		_servletContext = null ;
		_servletRequest = null ;
	}

	@Override
	public DeleteComponentResult execute(final DeleteComponentAction action, final ExecutionContext context) throws ActionException 
  {	
		try 
		{			
			// CyrusCaldavCredential CyrusCredential = new CyrusCaldavCredential("213.246.42.106", "eve", "K87xux5myJSi") ;
			CaldavCredential credential = AgendaCredentialManager.getCredential(action.getSessionElements()) ;
			
			BaseCaldavClient client = new BaseCaldavClient(credential) ;
			CalDavCollectionManager collectionManager = new CalDavCollectionManager(client) ;
			
			String sUidToDelete = action.getUID() ;
			if ((null == sUidToDelete) || "".equals(sUidToDelete))
				return new DeleteComponentResult(false, "No component to delete") ;
			
			collectionManager.deleteComponentByUid(sUidToDelete) ;
			
			return new DeleteComponentResult(true, "") ;
		}
		catch (Exception cause) 
		{
			_logger.error("Unable to delete component", cause) ;
			return new DeleteComponentResult(false, cause.getMessage()) ;
		}
  }
		
	@Override
	public void rollback(final DeleteComponentAction action,
        							 final DeleteComponentResult result,
                       final ExecutionContext context) throws ActionException
  {
		// Nothing to do here
  }
 
	@Override
	public Class<DeleteComponentAction> getActionType() {
		return DeleteComponentAction.class ;
	}
}
