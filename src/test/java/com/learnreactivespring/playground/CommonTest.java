package com.learnreactivespring.playground;

import com.vdurmont.semver4j.Semver;
import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CommonTest {

    @Test
    public void getFirstElementInEmptyList() {

        List<String> elements = List.of();

        String firstElement = elements.stream().findFirst().orElse(null);

        Assert.isNull(firstElement, "Result must be null");
    }

    @Test
    public void splitString() {

        List<String> elements = Arrays.asList("/800/829/456////", "/800/");

        String categoriesTree = elements.stream()
                .findFirst()
                .orElse(null);

        Long categoryId = Long.valueOf(categoriesTree.split("/")[1]);

        assertEquals(categoryId, 800);
    }

    @Test
    public void stringFormat() {

        String baseUrl = "https://localhost:80/products/%d";

        String expected = "https://localhost:80/products/123";

        Integer productId = 123;

        String url = String.format(baseUrl, productId);

        assertEquals(expected, url);
    }

    @Test
    public void validateVersionAppGreaterThanCurrentVersion() {

        var version = new Semver("1.2.0");

        var currentVersion = new Semver("1.1.0");

        Boolean isGreaterThan = version.isGreaterThan(currentVersion);

        assertTrue(isGreaterThan);
    }

    @Test
    public void validateVersionAppLowerThanCurrentVersion() {

        var version = new Semver("1.2.0");

        var currentVersion = new Semver("1.2.1");

        Boolean isGreaterThan = version.isGreaterThan(currentVersion);

        assertFalse(isGreaterThan);
    }
}
