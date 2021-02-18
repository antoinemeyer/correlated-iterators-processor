package com.teketik.cip;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Collection of convenient {@link CorrelatedIterable} wrappers.
 */
public abstract class CorrelatedIterables {

    private CorrelatedIterables() {
    }

    public interface CorrelationDoubleStreamConsumer<K extends Comparable, A, B> {
        public void consume(K key, List<A> aElements, List<B> bElements);
    }

    public static <K extends Comparable, A, B> void correlate(
            Iterator<A> iteratorA, Class<A> typeA,
            Iterator<B> iteratorB, Class<B> typeB,
            CorrelationDoubleStreamConsumer<K, A, B> streamConsumer
    ) {
        stream(
                new CorrelatedIterable(
                        new IteratorDefinition<A>(iteratorA, typeA),
                        new IteratorDefinition<B>(iteratorB, typeB)
                )
        ).forEach(payload -> {
            streamConsumer.consume(
                    (K) payload.getKey(),
                    (List<A>) payload.getPayload().getOrDefault(typeA, Collections.emptyList()),
                    (List<B>) payload.getPayload().getOrDefault(typeB, Collections.emptyList())
            );
        });
    }

    public interface CorrelationTripleStreamConsumer<K extends Comparable, A, B, C> {
        public void consume(K key, List<A> aElements, List<B> bElements, List<C> cElements);
    }

    public static <K extends Comparable, A, B, C> void correlate(
            Iterator<A> iteratorA, Class<A> typeA,
            Iterator<B> iteratorB, Class<B> typeB,
            Iterator<C> iteratorC, Class<C> typeC,
            CorrelationTripleStreamConsumer<K, A, B, C> streamConsumer
    ) {
        stream(
                new CorrelatedIterable(
                        new IteratorDefinition<A>(iteratorA, typeA),
                        new IteratorDefinition<B>(iteratorB, typeB),
                        new IteratorDefinition<C>(iteratorC, typeC)
                )
        ).forEach(payload -> {
            streamConsumer.consume(
                    (K) payload.getKey(),
                    (List<A>) payload.getPayload().getOrDefault(typeA, Collections.emptyList()),
                    (List<B>) payload.getPayload().getOrDefault(typeB, Collections.emptyList()),
                    (List<C>) payload.getPayload().getOrDefault(typeC, Collections.emptyList())
            );
        });
    }

    public interface CorrelationQuadrupleStreamConsumer<K extends Comparable, A, B, C, D> {
        public void consume(K key, List<A> aElements, List<B> bElements, List<C> cElements, List<D> dElements);
    }

    public static <K extends Comparable, A, B, C, D> void correlate(
            Iterator<A> iteratorA, Class<A> typeA,
            Iterator<B> iteratorB, Class<B> typeB,
            Iterator<C> iteratorC, Class<C> typeC,
            Iterator<D> iteratorD, Class<D> typeD,
            CorrelationQuadrupleStreamConsumer<K, A, B, C, D> streamConsumer
    ) {
        stream(
                new CorrelatedIterable(
                        new IteratorDefinition<A>(iteratorA, typeA),
                        new IteratorDefinition<B>(iteratorB, typeB),
                        new IteratorDefinition<C>(iteratorC, typeC),
                        new IteratorDefinition<D>(iteratorD, typeD)
                )
        ).forEach(payload -> {
            streamConsumer.consume(
                    (K) payload.getKey(),
                    (List<A>) payload.getPayload().getOrDefault(typeA, Collections.emptyList()),
                    (List<B>) payload.getPayload().getOrDefault(typeB, Collections.emptyList()),
                    (List<C>) payload.getPayload().getOrDefault(typeC, Collections.emptyList()),
                    (List<D>) payload.getPayload().getOrDefault(typeD, Collections.emptyList())
            );
        });
    }

    private static Stream<CorrelatedPayload> stream(final CorrelatedIterable correlatedIterables) {
        return StreamSupport.stream(
                Spliterators.spliteratorUnknownSize(
                        correlatedIterables.iterator(),
                        Spliterator.ORDERED
                ),
                false
        );
    }

}
