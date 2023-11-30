function validateForm() {
    let id = document.forms["str"]["mbrId"].value;
    let pwd = document.forms["str"]["pswd"].value;
    let nm = document.forms["str"]["nm"].value;
    let ogdp = document.forms["str"]["ogdp"].value;
    if (id == "") {
        alert("사용자 아이디를 입력해 주세요");
        return false;
    }
    else if (pwd == "") {
        alert("비밀번호를 입력해 주세요");
        return false;
    }
    else if (nm == "") {
        alert("이름을 입력해 주세요");
        return false;
    }
    else if (ogdp == "") {
        alert("소속기관명을 입력해 주세요");
        return false;
    }
}