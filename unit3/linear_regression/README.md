## Linear regression
Linear regression attempts to model the relationship between two variables by fitting a linear equation to observed data. One variable is considered to be an explanatory variable, and the other is considered to be a dependent variable. For example, a modeler might want to relate the weights of individuals to their heights using a linear regression model.

Required libraries
```scala
import org.apache.spark.ml.classification.LogisticRegression
import org.apache.spark.sql.SparkSession
import org.apache.spark.ml.feature.VectorAssembler
import org.apache.spark.ml.linalg.Vectors
import org.apache.spark.ml.Pipeline
```


Reducing errors with this settings
```scala
import org.apache.log4j._
Logger.getLogger("org").setLevel(Level.ERROR)
```

Importing Spark session
```scala
val spark = SparkSession.builder().getOrCreate()
```

Loading Data
```scala
val data  = spark.read.option("header","true").option("inferSchema", "true").format("csv").load("advertising.csv")
```

Visualizing schema
```scala
data.printSchema()
```


Print DataFrame
```scala
data.head(1)

val colnames = data.columns
val firstrow = data.head(1)(0)
println("\n")
println("Example data row")
for(ind <- Range(1, colnames.length)){
    println(colnames(ind))
    println(firstrow(ind))
    println("\n")
}
```

Adding the hour column
```scala
val timedata = data.withColumn("Hour",hour(data("Timestamp")))
```

Renaming column
```scala
val logregdata = timedata.select(data("Clicked on Ad").as("label"), $"Daily Time Spent on Site", $"Age", $"Area Income", $"Daily Internet Usage", $"Hour", $"Male")
```

Encode data with VectorAssembler
```scala
val assembler = (new VectorAssembler()
                  .setInputCols(Array("Daily Time Spent on Site", "Age","Area Income","Daily Internet Usage","Hour","Male"))
                  .setOutputCol("features"))
```


Split data into training and testing set
```scala
val Array(training, test) = logregdata.randomSplit(Array(0.7, 0.3), seed = 12345)
```


ML starts here
```scala
val lr = new LogisticRegression()
```

Run the data transformation
```scala
val pipeline = new Pipeline().setStages(Array(assembler, lr))
```

Fit the model
```scala
val model = pipeline.fit(training)
```

Get the predictions for the test set
```scala
val results = model.transform(test)
```

Confusion matrix
```scala
import org.apache.spark.mllib.evaluation.MulticlassMetrics

val predictionAndLabels = results.select($"prediction",$"label").as[(Double, Double)].rdd
val metrics = new MulticlassMetrics(predictionAndLabels)

println("Confusion matrix:")
println(metrics.confusionMatrix)
```

View Model accuracy
```scala
metrics.accuracy
```