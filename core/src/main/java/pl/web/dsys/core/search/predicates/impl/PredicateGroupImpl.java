package pl.web.dsys.core.search.predicates.impl;


import pl.web.dsys.core.search.predicates.PredicateGroup;
import pl.web.dsys.core.search.predicates.PredicateOption;

import java.util.List;

public class PredicateGroupImpl implements PredicateGroup {
    private final String title;
    private final String name;
    private final List<PredicateOption> predicateOptions;

    public PredicateGroupImpl(String title, String name, List<PredicateOption> predicateOptions) {
        this.title = title;
        this.name = name;
        this.predicateOptions = predicateOptions;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public List<PredicateOption> getOptions() {
        return predicateOptions;
    }
}
