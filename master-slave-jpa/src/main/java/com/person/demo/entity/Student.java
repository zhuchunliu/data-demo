package com.person.demo.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * User
 *
 * @author jimson
 * @date 2017/11/20
 */
@Data
@Table(name = "student")
@Entity
public class Student implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String mobile;

    @Column(name = "loginname")
    private String loginName;

    @Column(name = "username")
    private String userName;

    @Column(name = "gender")
    private String gender;

    @Column(name = "orgname")
    private String orgName;

    @Column(name = "deptname")
    private String deptName;

}
