package service;

import com.person.demo.MasterJpaApplication;
import com.person.demo.config.DbContextHolder;
import com.person.demo.repository.StudentRepository;
import com.person.demo.service.StudentJpaService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by Darren on 2018-06-27
 **/
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = MasterJpaApplication.class)
public class StudentJpaServiceTest {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private StudentJpaService studentJpaService;

    @Test
    public void getAllStudent(){
//        DbContextHolder.setDbType(DbContextHolder.DbType.master);
        studentRepository.findAll().forEach(System.err::println);
    }




    @Test
    public void getStudent(){
        studentJpaService.getStudent().forEach(System.err::println);
    }

    @Test
    public void deleteStudent(){
        studentJpaService.deleteStudent();
    }

    @Test
    public void insertStudent(){
        studentJpaService.insertStudent();
    }

    @Test
    public void updateStudentName(){
        studentJpaService.updateStudentName();
    }

    @Test
    public void updateNoTrans(){
        studentJpaService.updateNoTrans();
    }



}
