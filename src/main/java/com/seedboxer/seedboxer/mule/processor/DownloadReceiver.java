/*******************************************************************************
 * DownloadReceiver.java
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
package com.seedboxer.seedboxer.mule.processor;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.seedboxer.seedboxer.core.domain.Configuration;
import com.seedboxer.seedboxer.core.domain.DownloadQueueItem;
import com.seedboxer.seedboxer.core.domain.User;
import com.seedboxer.seedboxer.core.domain.UserConfiguration;
import com.seedboxer.seedboxer.core.logic.UsersController;

/**
 * @author Jorge Davison (jdavisonc)
 *
 */
@Component
public class DownloadReceiver implements Processor {

	private static final Logger LOGGER = LoggerFactory.getLogger(DownloadReceiver.class);

	@Autowired
	private UsersController usersController;

	@Override
	public void process(Exchange exchange) throws Exception {
		Message msg = exchange.getIn();

		DownloadQueueItem item = (DownloadQueueItem) msg.getBody();
		User user = item.getUser();
		long downloadId = item.getId();

		msg.setHeader(Configuration.USER_ID, user.getId());
		msg.setHeader(Configuration.DOWNLOAD_ID, item.getId());
		msg.setHeader(Configuration.START_TIME, new Date());
		List<UserConfiguration> configs = usersController.getUserConfig(user.getId());
		for (UserConfiguration conf : configs) {
			msg.setHeader(conf.getName(), conf.getValue());
		}

		processDownload(msg, item);
		LOGGER.info("USER_ID={}, DOWNLOAD_ID={}", user.getId(), downloadId);
	}

	private void processDownload(Message msg, DownloadQueueItem item) throws FileNotFoundException {
		String downloadPath = item.getDownload();
		File toUpload = new File(downloadPath);
		if (!toUpload.exists()) {
			throw new FileNotFoundException("File " + downloadPath + " doesn't exists.");
		}
		String fileName = toUpload.getName();

		msg.setHeader(Configuration.FILES, Collections.singletonList(downloadPath));
		msg.setHeader(Configuration.FILES_NAME, Collections.singletonList(fileName));
	}

}
