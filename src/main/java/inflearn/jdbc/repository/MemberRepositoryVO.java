package inflearn.jdbc.repository;

import inflearn.jdbc.connection.DBConnectionUtil;
import inflearn.jdbc.domain.Member;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;

/**
 *  JDBC - DriverManager 사용
 * */

@Slf4j
public class MemberRepositoryVO {

    public Member save(Member member) throws SQLException {
//        String sql ="insert into member(member_id, money) value(?,?)";
        String sql ="insert into member(member_id, money) values (?, ?)";

        String sql2 = "select * from member";
        Connection connection = null; // 연결
        PreparedStatement preparedStatement = null; // 데이터 베이스에 쿼리 날린다


        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, member.getMemberId());
            preparedStatement.setInt(2, member.getMoney());
            preparedStatement.executeUpdate();
            return member;
        } catch (SQLException e) {
            log.error("db Error ", e);
            throw e;
        } finally {
            close(connection,preparedStatement,null);
        }
    }

    private void close(Connection connection, Statement statement, ResultSet resultSet){    // statement 기본 Sql, preparedStatement 는 더 기능들이 많고 Binding 되어 있다
        if (resultSet != null){
            try {
                resultSet.close();  // close 는 반드시 해준다. 이유: 리소스를 계속 사용하게 된다 // 시작과 역 순으로 close , 연결이 안ㄲㅡㄴㅎ어 질 수 있다.
            } catch (SQLException e) {
                log.info("error ",e);
            }
        }
        if (statement != null){
            try {
                statement.close();  // close 는 반드시 해준다. 이유: 리소스를 계속 사용하게 된다 // 시작과 역 순으로 close , 연결이 안ㄲㅡㄴㅎ어 질 수 있다.
            } catch (SQLException e) {
                log.info("error ",e);
            }

        }
        if (connection != null){
            try {
                connection.close();
            } catch (SQLException e) {
                log.info("error ",e);
            }
        }
    }

    private Connection getConnection() {
        return DBConnectionUtil.getConnection();
    }

}
