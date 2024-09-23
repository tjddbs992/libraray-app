package com.group.libraryapp.repository.user;

import com.group.libraryapp.dto.user.response.UserResponse;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository // 스프링 빈으로 등록!!, jdbcTemplate을 바로 가져올 수 있음!
public class UserJdbcRepository {
    // sql을 직접 작성하면, 에러가 컴파일 시점에 발견되지 않고, 런타임 시점에 발견되는 점이 좋지 않다.
    // sql을 직접 작성하면, 특정 데이터베이스에 종속적이게 된다.
    // sql을 직접 작성하면, 테이블을 하나 만들 때마다 CRUD 쿼리가 항상 필요해서 반복 작업이 많아진다.
    // sql을 직접 작성하면, 데이터베이스의 테이블과 객체는 패러다임이 다르다.
    // (객체에서 학생과 교실은 서로를 가리킬 수 있지만, 디비에서는 학생만 id로 교실을 가리킴. 또는 부모 클래스와 자식 클래스의 상속 문제가 다름)
    // -> JPA를 사용해서 해결!
    private final JdbcTemplate jdbcTemplate;

    public UserJdbcRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public boolean isUserNotExist(long id){
        String readSql = "select * from user where id = ?";
        return jdbcTemplate.query(readSql, (rs, rowNum) -> 0, id).isEmpty();
    }

    public void updateUserName(String name, long id){
        String sql = "update user set name = ? where id = ?";
        jdbcTemplate.update(sql, name, id);
    }

    public boolean isUserNotExist(String name){
        String readSql = "select * from user where name = ?";
        return jdbcTemplate.query(readSql, (rs, rowNum) -> 0, name).isEmpty();
    }

    public void deleteUser(String name) {
        String sql = "delete from user where name = ?";
        jdbcTemplate.update(sql, name);
    }

    public List<UserResponse> getUsers(){
        String sql = "select * from user";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            long id = rs.getLong("id");
            String name = rs.getString("name");
            int age = rs.getInt("age");
            return new UserResponse(id, name, age);
        });
    }

    public void saveUser(String name, int age){
        String sql = "insert into user (name, age) values (?, ?)";
        jdbcTemplate.update(sql, name, age);
    }
}
