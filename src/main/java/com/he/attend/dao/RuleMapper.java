package com.he.attend.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.he.attend.model.Rule;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RuleMapper extends BaseMapper<Rule>{
    public void deleteById(Integer ruleId);
}
