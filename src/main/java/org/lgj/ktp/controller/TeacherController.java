package org.lgj.ktp.controller;

import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpSession;

import org.apache.ibatis.annotations.Param;
import org.lgj.ktp.dto.DeleteCourseDTO;
import org.lgj.ktp.dto.LoginInfo;
import org.lgj.ktp.dto.TeacherInfoDTO;
import org.lgj.ktp.entity.Teacher;
import org.lgj.ktp.service.TeacherService;
import org.lgj.ktp.util.JSONResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


@RequestMapping("/teacher")
@RestController
@CrossOrigin
@Api
public class TeacherController {
	@Autowired
	private TeacherService teacherService;
	
	/**
	 * 用户注册   2019-12-03
	 * @param user
	 * @return
	 */
	@ApiOperation(value = "注册",notes = "注册")
	@RequestMapping(value = "/register",method = RequestMethod.POST)
	public JSONResult regisster(@RequestBody Teacher teacher) {
		JSONResult jsonResult = new JSONResult();
		teacher.setId(UUID.randomUUID().toString().replace("-", ""));
		boolean success = teacherService.register(teacher);
		if(success) {
			jsonResult.setMessage("success");
		}
		else {
			jsonResult.setMessage("手机或邮箱已注册");
		}
		return jsonResult;
	}
	
	/**
	 * 登录 2019-12-03
	 * @param loginInfo
	 * @return
	 */
	@ApiOperation(value = "登录",notes = "登录")
	@RequestMapping(value = "/login",method = RequestMethod.POST)
	public JSONResult login(@RequestBody LoginInfo loginInfo,HttpSession session) {
		JSONResult jsonResult = new JSONResult();
		Teacher teacher = teacherService.login(loginInfo);
		if(teacher != null) {
			jsonResult.setMessage("success");
			session.setAttribute("user", teacher);
			jsonResult.setData(teacher);
		}
		else {
			jsonResult.setMessage("登录失败");
		}
		return jsonResult;
	}
	
	/**
	 * 获取教师信息
	 * @param courseId
	 * @return
	 */
	@RequestMapping(value = "/getTeacherInfo",method = RequestMethod.GET)
	@ApiOperation(value = "获取教师信息",notes = "获取教师信息")
	public TeacherInfoDTO getTeacherInfo(@RequestParam("courseId")String courseId) {
		TeacherInfoDTO teacherInfoDTO =  teacherService.getTeacherInfo(courseId);
		return teacherInfoDTO;
	}
	
	/**
	 * 获取教师名字
	 */
	@RequestMapping(value = "/getTeacherName",method = RequestMethod.GET)
	@ApiOperation(value = "获取教师名字",notes = "获取教师名字")
	public List<String> getTeacherName(@RequestParam("studentId")String studentId){
		List<String> teacherName = teacherService.getTeacherName(studentId);
		return teacherName;
	}
	
	@RequestMapping(value = "/deleteHomework",method = RequestMethod.POST)
	@ApiOperation(value = "删除作业",notes = "删除作业")
	public String deleteHomework(@RequestParam("homeworkId")String homeworkId) {
		boolean success = teacherService.deleteHomework(homeworkId);
		if(success) {
			return "success";
		}
		else {
			return "false";
		}
	}
	
	
	@RequestMapping(value = "/deleteCourse",method = RequestMethod.POST)
	@ApiOperation(value = "删除作业",notes = "删除作业")
	public String deleteCourse(@RequestBody DeleteCourseDTO deleteCourseDTO) {
		String name = teacherService.checkPassword(deleteCourseDTO.getUserId(),deleteCourseDTO.getPassword());
		if(name != null) {
			//删除课程作业
			boolean success1 = teacherService.deleteworkByCourseId(deleteCourseDTO.getCourseId());
			//删除选课表信息
			boolean success2 = teacherService.deleteSelectCourse(deleteCourseDTO.getCourseId());
			//删除课程
			boolean success = teacherService.deleteCourse(deleteCourseDTO.getCourseId());
			if(success && success1 && success2) {
				return "success";
			}
			else {
				return "false";
			}
		}
		else {
			return "密码错误";
		}
		
	}
}
