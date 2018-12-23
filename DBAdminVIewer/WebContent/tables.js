$(function(){
	
$('span.table_toggle').click(function(){
	var l= $(this).attr("tname");
	$('#' + l).toggle();
	if($("#" + l).is(":visible")){
		$(this).html("-" + l.toUpperCase());
	}
	else{
		$(this).html("+" + l.toUpperCase());
	}
});
});