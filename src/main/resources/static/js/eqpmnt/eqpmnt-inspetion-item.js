let headers = {};
(function ($) {

    const _csrf = $('meta[name="_csrf"]').attr('content');
    const _csrf_header = $('meta[name="_csrf_header"]').attr('content');
    // const headers = {}
    headers[_csrf_header] = _csrf;

    $(document).on('click', '#regBtn', function(e) {
        let formData = $("#register").serialize();
        formData = formData.replace(/%0D%0A/g, "<br>");
        console.log(formData)
        $.ajax({
            type: "POST",
            url: "/realtime/eqpmnt/items/register",
            data: formData,
            contentType: 'application/x-www-form-urlencoded; charset=UTF-8',
            beforeSend: function(xhr) {
                xhr.setRequestHeader(_csrf_header, _csrf);
            },
            success: function (response) {
                if(response.status == "success"){
                    alert(response.message);
                    location.reload();
                }else{
                    alert(response.message);
                }
            },
            error: function (xhr, status, error) {
                console.log(error);
            }
        });
    });


})(jQuery);