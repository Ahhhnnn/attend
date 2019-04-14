package com.he.attend.service.impl;


import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.he.attend.dao.RuleMapper;
import com.he.attend.dao.StaffMapper;
import com.he.attend.model.Rule;
import com.he.attend.model.Staff;
import com.he.attend.service.RuleService;
import com.he.attend.service.StaffService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RuleServiceImpl extends ServiceImpl<RuleMapper, Rule> implements RuleService {


    @Override
    public void deleteById(Integer ruleId) {
        baseMapper.deleteById(ruleId);
    }

    @Override
    public List<Rule> query() {

        return baseMapper.selectList(null);
    }
}
