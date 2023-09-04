package com.example.demo.rest;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.DAO.StudentDOA;
import com.example.demo.model.Student;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
@RestController
@RequestMapping("/api")
public class StudentDAOController implements StudentDOA{
    private EntityManager entityManager;
    private List<Student> theStudents;
    // @PostConstruct
    // public void loadData(){
    //     TypedQuery<Student> theQuery = entityManager.createQuery("FROM Student", Student.class);
    //     theStudents = theQuery.getResultList();
    // }

    public StudentDAOController(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public Integer lengthOfList(){
        TypedQuery<Student> theQuery = entityManager.createQuery("FROM Student", Student.class);
          return theQuery.getResultList().size();
         
    }

    @GetMapping("/students/{studentId}")
    @Override
    public Student getStudent(@PathVariable Integer studentId) {
        TypedQuery<Student> theQuery = entityManager.createQuery("FROM Student WHERE ID=:theID", Student.class);
        theQuery.setParameter("theID", studentId.toString());
        Integer length = lengthOfList();
        if(studentId > length || studentId<=0){
            throw new StudentNotFoundException("Student Id not Found - " + studentId);
        }
        return theQuery.getSingleResult();
    }
    @GetMapping("/students/all")
    @Override
    public List<Student> findAllStudents() {
        TypedQuery<Student> theQuery = entityManager.createQuery("FROM Student", Student.class);
        theStudents = theQuery.getResultList();
        return theStudents;
    }  
    @Transactional
    @PostMapping("/students/insert")
    @Override
    public Map<String,String> insertStudent(@RequestBody Map<String,String> map) {
        Query theQuery = entityManager.createNativeQuery("INSERT INTO Student (first_name,last_name,email,date_of_birth,location,phoneNumber) VALUES (:userFirstName,:userLastName,:userEmail,:userDOB,:userLocation,:userPhoneNumber)", Student.class);
        theQuery.setParameter("userFirstName", map.get("firstName"));
        theQuery.setParameter("userLastName", map.get("lastName"));
        theQuery.setParameter("userEmail", map.get("email"));
        theQuery.setParameter("userDOB", map.get("dob"));
        theQuery.setParameter("userLocation", map.get("location"));
        theQuery.setParameter("userPhoneNumber", map.get("phoneNumber"));
       int numberOfRows = theQuery.executeUpdate();
       System.out.println(numberOfRows);
       return map;
    }
}

