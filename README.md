# Helix Playground

A playground project for getting to grips with ZooKeeper. In particuluar, looking at how state management can be driven by resources being added to a process and having that propogate to ZooKeeper and notify the relevant nodes.

## Run It
1. `docker-compose up`
2. `quarkus dev`

## Endpoints
`GET /state` - get cluster state

`GET /processes`- get list of running processes 

`POST /processes/:processId/assign-partition` - assign partition
```json
{
  "partition": "string"
}
```

`POST /processes/:processId/assign-resource` - assign resource
```json
{
  "resourceName": "string",
  "partition": "string"
}
```
