package net.rishon.systems.filemanager;

import net.rishon.systems.Main;

import java.util.UUID;

public class DataHandler {

    private final Main instance;
    private final UUID uuid;

    public DataHandler(Main instance, UUID uuid) {
        this.instance = instance;
        this.uuid = uuid;
    }

    public boolean getTPM() {
        return this.instance.getDataManager().toggled_messages.contains(getUUID().toString());
    }

    public void setTPM(boolean value) {
        if (value && !getTPM()) {
            this.instance.getDataManager().toggled_messages.add(getUUID().toString());
        } else if (!value && getTPM()) {
            this.instance.getDataManager().toggled_messages.remove(getUUID().toString());
        }
    }

    public UUID getUUID() {
        return this.uuid;
    }


}
