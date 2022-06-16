package com.example.payload;


public class MessageResponse {
    /**
     * message.
     */
    private String message;

    /**
     * MessageResponse.
     * @param themessage
     */
    public MessageResponse(final String themessage) {
        this.message = themessage;
    }

    /**
     * getMessage.
     * @return message
     */
    public String getMessage() {
        return message;
    }

    /**
     * setMessage.
     * @param amessage
     */
    public void setMessage(final String amessage) {
        this.message = amessage;
    }
}
