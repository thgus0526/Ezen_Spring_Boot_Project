const validationStatus ={
    email:false,
    password: false
};


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

// 핸드폰 번호 유효성 검사
function validatePhone() {
    var phone = document.getElementById('phone');
    var phoneInfo = document.getElementById('phoneInfo');
    var phonePattern = /^010\d{8}$/;
    if (!phonePattern.test(phone.value)) {
        phone.classList.add('is-invalid');
        $('#phoneInfo').removeClass('text-info').addClass('text-danger');
        phoneInfo.textContent = "하이픈(-)을 제외한 핸드폰 번호를 입력해주세요. ex) 01012345678";
        validationStatus.phone = false;
        checkAllValidations();
        return false;
    } else {
        $('#phoneInfo').removeClass('text-danger').addClass('text-info');
        phone.classList.remove('is-invalid');
        phoneInfo.textContent = "";
        validationStatus.phone = true;
        checkAllValidations();
        return true;
    }
}
// 비밀번호 유효성 검사
function validatePassword() {
    let password = document.getElementById("password").value;
    let confirmPassword = document.getElementById("passwordConfirm").value;
    let passwordCheck = document.getElementById("passwordCheck");
    let passwordNotMatch = document.getElementById("passwordNotMatch");

    const regex = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[!@#$%^&*])[A-Za-z\d!@#$%^&*]{6,15}$/;

    if (!regex.test(password)) {
        passwordCheck.innerHTML = "";
        passwordNotMatch.innerHTML = "비밀번호는 숫자, 영어, 특수문자를 포함한 15글자 이내여야 합니다.";
        checkAllValidations();
        return false;
    } else if (password !== confirmPassword) {
        passwordCheck.innerHTML = "";
        passwordNotMatch.innerHTML = "비밀번호가 일치하지 않습니다.";
        checkAllValidations();
        return false;
    } else {
        passwordNotMatch.innerHTML = "";
        passwordCheck.innerHTML = "비밀번호가 일치합니다.";
        validationStatus.password = true;
        checkAllValidations();
        return true;
    }
}

function validateEmail() {
    validationStatus.email = false;
    var email = document.getElementById('email');
    var userEmailInfo = document.getElementById('userEmailInfo');
    var emailPattern = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;

    if (!emailPattern.test(email.value)) {
        email.classList.add('is-invalid');
        $('#userEmailInfo').removeClass('text-info').addClass('text-danger');
        userEmailInfo.textContent = "유효한 이메일을 입력해주세요.";

        return false;
    }

    if(!validationStatus.checkEmail){
        email.classList.add('is-invalid');
        $('#userEmailInfo').removeClass('text-info').addClass('text-danger');
        userEmailInfo.textContent = '이메일 인증을 해주세요.';

        return false;
    }
    return true;
}


// 이메일 유효성 검사
function checkUserEmail(){
    validationStatus.checkEmail = true;
    var email = document.getElementById("email");
    var userEmailInfo = document.getElementById('userEmailInfo');

    if(!validateEmail()){
        return;
    }

    $.ajax({
        type: 'GET',
        url: '/user/checkUserEmail',
        data: {email: email.value},
        success: function(response) {
            if(response.available){
                sendVerificationCode();
            } else {
                email.classList.add('is-invalid');
                $('#userEmailInfo').removeClass('text-info').addClass('text-danger');
                userEmailInfo.textContent = '이미 사용 중인 이메일입니다.';
                checkAllValidations();
            }
        },
        error : function(xhr, status, error){
            console.error('Ajax 요청 실패' + status+ ', '+error);
        }
    });

}
// 이메일 인증코드 보내는 함수
function sendVerificationCode(){
    var email = document.getElementById("email");

    $('#userEmailInfo').removeClass('text-danger').removeClass('text-info').addClass('text-success');
    $('#userEmailInfo').text("이메일 인증 코드 전송중입니다.");
    var txt = $('#userEmailInfo').textContent;
    console.log(txt);

    $.ajax({
        url: '/user/send-email',
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify({email: email.value}),
        success: function(response){
            $('#userEmailInfo').text(response);
            email.classList.remove('is-invalid');
            $('#userEmailInfo').removeClass('text-success').addClass('text-info');
            alert('인증 코드가 정상적으로 보내졌습니다,');
            $('#email').prop('readonly', true);
            $('#insertEmail').removeClass('d-none');
            checkAllValidations();
        },
        error: function(response) {
            $('#userEmailInfo').text(response.responseText);
            email.classList.add('is-invalid');
            $('#userEmailInfo').removeClass('text-success').addClass('text-danger');
            alert('인증 코드가 전송되지 않았습니다.');
        }
    })
}

// 이메일 인증 코드 확인
function verifyCode() {
    let email = $('#email').val();
    let verificationCode = $('#verificationCode').val();

    var verificationCodeE = document.getElementById("verificationCode");
    $.ajax({
        url: '/user/verify-code',
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify({ email: email, code: verificationCode }),
        success: function(response) {
            $('#verificationResult').text(response.message);
            if (response.success) {
                verificationCodeE.classList.remove('is-invalid');
                $('#userEmailInfo').removeClass('text-danger').addClass('text-info');
                alert('인증코드가 확인되었습니다.');
                $('.unverifiedCode').addClass('d-none');
                validationStatus.email = true;
                checkAllValidations();
            } else {
                verificationCodeE.classList.add('is-invalid');
                $('#verificationResult').removeClass('text-info').addClass('text-danger');
                alert('인증코드가 틀렸습니다.');
                checkAllValidations();
            }
        },
        error: function(response) {
            $('#verificationResult').text('인증 코드 확인에 실패했습니다.');
            $('#verificationResult').removeClass('text-info').addClass('text-danger');
        }
    });
}