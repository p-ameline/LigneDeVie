package com.ldv.shared.graph;

import java.util.Iterator;
import java.util.Vector;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Simply a vector of LdvModelModel
 * 
 * @author PA
 */
public class LdvModelModelArray extends Vector<LdvModelModel> implements IsSerializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6489490271615835066L;

	/**
	 * Default constructor
	 */
	public LdvModelModelArray() { 
		super() ;
	}
	
	/**
	 * Copy constructor  
	 * 
	 * @param source Models array
	 * 
	 **/
	public LdvModelModelArray(final LdvModelModelArray source) {
		initFromModelModelArray(source) ;
	}
	
	/**
	 * Initialize from a model
	 * 
	 * @param model LdvModelModelArray object to initialize from
	 */
	public void initFromModelModelArray(final LdvModelModelArray model) 
	{
		clear() ;
		
		if ((null == model) || model.isEmpty())
			return ;
		
		for (Iterator<LdvModelModel> itr = model.iterator() ; itr.hasNext() ; )
			add(new LdvModelModel(itr.next())) ;
	}
	
	/**
	 * Return a vector of models for a given node
	 * 
	 * @param sDocumentId Document (tree for a given person/object) that contains the node 
	 * @param sNodeId     Node Id inside the tree
	 * 
	 * @return a vector of LdvModelModel (that may be empty if no model was found)
	 */ 
	public Vector<LdvModelModel> getModelForNode(final String sDocumentId, final String sNodeId)
	{
		Vector<LdvModelModel> aModels = new Vector<LdvModelModel>() ;
		
		Iterator<LdvModelModel> itr = this.iterator() ; 
		while (itr.hasNext()) 
		{
			LdvModelModel nodeModel = itr.next() ; 
			if (sDocumentId.equals(nodeModel.getObject()) && sNodeId.equals(nodeModel.getNode()))
				aModels.add(nodeModel) ;
		} 
		
		return aModels ;
	}
	
	/**
	 * Add a LdvModelRight to the vector
	 * 
	 * @param right object to be added
	 */
	public void addModel(LdvModelModel model) {
		add(new LdvModelModel(model)) ;
	}
	
	/**
	 * Set the rights for a node Id (either by editing it if present or adding it)  
	 * 
	 * @param sFullNodeId Full node Id (document + node)
	 * @param sType       Type of model
	 * @param sModel      Model Id
	 */
	public void set(final String sFullNodeId, final LdvModelModel.MODEL_TYPE iType, final String sModel)
	{
		String sTreeId = LdvGraphTools.getNodeTreeId(sFullNodeId) ;
		String sNodeId = LdvGraphTools.getNodeNodeId(sFullNodeId) ;
		
		set(sTreeId, sNodeId, iType, sModel) ;
	}
	
	/**
	 * Set the rights for a node Id (either by editing it if present or adding it)  
	 * 
	 * @param sDocumentId Document (tree for a person or object) that contains the node 
	 * @param sNodeId     Node Id inside the tree
	 * @param sType       Type of model
	 * @param sModel      Model Id
	 */
	public void set(final String sDocumentId, final String sNodeId, final LdvModelModel.MODEL_TYPE iType, final String sModel)
	{
		// If we find a node with this Id, we set its rights
		//
		if (false == isEmpty())
		{
			for (Iterator<LdvModelModel> itr = iterator() ; itr.hasNext() ; )
			{
				LdvModelModel nodeModel = itr.next() ;
				if (sDocumentId.equals(nodeModel.getObject()) && sNodeId.equals(nodeModel.getNode()))
				{
					nodeModel.setType(iType) ;
					nodeModel.setModel(sModel) ;
					return ;
				}
			}
		}
		
		// If still there, it means no such node Id was found, so we add it
		//
		add(new LdvModelModel(sDocumentId, sNodeId, iType, sModel)) ;			
	}
	
	/**
	 * Remove all models for a given document  
	 * 
	 * @param sDocId Document Id
	 */
	public void RemoveDocument(String sDocId)
	{
		if (isEmpty())
			return ;
		
		for (int index = 0 ; index < size() ; )
		{
			LdvModelModel model = elementAt(index) ;
			
			if (sDocId.equals(model.getObject()))
				this.remove(index) ;
			else
				index++ ;
		}			
	}
}
