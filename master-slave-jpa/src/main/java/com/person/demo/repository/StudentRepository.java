package com.person.demo.repository;


import com.person.demo.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by zhuchunliu on 2017/7/28.
 */
    public interface StudentRepository extends JpaRepository<Student,Integer> {

    @Query("select a from Student a where a.userName = ?1")
    List<Student> findByName(String name);

    @Modifying
    @Transactional
    @Query("update Student a set a.userName = ?2 where a.loginName = ?1 ")
    void updateStudent(String loginName,String userName);
}
