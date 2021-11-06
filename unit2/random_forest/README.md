## Random forest
Random forests or random decision forests are an ensemble learning method for classification, regression and other tasks that operates by constructing a multitude of decision trees at training time.
For classification tasks, the output of the random forest is the class selected by most trees.
For regression tasks, the mean or average prediction of the individual trees is returned.
Random decision forests correct for decision trees habit of overfitting to their training set.
Random forests generally outperform decision trees, but their accuracy is lower than gradient boosted trees. However, data characteristics can affect their performance.

This exersice consist in implement a basic random forest in Spark with scala, show the predicted values and measure the quality of the result.

The implementation of this requirements was implemented in the following file:
[sf.scala](rf.scala).