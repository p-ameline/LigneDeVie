package com.ldv.client.widgets;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import com.ldv.shared.database.Lexicon;
import com.ldv.shared.model.LdvModelLexicon;

import static com.google.gwt.event.dom.client.KeyCodes.*;

import java.util.ArrayList;
import java.util.Iterator; 

/**
 * TextBox with a drop down list from Lexicon
 * 
 * Inspired from http://sites.google.com/site/gwtcomponents/auto-completiontextbox
 */
public class LexiqueTextBox extends TextBox implements ChangeHandler, HasConceptChangedHandlers
{   
  protected PopupPanel _choicesPopup = new PopupPanel(true) ;
  protected ListBox    _choices      = new ListBox() ;
  
  protected boolean    _popupAdded   = false ;
  protected boolean    _visible      = false ;
  
  protected Lexicon            _selectedLexicon ;
  protected ArrayList<Lexicon> _lexiconList ;
  
  private   String     _sLanguage ;
  
  /**
   * Default Constructor
   *
   */
  public LexiqueTextBox(final String sLanguage)
  {
    super() ;
    
    _sLanguage = sLanguage ;

    // this.setStyleName("AutoCompleteTextBox") ;
       
    _choicesPopup.add(_choices) ;
    _choicesPopup.addStyleName("AutoCompleteChoices") ;
       
    _choices.setStyleName("list") ;
    
    _selectedLexicon = null ;
    _lexiconList     = new ArrayList<Lexicon>() ;
  }

  public boolean isControlKey(KeyUpEvent event)
  {
  	if ((event.isDownArrow()) || (event.isUpArrow()))
  		return true ;
  	
  	int iNativeKeyCode = event.getNativeKeyCode() ;
  	if ((KEY_ENTER == iNativeKeyCode) || (KEY_ESCAPE == iNativeKeyCode))
  		return true ;
  	
  	return false ;
  }
  
  /**
   * A key was released, process special keys
   * 
   * @return <code>true</code> if the list must be refreshed, <code>false</code> if not
   *  
   */
  public boolean processKeyUp(KeyUpEvent event)
  {
    if (event.isDownArrow())
    {
    	// Get getSelectedIndex() returns 0 for first element
    	//
      int iSelectedIndex = _choices.getSelectedIndex() ;
      iSelectedIndex++ ;
      
      if (iSelectedIndex >= _choices.getItemCount())
      	iSelectedIndex = 0 ;

      _choices.setSelectedIndex(iSelectedIndex) ;
           
      return false ;
    }
       
    if (event.isUpArrow())
    {
      int iSelectedIndex = _choices.getSelectedIndex() ;
      iSelectedIndex-- ;
      
      if (iSelectedIndex < 0)
      	iSelectedIndex = _choices.getItemCount() ;
    
      _choices.setSelectedIndex(iSelectedIndex) ;
           
      return false ;        
    }
       
    int iNativeKeyCode = event.getNativeKeyCode() ;
    
    if (KEY_ENTER == iNativeKeyCode)
    {
      if (_visible)
      {
        complete() ;
      }
           
      return false ;
    }
       
    if (KEY_ESCAPE == iNativeKeyCode)
    {
    	closeChoices()  ;
      _visible = false ;
           
      return false ;
    }
    
    _selectedLexicon = null ;
    
    return true ;
       
/*
    String text = this.getText();
    String[] matches = new String[]{};
    if(text.length() > 0)
    {
      // matches = items.getCompletionItems(text);
    }
       
    if (matches.length > 0)
    {
      _choices.clear();
           
      for(int i = 0; i < matches.length; i++)
      {
        _choices.addItem((String) matches[i]);
      }
           
      // if there is only one match and it is what is in the
      // text field anyways there is no need to show autocompletion
      if (matches.length == 1 && matches[0].compareTo(text) == 0)
      {
        _choicesPopup.hide();
      } 
      else 
      {
        _choices.setSelectedIndex(0);
        _choices.setVisibleItemCount(matches.length + 1);
               
        if (!_popupAdded)
        {
          RootPanel.get().add(_choicesPopup);
          _popupAdded = true;
        }
        _choicesPopup.show();
        _visible = true;
        _choicesPopup.setPopupPosition(this.getAbsoluteLeft(),
        this.getAbsoluteTop() + this.getOffsetHeight());
        //choicesPopup.setWidth(this.getOffsetWidth() + "px");
        _choices.setWidth(this.getOffsetWidth() + "px");
      }

    } else {
      _visible = false;
      _choicesPopup.hide();
    }
*/
  }

  /**
   * A mouseclick in the list of items
   */
  public void onChange(Widget arg0) {
    complete();
  }
 
  public void onClick(Widget arg0) {
    complete();
  }
   
  // add selected item to textbox
  public void complete()
  {
    if (_choices.getItemCount() > 0)
    {
    	int iSelectedIndex = _choices.getSelectedIndex() ;
    	
    	if ((iSelectedIndex >= 0) && (iSelectedIndex < _choices.getItemCount()))
    	{
    		String sCode = _choices.getValue(iSelectedIndex) ;
    		
    		Lexicon lexicon = getLexiconFromCode(sCode) ;
    		if (null != lexicon)
    		{
    			_selectedLexicon = new Lexicon(lexicon) ;
    			setText(lexicon.getLabel(Lexicon.LabelType.displayLabel, Lexicon.Declination.nullDeclination, _sLanguage)) ;
    		}
    	}
    }
       
    closeChoices() ;
    
    ConceptChangedEvent event = new ConceptChangedEvent() ;
    fireEvent(event) ;
  }

  public Lexicon getSelected() {
  	return _selectedLexicon ;
  }
  
	@Override
  public void onChange(ChangeEvent event) {
		complete() ;
  }
	
	public void clearList()
	{
		_choices.clear() ;
		_lexiconList.clear() ;
	}
	
	/**
   * Add a new lexicon entry in the list of proposed choices
   */
	public void addChoice(final LdvModelLexicon model) 
	{
		if (null == model)
			return ;
		
		Lexicon lexicon = new Lexicon(model) ;
		String sLabel = lexicon.getLabel(Lexicon.LabelType.selectionLabel, Lexicon.Declination.nullDeclination, _sLanguage) ;
		
		_choices.addItem(sLabel, model.getCode()) ;
		
		_lexiconList.add(lexicon) ;
	}
	
	protected Lexicon getLexiconFromCode(final String sCode)
	{
		if (_lexiconList.isEmpty() || (null == sCode) || "".equals(sCode))
			return null ;
		
		for (Iterator<Lexicon> it = _lexiconList.iterator() ; it.hasNext() ; )
		{
			Lexicon lexicon = it.next() ;
			if (sCode.equals(lexicon.getCode()))
				return lexicon ;
		}
		
		return null ;
	}

	public void showPopup()
	{
    if (!_popupAdded)
    {
      RootPanel.get().add(_choicesPopup);
      _popupAdded = true;
    }
    
    _choicesPopup.setPopupPosition(this.getAbsoluteLeft(), this.getAbsoluteTop() + this.getOffsetHeight()) ;
        //choicesPopup.setWidth(this.getOffsetWidth() + "px");
    // _choices.setWidth(this.getOffsetWidth() + "px") ;
    
    _choicesPopup.show() ;
    _visible = true ;
    
    int iNbVisibleItems = _choices.getItemCount() ;
    if (iNbVisibleItems > 10)
    	iNbVisibleItems = 10 ;
    
    _choices.setSelectedIndex(0) ;
    _choices.setVisibleItemCount(iNbVisibleItems) ;
	}
	
	public void hidePopup()
	{
		_choicesPopup.hide() ;
    _visible = false ;
	}
	
	public ListBox getListBox() {
		return _choices ;
	}
	
	public String getLanguage() {
		return _sLanguage ;
	}
	public void setLanguage(final String sLanguage) {
		_sLanguage = sLanguage ;
	}
	
	/**
   * Close choices component, clearing list and hiding PopupPanel  
   *
   */
	public void closeChoices() 
	{
		_choices.clear() ;
    _choicesPopup.hide() ;
	}
	
	@Override
	public HandlerRegistration addConceptChangedHandler(ConceptChangedHandler handler) {
		return addHandler(handler, ConceptChangedEvent.getType()) ;
	}
}
