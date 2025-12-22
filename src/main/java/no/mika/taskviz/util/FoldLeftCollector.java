package no.mika.taskviz.util;

import java.util.Collections;
import java.util.Set;
import java.util.function.*;
import java.util.stream.Collector;

/*
Copyright 2025 Statens pensjonskasse

Permission is hereby granted, free of charge, to any person obtaining a copy of this software
and associated documentation files (the “Software”), to deal in the Software without
restriction,including without limitation the rights to use, copy, modify, merge, publish,
distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the
Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies
or substantial portions of the Software.

THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR
OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
OTHER DEALINGS IN THE SOFTWARE.
 */
class FoldLeftCollector<T, R> implements Collector<T, FoldLeftCollector.Holder<T, R>, R> {
    private final R initial;
    private final BiFunction<R, T, R> reduce;

    FoldLeftCollector(final R initial, final BiFunction<R, T, R> reduce) {
        this.initial = initial;
        this.reduce = reduce;
    }

    @Override
    public Supplier<Holder<T, R>> supplier() {
        return () -> new Holder<>(initial, reduce);
    }

    @Override
    public BiConsumer<Holder<T, R>, T> accumulator() {
        return Holder::update;
    }

    @Override
    public BinaryOperator<Holder<T, R>> combiner() {
        return (h1, h2) -> {
            throw new UnsupportedOperationException("Does not support parallel operations in foldLeft(...): should be sequential");
        };
    }

    @Override
    public Function<Holder<T, R>, R> finisher() {
        return Holder::get;
    }

    @Override
    public Set<Characteristics> characteristics() {
        return Collections.emptySet();
    }

    static class Holder<A, R> {
        private final BiFunction<R, A, R> reduce;
        private R value;

        private Holder(final R value, final BiFunction<R, A, R> reduce) {
            this.value = value;
            this.reduce = reduce;
        }

        void update(final A a) {
            value = reduce.apply(value, a);
        }

        R get() {
            return value;
        }
    }
}
