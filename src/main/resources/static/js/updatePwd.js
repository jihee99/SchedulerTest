(function ($) {
    const _csrf = $('meta[name="_csrf"]').attr('content');
    const _csrf_header = $('meta[name="_csrf_header"]').attr('content');
    const headers = {}
    headers[_csrf_header] = _csrf;

    document.forms["update"].onsubmit = function(event) {

        event.preventDefault();

        const username = document.forms["update"]["username"].value;
        if(username == ""){
            alert("가입한 이메일을 입력해주세요.");
            return false;
        } else{
            $.ajax({
                type: "POST",
                url: "/update/pwd/process2",
                data: { username: $("#username").val() },
                beforeSend: function(xhr) {
                    xhr.setRequestHeader(_csrf_header, _csrf);
                },
                success: function (response) {
                    console.log(response);
                    // alert(response);
                    if (response.status === "success") {
                        alert("사용자 이름 일치함!!");
                    } else {
                        alert("에러: " + response.message);
                    }
                },
                error: function (xhr, status, error) {
                    alert("에러: " + xhr.responseText);
                }
            });
        }

        // 기본 폼 제출 막기
        return false;
    }

})(jQuery);
// async function getCsrfToken() {
//     try {
//         const response = await fetch('/get-csrf-token');
//         const data = await response.json();
//
//         // data 객체에서 _csrf 값을 가져와서 사용
//         const _csrf = data._csrf;
//
//         // 이후에 _csrf 값을 사용하여 요청을 보낼 수 있음
//         sendPasswordUpdateRequest(_csrf);
//
//     } catch (error) {
//         console.error('Error fetching CSRF token:', error);
//     }
// }

// // 함수 호출
// getCsrfToken();


// document.forms["update"].onsubmit = function(event) {
// function validateForm(){
//     event.preventDefault(); // 기본 동작 막기
//
//     // type1
//     // const currentPwd = document.forms["update"]["currentPwd"].value;
//     // const newPwd1 = document.forms["update"]["newPwd1"].value;
//     // const newPwd2 = document.forms["update"]["newPwd2"].value;
//     //
//     // if(newPwd1 === newPwd2){
//     //
//     //     let xhr = new XMLHttpRequest();
//     //
//     //     xhr.open("POST", "/update/pwd/process", true);
//     //     xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
//     //
//     //     // xhr.setRequestHeader("X-CSRF-TOKEN", csrfToken);
//     //
//     //     let requestData = "currentPwd=" + encodeURIComponent(currentPwd) +
//     //         "&newPwd1=" + encodeURIComponent(newPwd1) +
//     //         "&newPwd2=" + encodeURIComponent(newPwd2);
//     //
//     //     xhr.send(requestData);
//     //
//     //     xhr.onreadystatechange = function() {
//     //         if (xhr.readyState == 4) { // 요청이 완료되었을 때
//     //             if (xhr.status == 200) { // 성공적으로 응답을 받았을 때
//     //                 var response = JSON.parse(xhr.responseText);
//     //                 console.log(response);
//     //
//     //                 // 응답에 따른 처리
//     //                 if (response.status === "success") {
//     //                     alert("비밀번호가 성공적으로 변경되었습니다.");
//     //                     // 변경 성공 시 로그인 화면으로 리다이렉트
//     //                     window.location.href = "/login";
//     //                 } else {
//     //                     alert("에러: " + response.message);
//     //                 }
//     //             } else {
//     //                 alert("에러: " + xhr.responseText);
//     //             }
//     //         }
//     //     };
//     //
//     //
//     //     // $.ajax({
//     //     //     type: "POST",
//     //     //     url: "/update/pwd/check",
//     //     //     data: { currentPwd: currentPwd, newPwd1: newPwd1, newPwd2: newPwd2 },
//     //     //     success: function (response) {
//     //     //         console.log(response)
//     //     //         // alert(response);
//     //     //         // if (response === "비밀번호가 성공적으로 변경되었습니다.") {
//     //     //         //     // document.forms["update"].submit();
//     //     //         //     // 변경 성공 시 로그인 화면으로 리다이렉트
//     //     //         //     window.location.href = "/login";
//     //     //         // }
//     //     //     },
//     //     //     error: function (xhr, status, error) {
//     //     //         alert("에러: " + xhr.responseText);
//     //     //     }
//     //     // });
//     // }else{
//     //     alert("비밀번호가 일치하지 않습니다.");
//     //     return false;
//     // }
//     //
//     // return false;
//
//     // type2
//     const username = document.forms["update"]["username"].value;
//     if(username == ""){
//         alert("가입한 이메일을 입력해주세요.");
//         return false;
//     }else{
//
//         const xhr = new XMLHttpRequest();
//
//         xhr.open("POST", "/update/pwd/process2", true);
//         xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
//
//         xhr.onload = function() {
//             if (xhr.status === 200) {
//                 const response = JSON.parse(xhr.responseText);
//
//                 if (response.status === "success") {
//                     alert("사용자 이름 일치함!!");
//                 } else {
//                     alert("에러: " + response.message);
//                 }
//             } else {
//                 alert("에러: " + xhr.statusText);
//             }
//         };
//
//         // 에러 처리
//         xhr.onerror = function() {
//             alert("네트워크 에러 발생");
//         };
//
//         // 데이터 전송
//         const data = "username=" + encodeURIComponent(username);
//         xhr.send(data);
//
//         // $.ajax({
//         //     type: "POST",
//         //     url: "/update/pwd/process",
//         //     data: { username: $("#username").val() },
//         //     success: function (response) {
//         //         console.log(response);
//         //         // alert(response);
//         //         if (response.status === "success") {
//         //             alert("사용자 이름 일치함!!");
//         //         } else {
//         //             alert("에러: " + response.message);
//         //         }
//         //     },
//         //     error: function (xhr, status, error) {
//         //         alert("에러: " + xhr.responseText);
//         //     }
//         // });
//     }
//
//     // 기본 폼 제출 막기
//     return false;
//
// }