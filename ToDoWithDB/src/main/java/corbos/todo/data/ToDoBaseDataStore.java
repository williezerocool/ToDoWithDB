package corbos.todo.data;

import corbos.todo.Response;
import corbos.todo.ToDo;

public abstract class ToDoBaseDataStore implements ToDoDataStore {

    @Override
    public final Response add(ToDo task) {

        Response result = new Response();

        if (task.getName() == null || task.getName().trim().length() == 0) {
            result.setMessage("Task name is required!");
            return result;
        }

        if (this.getIncompleteByName(task.getName()) != null) {
            result.setMessage(String.format("Cannot add duplicate task: %s", task.getName()));
            return result;
        }

        return addValidated(task);
    }

    protected abstract Response addValidated(ToDo task);
}
