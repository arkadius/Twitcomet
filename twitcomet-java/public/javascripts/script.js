$(function() {
	$(".post-event").on('click', function() {
		$("#post-zone:hidden").slideDown();
	});
	
	$(".close").on('click', function() {
		$("#post-zone:visible").slideUp();
	})
	
	$('.date').each(function() {
		var depuis = moment($(this).text(), "DD/MM/YYYY HH:mm:ss").fromNow();
		$(this).append(' ('+depuis+')');
	});
});