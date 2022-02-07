package demo.service;

import demo.data.DataAccess;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class ServiceParamTest {

    private static UserServiceImpl service;

    private static DataAccess dao;

    private static int counter;

    @BeforeAll
    static void setup() {
        dao = mock(DataAccess.class);

        service = new UserServiceImpl(dao);

        counter = 0;
    }

    @ParameterizedTest
    @ValueSource(strings = {"proxy collection", "collection", "{quote}\n{quote}"})
    void getQuotes(String collection) {
        doReturn(collection).when(dao).selectAll();

        assertEquals(200, service.getQuotes().getStatus());
    }

    @ParameterizedTest
    @NullSource
    void getQuotes_Fail(String collection) {
        doReturn(collection).when(dao).selectAll();

        assertEquals(400, service.getQuotes().getStatus());
    }

    @ParameterizedTest
    @ValueSource(strings = {"proxy quote", "quote", "$#%#%&", "1312"})
    void getQuote(String quote) {

        doReturn(quote).when(dao).selectQuote(1);

        assertEquals(200, service.getQuote(1).getStatus());

        verify(dao, times(++counter)).selectQuote(1);
    }

    @ParameterizedTest
    @NullSource
    void getQuote_Fail(String quote) {
        doReturn(quote).when(dao).selectQuote(0);

        assertEquals(404, service.getQuote(0).getStatus());

        verify(dao).selectQuote(0);
    }

    @ParameterizedTest
    @CsvSource({"1,proxy quote", "2,quote", "3,$#%#%&", "9,1312"})
    void updateQuote(int id, String quote) {
        doReturn(1).when(dao).updateQuote(id, quote);

        assertEquals(200, service.updateQuote(quote, id).getStatus());

        verify(dao).updateQuote(id, quote);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"proxy quote & quote & $#%#%& & 1312", "this is valid & this aint"})
    void addQuoteCollection_Fail(String collection) {
        assertEquals(400, service.addQuoteCollection(collection).getStatus());

        verify(dao).insertCollection(collection);
    }

}
