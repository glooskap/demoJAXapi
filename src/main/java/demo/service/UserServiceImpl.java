package demo.service;

import demo.data.DataAccess;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

@Path("quotes")
public class UserServiceImpl implements UserService {

    private DataAccess dao;

    public UserServiceImpl() {
        dao = new DataAccess();
    }

    public UserServiceImpl(DataAccess dao) {
        this.dao = dao;
    }

    @Override
    public Response getQuotes() {
        String response = dao.selectAll();
        if (response == null)
            return Response.status(Response.Status.BAD_REQUEST).build();
        return Response.status(Response.Status.OK).entity(response).build();
    }

    @Override
    public Response getQuote(int id) {
        String response = dao.selectQuote(id);
        if (response == null)
            return Response.status(Response.Status.NOT_FOUND).build();
        return Response.status(Response.Status.OK).entity(response).build();
    }

    @Override
    public Response updateQuote(String quote, int id) {

        int ans = dao.updateQuote(id, quote);

        if( ans == 0)
            return Response.status(Response.Status.BAD_REQUEST).build();
        if( ans == -1)
            return Response.status(Response.Status.NOT_FOUND).build();
        return Response.status(Response.Status.OK).entity("Quote updated").build();
    }

    @Override
    public Response addQuote(String quote) {
        if (dao.insertQuote(quote) == 1)
            return Response.status(Response.Status.CREATED).entity("Quote created").build();
        return Response.status(Response.Status.BAD_REQUEST).build();
    }

    @Override
    public Response addQuoteCollection(String quotes) {
        if (dao.insertCollection(quotes) == 1)
            return Response.status(Response.Status.CREATED).entity("Quotes created").build();
        return Response.status(Response.Status.BAD_REQUEST).build();
    }

    @Override
    public Response deleteQuote(int id) {
        if(dao.deleteQuote(id) == -1)
            return Response.status(Response.Status.NOT_FOUND).build();
        return Response.status(Response.Status.OK).entity("Quote deleted").build();
    }

}
