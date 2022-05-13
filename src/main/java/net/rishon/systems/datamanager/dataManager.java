package net.rishon.systems.datamanager;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class dataManager {

    public ArrayList<String> mutedServers = new ArrayList<>();
    public List<String> toggled_messages = new ArrayList<>();

    public dataManager() {
    }

}
