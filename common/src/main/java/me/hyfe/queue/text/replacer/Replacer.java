package me.hyfe.queue.text.replacer;

import java.util.function.UnaryOperator;

@FunctionalInterface
public interface Replacer extends UnaryOperator<Subject> {
}