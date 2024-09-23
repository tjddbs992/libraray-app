package com.group.libraryapp.domain.user;

import com.group.libraryapp.domain.user.loanhistory.UserLoanHistory;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class User  {

    @Id // 아래에 있는 필드를 primary key로 간주하겠다는 어노테이션
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // primary key를 auto increment로 설정했기 때문에(IDENTITY로 설정) 자동 생성되는 값이라는 것을 알려주기 위한 어노테이션
    private Long id;

    @Column(nullable = false, length = 20, name = "name") // Mysql의 name varchar(20)
    private String name;
    // age는 null이어도 괜찮고, java에서의 Integer와 Mysql에서의 int는 완전히 동일해서 @Column 어노테이션 생략 가능
    private Integer age;
    // getter가 있으면 스프링이 이 데이터를 http에 담아서 json 형식으로 보내줌.

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    // 연관관계의 주인이 가지고 있는 필드 이름 user를 넣어주어야함.
    // 연관관계의 주인의 값이 설정되어야만 진정한 데이터가 저장된다.
    // 연관관계의 주인 효과 : 객체가 연결되는 기준이 된다! 연관관계의 주인에서 setter가 사용되어야 테이블간에 연결이 된다!

    // cascade 옵션. cascade : 폭포처럼 흐르다
    // 한 객체가 저장되거나 삭제될 때, 그 변경이 폭포처럼 흘러 연결되어 있는 객체도 함께 저장되거나 삭제되는 기능

    // orphanRemoval 옵션 : 객체간의 관계가 끊어진 데이터를 자동으로 제거하는 옵션.
    // ex) userLoanHistories 리스트에 책1과 책2가 들어있는데 (유저에 책1, 책2가 연결돼있는데) 책1만 제거한다고 하자. (removeIf)
    // 함수가 끝나고 트랜잭션이 반영되면, 리스트에서는 책1이 제거되지만 데이터베이스에는 아무런 변화가 없다.
    // cascade 옵션은 user가 저장되거나 삭제될 때 연결된 테이블에 영향이 있는 것이고,
    // 이 경우에는 유저는 그대로 있지만, 유저와 연결된 책1만 리스트에서 지운 것이기 때문이다.
    // 만약 정말 책1을 빌린 기록을 지우고싶다면, userLoanHistoryRepository를 가져와서 delete까지 해주어야한다.
    // 이럴 때, userLoanHistoryRepository를 직접 건드리지 않고, 단지 연결을 끊어낸 것만으로 데이터베이스에서도
    // 데이터를 사라지게 하고싶다면 orphanremoval 옵션을 사용하면 된다. 이것도 OneToMany 관계에서 사용한다.
    private List<UserLoanHistory> userLoanHistories = new ArrayList<>();

    // 1:1 연관관계일 때
    // setter를 해주면 테이블간에 연결은 되지만, 트랜잭션이 끝나지 않았을 때 객체끼리는 연결되지 않는다.
    // 한쪽만 연결해두면 반대쪽은 알수 없다!
    // ex) person.setAddress(address);
    // System.out.println(address.getPerson()); <- null이 될 것!!
    // 해결책 : setter 한번에 둘을 같이 이어주자! setAddress를 불러올 때 이 setter함수안에 다시 this.address.setPerson 하면됨

    // N:1 연관관계일 때
    // 연관관계의 주인은 무조건 N인 쪽이다.
    // @ManyToOne은 단방향으로만 사용할 수 있다.
    // @JoinColumn : 연관관계의 주인이 활용할 수 있는 어노테이션. @Column 어노테이션 대신 @JoinColumn 어노테이션 사용한다고 생각
    // 연관관계의 주인에 연결하는 필드의 이름이나 null 여부, 유일성 여부, 업데이트 여부 등을 지정.

    // N:M 관계
    // @ManyToMany : 구조가 복잡하고, 테이블이 직관적으로 매핑되지 않아 사용하지 않는 것을 추천!!!!!
    // N : 1 관계를 여러개로 사용하는 것이 더 직관적이어서 좋음! N:M을 표현하고 싶다면 이렇게 하는 것이 좋다.

    protected User() {} // JPA, 즉 Entity를 사용하려면 매개변수가 없는 기본 생성자가 꼭 필요하다

    public User(String name, Integer age) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException(String.format("잘못된 name(%s)이 들어왔습니다", name));
        }
        this.name = name;
        this.age = age;
    }

    public void updateName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getAge() {
        return age;
    }

    // BookService의 loanBook 함수에서 userLoanHistory 객체를 직접 사용하지 않기 위해서 User 도메인 계층에서 함수 추가
    // User 객체와 UserLoanHistory 객체가 직접적으로 협력할 수 있도록 바꿈.
    // 도메인 계층에서 이렇게 협력할 수 있도록 한 것을 도메인 계층에 비즈니스 로직이 들어갔다 라고 표현함.
    public void loanBook(String bookName) {
        this.userLoanHistories.add(new UserLoanHistory(this, bookName));
    }

    // user와 userLoanHistory끼리 협업해서 유저 안에있는 연결된 userloanHistory 중에 들어오는 책 이름과 같은 걸 찾아서 반납처리
    public void returnBook(String bookName) {
        UserLoanHistory targetHistory = this.userLoanHistories.stream()
                .filter(history -> history.getBookName().equals(bookName))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
        targetHistory.doReturn();
    }
    // stream : 함수형 프로그래밍을 할 수 있게, stream을 시작한다.
    // filter : 들어오는 객체들 중에 다음 조건을 충족하는 것만 필터링한다.
    // UserLoanHistory 중 책 이름이 bookName과 같은 것!
    // findFirst : 첫 번째로 해당하는 UserLoanHistory를 찾는다.
    // orElseThroe : Optional을 제거하기 위해 없으면 예외를 던진다.
    // 그렇게 찾은 UserLoanHistry를 반납처리 한다.

    // 연관관계를 사용하면 무엇이 좋을까?
    // 1. 각자의 역할에 집중하게 된다! (= 응집성) (계층별로 응집성이 강해진다.)
    // 서비스 계층의 역할은, 꼭 필요한 경우에 서로 다른 도메인끼리 협업을 할 수 있게 도와주고, 트랜잭션을 관리하고, 외부 의존성(스프링 빈이 필요한 상황)을 관리하는 역할
    // 도메인의 역할은 이 도메인 객체가 표현하고 있는 비즈니스, 관심사에 대해서 로직을 처리하는 역할
    // 2. 새로운 개발자가 코드를 읽을 때 이해하기 쉬워진다.
    // 서비스 계층에 모든 코드가 다 있으면, 객체 지향적이지도 않고, 절차 지향적이고 코드가 몇백줄 넘어가면서 한 줄 한 줄 읽다보면 이해하기 어렵다.
    // 하지만 도메인 코드로 어느 정도 로직이 존재한다면 계층이 분리되어 있고, 이 도메인 계층이 어떤 일을 하는지 각각 파악할 수 있다 보니까
    // 새로운 개발자가 코드를 읽을 때 이해하기 쉽다.
    // 3. 테스트 코드 작성이 쉬워진다.
    // 도메인 계층의 로직이 많이 들어가게 되면 그 단위 단위, 여기서는 loanBook, returnBook 이렇게 도메인 계층을 부르는
    // 함수를 테스트함으로써 테스트코드의 작성이 조금 더 쉬워지게 된다.

    // 연관관계를 사용하는 것이 항상 좋을까? 그렇지 않다.
    // 지나치게 사용하면, 성능상의 문제가 생길 수도 있고 도메인 간의 복잡한 연결로 인해 시스템을 파악하기 어려워질 수 있다.
    // 너무 얽혀있으면, A를 수정했을 때 BCD 까지 영향이 퍼지게 된다.
    // 비즈니스 요구사항, 기술적인 요구사항, 도메인 아키텍처 등 여러 부분을 고민해서 연관관계 사용을 선택해야 한다.
}
