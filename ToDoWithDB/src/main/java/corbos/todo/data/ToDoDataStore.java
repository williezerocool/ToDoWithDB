package corbos.todo.data;

import corbos.todo.Response;
import corbos.todo.ToDo;
import java.util.List;

/**
 * 
 * @author parallels
 * @
 */
public interface ToDoDataStore {

    Response add(ToDo task);

    List<ToDo> all();
    
    ToDo getIncompleteByName(String name);

    Response update(ToDo task);

}
