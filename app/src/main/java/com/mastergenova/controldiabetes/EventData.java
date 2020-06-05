package com.mastergenova.controldiabetes;

import com.google.gson.annotations.SerializedName;

public class EventData {

    @SerializedName(("eventType"))
    private String eventType;

    @SerializedName("value")
    private Double value;

    @SerializedName("timestamp")
    private String timestamp;

    @SerializedName("metadata")
    private String metadata;

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getMetadata() {
        return metadata;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }

}
