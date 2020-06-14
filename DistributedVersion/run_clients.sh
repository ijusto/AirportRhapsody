#Clients
gnome-terminal -x bash -c "java clientSide/clients/ClientPassenger |tee ClientPassenger.txt"
gnome-terminal -x bash -c "java clientSide/clients/ClientPorter |tee ClientPorter.txt"
gnome-terminal -x bash -c "java clientSide/clients/ClientBusDriver |tee ClientBusDriver.txt"
