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
gnome-terminal -- java serverSide/servers/ServerGenReposInfo
gnome-terminal -- java serverSide/servers/ServerBaggageColPoint
gnome-terminal -- java serverSide/servers/ServerArrivalLounge
gnome-terminal -- java serverSide/servers/ServerArrivalTerminalExit
gnome-terminal -- java serverSide/servers/ServerArrivalTermTransfQuay
gnome-terminal -- java serverSide/servers/ServerBaggageReclaimOffice
gnome-terminal -- java serverSide/servers/ServerDepartureTerminalEntrance
gnome-terminal -- java serverSide/servers/ServerDepartureTermTransfQuay
gnome-terminal -- java serverSide/servers/ServerTemporaryStorageArea
