package cn.scala

import org.apache.spark.sql.SparkSession

object test {

  def main(args: Array[String]): Unit = {
    val sc=SparkSession.builder().appName("ss").master("local[2]").getOrCreate()
    val r=sc.read.json("")
  }
}
