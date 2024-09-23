package com.group.libraryapp.dto.user.request;

public class UserUpdateRequest {
    private long id;
    private String name;
    private Integer age;

    public Integer getAge() {
        return age;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
