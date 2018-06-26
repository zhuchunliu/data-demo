package com.person.demo.model;

import lombok.Data;
import tk.mybatis.mapper.annotation.NameStyle;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * User
 *
 * @author jimson
 * @date 2017/11/20
 */
@Data
@Table(name = "student")
@NameStyle
public class Student implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String mobile;

    private String loginName;

    private String userName;

    private String gender;

    private String orgName;

    private String deptName;

}
