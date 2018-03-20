package com.ldv.client.mvp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Vector;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.widgetideas.client.ResizableWidget;

import com.ldv.client.canvas.LdvCoordinatesCartesian;
import com.ldv.client.canvas.LdvCoordinatesPolar;
import com.ldv.client.canvas.LdvTeamRosaceButton;
import com.ldv.client.canvas.LdvTeamRosaceCanvas;
import com.ldv.client.canvas.LdvTeamRosaceIcon;
import com.ldv.client.canvas.LdvTeamRosaceListObject;
import com.ldv.client.canvas.LdvTeamRosaceObject;
import com.ldv.client.canvas.LdvTeamRosacePetalDescriptor;
import com.ldv.client.canvas.LdvTeamRosacePie;
import com.ldv.client.canvas.LdvTeamRosaceCircle;
import com.ldv.client.canvas.LdvTeamRosaceStructure;
import com.ldv.client.canvas.LdvTeamRosaceText;
import com.ldv.client.canvas.TrigonometricFcts;
import com.ldv.client.model.LdvModelMandate;
import com.ldv.client.model.LdvModelMandatePair;
import com.ldv.client.model.LdvModelMandatePosition;
import com.ldv.client.model.LdvModelRosace;
import com.ldv.client.model.LdvModelRosacePetal;

public class LdvTeamRosaceView extends SimplePanel implements ResizableWidget, LdvTeamRosacePresenter.Display
{
	private ScrollPanel             _MainPanel ;
	private LdvTeamRosaceCanvas     _Canvas ;
	private Canvas                  _CanvasControl ;
	private LdvTeamRosaceListObject _ObjectsList ;
	private LdvTeamRosaceCircle     _CenterCircle ;
	
	private int                     _iBaseRadius ;     // Radius of the center circle (owner's petal)
	private int                     _iDimension ;
		
	private ArrayList<LdvTeamRosaceStructure> _aRosaceStructures = new ArrayList<LdvTeamRosaceStructure>() ; 
	
	public LdvTeamRosaceView()
	{	
		super() ;
		
		_CanvasControl = Canvas.createIfSupported() ;
		if (null == _CanvasControl)
			return ;
		
		_iBaseRadius = 50 ;
		
		
		_iDimension = 8 * _iBaseRadius ;  // 3 petals + border (labels + margins)
		_Canvas      = new LdvTeamRosaceCanvas(_CanvasControl, _iDimension, _iDimension) ;
		_ObjectsList = new LdvTeamRosaceListObject() ;
		
		this.add(_CanvasControl) ;

		this.addStyleName("ldvProjectRosace") ;
		
		// createStructures() ;
		
		
		
		// test the mandate
		// listMandate = new ArrayList<LdvModelMandatePair>() ;
		// int heightIcon = iHeightMax + 2 ;
		
/*
		String name1 = "Qianyue" ;
		double distance1 = 60;
		double angle1 = 70;
		LdvModelMandatePosition position1 = new LdvModelMandatePosition(distance1, angle1);
		
		String name2 = "Shaokun" ;
		double distance2 = 110;
		double angle2 = 200;
		LdvModelMandatePosition position2 = new LdvModelMandatePosition(distance2, angle2);
		
		LdvModelMandatePair mandate1 = new LdvModelMandatePair();
		mandate1.getMember().setLabel(name1);
		mandate1.getMandate().setPosition(position1);
		mandate1.getMandate().setIcon(_Canvas, position1, heightIcon);
		
		LdvModelMandatePair mandate2 = new LdvModelMandatePair();
		mandate2.getMember().setLabel(name2);
		mandate2.getMandate().setPosition(position2);
		mandate2.getMandate().setIcon(_Canvas, position2, heightIcon);
		
		setIconPosition(mandate1.getMandate().getIcon(), centerX, centerY, position1);
		setIconPosition(mandate2.getMandate().getIcon(), centerX, centerY, position2);
		
		// listMandate.add(mandate1);
		// listMandate.add(mandate2);
		
		_ObjectsList.add(mandate1.getMandate().getIcon());
		_ObjectsList.add(mandate2.getMandate().getIcon());
*/
                        
		_ObjectsList.draw() ;
		
		_MainPanel = new ScrollPanel(_CanvasControl) ;
		
		int iPanelDimension = 6 * _iBaseRadius ;
		
		_MainPanel.setWidth(Integer.toString(iPanelDimension)) ;
		_MainPanel.setHeight(Integer.toString(iPanelDimension)) ;
	    	    
		setWidget(_MainPanel) ;
	}
	
	/**
	 * Initialize all petals from a Rosace description
	 */
	@Override
	public void initializeFromRosace(final LdvModelRosace rosace)
	{
		_aRosaceStructures.clear() ;
		
		initializeObjectsList() ;
		
		if ((null == rosace) || rosace.getPetals().isEmpty())
		{
			initializeObjectsList() ;
			return ;
		}
		
		Vector<LdvModelRosacePetal> rosacePetals = rosace.getPetals() ;

		// Create petals
		//
		for (Iterator<LdvModelRosacePetal> iter = rosacePetals.iterator() ; iter.hasNext() ; ) 
		{
			LdvModelRosacePetal petal = iter.next() ;
			LdvTeamRosaceStructure struct = new LdvTeamRosaceStructure(petal) ;
			_aRosaceStructures.add(struct) ;
		}
		
		initializeObjectsList() ;
		
		addButtons() ;
	}
	
	/**
	 * Draw the Rosace
	 */
	protected void initializeObjectsList() 
	{
		_ObjectsList.clear() ;
		
		if (_aRosaceStructures.isEmpty())
			return ;
		
		int centerX  = 4 * _iBaseRadius ;
		int centerY  = 4 * _iBaseRadius ;
		
		// Sort vRosaceStructures according to ascending angle
		//
		Comparator<LdvTeamRosaceStructure> orderAngle = new Comparator<LdvTeamRosaceStructure>()
		{
			public int compare(LdvTeamRosaceStructure o1, LdvTeamRosaceStructure o2) {
				return (o1.getRosaceAngleLdvD() - o2.getRosaceAngleLdvD()) ;
			}
		};
		Collections.sort(_aRosaceStructures, orderAngle) ;
			
		int iHeightMax     = 0 ;
		int iPreviousAngle = 360 ;
			
		// Iterate through structures in reverse order
		//
		for (ListIterator<LdvTeamRosaceStructure> iterator = _aRosaceStructures.listIterator(_aRosaceStructures.size()) ; iterator.hasPrevious() ; )
		{
			LdvTeamRosaceStructure curStruct = iterator.previous() ;
			
			// Regular petal (pie slice shaped)
			//
			if (false == curStruct.isCenter())
			{
				int iRadius = 2 * _iBaseRadius ;
				int iHeight = curStruct.getRadiusMax() - 1 ;
					
				int iRadiusMax      = curStruct.getRadiusMax() ;
				int iStructureAngle = curStruct.getRosaceAngleLdvD() ;
					
				double iLeftAngle  = TrigonometricFcts.getCanvasRAngleFromLdvDAngle(iStructureAngle) ;
				double iRightAngle = TrigonometricFcts.getCanvasRAngleFromLdvDAngle(iPreviousAngle - 5) ;
					
				for (int j = curStruct.getRadiusMin() ; j <= iRadiusMax ; j++, iRadius += _iBaseRadius, iHeight--)
				{
					if (iHeight > iHeightMax) 
						iHeightMax = iHeight ;
					
					String sColor = "white" ;
					LdvTeamRosacePetalDescriptor petalDescr = curStruct.getPetalDescriptor(j) ;
					if (null != petalDescr)
					{
						sColor      = petalDescr.getColor() ;
						iLeftAngle  = TrigonometricFcts.getCanvasRAngleFromLdvDAngle(petalDescr.getLeftRosaceAngleLdvD()) ;
						iRightAngle = TrigonometricFcts.getCanvasRAngleFromLdvDAngle(petalDescr.getRightRosaceAngleLdvD()) ;
					}
						
					LdvTeamRosacePie pie = new LdvTeamRosacePie(_Canvas, centerX, centerY, iRadius, iLeftAngle, iRightAngle, sColor, iHeight) ;
					_ObjectsList.add(pie) ;
					if (j == iRadiusMax) 
						iRadius -= _iBaseRadius ;
				}
					
				iPreviousAngle = curStruct.getRosaceAngleLdvD() ;
					
				LdvTeamRosaceText text = new LdvTeamRosaceText(_Canvas, centerX, centerY, iRadius, iLeftAngle, iRightAngle, curStruct.getLabel()) ;
				_ObjectsList.add(text) ;				
			}
			
			// Center petal (full circle shaped)
			//
			else
			{
				String sColor = "white" ;
				LdvTeamRosacePetalDescriptor petalDescr = curStruct.getPetalDescriptor(curStruct.getRadiusMin()) ;
				if (null != petalDescr)
					sColor = petalDescr.getColor() ;
				
				_CenterCircle = new LdvTeamRosaceCircle(_Canvas, centerX, centerY, _iBaseRadius, sColor, iHeightMax + 1) ;
				_ObjectsList.add(_CenterCircle) ;
			}
		}
		
		_ObjectsList.draw() ;
	}
	
	/**
	 * Add control buttons
	 */
	protected void addButtons()
	{
		LdvTeamRosaceButton closeButton = new LdvTeamRosaceButton(_Canvas, _iDimension - 48, 0, 48, 48, LdvTeamRosaceButton.BUTTON_TYPE.close) ;
		_ObjectsList.add(closeButton) ;
		closeButton.draw() ;
	}
	
/*
	private void createStructures()
	{
		LdvTeamRosaceStructure struct1 = new LdvTeamRosaceStructure(3, "Doctors", 1, 2) ;
		LdvTeamRosacePetalDescriptor petal11 = new LdvTeamRosacePetalDescriptor(struct1, 1, "blue", 2, 88) ;
		LdvTeamRosacePetalDescriptor petal12 = new LdvTeamRosacePetalDescriptor(struct1, 2, "cyan", 2, 88) ;
		struct1.getPetals().add(petal11) ;
		struct1.getPetals().add(petal12) ;
		
		LdvTeamRosaceStructure struct2 = new LdvTeamRosaceStructure(93, "Health professionnals", 1, 2) ;
		LdvTeamRosacePetalDescriptor petal21 = new LdvTeamRosacePetalDescriptor(struct2, 1, "red", 92, 178) ;
		LdvTeamRosacePetalDescriptor petal22 = new LdvTeamRosacePetalDescriptor(struct2, 2, "pink", 92, 178) ;
		struct2.getPetals().add(petal21) ;
		struct2.getPetals().add(petal22) ;
		
		LdvTeamRosaceStructure struct3 = new LdvTeamRosaceStructure(183, "Familly", 1, 2) ;
		LdvTeamRosacePetalDescriptor petal31 = new LdvTeamRosacePetalDescriptor(struct3, 1, "chocolate", 182, 358) ;
		LdvTeamRosacePetalDescriptor petal32 = new LdvTeamRosacePetalDescriptor(struct3, 2, "khaki", 182, 358) ;
		struct3.getPetals().add(petal31) ;
		struct3.getPetals().add(petal32) ;
		
		LdvTeamRosaceStructure struct4 = new LdvTeamRosaceStructure(0, "Me", 0, 0) ;
		LdvTeamRosacePetalDescriptor petal4 = new LdvTeamRosacePetalDescriptor(struct4, 1, "green", 0, 0) ;
		struct4.getPetals().add(petal4) ;
	
		_aRosaceStructures.add(struct1) ;
		_aRosaceStructures.add(struct2) ;
		_aRosaceStructures.add(struct3) ;
		_aRosaceStructures.add(struct4) ;
	}
*/
	
/*
	public double getLdvAngleFromCanvasAngle(double canvasAngle) {
		return getCanvasAngleFromLdvAngle(canvasAngle) ;
	}
*/
	
	public LdvTeamRosaceObject hiTest(double x, double y) {
		return _ObjectsList.hiTest(x, y) ;
	}
	
	/**
	 * Add an icon that represents a team member mandate
	 */
	public void addIconForMandate(LdvModelMandatePair mandatePair)
	{
		LdvModelMandate mandate = mandatePair.getMandate() ;
		
		LdvModelMandatePosition mandatePosition = new LdvModelMandatePosition(mandate.getDistance(), mandate.getAngle()) ;
		mandate.setPosition(mandatePosition) ;
		
		LdvTeamRosaceIcon icon = new LdvTeamRosaceIcon(_Canvas, mandatePair, 0) ;
		setIconPosition(icon, 4 * _iBaseRadius, 4 * _iBaseRadius, mandatePosition) ;
		
		_ObjectsList.add(icon) ;
		
		icon.draw() ;
	}
	
	// put the icon the center of the pie
	
	/**
	 * Set the position of an icon
	 * 
	 * @param icon     icon to be set a position
	 * @param centerX  Abscissa of polar reference frame's origin
	 * @param centerY  Ordinate of polar reference frame's origin
	 * @param position position that defines the petal (angle and radius)
	 */
	public void setIconPosition(LdvTeamRosaceIcon icon, final int centerX, final int centerY, final LdvModelMandatePosition position)
	{	
		// Find the Cartesian coordinates that fits the polar coordinates of the petal
		//
		double angle    = TrigonometricFcts.getCanvasRAngleFromLdvDAngle(position.getAngle()) ;
		double distance = position.getDistance() ;
		
		LdvCoordinatesPolar     polar     = new LdvCoordinatesPolar(angle, distance) ;
		LdvCoordinatesCartesian cartesian = new LdvCoordinatesCartesian(polar, new LdvCoordinatesCartesian(centerX, centerY)) ;

		double iconX = cartesian.getX() ;
		double iconY = cartesian.getY() ;
		
		// Get the object for this Cartesian coordinates 
		//
		LdvTeamRosaceObject iconPie = _ObjectsList.hiTest(iconX, iconY) ;
		
		if (null == iconPie)
			return ;
        
		// Get the center position of the pie
		//
		double dLeftAngleR  = iconPie.getLeftAngleCanvasR() ;
		double dRightAngleR = iconPie.getRightAngleCanvasR() ;
		double dRadius      = iconPie.getRadius() - (_iBaseRadius / 2) ;
		
		// In Canvas coordinates, left angle > right angle
		//
		double dIconAngle   = dRightAngleR + (dLeftAngleR - dRightAngleR) / 2 ;
        
		LdvCoordinatesPolar     polarNew     = new LdvCoordinatesPolar(dIconAngle, dRadius) ;
		LdvCoordinatesCartesian cartesianNew = new LdvCoordinatesCartesian(polarNew, new LdvCoordinatesCartesian(centerX, centerY)) ;
        
		icon.setX(cartesianNew.getX()) ;
		icon.setY(cartesianNew.getY()) ;    
	}
	
/*
	public void addMandatePair(LdvModelMandatePair mandatePair) 
	{
		if (null == mandatePair)
			return ;
	
		LdvTeamRosaceIcon Icon = new LdvTeamRosaceIcon(_Canvas, x, y, height) ;
		
		_ObjectsList.add(mandatePair.getMandate().getIcon()) ;
	}
*/
	
	@Override
	public Widget asWidget() {
		return this ;
	}

	@Override
	public void onResize(int width, int height) {
	}

	public LdvTeamRosaceCanvas getCanvasProxy() {
		return _Canvas ;
	}
	
	public Canvas getCanvas() {
		return _CanvasControl ;
	}
	
	public LdvTeamRosaceListObject getListObject(){
		return _ObjectsList ;
	}
	
	/**
	*  Set element ZIndex in CSS
	*  
	* @param iZorder value to be set as ZIndex   
	**/
	@Override
	public void setZorder(int iZorder) {
		this.getElement().getStyle().setZIndex(iZorder) ;
	} 

/*	
	public ArrayList<LdvModelMandatePair> getListMandate() {
		return this.listMandate;
	}
*/
}
