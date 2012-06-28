package com.superdownloader.proEasy.services;

import java.util.Collections;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.superdownloader.proEasy.logic.FilesController;
import com.superdownloader.proEasy.types.FileValue;
import com.superdownloader.proEasy.types.Response;

/**
 * WebService for handle downloads
 * @author harley
 *
 */
@Path("/downloads")
@Component
@Scope("request")
public class DownloadsService {

	private static final Logger LOGGER = LoggerFactory.getLogger(DownloadsService.class);

	@Autowired
	private FilesController controller;

	@GET
	@Path("/list")
	@Produces("text/xml")
	public List<FileValue> list() {
		try {
			return controller.getCompletedFiles();
		} catch (Exception e) {
			LOGGER.error("Can not read list of downloads", e);
			return Collections.emptyList();
		}
	}

	@GET
	@Path("/put")
	@Produces("text/xml")
	public Response put(@QueryParam("username") String username,
			@QueryParam("fileName") List<String> fileNames) {
		try {
			controller.putToDownload(username, fileNames);
			return Response.createSuccessfulResponse();
		} catch (Exception e) {
			LOGGER.error("Can not put to download", e);
			return Response.createErrorResponse("Can not put to download");
		}
	}

	@GET
	@Path("/queue")
	@Produces("text/xml")
	public List<FileValue> queue(@QueryParam("username") String username) {
		try {
			return controller.downloadsInQueue(username);
		} catch (Exception e) {
			LOGGER.error("Can not read queue", e);
			return Collections.emptyList();
		}
	}

}
