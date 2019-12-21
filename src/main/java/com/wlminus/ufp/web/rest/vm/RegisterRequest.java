package com.wlminus.ufp.web.rest.vm;

import java.util.List;

public class RegisterRequest {
    private String studentCode;
    private List<String> requestCourse;

    public RegisterRequest() {
    }

    public RegisterRequest(String studentCode, List<String> requestCourse) {
        this.studentCode = studentCode;
        this.requestCourse = requestCourse;
    }

    public String getStudentCode() {
        return studentCode;
    }

    public void setStudentCode(String studentCode) {
        this.studentCode = studentCode;
    }

    public List<String> getRequestCourse() {
        return requestCourse;
    }

    public void setRequestCourse(List<String> requestCourse) {
        this.requestCourse = requestCourse;
    }

    @Override
    public String toString() {
        return "RegisterRequest{" +
            "studentCode='" + studentCode + '\'' +
            ", requestCourse=" + requestCourse +
            '}';
    }
}
