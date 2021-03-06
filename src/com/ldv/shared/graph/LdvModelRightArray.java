package com.ldv.shared.graph;

import java.util.Iterator;
import java.util.Vector;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Vector of LdvModelRight objects
 * 
 * @author Philippe
 */
public class LdvModelRightArray extends Vector<LdvModelRight> implements IsSerializable
{
	/**
   * 
   */
  private static final long serialVersionUID = 7568294601599378479L;

	public LdvModelRightArray() { 
		super() ;
	}
	
	/**
	 * Copy constructor  
	 * 
	 * @param source Model rights array
	 */
	public LdvModelRightArray(final LdvModelRightArray source) {
		initFromModelRightArray(source) ;
	}
	
	/**
	 * Initialize from a model
	 */
	public void initFromModelRightArray(final LdvModelRightArray source) 
	{
		clear() ;
		
		if ((null == source) || source.isEmpty())
			return ;
		
		for (Iterator<LdvModelRight> itr = source.iterator() ; itr.hasNext() ; )
			add(new LdvModelRight(itr.next())) ;
	}
	
	/**
	 * Get the rights for a given node
	 * 
	 * @param sNodeId Id of node to get rights for
	 * 
	 * @return A vector of LdvModelRight (that may be empty)
	 */
	public Vector<LdvModelRight> getRightForNode(final String sNodeId)
	{
		Vector<LdvModelRight> aRights = new Vector<LdvModelRight>() ;
		
		Iterator<LdvModelRight> itr = this.iterator() ; 
		while (itr.hasNext()) 
		{
			LdvModelRight nodeRight = itr.next() ; 
			if (sNodeId.equals(nodeRight.getNode()))
				aRights.add(nodeRight) ;
		} 
		
		return aRights ;
	}

	/**
	 * Get the right that fits a model 
	 * 
	 * @param model Model of the right we are looking for
	 * 
	 * @return A LdvModelRight object if found, <code>null</code> if not
	 */
	public LdvModelRight getRightForModel(final LdvModelRight model)
	{
		if (null == model)
			return null ;
		
		for (Iterator<LdvModelRight> itr = this.iterator() ; itr.hasNext() ; ) 
		{
			LdvModelRight nodeRight = itr.next() ; 
			if (model.equals(nodeRight))
				return nodeRight ;
		} 
		
		return null ;
	}
	
	/**
	 * Add a LdvModelRight to the vector
	 * 
	 * @param right object to be added
	 */
	public void addRight(final LdvModelRight right) {
		add(new LdvModelRight(right)) ;
	}
	
	/**
	 * Remove all rights information for a given document  
	 * 
	 * @param sDocId Document Id
	 */
	public void RemoveDocument(String sDocId)
	{
		if (isEmpty())
			return ;
		
		int iDocIdLen = sDocId.length() ;
		
		for (int index = 0 ; index < size() ; )
		{
			LdvModelRight right = elementAt(index) ;
			
			String sNodeId = right.getNode() ;
			if ((sNodeId.length() >= iDocIdLen) && (sDocId.equals(sNodeId.substring(0, iDocIdLen))))
				this.remove(index) ;
			else
				index++ ;
		}			
	}
	
	/**
	 * Set the rights for a node Id (either by editing it if present or adding it)  
	 * 
	 * @param sNodeId Node Id to set rights for
	 * @param sRight  Rights to set
	 * 
	 **/
	public void set(String sNodeId, String sRight)
	{
		// If we find a node with this Id, we set its rights
		//
		if (false == isEmpty())
		{
			for (Iterator<LdvModelRight> itr = iterator() ; itr.hasNext() ; )
			{
				LdvModelRight nodeRight = itr.next() ;
				if (sNodeId.equals(nodeRight.getNode()))
				{
					nodeRight.setRight(sRight) ;
					return ;
				}
			}
		}
		
		// If still there, it means no such node Id was found, so we add it
		//
		add(new LdvModelRight(sNodeId, sRight)) ;			
	}
}
