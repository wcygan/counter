load('ext://restart_process', 'docker_build_with_restart')
load('ext://helm_resource', 'helm_resource', 'helm_repo')
helm_repo('bitnami', 'https://charts.bitnami.com/bitnami')

# --------------------------------- Kafka --------------------------------- #

helm_resource(
  name='kafka',
  chart='bitnami/kafka',
  resource_deps=['bitnami'],
  flags=[
    '--values=./kafka/values.yaml',
    '--version=26.8.4',
  ]
)