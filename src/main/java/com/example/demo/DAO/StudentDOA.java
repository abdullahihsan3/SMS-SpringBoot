package com.example.demo.DAO;

import java.util.List;
import java.util.Map;


import com.example.demo.model.Student;

public interface StudentDOA {
    Student getStudent(Integer Id);
    List<Student> findAllStudents();
    Map<String,String> insertStudent(Map<String,String> student);
}
