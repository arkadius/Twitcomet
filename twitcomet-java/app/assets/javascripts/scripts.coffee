# Provide cache for templates and other things in the future
cache = 
	templates: {}

# ---------- Generals functions ----------------- 

# Initialize the page
init = () ->
	moment.lang 'fr';

# Show global success message		
window.showSuccess = (message) ->
	showMessage message, 'success'

# Show global error message
window.showError = (message) ->
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
			html = loadTemplate('messageTemplate')({ messages: data })
			$("#timeline").prepend(html)
			highlightNewMessages()
		error: ->
			showError "Cannot retreive new messages"

# Post new message 
sendMessage = (msg) ->
	jsRoutes.controllers.Wall.create().ajax 
			dataType: 'json'
			data:
				message: msg
			success: ->
				loadNewMessages()		# TODO : use callback here !!
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
		sendMessage($('#twit-zone').val())
		$('#post-zone textarea').val('');
					
		