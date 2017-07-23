package corbos.todo.data;

import corbos.todo.Response;
import corbos.todo.ToDo;
import java.util.ArrayList;
import java.util.List;

public class ToDoMemoryDataStore extends ToDoBaseDataStore {

    private static final List<ToDo> tasks = new ArrayList<>();

    @Override
    protected Response addValidated(ToDo task) {
        Response result = new Response();
        tasks.add(task);
        result.setSuccess(true);
        return result;
    }

    @Override
    public List<ToDo> all() {
        return new ArrayList<ToDo>(tasks);
    }

    @Override
    public ToDo getIncompleteByName(String name) {
        return tasks.stream()
                .filter(i -> i.getName().equalsIgnoreCase(name) && !i.isComplete())
                .findFirst()
                .orElse(null);
    }

    @Override
    public Response update(ToDo task) {
        Response result = new Response();
        for (ToDo t : tasks) {
            if (t.getName().equalsIgnoreCase(task.getName()) && t.getCreateDate().equals(task.getCreateDate())) {
                t.setIsComplete(task.isComplete());
                result.setSuccess(true);
            }
        }

        if (!result.isSuccess()) {
            result.setMessage(String.format("Could not update task: %s.", task.getName()));
        }

        return result;
    }

}
