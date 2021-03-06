package org.lgj.ktp.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.lgj.ktp.dto.StudentListDTO;
import org.lgj.ktp.entity.Student;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface StudentMapper {
    int deleteByPrimaryKey(String id);

    int insert(Student record);

    int insertSelective(Student record);

    Student selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Student record);

    int updateByPrimaryKey(Student record);
    
    //注册
    int register(Student student);
    
    //登录
    Student login(@Param("phoneMail")String phoneMail,@Param("password")String password);
    
    //获取学生列表
    List<StudentListDTO> getStudentList(@Param("courseId")String courseId);
    
    //获取学生姓名
    List<String> getStudentName(@Param("courseId")String courseId);
    
    List<String> getTeacherName(@Param("courseId")String courseId);
    
    String checkPassword(@Param("userId")String userId,@Param("password")String passeord);
    
    int deleteCourse(@Param("userId")String userId,@Param("courseId")String courseId);
}