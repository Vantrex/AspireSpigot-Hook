package de.aspire.hook.api;

import de.aspire.spigot.api.IProfile;
import org.apache.commons.lang.IllegalClassException;

public interface IHookManager {

    /**
     * Update profile
     *
     * @param iProfile iProfile which should be getting updated
     */

    void update(IProfileHook iProfile);

    /**
     * Update all profiles
     *
     */

    void updateAll();

    /**
     * Get profile from name
     *
     * @param name from the profile
     * @return null if profile is not valid
     */

    IProfileHook getProfile(String name);

    /**
     * Add a new profile
     *
     * @param name name of the profile
     * @param iProfileHook new instance of the profile
     */

    void addProfile(String name, IProfileHook iProfileHook);

    /**
     * Get IProfileHook from the spigot profile
     *
     * @param iProfile spigot profile
     * @return null if profile is not valid
     */

    IProfileHook getFromSpigotProfile(IProfile iProfile);

    /**
     *
     * @param iProfileHook hook profile
     * @return active spigot profile if hook profile is not valid
     */

    IProfile getFromHookProfile(IProfileHook iProfileHook);


    /**
     * Apply/Save profile
     *
     * @param iProfileHook profile which should be applied/saved
     * @exception NullPointerException if the profile is not valid/its not a spigot profile
     */

    void apply(IProfileHook iProfileHook);

    /**
     * Save all profiles
     */

    void applyAll();

    /**
     * Request if profile is valid
     *
     * @param name name of the profile
     * @return false if the profile is not valid true if it is valid
     */

    boolean isValidProfile(String name);

    /**
     * Get default spigot profile (same as getHookDefault())
     *
     * @return valid IProfile
     */

    IProfile getSpigotDefault();

    /**
     * Get default hook profile (same as getHookDefault())
     *
     * @return valid IProfileHook
     */

    IProfileHook getHookDefault();

    /**
     * Set current active profile by IProfileHook
     *
     * @param iProfileHook current profile
     */

    void setDefault(final IProfileHook iProfileHook);

    /**
     * Set current active profile by string
     *
     * @param name name of the profiled
     */

    void setDefault(final String name);



}
