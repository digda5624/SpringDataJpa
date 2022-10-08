package study.datajpa.controller;

import lombok.AllArgsConstructor;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import study.datajpa.entity.Member;
import study.datajpa.repository.MemberRepository;

import java.util.Map;

@RestController
@AllArgsConstructor
public class HelloController {

    MemberRepository memberRepository;


    @GetMapping("/hello")
    public String hello(){
        return "hello";
    }

    @GetMapping("/osiv")
    public void test(){
        Member member = new Member("test");
        Member member2 = new Member("test");

        memberRepository.save(member);
        memberRepository.save(member2);
        Map<Object, Object> resourceMap = TransactionSynchronizationManager.getResourceMap();
        throw new RuntimeException();
//        return;

    }
}
