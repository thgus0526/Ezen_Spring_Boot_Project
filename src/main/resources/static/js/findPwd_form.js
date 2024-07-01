
// 이메일 유효성 검사


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
                window.location.href = '/user/changePwd';
            } else {
                verificationCodeE.classList.add('is-invalid');
                $('#verificationResult').removeClass('text-info').addClass('text-danger');
                alert('인증코드가 틀렸습니다.');

            }
        },
        error: function(response) {
            $('#verificationResult').text('인증 코드 확인에 실패했습니다.');
            $('#verificationResult').removeClass('text-info').addClass('text-danger');
        }
    });
}