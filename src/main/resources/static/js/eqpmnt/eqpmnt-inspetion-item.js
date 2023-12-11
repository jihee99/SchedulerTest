let headers = {};
(function ($) {

    const _csrf = $('meta[name="_csrf"]').attr('content');
    const _csrf_header = $('meta[name="_csrf_header"]').attr('content');
    // const headers = {}
    headers[_csrf_header] = _csrf;

    $(document).on('click', '#searchBtn', function(e) {
        let formData = $("#search").serialize();

        $.ajax({
            type: "GET",
            url: "/realtime/eqpmnt/items/search",
            data: formData,
            contentType: 'application/x-www-form-urlencoded; charset=UTF-8',
            beforeSend: function(xhr) {
                xhr.setRequestHeader(_csrf_header, _csrf);
            },
            success: function (response) {
                console.log(response)
                $('#tableFragment').replaceWith(response);
            },
            error: function (xhr, status, error) {
                console.log(error);
            }
        });

    });

    $(document).on('click', '#regBtn', function(e) {
        let formData = $("#register").serialize();
        formData = formData.replace(/%0D%0A/g, "<br>");

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

    $(document).on('click', '.table-row', function(e) {
        const artclId = $(this).data('artcl');

        $.ajax({
            type: "GET",
            url: "/realtime/eqpmnt/items/details",
            data: {artclId : artclId},
            contentType: 'application/x-www-form-urlencoded; charset=UTF-8',
            beforeSend: function(xhr) {
                xhr.setRequestHeader(_csrf_header, _csrf);
            },
            success: function (response) {

                console.log(response)
                if(response.status == 'success'){
                    let data = response.data;

                    $('#detailType').val(data.obsvtrType).prop('disabled', true);
                    $('#inspectionDetailNm').val(data.chckArtclNm).prop('disabled', true);
                    $('#detailGuide').val(data.chckArtclGuide).prop('disabled', true);

                    $('#detailModal').modal('show');
                }else{
                    alert(response.message);
                }
                // $('#detailModalBody').html(response);
                // $('#detailModal').modal('show');
            },
            error: function (xhr, status, error) {
                console.log(error);
            }
        });
    });


})(jQuery);