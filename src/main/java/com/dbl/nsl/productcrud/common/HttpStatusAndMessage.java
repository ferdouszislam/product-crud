package com.dbl.nsl.productcrud.common;

import org.springframework.http.HttpStatus;

public class HttpStatusAndMessage {

	private HttpStatus status;

	private String message;

	public HttpStatusAndMessage() {
		super();
	}

	public HttpStatusAndMessage(HttpStatus status, String message) {
		super();
		this.status = status;
		this.message = message;
	}

	public HttpStatus getStatus() {
		return status;
	}

	public void setStatus(HttpStatus status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "HttpStatusAndMessage [status=" + status + ", message=" + message
				+ "]";
	}

}
