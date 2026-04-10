package com.authserver.authserver.base;

public interface ConvertorInterface<Entry, Model> {
    public abstract Model toModel(Entry entry, Model existing);

    public abstract Entry toEntry(Model model);
}
