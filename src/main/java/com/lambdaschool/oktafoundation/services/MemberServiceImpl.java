package com.lambdaschool.oktafoundation.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service(value = "memberService")
public class MemberServiceImpl implements MemberService
{
}