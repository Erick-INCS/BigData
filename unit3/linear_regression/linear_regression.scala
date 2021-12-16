// Required libraries
import org.apache.spark.ml.classification.LogisticRegression
import org.apache.spark.sql.SparkSession
import org.apache.spark.ml.feature.VectorAssembler
import org.apache.spark.ml.linalg.Vectors
import org.apache.spark.ml.Pipeline


// Reducing errors with this settings
import org.apache.log4j._
Logger.getLogger("org").setLevel(Level.ERROR)

// Importing Spark session
val spark = SparkSession.builder().getOrCreate()

// Loading Data
val data  = spark.read.option("header","true").option("inferSchema", "true").format("csv").load("advertising.csv")

// Visualizing schema
data.printSchema()


// Print DataFrame
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

// Adding the hour column
val timedata = data.withColumn("Hour",hour(data("Timestamp")))

// Renaming column
val logregdata = timedata.select(data("Clicked on Ad").as("label"), $"Daily Time Spent on Site", $"Age", $"Area Income", $"Daily Internet Usage", $"Hour", $"Male")

// Encode data with VectorAssembler
val assembler = (new VectorAssembler()
                  .setInputCols(Array("Daily Time Spent on Site", "Age","Area Income","Daily Internet Usage","Hour","Male"))
                  .setOutputCol("features"))


// Split data into training and testing set
val Array(training, test) = logregdata.randomSplit(Array(0.7, 0.3), seed = 12345)


// ML starts here
val lr = new LogisticRegression()

// Run the data transformation
val pipeline = new Pipeline().setStages(Array(assembler, lr))

// Fit the model
val model = pipeline.fit(training)

// Get the predictions for the test set
val results = model.transform(test)

// Confusion matrix
import org.apache.spark.mllib.evaluation.MulticlassMetrics

val predictionAndLabels = results.select($"prediction",$"label").as[(Double, Double)].rdd
val metrics = new MulticlassMetrics(predictionAndLabels)

println("Confusion matrix:")
println(metrics.confusionMatrix)

// View Model accuracy
metrics.accuracy

