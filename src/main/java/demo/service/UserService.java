package demo.service;

import demo.Config;
import demo.data.DataAccess;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class UserService{

    private DataAccess dao;

    public UserService() {
        dao = new DataAccess();
    }

    public UserService(DataAccess dao) {
        this.dao = dao;
    }

    public String getQuotes() {
        return dao.selectAll();
    }

    public String getQuote(int id) {
        if (id<0) return null;
        return dao.selectQuote(id);
    }

    public int updateQuote(String quote, int id) {
        if (id<0 || quote==null || quote.isEmpty()) return 0;
        return dao.updateQuote(id, quote);
    }

    public int addQuote(String quote) {
        if (quote==null || quote.isEmpty()) return 0;
        return dao.insertQuote(quote);
    }

    public int addQuoteCollection(String quotes) {
        if (quotes==null || quotes.isEmpty()) return -1;
        return dao.insertCollection(quotes);
    }

    public int deleteQuote(int id) {
        if (id<0) return -1;
        return dao.deleteQuote(id);
    }

    public int upload(String path, InputStream uploadedInputStream) {
        if (path==null || path.isEmpty()) return 0;

        String fileLocation = Config.UPLOAD_FOLDER + path;

        try {
            int read;
            byte[] bytes = new byte[1024];
            FileOutputStream out = new FileOutputStream((fileLocation));
            while ((read = uploadedInputStream.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }

        return dao.loadFile(fileLocation);
    }

}
