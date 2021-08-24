$(function () {
   //列出该店铺下所有授权信息的URL
   var listUrl = '/o2o/shopadmin/listshopauthmapsbyshop?pageIndex=1&pageSize=999';
   //列出修改权限信息的url
   var modifyUrl = '/o2o/shopadmin/modifyshopauthmap';

    function getList() {
        $.getJSON(listUrl, function (data) {
            if (data.success) {
                var shopauthList = data.shopAuthMapList;
                var tempHtml = '';
                shopauthList.map(function (item, index) {
                    var testOp = '恢复';
                    var contraryStatus = 0;
                    if (item.enableStatus == 1){
                        //若状态值为1，说明授权生效，操作变为删除，点击删除可以禁用此功能
                        testOp = '删除';
                        contraryStatus =  0;
                    }else {
                        contraryStatus = 1;
                    }
                    tempHtml += ''
                        +      '<div class="row row-shopauth">'
                        +          '<div class="col-40">'+ item.employee.name +'</div>';

                    if(item.titleFlag != 0) {
                        //若不是店家本人的授权信息,则加入编辑以及改变状态等操作

                        tempHtml += '<div class="col-20">' + item.title + '</div>'
                        + '<div class="col-40">'
                        + '<a href="#" class="edit" data-employee-id="' + item.employee.userId
                        + '" data-auth-id="' + item.shopAuthId
                        + '" data-status="' + item.enableStatus + '">编辑</a>'
                        + '<a href="#" class="delete" data-employee-id="' + item.employee.userId
                        + '" data-auth-id="' + item.shopAuthId + '" data-status="' + contraryStatus
                        + '">'+testOp+'</a>'
                        + '</div>'
                        + '</div>';
                    }else {
                        //若为店家，则不允许操作
                        tempHtml += '<div class="col-20">' + item.title
                        + '</div>' + '<div class="col-40">'
                        + '<span>不可操作</span>'+'</div>'
                    }
                    tempHtml += '</div>'
                });
                $('.shopauth-wrap').html(tempHtml);
            }
        });
    }

    getList();

    /*
    * 给a标签的click事件绑定上对应的方法 即点击带有edit的a标签就会跳转到授权编辑页面，
    * 点击带有status的a标签就会去更新授权信息的状态
    * */
    $('.shopauth-wrap').on('click', 'a', function (e) {
        var target = $(e.currentTarget);
        if (target.hasClass('edit')) {
            window.location.href = '/o2o/shopadmin/shopauthedit?shopauthId='
                + e.currentTarget.dataset.authId;
        } else if (target.hasClass('status')) {
            changeStatus(e.currentTarget.dataset.authId,e.currentTarget.dataset.status);
        }
    });

    function changeStatus(id,status) {
        var shopAuth = {};
        shopAuth.shopAuthId = id;
        shopAuth.enableStatus = status; //1为可用 0为不可用
        $.confirm("确定吗?",function () {
            $.ajax({
                url:modifyUrl,
                type: 'POST',
                data: {
                    //json参数转化为字符串
                    shopAuthMapStr : JSON.stringify(shopAuth),
                    statusChange: true,
                },
                dataType: 'json',
                success: function (data) {
                    if (data.success){
                        $.toast("操作成功！");
                        getList();
                    }else {
                        $.toast("操作失败！");
                    }
                }
            });
        });
    }

    function deleteItem(id) {
        $.confirm('确定么?', function () {
            $.ajax({
                url: deleteUrl,
                type: 'POST',
                data: {
                    shopAuthId: id,
                },
                dataType: 'json',
                success: function (data) {
                    if (data.success) {
                        $.toast('删除成功！');
                        getList();
                    } else {
                        $.toast('删除失败！');
                    }
                }
            });
        });
    }


});