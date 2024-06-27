// csrf 토큰
let userId
$(document).ready(function() {

    let token = $("meta[name='_csrf']").attr("content");
    let header = $("meta[name='_csrf_header']").attr("content");
    userId = $("meta[name='user-id']").attr("content");
    console.log(userId);
    $(document).ajaxSend(function(e, xhr, options) {
        xhr.setRequestHeader(header, token);
    });
});