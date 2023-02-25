#!/bin/bash

NUM_SENSORS="${1:-20}"
NUM_METRICS="${2:-100}"

TOTAL_SAMPLES="$((NUM_SENSORS * NUM_METRICS))"
SAMPLE_NUMBER=1
for i in $(seq 1 "$NUM_SENSORS"); do
  for j in $(seq 1 "$NUM_METRICS"); do
    echo "Samples Remaining: $SAMPLE_NUMBER/$TOTAL_SAMPLES"
    curl --silent -o /dev/null -X POST "http://localhost:8080/sensor/$i/metric" \
      -H "Content-Type: application/json" \
      -H "Accept: application/json" \
      --data '[ {"metricType": "TEMPERATURE", "value": 10}, {"metricType": "WIND_SPEED", "value": 30}, {"metricType": "HUMIDITY", "value": 50}]'
    SAMPLE_NUMBER=$((SAMPLE_NUMBER + 1))
  done
done
