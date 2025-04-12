package com.bitvavo;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.Arguments;



import static org.junit.jupiter.api.Assertions.assertEquals;

public class IntegrationTest extends AbstractIntegrationTest{
    @ParameterizedTest(name = "{index}")
    @MethodSource("provideTestCases")
    public void testMain(String input, String expectedOutput){
        ByteArrayInputStream testInput = new ByteArrayInputStream(input.getBytes());
        System.setIn(testInput);
        Application.main(new String[0]);

        assertEquals(expectedOutput, outContent.toString());
    }

    private static Stream<Arguments> provideTestCases() throws FileNotFoundException {
        return AbstractIntegrationTest.loadTestCases("src/test/resources/test-cases");
    }
}
