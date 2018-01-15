package com.albom.iion.isr.processing;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;

import com.albom.iion.isr.data.Point;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CoherentNoiseFinderTest {

    private CoherentNoiseFinder coherentNoiseFinder;

    @ParameterizedTest(name = "Should {0}.")
    @MethodSource("parametersForShouldFindCoherentNoiseCorrectly")
    void findCoherentNoiseCorrectly(
            String testCaseName,
            List<Point> inputPoints,
            List<Boolean> expectedResult)
    {
        //given
        coherentNoiseFinder = new CoherentNoiseFinder();

        //when
        List<Boolean> actualResult = coherentNoiseFinder.find(inputPoints);

        //then
        assertIterableEquals(expectedResult, actualResult);
    }

    private Stream<Arguments> parametersForShouldFindCoherentNoiseCorrectly()
    {
        return Stream.of(
                Arguments.of(
                        " return false for all points",
                        preparePoints(),
                        prepareExpectedResult()
                ),
                Arguments.of(
                        " return empty list if input is empty list",
                        Collections.emptyList(),
                        Collections.emptyList()
                )
        );
    }

    private List<Point> preparePoints()
    {
        List<Point> points = new ArrayList<>();
        points.add(preparePoint(LocalDateTime.now(), 1, 2, 5.5));
        points.add(preparePoint(LocalDateTime.now(), 2, 3, 7.4));

        return points;
    }

    private List<Boolean> prepareExpectedResult()
    {
        List<Boolean> result = new ArrayList<>();
        Collections.fill(result, false);

        return result;
    }

    private Point preparePoint(LocalDateTime dateTime, int alt, int lag, double value)
    {
        return new Point(dateTime, alt, lag, value);
    }

}
