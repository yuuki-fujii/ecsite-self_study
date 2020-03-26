$(function(){
	$('#search_address').on("click",function(){
		AjaxZip3.zip2addr('zipcode','','address','address');
	});
});



$(function(){
	$('#search_address_again').on("click",function(){
		AjaxZip3.zip2addr('destinationZipcode','','destinationAddress','destinationAddress');
	});
});