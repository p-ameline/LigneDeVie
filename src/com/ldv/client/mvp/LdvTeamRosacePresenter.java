package com.ldv.client.mvp;

import java.util.ArrayList;
import java.util.Iterator;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

import com.google.inject.Inject;

import com.ldv.client.canvas.LdvTeamRosaceButton;
import com.ldv.client.canvas.LdvTeamRosaceCanvas;
import com.ldv.client.canvas.LdvTeamRosaceIcon;
import com.ldv.client.canvas.LdvTeamRosaceListObject;
import com.ldv.client.canvas.LdvTeamRosaceObject;
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
	
	private boolean                        _bIsVisible ;
	
	public interface Display extends WidgetDisplay 
	{
		public Canvas                  getCanvas() ;
		public LdvTeamRosaceCanvas     getCanvasProxy() ;
		public LdvTeamRosaceObject     hiTest(double x, double y) ;
		public LdvTeamRosaceListObject getListObject() ;
		public void                    initializeFromRosace(LdvModelRosace rosace) ;
		public void                    addIconForMandate(final LdvModelMandatePair mandatePair) ;
		public void                    setZorder(int iZorder) ;
	}
	
	@Inject
	public LdvTeamRosacePresenter(final LdvProjectWindowPresenter project, final Display display, EventBus eventBus) 
	{		
		super(display, eventBus) ;
		
		_projectPresenter = project ;
		_bIsVisible       = false ;
		
		bind() ;
	}
	
	@Override
	protected void onBind() 
	{
		// Capture mouse clicks on canvas
		//
		display.getCanvas().addMouseDownHandler(new MouseDownHandler() {			
			@Override
			public void onMouseDown(MouseDownEvent event) {
				_x = event.getX() ;
				_y = event.getY() ;
				doMouseDown() ;			
			}		
		});	
	}
	
	public void doMouseDown()
	{
		// Log.info("Calling doMouseDown") ;	
		//System.out.println("x: "+x+", y: "+y);
		
		// When handling a mouse down event, better prevent the project presenter to react
		//
		if (_bIsVisible)
			_projectPresenter.setMouseCaptured(true) ;
		
		LdvTeamRosaceObject clickObject = display.hiTest(_x, _y) ;
		
		if (null == clickObject)
			return ;
		
		String sHitObjectName = clickObject.getName() ; 
		
		if ("button".equalsIgnoreCase(sHitObjectName))
		{
			LdvTeamRosaceButton button = (LdvTeamRosaceButton) clickObject ;
			if (null != button)
			{
				switch (button.getType())
				{
					case close:
						_bIsVisible = false ;
						updateZOrder() ;
						break ;
				}
			}
			return ;
		}
		
		if ("icon".equalsIgnoreCase(sHitObjectName))
		{
			return ;
		}
		
		if ("pie".equalsIgnoreCase(sHitObjectName) || "circle".equalsIgnoreCase(sHitObjectName))
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
	
	/**
	 * 
	 * New mandate dialog
	 *
	 */
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
	
	/**
	 * Initialize all the Rosace components inside the view and add it to the interface
	 * 
	 * @param projectPanel Panel to add the view to
	 */
	public void initComponents(Panel projectPanel) 
	{
		// Log.info("LdvTeamRosacePresenter::initComponents for project " + _projectPresenter.getProjectUri()) ;
		
		_rosace = _projectPresenter.getRosace() ;
			
		// Asks the view to initialize its own components
		//
		display.initializeFromRosace(_rosace) ;

		// Display health team mandates
		//
		LdvModelTeam team = _projectPresenter.getTeam() ;
		if (null != team)
			initMandates(team.getTeam()) ;
		
		// Insert the view into project panel 
		//
		projectPanel.add(display.asWidget()) ;
	}
	
	/**
	 * Set the proper z-index depending on the visibility status
	 */
	public void updateZOrder()
	{
		if (null == _projectPresenter)
			return ;
		
		int iProjectZOrder = _projectPresenter.getZOrderForDisplay() ;
		if (_bIsVisible)
			display.setZorder(iProjectZOrder + 1) ;
		else
			display.setZorder(iProjectZOrder - 1) ;
	}
	
	/**
	 * Drawing team members' icons
	 * 
	 * @param aMandates Team users' mandates
	 */
	private void initMandates(final ArrayList<LdvModelMandatePair> aMandates)
	{
		if ((null == aMandates) || aMandates.isEmpty())
			return ;
		
		for (Iterator<LdvModelMandatePair> iter = aMandates.iterator() ; iter.hasNext() ; ) 
		{
			LdvModelMandatePair mandatePair = iter.next() ;
			display.addIconForMandate(mandatePair) ;
		}
	}

	public boolean isVisible() {
		return _bIsVisible ;
	}
	public void setVisible(boolean bIsVisible) {
		_bIsVisible = bIsVisible ;
	}
	
	/**
	 * Is the point (iX, iY) inside the canvas?
	 * 
	 * @param iX Absolute abscissa of the point (as measured from the browser window's client area)
	 * @param iY Absolute ordinate of the point (as measured from the browser window's client area)
	 * 
	 * @return <code>true</code> if the point is inside the canvas, <code>false</code> if not
	 */
	public boolean isPointInsideRosace(final int iX, final int iY)
	{
		int iLeft   = display.getCanvas().getAbsoluteLeft() ;
		int iTop    = display.getCanvas().getAbsoluteTop() ;
		int iWidth  = display.getCanvas().getOffsetWidth() ;
		int iHeight = display.getCanvas().getOffsetHeight() ;
		
		if ((iX < iLeft) || (iX > iLeft + iWidth))
			return false ;
		if ((iY < iTop) || (iY > iTop + iHeight))
			return false ;
		
		return true ;
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
