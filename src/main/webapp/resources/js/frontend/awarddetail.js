$(function () {
    //从地址栏中获取userAwardId
    var userAwardId = getQueryString("userAwardId");
    var awardId = getQueryString("awardId");
    //根据userAwardId获取用户奖品映射信息
    var userAwardUrl = '/o2o/frontend/getawardbyuserawardid?userAwardId='+userAwardId;
    var awardUrl = '/o2o/frontend/getawardbyawardid?awardId='+awardId;
    //完成后端奖品管理预览工作，判断是预览店铺奖品还是其他
    if (userAwardId){
        $.getJSON(userAwardUrl,function (data) {
            if (data.success){
                //获取奖品信息并展示
                var award = data.award;
                $('#award-img').attr('src',getContextPath()+award.awardImg);
                $('#create-time').text(new Date(data.userAwardMap.createTime).format("yyyy-MM-dd"));
                $('#award-name').text(award.awardName);
                $('#award-desc').text(award.awardDesc);

                var imgListHtml = '';
                //若未去实体店兑换奖品，生成的礼品的二维码供商家扫描
                if (data.usedStatus == 0){
                    imgListHtml += '<div> <img src="/o2o/frontend/generateqrcode4award?userAwardId='+userAwardId+'" width="100%"/></div>';
                    $('#imgList').html(imgListHtml);
                }
            }
        });
    }else {
        $.getJSON(awardUrl,function (data) {
            if (data.success){
                //获取奖品信息并展示
                var award = data.award;
                $('#award-img').attr('src',getContextPath()+award.awardImg);
                $('#create-time').text(new Date(data.award.createTime).format("yyyy-MM-dd"));
                $('#award-name').text(award.awardName);
                $('#award-desc').text(award.awardDesc);
                var imgListHtml = '';
                $('#imgList').html(imgListHtml);
            }
        });
    }



    //若点击“我的”，则显示侧栏
    $('#me').click(function () {
        $.openPanel('#panel-right-demo');
    });

    $.init();
});