package com.person.demo.service;

import com.person.demo.dao.StudentMapper;
import com.person.demo.model.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * Created by Darren on 2018-06-25
 **/
@Service
public class StudentService {

    @Autowired
    private StudentMapper studentMapper;

    public List<Student> getStudent(){
        return studentMapper.getAllStudent();
    }

    public void deleteStudent(){
        Example example = new Example(Student.class);
        example.createCriteria().andEqualTo("userName","test");
        studentMapper.deleteByExample(example);
    }

    public void insertStudent(){
        Student student = new Student();
        student.setUserName("test");
        studentMapper.insert(student);
    }

    @Transactional
    public void updateStudentName(){
        Student student = studentMapper.selectByPrimaryKey(2);
        student.setUserName(student.getUserName()+123);
        studentMapper.updateByPrimaryKey(student);
    }

    public void updateNoTrans(){
        Student student = studentMapper.selectByPrimaryKey(2);
        student.setUserName(student.getUserName()+123);
        studentMapper.updateByPrimaryKey(student);
    }
}
