$(function () {
    var awardName = '';
    getList();
    function getList() {
        var listUrl = '/o2o/shopadmin/listuserawardmapsbyshop?pageIndex=0&pageSize=999&awardName='+awardName;
        $.getJSON(listUrl,function (data) {
            if (data.success){
                var userAwardMapList = data.userAwardMapList;
                var tempHtml = '';
                //遍历购买信息列表，拼接出列信息
                userAwardMapList.map(function (item, index) {
                    tempHtml += ''
                        +'<div class="row row-awarddeliver">'
                        +    '<div class="col-10">'+ item.award.awardName +'</div>'
                        +    '<div class="col-40 awarddeliver-time">'+new Date(item.createTime).format("yyyy-MM-dd hh:mm:ss")+'</div>'
                        +    '<div class="col-20">'+ item.user.name +'</div>'
                        +    '<div class="col-10">'+item.point+'</div>'
                        +    '<div class="col-20">'+item.operator.name+'</div>'
                        +'</div>';
                });
                $('.awarddeliver-wrap').html(tempHtml);
            }
        });
    }

    $('#search').on('input', function (e) {
        //当在搜索框里输入信息的时候
        //依据输入的商品名模糊查询该商品的购买记录
        awardName = e.target.value;
        //清空商品购买记录列表
        $('.awarddeliver-wrap').empty();
        //再次加载
        getList();
    });
});