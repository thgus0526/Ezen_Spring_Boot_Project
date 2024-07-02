// --------------------------------------------------위도 경도변환---------------------------------------------------

var RE = 6371.00877; // 지구 반경(km)
var GRID = 5.0; // 격자 간격(km)
var SLAT1 = 30.0; // 투영 위도1(degree)
var SLAT2 = 60.0; // 투영 위도2(degree)
var OLON = 126.0; // 기준점 경도(degree)
var OLAT = 38.0; // 기준점 위도(degree)
var XO = 43; // 기준점 X좌표(GRID)
var YO = 136; // 기1준점 Y좌표(GRID)

// range input 값이 변경될 때마다 호출되는 함수
function updateMaxTempValue(maxTemp, value) {
    document.getElementById('customRangeMaxTempValue').textContent = value;
    document.getElementById('hiddenMaxTemp').value=value;

}
function updateMinTempValue(minTemp, value){
    document.getElementById('customRangeMinTempValue').textContent = value;
    document.getElementById('hiddenMinTemp').value=value;
}

function dfs_xy_conv(code, v1, v2) {
    var DEGRAD = Math.PI / 180.0;
    var RADDEG = 180.0 / Math.PI;

    var re = RE / GRID;
    var slat1 = SLAT1 * DEGRAD;
    var slat2 = SLAT2 * DEGRAD;
    var olon = OLON * DEGRAD;
    var olat = OLAT * DEGRAD;

    var sn =
        Math.tan(Math.PI * 0.25 + slat2 * 0.5) /
        Math.tan(Math.PI * 0.25 + slat1 * 0.5);
    sn = Math.log(Math.cos(slat1) / Math.cos(slat2)) / Math.log(sn);
    var sf = Math.tan(Math.PI * 0.25 + slat1 * 0.5);
    sf = (Math.pow(sf, sn) * Math.cos(slat1)) / sn;
    var ro = Math.tan(Math.PI * 0.25 + olat * 0.5);
    ro = (re * sf) / Math.pow(ro, sn);
    var rs = {};
    if (code == "toXY") {
        rs["lat"] = v1;
        rs["lng"] = v2;
        var ra = Math.tan(Math.PI * 0.25 + v1 * DEGRAD * 0.5);
        ra = (re * sf) / Math.pow(ra, sn);
        var theta = v2 * DEGRAD - olon;
        if (theta > Math.PI) theta -= 2.0 * Math.PI;
        if (theta < -Math.PI) theta += 2.0 * Math.PI;
        theta *= sn;
        rs["x"] = Math.floor(ra * Math.sin(theta) + XO + 0.5);
        rs["y"] = Math.floor(ro - ra * Math.cos(theta) + YO + 0.5);
    } else {
        rs["x"] = v1;
        rs["y"] = v2;
        var xn = v1 - XO;
        var yn = ro - v2 + YO;
        ra = Math.sqrt(xn * xn + yn * yn);
        if (sn < 0.0) -ra;
        var alat = Math.pow((re * sf) / ra, 1.0 / sn);
        alat = 2.0 * Math.atan(alat) - Math.PI * 0.5;

        if (Math.abs(xn) <= 0.0) {
            theta = 0.0;
        } else {
            if (Math.abs(yn) <= 0.0) {
                theta = Math.PI * 0.5;
                if (xn < 0.0) -theta;
            } else theta = Math.atan2(xn, yn);
        }
        var alon = theta / sn + olon;
        rs["lat"] = alat * RADDEG;
        rs["lng"] = alon * RADDEG;
    }
    return rs;
}
function locationfunc(coordinates) {
    coordinates.forEach(coordinate =>{
        console.log("locationfunc");
        const now = new Date();
        const year = now.getFullYear();
        const month = String(now.getMonth() + 1).padStart(2, "0"); // 1을 더한 후 두 자리 수로 맞춤
        const date = String(now.getDate()).padStart(2, "0");
        const hour = String(now.getHours()).padStart(2, "0");

        const formattedDate = `${year}${month}${date}`;

        var baseDate = formattedDate;
        if(hour < '06'){
            baseDate =  `${year}${month}${date-1}`;
        }

        var rs = dfs_xy_conv("toXY", coordinate.latitude, coordinate.longitude);

        const apiKey = "pT92G96xAGF0VK2U3O0kj%2BmVmHumwJTe08EgnL98rAQTQQxeaqyiD85Sx9nrgex5BOEZp81ZKdK3a1llX6TMfw%3D%3D"; // 여기에 자신의 OpenWeatherMap API 키를 입력하세요
        const url = `https://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst?serviceKey=${apiKey}&pageNo=1&numOfRows=1000&dataType=JSON&base_date=${baseDate}&base_time=0500&nx=${rs.x}&ny=${rs.y}`;
        fetch(url)
            .then((response) => {
                if(!response.ok){
                    throw new Error('Network response was not ok' + response.statusText);
                }
                return response.json();
            })
            .then((data) => {

                console.log('Data received: ', data);

                if(!data || !data.response || !data.response.body || !data.response.body.items || !data.response.body.items.item){
                    throw new Error('Invalid data structure');
                }
                const items = data.response.body.items.item;

                // fcstTime의 데이터 필터링
                const filteredItems = items.filter(item => item.fcstDate === formattedDate)
                    .filter(item => item.fcstTime.substring(0, 2) === hour);

                // 필요한 카테고리 데이터 추출
                const tmpData = filteredItems.find(item => item.category === 'TMP');
                console.log("온도 = " +tmpData);
                console.log("아이디 = " +coordinate.id);
                // 문자발송을 위한 데이터 전송함수 호출
                sendDataToServer(coordinate.id, tmpData.fcstValue);

            })
            .catch((error) =>
                console.error("Error fetching weather data:". error)
            );
    });
}

function sendDataToServer(userId, temperature){
    // AJAX 를 이용하여 서버로 데이터 전송
    console.log("sendDataToServer로 들어온 아이디 : " + userId);
    console.log("sendDataToServer로 들어온 온도 : " + temperature);

    $.ajax({
        type: 'POST',
        url: '/user/send',
        contentType: 'application/json',
        data: JSON.stringify({
            userId: userId,
            value: temperature

        }),
        success: function(response){
            console.log('Data send successfully', response);
        },
        error: function(error){
            console.error('Error sending data:', error);
        }
    });
}