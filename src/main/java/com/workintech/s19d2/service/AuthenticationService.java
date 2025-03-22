package com.workintech.s19d2.service;


import com.workintech.s19d2.entity.Member;
import com.workintech.s19d2.entity.Role;
import com.workintech.s19d2.repository.MemberRepository;
import com.workintech.s19d2.repository.RoleRepository;
import lombok.AllArgsConstructor;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@AllArgsConstructor
public class AuthenticationService {


    private final MemberRepository memberRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;


    public Member register(String email, String password) {
        Optional<Member> members = memberRepository.findByEmail(email);
        if (members.isPresent()) {

            throw new RuntimeException("User with given email already exist: " + email);
        }


        String encodedPassword = passwordEncoder.encode(password);

        Role adminRole = roleRepository.findByAuthority("ADMIN").orElseThrow(() -> new RuntimeException("ADMIN role not found"));




        Member newMember = new Member();
        newMember.setEmail(email);
        newMember.setPassword(encodedPassword);
        newMember.setRoles(List.of(adminRole));


        return memberRepository.save(newMember);

    }


}
