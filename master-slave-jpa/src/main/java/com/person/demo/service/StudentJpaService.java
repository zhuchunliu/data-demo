package com.person.demo.service;

import com.person.demo.entity.Student;
import com.person.demo.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by Darren on 2018-06-27
 **/
@Service
public class StudentJpaService {

    @Autowired
    private StudentRepository studentRepository;

    public List<Student> getStudent(){
        return studentRepository.findAll();
    }

    public void deleteStudent(){
        List<Student> list = studentRepository.findByName("test");
        if(null != list && 0 != list.size()) {
            studentRepository.delete(list.get(0));
        }
    }

    public void insertStudent(){
        Student student = new Student();
        student.setUserName("test");
        studentRepository.save(student);
    }

    @Transactional
    public void updateStudentName(){
        Student student = studentRepository.findOne(2);
        System.err.println(student);
        student.setUserName(student.getUserName()+123);
        studentRepository.save(student);
    }

    public void updateNoTrans(){
        Student student = studentRepository.findOne(2);
        System.err.println(student);
        student.setUserName(student.getUserName()+123);
        studentRepository.updateStudent("admin","管理员123");
        studentRepository.save(student);
    }
}
