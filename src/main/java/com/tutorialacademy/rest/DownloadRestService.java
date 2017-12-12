package com.tutorialacademy.rest;

import java.io.File;
import java.io.IOException;
import java.util.Base64;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.glassfish.jersey.server.ResourceConfig;

@Path("/")
public class DownloadRestService extends ResourceConfig {
	
	final static Logger logger = Logger.getLogger( DownloadRestService.class );
	
	@GET
    @Path( "download/{fileName}" )
	@Produces( MediaType.APPLICATION_OCTET_STREAM )
    public Response download( @PathParam( "fileName" ) final String fileName )
    {
		logger.info( "Download Request: " + fileName );

		try {
			// get relative file path
	        ClassLoader classLoader = new DownloadRestService().getClass().getClassLoader();
	        File file = new File( classLoader.getResource( fileName ).getFile());
	         
	        logger.info( "File Found: " + file.exists() );
			
	        // encode to base64
	        byte[] data = Base64.getEncoder().encode( IOUtils.toByteArray( file.toURI() ) );
	        
	        // prepare response
			return Response
					.ok( data, "image/jpg" )
					.header( "Content-Disposition","inline; filename = \"" + fileName + "\"" )
					.build();
		}
		catch ( IOException e ) {
			return Response.status( Response.Status.NOT_FOUND ).entity( e.getMessage() ).build();
		}
    }
	
}
