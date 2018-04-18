package com.tap.chatstory.data.usersource.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by lebron on 2018/4/16.
 */

public class TaskModel {
    @SerializedName("mcoin")
    int coins;
    @SerializedName("task")
    List<TaskDetail> taskList;

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public List<TaskDetail> getTaskList() {
        return taskList;
    }

    public void setTaskList(List<TaskDetail> taskList) {
        this.taskList = taskList;
    }

    public class TaskDetail {
        @SerializedName("name")
        String taskName;
        @SerializedName("reward")
        int reward;
        @SerializedName("finished")
        int finished;

        public String getTaskName() {
            return taskName;
        }

        public void setTaskName(String taskName) {
            this.taskName = taskName;
        }

        public int getReward() {
            return reward;
        }

        public void setReward(int reward) {
            this.reward = reward;
        }

        public int getFinished() {
            return finished;
        }

        public void setFinished(int finished) {
            this.finished = finished;
        }
    }
}
