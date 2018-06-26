package service;

import com.person.demo.MasterApplication;
import com.person.demo.dao.StudentMapper;
import com.person.demo.service.StudentService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by Darren on 2018-06-25
 **/
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = MasterApplication.class)
public class StudentServiceTest {

    @Autowired
    private StudentService studentService;


    @Test
    public void getStudent(){
        studentService.getStudent().forEach(System.err::println);
    }

    @Test
    public void deleteStudent(){
        studentService.deleteStudent();
    }

    @Test
    public void insertStudent(){
        studentService.insertStudent();
    }

    @Test
    public void updateStudentName(){
        studentService.updateStudentName();
    }

    @Test
    public void updateNoTrans(){
        studentService.updateNoTrans();
    }


}
