package com.restful.quanlysinhvien.services.service_impl;

import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.restful.quanlysinhvien.domain.Role;
import com.restful.quanlysinhvien.domain.Student;

@Component("userDetailsService")
public class UserDetailsCustom implements UserDetailsService {
    private final StudentService studentService;

    public UserDetailsCustom(StudentService studentService) {
        this.studentService = studentService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Student student = this.studentService.findStudentByUsername(username);
        if (student == null) {
            throw new UsernameNotFoundException("Bad credentials");
        }
        // Gán role vào authority
        Role role = student.getRole();
        String roleName = "ROLE_" + role.getNameRole().toUpperCase(); // VD: ROLE_ADMIN
        GrantedAuthority authority = new SimpleGrantedAuthority(roleName);

        return new org.springframework.security.core.userdetails.User(
                student.getStudentCode(),
                student.getPassword(),
                Collections.singletonList(authority));
    }

}
