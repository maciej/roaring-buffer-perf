# RoaringBitmap direct vs heap buffer performance

To run the benchmark execute:
```
sbt "jmh:runMain me.maciejb.buffper.BackingBufferBenchmarkRunner"
```

## Results

Running:
* JMH1.13
* JDK 1.8.0_101, VM 25.101-b13
* MacBookProc 15’ Late 2013, Intel(R) Core(TM) i7-4960HQ CPU @ 2.60GHz, 16GiB .
* OSX 10.11.6

```
Benchmark                            (bufferType)           (dataset)  Mode  Cnt    Score   Error  Units
BackingBufferBenchmark.intersectAll          heap  wikileaks-noquotes  avgt   20   13.648 ± 0.100  us/op
BackingBufferBenchmark.intersectAll        direct  wikileaks-noquotes  avgt   20    6.250 ± 0.047  us/op
BackingBufferBenchmark.intersectAll          heap     weather_sept_85  avgt   20  108.748 ± 1.671  us/op
BackingBufferBenchmark.intersectAll        direct     weather_sept_85  avgt   20   39.418 ± 0.373  us/op
BackingBufferBenchmark.intersectAll          heap       dimension_033  avgt   20  422.748 ± 5.789  us/op
BackingBufferBenchmark.intersectAll        direct       dimension_033  avgt   20  102.691 ± 0.859  us/op
BackingBufferBenchmark.intersectAll          heap       census-income  avgt   20   24.322 ± 0.277  us/op
BackingBufferBenchmark.intersectAll        direct       census-income  avgt   20    7.409 ± 0.072  us/op
BackingBufferBenchmark.intersectAll          heap        uscensus2000  avgt   20    2.980 ± 0.024  us/op
BackingBufferBenchmark.intersectAll        direct        uscensus2000  avgt   20    3.064 ± 0.028  us/op
```
