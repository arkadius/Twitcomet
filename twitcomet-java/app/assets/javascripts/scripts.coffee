# Provide cache for templates and other things in the future
cache = 
	templates: {}

# ---------- Generals functions ----------------- 

# Initialize the page
init = () ->
	moment.lang 'fr';

# Show global success message		
showSuccess = (message) ->
	showMessage message, 'success'

# Show global error message
showError = (message) ->
	showMessage message, 'error'
	
# Helper to show global message
showMessage = (message, type) ->
	el = $('<div />').addClass("alert hide alert-#{type}").html(message).prependTo('#main').fadeIn(500)
	setTimeout ->
		el.slideUp(200)
	, 3000

# Load, compile and cache a handlebars template			
loadTemplate = (templateId) ->
	cache.templates[templateId] ?= Handlebars.compile($id(templateId).html())

# Quick helper for jQuery
$id = (id) -> $('#'+id)

# ---------------- Tweet functions -------------- 
	
# Load all new messages not already loaded and show these
loadNewMessages = () ->
	lastId = getLastIdMessage()
	jsRoutes.controllers.Wall.fetchMessages(lastId).ajax
		dataType: 'json'
		success: (data) ->
			receiveNewMessage data
		error: ->
			showError "Cannot retreive new messages"

# Post new message 
sendMessage = (msg, success) ->
	jsRoutes.controllers.Wall.create().ajax 
			dataType: 'json'
			data:
				message: msg
			success: ->
				success?()
			error: ->
				showError "Your tweet is so null..."

# Get the ID of the lastest message loaded
getLastIdMessage = () ->
	$(".message:first").data('id')

# Highlight the new messages in yellow
highlightNewMessages = () ->
	$('#timeline .new')
		.removeClass('new')
		.css
			backgroundColor: "#FFFFAA"
		.animate
			backgroundColor: "#F5F5F5"
		, 1000
		
# Display new messages
receiveNewMessage = (mess) -> 
	html = loadTemplate('messageTemplate')({ messages: mess })
	$("#timeline").prepend(html)
	highlightNewMessages()
	
# --------------- When Dom ready -------------
$ ->
	
	init()
	
	# Event : Open Post box
	$('.post-event').on 'click', (e) ->
		e.preventDefault();
		$("#post-zone:hidden").slideDown()
	
	# Event : Close Post box
	$('#post-zone .close').on 'click', (e) -> 
		e.preventDefault()
		$('#post-zone:visible').slideUp()
	
	# Event : Send tweet
	$('#twitit').on 'click', (e) ->
		e.preventDefault()
		sendMessage $('#twit-zone').val(), ->
			$('#post-zone textarea').val('');
			
	# Event : Send message to debug app
	$('.send-msg-debug ').on 'click', (e) ->
		e.preventDefault()
		nb = $(this).data('nb')
		sendMessage "Debug #{i}" for i in [0..nb]


# --------------- Expose functions to global scope --------------
window.receiveNewMessage = receiveNewMessage
window.showError = showError
window.showSuccess = showSuccess
		