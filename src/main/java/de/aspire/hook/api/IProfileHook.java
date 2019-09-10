package de.aspire.hook.api;

import de.aspire.spigot.api.IProfile;

public interface IProfileHook {

    /**
     * Set profile name
     *
     * @param name set profile name
     */

    void setName(String name);

    /**
     * Get profile name
     *
     * @return profile name
     */

    String getName();


    /**
     * Get spigot profile
     *
     * @return spigot profile
     */

    IProfile getSpigotProfile();

    /**
     * Apply/Save changes to the spigot
     */

    void applyChanges();

}
