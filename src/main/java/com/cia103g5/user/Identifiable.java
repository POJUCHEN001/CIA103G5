package com.cia103g5.user;

/**##########################
 #                          #
 #  身份繼承（控制 jwt 參數    #
 #                          #
 ##########################*/

public abstract class Identifiable {
    public abstract String getId();
    public abstract UserIdentityEnum getIdentity();
}
