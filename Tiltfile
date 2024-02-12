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

# --------------------------------- Protobuf --------------------------------- #

# Compile command for the protobuf files
proto_compile_cmd = 'buf generate proto'

# Local resource to compile the protobuf files
local_resource(
  'proto_compile',
  proto_compile_cmd,
  deps=['./proto'],
  ignore=[
    './**',
    '!./proto',
  ],
)

# --------------------------------- Rng --------------------------------- #

# Compile command for the Go application
rng_compile_cmd = 'CGO_ENABLED=0 GOOS=linux go build -o ../build/rng .'

# Local resource to compile the Go application
local_resource(
  'rng_compile',
  rng_compile_cmd,
  deps=['./rng', './generated'],
  dir='./rng',
  ignore=[
        'scripts/*',
        '.gitignore',
        'filter/*',
        'build/*',
      ],
)

# Docker build with restart for the Go application
docker_build_with_restart(
    'wcygan/counter-rng',
    '.',
    dockerfile='rng/Dockerfile',
    entrypoint='/app/rng',
    ignore=[
          'scripts/*',
          '.gitignore',
          'filter/*',
          'build/*',
        ],
    live_update=[
        sync('./', '/app'),
    ],
)

# ------------------------------ Filter ----------------------------- #

# Compile command for the Java application
filter_compile_cmd = './gradlew jar'

# Local resource to compile the Java application
local_resource(
  'filter_compile',
  filter_compile_cmd,
  deps=['./filter', './build.gradle', './gradlew'],
  dir='./filter',
  ignore=[
        'scripts/*',
        '.gitignore',
        'filter/.gradle*',
        'filter/build*',
        'build/*',
  ],
)

docker_build(
    'wcygan/counter-filter',
    'filter',
    dockerfile='filter/Dockerfile',
    entrypoint='java -jar pipeline.jar',
    ignore=[
          'scripts/*',
          '.gitignore',
          'filter/.gradle*',
          'filter/build*',
          'build/*',
    ],
)

# --------------------------------- Resources --------------------------------- #

k8s_yaml([
  'rng/deployment.yaml',
  'filter/deployment.yaml',
  'admin-dashboard.yaml',
])