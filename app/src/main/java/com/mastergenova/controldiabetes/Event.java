package com.mastergenova.controldiabetes;

import com.google.gson.annotations.SerializedName;

public class Event {

    @SerializedName("event")
    private EventData event;

    public EventData getEvent() {
        return event;
    }

    public void setEvent(EventData event) {
        this.event = event;
    }

}
