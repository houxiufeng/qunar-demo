package com.example.demo.service;

import com.example.demo.common.Student;
import com.gobrs.async.core.TaskSupport;
import com.gobrs.async.core.anno.Task;
import com.gobrs.async.core.task.AsyncTask;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
@Task(value = "bs")
//https://async.sizegang.cn/pages/2f84sz/
//A->B->C->D,默认B失败则后续不执行。failSubExec = true 则后续的C D可以执行。
//@Task(value = "bs", failSubExec = true)
public class BService extends AsyncTask<Student, Boolean> {

    @Override
    public Boolean task(Student param, TaskSupport taskSupport) {
        System.out.println("BService Begin-->" + param);
        param.setName(param.getName() + 2);
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
//        int i = 10 / 0;
        return false;
    }

    @Override
    public void prepare(Student param) {
        super.prepare(param);
    }

    @Override
    public boolean necessary(Student param, TaskSupport support) {
        return super.necessary(param, support);
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
