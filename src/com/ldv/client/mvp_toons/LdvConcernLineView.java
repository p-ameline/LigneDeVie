package com.ldv.client.mvp_toons;

import java.util.ArrayList;
import java.util.Iterator;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.widgetideas.client.ResizableWidget;
import com.google.gwt.dom.client.Style; 
import com.google.gwt.dom.client.Style.Position;
import com.ldv.client.canvas.LdvBoxSeverity;

public class LdvConcernLineView extends Composite implements ResizableWidget, LdvConcernLinePresenter.Display
{	
	private final AbsolutePanel _mainPanel ;
	private final AbsolutePanel _titlePanel ;
	private final AbsolutePanel _toolsPanel ;
	private ArrayList<LdvBoxSeverity> _severityBoxList ;
	private final AbsolutePanel _goalLine;
	private String mainPanelId ;
		
	public LdvConcernLineView()
	{	
		super() ;
		
		_mainPanel = new AbsolutePanel() ;
		_mainPanel.addStyleName("ldv-Toon-ConcernLine") ;
		_mainPanel.getElement().getStyle().setPosition(Position.ABSOLUTE) ;
		
		_titlePanel = new AbsolutePanel() ;
		_titlePanel.addStyleName("ldv-Toon-Concernline-TitlePanel") ;
		
		_toolsPanel = new AbsolutePanel() ;
		_toolsPanel.addStyleName("ldv-Toon-Concernline-ToolsPanel") ;
		
		_severityBoxList = new ArrayList<LdvBoxSeverity>() ;
				
		_goalLine = new AbsolutePanel() ;
		_goalLine.addStyleName("ldv-Toon-Concernline-GoalLine") ;
		
		_mainPanel.add(_titlePanel) ;
		_mainPanel.add(_toolsPanel) ;
		
		initWidget(_mainPanel) ;
	}	
	
	@Override
	public void setID(int linenumber){
		mainPanelId = "ConcernLine"+Integer.toString(linenumber) ;
		DOM.setElementAttribute(_mainPanel.getElement(), "id", mainPanelId) ;
	}
	
	@Override
	public void setWidth(int width){
		String width_String = Integer.toString(width) + "px" ;
		_mainPanel.setWidth(width_String) ;
	}
	
	@Override
	public void setTitle(String title){
		HTML titleHTML = new HTML(title) ;
		_titlePanel.add(titleHTML) ;	
	}
	
	/**
	 * Set concern line's left and bottom coordinates 
	 * 
	 * @param left Left position for line
	 * @param top  Top position for line
	 */
	@Override
	public void setPosition(int left, int top)
	{
		_mainPanel.getElement().getStyle().setLeft(left, Style.Unit.PX) ;
		_mainPanel.getElement().getStyle().setBottom(top, Style.Unit.PX) ;		
	}
	
	@Override
	public void addSeverityBox(LdvBoxSeverity boxSeverity)
	{		
		String width_String = Integer.toString(boxSeverity.getBoxWidth()) + "px" ;	
		String height_String = "17px" ;
		LdvBoxSeverity newBoxSeverity = boxSeverity ;
		newBoxSeverity.getElement().getStyle().setPosition(Position.ABSOLUTE) ;

		newBoxSeverity.setWidth(width_String) ;
		newBoxSeverity.setHeight(height_String) ;
		newBoxSeverity.getElement().getStyle().setLeft(boxSeverity.getStartPosition(), Style.Unit.PX) ;
		
		newBoxSeverity.getElement().getStyle().setBackgroundColor(boxSeverity.getColor()) ;
		
		_severityBoxList.add(newBoxSeverity) ;
				
		_toolsPanel.add(newBoxSeverity) ;		
	}
	
	@Override
	public void ReAddSeverityBox()
	{
		_toolsPanel.clear();
		for(Iterator<LdvBoxSeverity> iter = this._severityBoxList.iterator();iter.hasNext();){
			LdvBoxSeverity boxSeverity = iter.next() ;
			boxSeverity.getElement().getStyle().setPosition(Position.ABSOLUTE) ;
			
			String width_String = Integer.toString(boxSeverity.getBoxWidth()) + "px" ;	
			boxSeverity.setWidth(width_String) ;
			boxSeverity.getElement().getStyle().setLeft(boxSeverity.getStartPosition(), Style.Unit.PX) ;
			
			_toolsPanel.add(boxSeverity) ;
		}
	}
		
	@Override
	public AbsolutePanel getMainPanel(){
		return this._mainPanel ;
	}
	
	@Override
	public ArrayList<LdvBoxSeverity> getSeverityBoxList(){
		return this._severityBoxList ;
	}
	
	@Override
	public void setSeverityBoxList(ArrayList<LdvBoxSeverity> boxSeverityList){
		this._severityBoxList = boxSeverityList;
	}	
	
	@Override
	public String getMainPanelId(){
		return this.mainPanelId ;
	}
	
	public void setMainPanelId(String id){
		this.mainPanelId = id ;
	}

	@Override
	public Widget asWidget() {
		return this ;
	}

	@Override
	public void onResize(int width, int height) {
		// TODO Auto-generated method stub		
	}
}
