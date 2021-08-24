$(function () {
     var shopId = getQueryString('shopId');
     var shopInfoUrl = '/o2o/shopadmin/getshopmanagerinfo?shopId='+shopId;
     $.getJSON(shopInfoUrl,function (data) {
         if (data.redirect){
             window.location.href=data.url;
         }else {
             if (data.shopId != undefined && data.shopId != null){
                 shopId = data.shopId;
             }
             $('#shopInfo').attr('href','/o2o/shop/shopedit?shopId='+shopId);
         }
     })
})