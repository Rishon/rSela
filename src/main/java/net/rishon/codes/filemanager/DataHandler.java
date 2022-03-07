package net.rishon.codes.filemanager;

import net.rishon.codes.utils.Lists;

import java.util.UUID;

public class DataHandler {

    private UUID uuid;

    public DataHandler(UUID uuid) {
        this.uuid = uuid;
    }

    public boolean getTPM() {
        return Lists.toggled_messages.contains(getUUID().toString());
    }

    public void setTPM(boolean value) {
        if (value && !getTPM()) {
            Lists.toggled_messages.add(getUUID().toString());
        } else if (!value && getTPM()) {
            Lists.toggled_messages.remove(getUUID().toString());
        }
    }

    public UUID getUUID() {
        return this.uuid;
    }


}
