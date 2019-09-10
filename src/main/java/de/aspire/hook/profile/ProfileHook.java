package de.aspire.hook.profile;

import de.aspire.hook.api.IProfileHook;


public abstract class ProfileHook implements IProfileHook{

    private String name;

    public ProfileHook(String name){
        this.name = name;
    }

    public abstract boolean isValid();

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }


}
