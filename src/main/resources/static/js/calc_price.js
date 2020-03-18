$(function(){
	calc_price();
		
	$('input[name="size"]').on("change",function() {
		calc_price();
	});
	
	$("input:checkbox").on("change",function() {
		calc_price();
	});

	$("select[name='quantity']").on("change",function() {
		calc_price();
	});
	
	
	function calc_price (){
		var size = $("input:radio:checked").val();
		var topping_count = $("input:checkbox:checked").length;
		var curry_num = $("select[name='quantity'] option:selected").val();
		
		if (size == "M") {
			var size_price = $("#priceSizeM").text().replace(/,/g,'')*1;
			var topping_price = $("#toppingUnitPriceSizeM").text() * topping_count;
		} else {
			var size_price = $("#priceSizeL").text().replace(/,/g,'')*1;
			var topping_price = $("#toppingUnitPriceSizeL").text() * topping_count;
		}
		
		var price = (size_price + topping_price) * curry_num;
		
		console.log(price);
		
		$("#totalprice").text(price.toLocaleString());
		
	};	
});