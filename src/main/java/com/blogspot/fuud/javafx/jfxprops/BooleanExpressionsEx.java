package com.blogspot.fuud.javafx.jfxprops;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import java.util.ArrayList;
import java.util.List;

public final class BooleanExpressionsEx {
    public static ThenClause If(ObservableValue<Boolean> observableValue) {
        return new ThenClause(observableValue);
    }
}

class ThenClause {
    private final ObservableValue<Boolean> observableValue;

    ThenClause(ObservableValue<Boolean> observableValue) {
        this.observableValue = observableValue;
    }

    <T> ElseClause<T> Then(T value) {
        final ArrayList<ConditionValue<T>> conditionals = new ArrayList<>();
        conditionals.add(new ConditionValue<>(observableValue, value));
        return new ElseClause<>(conditionals);
    }
}

class GenericThenClause<T> {
    private final List<ConditionValue<T>> conditionValues;
    private final ObservableValue<Boolean> condition;

    GenericThenClause(List<ConditionValue<T>> conditionValues, ObservableValue<Boolean> condition) {
        this.conditionValues = conditionValues;
        this.condition = condition;
    }

    ElseClause<T> Then(T value) {
        conditionValues.add(new ConditionValue<>(condition, value));
        return new ElseClause<>(conditionValues);
    }
}

class ElseClause<T> {
    private final List<ConditionValue<T>> conditionValues;

    ElseClause(List<ConditionValue<T>> conditionValues) {
        this.conditionValues = conditionValues;
    }

    GenericThenClause<T> ElseIf(ObservableValue<Boolean> observableValue) {
        return new GenericThenClause<>(conditionValues, observableValue);
    }

    ObservableValue<T> Else(final T value) {
        return new ObservableValueBase<>(conditionValues, value);
    }
}

class ConditionValue<T> {
    ConditionValue(ObservableValue<Boolean> condition, T value) {
        this.condition = condition;
        this.value = value;
    }

    ObservableValue<Boolean> condition;
    T value;
}

class ObservableValueBase<T> extends javafx.beans.value.ObservableValueBase<T> {
    private final List<ConditionValue<T>> conditionValues;
    private final T elseValue;

    public ObservableValueBase(List<ConditionValue<T>> conditionValues, T elseValue) {
        this.conditionValues = conditionValues;
        this.elseValue = elseValue;
        for (ConditionValue<T> conditionValue : conditionValues) {
            conditionValue.condition.addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean aBoolean2) {
                    ObservableValueBase.this.fireValueChangedEvent();
                }
            });
        }
    }

    @Override
    public T getValue() {
        for (ConditionValue<T> conditionValue : conditionValues) {
            if (conditionValue.condition.getValue()) {
                return conditionValue.value;
            }
        }
        return elseValue;
    }
}
