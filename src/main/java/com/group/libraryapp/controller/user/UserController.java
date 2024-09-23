package com.group.libraryapp.controller.user;

import com.group.libraryapp.dto.user.request.UserCreateRequest;
import com.group.libraryapp.dto.user.request.UserUpdateRequest;
import com.group.libraryapp.dto.user.response.UserResponse;
import com.group.libraryapp.service.user.UserServiceV1;
import com.group.libraryapp.service.user.UserServiceV2;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
public class UserController { // jdbcTemplate을 이용해서 sql을 날릴 수 있음!
    private final UserServiceV2 userService;

    // 생성자를 만들어서 jdbcTemplate을 파라미터로 넣으면, 자동으로 들어옴!
    // 2, 서비스, 레포지터리 클래스들도 스프링 빈으로 만들어서 jdbcTemplate은 레포지터리에서만 받아오고,
    // 나머지 클래스는 생성자에 jdbcTemplate을 받아올 필요가 없으므로 그냥 파라미터로 클래스를 받아옴!
    public UserController(UserServiceV2 userService) {
        this.userService = userService;
    }

//    @PostMapping("/user")
//    public void saveUser(@RequestBody UserCreateRequest request) {
//        String sql = "insert into user (name, age) values (?, ?)";
//        jdbcTemplate.update(sql, request.getName(), request.getAge());
//    }
    @PostMapping("/user")
    public void saveUser(@RequestBody UserCreateRequest request) {
        userService.saveUser(request);
    }

//    @GetMapping("/user")
//    public List<UserResponse> getUsers() {
//        String sql = "select * from user";
//        // control O 눌러서 maprow 오버라이드
//        // maprow의 역할은 user 정보를 UserResponse 타입으로 바꿔주는 역할. query의 결과를 받아 객체를 반환.
//        return jdbcTemplate.query(sql, (rs, rowNum) -> { // 람다로 바꿨음.
//            long id = rs.getLong("id");
//            String name = rs.getString("name");
//            int age = rs.getInt("age");
//            return new UserResponse(id, name, age);
//        });
//    }

    @GetMapping("/user")
    public List<UserResponse> getUsers() {
        return userService.getUsers();
    }

//    @PutMapping("/user")
//    public void updateUser(@RequestBody UserUpdateRequest request) {
//        String readSql = "select * from user where id = ?";
//        boolean isUserNotExist = jdbcTemplate.query(readSql, (rs, rowNum) -> 0, request.getId()).isEmpty();
//        // readSql의 ? 부분에 id를 넣어라. 그리고 select sql의 결과가 반환된다면 0으로 변환해라. query는 반환된 값(0)을 리스트로 감싸준다.
//        // 해당 id의 유저가 있다면 결과가 반환돼서 [0]이 반환되고, 유저가 없다면 빈 리스트 []가 반환된다. 이걸 판단해서 불리안 값으로 넣는다.
//        if (isUserNotExist) {
//            throw new IllegalArgumentException();
//        }
//
//        String sql = "update user set name = ? where id = ?";
//        jdbcTemplate.update(sql, request.getName(), request.getId());
//    }
    // 컨트롤러의 함수 한개의 역할
    // 1. API의 진입 지점으로써 HTTP Body를 객체로 변환하고있다. -> Controller 역할
    // 2. 현재 유저가 있는지, 없는지 등을 확인하고 예외 처리를 해준다. -> Service 역할
    // 3. SQL을 사용해 실제 DB와의 통신을 담당한다. -> Repository 역할    이 세가지로 각각 다시 나누기!
    // controller <- service <- repository : layered architecture (dto는 이 중간중간에 데이터를 넘겨주면서 사용됨)

    @PutMapping("/user")
    public void updateUser(@RequestBody UserUpdateRequest request) {
        userService.updateUser(request);
    }



//    @DeleteMapping("/user")
//    // 이 부분도 객체로 만들 수 있지만 쿼리가 하나라서 그냥 @RequestParam 사용함
//    public void deleteUser(@RequestParam String name) {
//        String readSql = "select * from user where name = ?";
//        boolean isUserNotExist = jdbcTemplate.query(readSql, (rs, rowNum) -> 0, name).isEmpty();
//        if (isUserNotExist) {
//            throw new IllegalArgumentException();
//        }
//
//        String sql = "delete from user where name = ?";
//        jdbcTemplate.update(sql, name);
//    }

    @DeleteMapping("/user")
    public void deleteUser(@RequestParam String name) {
        userService.deleteUser(name);
    }
}
