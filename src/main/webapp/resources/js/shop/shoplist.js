$(function () {
    getlist();
    //获取店铺信息
     function getlist() {
         $.ajax({
             url:"/o2o/shopadmin/getshoplist",
             type:"get",
             data:"json",
             success:function (data) {
                 if (data.success){
                     handleList(data.shopList);
                     handleUser(data.user);
                 }
             }
         })
     }

     function handleUser(data) {
         $('#user-name').text(data.name);
     }

     //遍历店铺信息
     function handleList(data) {
         var html = '';
         data.map(function (item,index) {
             html += '<div class="row row-shop"><div class="col-40">'
                 + item.shopName +'</div><div class="col-40">'
                 + shopStatus(item.enableStatus) +'</div><div class="col-20">'
                 + goShop(item.enableStatus, item.shopId) +'</div></div>';
         });
         $('.shop-wrap').html(html);
     }

     //获取店铺状态
    function shopStatus(status) {
              if (status == 0){
                  return '审核中';
              }else if (status == -1){
                  return '店铺非法';
              }else if (status == 1){
                  return '审核通过';
              }
    }

    //进入店铺页
    function goShop(status,shopId) {
      if (status != 0 && status != -1){
          return '<a href="/o2o/shop/shopmanage?shopId='+shopId+'">进入</a>';
      }else {
          return '';
      }
    }

})