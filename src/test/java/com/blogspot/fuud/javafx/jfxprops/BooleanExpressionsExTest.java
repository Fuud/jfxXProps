package com.blogspot.fuud.javafx.jfxprops;

import javafx.beans.InvalidationListener;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import org.junit.Test;
import org.mockito.InOrder;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class BooleanExpressionsExTest {
    @Test
    public void testIfElseTrue() throws Exception {
        SimpleBooleanProperty boolProp = new SimpleBooleanProperty(true);
        final ObservableValue<String> resultedProperty = BooleanExpressionsEx.If(boolProp).Then("foo").Else("bar");
        assertEquals("if bool prop is true then branch should be used", "foo", resultedProperty.getValue());
    }

    @Test
    public void testIfElseFalse() throws Exception {
        SimpleBooleanProperty boolProp = new SimpleBooleanProperty(false);
        final ObservableValue<String> resultedProperty = BooleanExpressionsEx.If(boolProp).Then("foo").Else("bar");
        assertEquals("if bool prop is false then branch should be used", "bar", resultedProperty.getValue());
    }

    @Test
    public void testIfElseChangeListener() throws Exception {
        SimpleBooleanProperty boolProp = new SimpleBooleanProperty(true);
        final ObservableValue<String> resultedProperty = BooleanExpressionsEx.If(boolProp).Then("foo").Else("bar");

        @SuppressWarnings("unchecked")
        final ChangeListener<String> changeListener = mock(ChangeListener.class);

        resultedProperty.addListener(changeListener);

        boolProp.setValue(false);

        verify(changeListener).changed(resultedProperty, "foo", "bar");

    }

    @Test
    public void testIfElseInvalidationListener() throws Exception {
        SimpleBooleanProperty boolProp = new SimpleBooleanProperty(true);
        final ObservableValue<String> resultedProperty = BooleanExpressionsEx.If(boolProp).Then("foo").Else("bar");

        final InvalidationListener invalidationListener = mock(InvalidationListener.class);

        resultedProperty.addListener(invalidationListener);

        boolProp.setValue(false);

        verify(invalidationListener).invalidated(resultedProperty);

    }

    @Test
    public void testIfElseIfElse() throws Exception {
        SimpleBooleanProperty boolPropFirst = new SimpleBooleanProperty(true);
        SimpleBooleanProperty boolPropSecond = new SimpleBooleanProperty(true);
        final ObservableValue<String> resultedProperty =
                BooleanExpressionsEx.
                        If(boolPropFirst).Then("foo").
                        ElseIf(boolPropSecond).Then("bar").
                        Else("neither foo nor bar");

        assertEquals("if first bool prop is true then branch should be used", "foo", resultedProperty.getValue());
        boolPropSecond.setValue(false);
        assertEquals("if first bool prop is true then branch should be used", "foo", resultedProperty.getValue());

        boolPropFirst.setValue(false);
        boolPropSecond.setValue(true);
        assertEquals("if first bool prop is false and second prop is true then elseIf branch should be used", "bar", resultedProperty.getValue());

        boolPropFirst.setValue(false);
        boolPropSecond.setValue(false);
        assertEquals("if first and second bool prop is false then else branch should be used",
                "neither foo nor bar", resultedProperty.getValue());

    }


    @Test
    public void testIfElseIfElseChangeListener() throws Exception {
        SimpleBooleanProperty boolPropFirst = new SimpleBooleanProperty(true);
        SimpleBooleanProperty boolPropSecond = new SimpleBooleanProperty(true);
        final ObservableValue<String> resultedProperty =
                BooleanExpressionsEx.
                        If(boolPropFirst).Then("foo").
                        ElseIf(boolPropSecond).Then("bar").
                        Else("neither foo nor bar");

        @SuppressWarnings("unchecked")
        final ChangeListener<String> changeListener = mock(ChangeListener.class);
        InOrder inOrder = inOrder(changeListener);

        resultedProperty.addListener(changeListener);

        boolPropFirst.setValue(false);
        inOrder.verify(changeListener).changed(resultedProperty, "foo", "bar");

        boolPropSecond.setValue(false);
        inOrder.verify(changeListener).changed(resultedProperty, "bar", "neither foo nor bar");

        boolPropFirst.setValue(true);
        inOrder.verify(changeListener).changed(resultedProperty, "neither foo nor bar", "foo");
    }

    @Test
    public void testIfElseIfElseInvalidationListener() throws Exception {
        SimpleBooleanProperty boolPropFirst = new SimpleBooleanProperty(true);
        SimpleBooleanProperty boolPropSecond = new SimpleBooleanProperty(true);
        final ObservableValue<String> resultedProperty =
                BooleanExpressionsEx.
                        If(boolPropFirst).Then("foo").
                        ElseIf(boolPropSecond).Then("bar").
                        Else("neither foo nor bar");

        final InvalidationListener invalidationListener = mock(InvalidationListener.class);
        InOrder inOrder = inOrder(invalidationListener);

        resultedProperty.addListener(invalidationListener);

        boolPropFirst.setValue(false);
        inOrder.verify(invalidationListener).invalidated(resultedProperty);

        boolPropSecond.setValue(false);
        inOrder.verify(invalidationListener).invalidated(resultedProperty);

        boolPropFirst.setValue(true);
        inOrder.verify(invalidationListener).invalidated(resultedProperty);
    }
}
