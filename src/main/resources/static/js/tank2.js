const tankResultTable = new Tabulator("#inflowList",{
        layout:"fitColumns",
        height: "100%",
        placeholder: "모델이 실행되지 않았습니다.",
        pagination:"local",
        paginationSize:10,
        paginationSizeSelector:[10,30,50,100],
        columns: [
            {title: "Date", field: "date"},
            {title: "Predicted rainfall(mm)", field: "rmm"},
            {title: "Estimated inflow(CMS).", field: "qsCms"}
        ]
    });
const tankMonthlyResultTable = new Tabulator("#inflowMonthlyList",{
    layout:"fitColumns",
    height: "100%",
    placeholder: "모델이 실행되지 않았습니다.",
    pagination:"local",
    paginationSize:10,
    paginationSizeSelector:[10,30,50,100],
    columns: [
        {title: "Date", field: "date"},
        {title: "Predicted rainfall(mm)", field: "rmm"},
        {title: "Estimated inflow(CMS).", field: "qsCms"}
    ]
});

const inflowChart = AmCharts.makeChart("chartDiv", {
        "type": "serial",
        "categoryField": "date",    // 날짜 필드를 카테고리로 사용
        "categoryAxis": {
            "gridPosition": "start",
            "labelFrequency": 0,
            "labelRotation": -90,
            "minHorizontalGap": 30,
            "gridAlpha" : 0
        },
        "chartCursor": {
            "enabled": true
        },
        "trendLines": [],
        "graphs": [
            {
                "balloonText": "[[title]] of [[category]]:[[value]]",
                "fillAlphas": 1,
                "fillColors": "#1993FC",
                "id": "AmGraph-1",
                "lineThickness": 0,
                "title": "Rainfall",
                "type": "column",
                "valueField": "rmm",// rmm 데이터를 사용하여 그래프 표시
                "valueAxis": "ValueAxis-2"
            },
            {
                "balloonText": "[[title]] of [[category]]:[[value]]",
                "id": "AmGraph-2",
                "lineColor": "#FF0000",
                "lineThickness": 2,
                "title": "Inflow",
                "type": "line",
                "valueField": "qsCms", // qscms 데이터를 사용하여 그래프 표시
                "valueAxis": "ValueAxis-1"
            }
        ],
        "guides": [],
        "backgroundColor": "rgba(0, 0, 0, 0)",
        "valueAxes": [
            {
                "id": "ValueAxis-1",
                "includeGuidesInMinMax": true,
                "position": "left", // 왼쪽에 표시할 y축
                "gridThickness": 0,
                "gridAlpha": 0,
                "title": "Estimated inflow(CMS)" // 첫 번째 y축의 제목
            },
            {
                "id": "ValueAxis-2",
                "reversed": true,
                "position": "right", // 오른쪽에 표시할 y축
                "gridThickness": 0,
                "gridAlpha": 0,
                "title": "Predicted rainfall(mm)" // 두 번째 y축의 제목
            }
        ],
        "allLabels": [],
        "balloon": {},
        "legend": {
            "enabled": true,
            "useGraphSettings": true,
            "reversedOrder": true
        },
        "titles": [],
        "dataProvider": []
    }
);

const inflowMonthlyChart = AmCharts.makeChart("chartDivMonthly", {
        "type": "serial",
        "categoryField": "date",    // 날짜 필드를 카테고리로 사용
        "categoryAxis": {
            "gridPosition": "start",
            "labelFrequency": 0,
            "labelRotation": -90,
            "minHorizontalGap": 30,
            "gridAlpha" : 0
        },
        "chartCursor": {
            "enabled": true
        },
        "trendLines": [],
        "graphs": [
            {
                "balloonText": "[[title]] of [[category]]:[[value]]",
                "fillAlphas": 1,
                "fillColors": "#1993FC",
                "id": "AmGraph-1",
                "lineThickness": 0,
                "title": "Rainfall",
                "type": "column",
                "valueField": "rmm",// rmm 데이터를 사용하여 그래프 표시
                "valueAxis": "ValueAxis-2"
            },
            {
                "balloonText": "[[title]] of [[category]]:[[value]]",
                "id": "AmGraph-2",
                "lineColor": "#FF0000",
                "lineThickness": 2,
                "title": "Inflow",
                "type": "line",
                "valueField": "qsCms", // qscms 데이터를 사용하여 그래프 표시
                "valueAxis": "ValueAxis-1"
            }
        ],
        "guides": [],
        "backgroundColor": "rgba(0, 0, 0, 0)",
        "valueAxes": [
            {
                "id": "ValueAxis-1",
                "includeGuidesInMinMax": true,
                "position": "left", // 왼쪽에 표시할 y축
                "gridThickness": 0,
                "gridAlpha": 0,
                "title": "Estimated inflow(CMS)" // 첫 번째 y축의 제목
            },
            {
                "id": "ValueAxis-2",
                "reversed": true,
                "position": "right", // 오른쪽에 표시할 y축
                "gridThickness": 0,
                "gridAlpha": 0,
                "title": "Predicted rainfall(mm)" // 두 번째 y축의 제목
            }
        ],
        "allLabels": [],
        "balloon": {},
        "legend": {
            "enabled": true,
            "useGraphSettings": true,
            "reversedOrder": true
        },
        "titles": [],
        "dataProvider": []
    }
);


// 차트 버튼 클릭 이벤트 처리
document.getElementById("chartDivBtn").addEventListener('click', function() {
    // 활성 탭 변경
    document.querySelectorAll(".nav-link").forEach(link => {
        link.classList.remove("active");
    });
    this.classList.add("active");

    // 차트 표시 및 숨김
    document.getElementById("chartDiv").style.display = "block";
    document.getElementById("chartDivMonthly").style.display = "none";

    document.getElementById("inflowList").style.display = "block";
    document.getElementById("inflowMonthlyList").style.display = "none";
});

// 월별 차트 버튼 클릭 이벤트 처리
document.getElementById("chartDivMonthlyBtn").addEventListener('click', function() {
    // 활성 탭 변경
    document.querySelectorAll(".nav-link").forEach(link => {
        link.classList.remove("active");
    });
    this.classList.add("active");

    // 차트 표시 및 숨김
    document.getElementById("chartDiv").style.display = "none";
    document.getElementById("chartDivMonthly").style.display = "block";

    document.getElementById("inflowList").style.display = "none";
    document.getElementById("inflowMonthlyList").style.display = "block";
});


document.getElementById("tank-button").addEventListener('click', function (format, data){
    let origin = new FormData(document.getElementById("excelform"));

    let fd = new FormData();

    // FormData에 파일 및 데이터 추가
    fd.append('floatingSelect', origin.get('floatingSelect'));

    let xhr = new XMLHttpRequest();
    let url = '/tank2/run';

    xhr.open('POST', url, true);
    xhr.onload = function () {
        if (xhr.status === 200) {
            // console.log(xhr.response); // 성공한 경우 서버 응답 출력
            let data = xhr.response;

            formattedRowData(data);

        } else {
            console.log('파일업로드 실패');
        }
    };
    xhr.onerror = function (e) {
        console.log(e);
        console.log('파일업로드 실패');
    };

    xhr.send(fd); // FormData를 전송



});

function formattedRowData(data){

    // 전달받은 데이터가 문자열일때 JSON으로 파싱
    if (typeof data === 'string') {
        data = JSON.parse(data);
    }
    console.log(data)

    document.getElementById("ratio").textContent = data.ratio;
    document.getElementById("sum_of_areal_rainfall").textContent = data.realRainfall;
    document.getElementById("sum_of_observed_flow_depth").textContent = data.observedFlowDept;
    document.getElementById("sum_of_computed_flow_depth").textContent =  data.computedFlowDept;
    document.getElementById("sum_of_evapotranspiration").textContent = data.evapotranspiration;
    document.getElementById("obs_mean").textContent = data.obsMean;
    document.getElementById("obs_sdev").textContent = data.obsSdev;
    document.getElementById("sim_mean").textContent = data.simMean;
    document.getElementById("sim_sdev").textContent = data.simSdev;

    // tankResultTable.setData(data.inflows);

    // inflowChart 데이터 할당하고 다시 그리기
    inflowChart.dataProvider = data.inflows;
    inflowChart.validateData();

    const sortedData = data.inflows.sort((a, b) => new Date(a.date) - new Date(b.date));

    // 날짜 별로 그룹화
    const groupedData = {};
    sortedData.forEach(entry => {
        const [year, month] = entry.date.split('/');
        const monthlyKey = `${year}/${month}`;

        if (!groupedData[monthlyKey]) {
            groupedData[monthlyKey] = {
                date: monthlyKey,
                rmm: 0,
                qoCms: 0,
                qsCms: 0
            };
        }
        groupedData[monthlyKey].rmm += parseFloat(entry.rmm);
        groupedData[monthlyKey].qoCms += parseFloat(entry.qoCms);
        groupedData[monthlyKey].qsCms += parseFloat(entry.qsCms);

        // 숫자 값을 소수점 둘째 자리까지 포맷팅
        groupedData[monthlyKey].rmm = parseFloat(groupedData[monthlyKey].rmm.toFixed(2));
        groupedData[monthlyKey].qoCms = parseFloat(groupedData[monthlyKey].qoCms.toFixed(2));
        groupedData[monthlyKey].qsCms = parseFloat(groupedData[monthlyKey].qsCms.toFixed(2));

    });

    tankResultTable.setData(Object.values(data.inflows));
    tankMonthlyResultTable.setData(Object.values(groupedData));
    inflowMonthlyChart.dataProvider = Object.values(groupedData);
    inflowMonthlyChart.validateData();

    document.getElementById("content-box").style.display = "block";
    document.getElementById("inflowMonthlyList").style.display = "none";
}
