package de.helfenkannjeder.helfomat.service;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Valentin Zickner
 */
@Service
public class ListCache<T> {
    private List<T> list = new ArrayList<>();

    public void add(T template) {
        list.add(template);
    }

    public List<T> getAll() {
        return Collections.unmodifiableList(list);
    }
}
