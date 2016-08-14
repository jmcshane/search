$('#searchSubmit').click(function() {
	var input = $('#searchInput').val();
	var type = $('#inputType').val()

	$.get('/search/' + type, {input: input}, function(data) {
			$("#result").val(data);
		}, 'text');
});