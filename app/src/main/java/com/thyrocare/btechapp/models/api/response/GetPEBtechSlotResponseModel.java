package com.thyrocare.btechapp.models.api.response;

public class GetPEBtechSlotResponseModel {

    /**
     * Id : 3
     * SlotMasterId : 8
     * Slot : 05:30 - 06:30
     */

    private Integer Id;
    private String SlotMasterId;
    private String Slot;
    private String NewSlot;

    public Integer getId() {
        return Id;
    }

    public void setId(Integer Id) {
        this.Id = Id;
    }

    public String getSlotMasterId() {
        return SlotMasterId;
    }

    public void setSlotMasterId(String SlotMasterId) {
        this.SlotMasterId = SlotMasterId;
    }

    public String getSlot() {
        return Slot;
    }

    public void setSlot(String Slot) {
        this.Slot = Slot;
    }

    public String getNewSlot() {
        return NewSlot;
    }

    public void setNewSlot(String newSlot) {
        NewSlot = newSlot;
    }
}
