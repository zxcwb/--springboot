$(function () {
    var name = '';
    getList();

    function getList() {
        var listUrl = '/o2o/shopadmin/listusershopmapsbyshop?pageIndex=0&pageSize=999&name='+name;
        $.getJSON(listUrl,function (data) {
            if (data.success){
                var userShopMapList = data.userShopMapList;
                var tempHtml = '';
                //遍历购买信息列表，拼接出列信息
                userShopMapList.map(function (item, index) {
                    tempHtml += ''
                        +'<div class="row row-usershopcheck">'
                        +    '<div class="col-40">'+ item.user.name +'</div>'
                        +    '<div class="col-60">'+item.point+'</div>'
                        +'</div>';
                });
                $('.usershopcheck-wrap').html(tempHtml);
            }
        });
    }

    $('#search').on('input', function (e) {
        //当在搜索框里输入信息的时候
        //依据输入的商品名模糊查询该商品的购买记录
        name = e.target.value;
        //清空商品购买记录列表
        $('.usershopcheck-wrap').empty();
        //再次加载
        getList();
    });
});