package corbos.todo.data;

import corbos.todo.Config;
import corbos.todo.Response;
import corbos.todo.ToDo;
import java.util.List;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class ToDoDBDataStore extends ToDoBaseDataStore {

    private final Config configuration;

    public ToDoDBDataStore(Config config) {
        configuration = config;
    }

    public ToDoDBDataStore() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected Response addValidated(ToDo task) {
        Response result = new Response();
        Connection conn = null;
        try {

            conn = getConnection();

            PreparedStatement statement = conn.prepareStatement("INSERT INTO Task (`Name`) VALUES (?);");
            statement.setString(1, task.getName());
            statement.execute();

            result.setSuccess(true);

        } catch (SQLException ex) {
            result.setMessage(ex.getMessage());
        } finally {
            try {
                if (conn != null && !conn.isClosed()) {
                    conn.close();
                }
            } catch (Exception ex) {
            }
        }
        return result;
    }

    @Override
    public List<ToDo> all() {
        List<ToDo> result = new ArrayList<>();
        Connection conn = null;
        try {

            conn = getConnection();

            Statement s = conn.createStatement();
            ResultSet rs = s.executeQuery("SELECT `Name`, CreateDate, IsComplete FROM Task;");
            while (rs.next()) {
                result.add(mapToDo(rs));
            }

        } catch (SQLException ex) {
        } finally {
            try {
                if (conn != null && !conn.isClosed()) {
                    conn.close();
                }
            } catch (Exception ex) {
            }
        }
        return result;
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                configuration.getConnectionString(),
                configuration.getUserName(),
                configuration.getPassword()
        );
    }
    
    public Connection getConn() throws SQLException {
       Connection conn =  getConnection();
       return conn;
    }

    @Override
    public Response update(ToDo task) {
        Response result = new Response();
        Connection conn = null;
        try {

            conn = getConnection();

            String sql = "UPDATE Task SET IsComplete = ? WHERE `Name` = ? AND CreateDate = ?;";

            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setBoolean(1, task.isComplete());
            statement.setString(2, task.getName());
            statement.setTimestamp(3, Timestamp.valueOf(task.getCreateDate()));
            int rows = statement.executeUpdate();

            result.setSuccess(rows > 0);

        } catch (SQLException ex) {
            result.setMessage(ex.getMessage());
        } finally {
            try {
                if (conn != null && !conn.isClosed()) {
                    conn.close();
                }
            } catch (Exception ex) {
            }
        }
        return result;
    }

    @Override
    public ToDo getIncompleteByName(String name) {

        Connection conn = null;
        try {

            conn = getConnection();

            String sql = "SELECT `Name`, CreateDate, IsComplete FROM Task WHERE `Name` = ?;";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, name);

            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                return mapToDo(rs);
            }

        } catch (SQLException ex) {
        } finally {
            try {
                if (conn != null && !conn.isClosed()) {
                    conn.close();
                }
            } catch (Exception ex) {
            }
        }
        return null;
    }

    private ToDo mapToDo(ResultSet rs) throws SQLException {
        ToDo item = new ToDo();
        item.setName(rs.getString("Name"));
        LocalDateTime createDate = rs.getTimestamp("CreateDate").toLocalDateTime();
        item.setCreateDate(createDate);
        item.setIsComplete(rs.getBoolean("IsComplete"));
        return item;
    }

}
