## Linear Support Vector Machine
A support vector machine constructs a hyperplane or set of hyperplanes in a high or infinite-dimensional space, which can be used for classification, regression, or other tasks.
Intuitively, a good separation is achieved by the hyperplane that has the largest distance to the nearest training-data points of any class (so-called functional margin), since in general the larger the margin the lower the generalization error of the classifier.
LinearSVC in Spark ML supports binary classification with linear SVM. Internally, it optimizes the Hinge Loss using OWLQN optimizer.

## Example
Import dependecies
```scala
import org.apache.spark.ml.classification.LinearSVC
```

Load training data
```scala
val training = spark.read.format("libsvm").load("sample_libsvm_data.txt")

val lsvc = new LinearSVC().
  setMaxIter(10).
  setRegParam(0.1)
```

Fit the model
```scala
val lsvcModel = lsvc.fit(training)
```

Print the coefficients and intercept for linear svc
```scala
println(s"Coefficients: ${lsvcModel.coefficients} Intercept: ${lsvcModel.intercept}")
```
