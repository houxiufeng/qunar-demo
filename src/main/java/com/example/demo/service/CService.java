package com.example.demo.service;

import com.example.demo.common.Student;
import com.gobrs.async.core.TaskSupport;
import com.gobrs.async.core.anno.Task;
import com.gobrs.async.core.task.AsyncTask;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
@Task(value = "cs")
public class CService extends AsyncTask<Student, Boolean> {

    @Override
    public Boolean task(Student param, TaskSupport taskSupport) {
        System.out.println("CService Begin-->" + param);
        param.setName(param.getName() + 3);
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    @Override
    public void prepare(Student param) {
        super.prepare(param);
    }

    @Override
    public boolean necessary(Student param, TaskSupport support) {
        return true;
    }

    @Override
    public void onSuccess(TaskSupport support) {
        super.onSuccess(support);
    }

    @Override
    public void onFail(TaskSupport support, Exception exception) {
        super.onFail(support, exception);
    }
}
