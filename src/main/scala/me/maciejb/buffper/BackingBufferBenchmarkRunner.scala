package me.maciejb.buffper

import java.io.DataOutputStream
import java.nio.ByteBuffer
import java.util.concurrent.TimeUnit

import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream
import org.openjdk.jmh.annotations.{BenchmarkMode, OutputTimeUnit, _}
import org.openjdk.jmh.runner.Runner
import org.openjdk.jmh.runner.options.{Options, OptionsBuilder}
import org.roaringbitmap.RealDataset._
import org.roaringbitmap.ZipRealDataRetriever
import org.roaringbitmap.buffer.{BufferFastAggregation, ImmutableRoaringBitmap}

import scala.collection.JavaConverters._

@State(Scope.Benchmark)
class BackingBufferBenchmark {

  @Param(Array(WIKILEAKS_NOQUOTES, WEATHER_SEPT_85, DIMENSION_033, CENSUS_INCOME, USCENSUS2000))
  var dataset: String = _

  @Param(Array("heap", "direct"))
  var bufferType: String = _

  var bitmaps: Array[ImmutableRoaringBitmap] = _

  private def backByDirectBuffer(bitmap: ImmutableRoaringBitmap): ImmutableRoaringBitmap = {
    val bos = new ByteOutputStream()
    val dos = new DataOutputStream(bos)
    bitmap.serialize(dos)

    val bytes = bos.getBytes

    val directBuf = ByteBuffer.allocateDirect(bytes.size)
    directBuf.put(bytes)
    directBuf.rewind()
    new ImmutableRoaringBitmap(directBuf)
  }

  private def backByHeapBuffer(bitmap: ImmutableRoaringBitmap): ImmutableRoaringBitmap = {
    val bos = new ByteOutputStream()
    val dos = new DataOutputStream(bos)
    bitmap.serialize(dos)

    val bytes = bos.getBytes

    val heapBuf = ByteBuffer.allocate(bytes.size)
    heapBuf.put(bytes)
    heapBuf.rewind()
    new ImmutableRoaringBitmap(heapBuf)
  }

  @Setup
  def setup(): Unit = {
    val bitPositionsArr = new ZipRealDataRetriever(dataset)
      .fetchBitPositions()
      .asScala
      .filter(_ != null)
      .toArray

    bitmaps = bitPositionsArr.map { bitPositions =>
      val raw = ImmutableRoaringBitmap.bitmapOf(bitPositions: _*)
      bufferType match {
        case "heap" => backByHeapBuffer(raw)
        case "direct" => backByDirectBuffer(raw)
      }
    }
  }


  @Benchmark
  @BenchmarkMode(Array(Mode.AverageTime))
  @OutputTimeUnit(TimeUnit.MICROSECONDS)
  def intersectAll(): ImmutableRoaringBitmap = {
    BufferFastAggregation.and(bitmaps: _*)
  }

}

object BackingBufferBenchmarkRunner {

  def main(args: Array[String]): Unit = {
    val opt: Options = new OptionsBuilder()
      .include(classOf[BackingBufferBenchmark].getSimpleName)
      .forks(1)
      .warmupIterations(10)
      .build
    new Runner(opt).run
  }

}
