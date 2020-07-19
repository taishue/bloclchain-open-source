package com.coinsthai.converter;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

/**
 * Created by
 */
public class ConvertedPage<F> extends PageImpl<F> {
    
    @JsonIgnore
    private final Page originalPage;
    
    public ConvertedPage(Page originalPage, List<F> content) {
        super(content);
        this.originalPage = originalPage;
    }
    
    @Override
    public long getTotalElements() {
        return originalPage.getTotalElements();
    }
    
    @Override
    public int getTotalPages() {
        return originalPage.getTotalPages();
    }
    
    @Override
    @JsonIgnore
    public Sort getSort() {
        return originalPage.getSort();
    }
    
    @Override
    @JsonIgnore
    public Pageable previousPageable() {
        return originalPage.previousPageable();
    }
    
    @Override
    @JsonIgnore
    public Pageable nextPageable() {
        return originalPage.nextPageable();
    }
    
    @Override
    public int getNumberOfElements() {
        return originalPage.getNumberOfElements();
    }
    
    @Override
    public int getSize() {
        return originalPage.getSize();
    }
    
    @Override
    public int getNumber() {
        return originalPage.getNumber();
    }
    
}
