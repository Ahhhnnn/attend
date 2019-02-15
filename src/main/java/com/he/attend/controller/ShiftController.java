package com.he.attend.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.he.attend.common.JsonResult;
import com.he.attend.common.PageResult;
import com.he.attend.model.Shift;
import com.he.attend.model.Staff;
import com.he.attend.service.ShiftService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@Slf4j
@RestController
@RequestMapping("${api.version}/shift")
public class ShiftController {
    @Autowired
    private ShiftService shiftService;

    @ApiOperation(value = "查询所有班次", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "第几页", required = true, dataType = "Integer", paramType = "form"),
            @ApiImplicitParam(name = "limit", value = "每页多少条", required = true, dataType = "Integer", paramType = "form"),
            @ApiImplicitParam(name = "searchKey", value = "筛选条件字段", dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "searchValue", value = "筛选条件关键字", dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "access_token", value = "令牌", required = true, dataType = "String", paramType = "form")
    })
    @RequestMapping("/query")
    public PageResult<Shift> list(Integer page, Integer limit, String searchKey, String searchValue) {
        if (page == null) {
            page = 0;
        }
        if (limit == null) {
            limit = 10;
        }
        Page<Shift> shiftPage = new Page<>(page, limit);
        EntityWrapper<Shift> wrapper = new EntityWrapper<>();
        if (searchKey != null && !searchKey.trim().isEmpty() && searchValue != null && !searchValue.trim().isEmpty()) {
            if(searchKey.equals("shift_name")){
                wrapper.like(searchKey,searchValue);
                wrapper.orderBy("createTime", true);
            }else if (searchKey.equals("begin_time")){
                wrapper.gt(searchKey,searchValue);
                wrapper.orderBy("beginTime");
            }else if(searchKey.equals("end_time")){
                wrapper.lt(searchKey,searchValue);
                wrapper.orderBy("endTime");
            }
            else {
                wrapper.eq(searchKey, searchValue);
                wrapper.orderBy("createTime", true);
            }
        }
        shiftService.selectPage(shiftPage, wrapper);
        List<Shift> shiftList = shiftPage.getRecords();
        return new PageResult<>(shiftList, shiftPage.getTotal());
    }
    @ApiOperation(value = "根据id删除班次")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "shiftId", value = "班次id", required = true, dataType = "Integer", paramType = "form"),
    })
    @RequestMapping("/delete/{shiftId}")
    public PageResult delteShift(@PathVariable("shiftId") Integer shiftId){
    try {
        shiftService.deleteById(shiftId);
        return new PageResult("删除成功",200);
    }catch (Exception e) {
        log.error(e.getMessage(),e);
        return new PageResult("删除失败", 400);
        }
    }

    @ApiOperation(value = "修改班次状态", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "shiftId", value = "班次Id", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "state", value = "状态：0正常，1禁用", required = true, dataType = "Integer", paramType = "form"),
            @ApiImplicitParam(name = "access_token", value = "令牌", required = true, dataType = "String", paramType = "form")
    })
    @PutMapping("/state")
    public JsonResult updateState(Integer shiftId, Integer state) {
        if (state == null || (state != 0 && state != 1)) {
            return JsonResult.error("state值需要在[0,1]中");
        }
        try {
            shiftService.updateById(state,shiftId);
            return JsonResult.ok();
        }catch (Exception e) {
            log.error(e.getMessage(),e);
            return JsonResult.error();
        }
    }
    @RequestMapping("/update")
    public PageResult updateShift(Shift shift){
    try {
        shiftService.update(shift);
        return new PageResult("修改成功",200);
    }catch (Exception e) {
        log.error(e.getMessage(),e);
        return new PageResult("修改失败", 400);
        }
    }

    @RequestMapping("/insert")
    public PageResult insertShift(Shift shift){

        if(shiftService.insert(shift)){
            return new PageResult("新增成功",200);
        }
        return new PageResult("新增失败",400);
    }
}
