Pods=$(kubectl get pods |  grep -i -v terminating | awk '{print $1}');

for P in $Pods; do

	echo "=====> $P";
	kubectl logs $P;

done;