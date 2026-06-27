package com.scada.entities;

import java.util.List;

import lombok.Data;
@Data
public class FaultValidationResponse {

    private boolean success;
    private String message;
    private List<String> correctSequence;
    private List<String> userSequence;   // ✅ NEW FIELD

    public FaultValidationResponse() {}

    public FaultValidationResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public FaultValidationResponse(boolean success, String message,
                                   List<String> correctSequence,
                                   List<String> userSequence) {
        this.success = success;
        this.message = message;
        this.correctSequence = correctSequence;
        this.userSequence = userSequence;
    }

    // ------- Getters & Setters ---------

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<String> getCorrectSequence() {
        return correctSequence;
    }

    public void setCorrectSequence(List<String> correctSequence) {
        this.correctSequence = correctSequence;
    }

    public List<String> getUserSequence() {
        return userSequence;
    }

    public void setUserSequence(List<String> userSequence) {
        this.userSequence = userSequence;
    }
}
