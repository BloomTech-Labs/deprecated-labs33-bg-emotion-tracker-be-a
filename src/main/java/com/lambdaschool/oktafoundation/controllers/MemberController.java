package com.lambdaschool.oktafoundation.controllers;

import com.lambdaschool.oktafoundation.models.Member;
import com.lambdaschool.oktafoundation.models.User;
import com.lambdaschool.oktafoundation.repository.MemberRepository;
import com.lambdaschool.oktafoundation.services.MemberService;
import com.lambdaschool.oktafoundation.utils.CsvUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/members")
public class MemberController
{
    @Autowired
    MemberRepository memberRepository;

    @Autowired
    MemberService memberService;

    @PreAuthorize("hasAnyRole('SUPERADMIN')")
    @GetMapping(value = "/members",
            produces = "application/json")
    public ResponseEntity<?> listAllMembers()
    {
        List<Member> myMembers = memberService.findAll();
        return new ResponseEntity<>(myMembers,
                HttpStatus.OK);
    }

    @PostMapping(value = "/upload", consumes = "multipart/form-data")
    public void uploadSimple(MultipartFile csvfile)throws Exception {
        System.out.println(csvfile);
        memberRepository.saveAll(CsvUtils.read(Member.class, csvfile.getInputStream()));
    }

}