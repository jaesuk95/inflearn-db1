package inflearn.jdbc.repository;

import inflearn.jdbc.domain.Member;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class MemberRepositoryVOTest {

    MemberRepositoryVO repositoryVO = new MemberRepositoryVO();

    @Test
    void save() throws SQLException {
        Member memberVO = new Member("memberVO", 10000);
        repositoryVO.save(memberVO);
    }
}