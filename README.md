# lagom-java-example
A sample Lagom application with Cinnamon metrics

## Running
Run with `helloServiceImpl/test:runMain play.core.server.ProdServerStart` to gather metrics

Call http://localhost:9000/api/hello/World and see metrics in console

## Deployment
From sbt, deploy to Minikube by running `rpDeploy minikube`
To generate Kubernetes YAML resource files, run the following command:

`rp generate-kubernetes-resources jmarin/helloserviceimpl:<VERSION> --generate-all`
