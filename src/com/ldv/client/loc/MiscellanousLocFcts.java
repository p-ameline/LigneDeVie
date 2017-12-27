package com.ldv.client.loc;

import com.google.gwt.core.client.GWT;

/**
 * Miscellaneous localization functions
 */
public class MiscellanousLocFcts 
{
	/**
	 * Get the label of a given month (for example "January" as month 1)
	 * 	  
	 * @param iMonth Month index, from 1 for January to 12 for December
	 * 
	 * @return The localization label for this month (or <code>""</code> if iMonth is not in the 1-12 interval)
	 */
	public static String getMonthLabel(int iMonth) 
	{
		if ((iMonth < 1) || (iMonth > 12))
			return "" ;
		
		LdvConstants constants = GWT.create(LdvConstants.class) ;
		
		switch(iMonth)
		{
			case  1 : return constants.generalMonthJanuary() ;
			case  2 : return constants.generalMonthFebruary() ;
			case  3 : return constants.generalMonthMarch() ;
			case  4 : return constants.generalMonthApril() ;
			case  5 : return constants.generalMonthMay() ;
			case  6 : return constants.generalMonthJune() ;
			case  7 : return constants.generalMonthJully() ;
			case  8 : return constants.generalMonthAugust() ;
			case  9 : return constants.generalMonthSeptember() ;
			case 10 : return constants.generalMonthOctober() ;
			case 11 : return constants.generalMonthNovember() ;
			case 12 : return constants.generalMonthDecember() ;
			default : return "" ;
		}
	}
	
	/**
	 * Get the label of a given day of the week (for example "Monday" as day 1)
	 * 	  
	 * @param iDoW Day of week index, from 1 for Monday to 7 for Sunday
	 * 
	 * @return The localization label for this day (or <code>""</code> if iDoW is not in the 1-7 interval)
	 */
	public static String getDayOfWeekLabel(int iDoW)
	{
		if ((iDoW < 1) || (iDoW > 7))
			return "" ;
		
		LdvConstants constants = GWT.create(LdvConstants.class) ;
		
		switch(iDoW)
		{
			case  1 : return constants.generalDayMonday() ;
			case  2 : return constants.generalDayTuesday() ;
			case  3 : return constants.generalDayWednesday() ;
			case  4 : return constants.generalDayThursday() ;
			case  5 : return constants.generalDayFriday() ;
			case  6 : return constants.generalDaySaturday() ;
			case  7 : return constants.generalDaySunday() ;
			default : return "" ;
		}
	}
	
	/**
	 * Get the two chars label of a given day of the week (for example "Mo" (for Monday) as day 1)
	 * 	  
	 * @param iDoW Day of week index, from 1 for Monday to 7 for Sunday
	 * @param bFirstLetterAsUppercase <code>true</code> if first letter must be uppercased (for example "Mo" instead of "mo")
	 * 
	 * @return The two chars localization label for this day (or <code>""</code> if iDoW is not in the 1-7 interval)
	 */
	public static String getDayOfWeekTwoCharsLabel(int iDoW, boolean bFirstLetterAsUppercase)
	{
		if ((iDoW < 1) || (iDoW > 7))
			return "" ;
		
		LdvConstants constants = GWT.create(LdvConstants.class) ;
		
		String sDoW = "" ;
		
		switch(iDoW)
		{
			case  1 : sDoW = constants.generalDayMonday2l() ;    break ;
			case  2 : sDoW = constants.generalDayTuesday2l() ;   break ;
			case  3 : sDoW = constants.generalDayWednesday2l() ; break ;
			case  4 : sDoW = constants.generalDayThursday2l() ;  break ;
			case  5 : sDoW = constants.generalDayFriday2l() ;    break ;
			case  6 : sDoW = constants.generalDaySaturday2l() ;  break ;
			case  7 : sDoW = constants.generalDaySunday2l() ;    break ;
			default : return "" ;
		}
		
		if (bFirstLetterAsUppercase)
			return getUppercasedFirstLetter(sDoW) ;
		
		return sDoW ;
	}
	
	/**
	 * Get the three chars label of a given day of the week (for example "Mon" (for Monday) as day 1)
	 * 	  
	 * @param iDoW Day of week index, from 1 for Monday to 7 for Sunday
	 * @param bFirstLetterAsUppercase <code>true</code> if first letter must be uppercased (for example "Mo" instead of "mo")
	 * 
	 * @return The three chars localization label for this day (or <code>""</code> if iDoW is not in the 1-7 interval)
	 */
	public static String getDayOfWeekThreeCharsLabel(int iDoW, boolean bFirstLetterAsUppercase)
	{
		if ((iDoW < 1) || (iDoW > 7))
			return "" ;
		
		LdvConstants constants = GWT.create(LdvConstants.class) ;
		
		String sDoW = "" ;
		
		switch(iDoW)
		{
			case  1 : sDoW = constants.generalDayMonday3l() ;    break ;
			case  2 : sDoW = constants.generalDayTuesday3l() ;   break ;
			case  3 : sDoW = constants.generalDayWednesday3l() ; break ;
			case  4 : sDoW = constants.generalDayThursday3l() ;  break ;
			case  5 : sDoW = constants.generalDayFriday3l() ;    break ;
			case  6 : sDoW = constants.generalDaySaturday3l() ;  break ;
			case  7 : sDoW = constants.generalDaySunday3l() ;    break ;
			default : return "" ;
		}
		
		if (bFirstLetterAsUppercase)
			return getUppercasedFirstLetter(sDoW) ;
		
		return sDoW ;
	}
	
	/**
	 * Get the three chars label of a given month (for example "Jan" (for January) as month 1)
	 * 	  
	 * @param iMonth Month index, from 1 for January to 12 for December
	 * @param bFirstLetterAsUppercase <code>true</code> if first letter must be uppercased (for example "Jan" instead of "jan")
	 * 
	 * @return The three chars localization label for this month (or <code>""</code> if iDoW is not in the 1-12 interval)
	 */
	public static String getMonthThreeCharsLabel(int iMonth, boolean bFirstLetterAsUppercase)
	{
		if ((iMonth < 1) || (iMonth > 12))
			return "" ;
		
		LdvConstants constants = GWT.create(LdvConstants.class) ;
		
		String sMonth = "" ;
		
		switch(iMonth)
		{
			case  1 : sMonth = constants.generalMonthJanuary3l() ;   break ;
			case  2 : sMonth = constants.generalMonthFebruary3l() ;  break ;
			case  3 : sMonth = constants.generalMonthMarch3l() ;     break ;
			case  4 : sMonth = constants.generalMonthApril3l() ;     break ;
			case  5 : sMonth = constants.generalMonthMay3l() ;       break ;
			case  6 : sMonth = constants.generalMonthJune3l() ;      break ;
			case  7 : sMonth = constants.generalMonthJully3l() ;     break ;
			case  8 : sMonth = constants.generalMonthAugust3l() ;    break ;
			case  9 : sMonth = constants.generalMonthSeptember3l() ; break ;
			case 10 : sMonth = constants.generalMonthOctober3l() ;   break ;
			case 11 : sMonth = constants.generalMonthNovember3l() ;  break ;
			case 12 : sMonth = constants.generalMonthDecember3l() ;  break ;
			default : return "" ;
		}
		
		if (bFirstLetterAsUppercase)
			return getUppercasedFirstLetter(sMonth) ;
		
		return sMonth ;
	}
	
	/**
	 * Get a string with an upper cased first letter
	 * 	  
	 * @param sModel String which first letter is to be changed to upper case
	 */
	public static String getUppercasedFirstLetter(final String sModel)
	{
		if ((null == sModel) || "".equals(sModel))
			return "" ;
		
		String sFirst = sModel.substring(0, 1) ;
		sFirst = sFirst.toUpperCase() ;
		
		String sRemains = "" ;
		int iModelLength = sModel.length() ;
		if (iModelLength > 1)
			sRemains = sModel.substring(1, iModelLength) ;
			
		return sFirst + sRemains ;
	}
}
