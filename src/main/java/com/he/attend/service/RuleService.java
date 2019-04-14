package com.he.attend.service;


import com.baomidou.mybatisplus.service.IService;
import com.he.attend.model.Rule;
import com.he.attend.model.Staff;

import java.util.List;

public interface RuleService extends IService<Rule> {

    public void deleteById(Integer ruleId);

    List<Rule> query();
}
