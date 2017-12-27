package com.ldv.client.gin ;

import com.ldv.client.bigbro_mvp.ArchetypeDialogPresenter;
import com.ldv.client.bigbro_mvp.ArchetypeDialogView;
import com.ldv.client.bigbro_mvp.MultipleDialogPresenter;
import com.ldv.client.bigbro_mvp.MultipleDialogView;

import com.google.inject.Singleton;

import net.customware.gwt.presenter.client.DefaultEventBus;
import net.customware.gwt.presenter.client.EventBus;
import net.customware.gwt.presenter.client.gin.AbstractPresenterModule;

public class LdvClientModuleBase extends AbstractPresenterModule 
{
  @Override
  protected void configure()
  {		
    bind(EventBus.class).to(DefaultEventBus.class).in(Singleton.class) ;
		
    bindPresenter(ArchetypeDialogPresenter.class, ArchetypeDialogPresenter.Display.class, ArchetypeDialogView.class) ;
		bindPresenter(MultipleDialogPresenter.class,  MultipleDialogPresenter.Display.class,  MultipleDialogView.class) ;  }
}
