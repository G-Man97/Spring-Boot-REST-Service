package com.gmalykhin.spring.boot.spring_boot_rest_new.util;

import com.gmalykhin.spring.boot.spring_boot_rest_new.exception_handling.IncorrectFieldData;
import org.junit.jupiter.api.Test;

import static java.time.LocalDate.parse;
import static org.junit.jupiter.api.Assertions.*;

class UtilsTest {

    @Test
    void initCap_Should_True_When_Returns_Equals_To_Expected_String() {

        assertEquals("Some_string", Utils.initCap("some_string"));
        assertEquals("Another_string", Utils.initCap("Another_string"));
    }

    @Test
    void initCap_Should_True_When_Returns_Not_Equals_To_Expected_String() {

        assertNotEquals("third_string", Utils.initCap("third_string"));
    }



    @Test
    void checkBirthday_Should_Throw_Exception_If_More_Then_60() {
        assertThrowsExactly(IncorrectFieldData.class, () -> Utils.checkBirthday(parse("1950-06-12")));
    }

    @Test
    void checkBirthday_Should_Throw_Exception_If_Less_Then_18() {
        assertThrowsExactly(IncorrectFieldData.class, () -> Utils.checkBirthday(parse("2006-08-18")));
    }


}