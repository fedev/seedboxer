/*******************************************************************************
 * Notification.java
 * 
 * Copyright (c) 2012 SeedBoxer Team.
 * 
 * This file is part of SeedBoxer.
 * 
 * SeedBoxer is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * SeedBoxer is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with SeedBoxer.  If not, see <http ://www.gnu.org/licenses/>.
 ******************************************************************************/
package com.superdownloader.proeasy.mule.processor.notification;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;

/**
 *
 * @author harley
 *
 */
public abstract class Notification implements Processor {

	@Override
	public void process(Exchange exchange) throws Exception {
		Message msg = exchange.getIn();
		String failureEndpoint = (String) exchange.getProperty(Exchange.FAILURE_ENDPOINT);
		if (failureEndpoint == null) {
			processSuccessNotification(msg);
		} else {
			processFailNotification(msg, (Throwable) exchange.getProperty(Exchange.EXCEPTION_CAUGHT));
		}
	}

	protected abstract void processSuccessNotification(Message msg);

	protected abstract void processFailNotification(Message msg, Throwable t);

}
