package com.wallet.walletservice.helper;

import org.apache.commons.text.RandomStringGenerator;

public final class WalletNameGenerator {
    private static final String PREFIX = "SC-";
    private static final int LENGTH = 8;
    private static final RandomStringGenerator GENERATOR = new RandomStringGenerator.Builder()
            .withinRange('0', 'Z')
            .filteredBy(Character::isLetterOrDigit)
            .build();

    private WalletNameGenerator() {
        throw new UnsupportedOperationException();
    }

    public static String generate() {
        return PREFIX + GENERATOR.generate(LENGTH).toUpperCase();
    }

}
