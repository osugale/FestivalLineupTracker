package com.gomz.festivallineuptracker.service;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public final class FestivalFitCalculator {

    private FestivalFitCalculator() {
    }




    public static int resolveRoot(int genreId, Map<Integer, Integer> childToParent) {
        return childToParent.getOrDefault(genreId, genreId);
    }




    public static Set<Integer> collapseToRoots(Iterable<Integer> genreIds, Map<Integer, Integer> childToParent) {
        Set<Integer> roots = new HashSet<>();
        if (genreIds == null) {
            return roots;
        }
        for (Integer genreId : genreIds) {
            if (genreId != null) {
                roots.add(resolveRoot(genreId, childToParent));
            }
        }
        return roots;
    }






    public static int fitPercent(Set<Integer> userParentIds, Set<Integer> festivalRootIds) {
        if (userParentIds == null || userParentIds.isEmpty()) {
            return 0;
        }
        int matched = 0;
        for (Integer parentId : userParentIds) {
            if (festivalRootIds.contains(parentId)) {
                matched++;
            }
        }
        return (int) Math.round(100.0 * matched / userParentIds.size());
    }



}
