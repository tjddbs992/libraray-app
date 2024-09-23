package com.group.libraryapp.dto.user.request;


public class UserCreateRequest {

    private String name;
    private Integer age;

    public UserCreateRequest() {}
    // !! 궁금한 점. 원래 그냥 기본생성자 없었는데도 유저 저장이 잘 됐음.
    // 근데 나중에 책 대출 시스템 코드를 짜고나서 다시 유저를 저장하니까 서버 오류라고 저장이 안됨.
    // gpt에 물어보니 기본생성자를 만들면 된다고 해서 만드니까 저장됐음.
    // 그럼 처음에 짰던 코드는 왜 저장이 됐던 것이며 BookCreateRequest dto에는 기본생성자가 없는데 책 저장이 왜 잘되는거임?
    // 그리고 밑에 생성자를 만들어줬는데 왜 또 기본생성자를 만들어야 오류가 안나는거임?
    // 저건 디비에 넣는거고 유저는 웹에 저장된게 뜨도록 만들어야해서 그런건가?
    // 이 질문을 그대로 해보니 JSON을 dto로 가져오는 과정에서 Jackson이 역직렬화를 해야하는데 그때 기본생성자가 필요하게 돼서 라고함. 맞나?
    // 그럼 책정보 저장은 디비에만 저장해서 기본생성자 없이도 웹에서 입력하면 디비로 들어가는건가?

    public UserCreateRequest(String name, Integer age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public Integer getAge() {
        return age;
    }
}
