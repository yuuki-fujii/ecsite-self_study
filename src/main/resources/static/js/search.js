$(function(){
	
	$('.page-button').on("click",function(){
		var page = $(this).val();
		var name = $('#searchForm [name="name"]').val();
		$('#searchForm [name="page"]').val(page);
		$('#searchForm [name="name"]').val(name);
		$('#searchForm').submit();
	});
	
});
