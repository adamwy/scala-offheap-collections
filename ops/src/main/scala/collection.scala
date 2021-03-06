import mutabilite.generic.{Map, Seq, Set}
import scala.language.experimental.{macros => CanMacro}

/**
  * Library implicits.
  */
package object mutabilite {

  implicit object BooleanHash extends Hash_Boolean {
    def hash(value: Boolean): Int = if (value) 1231 else 1237
  }

  implicit object CharHash extends Hash_Char {
    def hash(value: Char): Int = value
  }

  implicit object ByteHash extends Hash_Byte {
    def hash(value: Byte): Int = value
  }

  implicit object ShortHash extends Hash_Short {
    def hash(value: Short): Int = value
  }

  implicit object IntHash extends Hash_Int {
    def hash(value: Int): Int = value
  }

  implicit object LongHash extends Hash_Long {
    def hash(value: Long): Int = (value ^ (value >>> 32)).asInstanceOf[Int]
  }

  implicit object FloatHash extends Hash_Float {
    def hash(value: Float): Int = java.lang.Float.floatToIntBits(value)
  }

  implicit object DoubleHash extends Hash_Double {
    def hash(value: Double): Int = {
      val bits = java.lang.Double.doubleToLongBits(value)
      (bits ^ (bits >>> 32)).asInstanceOf[Int]
    }
  }

  implicit object ObjectHash extends Hash_Object {
    def hash(value: Any): Int = value.hashCode()
  }

  implicit class SeqOps[A](val seq: Seq[A]) extends AnyVal {

    /**
      * Build new sequence by transforming each value using given function.
      *
      * @param f transforming function
      * @tparam B transforming function target type
      * @return mapped sequence
      */
    def map[B](f: A => B): Seq[B] = macro mutabilite.macros.SeqOpsMacrosWhitebox.map[B]

    /**
      * Build new sequence by applying given function to each value and appending all elements from resulting sequences.
      *
      * @param f function that returns sequence of B
      * @tparam B resulting sequence target type
      * @return sequence that contains all elements provided by f
      */
    def flatMap[B](f: A => Seq[B]): Seq[B] = macro mutabilite.macros.SeqOpsMacrosWhitebox
      .flatMap[B]

    /**
      * Filter sequence with given predicate.
      *
      * @param f predicate that filters sequence values
      * @return filtered sequence
      */
    def filter(f: A => Boolean): Seq[A] = macro mutabilite.macros.SeqOpsMacrosWhitebox
      .filter[A]

    /**
      * Foreach implementation that inlines given closure.
      *
      * @param f function called for every element in the sequence
      */
    def foreach(f: A => Unit): Unit = macro mutabilite.macros.SeqOpsMacros.foreach

    /**
      * Apply given operator going from left to right starting with given element.
      *
      * @param z starting element
      * @param op applied operator
      * @tparam B type of the starting element and resulting value
      * @return resulting value
      */
    def foldLeft[B](z: B)(op: (B, A) => B): B = macro mutabilite.macros.SeqOpsMacros
      .foldLeft[B]

    /**
      * Apply given operator going from right to left starting with given element.
      *
      * @param z starting element
      * @param op applied operator
      * @tparam B type of the starting element and resulting value
      * @return resulting value
      */
    def foldRight[B](z: B)(op: (A, B) => B): B = macro mutabilite.macros.SeqOpsMacros
      .foldRight[B]

    /**
      * Apply given operator going from left to right starting with first element.
      *
      * @param op applied operator
      * @return resulting value
      */
    def reduceLeft(op: (A, A) => A): A = macro mutabilite.macros.SeqOpsMacros
      .reduceLeft[A]

    /**
      * Apply given operator going from left to right starting with last element.
      *
      * @param op applied operator
      * @return resulting value
      */
    def reduceRight(op: (A, A) => A): A = macro mutabilite.macros.SeqOpsMacros
      .reduceRight[A]

    /**
      * Map sequence in place.
      *
      * @param f transforming function
      */
    def transform(f: A => A): Unit = macro mutabilite.macros.SeqOpsMacros.transform

    /**
      * Test given predicate for all values.
      *
      * @param p predicate to be tested
      * @return true if given predicate returns true for every element in sequence, false otherwise
      */
    def forall(p: A => Boolean): Boolean = macro mutabilite.macros.SeqOpsMacros.forall

    /**
      * Test if value for which given predicate is true exists.
      *
      * @param p predicate to be tested
      * @return true if given predicate returns true for any element in sequence, false otherwise
      */
    def exists(p: A => Boolean): Boolean = macro mutabilite.macros.SeqOpsMacros.exists

    /**
      * Test if other sequence has the same elements in the same order.
      *
      * @param other sequence to compare
      * @return true if the other sequence has the same elements, false otherwise
      */
    def sameElements(other: Seq[A]): Boolean = macro mutabilite.macros.SeqOpsMacros.sameElements

    /**
      * Construct a map using keys from this sequence associated to values from the other.
      *
      * @param values values associated with keys at the same indices
      * @tparam B the type of result map values
      * @return map with keys from this sequence and values from the other
      */
    def zipToMap[B](values: Seq[B]): Map[A, B] = macro mutabilite.macros.SeqOpsMacrosWhitebox
      .zipToMap[A, B]

    /**
      * Group values to a map.
      *
      * @param f function that transforms sequence values to map keys
      * @tparam K target map key type
      * @return map with keys mapped by f and values that map to the same key
      */
    def groupBy[K](f: A => K): Map[K, _] = macro mutabilite.macros.SeqOpsMacrosWhitebox
      .groupBy[A, K]
  }

  implicit class SetOps[A](val set: Set[A]) extends AnyVal {

    /**
      * Build new set by transforming each value using given function.
      *
      * @param f transforming function
      * @tparam B transforming function target type
      * @return mapped set
      */
    def map[B](f: A => B): Set[B] = macro mutabilite.macros.SetOpsMacrosWhitebox.map[B]

    /**
      * Build new set by applying given function to each value and appending all elements from resulting sets.
      *
      * @param f function that returns set of B
      * @tparam B resulting set target type
      * @return set that contains all elements provided by f
      */
    def flatMap[B](f: A => Set[B]): Set[B] = macro mutabilite.macros.SetOpsMacrosWhitebox
      .flatMap[B]

    /**
      * Filter set with given predicate.
      *
      * @param f predicate that filters set values
      * @return filtered set
      */
    def filter(f: A => Boolean): Set[A] = macro mutabilite.macros.SetOpsMacrosWhitebox
      .filter[A]

    /**
      * Foreach implementation that inlines given closure.
      *
      * @param f function called for every element in the set
      */
    def foreach(f: A => Unit): Unit = macro mutabilite.macros.SetOpsMacros.foreach

    /**
      * Apply given operator to every element in the set starting with given element.
      *
      * @param z starting element
      * @param op applied operator
      * @tparam B type of the starting element and resulting value
      * @return resulting value
      */
    def fold[B](z: B)(op: (B, A) => B): B = macro mutabilite.macros.SetOpsMacros
      .fold[B]

    /**
      * Apply given operator to every element in the set.
      *
      * @param op applied operator
      * @return resulting value
      */
    def reduce(op: (A, A) => A): A = macro mutabilite.macros.SetOpsMacros
      .reduce[A]

    /**
      * Test given predicate for all values.
      *
      * @param p predicate to be tested
      * @return true if given predicate returns true for every element in set, false otherwise.
      */
    def forall(p: A => Boolean): Boolean = macro mutabilite.macros.SetOpsMacros.forall

    /**
      * Test if value for which given predicate is true exists.
      *
      * @param p predicate to be tested
      * @return true if given predicate returns true for any element in set, false otherwise.
      */
    def exists(p: A => Boolean): Boolean = macro mutabilite.macros.SetOpsMacros.exists
  }

  implicit class MapOps[K, V](val map: Map[K, V]) extends AnyVal {

    /**
      * Build new sequence by transforming each pair of key and value using given function.
      *
      * @param f transforming function
      * @tparam B transforming function target type
      * @return mapped sequence
      */
    def map[B](f: (K, V) => B): Seq[B] = macro mutabilite.macros.MapOpsMacrosWhitebox
      .map[B]

    /**
      * Build new map by transforming each key with given function.
      *
      * @param f transforming function
      * @tparam B target key type
      * @return map with transformed keys and original values
      */
    def mapKeys[B](f: K => B): Map[B, V] = macro mutabilite.macros.MapOpsMacrosWhitebox
      .mapKeys[V, B]

    /**
      * Build new map by transforming each value with given function
      *
      * @param f transforming function
      * @tparam B target value type
      * @return map with transformed values and original keys
      */
    def mapValues[B](f: V => B): Map[K, B] = macro mutabilite.macros.MapOpsMacrosWhitebox
      .mapValues[K, B]

    /**
      * Build new sequence by applying given function to each pair of key and value and appending all elements
      * from resulting sequences.
      *
      * @param f function that returns sequence of B
      * @tparam B resulting sequence target type
      * @return sequence that contains all elements provided by f
      */
    def flatMap[B](f: (K, V) => Seq[B]): Seq[B] = macro mutabilite.macros.MapOpsMacrosWhitebox
      .flatMap[B]

    /**
      * Filter map with given predicate.
      *
      * @param f predicate that filters pairs of key and value
      * @return filtered map
      */
    def filter(f: (K, V) => Boolean): Map[K, V] = macro mutabilite.macros.MapOpsMacrosWhitebox
      .filter[K, V]

    /**
      * Foreach implementation that inlines given closure.
      *
      * @param f function called for every element in the map
      */
    def foreach(f: (K, V) => Unit): Unit = macro mutabilite.macros.MapOpsMacros.foreach

    /**
      * Apply given operator to every pair of key and value in the map starting with given element.
      *
      * @param z starting element
      * @param op applied operator
      * @tparam B type of the starting element and resulting value
      * @return resulting value
      */
    def fold[B](z: B)(op: (B, K, V) => B): B = macro mutabilite.macros.MapOpsMacros
      .fold[B]

    /**
      * Apply given operator to every key in the map.
      *
      * @param op applied operator
      * @return resulting value
      */
    def reduceKeys(op: (K, K) => K): K = macro mutabilite.macros.MapOpsMacros
      .reduceKeys[K]

    /**
      * Apply given operator to every value in the map.
      *
      * @param op applied operator
      * @return resulting value
      */
    def reduceValues(op: (V, V) => V): V = macro mutabilite.macros.MapOpsMacros
      .reduceValues[V]

    /**
      * Map values in place.
      *
      * @param f transforming function
      */
    def transformValues(f: V => V): Unit = macro mutabilite.macros.MapOpsMacros.transformValues

    /**
      * Test given predicate for all pairs of key and value.
      *
      * @param p predicate to be tested
      * @return true if given predicate returns true for every pair of key and value in map, false otherwise
      */
    def forall(p: (K, V) => Boolean): Boolean = macro mutabilite.macros.MapOpsMacros.forall

    /**
      * Test if value for which given predicate is true exists.
      *
      * @param p predicate to be tested
      * @return true if given predicate returns true for any pair of key and value in map, false otherwise
      */
    def exists(p: (K, V) => Boolean): Boolean = macro mutabilite.macros.MapOpsMacros.exists
  }
}
