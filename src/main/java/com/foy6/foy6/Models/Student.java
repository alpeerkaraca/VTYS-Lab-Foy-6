package com.foy6.foy6.Models;

import java.util.Objects;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

public class Student {
    private static final long serialVersionUID = 1L;

    @Id
    @Field("student_no")
    private String studentNo;

    @Field("student_name")
    private String studentName;

    @Field("department")
    private String department;

    public Student() {
    }

    public Student(String studentNo, String studentName, String department) {
        this.studentNo = studentNo;
        this.studentName = studentName;
        this.department = department;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public String getStudentNo() {
        return studentNo;
    }

    public void setStudentNo(String studentNo) {
        this.studentNo = studentNo;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    @Override
    public int hashCode() {
        return Objects.hash(studentNo);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Student other = (Student) obj;
        if (studentNo == null) {
            if (other.studentNo != null)
                return false;
        } else if (!studentNo.equals(other.studentNo))
            return false;
        return true;
    }
}
