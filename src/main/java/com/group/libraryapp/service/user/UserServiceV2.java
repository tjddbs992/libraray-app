package com.group.libraryapp.service.user;


import com.group.libraryapp.domain.user.User;
import com.group.libraryapp.domain.user.UserRepository;
import com.group.libraryapp.dto.user.request.UserCreateRequest;
import com.group.libraryapp.dto.user.request.UserUpdateRequest;
import com.group.libraryapp.dto.user.response.UserResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceV2 {
    private final UserRepository userRepository;

    public UserServiceV2(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // 아래 있는 함수가 시작될 때 start transaction;을 해준다 (트랜잭션을 시작!)
    // 함수가 예외 없이 잘 끝났다면 commit
    // 혹시라도 문제가 있다면 rollback
    // 주의! IOException과 같은 Checked Exception은 롤백이 일어나지 않는다.
    @Transactional // 서비스 계층에 적용. 비즈니스 로직 또는 sql 접근을 해야하는 경우 트랜잭션 사용.
    public void saveUser(UserCreateRequest request) {
        // save 메소드에 객체를 넣어주면 INSESRT SQL이 자동으로 날라간다! save되고 난 후의 User는 id가 들어있다!
        User u = userRepository.save(new User(request.getName(), request.getAge()));
    }
    // 영속성 컨텍스트는 트랜잭션이 시작할 때 시작하고, 종료될 때 같이 종료된다.
    // 1. 변경 감지 (Dirty Check)
    // 영속성 컨텍스트 안에서 불러와진 Entity는 명시적으로 save(UserRepostiroy.save)하지 않더라도, 변경을 감지해 자동 저장된다.
    // 2. 쓰기 지연
    // DB의 INSERT / UPDATE / DELETE SQL을 바로 날리는 것이 아니라, 트랜잭션이 commit될 때 모아서 한 번만 날린다.
    // 3. 1차 캐싱
    // ID를 기준으로 Entity를 기억한다! 이렇게 캐싱된 객체는 완전히 동일하다! (객체 인스턴스의 주소까지 완전히 동일하다는 의미)
    // (정보 조회 최초 한번만 디비와 통신하고, 다음에 또 같은 아이디의 유저 정보를 불러올 때 디비와 통신 없이 캐시메모리에서 넘겨줌)
    // 4. 지연 로딩(Lazy Loading)
    // 연결되어 있는 객체를 꼭 필요한 순간에만 로딩한다!! @OneToMany의 fetch 옵션
    // fetch 옵션의 Lazy를 Eager로 바꾸면, 한번에 데이터를 가져오게 바꿀 수 있다.

    @Transactional(readOnly = true)  // readOnly는 데이터를 조회만 하는 기능일 때 사용
    // 데이터 수정이나 삭제 등을 위한 불필요한 기능이 빠져서 약간의 성능적 이점이 있음.
    public List<UserResponse> getUsers(){
        // List<User> users = userRepository.findAll(); 더 간단하게 변경
        return userRepository.findAll().stream().map(user -> new UserResponse(user.getId(), user.getName(), user.getAge()))
                .collect(Collectors.toList());
    } // findAll을 사용하면 모든 데이터를 가져온다! select * from user; , UserResponse에 생성자를 추가하면 코드를 더 깔끔하게 할 수 있음.

    @Transactional
    public void updateUser(UserUpdateRequest request) {
        // select * from user where id = ?;
        User user = userRepository.findById(request.getId())
                .orElseThrow(IllegalArgumentException::new); // Optional<User> 이 자바의 옵셔널 기능에서 orElseThrow 사용
        // user가 없는 경우 예외를 던지고, user를 찾았다면 user에 들어간다.

        user.updateName(request.getName());
        // userRepository.save(user); : 영속성 컨텍스트의 변경 감지 특성으로 인해서, save 따로 하지않아도 자동 저장됨.
        // 객체를 업데이트해주고, save 메소드를 호출한다. 그러면 자동으로 UPDATE SQL이 날라가게 된다.
    }

    @Transactional
    public void deleteUser(String name) {
        // SELECT * FROM user WHERE name = ?;
        User user = userRepository.findByName(name).orElseThrow();
        // !! 그 중간에 UserRepository를 Optional로 바꿨는데 여기서 에러가 나서
        // 대충 뒤에 .orElseThrow()를 붙였는데 ()안에 IllegalArgumentException() 넣으면 에러나서 그냥 지우고 저렇게 썼음.
        // 이렇게 해도 되나..? !!
        // findByName 함수는 UserRepository 인터페이스에 따로 추가해줌.
        // 존재하면 User 객체를 반환하고, 없으면 null 반환
        if (user == null) {
            throw new IllegalArgumentException();
        }
        
        userRepository.delete(user);
        // 주어지는 데이터를 DB에서 제거한다.
    }
}
