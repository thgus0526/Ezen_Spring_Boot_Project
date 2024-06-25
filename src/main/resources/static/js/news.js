
// --------------------------------------------------뉴스---------------------------------------------------

document.addEventListener("DOMContentLoaded", () => {
    const apiKey = "pub_47274cc26048b8d0cbbb009de6d436e3a53bd"; // 여기에 올바른 API 키를 입력하세요.
    const url = `https://newsdata.io/api/1/latest?country=kr&category=environment&apikey=pub_47274cc26048b8d0cbbb009de6d436e3a53bd`;
    const newsContainer = document.getElementById("news-container");

    fetch(url)
        .then((response) => {
            if (!response.ok) {
                throw new Error("Network response was not ok");
            }
            return response.json();
        })
        .then((data) => {
            console.log(data);
            if (data.results) {
                data.results.forEach((article) => {
                    const newsCard = document.createElement("div");
                    newsCard.className = "news-card";

                    const img = document.createElement("img");
                    img.src =
                        article.image_url || "https://via.placeholder.com/300x200";
                    img.alt = article.title;

                    const body = document.createElement("div");
                    body.className = "news-card-body";

                    const title = document.createElement("div");
                    title.className = "news-card-title";
                    title.innerText = article.title;

                    const description = document.createElement("div");
                    description.className = "news-card-description";
                    description.innerText =
                        article.description || "No description available.";

                    const footer = document.createElement("div");
                    footer.className = "news-card-footer";

                    const link = document.createElement("a");
                    link.href = article.link;
                    link.target = "_blank";
                    link.innerText = "Read more";

                    footer.appendChild(link);
                    body.appendChild(title);
                    body.appendChild(description);
                    newsCard.appendChild(img);
                    newsCard.appendChild(body);
                    newsCard.appendChild(footer);
                    newsContainer.appendChild(newsCard);
                });
            } else {
                console.error("No articles found");
            }
        })
        .catch((error) => {
            console.error("Error fetching news:", error);
        });
});

function sample6_execDaumPostcode() {
    new daum.Postcode({
        oncomplete: function(data) {
            // 팝업에서 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분.

            // 각 주소의 노출 규칙에 따라 주소를 조합한다.
            // 내려오는 변수가 값이 없는 경우엔 공백('')값을 가지므로, 이를 참고하여 분기 한다.
            var addr = ''; // 주소 변수
            var extraAddr = ''; // 참고항목 변수

            //사용자가 선택한 주소 타입에 따라 해당 주소 값을 가져온다.
            if (data.userSelectedType === 'R') { // 사용자가 도로명 주소를 선택했을 경우
                addr = data.roadAddress;
            } else { // 사용자가 지번 주소를 선택했을 경우(J)
                addr = data.jibunAddress;
            }

            // 사용자가 선택한 주소가 도로명 타입일때 참고항목을 조합한다.
            if(data.userSelectedType === 'R'){
                // 법정동명이 있을 경우 추가한다. (법정리는 제외)
                // 법정동의 경우 마지막 문자가 "동/로/가"로 끝난다.
                if(data.bname !== '' && /[동|로|가]$/g.test(data.bname)){
                    extraAddr += data.bname;
                }
                // 건물명이 있고, 공동주택일 경우 추가한다.
                if(data.buildingName !== '' && data.apartment === 'Y'){
                    extraAddr += (extraAddr !== '' ? ', ' + data.buildingName : data.buildingName);
                }
                // 표시할 참고항목이 있을 경우, 괄호까지 추가한 최종 문자열을 만든다.
                if(extraAddr !== ''){
                    extraAddr = ' (' + extraAddr + ')';
                }
                // 조합된 참고항목을 해당 필드에 넣는다.
                document.getElementById("addressNotes").value = extraAddr;

            } else {
                document.getElementById("addressNotes").value = '';
            }

            // 우편번호와 주소 정보를 해당 필드에 넣는다.
            document.getElementById('addressZipcode').value = data.zonecode;
            document.getElementById("addressStreet").value = addr;
            // 커서를 상세주소 필드로 이동한다.
            document.getElementById("addressDetail").focus();
        }
    }).open();
}

function checkUserId(){
    let userId = document.getElementById('userId').value;
    if(userId.trim() ===''){
        alert('아이디를 입력하세요.');
        return;
    }

    // userIdInfo 요소 찾기
    let userIdInfoElement = document.getElementById('userIdInfo');
    if (userIdInfoElement) {
        userIdInfoElement.innerHTML = '';  // 요소가 존재하면 innerHTML 설정
    } else {
        console.error('userIdInfo 요소를 찾을 수 없습니다.');
        return;
    }

    // userIdError 요소 찾기
    let userIdErrorElement = document.getElementById('userIdError');
    if (userIdErrorElement) {
        userIdErrorElement.innerHTML = '';  // 요소가 존재하면 innerHTML 설정
    } else {
        console.error('userIdError 요소를 찾을 수 없습니다.');
        return;
    }

    console.log(userId);
    $.ajax({
        type: 'GET',
        url: '/user/checkUserId',
        data: {userId: userId},
        success: function(response) {
            if(response.available){
                document.getElementById('userIdInfo').innerHTML='사용가능한 아이디 입니다.';
            } else {
                document.getElementById('userIdError').innerHTML='이미 사용 중인 아이디입니다.';
            }
        },
        error : function(xhr, status, error){
            console.error('Ajax 요청 실패' + status+ ', '+error);
        }
    });

}
function validateForm(){
    let password = document.getElementById("password").value;
    let confirmPassword = document.getElementById("passwordConfirm").value;


    if(password !== confirmPassword){
        document.getElementById("passwordNotMatch").innerHTML="비밀번호가 일치하지 않습니다.";


    } else {
        document.getElementById("passwordNotMatch").innerHTML="";
        document.getElementById("passwordCheck").innerHTML="비밀번호가 일치합니다.";

    }

}

//     function checkUserEmail(){
//         let email = document.getElementById("email").value;
//
//         if(email.trim() ===''){
//             alert('이메일을 입력하세요');
//             return;
//         }
//         let userEmailInfo = document.getElementById('userEmailInfo');
//         if (userEmailInfo) {
//             userEmailInfo.innerHTML = '';  // 요소가 존재하면 innerHTML 설정
//         } else {
//             console.error('userEmailInfo 요소를 찾을 수 없습니다.');
//             return;
//         }
//         let userEmailError = document.getElementById('userEmailError');
//         if (userEmailError) {
//             userEmailError.innerHTML = '';  // 요소가 존재하면 innerHTML 설정
//         } else {
//             console.error('userIdError 요소를 찾을 수 없습니다.');
//             return;
//         }
//
//         $.ajax({
//             type: 'GET',
//             url: '/user/checkUserEmail',
//             data: {email: email},
//             success: function(response) {
//                 if(response.available){
//                     document.getElementById('userEmailInfo').innerHTML='사용가능한 이메일 입니다.';
//                 } else {
//                     document.getElementById('userEmailError').innerHTML='이미 사용 중인 이메일입니다.';
//
//                 }
//             },
//             error : function(xhr, status, error){
//                 console.error('Ajax 요청 실패' + status+ ', '+error);
//             }
//         });
//
//
//
//
// };