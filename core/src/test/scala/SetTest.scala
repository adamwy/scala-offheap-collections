package test

import scala.{collection => stdlib}
import org.scalatest.{BeforeAndAfter, FunSuite}
import offheap.collection._

class SetTest extends FunSuite with BeforeAndAfter {
  var set: IntHashSet = _

  before {
    set = new IntHashSet
    1 to 7 foreach (set.add(_))
    5 to 10 foreach (set.add(_))
  }

  test("isEmpty") {
    assert(set.nonEmpty)
    assert(new IntHashSet().isEmpty)
  }

  test("size") {
    assert(set.size == 10)
  }

  test("foreach") {
    var result = List[Int]()
    set.foreach(result ::= _)

    assert(result.size == result.distinct.size)
    assert(result.sum == 10 * 11 / 2)
  }

  test("apply") {
    assert(!set(0))
    1 to 10 foreach (x => assert(set(x)))
    11 to 100 foreach (x => assert(!set(x)))
  }

  test("add") {
    assert(!set.add(5))
    assert(set.add(15))
    assert(set.add(45))

    assert(set.size == 12)
    assert(set(15))
    assert(set(45))
  }

  test("remove") {
    for (i <- 1 to 10 by 2) {
      assert(set.remove(i))
      assert(!set.remove(i))
    }
    assert(set.size == 5)
    1 to 10 by 2 foreach (i => assert(!set(i)))
    2 to 10 by 2 foreach (i => assert(set(i)))
  }

  test("intersect") {
    val other = new IntHashSet
    5 to 15 foreach (other.add(_))

    val intersect = set intersect other
    assert(intersect.size == 6)
    1 to 4 foreach (i => assert(!intersect(i)))
    5 to 10 foreach (i => assert(intersect(i)))
    11 to 15 foreach (i => assert(!intersect(i)))
  }

  test("union") {
    val other = new IntHashSet
    5 to 15 foreach (other.add(_))

    val union = set union other
    assert(union.size == 15)
    1 to 15 foreach (i => assert(union(i)))
  }

  test("diff") {
    val other = new IntHashSet
    5 to 15 foreach (set.add(_))
    5 to 10 foreach (other.add(_))

    val diff = set diff other
    assert(diff.size == 9)
    1 to 4 foreach (i => assert(diff(i)))
    5 to 10 foreach (i => assert(!diff(i)))
    11 to 15 foreach (i => assert(diff(i)))
  }
}
