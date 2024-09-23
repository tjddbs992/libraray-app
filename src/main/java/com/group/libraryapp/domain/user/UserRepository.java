package com.group.libraryapp.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// @Repository 어노테이션을 붙이지 않더라도, JpaRepostiroy를 상속받으면서 스프링 빈을 주입받음.
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByName(String name);
    // 반환 타입은 User이다. 유저가 없다면, null이 반환된다.
    // 함수 이름만 작성하면, 알아서 SQL이 조립된다!
    // find라고 작성하면, 1개의 데이터만 가져온다.
    // By 뒤에 붙는 필드 이름으로 SELECT 쿼리의 WHERE 문이 작성된다.
    // SELECT * FROM user WHERE name = ?;

    // By 앞에 들어갈 수 있는 구절 정리
    // find : 1건을 가져온다. 반환 타입은 객체가 될 수도 있고, Optional<타입>이 될 수도 있다.
    // findAll : 쿼리의 결과물이 N개인 경우 사용. List<타입> 반환.
    // exists : 쿼리 결과가 존재하는지 확인. 반환 타입은 boolean
    // count : SQL의 결과 개수를 센다. 반환 타입은 long이다.

    // By 뒤에 들어갈 수 있는 구절 정리
    // 각 구절은 And 나 Or로 조합할 수도 있다.
    // ex) List<User> findAllByNameAndAge(String name, int age); -> SELECT * FROM user WHERE name = ? AND age = ?;
    // GreaterThan : 초과
    // GreaterThanEqual : 이상
    // LessThan : 미만
    // LessThanEqual : 이하
    // Between : 사이에
    // ex) List<User> findAllByAgeBetween(int startAge, int endAge); -> SELECT * FROM user WHERE age BETWEEN ? AND ?;
    // StartsWith : ~로 시작하는
    // EndsWith : ~로 끝나는
}
