/*******************************************************************************
 * HibernateContentDao.java
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

package com.seedboxer.seedboxer.core.persistence.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.seedboxer.seedboxer.core.domain.Content;
import com.seedboxer.seedboxer.core.domain.User;
import com.seedboxer.seedboxer.core.persistence.ContentDao;

/**
 * 
 * @author Jorge Davison (jdavisonc)
 *
 */
@Transactional
@Repository
public class HibernateContentDao extends HibernateDao implements ContentDao {
	
	@Override
	public void save(Content content){
		getCurrentSession().save(content);
	}
	
	@Override
	public List<Content> getAllContent(User user) {
		Criteria criteria = getCurrentSession().createCriteria(Content.class);
		criteria.add(Restrictions.eq("user", user));
		return criteria.list();
	}

	@Override
	public <T extends Content> List<T> getContentHistory(Class<T> clazz, boolean isHistory){
		Criteria criteria = getCurrentSession().createCriteria(clazz);
		criteria.add(Restrictions.eq("history", isHistory));
		return criteria.list();
	}

	@Override
	public <T extends Content> List<T> getHistoryContentFilteredByNameAndUser(Class<T> clazz, String name,
			User user){
		Criteria criteria = getCurrentSession().createCriteria(clazz);
		Criterion rest1 = Restrictions.and(Restrictions.eq("name", name),Restrictions.eq("history",true));
		rest1 = Restrictions.and(rest1,Restrictions.eq("user",user));
		criteria.add(rest1);
		return criteria.list();
	}

}
