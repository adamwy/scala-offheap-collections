package offheap.collection

trait Seq[A] extends Traversable[A] {
  def apply(index: Int): A
  def append(elems: A*): Unit
  def append(that: Traversable[A]): Unit
  def update(index: Int, value: A): Unit
  def remove(n: Int): A
  def index(elem: A): Int
  def insert(index: Int, elem: A): Unit
}