package demo.service;

import demo.data.DataAccess;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.*;

public class ServiceTest {

    private static UserServiceImpl service;
    private static DataAccess dao;

    @BeforeAll
    static void setup() {
        dao = mock(DataAccess.class);

        service = new UserServiceImpl(dao);
    }

    @Test
    void getQuotes() {
        doReturn("proxy collection").when(dao).selectAll();

        assertEquals(200, service.getQuotes().getStatus());

        verify(dao, times(1)).selectAll();
    }

    @Test
    void getQuotes_Fail() {
        doReturn(null).when(dao).selectAll();

        assertEquals(400, service.getQuotes().getStatus());

        verify(dao, times(2)).selectAll();
    }

    @Test
    void getQuote() {
        doReturn("proxy quote").when(dao).selectQuote(1);

        assertEquals(200, service.getQuote(1).getStatus());

        verify(dao).selectQuote(1);
    }

    @Test
    void getQuote_Fail() {
        assertEquals(404, service.getQuote(0).getStatus());

        verify(dao).selectQuote(0);
    }

    @Test
    void updateQuote() {
        doReturn(1).when(dao).updateQuote(1, "new quote");

        assertEquals(200, service.updateQuote("new quote", 1).getStatus());

        verify(dao).updateQuote(1, "new quote");
    }

    @Test
    void updateQuote_Fail() {
        assertNotEquals(200, service.updateQuote("", 1).getStatus());

        verify(dao).updateQuote(1, "");
    }

    @Test
    void addQuote() {
        doReturn(1).when(dao).insertQuote("new quote");

        assertEquals(201, service.addQuote("new quote").getStatus());

        verify(dao).insertQuote("new quote");
    }

    @Test
    void addQuote_Fail() {
        assertEquals(400, service.addQuote("").getStatus());

        verify(dao).insertQuote("");
    }

    @Test
    void addQuoteCollection() {
        doReturn(1).when(dao).insertCollection("new collection");

        assertEquals(201, service.addQuoteCollection("new collection").getStatus());

        verify(dao).insertCollection("new collection");
    }

    @Test
    void addQuoteCollection_Fail() {
        assertEquals(400, service.addQuoteCollection("new").getStatus());

        verify(dao).insertCollection("new");
    }

    @Test
    void deleteQuote() {
        doReturn(1).when(dao).deleteQuote(1);

        assertEquals(200, service.deleteQuote(1).getStatus());

        verify(dao).deleteQuote(1);
    }

    @Test
    void deleteQuote_Fail() {
        doReturn(-1).when(dao).deleteQuote(0);

        assertEquals(404, service.deleteQuote(0).getStatus());

        verify(dao).deleteQuote(0);
    }
}