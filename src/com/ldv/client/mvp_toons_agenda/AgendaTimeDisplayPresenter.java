package com.ldv.client.mvp_toons_agenda;

import com.allen_sauer.gwt.log.client.Log;

import com.google.gwt.user.client.ui.Panel;
import com.google.inject.Inject;

import com.ldv.client.event.AgendaDayControllerInitEvent;
import com.ldv.client.event.AgendaDayControllerInitEventHandler;
import com.ldv.client.util.LdvSupervisor;

import net.customware.gwt.presenter.client.EventBus;
import net.customware.gwt.presenter.client.widget.WidgetDisplay;
import net.customware.gwt.presenter.client.widget.WidgetPresenter;

/**
 * AgendaTimeDisplayPresenter is in charge of drawing the day reference information (display hours) and provide all
 * days it controls with space/time information (what y coordinate corresponds to a given time)
 */
public class AgendaTimeDisplayPresenter extends WidgetPresenter<AgendaTimeDisplayPresenter.Display>
{
	public interface Display extends WidgetDisplay 
	{
		public int getHourSpanInPixel() ;
	}

	private final LdvSupervisor _supervisor ;
	
	@Inject
	public AgendaTimeDisplayPresenter(final Display display, EventBus eventBus, final LdvSupervisor supervisor) 
	{	
		super(display, eventBus);
		
		Log.info("entering constructor of LdvTimeDisplayPresenter.") ;
		
		_supervisor = supervisor ;
		
		bind() ;
	}
	  
	@Override
	protected void onBind() 
	{
		/**
		 * Event received when it is time to start
		 */
		eventBus.addHandler(AgendaDayControllerInitEvent.TYPE, new AgendaDayControllerInitEventHandler() 
		{
			@Override
			public void onInitAgendaDayController(AgendaDayControllerInitEvent event) 
			{
				Log.info("Handling LdvTimeDisplaySentEvent event") ;
				
				// event.getWorkspace().clear() ;
				Panel WelcomeWorkspace = event.getHostPanel() ;
				WelcomeWorkspace.add(display.asWidget()) ;
			}
		});
	}
		
	/**
	 * Get the number of pixels corresponding to a time in hours:minutes
	 * 
	 * @param iHours   Number of hours from 00:00
	 * @param iMinutes Number of minutes in this hour
	 * 
	 * @return Number of corresponding pixels as int
	 */
	public int getPhysicalTopFromTime(int iHours, int iMinutes) {
		return getPhysicalTopFromMinute(iHours * 60 + iMinutes) ; 
	}
	
	/**
	 * Get the number of pixels corresponding to a time in minutes
	 * 
	 * @param iMinutes Number of minutes from 00:00
	 * 
	 * @return Number of corresponding pixels as int
	 */
	public int getPhysicalTopFromMinute(int iMinutes)
	{
		double dHours = getHourAsDouble(iMinutes) ;
		double dPixels = dHours * display.getHourSpanInPixel() ;
		
		double dPixToReturn = Math.floor(dPixels) ;
		if (dPixels - dPixToReturn > 0.5)
			dPixToReturn++ ;
		
		return (int) dPixToReturn ; 
	}
	
	/**
	 * Get the number of pixels corresponding to a number of minutes
	 * 
	 * @param iTimeUnitCount Number of time units as int (i.e. hours, days... depending to zoom level)
	 * @return Number of corresponding pixels as int
	 */
	public double getHourAsDouble(int iMinutes)
	{
		double dHourAsDouble = iMinutes / 60 ;
		double dHours = Math.floor(dHourAsDouble) ;
		
		double dRemainingMinutes = iMinutes - (dHours * 60) ;
		double dRMasFraction = dRemainingMinutes / 60 ; 
		
		return dHours + dRMasFraction ; 
	}
	
	public int getMinutesFromYCoordinate(int iYPos) {
		return (60 * iYPos) / display.getHourSpanInPixel() ;
	}
	
	@Override
	protected void onUnbind() {		
	}

	@Override
	public void revealDisplay() {
	}

	@Override
	protected void onRevealDisplay() {
		// TODO Auto-generated method stub
		
	}
}
