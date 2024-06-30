const validationStatus   = {
    id: false,
    checkId: false,
    name: false,
    phone: false,
    zipcode: false,
    email: false,
    checkEmail: false,
    password: false,
    birth: false
};


// 아이디 유효성 검사
function validateId() {
    validationStatus.id = false;
    var id = document.getElementById('userId');
    var userIdInfo = document.getElementById('userIdInfo');
    const regex = /^[a-zA-Z][a-zA-Z0-9]{3,14}$/;
    const firstCharRegex = /^[a-zA-Z]/;  // 첫 글자가 영문인지 확인하는 정규식

    if (id.value.trim() === "") {
        id.classList.add('is-invalid');
        $('#userIdInfo').removeClass('text-info').addClass('text-danger');
        userIdInfo.textContent = "사용자 ID를 입력해주세요.";
        checkAllValidations();
        return false;
    } else {
        if (!firstCharRegex.test(id.value)) {
            id.classList.add('is-invalid');
            $('#userIdInfo').removeClass('text-info').addClass('text-danger');
            userIdInfo.textContent = 'ID는 첫 글자가 영문자여야 합니다.';
            checkAllValidations();
            return false;
        } else if (!regex.test(id.value)) {
            id.classList.add('is-invalid');
            $('#userIdInfo').removeClass('text-info').addClass('text-danger');
            userIdInfo.textContent = 'ID는 4자에서 15자의 영어 문자여야 합니다.';
            checkAllValidations();
            return false;
        } else {
            if(!validationStatus.checkId){
                userIdInfo.textContent = '';
                id.classList.add('is-invalid');
                $('#userIdInfo').removeClass('text-info').addClass('text-danger');
                userIdInfo.textContent = '아이디 중복검사를 해주세요.';
            }
            checkAllValidations();
            return true;
        }
    }
}

// 아이디 유효성 검사
function checkUserId(){
    validationStatus.checkId = true;
    let userId = document.getElementById('userId');
    var userIdInfo = document.getElementById('userIdInfo');

    if(!validateId()){
        return;
    }

    $.ajax({
        type: 'GET',
        url: '/user/checkUserId',
        data: {userId: userId.value},
        success: function(response) {
            if(response.available){
                userId.classList.remove('is-invalid');
                $('#userIdInfo').removeClass('text-danger').addClass('text-info');
                userIdInfo.textContent = "사용가능한 아이디 입니다.";
                validationStatus.id = true;
                console.log("qeqwe12312", validationStatus.id);
                $('#userId').prop('readonly', true);
                checkAllValidations();
            } else {
                userId.classList.add('is-invalid');
                $('#userIdInfo').removeClass('text-info').addClass('text-danger');
                userIdInfo.textContent = "이미 사용 중인 아이디입니다.";
                validationStatus.id = false;
                checkAllValidations();
            }
        },
        error : function(xhr, status, error){
            console.error('Ajax 요청 실패' + status+ ', '+error);
        }
    });

}

// 이름 유효성 검사
function validateName() {
    var name = document.getElementById('name');
    var nameInfo = document.getElementById('nameInfo');
    const regex = /^[a-zA-Z가-힣]{2,10}$/;

    if (regex.test(name.value)) {
        name.classList.remove('is-invalid');
        $('#nameInfo').removeClass('text-danger').addClass('text-info');
        nameInfo.textContent = "";
        validationStatus.name = true;
        checkAllValidations();
        return false;
    } else {
        name.classList.add('is-invalid');
        $('#nameInfo').removeClass('text-info').addClass('text-danger');
        nameInfo.textContent = '이름은 2자에서 10자 사이여야 합니다.';
        validationStatus.name = false;
        checkAllValidations();
        return true;
    }
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

function validateZipcode() {
    var zipcode = document.getElementById('addressZipcode');
    var zipcodeInfo = document.getElementById('zipcodeInfo');
    if (zipcode.value.trim() === "") {
        $('#zipcodeInfo').removeClass('text-info').addClass('text-danger');
        zipcode.classList.add('is-invalid');
        zipcodeInfo.textContent = "우편번호를 입력해주세요.";
        validationStatus.zipcode = false;
        checkAllValidations();
        return false;
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
        checkAllValidations();
        return false;
    }

    if(!validationStatus.checkEmail){
        email.classList.add('is-invalid');
        $('#userEmailInfo').removeClass('text-info').addClass('text-danger');
        userEmailInfo.textContent = '이메일 인증을 해주세요.';
        checkAllValidations();
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

function showDatePicker() {
    var birthInput = document.getElementById('birth');
    var currentDate = new Date();

    // Date 객체를 이용해 날짜 형식을 yyyy-mm-dd로 만듭니다.
    var year = currentDate.getFullYear();
    var month = ('0' + (currentDate.getMonth() + 1)).slice(-2); // 월은 0부터 시작하므로 +1을 해주고, 두 자리로 만듭니다.
    var day = ('0' + currentDate.getDate()).slice(-2); // 일을 두 자리로 만듭니다.
    var formattedDate = year + '-' + month + '-' + day;

    birthInput.setAttribute('type', 'date'); // HTML5에서 지원하는 date 타입으로 변경합니다.
    birthInput.setAttribute('max', formattedDate); // 최대 날짜를 오늘 날짜로 설정합니다.
    birthInput.focus(); // 입력 필드에 포커스를 줍니다.
}

function validateBirth() {
    var birth = document.getElementById('birth');
    var birthInfo = document.getElementById('birthInfo');
    var birthPattern = /^\d{4}-\d{2}-\d{2}$/; // YYYY-MM-DD 형식
    if (!birthPattern.test(birth.value)) {
        birth.classList.add('is-invalid');
        $('#birthInfo').removeClass('text-info').addClass('text-danger');
        birthInfo.textContent = "유효한 생년월일을 입력해주세요. (YYYY-MM-DD)";
        validationStatus.birth = false;
        checkAllValidations();
        return false;
    } else {
        birth.classList.remove('is-invalid');
        birthInfo.textContent = "";
        validationStatus.birth = true;
        checkAllValidations();
        return true;
    }
}

// 회원가입 스크립트
// 주소
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

    var zipcode = document.getElementById('addressZipcode');
    var zipcodeInfo = document.getElementById('zipcodeInfo');

    $('#zipcodeInfo').removeClass('text-danger').addClass('text-info');
    zipcode.classList.remove('is-invalid');
    zipcodeInfo.textContent = "";
    validationStatus.zipcode = true;

    checkAllValidations();
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

// 회원가입 버튼 활성화 검사
function checkAllValidations() {
    // const submitButton = document.getElementById('join');
    // const isAllValid = validationStatus.id && validationStatus.checkId &&
    //         validationStatus.password && validationStatus.name && validationStatus.phone &&
    //     validationStatus.zipcode && validationStatus.email &&
    //     validationStatus.checkEmail && validationStatus.birth;
    //
    // // console.log("validationStatus.id", validationStatus.id);
    // // console.log("validationStatus.checkId", validationStatus.checkId);
    // // console.log("validationStatus.name", validationStatus.name);
    // // console.log("validationStatus.phone", validationStatus.phone);
    // // console.log("validationStatus.zipcode", validationStatus.zipcode);
    // // console.log("validationStatus.email", validationStatus.email);
    // // console.log("validationStatus.checkEmail", validationStatus.checkEmail);
    // // console.log("validationStatus.password", validationStatus.password);
    // // console.log("validationStatus.birth", validationStatus.birth);
    // // console.log("-----------------------------------------------------------------");
    //
    // if (isAllValid) {
    //     $('#join').removeClass('btn-secondary').addClass('btn-primary');
    //     submitButton.disabled = false;
    // } else {
    //     $('#join').removeClass('btn-primary').addClass('btn-secondary');
    //     submitButton.disabled = true;
    // }
}