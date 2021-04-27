package com.steamy;

import com.steamy.specialists.ControlSpecialist;

public class Main {

    public static void main(String[] args) {

        final int NUM_LANES = 3;
        final int MAX_PATRONS_PER_PARTY = 5;

        ControlSpecialist controlSpecialist = new ControlSpecialist(NUM_LANES, MAX_PATRONS_PER_PARTY);

    }
}
