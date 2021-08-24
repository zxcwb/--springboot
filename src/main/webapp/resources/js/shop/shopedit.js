$(function () {

    var shopId = getQueryString('shopId');
    var isEdit = shopId ? true : false;
    var shopInfoUrl = "/o2o/shopadmin/getshopbyid?shopId="+shopId;
    var editShopUrl = "/o2o/shopadmin/registershop";

    if (isEdit){
        editShopUrl = "/o2o/shopadmin/modifyshop";
        getShopInfo(shopId)
    }

    function getShopInfo(shopId) {
        $.getJSON(shopInfoUrl, function (data) {
            if (data.success) {
                var shop = data.shop;
                $('#shop-name').val(shop.shopName);
                $('#shop-addr').val(shop.shopAddr);
                $('#shop-phone').val(shop.phone);
                $('#shop_desc').val(shop.shopDesc);
                var shopCategory = '<option data-id="' + shop.shopCategory.shopCategoryId + '"selected>'
                    + shop.shopCategory.shopCategoryName + '</option>';  // -->可能出错的位置

                var tempAreaHtml = '';
                data.areaList.map(function (item, index) {
                    tempAreaHtml += '<option data-id="' + item.areaId + '">'
                        + item.areaName + '</option>';
                });

                $('#shop-category').html(shopCategory);
                $('#shop-category').attr('disabled', 'disabled'); //不可修改的
                $('#area').html(tempAreaHtml);
                //$('#area').attr('data-id',shop.areaId);
                $("#area option[data-id='"+shop.area.areaId+"']").attr("selected", "selected")
            }
        });
    }

    $('#submit').click(function () {
        var shop = {};
        //修改店铺，必须传入此值
        if (isEdit){
            shop.shopId=shopId;
        }
        shop.shopName = $('#shop-name').val();
        shop.shopAddr = $('#shop-addr').val();
        shop.phone = $('#shop-phone').val();
        shop.shopDesc = $('#shop_desc').val();
        shop.shopCategory = {
            shopCategoryId: $('#shop-category').find('option').not(function () {
                return !this.selected;
            }).data('id')
        };
        shop.area = {
            areaId: $('#area').find('option').not(function () {
                return !this.selected;
            }).data('id')
        };
        var shopImg = $('#shop-img')[0].files[0];
        var formData = new FormData();
        formData.append('shopImg', shopImg);
        formData.append('shopStr', JSON.stringify(shop));

        //验证码！！！
        var verifyCodeActual = $('#j_captcha').val();
        if (!verifyCodeActual) {
            $.toast("请输入验证码！")
            return;
        }
        formData.append("verifyCodeActual", verifyCodeActual);
        $.ajax({
            url: editShopUrl,
            type: 'POST',
            data: formData,
            contentType: false,
            processData: false,
            cache: false,
            success: function (data) {
                if (data.success) {
                    $.toast('提交成功！')
                } else {
                    $.toast('提交失败!' + data.errMsg);
                }
                $('#captcha_img').click();
            }
        });
    });
});