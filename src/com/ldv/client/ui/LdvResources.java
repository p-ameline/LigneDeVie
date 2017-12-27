package com.ldv.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.CssResource.NotStrict;

/**
 * An object that implements this interface may have validation components.
 *
 * @author PA
 *
 */
public interface LdvResources extends ClientBundle 
{
	public static final LdvResources INSTANCE = GWT.create(LdvResources.class) ;

	@NotStrict
  @Source("Ligne_de_Vie.css")
  public CssResource css();
	
	/**
   * An image used for the sliding knob.
   * @return a prototype of this image
   */
  @Source("slider.gif")
  public ImageResource slider() ;
	
  /**
   * An image used for the sliding knob.
   * @return a prototype of this image
   */
	@Source("sliderDisabled.gif")
  public ImageResource sliderDisabled() ;
	
	/**
   * An image used for the sliding knob while sliding.
   * @return a prototype of this image
   */
  @Source("sliderSliding.gif")
  ImageResource sliderSliding() ;

  /**
   * Image used for time zoom
   * @return a prototype of this image
   */
  @Source("ZoomPlus.png")
  ImageResource zoomPlus() ;
  
  @Source("ZoomSmallPlus.png")
  ImageResource zoomSmallPlus() ;
  
  @Source("ZoomNone.png")
  ImageResource zoomNone() ;
  
  @Source("ZoomSmallMinus.png")
  ImageResource zoomSmallMinus() ;
  
  @Source("ZoomMinus.png")
  ImageResource zoomMinus() ;
  
  @Source("NewLine.png")
  ImageResource newLine() ;
  
/*
  @Source("config.xml")
  public TextResource initialConfiguration();

  @Source("manual.pdf")
  public DataResource ownersManual();
*/
}
