package com.gomz.festivallineuptracker.service;

import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FestivalFitCalculatorTest {

    @Test
    void resolveRoot_returnsParentForChildAndItselfForRoot() {
        Map<Integer, Integer> childToParent = Map.of(2, 1, 3, 1, 4, 1);

        assertEquals(1, FestivalFitCalculator.resolveRoot(2, childToParent));
        assertEquals(1, FestivalFitCalculator.resolveRoot(3, childToParent));
        assertEquals(1, FestivalFitCalculator.resolveRoot(1, childToParent));
    }

    @Test
    void collapseToRoots_collapsesChildrenOfTheSameParent() {
        Map<Integer, Integer> childToParent = Map.of(21, 1, 22, 1);

        assertEquals(Set.of(1), FestivalFitCalculator.collapseToRoots(Set.of(21, 22), childToParent));
    }

    @Test
    void fitPercent_roundsOneThirdTo33() {
        assertEquals(33, FestivalFitCalculator.fitPercent(Set.of(1, 2, 3), Set.of(1)));
    }

    @Test
    void fitPercent_matchesAllParentsAt100() {
        assertEquals(100, FestivalFitCalculator.fitPercent(Set.of(1, 2, 3), Set.of(1, 2, 3, 9)));
    }
}
