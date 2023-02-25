# 15ed5bb6-0ac0-4f39-a5e1-96b96f6726f7
Sensor API

# TODO
- Database index
- Swagger
- Document how to run everything (docker, local, tests)
- Document/Diagrams for redis cache, read replicas(read heavy application) & message broker for write heavy application
- Scripts for generating/testing the API easily
- Document the scripts
- Refactor all TODOs
- Add redis cache
- Add a Message Broker
- Document iterative approach
- Document improvements & parts that are not completed

## Completed
[X] Create sensor metrics via API
[X] Query Metrics for one or more sensors
[X] Query Metrics and default to all sensor ids if none are provided
[X] Query Metrics for a specific statistic
[X] Query Metrics and default to AVG statistic if one is not provided
[X] Query Metrics between a date range
[] Query Metrics without a date range
    - Time constraints, did not get to come back and complete this.
[X] Unit & Integration Testing with coverage report
[] Documentation on running locally & with docker


## Improvements
- Modify query string builder to use prepared statements to prevent SQL injection
- Create a dedicated DateRange class for the date range validation
- Create dedicated response or serialization classes for constructing complex responses such as the metric query results.
- Add better validation for Enum types (needs custom validator)

## Extras
[X] Test coverage reports
[] Better Database Index
[] Swagger Documentation
[] Bash scripts for testing the APIs
[] Infrastructure Diagrams for different architectures


## Running Locally

## Running with Docker & Postgres

## Database Selection
- Explain the reasoning for selecting postgres here.

### Database Index Selection

## API

POST /sensor/{sensorId}/metric
Creates sensor metrics against the given sensor
REQUEST
```json
[
  {
    "metricType": "TEMPERATURE",
    "value": 6.5
  },
  {
    "metricType": "HUMIDITY",
    "value": 6.5
  },
  {
    "metricType": "WIND_SPEED",
    "value": 6.5
  }
]
```

RESPONSE
200 status code
```json
[
  {
    "id": 41,
    "sensorId": 1,
    "metric": {
      "metricType": "WIND_SPEED",
      "value": 3.5
    },
    "createdDate": "2023-02-23T13:17:46.528805"
  },
  {
    "id": 42,
    "sensorId": 1,
    "metric": {
      "metricType": "TEMPERATURE",
      "value": 2.45
    },
    "createdDate": "2023-02-23T13:17:46.528805"
  }
]
```

POST /sensor/metric/query
REQUEST
```json
{
    "sensorIds": [1, 4, 7], // if not provided, all will be retrieved
    "metrics": ["TEMPERATURE", "HUMIDITY"],// if not provided all metric types will be retrieved
    "statistics": "SUM" // if not provided, defaults to AVG
    "fromDate": "2023-02-22T00:00:00", // both fromDate and endDate must be provided
    "endDate": "2023-03-20T23:59:59"
}
```

RESPONSE
200 status code
```json
{
  "statistic": "SUM",
  "results": [
    {
      "sensorId": 1,
      "metrics": [
        {
          "metricType": "TEMPERATURE",
          "value": 50
        },
        {
          "metricType": "HUMIDITY",
          "value": 10  
        }
      ]
    }
    {
      "sensorId": 4,
      "metrics": [
        {
          "metricType": "TEMPERATURE",
          "value": 50
        },
        {
          "metricType": "HUMIDITY",
          "value": 10  
        }
      ]
    }
    {
      "sensorId": 7,
      "metrics": [
        {
          "metricType": "TEMPERATURE",
          "value": 50
        },
        {
          "metricType": "HUMIDITY",
          "value": 10  
        }
      ]
    }
  ]
}
```

