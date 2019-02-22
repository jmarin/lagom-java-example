# lagom-java-example
A sample Lagom application with custom Cinnamon metrics for Persistent Entity

## Running
Run with `helloServiceImpl/test:runMain play.core.server.ProdServerStart`. When run locally, metrics will be exposed at `http://localhost:9001/metrics`

## Endpoints
`curl http://localhost:9000/api/hello/Alice` (to see a greeting message)

`curl -H "Contenty-Type: application/json" -X POST -d '{"message": "Hi"}'  http://localhost:9000/api/hello/Alice` (to change the greeting message)


## Deployment
From sbt, deploy to Minikube by running `rpDeploy minikube`
To generate Kubernetes YAML resource files, run the following command:

`rp generate-kubernetes-resources jmarin/helloserviceimpl:<VERSION> --generate-all`
