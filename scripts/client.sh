kubectl run kafka-client --restart='Never' --image docker.io/bitnami/kafka:3.6.1-debian-11-r4 --namespace default --command -- sleep infinity

# Wait until the kafka-client pod is ready
kubectl wait --for=condition=ready pod/kafka-client --timeout=60s

echo "Starting a shell session in the kafka-client pod..."

kubectl exec --tty -i kafka-client --namespace default -- bash