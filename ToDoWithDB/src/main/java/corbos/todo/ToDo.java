package corbos.todo;

import java.time.LocalDateTime;

public class ToDo {

    private String name;
    private boolean complete;
    private LocalDateTime createDate = LocalDateTime.now();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isComplete() {
        return complete;
    }

    public void setIsComplete(boolean isComplete) {
        this.complete = isComplete;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime date) {
        this.createDate = date;
    }

}
