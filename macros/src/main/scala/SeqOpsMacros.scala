package offheap.collection.macros

import scala.reflect.macros.whitebox

class SeqOpsMacros(val c: whitebox.Context) extends Common {
  import c.universe._
  import c.universe.definitions._

  def builderType[B: WeakTypeTag]: TypeName = {
    val B = weakTypeOf[B]
    val typeName = B match {
      case BooleanTpe => "Boolean"
      case CharTpe => "Char"
      case ByteTpe => "Byte"
      case ShortTpe => "Short"
      case IntTpe => "Int"
      case LongTpe => "Long"
      case FloatTpe => "Float"
      case DoubleTpe => "Double"
      case _ => "Object"
    }
    TypeName("BufferSeq_" + typeName)
  }

  def map[B: WeakTypeTag](f: Tree) =
    stabilized(c.prefix.tree) { pre =>
      val builder = builderType[B]
      q"""
        val builder = new $builder(initialSize = $pre.seq.size)
        var i = 0
        while (i < $pre.seq.size) {
          builder.append($f($pre.seq(i)))
          i += 1
        }
        builder
      """
    }

  def flatMap[B: WeakTypeTag](f: Tree) =
    stabilized(c.prefix.tree) { pre =>
      val builder = builderType[B]
      q"""
        val builder = new $builder
        var i = 0
        while (i < $pre.seq.size) {
          val el = $pre.seq(i)
          val result = $f(el)
          val resultSize = result.size
          var j = 0
          while (j < resultSize) {
            builder.append(result(j))
            j += 1
          }
          i += 1
        }
        builder
      """
    }

  def filter(f: Tree) =
    stabilized(c.prefix.tree) { pre =>
      q"""
        val result = new BufferSeq_Int
        var i = 0
        while (i < $pre.seq.size) {
          val el = $pre.seq(i)
          if ($f(el)) result.append(el)
          i += 1
        }
        result
      """
    }
}
