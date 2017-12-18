package com.ldv.server.guice;

import net.customware.gwt.dispatch.server.guice.ActionHandlerModule;

import org.apache.commons.logging.Log;
import com.google.inject.Singleton;
import com.ldv.server.handler.CheckPseudoHandler;
import com.ldv.server.handler.GetLanguagesHandler;
import com.ldv.server.handler.GetLdvGraphHandler;
import com.ldv.server.handler4ontology.GetLexiconFromCodeHandler;
import com.ldv.server.handler4ontology.GetLexiconListFromTextHandler;
import com.ldv.server.handler4ontology.GetSemanticNetworkHandler;
import com.ldv.server.handler4ontology.GetSynonymsHandler;
import com.ldv.server.handler.GetStatusHandler;
import com.ldv.server.handler.OpenLdvGraphHandler;
import com.ldv.server.handler.RegisterUserHandler;
import com.ldv.server.handler.SendLdvHandler;
import com.ldv.server.handler.ValidateUserHandler;
import com.ldv.server.handler4caldav.DeleteAvailabilityHandler;
import com.ldv.server.handler4caldav.GetAvailabilityHandler;
import com.ldv.server.handler4caldav.GetPlannedEventsHandler;
import com.ldv.server.handler4caldav.SaveNewAvailabilityHandler;
import com.ldv.server.handler4caldav.SaveNewEventHandler;
import com.ldv.server.handler4caldav.UpdateAvailabilityHandler;
import com.ldv.shared.rpc.GetGraphAction;
import com.ldv.shared.rpc.LdvCheckPseudoAction;
import com.ldv.shared.rpc.LdvGetLanguagesAction;
import com.ldv.shared.rpc.LdvGetStatusAction;
import com.ldv.shared.rpc.LdvRegisterUserAction;
import com.ldv.shared.rpc.LdvValidatorUserAction;
import com.ldv.shared.rpc.OpenGraphAction;
import com.ldv.shared.rpc.SendLoginAction;
import com.ldv.shared.rpc4caldav.DeleteComponentAction;
import com.ldv.shared.rpc4caldav.GetAvailabilityAction;
import com.ldv.shared.rpc4caldav.GetPlannedEventsAction;
import com.ldv.shared.rpc4caldav.SaveNewAvailabilityAction;
import com.ldv.shared.rpc4caldav.SaveNewEventAction;
import com.ldv.shared.rpc4caldav.UpdateAvailabilityAction;
import com.ldv.shared.rpc4ontology.GetLexiconAction;
import com.ldv.shared.rpc4ontology.GetLexiconListInfo;
import com.ldv.shared.rpc4ontology.GetSemanticNetworkInfo;
import com.ldv.shared.rpc4ontology.GetSynonymsListInfo;

/**
 * Module which binds the handlers and configurations
 *
 */
public class ServerModule extends ActionHandlerModule
{
	@Override
	protected void configureHandlers()
	{
		bindHandler(SendLoginAction.class,        SendLdvHandler.class) ;
		bindHandler(LdvCheckPseudoAction.class,   CheckPseudoHandler.class) ;
		bindHandler(LdvRegisterUserAction.class,  RegisterUserHandler.class) ;
		bindHandler(LdvGetLanguagesAction.class,  GetLanguagesHandler.class) ;
		bindHandler(LdvValidatorUserAction.class, ValidateUserHandler.class) ;
		bindHandler(LdvGetStatusAction.class,     GetStatusHandler.class) ;
		bindHandler(OpenGraphAction.class,        OpenLdvGraphHandler.class) ;
		
		bindHandler(GetPlannedEventsAction.class,    GetPlannedEventsHandler.class) ;
		bindHandler(SaveNewEventAction.class,        SaveNewEventHandler.class) ;
		bindHandler(GetAvailabilityAction.class,     GetAvailabilityHandler.class) ;
		bindHandler(SaveNewAvailabilityAction.class, SaveNewAvailabilityHandler.class) ;
		bindHandler(UpdateAvailabilityAction.class,  UpdateAvailabilityHandler.class) ;
		bindHandler(DeleteComponentAction.class,     DeleteAvailabilityHandler.class) ;
		
		bindHandler(GetLexiconAction.class,       GetLexiconFromCodeHandler.class) ;
		bindHandler(GetLexiconListInfo.class,     GetLexiconListFromTextHandler.class) ;
		bindHandler(GetSemanticNetworkInfo.class, GetSemanticNetworkHandler.class) ;
		bindHandler(GetSynonymsListInfo.class,    GetSynonymsHandler.class) ;
		
		bindHandler(GetGraphAction.class,         GetLdvGraphHandler.class) ;
  
		bind(Log.class).toProvider(LogProvider.class).in(Singleton.class) ;
	}
}
