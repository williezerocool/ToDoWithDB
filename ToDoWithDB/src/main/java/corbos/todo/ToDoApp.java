package corbos.todo;

import corbos.todo.data.ToDoDataStore;
import corbos.todo.data.ToDoFileDataStore;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


public class ToDoApp {

    private UI ui = new UI();
    private ToDoDataStore data;
    private Config config ;
    private String conn;
    private String userName;
    private String password;
    private DataStoreType dataStoreType;
   

    public ToDoApp(ToDoDataStore dataStore) {
        this.data = dataStore;
        this.config = new Config();
        this.conn = config.getConnectionString();
        this.userName = config.getUserName();
        this.password = config.getPassword();
        this.dataStoreType = config.getDataStoreType();
    }
    
    

    public void run() throws IOException, SQLException {

        ui.displayWelcome();

        int selection = UI.MIN_MENU_ITEM;
        while (selection >= UI.MIN_MENU_ITEM && selection < UI.MAX_MENU_ITEM) {

            selection = ui.getMenuSelection();

            switch (selection) {
                case UI.MENU_LIST:
                    list();
                    break;
                case UI.MENU_ADD:
                    add();
                    break;
                case UI.MENU_COMPLETE:
                    complete();
                    break;
                case UI.MENU_DELETE:
                    delete();
                    break;
                case UI.MENU_DELETE_COMPLETE:
                    deleteCompleteTasks();
                    break;
            }
        }

        ui.displayGoodbye();
    }

    private void list() {
        List<ToDo> items = data.all();
        if (items.size() == 0) {
            ui.displayNoItemsFound();
        } else {
            ui.displayTasks(items);
        }
    }

    private void add() {
        ToDo task = ui.createToDo();
        Response response = data.add(task);
        ui.displayResponse(response);
    }

    private void complete() {
        String name = ui.getTaskToComplete();
        ToDo task = data.getIncompleteByName(name);
        if (task == null) {
            ui.displayCouldNotFindTask(name);
        } else {
            task.setIsComplete(true);
            ui.displayResponse(data.update(task));
        }
    }

    private void delete() {
        // TODO: finish this workflow.
        String taskName = ui.getTaskToDelete();
        
        if(dataStoreType.toString().equalsIgnoreCase("database")) {
           
            try {

                Connection myConn = getConnection();
                
                String sql = "DELETE FROM Task Where Name = ?;";

                PreparedStatement preSt = myConn.prepareStatement(sql);
                preSt.setString(1, taskName);
                preSt.executeUpdate();

            } catch(SQLException ex) {
                ex.getErrorCode();
            }
        } else if (dataStoreType.toString().equalsIgnoreCase("file")) {
           
            ToDoFileDataStore tfds = new ToDoFileDataStore();
            
            List<ToDo> result = new ArrayList<>();
            
            for(ToDo task : data.all() ){
                if(task.getName().equalsIgnoreCase(taskName)) {
                    task = null;
                }else {
                    result.add(task);
                }
                
            }
            tfds.newWrite(result);
           
        }
        
        ui.printLine("Deleting a To-Do!");
    }

    private void deleteCompleteTasks() throws SQLException {
        // TODO: finish this workflow.
        if(dataStoreType.toString().equalsIgnoreCase("database")) {
        
            try {
            
                Connection myConn = getConnection();
                
                String sql = "DELETE FROM task WHERE isComplete = 1; ";
                Statement stMt = myConn.createStatement();
                stMt.execute(sql);
                
                
            } catch(SQLException ex) {
                ex.getErrorCode();
            }
            
        }else if (dataStoreType.toString().equalsIgnoreCase("file")) {
        
            ToDoFileDataStore tfds = new ToDoFileDataStore();
            List<ToDo> list = new ArrayList<>();
            
            for(ToDo task : data.all()) {
                
                if(!task.isComplete()) {
                    list.add(task);
                } 
             }
            tfds.newWrite(list);
        }
        
        ui.printLine("Deleting all complete tasks!");
    }
    
    private Connection getConnection() throws SQLException {
        Connection myConn = DriverManager.getConnection(
                        conn,
                        userName,
                        password);
        return myConn;
        
    }
}
