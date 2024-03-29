;$(function(){
	var init = function (){
		initBuyBtn();
		$('#addToCart').click(addProductToCart);
		$('#addProductPopup .count').change(calculateCost);
		$('#loadMore').click(loadMoreProducts);
		initSearchForm();
		$('#goSearch').click(goSearch);
		$('.remove-product').click(removeProductFromCart);
	};

	var showAddProductPopup = function (){
		var idProduct = $(this).attr('data-id-product');
		var product = $('#product'+idProduct);
		$('#addProductPopup').attr('data-id-product', idProduct);
		$('#addProductPopup .product-image').attr('src', product.find('.thumbnail img').attr('src'));
		$('#addProductPopup .name').text(product.find('.name').text());
		var price = product.find('.price').text();
		$('#addProductPopup .price').text(price);
		$('#addProductPopup .category').text(product.find('.category').text());
		$('#addProductPopup .producer').text(product.find('.producer').text());
		$('#addProductPopup .count').val(1);
		$('#addProductPopup .cost').text(price);
		$('#addToCart').removeClass('hidden');
		$('#addToCartIndicator').addClass('hidden');
		$('#addProductPopup').modal({
			show:true
		});
	};
	var initBuyBtn = function(){
		$('.buy-btn').click(showAddProductPopup);
	};
	var addProductToCart = function (){
		var idProduct = $('#addProductPopup').attr('data-id-product');
		var count = $('#addProductPopup .count').val();
		var btn = $('#addToCart');
		convertButtonToLoader(btn, 'btn-primary');
		$.ajax({
			url : '/ajax/json/product/add',
			method : 'POST',
			data :{
				idProduct : idProduct,
				count : count
			},
			success : function(data) {
				$('#currentShoppingCart .total-count').text(data.totalCount);
				$('#currentShoppingCart .total-cost').text(data.totalCost);
				$('#currentShoppingCart').removeClass('hidden');
				$('#addProductPopup').modal('hide');
			},
			error : function(data) {
				convertLoaderToButton(btn, 'btn-primary', addProductToCart);
				alert('Error');
			}
		});
	};
	var calculateCost = function(){
		var priceStr = $('#addProductPopup .price').text();
		var price = parseFloat(priceStr.replace('$',' '));
		var count = parseInt($('#addProductPopup .count').val());
		var min = parseInt($('#addProductPopup .count').attr('min'));
		var max = parseInt($('#addProductPopup .count').attr('max'));
		if(count >= min && count <= max) {
			var cost = price * count;
			$('#addProductPopup .cost').text('$ '+cost);
		} else {
			$('#addProductPopup .count').val(1);
			$('#addProductPopup .cost').text(priceStr);
		}
	};


	var convertButtonToLoader = function (btn, btnClass) {
		btn.removeClass(btnClass);
		btn.removeClass('btn');
		btn.addClass('load-indicator');
		var text = btn.text();
		btn.text('');
		btn.attr('data-btn-text', text);
		btn.off('click');
	};
	var convertLoaderToButton = function (btn, btnClass, actionClick) {
		btn.removeClass('load-indicator');
		btn.addClass('btn');
		btn.addClass(btnClass);
		btn.text(btn.attr('data-btn-text'));
		btn.removeAttr('data-btn-text');
		btn.click(actionClick);
	};

	var loadMoreProducts = function (){ // настройка поведения кнопки при нажатии
		var btn = $('#loadMore');
		convertButtonToLoader(btn, 'btn-success');
		var pageNumber = parseInt($('#productList').attr('data-page-number'));// считываем параметры страницы product.jsp и передаем их на сервер
		var url = '/ajax/html/more' + location.pathname + '?page=' + (pageNumber + 1) + '&' + location.search.substring(1);// считали страницу 1 и при нажатии кнопки мы отправляем на сервер ajax запрос и в запросе указать page текущую + 1
		$.ajax({
			url : url,
			success : function(html) {
				$('#productList .row').append(html);
				pageNumber= pageNumber++;
				var pageCount = parseInt($('#productList').attr('data-page-count'));// считываем параметры страницы product.jsp и передаем их на сервер
				if(pageNumber < pageCount){
					$('#productList').attr('data-page-number', pageNumber) // обнавляем текущую страницу
					convertLoaderToButton(btn, 'btn-success', loadMoreProducts);
				}else { // pageNumber >=  pageCount означает что мы достигли предела отображения данных на странице
					btn.remove();
				}

			},
			error : function(data) {
				convertLoaderToButton(btn, 'btn-success', loadMoreProducts);
				alert('Error');
			}
		});
	};


	var initSearchForm = function (){
		$('#allCategories').click(function(){
			$('.categories .search-option').prop('checked', $(this).is(':checked'));
		});
		$('.categories .search-option').click(function(){
			$('#allCategories').prop('checked', false);
		});
		$('#allProducers').click(function(){
			$('.producers .search-option').prop('checked', $(this).is(':checked'));
		});
		$('.producers .search-option').click(function(){
			$('#allProducers').prop('checked', false);
		});
	};
	var goSearch = function(){
		var isAllSelected = function(selector) {
			var unchecked = 0;
			$(selector).each(function(index, value) {
				if(!$(value).is(':checked')) {
					unchecked ++;
				}
			});
			return unchecked === 0;
		};
		if(isAllSelected('.categories .search-option')) {
			$('.categories .search-option').prop('checked', false);
		}
		if(isAllSelected('.producers .search-option')) {
			$('.producers .search-option').prop('checked', false);
		}
		$('form.search').submit();
	};
	var confirm = function (msg, okFunction) {
		if(window.confirm(msg)) {
			okFunction();
		}
	};
	var removeProductFromCart = function (){
		var btn = $(this);
		confirm('Are you sure?', function(){
			executeRemoveProduct(btn);
		});
	};
	var refreshTotalCost = function () {
		var total = 0;
		$('#shoppingCart .item').each(function(index, value) {
			var count = parseInt($(value).find('.count').text());
			var price = parseFloat($(value).find('.price').text().replace('$', ' '));
			var val = price * count;
			total = total + val;
		});
		$('#shoppingCart .total').text('$'+total);
	};
	var executeRemoveProduct = function (btn) {
		var idProduct = btn.attr('data-id-product');
		var count = btn.attr('data-count');
		btn.removeClass('btn-danger');
		btn.removeClass('btn');
		btn.addClass('load-indicator');
		var text = btn.text();
		btn.text('');
		btn.off('click');

		setTimeout(function(){
			var data = {
				totalCount : 1,
				totalCost : 1
			};
			if(data.totalCount === 0) {
				window.location.href = 'products.html';
			} else {
				var prevCount = parseInt($('#product'+idProduct+' .count').text());
				var remCount = parseInt(count);
				if(remCount === prevCount) {
					$('#product'+idProduct).remove();

					//
					if($('#shoppingCart .item').length === 0) {
						window.location.href = 'products.html';
					}
					//
				} else {
					btn.removeClass('load-indicator');
					btn.addClass('btn-danger');
					btn.addClass('btn');
					btn.text(text);
					btn.click(removeProductFromCart);
					$('#product'+idProduct+' .count').text(prevCount - remCount);
					if(prevCount - remCount == 1) {
						$('#product'+idProduct+' a.remove-product.all').remove();
					}
				}
				refreshTotalCost();
			}
		}, 1000);
	}

	init();
});