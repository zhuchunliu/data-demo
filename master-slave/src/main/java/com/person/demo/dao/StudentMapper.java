package com.person.demo.dao;

import com.person.demo.model.Student;
import com.person.demo.util.TkMapper;

import java.util.List;

/**
 * Created by Darren on 2018-06-25
 **/
public interface StudentMapper extends TkMapper<Student>{
    List<Student> getAllStudent();
}
