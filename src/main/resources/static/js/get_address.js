$(function(){
	$('#search_address').on("click",function(){
		AjaxZip3.zip2addr('zipcode','','address','address');
	});
	
	$('#search_address2').on("click",function(){
		AjaxZip3.zip2addr('destinationZipcode','','destinationAddress','destinationAddress');
	});
});