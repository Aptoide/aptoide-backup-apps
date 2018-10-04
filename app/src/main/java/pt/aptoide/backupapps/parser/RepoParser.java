package pt.aptoide.backupapps.parser;

import java.io.File;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.SAXException;
import pt.aptoide.backupapps.database.Database;
import pt.aptoide.backupapps.model.Server;
import pt.aptoide.backupapps.util.Md5Handler;

/**
 * Created with IntelliJ IDEA.
 * User: rmateus
 * Date: 29-07-2013
 * Time: 13:06
 * To change this template use File | Settings | File Templates.
 */
public class RepoParser {

  private RepoParser() {
  }

  public static RepoParser getInstance() {
    return SingletonHolder.INSTANCE;
  }

  public void parse(File f, Server server)
      throws ParserConfigurationException, SAXException, IOException {
    ParserHandler handler = new ParserHandler(server);
    SAXParser parser = SAXParserFactory.newInstance()
        .newSAXParser();
    parser.parse(f, handler);
    if (!handler.isDelta()) {
      server.setHash(Md5Handler.md5Calc(f));
      Database.getInstance()
          .setServerHash(server);
    }
  }

  private static class SingletonHolder {

    public static final RepoParser INSTANCE = new RepoParser();
  }
}
