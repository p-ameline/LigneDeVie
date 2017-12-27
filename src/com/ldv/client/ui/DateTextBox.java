package com.ldv.client.ui;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DatePicker;

import com.ldv.shared.model.LdvTime;

import static com.google.gwt.event.dom.client.KeyCodes.*;

import java.util.Date; 

// Inspired from http://sites.google.com/site/gwtcomponents/auto-completiontextbox
//
public class DateTextBox extends TextBox implements KeyUpHandler, ChangeHandler, HasDateChangedHandlers
{   
  protected PopupPanel _choicesPopup = new PopupPanel(true) ;
  protected DatePicker _choices      = new DatePicker() ;
  protected boolean    _popupAdded   = false ;
  protected boolean    _visible      = false ;
  
  private   LdvTime    _tDate        = new LdvTime(0) ;
   
  /**
   * Default Constructor
   *
   */
  public DateTextBox()
  {
    super();

    _choicesPopup.add(_choices) ;
    _choicesPopup.addStyleName("AutoCompleteChoices") ;
    
    connectLocalEvents() ;
  }
  
  private void connectLocalEvents()
  {
  	_choices.addValueChangeHandler(new ValueChangeHandler<Date>() {
  		public void onValueChange(ValueChangeEvent<Date> event) 
  		{
  			Date date = event.getValue() ;
  			_tDate.initFromJavaDate(date) ;
  		}
  	});
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
   * A key was released, start autocompletion
   */
  @Override
  public void onKeyUp(KeyUpEvent event)
  {
/*
    if (event.isDownArrow())
    {
      int selectedIndex = _choices.getSelectedIndex() ;
      selectedIndex++ ;
      if(selectedIndex > _choices.getItemCount())
      {
        selectedIndex = 0 ;
      }
      _choices.setSelectedIndex(selectedIndex) ;
           
      return ;
    }
       
    if (event.isUpArrow())
    {
      int selectedIndex = _choices.getSelectedIndex() ;
      selectedIndex-- ;
      if(selectedIndex < 0)
      {
        selectedIndex = _choices.getItemCount() ;
      }
      _choices.setSelectedIndex(selectedIndex) ;
           
      return ;        
    }
       
    int iNativeKeyCode = event.getNativeKeyCode() ;
    
    if (KEY_ENTER == iNativeKeyCode)
    {
      if (_visible)
      {
        complete() ;
      }
           
      return;
    }
       
    if (KEY_ESCAPE == iNativeKeyCode)
    {
      _choices.clear() ;
      _choicesPopup.hide() ;
      _visible = false ;
           
      return;
    }
       
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


  public void onClick(Widget arg0) {
  	_choicesPopup.show() ;
  }
   
  // add selected item to textbox
  public void complete()
  {
  	hidePopup() ;
  	
  	setText(_tDate.getLocalSimpleDate()) ;
    
    DateChangedEvent event = new DateChangedEvent() ;
    fireEvent(event) ;
  }

	@Override
  public void onChange(ChangeEvent event)
  {
		complete() ;
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
	}
	
	public void hidePopup()
	{
		_choicesPopup.hide() ;
    _visible = false ;
	}
	
/*
	public ListBox getListBox() {
		return _choices ;
	}
*/
	
	@Override
	public HandlerRegistration addDateChangedHandler(DateChangedHandler handler) {
		return addHandler(handler, DateChangedEvent.getType()) ;
	}
}
