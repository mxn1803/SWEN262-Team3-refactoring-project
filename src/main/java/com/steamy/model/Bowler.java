package com.steamy.model;/*
 * Bowler.java
 *
 * Version:
 *     $Id$
 *
 * Revisions:
 *     $Log: Bowler.java,v $
 *     Revision 1.3  2003/01/15 02:57:40  ???
 *     Added accessors and and equals() method
 *
 *     Revision 1.2  2003/01/12 22:23:32  ???
 *     *** empty log message ***
 *
 *     Revision 1.1  2003/01/12 19:09:12  ???
 *     Adding Party, Lane, Bowler, and Alley.
 *
 */

/**
 * Class that holds all bowler info
 */
public class Bowler {
    private final String FULL_NAME;
    private final String NICKNAME;
    private final String EMAIL;

    public Bowler(String nick, String full, String mail) {
        NICKNAME = nick;
        FULL_NAME = full;
        EMAIL = mail;
    }

    public String getNickName() { return NICKNAME; }

    public String getFullName() {
        return FULL_NAME;
    }

    public String getNick() {
        return NICKNAME;
    }

    public String getEmail() {
        return EMAIL;
    }
}