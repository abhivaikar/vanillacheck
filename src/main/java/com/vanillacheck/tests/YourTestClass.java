package com.vanillacheck.tests;

import com.vanillacheck.annotations.AutoTest;
import com.vanillacheck.annotations.BeforeAutoTest;
import com.vanillacheck.annotations.AfterAutoTest;
import org.assertj.core.api.Assertions;

public class YourTestClass {

    @BeforeAutoTest
    public void setup() {
        System.out.println("Before each test");
    }

    @AutoTest
    public void testExamplePass() {
        Assertions.assertThat(1 + 1).isEqualTo(2);
    }

    @AutoTest
    public void testExampleFail() {
        Assertions.assertThat(1 + 1).isEqualTo(3);
    }

    @AfterAutoTest
    public void teardown() {
        System.out.println("After each test");
    }
}
