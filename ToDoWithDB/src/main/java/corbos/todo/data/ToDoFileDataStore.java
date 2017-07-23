package corbos.todo.data;

import corbos.todo.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ToDoFileDataStore extends ToDoBaseDataStore {

    private static final String DATA_FILE_PATH = "to-do-data.txt";

    @Override
    public List<ToDo> all() {
        return read();
    }

    @Override
    protected Response addValidated(ToDo task) {
        List<ToDo> all = read();
        all.add(task);
        return write(all);
    }

    @Override
    public Response update(ToDo task) {
        Response result = new Response();
        boolean success = false;
        List<ToDo> tasks = all();
        for (ToDo t : tasks) {
            if (t.getName().equalsIgnoreCase(task.getName()) && t.getCreateDate().equals(task.getCreateDate())) {

                t.setIsComplete(task.isComplete());
                success = true;

            }
        }

        if (success) {
            result = write(tasks);
        } else {
            result.setMessage(String.format("Could not update task: %s.", task.getName()));
        }
        return result;
    }

    @Override
    public ToDo getIncompleteByName(String name) {
        return all().stream()
                .filter(i -> i.getName().equalsIgnoreCase(name) && !i.isComplete())
                .findFirst()
                .orElse(null);
    }

    private List<ToDo> read() {
        List<ToDo> result = new ArrayList<>();

        File file = new File(DATA_FILE_PATH);
        if (!file.exists()) {
            return result;
        }

        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line = null;
            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split("\\|");
                if (tokens.length == 3) {
                    ToDo task = new ToDo();
                    task.setName(tokens[0]);
                    task.setIsComplete(Boolean.parseBoolean(tokens[1]));
                    task.setCreateDate(LocalDateTime.parse(tokens[2]));
                    result.add(task);
                }

            }
            reader.close();

        } catch (IOException ex) {
            System.out.printf("ERROR!: %s", ex.getMessage());
        }

        return result;
    }
    


    private Response write(List<ToDo> tasks) {

        Response result = new Response();

        PrintWriter writer = null;
        try {
            writer = new PrintWriter(DATA_FILE_PATH);
            for (ToDo task : tasks) {
                writer.write(String.format(
                        "%s|%s|%s\n",
                        task.getName(),
                        task.isComplete(),
                        task.getCreateDate()));
            }
            writer.flush();
            result.setSuccess(true);
        } catch (FileNotFoundException ex) {
            result.setMessage(ex.getMessage());
        } finally {
            writer.close();
        }

        return result;
    }
    
    public void newWrite(List<ToDo> task) {
        write(task);
    }
}
