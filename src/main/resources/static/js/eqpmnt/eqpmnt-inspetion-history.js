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
                if(response.status == "success"){
                    console.log(response.list);
                }else{
                    alert(response.message);
                }
            },
            error: function (xhr, status, error) {

            },
            default: function(){

            }
        });
    });


    $('.icon').on('click',function(e){
        let hstryCode = e.target.dataset.hstry

        $.ajax({
            type: "POST",
            url: "/realtime/eqpmnt/get/hstry/details",
            data: { hstryCode: hstryCode },
            contentType: 'application/x-www-form-urlencoded; charset=UTF-8',
            beforeSend: function(xhr) {
                xhr.setRequestHeader(_csrf_header, _csrf);
            },
            success: function (response) {
                if(response.status == "success"){

                    $(".modal-body").empty();
                    let summary = response.data.summary;
                    let html = `<div class="summary"><div class="row">`
                                +`<div class="col fw-bolder">Inspector</div>`
                                +`<div class="col-sm-3">${summary.chck_flfr_nm}</div>`
                                +`<div class="col fw-bolder">Date</div>`
                                +`<div class="col-sm-5">${summary.chck_flfmt_day}</div>`
                                +`</div><hr></div>`
                                +`<div class="head py-2 px-2">`

                    for (let [index, item] of response.data.list.entries()) {
                        html += `<div class="head py-2 px-2">`
                                +`<div class="mt-2 row">`
                                +`<div class="col"><p class="fs-5">${index+1}. ${item.chck_artcl_nm}</p></div>`
                                +`<div class="col-auto"><div class="form-check">`
                                + `<input class="form-check-input" type="radio" name="flexRadioDefault${index}" id="Normal${index}" ${item.chck_rslt === 'N' ? 'checked' : ''}>`
                                + `<label class="form-check-label" for="Normal${index}">Normal</label>`
                                + `</div></div>`
                                + `<div class="col-auto"><div class="form-check">`
                                + `<input class="form-check-input" type="radio" name="flexRadioDefault${index}" id="Abnormal${index}" ${item.chck_rslt === 'A' ? 'checked' : ''}>`
                                + `<label class="form-check-label" for="Abnormal${index}">Abnormal</label>`
                                +`</div></div>`
                                +`</div>`
                                +`<label>Remark</label>`
                                +`<textarea class="form-control" rows="2">${item.rmrk !== null ? item.rmrk : ''}</textarea>`;
                        if (index < response.data.list.length - 1) {
                            html += `<hr>`;
                        }
                    }
                    $(".modal-body").append(html);
                    $('#details').modal('show');
                }else{

                    alert(response.message);
                    $('#details').modal('hide');
                }
            },
            error: function (xhr, status, error) {

            },
            default: function(){

            }
        });
    });

})(jQuery);