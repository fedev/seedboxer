package com.superdownloader.proEasy.processors.notifications;

import java.io.IOException;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.camel.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.superdownloader.proEasy.processors.Headers;
import com.superdownloader.proEasy.utils.C2DMUtils;

/**
 * @author harley
 *
 */
@Service(value = "c2dmNotification")
public class C2DMNotification extends Notification {

	private static final Logger LOGGER = LoggerFactory.getLogger(C2DMNotification.class);

	@Value("${proEasy.c2dm.username}")
	private String c2dmUsername;

	@Value("${proEasy.c2dm.password}")
	private String c2dmPassword;

	private String authToken = null;

	@Override
	protected void processSuccessNotification(Message msg) {
		sendMessage(msg, "OK");
	}

	@Override
	protected void processFailNotification(Message msg, Throwable t) {
		sendMessage(msg, "FAILED");
	}

	@SuppressWarnings("unchecked")
	private void sendMessage(Message msg, String sucess) {
		String deviceId = (String) msg.getHeader(Headers.NOTIFICATION_C2DM_DEVICEID);
		String name = ((List<String>) msg.getHeader(Headers.FILES_NAME)).get(0);
		String message = sucess + ";Files " + name + "...";
		try {
			int responseCode = C2DMUtils.sendMessage(authToken, deviceId, message);
			if (responseCode == 401) { // Invalid login, relogin
				register();
				C2DMUtils.sendMessage(authToken, deviceId, message);
			}
		} catch (IOException e) {
			LOGGER.error("Error sending notification to device", e);
		}
	}

	@PostConstruct
	private void register() {
		try {
			authToken = C2DMUtils.register(c2dmUsername, c2dmPassword);
		} catch (IOException e) {
			LOGGER.error("Error registering server to Google", e);
		}
	}

}
