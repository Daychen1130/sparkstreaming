package cn.scala


import org.apache.log4j.{Level, Logger}
import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD
import org.apache.spark.streaming.kafka010._
import org.apache.spark.streaming.kafka010.LocationStrategies.PreferConsistent
import org.apache.spark.streaming.kafka010.ConsumerStrategies.Subscribe
import org.apache.spark.streaming.{Seconds, StreamingContext}

object Hello {

  def main(args: Array[String]): Unit = {
    val Array(brokers,topics)=args
    val conf=new SparkConf().setAppName("KafkaCount")
    val ssc=new StreamingContext(conf,Seconds(5))

    Logger.getLogger("org").setLevel(Level.WARN)
    ssc.checkpoint("D://test")
    val kafkaParams = Map[String, Object](
      "bootstrap.servers"->"192.168.1.22:9092",
      "key.deserializer"-> "org.apache.kafka.common.serialization.StringDeserializer",
      "value.deserializer" ->"org.apache.kafka.common.serialization.StringDeserializer",
      "group.id"->"test",
      "auto.offset.reset"->"latest",
      "enable.auto.commit" -> (false:java.lang.Boolean)
    )
    val topicsSet=topics.split(",").toSet
//    val topics=Set("test")
    val msg=KafkaUtils.createDirectStream(ssc,PreferConsistent,Subscribe[String,String](topicsSet,kafkaParams))
    val rs=msg.map(_.value).flatMap(_.split(" ")).map(x=>(x,1)).reduceByKey(_+_)
    rs.print()

    ssc.start()
    ssc.awaitTermination()
  }
}
