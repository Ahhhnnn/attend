package com.he.attend.controller.AppCon;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.he.attend.common.PageResult;
import com.he.attend.model.Attend;
import com.he.attend.model.Shift;
import com.he.attend.model.Staff;
import com.he.attend.service.AttendService;
import com.he.attend.service.ShiftService;
import com.he.attend.service.StaffService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.xml.crypto.Data;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 供移动端调用接口
 */
@Slf4j
@Api(value = "app登录接口", tags = "AppUser")
@RestController
@RequestMapping("/appUser")
public class LoginController {

    @Autowired
    private StaffService staffService;

    @Autowired
    private ShiftService shiftService;

    @Autowired
    private AttendService attendService;
    @RequestMapping("/login")
    public PageResult login(String phone,String password){
        List<Staff> staffs=staffService.loginByPsd(phone,password);
        if(!CollectionUtils.isEmpty(staffs)) {
            return new PageResult(200,"登录成功",staffs.size(),staffs);
        }
        return new PageResult("登录失败,没有此用户",404);
    }

    /**
     * 修改用户密码
     * @param staffId
     * @param oldPassword
     * @param newPassword
     * @return
     */
    @RequestMapping("/repsd")
    public PageResult rePsd(Integer staffId,String oldPassword,String newPassword){
        EntityWrapper<Staff> entityWrapper=new EntityWrapper<>();
        entityWrapper.eq("staff_id",staffId);
        Staff staff=staffService.selectOne(entityWrapper);
        if(!staff.getPassword().equals(oldPassword)){
            return new  PageResult("原始密码输入错误！",400);
        }
        staff.setPassword(newPassword);
        staffService.updateById(staff);
        return new PageResult("修改密码成功!",200);
    }


    /**
     * app用户打卡接口
     * @param staffId 人员id
     * @param attendTime 打卡时间
     * @param place 打卡地点
     * @param shiftId 打卡班次
     * @return
     */
    @RequestMapping("/attend")
    public PageResult attend(Integer staffId,String attendTime,String place,Integer shiftId,String placeType){
        Staff staff=staffService.queryByStaffId(staffId);
        Shift shift=shiftService.getShiftById(shiftId);
        Attend attend=new Attend();
        attend.setStaffId(staffId);
        attend.setPlaceType(placeType);
        attend.setStaffName(staff.getStaffName());
        attend.setShiftId(shiftId);
        attend.setShiftName(shift.getShiftName());
        attend.setDeptId(staff.getDeptId());
        attend.setDeptName(staff.getDeptName());
        //设置打卡类型 根据打卡时间判断打卡类型  2019-03-30 17:32:18
        //判断是早上还是下午打卡
        String time=attendTime.split(" ")[1];
        String attendDay=attendTime.split(" ")[0];
        isDeleteCalendar(staffId,attendDay);
        SimpleDateFormat simfor=new SimpleDateFormat("HH:mm:ss");
        try {
            if((simfor.parse(time).before(simfor.parse("12:00:00")))&&(simfor.parse(time).before(simfor.parse(shift.getBeginTime())))){
                //在12点前及打卡开始时间之前打卡 是正常打卡
                attend.setType("正常");
            }
            if ((simfor.parse(time).before(simfor.parse("12:00:00")))&&(simfor.parse(time).after(simfor.parse(shift.getBeginTime())))){
                //在12点前及打卡开始时间之后打 迟到打卡
                attend.setType("迟到");
            }
            if ((simfor.parse(time).after(simfor.parse("12:00:00")))&&(simfor.parse(time).before(simfor.parse(shift.getEndTime())))){
                //在12点之后及打卡结束时间之前打 早退打卡
                attend.setType("早退");
            }
            if ((simfor.parse(time).after(simfor.parse("12:00:00")))&&(simfor.parse(time).after(simfor.parse(shift.getEndTime())))){
                //在12点之后及打卡结束时间之后打 正常打卡
                attend.setType("正常");
            }
        }catch (ParseException e) {
            log.error(e.getMessage(), e);
        }
        attend.setPlace(place);
        attend.setAttendTime(attendTime);

        attendService.insert(attend);
        return new  PageResult("打卡成功",200);
    }

    private void isDeleteCalendar(Integer staffId,String attendDay){
        List<Attend> attendList=attendService.queryByStaffIdAndAttendDay(staffId,attendDay);
        //list中可能有三种情况， 无记录 （第一次打卡） 有一次记录（第二次打卡） 有两条记录（多次打卡）
        if(CollectionUtils.isEmpty(attendList)){

        }else if(attendList.size()==1){

        }else if(attendList.size()==2){
            Attend attend=attendList.get(0);//第一个为最后插入的打卡记录
            attendService.deleteById(attend.getAttendId());
        }
    }
}
