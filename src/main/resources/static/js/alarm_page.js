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
function locationfunc(lat, lng) {
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

    var rs = dfs_xy_conv("toXY", lat, lng);

    const apiKey = "pT92G96xAGF0VK2U3O0kj%2BmVmHumwJTe08EgnL98rAQTQQxeaqyiD85Sx9nrgex5BOEZp81ZKdK3a1llX6TMfw%3D%3D"; // 여기에 자신의 OpenWeatherMap API 키를 입력하세요
    const url = `https://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst?serviceKey=${apiKey}&pageNo=1&numOfRows=1000&dataType=JSON&base_date=${baseDate}&base_time=0500&nx=${rs.x}&ny=${rs.y}`;
    fetch(url)
        .then((response) => response.json())
        .then((data) => {
            const items = data.response.body.items.item;

            // fcstTime의 데이터 필터링
            const filteredItems = items.filter(item => item.fcstDate === formattedDate)
                .filter(item => item.fcstTime.substring(0, 2) === hour);

            // 필요한 카테고리 데이터 추출
            const popData = filteredItems.find(item => item.category === 'POP');
            const skyData = filteredItems.find(item => item.category === 'SKY');
            const tmpData = filteredItems.find(item => item.category === 'TMP');
            const ptyData = filteredItems.find(item => item.category === 'PTY');

            // 강수 형태에 따라 문자열 결정
            var ptyString = "";
            var ptyImg = "";
            if (ptyData) {
                if (ptyData.fcstValue === "0") {
                    if (skyData.fcstValue === "1") {
                        ptyString = "맑음";
                        ptyImg = "sun.png";
                    }
                    else if (skyData.fcstValue === "3") {
                        ptyString = "구름많음";
                        ptyImg = "manyCloud.png";
                    }
                    else if (skyData.fcstValue === "4") {
                        ptyString = "흐림";
                        ptyImg = "cloud.png";
                    }
                } else if (ptyData.fcstValue === "1") {
                    ptyString = "비";
                    ptyImg = "rain.png";
                }
                else if (ptyData.fcstValue === "2") {
                    ptyString = "비/눈";
                    ptyImg = "rainSnow.png";
                }
                else if (ptyData.fcstValue === "3") {
                    ptyString = "눈";
                    ptyImg = "snow.png";
                }
                else if (ptyData.fcstValue === "5") {
                    ptyString = "빗방울";
                    ptyImg = "littleRain.png";
                }
                else if (ptyData.fcstValue === "6") {
                    ptyString = "빗방울눈날림";
                    ptyImg = "littleRainSnow.png";
                }
                else if (ptyData.fcstValue === "7") {
                    ptyString = "눈날림";
                    ptyImg = "littleSnow.png";
                }
            }
            // 문자발송을 위한 데이터 전송함수 호출
            sendDataToServer(tmpData.fcstValue);

            // 기존 내용을 지웁니다.
            while (weather1Div.firstChild) {
                weather1Div.removeChild(weather1Div.firstChild);
            }

            const weather1 = document.getElementById("weather1");

            const parseDate = `${year}년 ${month}월 ${date}일`;

            var imgElement = document.createElement('img'); // <img> 요소를 생성합니다.
            imgElement.src = "./img/" + ptyImg; // 이미지 URL을 설정합니다.
            imgElement.alt = '날씨이미지'; // alt 속성을 설정합니다.

            weather1Div.appendChild(imgElement);

            // 기존 내용을 지웁니다.
            // weather1Div.textContent = '';

            // 필요한 요소를 생성합니다.
            var ptyStringElement = document.createElement('p');
            ptyStringElement.className = 'weatherValue';
            ptyStringElement.textContent = ptyString;

            var parseDateElement = document.createElement('p');
            parseDateElement.textContent = parseDate;

            var hourElement = document.createElement('p');
            hourElement.textContent = `${hour}시 기준`;

            var temperatureElement = document.createElement('p');
            var tmpText = document.createElement('span');
            var tmpSpan = document.createElement('span');
            tmpText.className = 'weatherText';
            tmpSpan.className = 'weatherValue';
            tmpText.textContent = `온도 : `;
            tmpSpan.textContent = `${tmpData.fcstValue}°`;
            temperatureElement.appendChild(tmpText);
            temperatureElement.appendChild(tmpSpan);

            var popElement = document.createElement('p');
            var popText = document.createElement('span');
            var popSpan = document.createElement('span');
            popText.className = 'weatherText';
            popSpan.className = 'weatherValue';
            popText.textContent = `강수확률 : `;
            popSpan.textContent = `${popData.fcstValue}%`;
            popElement.appendChild(popText);
            popElement.appendChild(popSpan);

// 생성한 요소를 weather1Div에 추가합니다.
            weather1Div.appendChild(ptyStringElement);
            weather1Div.appendChild(parseDateElement);
            weather1Div.appendChild(hourElement);
            weather1Div.appendChild(temperatureElement);
            weather1Div.appendChild(popElement);

        })
        .catch((error) =>
            console.error("Error fetching weather data:", error)
        );
}

function sendDataToServer(data){
    // AJAX 를 이용하여 서버로 데이터 전송
    console.log("data" + data);
    $.ajax({
        type: 'POST',
        url: '/user/send',
        contentType: 'application/json',
        data: JSON.stringify({
            value: data
        }),
        success: function(response){
            console.log('Data send successfully', response);
        },
        error: function(error){
            console.error('Error sending data:', error);
        }
    });
}