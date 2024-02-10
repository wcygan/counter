# Counter

This application counts the amount of even numbers that are generated in 1-minute intervals 

<img src="documentation/counter-arch.png" alt="Counter Architecture" width="700"/>

## Prerequisites

1. A cluster to deploy to (e.g. [minikube](https://minikube.sigs.k8s.io/docs/start/))
2. [kubectl](https://kubernetes.io/docs/tasks/tools/install-kubectl/)
3. [helm](https://helm.sh/)
4. [tilt](https://tilt.dev/) (Optional: for local development)

## Quickstart

### Deploy locally using Tilt

Deploy using Tilt:

```sh
tilt up
```

Stop using Tilt:

```sh
tilt down
```
