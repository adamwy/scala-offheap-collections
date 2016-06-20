package benchmark

import org.openjdk.jmh.annotations._
import offheap.collection._
import HashEq.Implicits._
import org.openjdk.jmh.infra.Blackhole

import scala.collection.mutable.{HashSet => StdlibSet}

@State(Scope.Thread)
class IntSetBenchmark {

  import Benchmark._

  val set: HashSet_Int = {
    val set = new HashSet_Int(initialSize)
    var i = 0
    while (i < size) {
      set.add(i)
      i += 1
    }
    set
  }

  val genericSet: HashSet[Int] = {
    val set = new HashSet[Int](initialSize)
    var i = 0
    while (i < size) {
      set.add(i)
      i += 1
    }
    set
  }

  val stdSet: StdlibSet[Int] = {
    val set = StdlibSet[Int]()
    var i = 0
    while (i < size) {
      set.add(i)
      i += 1
    }
    set
  }

  var randKey: Int = _
  var nonExistingKey: Int = _

  @Setup(Level.Invocation)
  def setup = {
    randKey = random.nextInt(size)
    nonExistingKey = randKey + size
  }

  @Benchmark
  def containsExisting = set(randKey)

  @Benchmark
  def containsExistingGeneric = genericSet(randKey)

  @Benchmark
  def containsExistingStdlib = stdSet(randKey)

  @Benchmark
  def containsNonExisting = set(nonExistingKey)

  @Benchmark
  def containsNonExistingGeneric = genericSet(nonExistingKey)

  @Benchmark
  def containsNonExistingStdlib = stdSet(nonExistingKey)

  @Benchmark
  def add = {
    val s = new HashSet_Int(initialSize)
    var i = 0
    while (i < size) {
      s.add(i)
      i += 1
    }
  }

  @Benchmark
  def addGeneric = {
    val s = new HashSet[Int](initialSize)
    var i = 0
    while (i < size) {
      s.add(i)
      i += 1
    }
  }

  @Benchmark
  def addStdlib = {
    val s = StdlibSet[Int]()
    var i = 0
    while (i < size) {
      s.add(i)
      i += 1
    }
  }

  @Benchmark
  def foreach(blackhole: Blackhole) = set foreach (blackhole.consume(_))

  @Benchmark
  def foreachGeneric(blackhole: Blackhole) = genericSet foreachGeneric (blackhole.consume(_))

  @Benchmark
  def foreachStdlib(blackhole: Blackhole) =
    stdSet foreach (blackhole.consume(_))
}

@State(Scope.Thread)
class IntSetRemoveBenchmark {

  import Benchmark._

  var set: HashSet_Int = _

  @Setup(Level.Invocation)
  def setup = {
    set = new HashSet_Int(initialSize)
    var i = 0
    while (i < size) {
      set.add(i)
      i += 1
    }
  }

  @Benchmark
  def benchmark = {
    var i = 0
    while (i < size / 10) { set.remove(i * 10); i += 1 }
  }
}

@State(Scope.Thread)
class IntSetRemoveGenericBenchmark {

  import Benchmark._

  var set: HashSet[Int] = _

  @Setup(Level.Invocation)
  def setup = {
    set = new HashSet[Int](initialSize)
    var i = 0
    while (i < size) {
      set.add(i)
      i += 1
    }
  }

  @Benchmark
  def benchmark = {
    var i = 0
    while (i < size / 10) { set.remove(i * 10); i += 1 }
  }
}

@State(Scope.Thread)
class IntSetRemoveStdlibBenchmark {

  import Benchmark._

  var set: StdlibSet[Int] = _

  @Setup(Level.Invocation)
  def setup = {
    set = StdlibSet[Int]()
    var i = 0
    while (i < size) {
      set.add(i)
      i += 1
    }
  }

  @Benchmark
  def benchmark = {
    var i = 0
    while (i < size / 10) { set.remove(i * 10); i += 1 }
  }
}
