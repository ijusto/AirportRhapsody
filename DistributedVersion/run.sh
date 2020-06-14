#!/bin/bash
echo "Starting Server and Clients"
#Clean Ports
sudo fuser -k 22001/tcp
sudo fuser -k 22002/tcp
sudo fuser -k 22003/tcp
sudo fuser -k 22004/tcp
sudo fuser -k 22005/tcp
sudo fuser -k 22006/tcp
sudo fuser -k 22007/tcp
sudo fuser -k 22008/tcp
sudo fuser -k 22009/tcp
sudo fuser -k 22010/tcp
#Servers
gnome-terminal -x bash -c "java serverSide/servers/ServerGenReposInfo &>ServerGenReposInfo.txt"
gnome-terminal -x bash -c "java serverSide/servers/ServerBaggageColPoint &>ServerBaggageColPoint.txt"
gnome-terminal -x bash -c "java serverSide/servers/ServerArrivalLounge &>ServerArrivalLounge.txt"
gnome-terminal -x bash -c "java serverSide/servers/ServerArrivalTerminalExit &>ServerArrivalTerminalExit.txt"
gnome-terminal -x bash -c "java serverSide/servers/ServerArrivalTermTransfQuay &>ServerArrivalTermTransfQuay.txt"
gnome-terminal -x bash -c "java serverSide/servers/ServerBaggageReclaimOffice &>ServerBaggageReclaimOffice.txt"
gnome-terminal -x bash -c "java serverSide/servers/ServerDepartureTerminalEntrance &>ServerDepartureTerminalEntrance.txt"
gnome-terminal -x bash -c "java serverSide/servers/ServerDepartureTermTransfQuay &>ServerDepartureTermTransfQuay.txt"
gnome-terminal -x bash -c "java serverSide/servers/ServerTemporaryStorageArea &>ServerTemporaryStorageArea.txt"

