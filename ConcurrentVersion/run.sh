for i in $(seq 1 10000)
do
 echo -e "\nRun n.º " $i
 java main.AirportConcurrentVersion
done
