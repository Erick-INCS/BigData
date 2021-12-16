// Import spark session
import org.apache.spark.sql.SparkSession

// Reducing errors with:
import org.apache.log4j._
Logger.getLogger("org").setLevel(Level.ERROR)

// Creating the spark instance
val spark = SparkSession.builder().getOrCreate()

// Import KMeans
import org.apache.spark.ml.clustering.KMeans

// Loading the dataset
val dataset = spark.read.option("header", "true").
  option("inferSchema", "true").
  csv("Wholesale customers data.csv")

// Selecting Fresh, Milk, Grocey, Frozen, Detergents_Paper and Delicassen
val feature_data = dataset.select(
  "Fresh","Milk","Grocery",
  "Frozen","Detergents_Paper","Delicassen")

// Import vector assambler and vector libraries
import org.apache.spark.ml.feature.VectorAssembler
import org.apache.spark.ml.linalg.Vectors

// Creating a Vector Assembler object
val assembler = new VectorAssembler().
  setInputCols(Array(
    "Fresh", "Milk", "Grocery",
    "Frozen","Detergents_Paper",
    "Delicassen")).
  setOutputCol("features")

// Transform feature_data with the vector assembler
val features = assembler.transform(feature_data)
print("\n\n")
features.show(5)

// Create the Kmeans model (with k=3)
val kmeans = new KMeans().setK(3)
val model = kmeans.fit(features)

// Evaluating groups with "Within Set Sum of Squared Errors WSSSE"
val WSSSE = model.computeCost(features)
println(s"\n\nWithin Set Sum of Squared Errors = $WSSSE")

println("\n\nCluster Centers: ")
model.clusterCenters.foreach(println)
