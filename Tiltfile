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
        'beam-job/*',
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
          'beam-job/*',
          'build/*',
        ],
    live_update=[
        sync('./', '/app'),
    ],
)

# ------------------------------ Filter ----------------------------- #



# --------------------------------- Resources --------------------------------- #

k8s_yaml([
  'rng/deployment.yaml',
  'admin-dashboard.yaml',
])