package com.wallet.walletservice.helper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class WalletNameGeneratorTest {

    @Test
    void given_whenConstructorIsPrivate_thenThrowIllegalAccessException() throws NoSuchMethodException {
        // given - precondition or setup
        Constructor<WalletNameGenerator> constructor = WalletNameGenerator.class.getDeclaredConstructor();

        // when - action or the behaviour that we are going test
        Executable actual = constructor::newInstance;
        Class<IllegalAccessException> expectedType = IllegalAccessException.class;

        // then - verify the output
        assertTrue(Modifier.isPrivate(constructor.getModifiers()));
        assertThrows(expectedType, actual);
    }
}