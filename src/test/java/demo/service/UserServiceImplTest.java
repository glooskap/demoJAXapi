package demo.service;

import demo.data.DataAccess;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

public class UserServiceImplTest {

    private static UserServiceImpl service;

    private static DataAccess dao;

    @BeforeAll
    static void setup() {
        dao = mock(DataAccess.class);

        service = new UserServiceImpl(dao);
    }

    @BeforeEach
    void checkDao() {
        assertNotNull(dao);
    }

    @Test
    void getQuotes() {
        doReturn("proxy collection").when(dao).selectAll();

        //assertEquals("proxy collection", service.getQuotes().getEntity());

        assertEquals(200, service.getQuotes().getStatus());
    }

    @Test
    void getQuote() {
        doReturn("proxy quote").when(dao).selectQuote(1);

        assertEquals(200, service.getQuote(1).getStatus());
    }

    @Test
    void updateQuote() {
        doReturn(1).when(dao).updateQuote(1, "new quote");

        assertEquals(200, service.updateQuote("new quote", 1).getStatus());
    }

    @Test
    void addQuote() {
        doReturn(1).when(dao).insertQuote("new quote");

        assertEquals(201, service.addQuote("new quote").getStatus());
    }

    @Test
    void addQuoteCollection() {
        doReturn(1).when(dao).insertCollection("new collection");

        assertEquals(201, service.addQuoteCollection("new collection").getStatus());
    }

    @Test
    void deleteQuote() {
        doReturn(1).when(dao).deleteQuote(1);

        assertEquals(200, service.deleteQuote(1).getStatus());
    }
}