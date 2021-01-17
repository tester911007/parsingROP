package connection;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config
{
    private Config() {
    }

    public static final String DB_URL = "db.url";
    public static final String DB_DATABASE = "db.database";
    public static final String DB_LOGIN = "db.login";
    public static final String DB_PASSWORD = "db.password";
    public static final String DB_LIMIT = "db.limit";

    private static final Properties properties = new Properties();

    public static String getProperty(String name){
        if(properties.isEmpty()){
            try(InputStream is = Config.class.getClassLoader().getResourceAsStream("config.properties")) {
                properties.load(is);
            } catch (IOException ex){
                ex.printStackTrace();
            }
        }
        return properties.getProperty(name);
    }
}