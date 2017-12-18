package com.ldv.client.canvas;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

public class LdvTeamRosaceListObject extends ArrayList<LdvTeamRosaceObject>
{
	//private LdvListObject listObject;
	
	/**
   * 
   */
  private static final long serialVersionUID = 7308560831984862770L;

	public LdvTeamRosaceListObject() {
	}

	public void sort()
	{
		if (isEmpty())
			return ;
		
		Comparator<LdvTeamRosaceObject> orderHeight = new Comparator<LdvTeamRosaceObject>(){
		    public int compare(LdvTeamRosaceObject o1, LdvTeamRosaceObject o2) {
		    	return (o1.getHeight() - o2.getHeight()) ;
		    }
		};
		
		//sort the height of the pie
		Collections.sort(this, orderHeight) ;
	}
	
	public void reverseSort()
	{	
		if (isEmpty())
			return ;
		
		Comparator<LdvTeamRosaceObject> orderHeight = new Comparator<LdvTeamRosaceObject>() {
		    public int compare(LdvTeamRosaceObject o1, LdvTeamRosaceObject o2) {
		    	return (o2.getHeight() - o1.getHeight()) ;
		    }
		};
		
		//sort the height of the pie
		Collections.sort(this, orderHeight) ;
	}
	
	public void draw()
	{
		if (isEmpty())
			return ;
		
		sort() ;
		
		Iterator<LdvTeamRosaceObject> it = this.iterator() ;
		while (it.hasNext())
			it.next().draw() ;
	}
	
	public LdvTeamRosaceObject hiTest(double x, double y)
	{	
		if (isEmpty())
			return null ;
		
		reverseSort() ;
		
		Iterator<LdvTeamRosaceObject> it = this.iterator() ;
		while (it.hasNext())
		{
			LdvTeamRosaceObject object = it.next() ;
			if (object.contains(x,y))
				return object ;
		}
		
		return null ;
	}
	
	public void removeObjectsByName(String sName)
	{	
		if (isEmpty())
			return ;
		
		Iterator<LdvTeamRosaceObject> it = this.iterator() ;
		while (it.hasNext())
		{
			LdvTeamRosaceObject object = it.next() ;
			if (sName.equalsIgnoreCase(object.getName()))
				remove(object) ;
		}
	}

	public int getMaxHeight()
	{
		if (isEmpty())
			return 0 ;
		
		reverseSort() ;
		return this.get(0).getHeight() ;		
	}
}
