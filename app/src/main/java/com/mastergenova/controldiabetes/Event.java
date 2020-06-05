package com.mastergenova.controldiabetes;

import com.google.gson.annotations.SerializedName;

public class Event {

    @SerializedName("event")
    private Event event;

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

}
