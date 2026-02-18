package com.authserver.authserver.base;

public interface BaseConvertorInterface<Entry, Model> {
    public abstract Model toModel(Entry entry, Model existing);

    public abstract Entry toEntry(Model model);
}
