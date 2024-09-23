package com.group.libraryapp.domain.book;

import javax.persistence.*;

// JPA 객체를 새로 만들게 되면, 레포지토리도 같이 새로 만들어주는 편이다.
// 항상 도메인을 만들 떄에는, 레포지토리도 세트로 만들어준다고 생각해라.
@Entity
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id = null;

    @Column(nullable = false) //, length = 255, name = "name")
    // 길이제한 똑같이 255라서 생략, 필드 이름도 name으로 똑같아서 생략 가능
    private String name;

    protected Book() {

    }

    public Book(String name) {
        if(name == null || name.isEmpty()) {
            throw new IllegalArgumentException(String.format("잘못된 name(%s)이 들어왔습니다", name));
        }
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
