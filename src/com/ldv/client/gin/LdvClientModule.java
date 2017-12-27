package com.ldv.client.gin ;

import com.google.inject.Singleton;

import com.ldv.client.LdVCachingDispatchAsynch;
import com.ldv.client.mvp.LdvTeamRosacePresenter;
import com.ldv.client.mvp.LdvTeamRosaceView;
import com.ldv.client.mvp.LdvTimeControlledAreaPresenter;
import com.ldv.client.mvp.LdvMainPresenter;
import com.ldv.client.mvp.LdvMainView;
import com.ldv.client.mvp.LdvManagementPresenter;
import com.ldv.client.mvp.LdvManagementView;
import com.ldv.client.mvp.LdvProjectWindowPresenter;
import com.ldv.client.mvp.LdvProjectWindowView;
import com.ldv.client.mvp.LdvRegisterPresenter;
import com.ldv.client.mvp.LdvRegisterView;
import com.ldv.client.mvp.LdvAgendaPresenter;
import com.ldv.client.mvp.LdvAgendaView;
import com.ldv.client.mvp.LdvAppPresenter;
import com.ldv.client.mvp.LdvTimeControlledAreaView;
import com.ldv.client.mvp.LdvValidatorPresenter;
import com.ldv.client.mvp.LdvValidatorView;
import com.ldv.client.mvp.LdvWelcomePresenter;
import com.ldv.client.mvp.LdvWelcomeView;

import com.ldv.client.mvp_toons.LdvBaseLinePresenter;
import com.ldv.client.mvp_toons.LdvBaseLineView;
import com.ldv.client.mvp_toons.LdvBirthSeparatorPresenter;
import com.ldv.client.mvp_toons.LdvBirthSeparatorView;
import com.ldv.client.mvp_toons.LdvConcernLinePresenter;
import com.ldv.client.mvp_toons.LdvConcernLineView;
import com.ldv.client.mvp_toons.LdvDocumentPresenter;
import com.ldv.client.mvp_toons.LdvDocumentView;
import com.ldv.client.mvp_toons.LdvNowSeparatorPresenter;
import com.ldv.client.mvp_toons.LdvNowSeparatorView;
import com.ldv.client.mvp_toons.LdvTimeControllerPresenter;
import com.ldv.client.mvp_toons.LdvTimeControllerView;
import com.ldv.client.mvp_toons.LdvTimeDisplayPresenter;
import com.ldv.client.mvp_toons.LdvTimeDisplayView;
import com.ldv.client.mvp_toons.LdvTimeStepsPresenter;
import com.ldv.client.mvp_toons.LdvTimeStepsView;
import com.ldv.client.mvp_toons_agenda.AgendaAvailabilityManagementPresenter;
import com.ldv.client.mvp_toons_agenda.AgendaAvailabilityManagementView;
import com.ldv.client.mvp_toons_agenda.AgendaDayPresenter;
import com.ldv.client.mvp_toons_agenda.AgendaDayView;
import com.ldv.client.mvp_toons_agenda.AgendaTimeDisplayPresenter;
import com.ldv.client.mvp_toons_agenda.AgendaTimeDisplayView;
import com.ldv.client.mvp_toons.LdvTimeContextPresenter;
import com.ldv.client.mvp_toons.LdvTimeContextView;
import com.ldv.client.util.LdvSupervisor;

public class LdvClientModule extends LdvClientModuleBase 
{
  @Override
  protected void configure()
  {		
    bindPresenter(LdvMainPresenter.class,        LdvMainPresenter.Display.class,        LdvMainView.class) ;
    bindPresenter(LdvWelcomePresenter.class,     LdvWelcomePresenter.Display.class,     LdvWelcomeView.class) ;
    bindPresenter(LdvManagementPresenter.class,  LdvManagementPresenter.Display.class,  LdvManagementView.class) ;
    // bindPresenter(LdvResponsePresenter.class, LdvResponsePresenter.Display.class,    LdvView.class) ;
    // bindPresenter(LdvLoginPresenter.class,    LdvLoginPresenter.Display.class,       LoginView.class) ;
    bindPresenter(LdvRegisterPresenter.class,    LdvRegisterPresenter.Display.class,    LdvRegisterView.class) ;
    bindPresenter(LdvValidatorPresenter.class,   LdvValidatorPresenter.Display.class,   LdvValidatorView.class) ;
    bindPresenter(LdvTimeControlledAreaPresenter.class, LdvTimeControlledAreaPresenter.Display.class, LdvTimeControlledAreaView.class) ;
    bindPresenter(LdvProjectWindowPresenter.class,      LdvProjectWindowPresenter.Display.class,      LdvProjectWindowView.class) ;
    
    bindPresenter(LdvTeamRosacePresenter.class,      LdvTeamRosacePresenter.Display.class,      LdvTeamRosaceView.class) ;
    
    bindPresenter(LdvNowSeparatorPresenter.class,   LdvNowSeparatorPresenter.Display.class,   LdvNowSeparatorView.class) ;
    bindPresenter(LdvBirthSeparatorPresenter.class, LdvBirthSeparatorPresenter.Display.class, LdvBirthSeparatorView.class) ;
    bindPresenter(LdvBaseLinePresenter.class,       LdvBaseLinePresenter.Display.class,       LdvBaseLineView.class) ;
    bindPresenter(LdvConcernLinePresenter.class,    LdvConcernLinePresenter.Display.class,    LdvConcernLineView.class) ;
    bindPresenter(LdvDocumentPresenter.class,       LdvDocumentPresenter.Display.class,       LdvDocumentView.class) ;
    
    bindPresenter(LdvTimeControllerPresenter.class, LdvTimeControllerPresenter.Display.class, LdvTimeControllerView.class) ;
    bindPresenter(LdvTimeDisplayPresenter.class,    LdvTimeDisplayPresenter.Display.class,    LdvTimeDisplayView.class) ;
    bindPresenter(LdvTimeStepsPresenter.class,      LdvTimeStepsPresenter.Display.class,      LdvTimeStepsView.class) ;
    bindPresenter(LdvTimeContextPresenter.class,    LdvTimeContextPresenter.Display.class,    LdvTimeContextView.class) ;

    bindPresenter(LdvAgendaPresenter.class,                    LdvAgendaPresenter.Display.class,                    LdvAgendaView.class) ;
    bindPresenter(AgendaDayPresenter.class,                    AgendaDayPresenter.Display.class,                    AgendaDayView.class) ;
    bindPresenter(AgendaTimeDisplayPresenter.class,            AgendaTimeDisplayPresenter.Display.class,            AgendaTimeDisplayView.class) ;
    bindPresenter(AgendaAvailabilityManagementPresenter.class, AgendaAvailabilityManagementPresenter.Display.class, AgendaAvailabilityManagementView.class) ;
    
    bind(LdvAppPresenter.class).in(Singleton.class) ;
    bind(LdvSupervisor.class).in(Singleton.class) ;
    bind(LdVCachingDispatchAsynch.class);
  }
}
