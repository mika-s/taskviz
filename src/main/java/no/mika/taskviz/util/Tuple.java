package no.mika.taskviz.util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Stream;

import static no.mika.taskviz.util.Collectors.foldLeft;

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
public interface Tuple {
    static <T1, T2> void forEach(
            final Stream<Tuple2<T1, T2>> tuples,
            final BiConsumer<T1, T2> f
    ) {
        tuples.forEach(t -> f.accept(t._1, t._2));
    }

    @SafeVarargs
    @SuppressWarnings("varargs")
    static <Key, Value> Map<Key, Value> asMap(final Tuple2<Key, Value> t1, final Tuple2<Key, Value>... tuples) {
        return Stream.concat(
                Stream.of(t1),
                Arrays.stream(tuples)
        ).collect(foldLeft(new HashMap<>(), (map, tuple) -> {
            if (map.containsKey(tuple._1)) {
                throw new IllegalStateException(String.format("Contains multiple keys: '%s'", tuple._1));
            }
            map.put(tuple._1, tuple._2);
            return map;
        }));
    }

    static <T1, T2> Tuple2<T1, T2> tuple(final T1 _1, final T2 _2) {
        return new Tuple2<>(_1, _2);
    }

    static <T1, T2, R> Function<Tuple2<T1, T2>, R> tupled(final BiFunction<T1, T2, R> f) {
        return tuple -> f.apply(tuple._1, tuple._2);
    }

}
