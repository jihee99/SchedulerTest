(function ($) {

    const _csrf = $('meta[name="_csrf"]').attr('content');
    const _csrf_header = $('meta[name="_csrf_header"]').attr('content');
    const headers = {}
    headers[_csrf_header] = _csrf;

    let historyList;

    // $('input[name="dates"]').daterangepicker();

    // $('input[name="daterange"]').daterangepicker({
    //     opens: 'left'
    // }, function(start, end, label) {
    //     console.log("A new date selection was made: " + start.format('YYYY-MM-DD') + ' to ' + end.format('YYYY-MM-DD'));
    // });

    $('input[name="datefilter"]').daterangepicker({
        autoUpdateInput: false,
        locale: {
            cancelLabel: 'Clear'
        }
    });

    $('input[name="datefilter"]').on('apply.daterangepicker', function(ev, picker) {
        $(this).val(picker.startDate.format('MM/DD/YYYY') + ' - ' + picker.endDate.format('MM/DD/YYYY'));
    });

    $('input[name="datefilter"]').on('cancel.daterangepicker', function(ev, picker) {
        $(this).val('');
    });


    $('#btnSearch').on('click',function(){

        // let formData =  new FormData($('#search')[0]);
        let formData = $("#search").serialize();
        console.log(formData)

        $.ajax({
            type: "POST",
            url: "/realtime/eqpmnt/get/hstry/search",
            data: formData,
            contentType: 'application/x-www-form-urlencoded; charset=UTF-8',
            beforeSend: function(xhr) {
                xhr.setRequestHeader(_csrf_header, _csrf);
            },
            success: function (response) {
                console.log(response);
            },
            error: function (xhr, status, error) {

            },
            default: function(){

            }
        });
    });

})(jQuery);