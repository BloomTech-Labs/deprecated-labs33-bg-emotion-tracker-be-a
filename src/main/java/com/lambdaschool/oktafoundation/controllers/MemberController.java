package com.lambdaschool.oktafoundation.controllers;

import com.lambdaschool.oktafoundation.models.Member;
import com.lambdaschool.oktafoundation.repository.MemberRepository;
import com.lambdaschool.oktafoundation.services.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/members")
public class MemberController {
    @Autowired
    MemberRepository memberRepository;

    @Autowired
    MemberService memberService;

    @PreAuthorize("hasAnyRole('SUPERADMIN, CLUBDIR')")
    @GetMapping(value = "/members",
            produces = "application/json")
    public ResponseEntity<?> listAllMembers() {
        List<Member> myMembers = memberService.findAll();
        return new ResponseEntity<>(myMembers,
                HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('SUPERADMIN, CLUBDIR')")
    @PostMapping(value = "/upload", consumes = "multipart/form-data", produces = "application/json")
    public ResponseEntity<?> uploadMembers(MultipartFile csvfile) throws Exception {
        List<Member> addedMembers = memberService.saveNewMembers(csvfile.getInputStream());
        if (addedMembers.size() < 1) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(addedMembers, HttpStatus.CREATED);
        }
    }

    // New code starts here
    @PreAuthorize("hasAnyRole('SUPERADMIN, CLUBDIR')")
    @GetMapping(value = "/member/{javaId}",
            produces = "application/json")
    public ResponseEntity<?> getMemberByJavaId(
            @PathVariable
                    Long javaId) {
        Member m = memberService.findMemberByJavaId(javaId);
        return new ResponseEntity<>(m, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('SUPERADMIN, CLUBDIR')")
    @GetMapping(value = "/member/{memberId}",
            produces = "application/json")
    public ResponseEntity<?> getMemberByMemberId(
            @PathVariable String memberId) {
        Member m = memberService.findMemberByStringId(memberId);
        return new ResponseEntity<>(m, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('SUPERADMIN, CLUBDIR')")
    @GetMapping(value = "/member/id/like/{partialmemberId}",
            produces = "application/json")
    public ResponseEntity<?> getMemberLikeId(
            @PathVariable String partialmemberId) {
        List<Member> m = memberService.findByIdContaining(partialmemberId);
        return new ResponseEntity<>(m, HttpStatus.OK);
    }

    @DeleteMapping(value = "/member/{javaId}")
    public ResponseEntity<?> deleteMemberByJavaId(
            @PathVariable long javaId) {
        memberService.delete(javaId);
        return new ResponseEntity<>(HttpStatus.OK);

    }

    @PostMapping(value = "/member",
            consumes = "application/json")
    public ResponseEntity<?> addNewMember(
            @RequestBody
                    String newmember) throws URISyntaxException {


        Member addedMember = memberService.saveNewMember(newmember);

        HttpHeaders responseHeaders = new HttpHeaders();
        URI addedMemberURI = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{memberid}")
                .buildAndExpand(addedMember.getId())
                .toUri();
        responseHeaders.setLocation(addedMemberURI);

        return new ResponseEntity<>(null,
                responseHeaders,
                HttpStatus.CREATED);
    }
}