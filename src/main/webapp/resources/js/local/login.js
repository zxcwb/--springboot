$(function () {
    //登录验证的controller url
    var loginUrl = '/o2o/local/logincheck';
    //从地址栏的URL里获取userType
    //var userType = getQueryString('userType');
    //登录次数，累计三次失败之后自动弹出要求验证码输入
    var loginCount = 0;

    $('#submit').click(function () {
        //获取输入的账号
        var username = $('#username').val();
        //获取输入的密码
        var password = $('#password').val();
        //获取验证码信息
        var verifyCodeActual = $('#j_captcha').val();
        //是否需要验证码验证，默认为false，即不需要
        var needVerify = false;

        //如果三次都失败了改为true
        if (loginCount >= 3){
            //那么就需要验证码验证了
            if (!verifyCodeActual){
                $.toast("请输入验证码！");
                return;
            }else {
                needVerify = true;
            }
        }

        //访问后台进行登录验证
        $.ajax({
            url:loginUrl,
            async:false,
            cache:false,
            type:'post',
            dataType:'json',
            data:{
                username:username,
                password:password,
                verifyCodeActual:verifyCodeActual,
                needVerify:needVerify
            },
            success:function (data) {
                if (data.success){
                    $.toast("登录成功！");
                    if (data.userType == 1){
                        window.location.href='/o2o/frontend/index';
                    }else if (data.userType == 2){
                        window.location.href='/o2o/shop/shoplist';
                    }
                }else {
                    $.toast("登录失败！");
                    loginCount++;
                    if(loginCount >= 3){
                        //登录三次失败，需要验证码验证
                        $("#verifyPart").show();
                    }
                }
            }
        });

        $('#register').click(function () {
            window.location.href='/o2o/local/registry';
        });
    });
});