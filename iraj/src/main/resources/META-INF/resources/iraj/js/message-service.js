'use strict';

angular.module('iraj-message-service', [])
	.factory('irajMessageService', function() {
		var irajMessageService = {};
		
		irajMessageService.displayFieldMessages = function (fieldMessages) {
			// fieldMessages = [{id: 'fieldId', message: 'messageToDisplay', type: 'warning' | 'error' | 'info' | 'success'}, ...]
			for (var i = 0; i < fieldMessages.length; i++) {
				irajMessageService.displayFieldMessage(fieldMessages[i]);
			}
		};
		
		irajMessageService.displayFieldMessage = function(fieldMessage) {
			var inputEl = jQuery('#'+fieldMessage.id); // search the input field
			// search the help-inline for that input
			var helpEl = inputEl.next('.help-inline');
			if (!helpEl.is('.help-inline')) {
				// the help inline doesn't exist, let's create it
				helpEl = inputEl.after('<div class="help-inline"/>').next('.help-inline');
			}
			// append the message to it
			helpEl.append(fieldMessage.message);
			// now search the parent "control-group" in order to change the type of warning
			inputEl.parents('.control-group').eq(0)
				.removeClass("warning error info success") // first remove previous messages type
				.addClass(fieldMessage.type);	// and add this type
		}
		
		irajMessageService.clearFieldMessages = function(idPrefix) {
			jQuery("[id^='"+idPrefix+"'] ~ .help-inline") // foreach help-inline which has a matching input as previous sibling
				.empty() // clear all text inside
				.parents('.control-group') // search the control-group
				.removeClass("warning error info success"); // and remove the type of message
		};
		
		return irajMessageService;
	})
;
