/*******************************************************************************
 * RssConsumer.java
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

package com.seedboxer.seedboxer.sources.processors;

import java.util.ArrayList;
import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.seedboxer.seedboxer.sources.type.MatchableItem;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;

/**
 *
 * @author The-Sultan
 */
@Component
public class RssConsumer implements Processor{

	private static final Logger LOGGER = LoggerFactory.getLogger(RssConsumer.class);

	@SuppressWarnings("unchecked")
	@Override
	public void process(Exchange exchange) throws Exception {
		Message msg = exchange.getIn();
		SyndFeed feed = (SyndFeed) msg.getBody();
		List<SyndEntry > entries = feed.getEntries();
		List<MatchableItem> items = new ArrayList<MatchableItem>();

		LOGGER.debug("Incoming rss entries: {}", entries.size());
		for(SyndEntry  entry : entries){
			items.add(new MatchableItem(entry.getTitle(),entry.getLink()));
		}

		exchange.getOut().setBody(items, items.getClass());
	}
}
