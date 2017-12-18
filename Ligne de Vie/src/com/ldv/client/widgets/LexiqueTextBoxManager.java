package com.ldv.client.widgets;

import java.util.ArrayList;
import java.util.Iterator;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.user.client.rpc.AsyncCallback;

import com.ldv.client.widgets.LexiqueTextBox;
import com.ldv.shared.model.LdvModelLexicon;
import com.ldv.shared.rpc4ontology.GetLexiconListInfo;
import com.ldv.shared.rpc4ontology.GetLexiconListResult;

import net.customware.gwt.dispatch.client.DispatchAsync;

/**
 * Object used to operate an array of LexiqueTextBoxIndex  
 * 
 */
public class LexiqueTextBoxManager
{
	protected ArrayList<LexiqueTextBoxIndex> _aLexiqueTextBoxBuffer ;
	protected int                            _iNextLTBBufferIndex ;
		
  public LexiqueTextBoxManager()
  {
  	_aLexiqueTextBoxBuffer = new ArrayList<LexiqueTextBoxIndex>() ;
  	_iNextLTBBufferIndex   = 0 ;
  }
  
  /**
   * Initializes a LexiqueTextBox choices ListBox from entered text 
   *
   * @param  lexiqueTextBox LexiqueTextBox which choices Listbox is to be updated
	 * @return <code>void</code> 
   */
	public void initLexiqueBoxList(final LexiqueTextBox lexiqueTextBox, final String sUserLdvId, final DispatchAsync dispatcher)
	{
		if (null == lexiqueTextBox)
			return ;
		
		// Get text
		//
		String sText = lexiqueTextBox.getText() ;
		
		if (sText.equals(""))
		{
			lexiqueTextBox.closeChoices() ;
			return ;
		}
		
		// Get index
		//
		LexiqueTextBoxIndex index = getLexiqueTextBoxIndex(lexiqueTextBox) ;
		if (null == index)
			return ;
		
		dispatcher.execute(new GetLexiconListInfo(sUserLdvId, index.getLanguage(), sText, index.getIndex()), new GetLexiqueBoxListCallback()) ;
	}
	
	/**
	*  Asynchronous callback function for calls to GetLexiconFromCodeHandler
	**/
	public class GetLexiqueBoxListCallback implements AsyncCallback<GetLexiconListResult> 
	{
		public GetLexiqueBoxListCallback() {
			super();
		}

		@Override
		public void onFailure(final Throwable cause)
		{
			Log.error("Handle Failure:", cause) ;    				
		}

		@Override
		public void onSuccess(final GetLexiconListResult result)
		{
			if (null == result)
				return ;
			
			ArrayList<LdvModelLexicon> entriesList = result.getLexiconArray() ;
			if ((null == entriesList) || entriesList.isEmpty())
				return ;
			
			LexiqueTextBoxIndex lexiqueBoxIndx = getLexiqueTextBox(result.getLexiconTextBoxIndex()) ;
			if (null == lexiqueBoxIndx)
				return ;
			
			lexiqueBoxIndx.initLexiqueTextBox(entriesList) ;			
		}
	}

  /**
   * Reference a new lexiqueTextBox (by creating a new dedicated index) and returns its index number   
   * 
   * @param lexiqueTextBox The LexiqueTextBox to reference
   * @param sUserLanguage  The user language to be used with this LexiqueTextBox
   * 
   * @return The index number the LexiqueTextBox was referenced with
   */
  public int addLexiqueTextBoxToBuffer(final LexiqueTextBox lexiqueTextBox, final String sUserLanguage)
	{
		int iAlreadyExistingIndex = getIndex(lexiqueTextBox) ;
		if (-1 != iAlreadyExistingIndex)
			return iAlreadyExistingIndex ;
		
		int iNewIndex = _iNextLTBBufferIndex++ ;
		
		_aLexiqueTextBoxBuffer.add(new LexiqueTextBoxIndex(lexiqueTextBox, iNewIndex, sUserLanguage)) ;
		
		return iNewIndex ;
	}
	
	/**
   * Find the index for a given LexiqueTextBox stored in the buffer  
   *
   * @param  lexiqueTextBox LexiqueTextBox to look for in buffer
	 * @return the index for this LexiqueTextBox if found, <code>-1</code> if not 
   */
	public int getIndex(final LexiqueTextBox lexiqueTextBox)
	{
		if (_aLexiqueTextBoxBuffer.isEmpty() || (null == lexiqueTextBox))
			return -1 ;
		
		for (Iterator<LexiqueTextBoxIndex> it = _aLexiqueTextBoxBuffer.iterator() ; it.hasNext() ; )
		{
			LexiqueTextBoxIndex index = it.next() ;
			if (lexiqueTextBox == index.getLexiqueTextBox())
				return index.getIndex() ;
		}
		
		return -1 ;
	}
	
	/**
   * Find the index for a given LexiqueTextBox stored in the buffer  
   *
   * @param  lexiqueTextBox LexiqueTextBox to look for in buffer
	 * @return the index for this LexiqueTextBox if found, <code>-1</code> if not 
   */
	protected LexiqueTextBoxIndex getLexiqueTextBoxIndex(final LexiqueTextBox lexiqueTextBox)
	{
		if (_aLexiqueTextBoxBuffer.isEmpty() || (null == lexiqueTextBox))
			return null ;
		
		for (Iterator<LexiqueTextBoxIndex> it = _aLexiqueTextBoxBuffer.iterator() ; it.hasNext() ; )
		{
			LexiqueTextBoxIndex index = it.next() ;
			if (lexiqueTextBox == index.getLexiqueTextBox())
				return index ;
		}
		
		return null ;
	}
	
	/**
   * Find the lexiqueTextBoxIndex for a given index  
   *
   * @param  iIndex Index of lexiqueTextBoxIndex to look for in buffer
	 * @return a lexiqueTextBoxIndex if found, <code>null</code> if not 
   */
	public LexiqueTextBoxIndex getLexiqueTextBox(int iIndex)
	{
		if (_aLexiqueTextBoxBuffer.isEmpty())
			return (LexiqueTextBoxIndex) null ;
		
		for (Iterator<LexiqueTextBoxIndex> it = _aLexiqueTextBoxBuffer.iterator() ; it.hasNext() ; )
		{
			LexiqueTextBoxIndex lexiqueTxtBx = it.next() ;
			if (iIndex == lexiqueTxtBx.getIndex())
				return lexiqueTxtBx ;
		}
		
		return (LexiqueTextBoxIndex) null ;
	}
	
	/**
   * Increment instance counter for a given index  
   *
   * @param  iIndex index of lexiqueTextBoxIndex whose instance counter is to be incremented 
   */
	public void incrementLexiqueTextBoxIndex(int iIndex)
	{
		LexiqueTextBoxIndex lexiqueTxtBxIdx = getLexiqueTextBox(iIndex) ;
		if (null == lexiqueTxtBxIdx)
			return ;
		
		lexiqueTxtBxIdx.incrementInstanceCounter() ;
	}
	
	/**
   * Decrement instance counter for a given index ; if counter becomes null then remove object  
   *
   * @param  iIndex index of lexiqueTextBoxIndex whose instance counter is to be incremented 
   */
	public void decrementLexiqueTextBoxIndex(int iIndex)
	{
		LexiqueTextBoxIndex lexiqueTxtBxIdx = getLexiqueTextBox(iIndex) ;
		if (null == lexiqueTxtBxIdx)
			return ;
		
		lexiqueTxtBxIdx.decrementInstanceCounter() ;
		
		if (lexiqueTxtBxIdx.stillExists())
			return ;
			
		for (Iterator<LexiqueTextBoxIndex> it = _aLexiqueTextBoxBuffer.iterator() ; it.hasNext() ; )
		{
			LexiqueTextBoxIndex lexiqueTxtBx = it.next() ;
			if (iIndex == lexiqueTxtBx.getIndex())
			{
				_aLexiqueTextBoxBuffer.remove(it) ;
				return ;
			}
		}
	}
}
