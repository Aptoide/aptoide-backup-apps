package pt.aptoide.backupapps.parser;

import android.util.Log;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.ext.DefaultHandler2;
import pt.aptoide.backupapps.database.Database;
import pt.aptoide.backupapps.model.RepoApk;
import pt.aptoide.backupapps.model.Server;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: rmateus
 * Date: 29-07-2013
 * Time: 13:06
 * To change this template use File | Settings | File Templates.
 */
public class ParserHandler extends DefaultHandler2 {


    private boolean delta;
    private int packageCount = 0;

    public boolean isDelta() {
        return delta;
    }

    public void setDelta(boolean delta) {
        this.delta = delta;
    }

    interface ElementHandler {

        public void endElement() throws SAXException;

    }

    HashMap<String, ElementHandler> elements = new HashMap<String, ElementHandler>();

    Database database = Database.getInstance();

    RepoApk apk = new RepoApk();
    StringBuilder sb = new StringBuilder();
    Server server;

    public ParserHandler(Server server){
        database.prepareDatabase();
        loadElements();
        this.server = server;
    }

    SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");

    boolean remove;
    private void loadElements() {
        elements.put("package", new ElementHandler() {
            @Override
            public void endElement() throws SAXException {

                if(remove){
                    database.deleteApk(apk.getPackageName());
                    remove = false;
                }else{
                    database.insertApk(apk);
                }
                packageCount++;
                apk = new RepoApk();
            }
        });



        elements.put("date", new ElementHandler() {
            @Override
            public void endElement() throws SAXException {

                Date d = null;
                try {
                    d = f.parse(sb.toString());
                } catch (ParseException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
                apk.setDate(d.getTime());

            }
        });

        elements.put("delta", new ElementHandler() {
            @Override
            public void endElement() throws SAXException {
                setDelta(true);
                if(sb.toString().length()>3){
                    server.setHash(sb.toString());
                }
            }
        });

        elements.put("apkpath", new ElementHandler() {
            @Override
            public void endElement() {
                server.setApkPath(sb.toString());
            }
        });

        elements.put("iconspath", new ElementHandler() {
            @Override
            public void endElement() {
                server.setIconsPath(sb.toString());
            }
        });

        elements.put("md5h", new ElementHandler() {
            @Override
            public void endElement() {
                apk.setMd5Sum(sb.toString());
            }
        });

        elements.put("repository", new ElementHandler() {
            @Override
            public void endElement() {
                if(!isDelta()){
                    database.updateServer(server);
                    database.removeRepoData();
                }else if(server.getHash().length() > 3){
                    database.setServerHash(server);
                }
            }
        });

        elements.put("del", new ElementHandler() {
            @Override
            public void endElement() throws SAXException {
                remove = true;
            }
        });

        elements.put("apkpath", new ElementHandler() {
            @Override
            public void endElement() {
                server.setApkPath(sb.toString());
            }
        });
        elements.put("appscount", new ElementHandler() {
            @Override
            public void endElement() {
                server.setAppsCount(Integer.parseInt(sb.toString()));
            }
        });

        elements.put("name", new ElementHandler() {
            @Override
            public void endElement() {
                apk.setName(sb.toString());
            }
        });

        elements.put("apkid", new ElementHandler() {
            @Override
            public void endElement() {
                 apk.setPackageName(sb.toString());
            }
        });

        elements.put("ver", new ElementHandler() {
            @Override
            public void endElement() {
                apk.setVersionName(sb.toString());
            }
        });

        elements.put("vercode", new ElementHandler() {
            @Override
            public void endElement() {
                apk.setVersionCode(Integer.parseInt(sb.toString()));
            }
        });

        elements.put("icon", new ElementHandler() {
            @Override
            public void endElement() {
                apk.setIconPath(sb.toString());
            }
        });

        elements.put("path", new ElementHandler() {
            @Override
            public void endElement() {
                apk.setPath(sb.toString());
            }
        });

        elements.put("sz", new ElementHandler() {
            @Override
            public void endElement() {
                apk.setSize(Long.parseLong(sb.toString()));
            }
        });

        elements.put("apklst", new ElementHandler() {
            @Override
            public void endElement() {
                if(packageCount==0){
                    setDelta(true);
                }
            }
        });




    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);
        sb.setLength(0);
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        super.characters(ch, start, length);
        sb.append(ch,start,length);
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        super.endElement(uri, localName, qName);

        try{
            elements.get(localName).endElement();
        }catch (NullPointerException e){
            //Log.d("TAG", "element not found:" + localName);
        }


    }
}
