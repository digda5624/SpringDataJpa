package study.datajpa.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class testRepositoryTest {

    @Autowired
    TestRepository t;

    @Test
    @DisplayName("[sucess] 이거_됨?")
    public void 이거_됨(){
        // given
        t.test();
        t.test();
        // when

        // then
    }
}