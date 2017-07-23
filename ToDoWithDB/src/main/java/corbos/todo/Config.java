package corbos.todo;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Config {

    private static final String DEFAULT_FILE_PATH = "todo.properties";

    private String connectionString;
    private String userName;
    private String password;
    private DataStoreType dataStoreType = DataStoreType.FILE;

    public Config() {
        init(DEFAULT_FILE_PATH);
    }

    public Config(String filePath) {
        init(filePath);
    }

    private void init(String filePath) {
        Properties prop = new Properties();
        try (FileInputStream stream = new FileInputStream(filePath)) {
            prop.load(stream);

            connectionString = prop.getProperty("connectionString");
            userName = prop.getProperty("userName");
            password = prop.getProperty("password");

            String dataStore = prop.getProperty("dataStoreType");
            if (dataStore.equalsIgnoreCase("database")) {
                dataStoreType = DataStoreType.DATABASE;
            } else if (dataStore.equalsIgnoreCase("file")) {
                dataStoreType = DataStoreType.FILE;
            }

        } catch (IOException ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    public String getConnectionString() {
        return connectionString;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public DataStoreType getDataStoreType() {
        return dataStoreType;
    }
}
