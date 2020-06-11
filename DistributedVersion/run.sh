#!/bin/bash
echo "Starting Server and Clients"
#Servers
gnome-terminal -- java serverSide/servers/ServerArrivalLounge
gnome-terminal -- java serverSide/servers/ServerArrivalTerminalExit
gnome-terminal -- java serverSide/servers/ServerArrivalTermTransfQuay
gnome-terminal -- java serverSide/servers/ServerBaggageColPoint
gnome-terminal -- java serverSide/servers/ServerBaggageReclaimOffice
gnome-terminal -- java serverSide/servers/ServerDepartureTerminalEntrance
gnome-terminal -- java serverSide/servers/ServerDepartureTermTransfQuay
gnome-terminal -- java serverSide/servers/ServerGenReposInfo
gnome-terminal -- java serverSide/servers/ServerTemporaryStorageArea

#Clients
gnome-terminal -- java clientSide/clients/ClientPassenger
gnome-terminal -- java clientSide/clients/ClientPorter
gnome-terminal -- java clientSide/clients/ClientBusDriver
