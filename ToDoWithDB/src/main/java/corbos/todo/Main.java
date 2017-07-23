package corbos.todo;

import corbos.todo.data.*;
import java.io.IOException;
import java.sql.SQLException;

public class Main {

    public static void main(String[] args) throws IOException, SQLException {
        Config config = new Config();
        ToDoApp app = new ToDoApp(getToDoDataStore(config));
        app.run();
    }

    private static ToDoDataStore getToDoDataStore(Config config) {
        if (config.getDataStoreType() == DataStoreType.DATABASE) {
            return new ToDoDBDataStore(config);
        } else if (config.getDataStoreType() == DataStoreType.FILE) {
            return new ToDoFileDataStore();
        }
        return new ToDoMemoryDataStore();
    }
}
