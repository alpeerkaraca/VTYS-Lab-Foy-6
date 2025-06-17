#!/bin/bash

# ==============================================================================
# Performance Benchmark Script
# ==============================================================================
# This script sends concurrent requests to the Redis, Hazelcast, and MongoDB
# endpoints to measure response times using both curl/xargs and siege.
# ==============================================================================

# --- Configuration ---
BASE_URL="http://localhost:8080/nosql-lab"
STUDENT_ID="26000000"
OUTPUT_DIR="./assets/results"

# curl test configuration
CURL_REQUEST_COUNT=100
CURL_PARALLEL_JOBS=10

# siege test configuration
SIEGE_CONCURRENCY=10
SIEGE_REPETITIONS=100 # siege runs -r N times for EACH concurrent user (-c C), so 10x100 = 1000 total requests.
SIEGE_REPETITIONS_PER_USER=10


# --- Script Start ---

echo "Creating output directory at $OUTPUT_DIR..."
mkdir -p "$OUTPUT_DIR"
echo "Directory created."
echo ""
echo "----------------------------------------------------"
echo "          Running curl Benchmarks"
echo "----------------------------------------------------"
echo ""

# --- Redis curl Benchmark ---
echo "Starting Redis (curl) benchmark..."
REDIS_URL="$BASE_URL/rd/$STUDENT_ID"
REDIS_OUTPUT_FILE="$OUTPUT_DIR/redis-time.results"
time seq 1 $CURL_REQUEST_COUNT | xargs -n1 -P$CURL_PARALLEL_JOBS -I{} curl -s "$REDIS_URL" >> "$REDIS_OUTPUT_FILE"
echo "Redis (curl) benchmark finished. Results saved to $REDIS_OUTPUT_FILE"
echo ""

# --- Hazelcast curl Benchmark ---
echo "Starting Hazelcast (curl) benchmark..."
HZ_URL="$BASE_URL/hz/$STUDENT_ID"
HZ_OUTPUT_FILE="$OUTPUT_DIR/hz-time.results"
time seq 1 $CURL_REQUEST_COUNT | xargs -n1 -P$CURL_PARALLEL_JOBS -I{} curl -s "$HZ_URL" >> "$HZ_OUTPUT_FILE"
echo "Hazelcast (curl) benchmark finished. Results saved to $HZ_OUTPUT_FILE"
echo ""

# --- MongoDB curl Benchmark ---
echo "Starting MongoDB (curl) benchmark..."
MONGO_URL="$BASE_URL/mon/$STUDENT_ID"
MONGO_OUTPUT_FILE="$OUTPUT_DIR/mongodb-time.results"
time seq 1 $CURL_REQUEST_COUNT | xargs -n1 -P$CURL_PARALLEL_JOBS -I{} curl -s "$MONGO_URL" >> "$MONGO_OUTPUT_FILE"
echo "MongoDB (curl) benchmark finished. Results saved to $MONGO_OUTPUT_FILE"
echo ""

echo ""
echo "----------------------------------------------------"
echo "           Running siege Benchmarks"
echo "----------------------------------------------------"
echo ""

# --- Check if siege is installed ---
if ! command -v siege &> /dev/null
then
    echo "ERROR: siege could not be found."
    echo "Please install siege to run this part of the benchmark."
    echo "On macOS: brew install siege"
    echo "On Debian/Ubuntu: sudo apt-get install siege"
    exit 1
fi


# --- Redis Siege Benchmark ---
echo "Starting Redis (siege) benchmark..."
REDIS_SIEGE_OUTPUT_FILE="$OUTPUT_DIR/redis-siege.results"
siege -H "Accept: application/json" -c $SIEGE_CONCURRENCY -r $SIEGE_REPETITIONS_PER_USER "$REDIS_URL" > "$REDIS_SIEGE_OUTPUT_FILE" 2>&1
echo "Redis (siege) benchmark finished. Results saved to $REDIS_SIEGE_OUTPUT_FILE"
echo ""

# --- Hazelcast Siege Benchmark ---
echo "Starting Hazelcast (siege) benchmark..."
HZ_SIEGE_OUTPUT_FILE="$OUTPUT_DIR/hz-siege.results"
siege -H "Accept: application/json" -c $SIEGE_CONCURRENCY -r $SIEGE_REPETITIONS_PER_USER "$HZ_URL" > "$HZ_SIEGE_OUTPUT_FILE" 2>&1
echo "Hazelcast (siege) benchmark finished. Results saved to $HZ_SIEGE_OUTPUT_FILE"
echo ""

# --- MongoDB Siege Benchmark ---
echo "Starting MongoDB (siege) benchmark..."
MONGO_SIEGE_OUTPUT_FILE="$OUTPUT_DIR/mongodb-siege.results"
siege -H "Accept: application/json" -c $SIEGE_CONCURRENCY -r $SIEGE_REPETITIONS_PER_USER "$MONGO_URL" > "$MONGO_SIEGE_OUTPUT_FILE" 2>&1
echo "MongoDB (siege) benchmark finished. Results saved to $MONGO_SIEGE_OUTPUT_FILE"
echo ""


echo "All benchmarks are complete."
echo "Results are saved in the directory: $OUTPUT_DIR"