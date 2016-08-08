package offheap.collection

import scala.language.experimental.{macros => CanMacro}

/**
  * Implicit operations on collections.
  */
package object ops {

  implicit class SeqOps[A](val seq: Seq[A]) extends AnyVal {

    /**
      * Build new sequence by transforming each value using given function.
      * 
      * @param f transforming function
      * @tparam B transforming function target type
      * @return mapped sequence
      */
    def map[B](f: A => B): Seq[B] = macro offheap.collection.macros.SeqOpsMacros
      .map[B]

    /**
      * Build new sequence by applying given function to each value and appending all elements from resulting sequences.
      * 
      * @param f function that returns sequence of B
      * @tparam B resulting sequence target type
      * @return sequence that contains all elements provided by f
      */
    def flatMap[B](f: A => Seq[B]): Seq[B] = macro offheap.collection.macros.SeqOpsMacros
      .flatMap[B]

    /**
      * Filter sequence with given predicate.
      *
      * @param f predicate that filters sequence values
      * @return filtered sequence
      */
    def filter(f: A => Boolean): Seq[A] = macro offheap.collection.macros.SeqOpsMacros
      .filter[A]

    /**
      * Foreach implementation that inlines given closure.
      *
      * @param f function called for every element in the sequence
      */
    def foreachMacro(f: A => Unit): Unit = macro offheap.collection.macros.SeqOpsMacros.foreach

    /**
      * Apply given operator going from left to right starting with given element.
      *
      * @param z starting element
      * @param op applied operator
      * @tparam B type of the starting element and resulting value
      * @return resulting value
      */
    def foldLeft[B](z: B)(op: (B, A) => B): B = macro offheap.collection.macros.SeqOpsMacros
      .foldLeft[B]

    /**
      * Apply given operator going from right to left starting with given element.
      *
      * @param z starting element
      * @param op applied operator
      * @tparam B type of the starting element and resulting value
      * @return resulting value
      */
    def foldRight[B](z: B)(op: (A, B) => B): B = macro offheap.collection.macros.SeqOpsMacros
      .foldRight[B]

    /**
      * Apply given operator going from left to right starting with first element.
      *
      * @param op applied operator
      * @return resulting value
      */
    def reduceLeft(op: (A, A) => A): A = macro offheap.collection.macros.SeqOpsMacros
      .reduceLeft[A]

    /**
      * Apply given operator going from left to right starting with last element.
      *
      * @param op applied operator
      * @return resulting value
      */
    def reduceRight(op: (A, A) => A): A = macro offheap.collection.macros.SeqOpsMacros
      .reduceRight[A]

    /**
      * Map sequence in place.
      *
      * @param f transforming function
      */
    def transform(f: A => A): Unit = macro offheap.collection.macros.SeqOpsMacros.transform

    /**
      * Test given predicate for all values.
      *
      * @param p predicate to be tested
      * @return true if given predicate returns true for every element in sequence, false otherwise
      */
    def forall(p: A => Boolean): Boolean = macro offheap.collection.macros.SeqOpsMacros.forall

    /**
      * Test if value for which given predicate is true exists.
      *
      * @param p predicate to be tested
      * @return true if given predicate returns true for any element in sequence, false otherwise
      */
    def exists(p: A => Boolean): Boolean = macro offheap.collection.macros.SeqOpsMacros.exists

    /**
      * Test if other sequence has the same elements in the same order.
      *
      * @param other sequence to compare
      * @return true if the other sequence has the same elements, false otherwise
      */
    def sameElements(other: Seq[A]): Boolean = macro offheap.collection.macros.SeqOpsMacros.sameElements

    /**
      * Construct a map using keys from this sequence associated to values from the other.
      *
      * @param values values associated with keys at the same indices
      * @tparam B the type of result map values
      * @return map with keys from this sequence and values from the other
      */
    def zipToMap[B](values: Seq[B]): Map[A, B] = macro offheap.collection.macros.SeqOpsMacros
      .zipToMap[A, B]
  }

  implicit class SetOps[A](val set: Set[A]) extends AnyVal {

    /**
      * Build new set by transforming each value using given function.
      *
      * @param f transforming function
      * @tparam B transforming function target type
      * @return mapped set
      */
    def map[B](f: A => B): Set[B] = macro offheap.collection.macros.SetOpsMacros
      .map[B]

    /**
      * Build new set by applying given function to each value and appending all elements from resulting sets.
      *
      * @param f function that returns set of B
      * @tparam B resulting set target type
      * @return set that contains all elements provided by f
      */
    def flatMap[B](f: A => Set[B]): Set[B] = macro offheap.collection.macros.SetOpsMacros
      .flatMap[B]

    /**
      * Filter set with given predicate.
      *
      * @param f predicate that filters set values
      * @return filtered set
      */
    def filter(f: A => Boolean): Set[A] = macro offheap.collection.macros.SetOpsMacros
      .filter[A]

    /**
      * Foreach implementation that inlines given closure.
      *
      * @param f function called for every element in the set
      */
    def foreachMacro(f: A => Unit): Unit = macro offheap.collection.macros.SetOpsMacros.foreach

    /**
      * Apply given operator to every element in the set starting with given element.
      *
      * @param z starting element
      * @param op applied operator
      * @tparam B type of the starting element and resulting value
      * @return resulting value
      */
    def fold[B](z: B)(op: (B, A) => B): B = macro offheap.collection.macros.SetOpsMacros
      .fold[B]

    /**
      * Apply given operator to every element in the set.
      *
      * @param op applied operator
      * @return resulting value
      */
    def reduce(op: (A, A) => A): A = macro offheap.collection.macros.SetOpsMacros
      .reduce[A]

    /**
      * Test given predicate for all values.
      *
      * @param p predicate to be tested
      * @return true if given predicate returns true for every element in set, false otherwise.
      */
    def forall(p: A => Boolean): Boolean = macro offheap.collection.macros.SetOpsMacros.forall

    /**
      * Test if value for which given predicate is true exists.
      *
      * @param p predicate to be tested
      * @return true if given predicate returns true for any element in set, false otherwise.
      */
    def exists(p: A => Boolean): Boolean = macro offheap.collection.macros.SetOpsMacros.exists
  }

  implicit class MapOps[K, V](val map: Map[K, V]) extends AnyVal {

    /**
      * Build new sequence by transforming each pair of key and value using given function.
      *
      * @param f transforming function
      * @tparam B transforming function target type
      * @return mapped sequence
      */
    def map[B](f: (K, V) => B): Seq[B] = macro offheap.collection.macros.MapOpsMacros
      .map[B]

    /**
      * Build new map by transforming each key with given function.
      *
      * @param f transforming function
      * @tparam B target key type
      * @return map with transformed keys and original values
      */
    def mapKeys[B](f: K => B): Map[B, V] = macro offheap.collection.macros.MapOpsMacros
      .mapKeys[V, B]

    /**
      * Build new map by transforming each value with given function
      *
      * @param f transforming function
      * @tparam B target value type
      * @return map with transformed values and original keys
      */
    def mapValues[B](f: V => B): Map[K, B] = macro offheap.collection.macros.MapOpsMacros
      .mapValues[K, B]

    /**
      * Build new sequence by applying given function to each pair of key and value and appending all elements
      * from resulting sequences.
      *
      * @param f function that returns sequence of B
      * @tparam B resulting sequence target type
      * @return sequence that contains all elements provided by f
      */
    def flatMap[B](f: (K, V) => Seq[B]): Seq[B] = macro offheap.collection.macros.MapOpsMacros
      .flatMap[B]

    /**
      * Filter map with given predicate.
      *
      * @param f predicate that filters pairs of key and value
      * @return filtered map
      */
    def filter(f: (K, V) => Boolean): Map[K, V] = macro offheap.collection.macros.MapOpsMacros
      .filter[K, V]

    /**
      * Foreach implementation that inlines given closure.
      *
      * @param f function called for every element in the map
      */
    def foreachMacro(f: (K, V) => Unit): Unit = macro offheap.collection.macros.MapOpsMacros.foreach

    /**
      * Apply given operator to every pair of key and value in the map starting with given element.
      *
      * @param z starting element
      * @param op applied operator
      * @tparam B type of the starting element and resulting value
      * @return resulting value
      */
    def fold[B](z: B)(op: (B, K, V) => B): B = macro offheap.collection.macros.MapOpsMacros
      .fold[B]

    /**
      * Apply given operator to every key in the map.
      *
      * @param op applied operator
      * @return resulting value
      */
    def reduceKeys(op: (K, K) => K): K = macro offheap.collection.macros.MapOpsMacros
      .reduceKeys[K]

    /**
      * Apply given operator to every value in the map.
      *
      * @param op applied operator
      * @return resulting value
      */
    def reduceValues(op: (V, V) => V): V = macro offheap.collection.macros.MapOpsMacros
      .reduceValues[V]

    /**
      * Map values in place.
      *
      * @param f transforming function
      */
    def transformValues(f: V => V): Unit = macro offheap.collection.macros.MapOpsMacros.transformValues

    /**
      * Test given predicate for all pairs of key and value.
      *
      * @param p predicate to be tested
      * @return true if given predicate returns true for every pair of key and value in map, false otherwise
      */
    def forall(p: (K, V) => Boolean): Boolean = macro offheap.collection.macros.MapOpsMacros.forall

    /**
      * Test if value for which given predicate is true exists.
      *
      * @param p predicate to be tested
      * @return true if given predicate returns true for any pair of key and value in map, false otherwise
      */
    def exists(p: (K, V) => Boolean): Boolean = macro offheap.collection.macros.MapOpsMacros.exists
  }
}
