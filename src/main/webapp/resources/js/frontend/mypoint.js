$(function () {
    var loading = false;
    var maxItems = 20;
    var pageSize = 10;

    //获取顾客各店铺积分记录列表的URL
    var listUrl = '/o2o/frontend/listusershopmapsbycustomer';
    var pageNum = 1;
    var shopName = '';
    addItems(pageSize,pageNum);

    //按照查询条件获取消费记录列表，并生成对应的HTML元素添加到页面中
    function addItems(pageSize,pageIndex) {
        //生成新条目的HTML
        var url = listUrl + '?pageIndex='+pageIndex +'&pageSize='+pageSize
            +'&shopName='+shopName;
        loading = true;
        $.getJSON(url,function (data) {
            if (data.success) {
                //获取总数
                maxItems = data.count;
                var html = '';
                data.userShopMapList.map(function (item,index) {
                    html += ''
                        +   '<div class="card" data-shop-id="'+ item.shop.shopId +'">'
                        +       '<div class="card-header">'+ item.shop.shopName +'</div>'
                        +       '<div class="card-content">'
                        +            '<div class="list-block media-list">'
                        +                '<ul>'
                        +                    '<li class="item-content">'
                        +                       '<div class="item-inner">'
                        +                           '<div class="item-subtitle">本店积分:'+ item.point +'</div>'
                        +                       '</div>'
                        +                   '</li>'
                        +               '</ul>'
                        +           '</div>'
                        +       '</div>'
                        +       '<div class="card-footer">'
                        + '<p class="color-gray">更新时间'
                        + new Date(item.createTime).format("yyyy-MM-dd")
                        +'</p>'
                        +       '</div>'
                        +   '</div>';
                });
                $('.list-div').append(html);
                var total = $('.list-div .card').length;
                if (total >= maxItems) {
                    // 加载完毕，则注销无限加载事件，以防不必要的加载
                    $.detachInfiniteScroll($('.infinite-scroll'));
                    // 删除加载提示符
                    $('.infinite-scroll-preloader').remove();
                }
                pageNum += 1;
                loading = false;
                $.refreshScroller();
            }
        });
    }

    $(document).on('infinite','.infinite-scroll-bottom',function () {
        //无极滚动
        if (loading)return;
        addItems(pageSize,pageNum);
    });

    //搜索查询条件，按照奖品名模糊查询
    $('#search').on('input',function (e) {
        shopName = e.target.value;
        $('.list-div').empty();
        pageNum = 1;
        addItems(pageSize,pageNum);
    });

    //侧边栏按钮事件绑定
    $('#me').click(function () {
        $.openPanel('#panel-right-demo');
    });

    $.init();
});