import org.apache.spark.ml.Pipeline
import org.apache.spark.ml.classification.DecisionTreeClassificationModel
import org.apache.spark.ml.classification.DecisionTreeClassifier
import org.apache.spark.ml.evaluation.MulticlassClassificationEvaluator
import org.apache.spark.ml.feature.{IndexToString, StringIndexer, VectorIndexer}

// Load the data stored in LIBSVM format as a DataFrame.
val data = spark.read.format("csv").
  option("header", "true").
  option("inferSchema", "true").
  load("iris.csv")


val vass = new VectorAssembler().
  setInputCols(Array("v0", "v1", "v2", "v3")).
  setOutputCol("v")

// Train a DecisionTree model.
val dt = new DecisionTreeClassifier().
  setLabelCol("target").
  setFeaturesCol("v")

val model = dt.fit(vass.transform(data))

// Make predictions.
val predictions = model.transform(vass.transform(data))

// Select example rows to display.
print("\n\nPredictions:\n")
predictions.select("prediction", "target", "probability").show(5)

// Select (prediction, true label) and compute test error.
val evaluator = new MulticlassClassificationEvaluator().
  setLabelCol("target").
  setPredictionCol("prediction").
  setMetricName("accuracy")

val accuracy = evaluator.evaluate(predictions)
println(s"\n\nTest Error = ${(1.0 - accuracy)}\n")

// val treeModel = model.stages(2).asInstanceOf[DecisionTreeClassificationModel]
// println(s"\n\nLearned classification tree model:\n ${treeModel.toDebugString}\n")
