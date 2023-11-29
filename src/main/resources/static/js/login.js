function validateForm() {
    let id = document.forms["str"]["username"].value;
    let pwd = document.forms["str"]["password"].value;
    if (id == "") {
        alert("사용자 아이디를 입력해 주세요");
        return false;
    }
    else if (pwd == "") {
        alert("비밀번호를 입력해 주세요");
        return false;
    }
}