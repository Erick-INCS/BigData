## Naive Bayes
Naive Bayes classifiers are a family of simple probabilistic, multiclass classifiers based on applying Bayes’ theorem with strong (naive) independence assumptions between every pair of features.
Naive Bayes can be trained very efficiently. With a single pass over the training data, it computes the conditional probability distribution of each feature given each label.
For prediction, it applies Bayes’ theorem to compute the conditional probability distribution of each label given an observation.
MLlib supports both multinomial naive Bayes and Bernoulli naive Bayes.

## Example
Importing libraries
```scala
import org.apache.spark.ml.classification.NaiveBayes
import org.apache.spark.ml.evaluation.MulticlassClassificationEvaluator
```

Load the data stored in LIBSVM format as a DataFrame.
```scala
val data = spark.read.format("libsvm").load("sample_libsvm_data.txt")
```

Split the data into training and test sets (30% held out for testing)
```scala
val Array(trainingData, testData) = data.randomSplit(Array(0.7, 0.3), seed = 1234L)
```

Train a NaiveBayes model.
```scala
val model = new NaiveBayes().
  fit(trainingData)
```

Select example rows to display.
```scala
val predictions = model.transform(testData)
predictions.show()
```

Select (prediction, true label) and compute test error
```scala
val evaluator = new MulticlassClassificationEvaluator().
  setLabelCol("label").
  setPredictionCol("prediction").
  setMetricName("accuracy")

val accuracy = evaluator.evaluate(predictions)
println(s"Test set accuracy = $accuracy")
```
