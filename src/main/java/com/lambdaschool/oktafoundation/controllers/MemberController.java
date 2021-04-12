package com.lambdaschool.oktafoundation.controllers;

import com.lambdaschool.oktafoundation.models.Member;
import com.lambdaschool.oktafoundation.repository.MemberRepository;
import com.lambdaschool.oktafoundation.utils.CsvUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@RestController
public class MemberController
{
    @Autowired
    MemberRepository memberRepository;

    @PostMapping(value = "/upload", consumes = "multipart/form-data")
    public void uploadSimple(MultipartFile csvfile)throws Exception {
        System.out.println(csvfile);
        memberRepository.saveAll(CsvUtils.read(Member.class, csvfile.getInputStream()));
    }

}