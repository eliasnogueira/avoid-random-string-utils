/*
 * MIT License
 *
 * Copyright (c) 2021 Elias Nogueira
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.eliasnogueira.data;

import com.eliasnogueira.model.CustomerData;
import com.github.javafaker.Faker;
import lombok.extern.java.Log;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Date;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@Log
class BasicExampleTest {

    private static Validator validator;

    @BeforeAll
    static void beforeAll() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    /*
     * This test shows the data generation using:
     *   - RandomUtils to generate an int value
     *   - RandomStringUtils to generate the String values
     *   - Fixed date
     */
    @Test
    @DisplayName("Data validations using RandomStringUtils")
    void randomStringUtils() {
        CustomerData customerData = CustomerData.builder().
                id(Integer.parseInt(RandomStringUtils.randomNumeric(10))).
                name(RandomStringUtils.randomAlphanumeric(50)).
                profession(RandomStringUtils.randomAlphanumeric(30)).
                accountNumber(RandomStringUtils.randomAlphanumeric(18)).
                address(RandomStringUtils.randomAlphanumeric(50)).
                phoneNumber(RandomStringUtils.randomAlphanumeric(14)).
                birthday(new Date()).
                build();

        log.info(customerData.toString());

        assertThat(testValidations(customerData)).isEmpty();
    }

    /*
     * This test shows the data generation using:
     *   - JavaFaker to generate all the necessary data
     */
    @Test
    @DisplayName("Data validations using faker library")
    void faker() {
        Faker faker = new Faker();

        CustomerData customerData = CustomerData.builder().
                id((int) faker.number().randomNumber()).
                name(faker.name().name()).
                profession(faker.company().profession()).
                accountNumber(faker.finance().iban("NL")).
                address(faker.address().streetAddress()).
                phoneNumber(faker.phoneNumber().cellPhone()).
                birthday(faker.date().birthday(18, 90)).
                build();

        log.info(customerData.toString());

        assertThat(testValidations(customerData)).isEmpty();
    }

    private Set<ConstraintViolation<CustomerData>> testValidations(CustomerData customerData) {
        Set<ConstraintViolation<CustomerData>> violations = validator.validate(customerData);
        violations.stream().map(ConstraintViolation::getMessage).forEach(log::severe);

        return violations;
    }
}
