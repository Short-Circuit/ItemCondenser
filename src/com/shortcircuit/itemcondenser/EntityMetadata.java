package com.shortcircuit.itemcondenser;

import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

public class EntityMetadata implements MetadataValue{
    private Plugin plugin;
    private Object value;
    public EntityMetadata(Plugin plugin, Object value){
        this.plugin = plugin;
        this.value = value;
    }
    @Override
    public boolean asBoolean() {
        return (boolean)value;
    }
    @Override
    public byte asByte() {
        return (byte)value;
    }
    @Override
    public double asDouble() {
        return (double)value;
    }
    @Override
    public float asFloat() {
        return (float)value;
    }
    @Override
    public int asInt() {
        return (int)value;
    }
    @Override
    public long asLong() {
        return (long)value;
    }
    @Override
    public short asShort() {
        return (short)value;
    }
    @Override
    public String asString() {
        return (String)value;
    }
    @Override
    public Plugin getOwningPlugin() {
        return plugin;
    }
    @Override
    public void invalidate() {
        return;        
    }
    @Override
    public Object value() {
        return value;
    }
}
