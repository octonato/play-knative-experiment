# Play with Knative

This project demos how to deploy a embedded Play app in Knative.

You can deploy is in any Knative installation. For simplicity this demo will use Minikube with Gloo. 

Follow instructions to install Minikube binaries, but skip the Istio installation.
Only install the binary, don't create the cluster.
https://knative.dev/docs/install/knative-with-minikube/

Follow instructions to install Gloo binaries.
Only install the binary, don't configure anything.
https://knative.dev/docs/install/knative-with-gloo/

> **Note**: the DNS section is tricky when using with Minikube. The LoadBalancer won't get an external IP address. The workaround is to remove it and readd it explicitly using Minikube external IP.

With both binaries installed, run:

```shell
./configure-knative-gloo.sh
```

This script will create the minikube cluster and install knative and gloo. It will also do some hacks to configure the DNS correctly.

## Build the Play project

Make sure your docker client connects to minikube's docker.

```shell
eval $(minikube docker-env)
```

Publish the docker image to the Minikube register.

```shell
cd helloworld-scala-play
sbt docker:publishLocal
```

## Deploy the service

```shell
kubectl apply -f helloworld-scala-play.yaml
```

## Then find the service host

```shell
kubectl get ksvc helloworld-scala-play \
    --output=custom-columns=NAME:.metadata.name,URL:.status.url

# It will print something like this, the URL is what you're looking for.
# NAME                URL
# helloworld-scala    http://helloworld-scala-play.default.1.2.3.4.xip.io
```

## Call the service

```shell
curl http://helloworld-scala-play.default.1.2.3.4.xip.io/hello/knative
```

Note, after a while, Knative will scale down the service to zero instances. The service will be automatically re-deployed when a request arrives.
