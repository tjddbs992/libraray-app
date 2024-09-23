package com.group.libraryapp.dto.calculator.request;
// dto : Data Transfer Object, 데이터 전달 객체

public class CalculatorAddRequest {
    private final int number1;
    private final int number2;

    // 커맨드 + n 에서 constructor, getter 선택
    public CalculatorAddRequest(int number2, int number1) {
        this.number2 = number2;
        this.number1 = number1;
    }

    public int getNumber1() {
        return number1;
    }

    public int getNumber2() {
        return number2;
    }
}
