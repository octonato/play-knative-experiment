
minikube start --memory=10000 --cpus=6 \
  --kubernetes-version=v1.14.0 \
  --vm-driver=hyperkit \
  --disk-size=30g \
  --extra-config=apiserver.enable-admission-plugins="LimitRanger,NamespaceExists,NamespaceLifecycle,ResourceQuota,ServiceAccount,DefaultStorageClass,MutatingAdmissionWebhook"

glooctl install knative  

MINIKUBE_IP=`minikube ip`

kubectl delete services knative-external-proxy -n gloo-system
kubectl expose deployment knative-external-proxy --type=LoadBalancer --name=knative-external-proxy -n gloo-system --external-ip=$MINIKUBE_IP


echo "this script will edit the config-domain from the knative-serving namespace"
echo "the goal is to add a DNS provider for the external minikube IP"
echo 
echo "copy the following line and add it under 'data:'"
echo
echo " ${MINIKUBE_IP}.xip.io: \"\""
echo 
echo "for example:"
echo "
apiVersion: v1
kind: ConfigMap
metadata:
  name: config-domain
  namespace: knative-serving
data:
  # xip.io is a 'magic' DNS provider, which resolves all DNS lookups for:
  # *.{ip}.xip.io to {ip}.
  ${MINIKUBE_IP}.xip.io: \"\"
"
echo 
echo "hit any key to start editing"
read 
kubectl edit cm config-domain --namespace knative-serving
