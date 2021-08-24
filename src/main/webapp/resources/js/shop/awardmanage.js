$(function () {
    //获取此店铺下的奖品列表的URL shopAdmin是为了解决前端和后端显示奖品的问题
    var listUrl = '/o2o/shopadmin/listawardsbyshop?pageIndex=1&pageSize=999&shopAdmin=1';
    var statusUrl = '/o2o/shopadmin/modifyaward';
    getList();

    function getList() {
        //从后台获取此店铺奖品列表
        $.getJSON(listUrl, function(data) {
                if (data.success) {
                    var awardList = data.awardList;
                    var tempHtml = '';
                    /*
                    * 遍历每条奖品信息，拼接成一行显示，列信息包括；
                    * 奖品名称，优先级、上架\下架（含awardId），编辑按钮（含awardId）
                    * 预览（含awardId）
                    * */
                    awardList.map(function(item, index) {
                            var textOp = "下架";
                            var contraryStatus = 0;
                            if (item.enableStatus == 0){
                                //若状态值为0，表明是已下架的奖品，操作变为上架（即点击上架按钮上架相关奖品）
                                textOp = "上架";
                                contraryStatus = 1;
                            }else {
                                contraryStatus = 0;
                            }
                            //拼接每件奖品的行信息
                            tempHtml += ''
                                + '<div class="row row-award">'
                                + '<div class="col-33">'
                                + item.awardName
                                + '</div>'
                                + '<div class="col-20">'
                                + item.point
                                + '</div>'
                                + '<div class="col-40"><a href="#" class="edit" data-id="'+
                                item.awardId
                                + '" data-status="'
                                + item.enableStatus
                                + '">编辑&nbsp;</a>'
                                + '<a href="#" class="status" data-id="'
                                + item.awardId
                                + '" data-status="'
                                + contraryStatus
                                + '">&nbsp;'
                                + textOp
                                + '&nbsp;</a>'
                                + '<a href="#" class="preview" data-id="'
                                + item.awardId
                                + '" data-status="'
                                + item.enableStatus
                                + '">&nbsp;预览</a>'
                                + '</div>'
                                + '</div>';
                        });
                    //将拼接好的信息赋值进html控件中
                    $('.award-wrap').html(tempHtml);
                }
            });
    }

    //将class为award-wrap里面的a标签绑定上点击事件
    $('.award-wrap').on('click', 'a', function(e) {
        var target = $(e.currentTarget);
        if (target.hasClass('edit')) {
            //alert(e.currentTarget.dataset.id);
            //如果有class edit则进入奖品信息编辑页面，并带有awardId参数
            window.location.href = '/o2o/shop/awardedit?awardId='
                + e.currentTarget.dataset.id;
        } else if (target.hasClass('status')) {
            //如果有class status则调用后台功能上/下架相关奖品，并带有awardId参数
            changeItemStatus(e.currentTarget.dataset.id,
                e.currentTarget.dataset.status);
        } else if (target.hasClass('preview')) {
            //如果有class preview则去前台展示系统该奖品详情页预览奖品情况
            window.location.href = '/o2o/frontend/awarddetail?awardId='
                + e.currentTarget.dataset.id;
        }
    });


    function changeItemStatus(id, enableStatus) {
        var award = {};
        award.awardId = id;
        award.enableStatus = enableStatus;
        $.confirm('确定么?', function() {
            //上架相关奖品
            $.ajax({
                url : statusUrl,
                type : 'POST',
                data : {
                    awardStr : JSON.stringify(award),
                    statusChange : true
                },
                dataType : 'json',
                success : function(data) {
                    if (data.success) {
                        $.toast('操作成功！');
                        getList();
                    } else {
                        $.toast('操作失败！');
                    }
                }
            });
        });
    }


    $('#new').click(function () {
        window.location.href='/o2o/shop/awardedit';
    });

});