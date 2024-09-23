package com.group.libraryapp.controller.calculator;

import com.group.libraryapp.dto.calculator.request.CalculatorAddRequest;
import com.group.libraryapp.dto.calculator.request.CalculatorMultiplyRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

// control + option + o 눌러서 import문 알아서 한번에 정리 가능

// API를 개발하려고 하는 클래스에는 가장 위에 @RestController를 써줘야한다.
@RestController // 주어진 클래스를 컨트롤러로 등록한다는 의미. 컨트롤러 : API의 입구

// 이 어노테이션의 역할은 지금 보고있는 이 CalculatorController라는 클래스를 API의 진입 지점으로 만들어 주는 것이다.
// 그러면 이 클래스 안에 메소드를 만들어서 API가 이 메소드를 사용할 수 있게 만드는 것.

public class CalculatorController {

    @GetMapping("/add") // 아래 함수를 HTTP Method 가 GET이고 HTTP Path가 /add API로 지정한다.
    public int addTwoNumbers(CalculatorAddRequest request) {
        // 쿼리를 통해서 넘어온 데이터를 함수에 연결해줄 때는 @RequestParam 사용
        // @RequestParam을 쓰면 길어질 수 있는데 객체를 사용하면 깔끔하게 데이터 받기 가능

        return request.getNumber1() + request.getNumber2();
    }

    @PostMapping("/multiply")
    public int multiplyTwoNumbers(@RequestBody CalculatorMultiplyRequest request) {

        return request.getNumber1() * request.getNumber2();
    }
}
