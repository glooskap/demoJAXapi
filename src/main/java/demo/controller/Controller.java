package demo.controller;

import demo.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import java.io.InputStream;

/**
 * expose resources and handle http requests
 */
@Path("quotes")
public class Controller {

	private UserService service;
	
	public Controller() {
		service = new UserService();
	}
	
	/**
     * GET request to read the whole collection of quotes
     * - invoked at demo/quotes/
     *
     * @return all quotes and status OK if completed, BAD REQUEST otherwise
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "GET the whole collection of quotes",
            responses = {
                    @ApiResponse(responseCode = "200", description = "the quotes collection"),
                    @ApiResponse(responseCode = "400", description = "BAD REQUEST")
            }
    )
	public Response readAll() {
        String response = service.getQuotes();
        if (response == null)
            return Response.status(Response.Status.BAD_REQUEST).build();
        return Response.status(Response.Status.OK).entity(response).build();
    }

    /**
     * GET request to read a specific quote
     * - invoked at demo/quotes/{id}
     *
     * @param id to specify the quote
     * @return the specified quote and status OK if completed, NOT FOUND otherwise
     */
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "GET a specific quote",
            responses = {
                    @ApiResponse(responseCode = "200", description = "the specified quote"),
                    @ApiResponse(responseCode = "404", description = "Quote not found")
            }
    )
    public Response readQuote(
            @PathParam(value = "id")
            @Parameter(description = "to specify the quote", required = true)
            int id)
    {
        String response = service.getQuote(id);
        if (response == null)
            return Response.status(Response.Status.NOT_FOUND).build();
        return Response.status(Response.Status.OK).entity(response).build();
    }

    /**
     * POST request to add a new quote
     * - consumes plain text
     * - invoked at demo/quotes/new
     *
     * @param quote String value to be added
     * @return status CREATED if completed, BAD REQUEST otherwise
     */
    @POST
    @Path("new")
    @Consumes(MediaType.TEXT_PLAIN)
    @Operation(summary = "POST a new quote",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Quote created"),
                    @ApiResponse(responseCode = "400", description = "BAD REQUEST")
            }
    )
    public Response createQuote(
    		@RequestBody(description = "String value to be added")
    		String quote)
    {	
        if (service.addQuote(quote) == 1)
            return Response.status(Response.Status.CREATED).entity("Quote created").build();
        return Response.status(Response.Status.BAD_REQUEST).build();
    }

    /**
     * POST request to add a collection of new quotes
     * - consumes plain text
     * - invoked at demo/quotes/new/collection
     *
     * @param quotes String '&'-separated values to be added
     * @return status CREATED if completed, BAD REQUEST otherwise
     */
    @POST
    @Path("new/collection")
    @Consumes({MediaType.TEXT_PLAIN})
    @Operation(summary = "POST a collection of new quotes",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Quotes created"),
                    @ApiResponse(responseCode = "400", description = "BAD REQUEST")
            }
    )
    public Response createCollection(
    		@RequestBody(description = "String '&'-separated values to be added ",
            content = @Content(examples = @ExampleObject("quote1 & quote2 & quote3 & ...")))
    String quotes)
    {
        if (service.addQuoteCollection(quotes) == 1)
            return Response.status(Response.Status.CREATED).entity("Quotes created").build();
        return Response.status(Response.Status.BAD_REQUEST).build();
    }

    /**
     * PUT request to update an existing quote
     * - consumes plain text
     * - invoked at demo/quotes/{id}
     *
     * @param id to specify the quote
     * @param quote String value to become the new quote body
     * @return status OK if completed, NOT FOUND otherwise
     */
    @PUT
    @Path("{id}")
    @Consumes(MediaType.TEXT_PLAIN)
    @Operation(summary = "PUT a specific quote",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Quote updated"),
                    @ApiResponse(responseCode = "404", description = "Quote not found")
            }
    )
    public Response updateQuote(
            @RequestBody(description = "String value to become the new quote body")
            String quote,
            @PathParam(value = "id")
            @Parameter(name = "id", description = "to specify the quote" , required = true)
            int id)
    {
        int ans = service.updateQuote(quote, id);

        if( ans == 0)
            return Response.status(Response.Status.BAD_REQUEST).build();
        if( ans == -1)
            return Response.status(Response.Status.NOT_FOUND).build();
        return Response.status(Response.Status.OK).entity("Quote updated").build();
    }

    /**
     * DELETE request to delete a quote
     * - invoked at demo/quotes/{id}
     *
     * @param id to specify the quote
     * @return status OK if completed, NOT FOUND otherwise
     */
    @DELETE
    @Path("{id}")
    @Operation(summary = "DELETE a specific quote",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Quote deleted"),
                    @ApiResponse(responseCode = "404", description = "Quote not found")
            }
    )
    public Response deleteQuote(
            @PathParam(value = "id")
            @Parameter(name = "id", description = "to specify the quote" , required = true)
            int id)
    {
        if(service.deleteQuote(id) == -1)
            return Response.status(Response.Status.NOT_FOUND).build();
        return Response.status(Response.Status.OK).entity("Quote deleted").build();
    }

    /**
     * POST request to upload a file
     *
     * @param file to be uploaded
     * @return status CREATED if completed, BAD REQUEST otherwise
     */
    @POST
    @Path("upload")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Operation(summary = "POST a file",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Quotes uploaded"),
                    @ApiResponse(responseCode = "400", description = "BAD REQUEST")
            }
    )
    public Response uploadFile(
            @RequestBody(description = "file to be uploaded")
            @FormDataParam("file") FormDataContentDisposition file,
            @FormDataParam("file") InputStream uploadedInputStream)
    {

        int ans = service.upload(file.getFileName(), uploadedInputStream);
        if ( ans == -1)
            return Response.status(Response.Status.BAD_REQUEST).entity("Error processing file").build();
        if (ans == 0)
            return Response.status(Response.Status.BAD_REQUEST).entity("Error uploading file").build();
        return Response.status(Response.Status.CREATED).entity("Quotes uploaded").build();
    }

}
