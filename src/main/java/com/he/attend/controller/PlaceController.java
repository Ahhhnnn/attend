package com.he.attend.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.he.attend.common.JsonResult;
import com.he.attend.common.PageResult;
import com.he.attend.model.Place;
import com.he.attend.model.Shift;
import com.he.attend.service.PlaceService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("${api.version}/place")
public class PlaceController {

    @Autowired
    private PlaceService placeService;

    @ApiOperation(value = "查询所有考勤地点", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "第几页", required = true, dataType = "Integer", paramType = "form"),
            @ApiImplicitParam(name = "limit", value = "每页多少条", required = true, dataType = "Integer", paramType = "form"),
            @ApiImplicitParam(name = "searchKey", value = "筛选条件字段", dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "searchValue", value = "筛选条件关键字", dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "access_token", value = "令牌", required = true, dataType = "String", paramType = "form")
    })
    @RequestMapping("/query")
    public PageResult<Place> list(Integer page, Integer limit, String searchKey, String searchValue) {
        if (page == null) {
            page = 0;
        }
        if (limit == null) {
            limit = 10;
        }
        Page<Place> placePage = new Page<>(page, limit);
        EntityWrapper<Place> wrapper = new EntityWrapper<>();
        if (searchKey != null && !searchKey.trim().isEmpty() && searchValue != null && !searchValue.trim().isEmpty()) {
            if(searchKey.equals("name")||searchKey.equals("rel_name")){
                wrapper.like(searchKey,searchValue);
                wrapper.orderBy("create_time", true);
            }
            else {
                wrapper.eq(searchKey, searchValue);
                wrapper.orderBy("create_time", true);
            }
        }
        placeService.selectPage(placePage, wrapper);
        List<Place> placesList = placePage.getRecords();
        return new PageResult<>(placesList, placePage.getTotal());
    }

    @RequestMapping("/insert")
    public PageResult insertPlace(Place place){

        if(placeService.insert(place)){
            return new PageResult("新增成功",200);
        }
        return new PageResult("新增失败",400);
    }

    @ApiOperation(value = "修改考勤地点状态", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "shiftId", value = "班次Id", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "state", value = "状态：0正常，1禁用", required = true, dataType = "Integer", paramType = "form"),
            @ApiImplicitParam(name = "access_token", value = "令牌", required = true, dataType = "String", paramType = "form")
    })
    @PutMapping("/statu")
    public JsonResult updateStatu(Integer id, Integer statu) {
        if (statu == null || (statu != 0 && statu != 1)) {
            return JsonResult.error("statu值需要在[0,1]中");
        }
        try {
            placeService.updateById(statu,id);
            return JsonResult.ok();
        }catch (Exception e) {
            log.error(e.getMessage(),e);
            return JsonResult.error();
        }
    }

    /**
     * 更新考勤地点
     * @param place
     * @return
     */
    @RequestMapping("/update")
    public PageResult updatePlace(Place place){
        try {
            placeService.update(place);
            return new PageResult("修改成功",200);
        }catch (Exception e) {
            log.error(e.getMessage(),e);
            return new PageResult("修改失败", 400);
        }
    }
}
