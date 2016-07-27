package me.maciejb.buffper

import java.nio.ByteBuffer


object DirectBufferArrayChecker {

  def main(args: Array[String]): Unit = {
    val buf = ByteBuffer.allocateDirect(1024)
    println(s"Has array: ${buf.hasArray}")
  }

}
