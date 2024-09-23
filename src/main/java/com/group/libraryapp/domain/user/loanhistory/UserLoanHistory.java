package com.group.libraryapp.domain.user.loanhistory;

import com.group.libraryapp.domain.user.User;

import javax.persistence.*;

@Entity
public class UserLoanHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id = null;
    @ManyToOne // N : 1 관계. 학생 : 교실 관계
    private User user;
    private String bookName;
    private boolean isReturn; // boolean으로 처리하게 되면 tinyint에 잘 매핑된다!

    protected UserLoanHistory() {}

    // isReturn 값은 어차피 false기 때문에 코드를 깔끔하게 하기 위해서 파라미터에서 빼고, 그냥 false로 초기화
    public UserLoanHistory(User user, String bookName) { //, boolean isReturn) {
        this.user = user;
        this.bookName = bookName;
        this.isReturn = false;
        // this.isReturn = isReturn;
    }

    public void doReturn(){
        this.isReturn = true;
    }

    public String getBookName() {
        return this.bookName;
    }
}
