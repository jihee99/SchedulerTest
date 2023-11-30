function validateForm() {
    let id = document.forms["str"]["username"].value;
    let pwd = document.forms["str"]["password"].value;
    let name = document.forms["str"]["name"].value;
    let telNo = document.forms["str"]["telno"].value;
    let ogdp = document.forms["str"]["ogdp"].value;
    if (id == "") {
        alert("사용자 아이디를 입력해 주세요");
        return false;
    }
    else if (pwd == "") {
        alert("비밀번호를 입력해 주세요");
        return false;
    }
}