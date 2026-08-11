package io.github.asmolenkov.tennismatchscoreboard.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageInfo {
    private static final int WINDOW_SIZE = 2;
    private int currentPage;
    private int pageSize;
    private long totalItems;
    private int totalPages;


    public boolean isHasPrevious() {
        return currentPage > 1; }

    public boolean isHasNext() {
        return currentPage < totalPages; }

    public int getPreviousPage() {
        return currentPage - 1; }

    public int getNextPage() {
        return currentPage + 1; }

    public List<Integer> getVisiblePageNumbers() {
        if (totalPages <= 0) {
            return Collections.emptyList();
        }

        List<Integer> pages = new ArrayList<>();
        int start = Math.max(1, currentPage - WINDOW_SIZE);
        int end = Math.min(totalPages, currentPage + WINDOW_SIZE);

        for (int i = start; i <= end; i++) {
            pages.add(i);
        }

        return pages;
    }


}
