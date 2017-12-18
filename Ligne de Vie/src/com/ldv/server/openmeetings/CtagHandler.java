/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License") +  you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
// package org.apache.openmeetings.service.calendar.caldav.handler;
package com.ldv.server.openmeetings;

import org.apache.commons.httpclient.HttpClient;
import org.apache.jackrabbit.webdav.DavException;
import org.apache.jackrabbit.webdav.MultiStatusResponse;
import org.apache.jackrabbit.webdav.property.DavPropertyName;
import org.apache.jackrabbit.webdav.property.DavPropertyNameSet;
import org.apache.jackrabbit.webdav.property.DavPropertySet;
import org.apache.jackrabbit.webdav.xml.Namespace;
import org.apache.openmeetings.db.dao.calendar.AppointmentDao;
import org.apache.openmeetings.db.entity.calendar.Appointment;
import org.apache.openmeetings.db.entity.calendar.OmCalendar;
import org.apache.openmeetings.service.calendar.caldav.AppointmentManager;
import org.apache.openmeetings.service.calendar.caldav.iCalUtils;
import org.osaf.caldav4j.CalDAVConstants;
import org.osaf.caldav4j.methods.PropFindMethod;
import org.red5.logging.Red5LoggerFactory;
import org.slf4j.Logger;

import java.io.IOException;

import static org.apache.openmeetings.util.OpenmeetingsVariables.webAppRootKey;
import static org.apache.jackrabbit.webdav.DavServletResponse.SC_OK;

/**
 * Class for Syncing through the help of Ctags.
 * It checks if the Ctag of the Calendar has changed.
 * If it has then update the events, otherwise leave it as it is.
 *
 * @see CalendarHandler
 */
public class CtagHandler extends AbstractCalendarHandler {
	private static final Logger log = Red5LoggerFactory.getLogger(CtagHandler.class, webAppRootKey);

	public static final Namespace NAMESPACE_CALSERVER = Namespace.getNamespace("cs", "http://calendarserver.org/ns/");
	public static final DavPropertyName DNAME_GETCTAG = DavPropertyName.create("getctag", NAMESPACE_CALSERVER);

	public CtagHandler(String path, OmCalendar calendar, HttpClient client, AppointmentDao appointmentDao, iCalUtils utils) {
		super(path, calendar, client, appointmentDao, utils);
	}

	@Override
	public OmCalendar syncItems() {
		//Calendar already inited.

		PropFindMethod propFindMethod = null;

		try {
			DavPropertyNameSet properties = new DavPropertyNameSet();
			properties.add(DNAME_GETCTAG);

			propFindMethod = new PropFindMethod(path, properties, CalDAVConstants.DEPTH_0);
			client.executeMethod(propFindMethod);

			if (propFindMethod.succeeded()) {
				for (MultiStatusResponse response : propFindMethod.getResponseBodyAsMultiStatus().getResponses()) {
					DavPropertySet set = response.getProperties(SC_OK);
					String ctag = AppointmentManager.getTokenFromProperty(set.get(DNAME_GETCTAG));

					if (ctag != null && !ctag.equals(calendar.getToken())) {
						EtagsHandler etagsHandler = new EtagsHandler(path, calendar, client, appointmentDao, utils);
						etagsHandler.syncItems();
						calendar.setToken(ctag);
					}
				}
			} else {
				log.error("Error executing PROPFIND Method, with status Code: "
						+ propFindMethod.getStatusCode());
			}

		} catch (IOException | DavException e) {
			log.error("Error during the execution of calendar-multiget Report.", e);
		} catch (Exception e) {
			log.error("Severe Error during the execution of calendar-multiget Report.", e);
		} finally {
			if (propFindMethod != null) {
				propFindMethod.releaseConnection();
			}
		}

		return calendar;
	}

	@Override
	public boolean updateItem(Appointment appointment) {
		EtagsHandler etagsHandler = new EtagsHandler(path, calendar, client, appointmentDao, utils);
		return etagsHandler.updateItem(appointment);
	}

	@Override
	public boolean deleteItem(Appointment appointment) {
		EtagsHandler etagsHandler = new EtagsHandler(path, calendar, client, appointmentDao, utils);
		return etagsHandler.deleteItem(appointment);
	}
}
