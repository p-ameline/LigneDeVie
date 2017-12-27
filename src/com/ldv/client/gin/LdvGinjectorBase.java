package com.ldv.client.gin ;

import com.ldv.client.bigbro_mvp.ArchetypeDialogPresenter;
import com.ldv.client.bigbro_mvp.MultipleDialogPresenter;

import net.customware.gwt.dispatch.client.gin.StandardDispatchModule;

import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;

// @GinModules({ ClientDispatchModule.class, LdvClientModule.class })
@GinModules({ StandardDispatchModule.class, LdvClientModuleBase.class })
public interface LdvGinjectorBase extends Ginjector
{
	ArchetypeDialogPresenter getArchetypeDialogPresenter() ;
	MultipleDialogPresenter  getMultipleDialogPresenter() ;
}
