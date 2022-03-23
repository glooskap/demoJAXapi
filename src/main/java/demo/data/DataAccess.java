package demo.data;

import demo.model.Quote;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.StringTokenizer;

/**
 * establish mysql connection and execute queries
 */
public class DataAccess {

    private static Connection conn;

    public DataAccess() {
        conn = DbConnection.getConnection();
    }

    /**
     * SELECT* FROM QUOTES
     *
     * @return the String value of the whole table or null if something goes wrong
     */
    public String selectAll() {

        ArrayList<Quote> quotes = new ArrayList<>();

        try {
            PreparedStatement ps = conn.prepareStatement("Select* from QUOTES;");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Quote temp = new Quote();
                temp.setQuote(rs.getString("quote"));
                temp.setId((rs.getInt("id")));
                quotes.add(temp);
            }

        } catch (SQLException e) {
            System.out.println("catch *");
            e.printStackTrace();
            return null;
        }
        if (quotes.isEmpty()) return null;
        return quotes.toString();
    }

    /**
     * SELECT QUOTE FROM QUOTES
     *
     * @param id to specify the quote
     * @return the String value of the specified quote or null if something goes wrong
     */
    public String selectQuote(int id) {

        Quote temp = new Quote();
        temp.setId(id);
        try {
            PreparedStatement ps = conn.prepareStatement("Select QUOTE from QUOTES where id='" +id+ "';");
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                temp.setQuote(rs.getString("quote"));
            }

        } catch (SQLException e) {
            System.out.println("catch select");
            e.printStackTrace();
            return null;
        }

        return temp.toString();
    }

    /**
     * INSERT INTO QUOTES <br>
     * //id is assigned by the database
     *
     * @param quote value to be inserted
     * @return 1 if successful or -1 if something goes wrong
     */
    public int insertQuote(String quote) {

        try {
            PreparedStatement ps = conn.prepareStatement("insert into QUOTES (quote) values('" + quote + "');");
            ps.executeUpdate();

        } catch (SQLException e) {
            System.out.println("catch insert");
            e.printStackTrace();
            return -1;
        }
        return 1;
    }

    /**
     * INSERT INTO QUOTES
     *
     * @param quote value to be inserted
     * @param id of the quote
     * @return 1 if successful or -1 if something goes wrong
     */
    public int insertQuote(int id, String quote) {

        try {
            PreparedStatement ps = conn.prepareStatement("insert into QUOTES (id, quote) values(" +id+ ",'" +quote+ "');");

            if (ps.executeUpdate() == 0) return 0;

        } catch (SQLException e) {
            System.out.println("catch insert");
            e.printStackTrace();
            return -1;
        }
        return 1;
    }

    /**
     * *transactional* <br>
     * INSERT INTO QUOTES <br>
     * //ids are assigned by the database <br>
     * either the whole collection is stored or none of it
     *
     * @param quotes '&'-separated values to be inserted
     * @return 1 if successful or -1 if something goes wrong
     */
    public int insertCollection(String quotes) {

        StringTokenizer tokenizer = new StringTokenizer(quotes, "&");

        try {
            conn.setAutoCommit(false);

            while (tokenizer.hasMoreTokens()) {
                String quote = tokenizer.nextToken();
                if (quote.length() < 10) throw new InputMismatchException();

                PreparedStatement ps = conn.prepareStatement("insert into QUOTES (quote) values('" + quote + "');");
                ps.executeUpdate();
            }
            conn.commit();

        } catch (SQLException e) {
            System.out.println("catch sql insert");
            e.printStackTrace();
            return -1;
        }
        catch (InputMismatchException e) {
            System.out.println("catch input format insert");
            e.printStackTrace();
            return -1;
        }
        return 1;
    }

    /**
     * UPDATE QUOTES SET QUOTE
     *
     * @param id to specify the quote
     * @param quote value to be inserted
     * @return 1 if successful or -1 if something goes wrong
     */
    public int updateQuote(int id, String quote) {

        try {
            PreparedStatement ps = conn.prepareStatement("update QUOTES set quote='" +quote+ "' where id='"+ id + "';");
            if (ps.executeUpdate() == 0) return 0;

        } catch (SQLException e) {
            System.out.println("catch update");
            e.printStackTrace();
            return -1;
        }
        return 1;
    }

    /**
     * DELETE FROM QUOTES
     *
     * @param id to specify the quote
     * @return 1 if successful or -1 if something goes wrong
     */
    public int deleteQuote(int id) {
        try {
            PreparedStatement ps = conn.prepareStatement("delete from QUOTES where id='" + id + "';");
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("catch delete");
            e.printStackTrace();
            return -1;
        }
        return 1;
    }

    /**
     * LOAD DATA INFILE <br>
     * INTO TABLE QUOTES
     *
     * @param path of the file
     * @return 1 if successful or -1 if something goes wrong
     */
    public int loadFile(String path) {
        try {
            PreparedStatement ps = conn.prepareStatement("LOAD DATA INFILE '" + path +
                    "' INTO TABLE QUOTES FIELDS TERMINATED BY ','" +
                    "ENCLOSED BY '\"'" +
                    "LINES TERMINATED BY '\\n'" +
                    "IGNORE 1 ROWS;");
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("catch upload");
            e.printStackTrace();
            return -1;
        }
        return 1;
    }

    public void cleanup() {
        try {
            PreparedStatement ps = conn.prepareStatement("delete from QUOTES");
            ps.executeUpdate();

        } catch (SQLException e) {
            System.out.println("catch cleanup");
            e.printStackTrace();
        }
        DbConnection.disconnect();
    }
}
