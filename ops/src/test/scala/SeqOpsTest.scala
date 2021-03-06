package test

import mutabilite._
import org.scalatest.FunSuite

class SeqOpsTest extends FunSuite {
  test("map int to string") {
    val seq: Seq_Int = new Seq_Int
    1 to 3 foreach (seq.append(_))

    val mapped = seq map { _ toString }
    val test: Seq_Object[String] = mapped
    assert(mapped.size == 3)
    1 to 3 foreach (i => assert(mapped(i - 1) == i.toString))
  }

  test("map string to int") {
    val seq: Seq_Object[String] = new Seq_Object[String]
    1 to 3 foreach (i => seq.append(i toString))

    val mapped = seq map { Integer.parseInt(_) }
    val test: Seq_Int = mapped
    assert(mapped.size == 3)
    1 to 3 foreach (i => assert(mapped(i - 1) == i))
  }

  test("map int") {
    val seq: Seq_Int = new Seq_Int
    1 to 3 foreach (seq.append(_))

    val mapped = seq.map(i => i * 2)
    val test: Seq_Int = mapped
    assert(mapped.size == 3)
    1 to 3 foreach (i => assert(mapped(i - 1) == i * 2))
  }

  test("flatMap int") {
    val seq: Seq_Int = new Seq_Int
    1 to 5 by 2 foreach (seq.append(_))

    val mapped = seq flatMap { i =>
      val r = new Seq_Int
      r.append(i)
      r.append(i + 1)
      r
    }
    val test: Seq_Int = mapped

    assert(mapped.size == 6)
    1 to 6 foreach (i => assert(mapped(i - 1) == i))
  }

  test("map") {
    val seq: Seq_Int = new Seq_Int
    1 to 3 foreach (seq.append(_))

    val mapped = seq.map(i => i * 2.0f)
    val test: Seq_Float = mapped
    assert(mapped.size == 3)
    1 to 3 foreach (i => assert(mapped(i - 1) == i * 2.0f))
  }

  test("flatMap") {
    val seq: Seq_Int = new Seq_Int
    1 to 5 by 2 foreach (seq.append(_))

    val mapped = seq flatMap { i =>
      val r = new Seq_Float
      r.append(i)
      r.append(i + 1)
      r
    }
    val test: Seq_Float = mapped

    assert(mapped.size == 6)
    1 to 6 foreach (i => assert(mapped(i - 1) == i))
  }

  test("filter") {
    val seq: Seq_Int = new Seq_Int
    1 to 10 foreach (seq.append(_))

    val filtered = seq filter (i => i % 2 == 0)
    val test: Seq_Int = filtered

    assert(filtered.size == 5)
    2 to 10 by 2 foreach (i => assert(filtered((i - 1) / 2) == i))
  }

  test("foreachMacro") {
    val seq: Seq_Int = new Seq_Int
    1 to 10 foreach (seq.append(_))

    var sum = 0
    seq foreach (sum += _)
    assert(sum == 10 * 11 / 2)
  }

  test("foldLeft") {
    val seq: Seq_Int = new Seq_Int
    1 to 3 foreach (seq.append(_))

    assert(seq.foldLeft(0)((acc, el) => (acc + el) * el) == 27)
    assert(new Seq_Int().foldLeft[Int](3)((_, _) => 1) == 3)
  }

  test("foldRight") {
    val seq: Seq_Int = new Seq_Int
    1 to 3 foreach (seq.append(_))

    assert(seq.foldRight(0)((el, acc) => (acc + el) * el) == 23)
    assert(new Seq_Int().foldRight[Int](3)((_, _) => 1) == 3)
  }

  test("reduceLeft") {
    val seq: Seq_Int = new Seq_Int
    1 to 3 foreach (seq.append(_))

    assert(seq.reduceLeft((acc, el) => (acc + el) * el) == 27)
  }

  test("reduceRight") {
    val seq: Seq_Int = new Seq_Int
    1 to 3 foreach (seq.append(_))

    assert(seq.reduceRight((el, acc) => (acc + el) * el) == 11)
  }

  test("transform") {
    val seq: Seq_Int = new Seq_Int
    1 to 10 foreach (seq.append(_))

    seq.transform(_ * 10)

    1 to 10 foreach (i => assert(seq(i - 1) == i * 10))
  }

  test("forall") {
    val seq: Seq_Int = new Seq_Int
    2 to 100 by 2 foreach (seq.append(_))

    assert(seq.forall(_ % 2 == 0))
    assert(!seq.forall(_ > 50))
  }

  test("exists") {
    val seq: Seq_Int = new Seq_Int
    2 to 100 by 2 foreach (seq.append(_))

    assert(seq.exists(_ == 62))
    assert(!seq.exists(_ % 2 == 1))
  }

  test("sameElements") {
    val seq: Seq_Int = new Seq_Int
    1 to 10 foreach (seq.append(_))

    val same: Seq_Int = new Seq_Int
    1 to 10 foreach (same.append(_))
    assert(seq sameElements same)

    val different: Seq_Int = new Seq_Int
    1 to 10 foreach (i => different.append(i * i))
    assert(!seq.sameElements(different))

    val smaller: Seq_Int = new Seq_Int
    1 to 5 foreach (smaller.append(_))
    assert(!seq.sameElements(smaller))
    assert(!smaller.sameElements(seq))
  }

  test("zipToMap") {
    val seq: Seq_Int = new Seq_Int
    1 to 10 foreach (seq.append(_))

    val smallSeq = new Seq_Int
    1 to 5 foreach (smallSeq.append(_))

    val strings = new Seq_Object[String]
    1 to 10 foreach (i => strings.append(i toString))

    val smallStrings = new Seq_Object[String]
    1 to 5 foreach (i => smallStrings.append(i toString))

    val zip = seq zipToMap strings
    val test: Map_Int_Object[String] = zip

    assert(zip.size == 10)
    1 to 10 foreach (i => assert(zip(i) == i.toString))

    assert(smallSeq.zipToMap(strings).size == 5)
    assert(seq.zipToMap(smallStrings).size == 5)
  }

  test("groupBy") {
    val seq: Seq_Int = new Seq_Int
    0 until 100 foreach (seq.append(_))

    val map = seq groupBy (_ % 10)
    val test: Map_Int_Object[Seq_Int] = map

    assert(map.size == 10)
    0 until 10 foreach { i =>
      val s = map(i)
      val t: Seq_Int = s
      assert(s.size == 10)
      0 until 10 foreach (j => assert(s(j) == i + j * 10))
    }
  }
}
