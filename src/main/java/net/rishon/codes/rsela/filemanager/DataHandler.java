package net.rishon.codes.rsela.filemanager;

import net.rishon.codes.rsela.utils.Lists;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DataHandler {

    private UUID uuid;
    private Map<UUID, Boolean> tpm = new HashMap<>();

    public DataHandler(UUID uuid) {
        this.uuid = uuid;
    }

    public void setTPM(Boolean value) {
        this.tpm.put(uuid, value);
        if (value && !Lists.toggled_messages.contains(uuid.toString())) {
            Lists.toggled_messages.add(uuid.toString());
        }
    }

    public Boolean getTPM() {
        return this.tpm.get(uuid);
    }

    public UUID getUUID() {
        return this.uuid;
    }


}
