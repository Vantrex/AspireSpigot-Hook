package de.aspire.hook.profile.knockback;

import de.aspire.hook.profile.ProfileHook;
import de.aspire.spigot.AspireSpigot;
import de.aspire.spigot.api.IProfile;
import de.aspire.spigot.profiles.knockback.KnockbackProfile;
import org.bukkit.Bukkit;

import java.util.logging.Level;

public class KnockbackProfileHook extends ProfileHook{

    private boolean friction; // whether or not the friction get´s modified
    private boolean sprintReset; // whether or not we reset the sprint
    private boolean airKB; // whether or not we have extra airKB

    private double horizontal; // horizontal kb
    private double vertical; // vertical kb

    private int hitDelay; // hitDelay

    private double airHorizontal; // horizontal air kb / extraHorizontal
    private double airVertical; // vertical air kb / extraVertical


    private double frictionValue; // value of the friction (default 2.0)

    private double verticalLimit; // verticalLimit


    private final KnockbackProfile knockbackProfile;

    public KnockbackProfileHook(String name) {
        super(name);

        /* Get the spigot profile */
        this.knockbackProfile = (KnockbackProfile) getSpigotProfile();
        /* Update values */
        update();



    }

    public void update(){
        /* Set values */
        setName(knockbackProfile.getName());
        setFriction(knockbackProfile.isFriction());
        setAirHorizontal(knockbackProfile.getAirHorizontal());
        setAirKB(knockbackProfile.isAirKB());
        setAirVertical(knockbackProfile.getAirVertical());
        setHitDelay(knockbackProfile.getHitDelay());
        setFrictionValue(knockbackProfile.getFrictionValue());
        setHorizontal(knockbackProfile.getHorizontal());
        setVertical(knockbackProfile.getVertical());
        setVerticalLimit(knockbackProfile.getVerticalLimit());
        setSprintReset(knockbackProfile.isSprintReset());

        Bukkit.getLogger().log(Level.FINE, "[AspireSpigotHook] KnockbackProfile " + getName() + " loaded!");
        /* Successfully loaded */
    }

    @Override
    public void applyChanges() {
        /* Apply values to the spigot profile*/
        knockbackProfile.setName(getName());
        knockbackProfile.setFriction(isFriction());
        knockbackProfile.setAirHorizontal(getAirHorizontal());
        knockbackProfile.setAirKB(isAirKB());
        knockbackProfile.setAirVertical(getAirVertical());
        knockbackProfile.setHitDelay(getHitDelay());
        knockbackProfile.setFrictionValue(getFrictionValue());
        knockbackProfile.setHorizontal(getHorizontal());
        knockbackProfile.setVertical(getVertical());
        knockbackProfile.setVerticalLimit(getVerticalLimit());
        knockbackProfile.setSprintReset(isSprintReset());

        Bukkit.getLogger().log(Level.FINE, "[AspireSpigotHook] Changes to KnockbackProfile " + getName() + " applied!");
        /* Succcessfully applied values to the spigot */
    }

    @Override
    public boolean isValid() {
        return AspireSpigot.getInstance().getKnockbackManager().getProfileMap().containsKey(getName());
    }

    @Override
    public IProfile getSpigotProfile() {
        return AspireSpigot.getInstance().getKnockbackManager().getProfileMap().getOrDefault(getName(),null);
    }

    public double getVerticalLimit() {
        return verticalLimit;
    }

    public int getHitDelay() {
        return hitDelay;
    }

    public double getFrictionValue() {
        return frictionValue;
    }

    public double getAirHorizontal() {
        return airHorizontal;
    }

    public double getAirVertical() {
        return airVertical;
    }

    public double getHorizontal() {
        return horizontal;
    }

    public double getVertical() {
        return vertical;
    }

    public void setVerticalLimit(double verticalLimit) {
        this.verticalLimit = verticalLimit;
    }

    public void setHitDelay(int hitDelay) {
        this.hitDelay = hitDelay;
    }

    public void setFrictionValue(double frictionValue) {
        this.frictionValue = frictionValue;
    }

    public void setFriction(boolean friction) {
        this.friction = friction;
    }

    public void setSprintReset(boolean sprintReset) {
        this.sprintReset = sprintReset;
    }

    public void setAirKB(boolean airKB) {
        this.airKB = airKB;
    }

    public void setHorizontal(double horizontal) {
        this.horizontal = horizontal;
    }

    public void setVertical(double vertical) {
        this.vertical = vertical;
    }

    public void setAirHorizontal(double airHorizontal) {
        this.airHorizontal = airHorizontal;
    }

    public void setAirVertical(double airVertical) {
        this.airVertical = airVertical;
    }

    public boolean isFriction() {
        return friction;
    }

    public boolean isAirKB() {
        return airKB;
    }

    public boolean isSprintReset() {
        return sprintReset;
    }

}
