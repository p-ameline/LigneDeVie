package com.ldv.client.widgets;

import java.util.ArrayList;
import java.util.Iterator;

import com.ldv.client.widgets.LexiqueTextBox;
import com.ldv.shared.model.LdvModelLexicon;

/**
 * Object used to reference a LexiqueTextBox in order to benefit from asynchronous connection to
 * the Lexicon server: Requests are sent with requesting TextBox's index so that answer can be used
 * to populate proper LexiqueTextBox  
 * 
 * Inspired from http://sites.google.com/site/gwtcomponents/auto-completiontextbox
 */
public class LexiqueTextBoxIndex
{
	private LexiqueTextBox _lexiqueTextBox ;
	private int            _iIndex ;
	private int            _iInstanceCounter ;
		
  public LexiqueTextBoxIndex(LexiqueTextBox lexiqTxtBx, int iIndex, final String sLanguage)
  {
    _lexiqueTextBox   = lexiqTxtBx ;
    _lexiqueTextBox.setLanguage(sLanguage) ;
    
    _iIndex           = iIndex ;
    _iInstanceCounter = 1 ;
  }

	public LexiqueTextBox getLexiqueTextBox() {
		return _lexiqueTextBox ;
	}
	public void setLexiqueTextBox(final LexiqueTextBox lexiqueTextBox) {
		_lexiqueTextBox = lexiqueTextBox ;
	}

	/**
   * Initialize a LexiqueTextBox from a list of lexicon entries
   */
	public void initLexiqueTextBox(final ArrayList<LdvModelLexicon> entriesList)
	{
		if (null == entriesList)
			return ;
		
		_lexiqueTextBox.clearList() ;
		
		if (entriesList.isEmpty())
		{
			_lexiqueTextBox.hidePopup() ;
			return ;
		}
		
		for (Iterator<LdvModelLexicon> it = entriesList.iterator() ; it.hasNext() ; )
			_lexiqueTextBox.addChoice(it.next()) ;
		
		_lexiqueTextBox.showPopup() ;
	}
	
	public int getIndex() {
		return _iIndex ;
	}
	public void setIndex(int iIndex) {
		_iIndex = iIndex ;
	}
		
	public String getLanguage() {
		return _lexiqueTextBox.getLanguage() ;
	}
	public void setLanguage(final String sLanguage) {
		_lexiqueTextBox.setLanguage(sLanguage) ;
	}
	
	public void incrementInstanceCounter() {
		_iInstanceCounter++ ;
	}
		
	public void decrementInstanceCounter() {
		_iInstanceCounter-- ;
	}
		
	public boolean stillExists() {
		return (_iInstanceCounter > 0) ;
	}
}
