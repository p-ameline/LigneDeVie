package com.ldv.client.mvp;

import java.util.ArrayList;

import com.allen_sauer.gwt.log.client.Log;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.inject.Inject;

import com.ldv.client.canvas.LdvTeamRosaceCanvas;
import com.ldv.client.canvas.LdvTeamRosaceIcon;
import com.ldv.client.canvas.LdvTeamRosaceListObject;
import com.ldv.client.canvas.LdvTeamRosaceObject;
import com.ldv.client.event.LdvTeamRosaceInitEvent;
import com.ldv.client.event.LdvTeamRosaceInitEventHandler;
import com.ldv.client.model.LdvModelMandatePair;
import com.ldv.client.model.LdvModelRosace;
import com.ldv.client.model.LdvModelTeam;

import net.customware.gwt.presenter.client.EventBus;
import net.customware.gwt.presenter.client.widget.WidgetDisplay;
import net.customware.gwt.presenter.client.widget.WidgetPresenter;

public class LdvTeamRosacePresenter extends WidgetPresenter<LdvTeamRosacePresenter.Display>
{
	private LdvProjectWindowPresenter      _projectPresenter ;
	private LdvModelRosace                 _rosace ;
	private int                            _x ;
	private int                            _y ;
	// here initial the listMandate for test
	private ArrayList<LdvModelMandatePair> _aMandatesList ;
	
	public interface Display extends WidgetDisplay 
	{
		public Canvas                  getCanvas() ;
		public LdvTeamRosaceCanvas     getCanvasProxy() ;
		public LdvTeamRosaceObject     hiTest(double x, double y) ;
		public LdvTeamRosaceListObject getListObject() ;
		public void                    initializeFromRosace(LdvModelRosace rosace) ;
	}
	
	@Inject
	public LdvTeamRosacePresenter(final Display display, EventBus eventBus) 
	{		
		super(display, eventBus) ;
		
		bind() ;
		Log.info("entering LdvCanvasPresenter constructor.") ;
	}
	
	public void doMouseDown()
	{
		Log.info("Calling doMouseDown") ;	
		//System.out.println("x: "+x+", y: "+y);
		LdvTeamRosaceObject clickObject = display.hiTest(_x, _y) ;
		
		if (null == clickObject)
			return ;
		
		String sHitObjectName = clickObject.getName() ; 
		
		if ("icon".equalsIgnoreCase(sHitObjectName))
		{
			
		}
		else 
		{					
			DialogBox newMandate = new MyDialog(clickObject) ;
			newMandate.center() ;
		}
	}
	
	public void addMandate(LdvModelMandatePair mandatePair)
	{	
		if (null == mandatePair)
			return ;
		
		_aMandatesList.add(mandatePair) ;
		
		// MaxHeight is the height of the icons
		int height = display.getListObject().getMaxHeight() ;
		
		LdvTeamRosaceIcon icon = new LdvTeamRosaceIcon(display.getCanvasProxy(), _x, _y, height, mandatePair) ;				
		display.getListObject().add(icon) ; 		
	}
	
	@Override
	protected void onBind() 
	{
		eventBus.addHandler(LdvTeamRosaceInitEvent.TYPE, new LdvTeamRosaceInitEventHandler() 
		{
			@Override
			public void onInitSend(LdvTeamRosaceInitEvent event) 
			{
				Log.info("LdvTeamRosacePresenter handling LdvTeamRosaceInitEvent for project " + event.getFather().getProjectUri()) ;
				initComponents(event) ;
			} 
		});
		
		display.getCanvas().addMouseDownHandler(new MouseDownHandler(){			
			@Override
			public void onMouseDown(MouseDownEvent event) {
				_x = event.getX() ;
				_y = event.getY() ;
				doMouseDown();			
			}		
		});	
	}
	
	class MyDialog extends DialogBox
	{  
		public MyDialog(LdvTeamRosaceObject clickObject)
		{    
			//this.setWidth("100%");
			setText("New Mandate");
		    
			HTML msg = new HTML("<center>A customize dialog box for new mandate.</center>",true);

			FlexTable table = new FlexTable();
			Label label1 = new Label("Name") ;
			Label label2 = new Label("Job") ;
			Label label3 = new Label("Speciality") ;
			Label label4 = new Label("Begin Date") ;
			Label label5 = new Label("End Date") ;
			Label label6 = new Label("Mandate Type") ;
		    
		  final TextBox nameBox       = new TextBox() ;
		  final TextBox jobBox        = new TextBox() ;
		  final TextBox specialityBox = new TextBox() ;
		  final TextBox beginTimeBox  = new TextBox() ;
		  final TextBox endTimeBox    = new TextBox() ;
		  final TextBox typeBox       = new TextBox() ;
		    
		  table.setWidget(0, 0, label1);
		  table.setWidget(0, 1, nameBox);
		  table.setWidget(1, 0, label2);
		  table.setWidget(1, 1, jobBox);
		  table.setWidget(2, 0, label3);
		  table.setWidget(2, 1, specialityBox);
		  table.setWidget(3, 0, label4);
		  table.setWidget(3, 1, beginTimeBox);
		  table.setWidget(4, 0, label5);
		  table.setWidget(4, 1, endTimeBox);
		  table.setWidget(4, 0, label6);
		  table.setWidget(4, 1, typeBox);
		    
		  Button cancelButton = new Button("Cancel") ;
		  Button addButton    = new Button("Add") ;
		    
			cancelButton.addClickHandler(new ClickHandler() {
		        public void onClick(ClickEvent event) {
		          MyDialog.this.hide() ;
		        }
		      });

			addButton.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					String name = new String(nameBox.getText()) ;
					LdvModelMandatePair mandatePair = new LdvModelMandatePair() ;
					mandatePair.getMember().setLabel(name) ;
					addMandate(mandatePair) ;
					MyDialog.this.hide() ;
				}
			});
			
			VerticalPanel panel = new VerticalPanel() ;
		    
			HorizontalPanel hPanel = new HorizontalPanel() ;
			hPanel.add(addButton) ;
			hPanel.add(cancelButton) ;
		    
			panel.setSpacing(6) ;
			panel.add(msg) ;
			panel.add(table) ;
			panel.add(hPanel) ;
		    
			panel.setCellHorizontalAlignment(hPanel, VerticalPanel.ALIGN_RIGHT) ;
			setWidget(panel) ;
		}	
	}
	
	private void initComponents(LdvTeamRosaceInitEvent event) 
	{
		if ((null == event) || (event.getTarget() != this))
			return ;
		
		Log.info("LdvTeamRosacePresenter::initComponents for project " + event.getFather().getProjectUri()) ;
		
		_projectPresenter = event.getFather() ;
		if (null != _projectPresenter)
		{
			_rosace = _projectPresenter.getRosace() ;
			
			LdvModelTeam team = _projectPresenter.getTeam() ;
			if (null != team)
				initMandates(team.getTeam()) ;
		}
		
		// Asks the view to init its own components
		//
		getDisplay().initializeFromRosace(_rosace) ;
		
		// Insert the view into project panel 
		//
		AbsolutePanel projectPanel = (AbsolutePanel) event.getProject() ;
		projectPanel.add(getDisplay().asWidget()) ;
	}
	
	private void initMandates(final ArrayList<LdvModelMandatePair> aMandates)
	{
		
	}
	
	@Override
	protected void onUnbind() {
		
	}

	@Override
	public void revealDisplay() {
		
	}

	@Override
	protected void onRevealDisplay() {
		// TODO Auto-generated method stub
		
	}	
}
