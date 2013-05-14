package com.iorga.iraj.message;

import java.util.List;

public class Messages {
	private final List<FieldMessage> fieldMessages;
	private final List<Message> messages;


	public Messages(final List<FieldMessage> fieldMessages, final List<Message> messages) {
		this.fieldMessages = fieldMessages;
		this.messages = messages;
	}

	public String getFirstMessage() {
		if (messages.size() > 0) {
			return messages.get(0).getMessage();
		} else if (fieldMessages.size() > 0) {
			return fieldMessages.get(0).getMessage();
		} else {
			return null;
		}
	}


	/// Getters & Setters ///
	////////////////////////
	public List<FieldMessage> getFieldMessages() {
		return fieldMessages;
	}
	public List<Message> getMessages() {
		return messages;
	}

}
