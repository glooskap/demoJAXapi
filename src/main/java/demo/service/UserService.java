package demo.service;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * expose resources and handle requests
 */
public interface UserService {

    /**
     * GET request to read the whole collection of quotes
     * - invoked at demo/quotes/
     *
     * @return all quotes and status OK if completed, BAD REQUEST otherwise
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "GET request to read the whole collection of quotes",
            responses = {
                    @ApiResponse(responseCode = "200", description = "the quotes collection"),
                    @ApiResponse(responseCode = "400", description = "BAD REQUEST")
            }
    )
    Response getQuotes();

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
    @Operation(summary = "GET request to read a specific quote",
            responses = {
                    @ApiResponse(responseCode = "200", description = "the specified quote"),
                    @ApiResponse(responseCode = "404", description = "Quote not found")
            }
    )
    Response getQuote(
            @PathParam(value = "id")
            @Parameter(description = "to specify the quote", required = true)
                    int id);

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
    @Operation(summary = "POST request to add a new quote",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Quote created"),
                    @ApiResponse(responseCode = "400", description = "BAD REQUEST")
            }
    )
    Response addQuote(@RequestBody(description = "String value to be added") String quote);

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
    @Operation(summary = "POST request to add a collection of new quotes",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Quotes created"),
                    @ApiResponse(responseCode = "400", description = "BAD REQUEST")
            }
    )
    Response addQuoteCollection(@RequestBody(description = "String '&'-separated values to be added ",
            content = @Content(examples = @ExampleObject("quote1 & quote2 & quote3 & ...")))
                                        String quotes);

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
    @Operation(summary = "PUT request to update a specific quote",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Quote updated"),
                    @ApiResponse(responseCode = "404", description = "Quote not found")
            }
    )
    Response updateQuote(
            @RequestBody(description = "String value to become the new quote body")
                    String quote,
            @PathParam(value = "id")
            @Parameter(name = "id", description = "to specify the quote" , required = true)
                    int id);

    /**
     * DELETE request to delete a quote
     * - invoked at demo/quotes/{id}
     *
     * @param id to specify the quote
     * @return status OK if completed, NOT FOUND otherwise
     */
    @DELETE
    @Path("{id}")
    @Operation(summary = "DELETE request to delete a specific quote",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Quote deleted"),
                    @ApiResponse(responseCode = "404", description = "Quote not found")
            }
    )
    Response deleteQuote(
            @PathParam(value = "id")
            @Parameter(name = "id", description = "to specify the quote" , required = true)
                    int id);
}
