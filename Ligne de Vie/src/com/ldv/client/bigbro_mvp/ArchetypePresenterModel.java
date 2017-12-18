package com.ldv.client.bigbro_mvp;

import com.ldv.client.bigbro.BBItem;

import com.ldv.shared.archetype.LdvArchetype;
import com.ldv.shared.archetype.LdvArchetypeControl;
import com.ldv.shared.archetype.LdvArchetypeDialogBox;
import com.ldv.shared.graph.LdvModelNodeArray;

import net.customware.gwt.dispatch.client.DispatchAsync;
import net.customware.gwt.presenter.client.EventBus;
import net.customware.gwt.presenter.client.widget.WidgetDisplay;
import net.customware.gwt.presenter.client.widget.WidgetPresenter;

/**
 * Abstract super-class for user parameters presenters
 * 
 * @author Philippe Ameline
 * 
 */
public abstract class ArchetypePresenterModel<D extends ArchetypePresenterModel.ArchetypeDisplayModel> extends WidgetPresenter<D> 
{
	public interface ArchetypeDisplayModel extends WidgetDisplay
	{
		public void initialize() ;
		public void setPosition(int iLeft, int iTop) ;
		public void setDimensions(int iWidth, int iHeight) ;
		public void setCaption(String sCaption) ;
		public void addWidget(LdvArchetypeControl control, BBItem bbItem) ;
	}
	
	protected final DispatchAsync     _dispatcher ;
	protected       LdvArchetype      _archetype ;
	protected final LdvModelNodeArray _patpatho ;
	protected       BBItem            _bbItem ;
	
	public ArchetypePresenterModel(final D             display, 
			                           final EventBus      eventBus,
			                           final DispatchAsync dispatcher) 
	{
		super(display, eventBus) ;
		
		_dispatcher = dispatcher ;
		_archetype  = null ;
		_patpatho   = null ;
		_bbItem     = null ;
	}
	
	@Override
	protected void onBind() 
	{
	}    
	
	/** 
	 * Initialize the view according to the archetype.
	 * 
	 * @param archetype Model archetype
	 * @param bbItem    BBItem that controls the dialog box
	 * @param sLang     Language to be found inside the archetype to display the properly localized GUI 
	 * 
	 */
	protected void initComponents(final LdvArchetype archetype, final BBItem bbItem, final String sLang) 
	{
		_archetype = archetype ;
		_bbItem    = bbItem ;
		
		if (null == _archetype)
			return ;
		
		// Get the archetype <code>dialogbox</code> description of controls to be created for this language 
		//
		LdvArchetypeDialogBox dialogBox = _archetype.getDialogBox(sLang) ;
		
		if ((null == dialogBox) || dialogBox.isEmpty())
			return ;
		
		getDisplay().setCaption(dialogBox.getCaption()) ;
		
		getDisplay().setPosition(dialogBox.getX(), dialogBox.getY()) ;
		getDisplay().setDimensions(dialogBox.getW(), dialogBox.getH()) ;
		
		// Creating controls
		//
		LdvArchetypeControl archetypeControl = dialogBox.getFirstControl() ; 
		
		while (null != archetypeControl)
		{
			getDisplay().addWidget(archetypeControl, _bbItem) ;
			
			archetypeControl = dialogBox.getNextControl(archetypeControl) ;
		}

		// Insert the view into project panel 
		//
		// AbsolutePanel projectPanel = (AbsolutePanel) event.getProject() ;
		// projectPanel.add(getDisplay().asWidget()) ;
	}
}
