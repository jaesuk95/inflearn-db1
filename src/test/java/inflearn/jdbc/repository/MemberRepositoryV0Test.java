package inflearn.jdbc.repository;

import inflearn.jdbc.domain.Member;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

class MemberRepositoryV0Test {

    MemberRepositoryV0 repositoryVO = new MemberRepositoryV0();

    @Test
    void save() throws SQLException {
        Member memberVO = new Member("memberVO", 10000);
        repositoryVO.save(memberVO);
    }
}