package core;

import core.Task;
import java.util.List;

public class TaskList {
    private List<Task> taskList;

    public List<Task> getTaskList() {
        return taskList;
    }

    public void setTaskList(List<Task> taskList) {
        this.taskList = taskList;
    }

    public void remove(Task task){
        taskList.remove(task);
    }

    @Override
    public String toString() {
        return taskList.toString();
    }
}
