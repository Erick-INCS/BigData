## KMeans with apache spark
Import spark session
```scala
import org.apache.spark.sql.SparkSession
```

Reducing errors with:
```scala
import org.apache.log4j._
Logger.getLogger("org").setLevel(Level.ERROR)
```

Creating the spark instance
```scala
val spark = SparkSession.builder().getOrCreate()
```

Import KMeans
```scala
import org.apache.spark.ml.clustering.KMeans
```

Loading the dataset
```scala
val dataset = spark.read.option("header", "true").
  option("inferSchema", "true").
  csv("Wholesale customers data.csv")
```

Selecting Fresh, Milk, Grocey, Frozen, Detergents_Paper and Delicassen
```scala
val feature_data = dataset.select(
  "Fresh","Milk","Grocery",
  "Frozen","Detergents_Paper","Delicassen")
```

Import vector assambler and vector libraries
```scala
import org.apache.spark.ml.feature.VectorAssembler
import org.apache.spark.ml.linalg.Vectors
```

Creating a Vector Assembler object
```scala
val assembler = new VectorAssembler().
  setInputCols(Array(
    "Fresh", "Milk", "Grocery",
    "Frozen","Detergents_Paper",
    "Delicassen")).
  setOutputCol("features")
```

Transform feature_data with the vector assembler
```scala
val features = assembler.transform(feature_data)
print("\n\n")
features.show(5)
```
```
+-----+----+-------+------+----------------+----------+--------------------+
|Fresh|Milk|Grocery|Frozen|Detergents_Paper|Delicassen|            features|
+-----+----+-------+------+----------------+----------+--------------------+
|12669|9656|   7561|   214|            2674|      1338|[12669.0,9656.0,7...|
| 7057|9810|   9568|  1762|            3293|      1776|[7057.0,9810.0,95...|
| 6353|8808|   7684|  2405|            3516|      7844|[6353.0,8808.0,76...|
|13265|1196|   4221|  6404|             507|      1788|[13265.0,1196.0,4...|
|22615|5410|   7198|  3915|            1777|      5185|[22615.0,5410.0,7...|
+-----+----+-------+------+----------------+----------+--------------------+
only showing top 5 rows
```

Create the Kmeans model (with `k=3`)
```scala
val kmeans = new KMeans().setK(3)
val model = kmeans.fit(features)
```

Evaluating groups with "Within Set Sum of Squared Errors WSSSE"
```scala
val WSSSE = model.computeCost(features)
println(s"\n\nWithin Set Sum of Squared Errors = $WSSSE")

println("\n\nCluster Centers: ")
model.clusterCenters.foreach(println)
```
```
Within Set Sum of Squared Errors = 8.033326561848463E10

Cluster Centers: 
[8000.04,18511.420000000002,27573.9,1996.68,12407.36,2252.02]
[8298.79758308157,3817.1933534743202,5269.567975830816,2567.85498489426,1768.9969788519638,1136.0392749244713]
[36156.38983050847,6123.64406779661,6366.779661016949,6811.118644067797,1050.0169491525423,3090.0508474576272]
```