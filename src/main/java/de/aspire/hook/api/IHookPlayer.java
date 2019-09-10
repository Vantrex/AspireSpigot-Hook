package de.aspire.hook.api;

import de.aspire.hook.profile.knockback.KnockbackProfileHook;

public interface IHookPlayer {

    /**
     * Set player KnockbackProfile
     *
     * @param knockbackProfile set the knockback by knowing the name of the profile. To reset the profile set the value to null
     */

    void setKnockbackProfile(String knockbackProfile);

    /**
     * Set player KnockbackProfile
     *
     * @param knockbackProfile set the knockback with the KnockbackProfileHook. To reset the profile set the value to null
     */

    void setKnockbackProfile(KnockbackProfileHook knockbackProfile);

    /**
     * Get the current KnockbackProfile of the player
     *
     * @return current KnockbackProfile of player.
     */

    KnockbackProfileHook getKnockbackProfile();

    /**
     * Set the current PotionProfile of the player
     *
     * @param potionProfile null = default profile
     */

    void setPotionProfile(String potionProfile);

    /**
     *
     * @return PotionProfile as string.
     */

    String getPotionProfile();


}
