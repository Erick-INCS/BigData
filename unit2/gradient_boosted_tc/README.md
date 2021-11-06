## Gradient boosted tree classifier
Gradient boosting is a machine learning technique for regression, classification and other tasks, which produces a prediction model in the form of an ensemble of weak prediction models, typically decision trees.
When a decision tree is the weak learner, the resulting algorithm is called gradient boosted trees, which usually outperforms random forest.
It builds the model in a stage-wise fashion like other boosting methods do, and it generalizes them by allowing optimization of an arbitrary differentiable loss function.

Gradient-Boosted Trees (GBTs) are ensembles of decision trees.
GBTs iteratively train decision trees in order to minimize a loss function. Like decision trees, GBTs handle categorical features, extend to the multiclass classification setting, do not require feature scaling, and are able to capture non-linearities and feature interactions.

`spark.mllib` supports GBTs for binary classification and for regression, using both continuous and categorical features.
`spark.mllib` implements GBTs using the existing decision tree implementation. Please see the decision tree guide for more information on trees.

Note: GBTs do not yet support multiclass classification. For multiclass problems, please use decision trees or Random Forests.

This exersice is based on the official documentation of Apache Spark 2.4 with Scala.

The implementation of this machine learning methos is available in the following file: [gbdtc.scala](gbtc.scala).