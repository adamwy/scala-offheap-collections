package benchmark

import org.openjdk.jmh.annotations._
import offheap.collection._
import SeqBuilders._
import HashEq.Implicits._
import org.openjdk.jmh.infra.Blackhole

import scala.util.Random
import scala.collection.mutable.{ArrayBuffer => StdlibSeq}

@State(Scope.Thread)
class SeqBenchmark {

  val seqSize = Benchmark.size

  val seq: BufferSeq_Int = {
    val seq = new BufferSeq_Int
    1 to seqSize foreach (seq.append(_))
    seq
  }

  val genericSeq: BufferSeq[Int] = {
    val seq = new BufferSeq[Int]
    1 to seqSize foreach (seq.append(_))
    seq
  }

  val stdSeq: StdlibSeq[Int] = {
    val seq = StdlibSeq[Int]()
    1 to seqSize foreach (seq.append(_))
    seq
  }

  val random = Random

  var randIndex: Int = _
  var randVal: Int = _

  @Setup(Level.Invocation)
  def setup = {
    randIndex = random.nextInt(seqSize)
    randVal = random.nextInt
  }

  @Benchmark
  def readSequential(blackhole: Blackhole) = {
    var i = 0
    while (i < seqSize) {
      blackhole.consume(seq(i))
      i += 1
    }
  }

  @Benchmark
  def readSequentialGeneric(blackhole: Blackhole) = {
    var i = 0
    while (i < seqSize) {
      blackhole.consume(genericSeq(i))
      i += 1
    }
  }

  @Benchmark
  def readSequentialStdlib(blackhole: Blackhole) = {
    var i = 0
    while (i < seqSize) {
      blackhole.consume(stdSeq(i))
      i += 1
    }
  }

  @Benchmark
  def readRandom = seq(randIndex)

  @Benchmark
  def readRandomGeneric = genericSeq(randIndex)

  @Benchmark
  def readRandomStdlib = stdSeq(randIndex)

  @Benchmark
  def append() = {
    val s = new BufferSeq_Int(initialSize = 16)
    var i = 0
    while (i < seqSize) {
      s.append(i)
      i += 1
    }
  }

  @Benchmark
  def appendGeneric() = {
    val s = new BufferSeq[Int](initialSize = 16)
    var i = 0
    while (i < seqSize) {
      s.append(i)
      i += 1
    }
  }

  @Benchmark
  def appendStdlib() = {
    val s = new StdlibSeq[Int](initialSize = 16)
    var i = 0
    while (i < seqSize) {
      s.append(i)
      i += 1
    }
  }

  @Benchmark
  def updateSequential() = {
    val s = seq
    var i = 0
    while (i < seqSize) {
      s(i) = i * 2
      i += 1
    }
  }

  @Benchmark
  def updateSequentialGeneric() = {
    val s = genericSeq
    var i = 0
    while (i < seqSize) {
      s(i) = i * 2
      i += 1
    }
  }

  @Benchmark
  def updateSequentialStdlib() = {
    val s = stdSeq
    var i = 0
    while (i < seqSize) {
      s(i) = i * 2
      i += 1
    }
  }

  @Benchmark
  def updateRandom = seq(randIndex) = randVal

  @Benchmark
  def updateRandomGeneric = genericSeq(randIndex) = randVal

  @Benchmark
  def updateRandomStdlib = stdSeq(randIndex) = randVal

  @Benchmark
  def foreach = {
    var sum = 0
    seq foreach (sum += _)
    sum
  }

  @Benchmark
  def foreachGeneric = {
    var sum = 0
    genericSeq foreachGeneric (sum += _)
    sum
  }

  @Benchmark
  def foreachStdlib = {
    var sum = 0
    stdSeq foreach (sum += _)
    sum
  }

  @Benchmark
  def prepend() = {
    val s = new BufferSeq_Int(initialSize = 16)
    var i = 0
    while (i < seqSize) {
      s.insert(0, i)
      i += 1
    }
  }

  @Benchmark
  def prependGeneric() = {
    val s = new BufferSeq[Int](initialSize = 16)
    var i = 0
    while (i < seqSize) {
      s.insert(0, i)
      i += 1
    }
  }

  @Benchmark
  def prependStdlib() = {
    val s = new StdlibSeq[Int](initialSize = 16)
    var i = 0
    while (i < seqSize) {
      s.insert(0, i)
      i += 1
    }
  }

  @Benchmark
  def map = seq map_Int (_ + 1)

  @Benchmark
  def mapGeneric = genericSeq map (_ + 1)

  @Benchmark
  def mapStdlib = stdSeq map (_ + 1)

  @Benchmark
  def flatMap = seq flatMap_Int { i =>
    val r = new BufferSeq_Int
    var j = 0
    while (j < 5) { r.append(i + j); j += 1 }
    r
  }

  @Benchmark
  def flatMapGeneric = genericSeq flatMap { i =>
    val r = new BufferSeq[Int]
    var j = 0
    while (j < 5) { r.append(i + j); j += 1 }
    r
  }

  @Benchmark
  def flatMapStdlib = stdSeq flatMap { i =>
    val r = StdlibSeq[Int]()
    var j = 0
    while (j < 5) { r.append(i + j); j += 1 }
    r
  }

  @Benchmark
  def filter = seq filter (_ % 2 == 0)

  @Benchmark
  def filterGeneric = genericSeq filter (_ % 2 == 0)

  @Benchmark
  def filterStdlib = stdSeq filter (_ % 2 == 0)
}

@State(Scope.Thread)
class SeqRemoveBenchmark {

  val origin: BufferSeq_Int = {
    val seq = new BufferSeq_Int
    1 to 10000 foreach (seq.append(_))
    seq
  }

  var seq: BufferSeq_Int = _

  @Setup(Level.Invocation)
  def setup = {
    seq = new BufferSeq_Int
    origin.foreach(seq.append(_))
  }

  @Benchmark
  def benchmark = {
    while (seq.nonEmpty) seq.remove(0)
  }
}

@State(Scope.Thread)
class SeqRemoveGenericBenchmark {

  val origin: BufferSeq[Int] = {
    val seq = new BufferSeq[Int]
    1 to 10000 foreach (seq.append(_))
    seq
  }

  var seq: BufferSeq[Int] = _

  @Setup(Level.Invocation)
  def setup = {
    seq = new BufferSeq[Int]
    seq.append(origin)
  }

  @Benchmark
  def benchmark = {
    while (seq.nonEmpty) seq.remove(0)
  }
}

@State(Scope.Thread)
class SeqRemoveStdlibBenchmark {

  val origin: StdlibSeq[Int] = {
    val seq = StdlibSeq[Int]()
    1 to 10000 foreach (seq.append(_))
    seq
  }

  var seq: StdlibSeq[Int] = _

  @Setup(Level.Invocation)
  def setup = {
    seq = StdlibSeq[Int](origin: _*)
  }

  @Benchmark
  def benchmark = {
    while (seq.nonEmpty) seq.remove(0)
  }
}
