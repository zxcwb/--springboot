$(function () {
    //修改平台密码的controller_url
    var url = '/o2o/local/changelocalpwd';
    //从地址栏的URL里获取userType
    var userType = getQueryString('userType');
    $('#submit').click(function () {
        //获取账号
        var username = $('#username').val();
        //获取原密码
        var password = $('#password').val();
        //获取新密码
        var newPassword = $('#newPassword').val();
        var confirmPassword = $('#confirmPassword').val();

        if (!password || !username){
            $.toast("请输入账号或密码");
            return;
        }

        if (newPassword != confirmPassword){
            $.toast("两次输入的密码不一致");
            return;
        }

        //添加表单数据
        var formData = new FormData();
        formData.append('username',username);
        formData.append('password',password);
        formData.append('newPassword',newPassword);

        //获取验证码
        var verifyCodeActual = $('#j_captcha').val();

        if (!verifyCodeActual){
            $.toast("请输入验证码!");
            return;
        }

        formData.append("verifyCodeActual",verifyCodeActual);

        //将参数post到后台修改密码
        $.ajax({
            url:url,
            type:'POST',
            contentType:false,
            processData:false,
            cache:false,
            data:formData,
            success:function (data) {
                if (data.success){
                    $.toast("提交成功！");
                    if (userType == 1){
                        window.location.href='/o2o/frontend/index';
                    }else {
                        window.location.href='/o2o/shop/shoplist';
                    }
                }else {
                    $.toast("提交失败");
                    $('#captcha_img').click();
                }
            }
        });
    });

    $('#back').click(function () {
        window.location.href='/o2o/local/login?userType='+userType;
    });

});