## Final practice unit 2

### Import the required libraries
```scala
import org.apache.spark.ml.feature.{StringIndexer,VectorIndexer,IndexToString,VectorAssembler}
import org.apache.spark.ml.classification.MultilayerPerceptronClassifier
import org.apache.spark.ml.Pipeline
import org.apache.spark.ml.evaluation.MulticlassClassificationEvaluator
import org.apache.spark.ml.classification.MultilayerPerceptronClassificationModel
import org.apache.log4j._
Logger.getLogger("org").setLevel(Level.ERROR)
```

### Spark session here
```scala
import org.apache.spark.sql.SparkSession
```

### Loading the data
```scala
val data = spark.
  read.option("header", "true").
  option("inferSchema","true")csv("iris.csv")
```

### Column names
```scala
print("\n\n")
data.columns
```

### Print schema
```scala
print("\n\n")
data.printSchema()
```

### Get additional infromation about the data
```scala
print("\n\n")
data.describe().show()
```

### Convertion from categorical to numerical
```scala
val assembler = new VectorAssembler().
  setInputCols(
    Array(
      "sepal_length",
      "sepal_width",
      "petal_length",
      "petal_width"
    )
  ).setOutputCol("features")

val features = assembler.transform(data)
features.show()


val labelIndexer = new StringIndexer().
  setInputCol("species").
  setOutputCol("indexedLabel").
  fit(features)

println(s"Found labels: ${labelIndexer.labels.mkString("[", ", ", "]")}")

val featureIndexer = new VectorIndexer().
  setInputCol("features").
  setOutputCol("indexedFeatures").
  setMaxCategories(4).
  fit(features)
```

### Split in training and testing data
```scala
val splits = features.randomSplit(Array(0.7, 0.3))
val trainingData = splits(0)
val testData = splits(1)
```

### Network layers
```scala
val layers = Array[Int](4, 5, 5, 3)
```

### The model
```scala
val trainer = new MultilayerPerceptronClassifier().
  setLayers(layers).
  setLabelCol("indexedLabel").
  setFeaturesCol("indexedFeatures").
  setBlockSize(128).
  setSeed(System.currentTimeMillis).
  setMaxIter(200)

val labelConverter = new IndexToString().
  setInputCol("prediction").
  setOutputCol("predictedLabel").
  setLabels(labelIndexer.labels)
```

### The pipeline
```scala
val pipeline = new Pipeline().
  setStages(Array(
    labelIndexer,
    featureIndexer,
    trainer,
    labelConverter
  ))
```

### Start the process
```scala
val model = pipeline.fit(trainingData)
```

### Get the final result
```scala
val predictions = model.transform(testData)
print("\n\n")
predictions.show(20)


val evaluator = new MulticlassClassificationEvaluator().
  setLabelCol("indexedLabel").
  setPredictionCol("prediction").
  setMetricName("accuracy")

val accuracy = evaluator.evaluate(predictions)
println("\n\nTest Error = " + (1.0 - accuracy))
```
