var course = 'BBBB0102001'
var interval = 0.04

clearInterval(logOutTimer.timerObj)
$.scwin.fn_createCaptcha = () => {}
NetFunnel_Action = (x, f) => f()

var maxVal = 0
var nowVal = 0
var timerCode = -1

function refresh() {
    $('#schSbjetNo').val(course)
    $.scwin.fn_preSch02()

    maxVal = parseInt($('#grid02 td:eq(10)').text())
    nowVal = parseInt($('#grid02 td:eq(11)').text())
}

function tryRegist() {
    if (nowVal < maxVal) {
        console.log('여석을 감지하여 신청을 시도')
        $.scwin.add(0);
        // $.scwin.fn_goReqAdd(0);
        clearInterval(timerCode)
    }
    refresh()
    if ($('#grid02 td').length == 0 || $ ('#grid02cnt').text == '0건') {
        console.log('오류가 발생했습니다. 매크로를 종료합니다')
        clearInterval(timerCode)
    }
}


// 매크로 실행
console.log(`매크로를 시작합니다. ${course} 과목을 대상으로 합니다.`)
refresh()
setTimeout(() => timerCode = setInterval(tryRegist, interval * 1000), 1)

// clearInterval(timerCode); 매크로 실행을 중단
