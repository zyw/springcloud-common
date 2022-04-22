package cn.v5cn.serialize.protobuf.entity;

import java.util.List;

/**
 * @author ZYW
 * @version 1.0
 * @date 2020-02-25 14:25
 */
public class School {
    private  String schoolName;
    private List<Student> students;
    public School(String schoolName, List<Student> students) {
        this.schoolName = schoolName;
        this.students = students;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }
}
