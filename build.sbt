name := "fileStream"

scalaVersion :=  "2.11.4"

libraryDependencies  ++= {
                          Seq(
                                "org.scalatest"       %%        "scalatest"              %       "2.2.2"          %    "test",
                                 "com.typesafe" % "config" % "1.2.1",
"ch.qos.logback" % "logback-classic" % "1.1.2"
                             )
                        }
parallelExecution in Test :=false
