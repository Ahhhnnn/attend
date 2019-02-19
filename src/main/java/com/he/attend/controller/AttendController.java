package com.he.attend.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.he.attend.common.PageResult;
import com.he.attend.model.Attend;
import com.he.attend.model.Staff;
import com.he.attend.service.AttendService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@Slf4j
@RestController
@RequestMapping("${api.version}/attend")
public class AttendController {
    @Autowired
    private AttendService attendService;


    @RequestMapping(value = "/update" ,method = RequestMethod.POST)
    public PageResult updateAttend(Attend attend){

        if(attendService.updateById(attend)){
            return new PageResult("修改成功",200);
        }
        return new PageResult("修改失败",400);
    }
    //逻辑删除考勤记录
    @RequestMapping("/delete/{attendId}")
    public PageResult delteAttend(@PathVariable("attendId") Integer attendId){
        try {
            attendService.logicDelete(attendId);
            return new PageResult("删除成功",200);
        }catch (Exception e) {
            log.error(e.getMessage(),e);
            return new PageResult("删除失败", 400);
        }
    }

    @ApiOperation(value = "查询考勤记录", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "第几页", required = true, dataType = "Integer", paramType = "form"),
            @ApiImplicitParam(name = "limit", value = "每页多少条", required = true, dataType = "Integer", paramType = "form"),
            @ApiImplicitParam(name = "searchKey", value = "筛选条件字段", dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "searchValue", value = "筛选条件关键字", dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "access_token", value = "令牌", required = true, dataType = "String", paramType = "form")
    })
    @RequestMapping("/query")
    public PageResult<Attend> list(Integer page, Integer limit, String searchKey, String searchValue) {
        if (page == null) {
            page = 0;
        }
        if (limit == null) {
            limit = 10;
        }
        Page<Attend> attendPage = new Page<>(page, limit);
        EntityWrapper<Attend> wrapper = new EntityWrapper<>();
        if (searchKey != null && !searchKey.trim().isEmpty() && searchValue != null && !searchValue.trim().isEmpty()) {
            if(searchKey.equals("staff_name")||searchKey.equals("dept_name")||searchKey.equals("shift_name")||searchKey.equals("place")){
                wrapper.like(searchKey,searchValue);
            }else {
                wrapper.eq(searchKey, searchValue);
            }
        }
        wrapper.eq("dr",0);
        wrapper.orderBy("createTime", true);
        attendService.selectPage(attendPage, wrapper);
        List<Attend> attendsList = attendPage.getRecords();
        return new PageResult<>(attendsList, attendPage.getTotal());
    }
}
