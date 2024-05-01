package com.example.demo.service;

import com.example.demo.common.Student;
import com.gobrs.async.core.TaskSupport;
import com.gobrs.async.core.anno.Task;
import com.gobrs.async.core.common.domain.AsyncParam;
import com.gobrs.async.core.task.AsyncTask;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
@Task(value = "as")
public class AService extends AsyncTask<Student, Boolean> {

    @Override
    public Boolean task(Student param, TaskSupport taskSupport) {
        System.out.println("AService Begin-->" + param);
        param.setName(param.getName() + 1);
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
////        System.out.println((AsyncParam<Map<String,Object>>)taskSupport.getParam());
//        AsyncParam<Map<String,Object>> param1 = (AsyncParam<Map<String,Object>>)taskSupport.getParam();
//        Map<String, Object> stringObjectMap = param1.get();
//        System.out.println(stringObjectMap.get("foo"));
//        System.out.println(stringObjectMap.get("foo1"));

        return true;
    }

    @Override
    public void prepare(Student param) {
        super.prepare(param);
    }

    @Override
    public boolean necessary(Student  param, TaskSupport support) {
        return super.necessary(param, support);
    }

    @Override
    public void onSuccess(TaskSupport support) {
        super.onSuccess(support);
    }

    @Override
    public void onFail(TaskSupport support, Exception exception) {
        System.out.println("AService onFail");
        super.onFail(support, exception);
    }
}
