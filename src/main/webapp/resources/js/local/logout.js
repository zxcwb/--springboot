$(function () {
    $('#log-out').click(function () {
        //清除session
        $.ajax({
            url:'/o2o/local/logout',
            type:'post',
            async:false,
            cache:false,
            dataType:'json',
            success:function (data) {
                if (data.success){
                    var userType = $('#log-out').attr('userType');
                    //清除成功后退出登录页面
                    window.location.reload();
                    window.location.href='/o2o/local/login?userType='+userType;
                    return false;
                }
            },
            error:function (data,error) {
                alert(error);
            }
        })
    })
});