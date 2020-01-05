package me.dylan.wands.gui.lib.data;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class GUIDataStore {
    private Map<String, Object> localData;

    public GUIDataStore() {}

    public <T> void set(String path, T t) {
        getMap().put(path, t);
    }

    public <T> T replace(String path, T newValue) {
        return (T) getMap().put(path, newValue);
    }

    public <T> void setIfAbsent(String path, Supplier<T> def) {
        getMap().computeIfAbsent(path, k -> def.get());
    }

    public <T> T replaceIfAbsent(String path, Supplier<T> def) {
        return (T) getMap().computeIfAbsent(path, k -> def.get());
    }

    public <T> T get(String path) {
        return (T) getRaw(path);
    }

    public <T> T getOrDefault(String path, T def) {
        T t = get(path);
        return (t == null) ? def : t;
    }

    public Object getRaw(String path) {
        return (localData == null) ? null : localData.get(path);
    }

    public boolean has(String path) {
        return getRaw(path) != null;
    }

    public <T> T remove(String path) {
        return (T) removeRaw(path);
    }

    public Object removeRaw(String path) {
        return (localData == null) ? null : localData.remove(path);
    }

    private Map<String, Object> getMap() {
        if (localData == null)
            localData = new HashMap<>();

        return localData;
    }

}
