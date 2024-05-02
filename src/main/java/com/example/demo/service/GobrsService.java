package com.example.demo.service;

import com.example.demo.common.RuleDto;
import com.example.demo.common.Student;
import com.gobrs.async.core.GobrsAsync;
import com.gobrs.async.core.common.domain.AsyncParam;
import com.gobrs.async.core.common.domain.AsyncResult;
import com.gobrs.async.core.config.ConfigFactory;
import com.gobrs.async.core.config.ConfigManager;
import com.gobrs.async.core.config.GobrsAsyncRule;
import com.gobrs.async.core.config.GobrsConfig;
import com.gobrs.async.core.engine.RuleThermalLoad;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import java.util.HashMap;
import java.util.Map;

@Service
public class GobrsService {

    @Autowired
    private GobrsAsync gobrsAsync;

    @Autowired
    private RuleThermalLoad ruleLoader;
    @Autowired
    private ConfigFactory configFactory;


    public AsyncResult gobrsTest(String ruleName) {
        //这个方法是串行执行，
//        AsyncResult resp = null;
        Student student = new Student("allen", 20);
//        Student student = new Student();
//        student.setName("allen");
//        student.setAge(18);
        AsyncResult resp = gobrsAsync.go(ruleName, () -> student);
        return resp;
    }

    public void addRule(RuleDto ruleDto) {
        GobrsAsyncRule r = new GobrsAsyncRule();
        r.setName(ruleDto.getName());
        r.setContent(ruleDto.getContent());
        ruleLoader.load(r);
//        ConfigManager.configInstance().addRule(ruleDto.getName(), r);
        configFactory.addRule(ruleDto.getName(), r);
    }

    public void deleteRule(String ruleName) {
        configFactory.getProcessRules().remove(ruleName);
    }
}
