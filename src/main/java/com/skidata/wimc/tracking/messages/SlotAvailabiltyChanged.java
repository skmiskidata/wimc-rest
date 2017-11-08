package com.skidata.wimc.tracking.messages;

import com.skidata.wimc.tracking.Slot;

public class SlotAvailabiltyChanged extends Message {
    Slot slot;

    public SlotAvailabiltyChanged(Slot slot) {
        this.slot = slot;
    }

    @Override
    public String toString() {
        return "SlotAvailabiltyChanged{" +
                "slot=" + slot.getId() +
                '}';
    }
}
