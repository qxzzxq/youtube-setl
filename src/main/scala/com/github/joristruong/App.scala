package com.github.joristruong

import com.github.joristruong.entity.{Video, VideoCountry, VideoStats}
import com.github.joristruong.factory.{LatestStatsFactory, PopularityScoreFactory, VideoIngestionFactory}
import com.jcdecaux.setl.Setl

object App {

  def main(args: Array[String]): Unit = {

    val setl = Setl.builder()
      .withDefaultConfigLoader()
      .getOrCreate()

    // Register Repositories
    setl
      .setSparkRepository[Video]("videosCARepository", deliveryId = "videosCA", readCache = true)
      .setSparkRepository[Video]("videosDERepository", deliveryId = "videosDE", readCache = true)
      .setSparkRepository[Video]("videosFRRepository", deliveryId = "videosFR", readCache = true)
      .setSparkRepository[Video]("videosGBRepository", deliveryId = "videosGB", readCache = true)
      .setSparkRepository[Video]("videosINRepository", deliveryId = "videosIN", readCache = true)
      .setSparkRepository[Video]("videosJPRepository", deliveryId = "videosJP", readCache = true)
      .setSparkRepository[Video]("videosKRRepository", deliveryId = "videosKR", readCache = true)
      .setSparkRepository[Video]("videosMXRepository", deliveryId = "videosMX", readCache = true)
      .setSparkRepository[Video]("videosRURepository", deliveryId = "videosRU", readCache = true)
      .setSparkRepository[Video]("videosUSRepository", deliveryId = "videosUS", readCache = true)
      .setSparkRepository[VideoCountry]("videosRepository")
      .setSparkRepository[VideoStats]("videosStatsRepository")

    // Instantiate pipeline
    val pipeline = setl
      .newPipeline()
      .setInput(0.5, "viewsWeight")
      .setInput(0.25, "trendingDaysWeight")
      .setInput(0.1, "likesRatioWeight")
      .setInput(0.05, "commentsWeight")

    // Register Stages
    pipeline
      .addStage[VideoIngestionFactory]()
      .addStage[LatestStatsFactory]()
      .addStage[PopularityScoreFactory]()
      .describe()

    // Display Diagram
    pipeline.showDiagram()

    // Run the pipeline
    pipeline.run()

  }

}
