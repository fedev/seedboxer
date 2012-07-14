package com.superdownloader.proeasy.mule.processors.postactions;

import java.io.File;
import java.io.IOException;
import java.security.PublicKey;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.common.IOUtils;
import net.schmizz.sshj.connection.channel.direct.Session;
import net.schmizz.sshj.connection.channel.direct.Session.Command;
import net.schmizz.sshj.transport.verification.HostKeyVerifier;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.superdownloader.proeasy.mule.processors.Headers;

import freemarker.template.TemplateException;

/**
 * @author harley
 *
 */
@Component(value = "sshCommandSender")
public class SSHCommandSender implements Processor {

	private static final Logger LOGGER = LoggerFactory.getLogger(SSHCommandSender.class);

	@Value("${proeasy.ssh.timeToJoin}")
	private int timeToJoin;

	@Value("${proeasy.ssh.variableNameInCmd}")
	private String variableNameInCmd;

	@Override
	public void process(Exchange exchange) {
		Message msg = exchange.getIn();
		String url = (String) msg.getHeader(Headers.SSH_URL);
		String username = (String) msg.getHeader(Headers.SSH_USERNAME);
		String password = (String) msg.getHeader(Headers.SSH_PASSWORD);
		String cmd = (String) msg.getHeader(Headers.SSH_CMD);

		try {
			String cmdProcessed = processTemplate(cmd, msg.getHeaders());
			sendSSHCmd(url, username, password, cmdProcessed);
		} catch (IOException e) {
			LOGGER.warn("Error sending ssh command.", e);
		} catch (TemplateException e) {
			LOGGER.warn("Error processing template of ssh command.", e);
		}
	}

	private void sendSSHCmd(String url, String username, String password, String command)
			throws IOException {
		final SSHClient sshClient = new SSHClient();

		// TODO: Try to change this to loadKnownHost() or something like this
		sshClient.addHostKeyVerifier(new HostKeyVerifier() {
			@Override
			public boolean verify(String arg0, int arg1, PublicKey arg2) {
				return true; // don't bother verifying
			}
		});

		try {
			sshClient.setConnectTimeout(timeToJoin);
			sshClient.setTimeout(timeToJoin);
			sshClient.connect(url);
			sshClient.authPassword(username, password);

			final Session session = sshClient.startSession();
			try {
				LOGGER.info("Sending ssh command: {} to {}", command, url);
				final Command cmd = session.exec(command);
				LOGGER.trace("Ssh response: {}", IOUtils.readFully(cmd.getInputStream()).toString());
				cmd.join(timeToJoin, TimeUnit.SECONDS);
				LOGGER.trace("Exit status: {}", cmd.getExitStatus());
			} finally {
				session.close();
			}
		} finally {
			sshClient.disconnect();
		}
	}

	@SuppressWarnings("unchecked")
	private String processTemplate(String command, Map<String, Object> templateVars)
			throws IOException, TemplateException {

		List<String> files = (List<String>) templateVars.get(Headers.FILES);
		StringBuffer sb = new StringBuffer();
		for (String file : files) {
			sb.append(new File(file).getName());
			sb.append(",");
		}
		sb.deleteCharAt(sb.length()-1);

		return command.replaceAll(variableNameInCmd, sb.toString());
	}

}
