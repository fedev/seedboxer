package com.superdownloader.proEasy.persistence;

import java.util.Map;

public interface UsersDao {

	public boolean isValidUser(String username, String password);

	public Map<String, String> getUserConfigs(String username);

}
