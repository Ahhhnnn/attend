package com.he.attend;

import com.he.attend.common.utils.DataUtils;
import com.he.attend.service.CalendarService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AttendApplicationTests {
	@Autowired
	private CalendarService calendarService;

	@Test
	public void contextLoads() {
	}


	@Test
	public void testCalendar()
	{
		List<Integer> list=calendarService.queryStaffIds();
		System.out.println(list);
	}

	@Test
	public void testGetDays(){
		DataUtils dataUtils=new DataUtils();
		List<String> daysList=dataUtils.getDays("2018-04-12","2018-05-20");
		System.out.println(daysList);
	}
}

