import org.apache.spark.sql.SparkSession

val spark = SparkSession.builder().getOrCreate()

val data  = spark.read.
    option("header","true").
    option("inferSchema", "true").
    csv("data/all_models.csv")

data.
    drop("Run").
    groupBy("Model").
    avg().
    write.
    csv("data/result_summary.csv")