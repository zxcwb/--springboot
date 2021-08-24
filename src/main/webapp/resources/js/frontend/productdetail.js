$(function() {
	var productId = getQueryString('productId');
	var productUrl = '/o2o/frontend/listproductdetailpageinfo?productId=' + productId;
	$.getJSON(
		productUrl, function(data) {
			if (data.success) {
				//获取商品相关信息
				var product = data.product;
				//给商品信息相关HTML控件赋值
				//商品缩略图
				$('#product-img').attr('src', getContextPath()+product.imgAddr);
				//商品更新时间
				$('#product-time').text(
					new Date(product.lastEditTime)
						.format("yyyy-MM-dd"));
				if (product.piont != undefined){
					$('#product-point').text('购买可得'+product.point+'积分');
				}
				//商品价格 原价 现价(折扣价)
				$('#product_normalPrice').text('原价:  '+product.normalPrice);
				$('#product_promotionPrice').text('折扣价:  '+product.promotionPrice+'元');
				//商品名称，商品描述
				$('#product-name').text(product.productName);
				$('#product-desc').text(product.productDesc);

				var imgListHtml = '';
				product.productImgList.map(function(item, index) {
					imgListHtml += '<div> <img src="'+getContextPath()
						+ item.imgAddr + '"/></div>';
				});

				// 生成购买商品的二维码供商家扫描,后面开发此功能
				if (data.needQRCode){
				  //若顾客已登录，则生成购买商品的二维码供商家扫描
					imgListHtml += '<div> <img src="/o2o/frontend/generateqrcode4product?productId='
						+product.productId+'" width="100%"></div>';
				}
				$('#imgList').html(imgListHtml);
			}
		});

	$('#me').click(function() {
		$.openPanel('#panel-right-demo');
	});

	$.init();
});
