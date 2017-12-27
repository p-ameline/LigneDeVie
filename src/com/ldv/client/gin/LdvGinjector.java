package com.ldv.client.gin ;

import com.google.gwt.inject.client.GinModules ;
import com.ldv.client.mvp.LdvAgendaPresenter;
import com.ldv.client.mvp.LdvAppPresenter;
import com.ldv.client.mvp.LdvTeamRosacePresenter;
import com.ldv.client.mvp.LdvMainPresenter;
import com.ldv.client.mvp.LdvManagementPresenter;
import com.ldv.client.mvp.LdvProjectWindowPresenter;
import com.ldv.client.mvp.LdvRegisterPresenter;
import com.ldv.client.mvp.LdvTimeControlledAreaPresenter;
import com.ldv.client.mvp.LdvValidatorPresenter;
import com.ldv.client.mvp.LdvWelcomePresenter;
import com.ldv.client.mvp_toons.LdvBaseLinePresenter;
import com.ldv.client.mvp_toons.LdvBirthSeparatorPresenter;
import com.ldv.client.mvp_toons.LdvConcernLinePresenter;
import com.ldv.client.mvp_toons.LdvDocumentPresenter;
import com.ldv.client.mvp_toons.LdvNowSeparatorPresenter;
import com.ldv.client.mvp_toons.LdvTimeControllerPresenter;
import com.ldv.client.mvp_toons.LdvTimeDisplayPresenter;
import com.ldv.client.mvp_toons.LdvTimeStepsPresenter;
import com.ldv.client.mvp_toons_agenda.AgendaAvailabilityManagementPresenter;
import com.ldv.client.mvp_toons_agenda.AgendaDayPresenter;
import com.ldv.client.mvp_toons_agenda.AgendaTimeDisplayPresenter;
import com.ldv.client.mvp_toons.LdvTimeContextPresenter;
import com.ldv.client.util.LdvSupervisor;

import net.customware.gwt.dispatch.client.gin.StandardDispatchModule;

@GinModules({ StandardDispatchModule.class, LdvClientModule.class })
public interface LdvGinjector extends LdvGinjectorBase
{
	LdvSupervisor                  getSuperInjector() ;
	LdvAppPresenter                getAppPresenter() ;
	LdvMainPresenter               getMainPresenter() ;
	LdvWelcomePresenter            getWelcomePresenter() ;
	LdvManagementPresenter         getManagementPresenter() ;
	LdvRegisterPresenter           getRegisterPresenter() ;
	LdvValidatorPresenter          getValidatorPresenter() ;
	LdvTimeControlledAreaPresenter getTimeControlPresenter() ;
	LdvProjectWindowPresenter      getProjectWindowPresenter() ;
	
	LdvTeamRosacePresenter         getCanvasPresenter() ;
	LdvNowSeparatorPresenter       getNowSeparatorPresenter() ;
	LdvBirthSeparatorPresenter     getBirthSeparatorPresenter() ;
	LdvBaseLinePresenter           getBaseLinePresenter() ;
	LdvConcernLinePresenter				 getConcernLinePresenter() ;	
	LdvDocumentPresenter					 getDocumentPresenter() ;
	LdvTimeControllerPresenter     getTimeControllerPresenter() ;
	LdvTimeDisplayPresenter        getTimeDisplayPresenter() ;
	LdvTimeStepsPresenter          getTimeStepsPresenter() ;
	LdvTimeContextPresenter        getTimeContextPresenter() ;
	
	LdvAgendaPresenter                    getAgendaPresenter() ;
	AgendaDayPresenter                    getAgendaDayPresenter() ;
	AgendaTimeDisplayPresenter            getAgendaTimeDisplayPresenter() ;
	AgendaAvailabilityManagementPresenter getAgendaAvailabilityManagementPresenter() ;
}
