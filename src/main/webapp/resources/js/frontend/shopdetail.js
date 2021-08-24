$(function () {
    //判断是否正在加载，正在加载就不要加载
    var loading = false;
    //分页允许返回的最大条数，超过此数则禁止访问后台
    var maxItems = 20;
    //默认一页返回的商品数
    var pageSize = 3;
    //列出商品列表的URL
    var listUrl = '/o2o/frontend/listproductsbyshop';
    //默认页码
    var pageNum = 1;
    //从地址栏里获取shopId
    var shopId = getQueryString("shopId");
    var parentId = getQueryString("parentId");
    var productCategoryId = '';
    var productName = '';
    //获取本店铺信息以及商品类别信息列表的URL
    var searchDivUrl = '/o2o/frontend/listshopdetailpageinfo?shopId='+shopId;
    //渲染出店铺的基本信息以及商品类别列表以供搜索查询
    getSearcgDivData();
    //预先加载10条商品信息
    addItems(pageSize,pageNum);

    //给兑换礼品的a标签赋予兑换礼品的URL
    $('#exchangelist').attr('href','/o2o/frontend/awardlist?shopId='+shopId);
    //获取本店铺信息以及商品类别信息列表
    function getSearcgDivData() {
        $.getJSON(
            searchDivUrl,function (data) {
                if (data.success){
                    var shop = data.shop;
                    $('#shop-cover-pic').attr('src',getContextPath()+shop.shopImg);
                    $('#shop-update-time').html(new Date(shop.lastEditTime).format("yyyy-MM-dd"));
                    $('#shop-name').html(shop.shopName);
                    $('#shop-desc').html(shop.shopDesc);
                    $('#shop-addr').html(shop.shopAddr);
                    $('#shop-phone').html(shop.phone);
                    //获取后台返回的该店铺的商品列表
                    var productCategoryList = data.productCategoryList;
                    var html = '';
                    //遍历商品列表，生成可以点击搜索相应的商品类别下的商品的a标签
                    productCategoryList
                        .map(function(item, index) {
                            html += '<a href="#" class="button" data-product-search-id='
                                + item.productCategoryId
                                + '>'
                                + item.productCategoryName
                                + '</a>';
                        });
                    $('#shopdetail-button-div').html(html);
                }
            });
    }

    function addItems(pageSize, pageIndex) {
        // 拼接出查询的URL，赋空值默认就去掉这个条件的限制，有值就代表按照这个条件去查询
        var url = listUrl + '?' + 'pageIndex=' + pageIndex + '&pageSize='
            + pageSize + '&productCategoryId=' + productCategoryId
            + '&productName=' + productName + '&shopId=' + shopId;
        //设定加载符，若还在后台取数据则不能再次访问后台，避免多从重复加载
        loading = true;
        //访问后台获取相应查询条件下的商品列表
        $.getJSON(url, function(data) {
            if (data.success) {
                //获取当前条件下的商品总数
                maxItems = data.count;
                var html = '';
                //遍历出商品列表，拼接出卡片的集合
                data.productList.map(function(item, index) {
                    html += '' + '<div class="card" data-product-id='
                        + item.productId + '>'
                        + '<div class="card-header">' + item.productName
                        + '</div>' + '<div class="card-content">'
                        + '<div class="list-block media-list">' + '<ul>'
                        + '<li class="item-content">'
                        + '<div class="item-media">' + '<img src="'+getContextPath()
                        + item.imgAddr + '" width="44">' + '</div>'
                        + '<div class="item-inner">'
                        + '<div class="item-subtitle">' + item.productDesc
                        + '</div>' + '</div>' + '</li>' + '</ul>'
                        + '</div>' + '</div>' + '<div class="card-footer">'
                        + '<p class="color-gray">'
                        + new Date(item.lastEditTime).format("yyyy-MM-dd")
                        + '更新</p>' + '<span>点击查看</span>' + '</div>'
                        + '</div>';
                });
                //将卡片集合添加到目标的HTML组件里
                $('.list-div').append(html);
                //获取目前为止已显示的卡片总数，包含之前已经加载的
                var total = $('.list-div .card').length;
                if (total >= maxItems){
                    //加载完毕，注销无限加载时间，以防不必要的加载
                    //$.detachInfiniteScroll($('.infinite-scroll'));
                    //删除加载提示符
                    //$('.infinite-scroll-preloader').remove();
                    //隐藏提示符
                    $('.infinite-scroll-preloader').hide();
                }else {
                    $('.infinite-scroll-preloader').show();
                }
                //否则页码加1，继续load出新的店铺
                pageNum += 1;
                //加载结束，可以再次加载了
                loading = false;
                //刷新页面，显示新加载的店铺
                $.refreshScroller();
            }
        });
    }
    $('.pull-left').attr('href','/o2o/frontend/shoplist?parentId='+parentId);
    //下滑屏幕进行自动搜索
    $(document).on('infinite', '.infinite-scroll-bottom', function() {
        if (loading)
            return;
        addItems(pageSize, pageNum);
    });

    //选择新的商品类别之后，重置页码，清空原先的商品列表，按照新的类别去查询
    $('#shopdetail-button-div').on(
        'click',
        '.button',
        function(e) {
            //获取商品类别Id
            productCategoryId = e.target.dataset.productSearchId;
            if (productCategoryId) {
                //若之前已选定了别的category，则移除其选定效果，改成选定新的
                if ($(e.target).hasClass('button-fill')) {
                    $(e.target).removeClass('button-fill');
                    productCategoryId = '';
                } else {
                    $(e.target).addClass('button-fill').siblings()
                        .removeClass('button-fill');
                }
                $('.list-div').empty();
                pageNum = 1;
                addItems(pageSize, pageNum);
            }
        });

    //点击卡片进入该商品的详情页
    $('.list-div')
        .on(
            'click',
            '.card',
            function(e) {
                var productId = e.currentTarget.dataset.productId;
                window.location.href = '/o2o/frontend/productdetail?productId='
                    + productId;
            });

    //需要查询的商品名字发生变化后，重置页码，情空商品列表，按照新的名字去查询
    $('#search').on('change', function(e) {
        productName = e.target.value;
        $('.list-div').empty();
        pageNum = 1;
        addItems(pageSize, pageNum);
    });

    //点击后打开有侧栏
    $('#me').click(function () {
        $.openPanel('#panel-right-demo');
    });

    $.init();
});