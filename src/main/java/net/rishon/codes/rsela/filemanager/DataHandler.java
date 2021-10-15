package net.rishon.codes.rsela.filemanager;

import net.rishon.codes.rsela.utils.Lists;

import java.util.UUID;

public class DataHandler {

    private UUID uuid;

    public DataHandler(UUID uuid) {
        this.uuid = uuid;
    }

    public void setTPM(boolean value) {
        if(value && !getTPM()) {
            Lists.toggled_messages.add(getUUID().toString());
        } else if(!value && getTPM()) {
            Lists.toggled_messages.remove(getUUID().toString());
        }
    }

    public boolean getTPM() {
        return Lists.toggled_messages.contains(getUUID().toString());
    }

    public UUID getUUID() {
        return this.uuid;
    }


}
