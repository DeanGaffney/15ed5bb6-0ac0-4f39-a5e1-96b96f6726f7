# 15ed5bb6-0ac0-4f39-a5e1-96b96f6726f7
Sensor API

# TODO
- Get API responses formatted correctly
- Write tests
- Add redis cache
- Document everything in the README to this point
- Refactor all TODOs
- Add a Message Broker
- Document how to run everything
- Document iterative approach


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
    "fromDate": "2023-02-22",// must provide both fromDate and endDate, if range is not provided defaults to latest records
    "endDate": "2023-03-23"
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

