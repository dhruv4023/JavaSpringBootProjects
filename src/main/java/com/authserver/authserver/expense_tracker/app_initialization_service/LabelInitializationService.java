package com.authserver.authserver.expense_tracker.app_initialization_service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.authserver.authserver.base.app_initialization_service.AbstractStartupInitializer;
import com.authserver.authserver.expense_tracker.models.LabelModel;
import com.authserver.authserver.expense_tracker.repositories.LabelRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class LabelInitializationService extends AbstractStartupInitializer {

    private final LabelRepository labelRepository;

    private final List<String> defaultLabels = List.of(
            "Food",
            "Transport",
            "Entertainment",
            "Bills",
            "Miscellaneous");

    @Override
    protected void initialize() {
        if (labelRepository.count() == 0) {
            for (String labelName : defaultLabels) {
                LabelModel label = new LabelModel();
                label.setLabelName(labelName);
                label.setDefaultLabel(true);
                labelRepository.save(label);
            }
        }
    }

    @Override
    public int getOrder() {
        return 10;
    }

}
