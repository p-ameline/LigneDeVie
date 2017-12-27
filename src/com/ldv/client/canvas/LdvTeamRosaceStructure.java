package com.ldv.client.canvas;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;

import com.ldv.client.model.LdvModelRosacePetal;
import com.ldv.client.model.LdvModelRosacePetalSegment;

/** 
 * A LdvTeamRosaceStructure represents an angular sector, a given category team members (whatever their distance)
 * 
 */
public class LdvTeamRosaceStructure 
{
	private int    _iRosaceAngleLdvD ;
	
	private String _sLabel ;
	
	private int    _iRadiusMin ;
	private int    _iRadiusMax ;
	
	private ArrayList<LdvTeamRosacePetalDescriptor> _vPetals ;
	
	/** 
	 * Default constructor 
	 */
	public LdvTeamRosaceStructure(int iRosaceAngleLdvD, String label, int radiusMin, int radiusMax)
	{	
		_iRosaceAngleLdvD = iRosaceAngleLdvD ;
		_sLabel           = label ;
		_iRadiusMin       = radiusMin ;
		_iRadiusMax       = radiusMax ;				
		_vPetals          = new ArrayList<LdvTeamRosacePetalDescriptor>() ;
	}
	
	/** 
	 * Constructor from a LdvModelRosacePetal object
	 * 
	 * @param petal object to initialize this object from 
	 */
	public LdvTeamRosaceStructure(LdvModelRosacePetal petal)
	{
		_vPetals = new ArrayList<LdvTeamRosacePetalDescriptor>() ;
		
		reset() ;
		
		if (null == petal)
			return ;
		
		_iRosaceAngleLdvD = petal.getAngle() ;
		_sLabel           = petal.getLabel() ;
		
		Vector<LdvModelRosacePetalSegment> segments = petal.getSegments() ;
		if ((null == segments) || segments.isEmpty())
			return ;
		
		int iMaxRadius = Integer.MIN_VALUE ;
		int iMinRadius = Integer.MAX_VALUE ;
		
		for (Iterator<LdvModelRosacePetalSegment> iter = segments.iterator() ; iter.hasNext() ; ) 
		{
			LdvModelRosacePetalSegment segment = iter.next() ;
			
			int iSegmRadius = segment.getRadius() ; 
			if (iSegmRadius > iMaxRadius)
				iMaxRadius = iSegmRadius ;
			if (iSegmRadius < iMinRadius)
				iMinRadius = iSegmRadius ;
			
			LdvTeamRosacePetalDescriptor segmStruct = new LdvTeamRosacePetalDescriptor(this, petal, segment) ;
			_vPetals.add(segmStruct) ;
		}
		
		_iRadiusMin = iMinRadius ;
		_iRadiusMax = iMaxRadius ;
	}

	void reset()
	{	
		_iRosaceAngleLdvD = -1 ;
		_sLabel           = "" ;
		_iRadiusMin       = -1 ;
		_iRadiusMax       = -1 ;
		
		_vPetals.clear() ;
	}
	
	public boolean isCenter() {
		return (0 == _iRadiusMin) && (0 == _iRadiusMax) ;
	}
	
	public int getRosaceAngleLdvD() {
		return _iRosaceAngleLdvD ;
	}
	public void setRosaceAngleLdvD(int angle) {
		_iRosaceAngleLdvD = angle ;
	}
	
	public String getLabel() {
		return _sLabel ;
	}
	public void setLabel(String label) {
		_sLabel = label ;
	}

	public int getRadiusMin() {
		return _iRadiusMin ;
	}
	public void setRadiusMin(int radiusMin) {
		_iRadiusMin = radiusMin ;
	}

	public int getRadiusMax() {
		return _iRadiusMax ;
	}
	public void setRadiusMax(int radiusMax) {
		_iRadiusMax = radiusMax ;
	}
	
	public LdvTeamRosacePetalDescriptor getPetalDescriptor(int iRadius)
	{
		if (_vPetals.isEmpty())
			return null ;
		
		Iterator<LdvTeamRosacePetalDescriptor> it = _vPetals.iterator() ;
		while(it.hasNext())
		{
			LdvTeamRosacePetalDescriptor currentPetal = it.next() ;
			if (currentPetal.getRadius() == iRadius)
				return currentPetal ;
		}
		
		return null ;
	}
	
	public ArrayList<LdvTeamRosacePetalDescriptor> getPetals() {
		return _vPetals ;
	}	
}
