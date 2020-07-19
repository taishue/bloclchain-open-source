package com.coinsthai.bch.address;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LegacyToCashAddressTest {

    @Test
    public void testLegacyToCachP2PKH() {
        String legacy_address = "1LjxNPv8mb7f2Y4LKdZ3mbJDGHZk5qsiWD";
        String cash_address = "bitcoincash:qrvg59r5dytgm03gqrmqsujdpljca57kuy7k50gn83";

        assertEquals(cash_address, AddressConverter.toCashAddress(legacy_address));
    }

    @Test
    public void testLegacyToCashP2SH() {
        String legacy_address = "3LYrwnQtLT758CsE2YChLsUsDjSLZpzXcn";
        String cash_address = "bitcoincash:pr8dlyf4sgpgmdh7nggwzfps732gtfw3kumq7hed27";

        assertEquals(cash_address, AddressConverter.toCashAddress(legacy_address));
    }
}
